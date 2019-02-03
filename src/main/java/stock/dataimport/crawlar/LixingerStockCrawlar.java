package stock.dataimport.crawlar;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;
import stock.util.HttpUtils;

/**
 * Created by xianyang.yxy on 2018/8/19 下午8:22.
 *
 * @author xianyang.yxy
 */
@Service
public class LixingerStockCrawlar implements StockCrawlar {
    private static final Logger logger = LoggerFactory.getLogger(LixingerStockCrawlar.class);
    public static final String TOKEN = "c8f6ac4e-3cf6-48b7-9ac2-2cdf5814be9c";
    private static final String LIXINGER_OPEN_API_URL = "https://open.lixinger.com/api/a/stock/fs/bank";
    private static final String Q_PROFIT_STATEMENT_BEPS_TOTAL = "q.profitStatement.beps.t";
    private static final String Q_PROFIT_STATEMENT_BEPS_T_Y2Y = "q.profitStatement.beps.t_y2y";

    @Override
    public StockBasicInfo fetchStockBasicInfo(String code) {
        return null;
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
        JSONObject params = new JSONObject();
        params.put("stockCodes", toJsonArray(code));
        params.put("token", TOKEN);
        params.put("startDate", start);
        params.put("endDate", end);
        params.put("metrics", toJsonArray(Q_PROFIT_STATEMENT_BEPS_TOTAL, Q_PROFIT_STATEMENT_BEPS_T_Y2Y));

        JSONObject result = null;
        try {
            result = HttpUtils.executePost(LIXINGER_OPEN_API_URL, params);
        } catch (Exception e) {
            logger.error("error listStockFinancialReports while execute post:{}",code);
        }
        return parseToStockFinancialReports(result);
    }

    private List<StockFinancialReport> parseToStockFinancialReports(JSONObject result) {
        if (result == null) {
            return Collections.emptyList();
        }
        int code = result.getIntValue("code");
        if (code != 0) {
            logger.error("lixinger api result is not success:{}", result);
            return Collections.emptyList();
        }
        JSONArray data = result.getJSONArray("data");

        List<StockFinancialReport> list = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            JSONObject iterm = data.getJSONObject(i);
            StockFinancialReport stockFinancialReport = toStockFinancialReport(iterm);
            if (stockFinancialReport != null) {
                list.add(stockFinancialReport);
            }
        }
        return list;
    }

    private StockFinancialReport toStockFinancialReport(JSONObject iterm) {
        StockFinancialReport p = new StockFinancialReport();
        p.setCode(iterm.getString("stockCode"));
        p.setName(iterm.getString("stockCnName"));
        p.setReportType(iterm.getString("reportType"));
        Date reportDate = iterm.getSqlDate("reportDate");
        p.setReportDate(reportDate);
        p.setStandardDate(iterm.getSqlDate("standardDate"));
        JSONObject profitBeps = getProfitBeps(iterm);
        p.setProfitBepsTotal(profitBeps.getBigDecimal("t"));
        BigDecimal y2y = profitBeps.getBigDecimal("t_y2y");
        if (y2y != null) {
            p.setProfitBepsY2y(y2y.multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP));
        }
        return p;
    }

    private JSONObject getProfitBeps(JSONObject iterm) {
        return iterm.getJSONObject("q").getJSONObject("profitStatement").getJSONObject("beps");
    }

    private JSONArray toJsonArray(String... values) {
        JSONArray jsonArray = new JSONArray();
        for (String value : values) {
            jsonArray.add(value);
        }
        return jsonArray;
    }
}
