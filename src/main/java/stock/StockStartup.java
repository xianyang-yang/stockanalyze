package stock;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import stock.dataimport.StockDataDownloader;

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

        s.downloadStockBasicInfo(false);
        s.downloadStockDaily(true,"20190215","20190317");
        //s.downloadStockFinancialReports();
    }
}
