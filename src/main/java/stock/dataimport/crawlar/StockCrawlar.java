package stock.dataimport.crawlar;

import java.util.Collections;
import java.util.List;

import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;

/**
 * Created by xianyang.yang on 2018/8/4 下午7:51.
 */
public interface StockCrawlar {
    StockBasicInfo fetchStockBasicInfo(String code);

    List<StockDaily> listStockDailys(String code, String start, String end);

    /**
     * 获取所有股票股票代码
     * @return
     */
    List<String> listAllCodes();

    List<StockFinancialReport> listStockFinancialReports(String code, String start, String end);

}
