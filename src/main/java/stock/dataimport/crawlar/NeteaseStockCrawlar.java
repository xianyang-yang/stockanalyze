package stock.dataimport.crawlar;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableInt;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import stock.model.StockBasicInfo;
import stock.model.StockDaily;
import stock.model.StockFinancialReport;
import stock.util.DateUtilsExt;
import stock.util.HttpUtils;
import stock.util.StockCodeUtils;

import static stock.util.PriceUtils.convertRMB;
import static stock.util.PriceUtils.toBigDecimal;

/**
 * create on 2018/8/4 下午2:30
 *
 * @author xianyang.yxy
 */
@Service
public class NeteaseStockCrawlar implements StockCrawlar {
    private static final Logger logger = LoggerFactory.getLogger(NeteaseStockCrawlar.class);

    private static final String STOCK_DAILY_DOWNLOAD_URL =
        "http://quotes.money.163.com/service/chddata.html?code=${code}&start=${start}&end=${end}&fields=TCLOSE;"
            + "HIGH;LOW;TOPEN;LCLOSE;CHG;PCHG;TURNOVER;VOTURNOVER;VATURNOVER;TCAP;MCAP";
    private static final String STOCK_CODE_PREFIX = "<a target=\"_blank\" href=\"http://quote.eastmoney.com/";
    private static final String STOCK_CODE_LIST_URL = "http://quote.eastmoney.com/stock_list.html";

    public static void main(String[] args) {

        StockBasicInfo stockBasicInfo = new NeteaseStockCrawlar().fetchStockBasicInfo("002146");
        System.out.println(stockBasicInfo);
        System.out.println(stockBasicInfo.getListDate());
    }

    @Override
    public StockBasicInfo fetchStockBasicInfo(String code) {
        StockBasicInfo s = new StockBasicInfo();
        s.setCode(code);

        String detailUrl = "http://quotes.money.163.com/f10/gszl_${code}.html".replace("${code}", code);
        String[] results = getTargetValueBy(detailUrl, buildParams("上市日期", "发行价格"), "<td>","</td>");
        s.setListDate(DateUtilsExt.parseToSqlDate(results[0]));
        s.setIssuePrice(convertRMB(results[1]));
        if (s.getListDate() == null) {
            logger.warn("get list date again:{}", code);
            String convertCode = StockCodeUtils.covertToNeteaseCode(code);
            String url = "http://quotes.money.163.com/${code}.html".replace("${code}", convertCode);

            String date = getTargetValueBy(url, "<p>首次上市", "</p>");
            s.setListDate(DateUtilsExt.parseToSqlDate(date));
        }
        return s;
    }

    @Override
    public List<StockDaily> listStockDailys(String code, String start, String end) {

        code = StockCodeUtils.covertToNeteaseCode(code);
        String url = STOCK_DAILY_DOWNLOAD_URL.replace("${code}", code)
            .replace("${start}", start)
            .replace("${end}", end);
        List<StockDaily> list = new ArrayList<>();
        HttpUtils.executeGet(url, Charset.forName("gbk"), (line, lineNum) -> {
            if (lineNum > 1) {
                String[] values = line.split(",");
                StockDaily s = toStockDaily(values);
                if (s != null) {
                    list.add(s);
                }
            }
            //System.out.println(line);
        });
        return list;
    }

    @Override
    public List<String> listAllCodes() {
        List<String> list = new ArrayList<>();
        HttpUtils.executeGet(STOCK_CODE_LIST_URL, Charset.forName("gbk"), (line, num) -> {
            if (!StringUtils.isEmpty(line)) {
                if (line.contains(STOCK_CODE_PREFIX) && line.endsWith("</a></li>")) {

                    String code = StringUtils.substringBetween(line, STOCK_CODE_PREFIX, ".html");
                    if (StringUtils.isNotEmpty(code)) {
                        code=StockCodeUtils.getCodeNum(code);
                        list.add(code);
                    } else {
                        logger.info("error fetch stock code:{}", line);
                    }
                }
            }
        });
        return list;
    }

    @Override
    public List<StockFinancialReport> listStockFinancialReports(String code, String start, String end) {
        return null;
    }

    private String[] buildParams(String... keys) {
        String[] result = new String[keys.length];
        int i = 0;
        for (String key : keys) {
            result[i] = "<td class=\"td_label\">" + key + "</td>";
            i++;
        }
        return result;
    }

    private StockDaily toStockDaily(String[] values) {
        if (values.length != 15) {
            logger.error("stock data is invalid:{}", Arrays.toString(values));
            return null;
        }
        try {
            StockDaily s = new StockDaily();
            Date date = DateUtils.parseDate(values[0], new String[] {"yyyy-MM-dd"});
            s.setTdate(date);
            String code = values[1];
            code = code.replace("'", "");
            s.setCode(code);
            s.setName(values[2]);
            s.setClosePrice(toBigDecimal(values[3]));
            s.setHighPrice(toBigDecimal(values[4]));
            s.setLowPrice(toBigDecimal(values[5]));
            s.setOpenPrice(toBigDecimal(values[6]));
            s.setLastClosePrice(toBigDecimal(values[7]));
            s.setChangeAmount(toBigDecimal(values[8]));
            s.setChageRate(toBigDecimal(values[9]));
            s.setTurnoverRate(toBigDecimal(values[10]));
            s.setTradingVolume(toBigDecimal(values[11]).longValue());
            s.setTurnover(toBigDecimal(values[12]).longValue());
            s.setTotalValue(toBigDecimal(values[13]).longValue());
            s.setCirculationMarketValue(toBigDecimal(values[14]).longValue());
            return s;
        } catch (Exception e) {
            logger.error("error parse to stock daily object:{}", Arrays.toString(values), e);
            return null;
        }
    }

    private String getTargetValueBy(String url, String lastLineKeyword, String cleanStr) {
        String[] results = getTargetValueBy(url, new String[] {lastLineKeyword}, cleanStr);
        return results[0];
    }

    private String[] getTargetValueBy(String url, String[] lastLineKeywords, String... cleanStrs) {
        MutableObject<String> lastLineObj = new MutableObject<>();
        String[] result = new String[lastLineKeywords.length];
        MutableInt curIndex = new MutableInt(0);
        HttpUtils.executeGet(url, Charset.forName("utf8"), (line, lineNum) -> {
            String lastLine = lastLineObj.getValue();
            Integer i = curIndex.getValue();
            if (lastLine != null && i < lastLineKeywords.length) {
                if (lastLine.contains(lastLineKeywords[i])) {
                    for (String cleanStr : cleanStrs) {
                        line=line.replace(cleanStr, "");
                    }
                    result[i] = line.trim();
                    curIndex.increment();
                }
            }
            lastLineObj.setValue(line);
        });
        return result;
    }
}
