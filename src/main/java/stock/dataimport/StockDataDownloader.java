package stock.dataimport;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import stock.constants.StockCodeTypeEnum;
import stock.dao.interfaces.StockBasicInfoMapper;
import stock.dao.interfaces.StockDailyMapper;
import stock.dao.interfaces.StockFinancialReportMapper;
import stock.dataimport.crawlar.StockCrawlar;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;


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

    /**
     * 下载指定类型的股票代码
     * @param stockCodeType 股票类型
     * @return
     */
    public List<String> getAllCodes(StockCodeTypeEnum stockCodeType) {
        List<String> allCodes = stockCrawlar.listAllCodes();
        if (stockCodeType == null) {
            return allCodes;
        }

        return allCodes.stream().filter(code -> stockCodeType.match(code)).collect(Collectors.toList());
    }

    /**
     * 下载所有A股股票财报信息，使用多线程加快速度
     * @return
     * @throws InterruptedException
     */
    public int downloadStockFinancialReports() throws InterruptedException {
        List<String> allCodes = getAllCodes(StockCodeTypeEnum.A_STOCK);

        List<List<String>> partition = Lists.partition(allCodes, 200);
        AtomicInteger total = new AtomicInteger(0);
        CountDownLatch cdl = new CountDownLatch(partition.size());
        for (List<String> codes : partition) {
            executorService.execute(() -> {
                int num = downloadStockFinancialReports(codes);
                total.addAndGet(num);
                cdl.countDown();
            });
        }
        cdl.await();

        logger.info("finish all downloadStockFinancialReports:{}", total.get());
        return total.get();
    }

    /**
     * 下载股票财报信息
     * @param allCodes 股票代码
     * @return
     */
    public int downloadStockFinancialReports(List<String> allCodes) {
        int i = 0;
        for (String code : allCodes) {
            i++;
            List<StockFinancialReport> list = stockCrawlar.listStockFinancialReports(code, "2016-01-01",
                "2018-01-01");
            if (list.isEmpty()) {
                logger.warn("no stock financial reports found:{}", code);
            } else {
                stockFinancialReportMapper.batchSave(list);
                logger.info("finish save stock financial info:{},{}/{}", code, i, allCodes.size());
            }
        }
        logger.info("finish save all stock basic info:{}->{}", allCodes.get(0), allCodes.get(allCodes.size() - 1));
        return i;

    }

    /**
     * 下载股票基本信息，包括股票代码,名称,上市时间,市盈率,市值等
     * @param updateWhenExist 股票信息存在时是否更新
     * @return 新增/更新的数量
     */
    public int downloadStockBasicInfo(boolean updateWhenExist) {
        //已下载的股票编码
        List<String> existStockCodes = stockBasicInfoMapper.listAllStockCodes(0, 10000);

        //最新的股票代码
        List<String> latestStockCodes = getAllCodes(StockCodeTypeEnum.A_STOCK);

        int total = 0;
        for (String code : latestStockCodes) {
            if (existStockCodes.contains(code) && !updateWhenExist) {
                continue;
            }

            StockBasicInfo stockBasicInfo = stockCrawlar.fetchStockBasicInfo(code);
            if (stockBasicInfo == null) {
                logger.warn("stock code is not exist:{}", code);
                continue;
            }

            stockBasicInfoMapper.saveOrUpdate(stockBasicInfo);
            total++;
            //logger.info("finish save stock basic info:{},{}/{}", stockBasicInfo, total, allCodes.size());
        }
        logger.info("finish saveOrUpdate all stock basic info,total num is:{},latestStockCodes size is:{}", total,
            latestStockCodes.size());
        return total;
    }

    public int downloadStockDaily(boolean flushToDb, String start, String end) {
        int total = 0;
        List<String> allCodes = getAllCodes(StockCodeTypeEnum.A_STOCK);
        int i = 0;
        for (String code : allCodes) {
            i++;
            List<StockDaily> stockDailies = stockCrawlar.listStockDailys(code, start, end);
            if (CollectionUtils.isEmpty(stockDailies)) {
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
