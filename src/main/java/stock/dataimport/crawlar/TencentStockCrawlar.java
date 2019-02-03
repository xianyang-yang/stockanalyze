package stock.dataimport.crawlar;

import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;
import stock.util.HttpUtils;
import stock.util.PriceUtils;
import stock.util.StockCodeUtils;

/**
 * Created by xianyang.yang on 2018/8/4 下午7:53.
 */
@Service
public class TencentStockCrawlar implements StockCrawlar {
    private static final Logger logger = LoggerFactory.getLogger(TencentStockCrawlar.class);

    @Override
    public StockBasicInfo fetchStockBasicInfo(String code) {
        String tencentCode = StockCodeUtils.covertToTencentCode(code);
        String url = "http://qt.gtimg.cn/q=${code}".replace("${code}", tencentCode);
        String result = HttpUtils.executeGet(url, Charset.forName("gbk"));
        String datas = StringUtils.substringBetween(result, "=\"", "\";");

        StockBasicInfo s = new StockBasicInfo();

        String[] fields = datas.split("~");
        if(fields==null || fields.length<47){
            logger.warn("no data found for stock code:{}",code);
            return null;
        }

        s.setCode(code);
        s.setName(fields[1]);
        s.setPeRadio(PriceUtils.toBigDecimal(fields[39]));
        s.setPbRadio(PriceUtils.toBigDecimal(fields[46]));
        s.setCirculationMarketValue(PriceUtils.toBigDecimal(fields[44]));
        s.setTotalValue(PriceUtils.toBigDecimal(fields[45]));

        return s;
    }

    @Override
    public List<StockDaily> listStockDailys(String code, String start, String end) {
        return null;
    }

    @Override
    public List<String> listAllCodes() {
        return null;
    }

    @Override
    public List<StockFinancialReport> listStockFinancialReports(String code, String start, String end) {
        return null;
    }
}
