package stock;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import stock.dataimport.StockDataDownloader;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.util.StockCodeUtils;

/**
 * create on 2018/7/31 下午9:31
 *
 * @author xianyang.yxy
 */

public class StockStartup {
    private static final Logger logger = LoggerFactory.getLogger(StockStartup.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
            "applicationContext.xml");

        StockDataDownloader s = context.getBean(StockDataDownloader.class);

        s.downloadStockBasicInfo(true);
        //s.downloadStockFinancialReports();
        //s.downloadStockDaily(true,"20180802","20190202");

        //updateStockCodes(s);

    }

    private static void updateStockCodes(StockDataDownloader s) throws IOException {
        List<String> allCodes = s.getAllCodes(true);

        long startTime=System.currentTimeMillis();
        File outputFile = new File(
            "/Users/xianyang.yang/IdeaProjects/stockanalyze/src/main/resources/data/stock-valid-code.txt");
        FileUtils.writeLines(outputFile,allCodes);

        logger.info("finish sava all stock:{},cost {}",allCodes.size(),(System.currentTimeMillis()-startTime));
    }
}
