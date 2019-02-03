package stock.dataimport;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileSystemUtils;
import stock.dao.interfaces.StockBasicInfoMapper;
import stock.dao.interfaces.StockDailyMapper;
import stock.dao.interfaces.StockFinancialReportMapper;
import stock.dataimport.crawlar.StockCrawlar;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;
import stock.util.FileUtilsExt;
import stock.util.HttpUtils;
import stock.util.StockCodeUtils;

import static stock.util.PriceUtils.toBigDecimal;

/**
 * @author xianyang.yxy
 */
@Service
public class StockDataDownloader {
    private static final Logger logger = LoggerFactory.getLogger(StockDataDownloader.class);

    @Autowired
    private StockDailyMapper stockDailyMapper;

    @Resource(name = "smartStockCrawlar")
    private StockCrawlar stockCrawlar;

    @Autowired
    private StockBasicInfoMapper stockBasicInfoMapper;
    @Autowired
    private StockFinancialReportMapper stockFinancialReportMapper;

    public static final ExecutorService executorService = Executors.newFixedThreadPool(20);

    public List<String> getAllCodes(boolean forceUpdate) {

        if (!forceUpdate) {
            return FileUtilsExt.readLines("data/stock-valid-code.txt");
        }
        return stockCrawlar.listAllCodes();
    }

    public int downloadStockFinancialReports() throws InterruptedException {
        List<String> allCodes = getAllCodes(false);

        List<List<String>> partition = Lists.partition(allCodes, 200);
        AtomicInteger total=new AtomicInteger(0);
        CountDownLatch cdl = new CountDownLatch(partition.size());
        for (List<String> codes : partition) {
            executorService.execute(() -> {
                int num = downloadStockFinancialReports(codes);
                total.addAndGet(num);
                cdl.countDown();
            });
        }
        cdl.await();

        logger.info("finish all downloadStockFinancialReports:{}",total.get());
        return total.get();
    }

    public int downloadStockFinancialReports(List<String> allCodes) {
        int i = 0;
        for (String code : allCodes) {
            i++;
            List<StockFinancialReport> list = stockCrawlar.listStockFinancialReports(code, "2016-01-01",
                "2018-01-01");
            if(list.isEmpty()){
                logger.warn("no stock financial reports found:{}",code);
            }else {
                stockFinancialReportMapper.batchSave(list);
                logger.info("finish save stock financial info:{},{}/{}", code, i, allCodes.size());
            }
        }
        logger.info("finish save all stock basic info:{}->{}", allCodes.get(0), allCodes.get(allCodes.size() - 1));
        return i;

    }

    public int downloadStockBasicInfo(boolean flushToDb) {
        List<String> allCodes = getAllCodes(false);
        int i = 0;
        for (String code : allCodes) {
            i++;
            StockBasicInfo stockBasicInfo = stockCrawlar.fetchStockBasicInfo(code);
            if(stockBasicInfo==null){
                continue;
            }
            System.out.println(stockBasicInfo);

            if (flushToDb) {
                stockBasicInfoMapper.saveOrUpdate(stockBasicInfo);
            }
            logger.info("finish save stock basic info:{},{}/{}", stockBasicInfo, i, allCodes.size());
        }
        logger.info("finish save all stock basic info");
        return i;
    }

    public int downloadStockDaily(boolean flushToDb, String start, String end) {
        int total = 0;
        List<String> allCodes = getAllCodes(false);
        int i = 0;
        for (String code : allCodes) {
            i++;
            List<StockDaily> stockDailies = stockCrawlar.listStockDailys(code, start, end);
            if(CollectionUtils.isEmpty(stockDailies)){
                logger.warn("no stock dailys records found:{}, between {} to {}", code, start, end);
                continue;
            }

            if (flushToDb) {
                int num = stockDailyMapper.batchSave(stockDailies);
                total += num;
                logger.info("finish save stock daily:{},{},{}/{}", code, stockDailies.size(), i, allCodes.size());
            }
        }

        return total;
    }

}
