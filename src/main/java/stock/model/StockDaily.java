package stock.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 股票日交易信息
 * create on 2018/7/31 下午9:11
 *
 * @author xianyang.yxy
 */

public class StockDaily extends SelfDescriable {
    private Long id;
    private String code;
    private String name;
    private Date tdate;
    private BigDecimal openPrice;
    private BigDecimal closePrice;
    private BigDecimal highPrice;
    private BigDecimal lowPrice;
    private BigDecimal lastClosePrice;
    private BigDecimal chageRate;
    private BigDecimal changeAmount;
    private BigDecimal turnoverRate;
    private Long tradingVolume;
    private Long turnover;
    private Long totalValue;
    private Long circulationMarketValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getTdate() {
        return tdate;
    }

    public void setTdate(Date tdate) {
        this.tdate = tdate;
    }

    public BigDecimal getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(BigDecimal openPrice) {
        this.openPrice = openPrice;
    }

    public BigDecimal getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(BigDecimal closePrice) {
        this.closePrice = closePrice;
    }

    public BigDecimal getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(BigDecimal highPrice) {
        this.highPrice = highPrice;
    }

    public BigDecimal getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(BigDecimal lowPrice) {
        this.lowPrice = lowPrice;
    }

    public BigDecimal getLastClosePrice() {
        return lastClosePrice;
    }

    public void setLastClosePrice(BigDecimal lastClosePrice) {
        this.lastClosePrice = lastClosePrice;
    }

    public BigDecimal getChageRate() {
        return chageRate;
    }

    public void setChageRate(BigDecimal chageRate) {
        this.chageRate = chageRate;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getTurnoverRate() {
        return turnoverRate;
    }

    public void setTurnoverRate(BigDecimal turnoverRate) {
        this.turnoverRate = turnoverRate;
    }

    public Long getTradingVolume() {
        return tradingVolume;
    }

    public void setTradingVolume(Long tradingVolume) {
        this.tradingVolume = tradingVolume;
    }

    public Long getTurnover() {
        return turnover;
    }

    public void setTurnover(Long turnover) {
        this.turnover = turnover;
    }

    public Long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Long totalValue) {
        this.totalValue = totalValue;
    }

    public Long getCirculationMarketValue() {
        return circulationMarketValue;
    }

    public void setCirculationMarketValue(Long circulationMarketValue) {
        this.circulationMarketValue = circulationMarketValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
