package stock.dataimport.crawlar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;

/**
 * Created by xianyang.yang on 2018/8/4 下午8:43.
 */
@Service
public class SmartStockCrawlar implements StockCrawlar {
    @Autowired
    private NeteaseStockCrawlar neteaseStockCrawlar;
    @Autowired
    private TencentStockCrawlar tencentStockCrawlar;
    @Autowired
    private LixingerStockCrawlar lixingerStockCrawlar;

    @Override
    public StockBasicInfo fetchStockBasicInfo(String code) {
        StockBasicInfo s1 = tencentStockCrawlar.fetchStockBasicInfo(code);
        if(s1==null){
            return null;
        }
        StockBasicInfo s2 = neteaseStockCrawlar.fetchStockBasicInfo(code);

        s1.setListDate(s2.getListDate());
        s1.setIssuePrice(s2.getIssuePrice());
        return s1;
    }

    @Override
    public List<StockDaily> listStockDailys(String code, String start, String end) {
        return neteaseStockCrawlar.listStockDailys(code, start, end);
    }

    @Override
    public List<String> listAllCodes() {
        return neteaseStockCrawlar.listAllCodes();
    }

    @Override
    public List<StockFinancialReport> listStockFinancialReports(String code, String start, String end) {
        return lixingerStockCrawlar.listStockFinancialReports(code, start, end);
    }
}
