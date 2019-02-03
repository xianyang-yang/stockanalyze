package stock.model;

import java.math.BigDecimal;
import java.sql.Date;

public class StockBasicInfo extends SelfDescriable {
    private Long id;

    private String code;

    private String name;

    private BigDecimal peRadio;

    private BigDecimal pbRadio;

    private String industry;

    private Date listDate;

    private BigDecimal issuePrice;

    private BigDecimal firstOpenPrice;

    private BigDecimal totalValue;

    private BigDecimal circulationMarketValue;

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
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public BigDecimal getPeRadio() {
        return peRadio;
    }

    public void setPeRadio(BigDecimal peRadio) {
        this.peRadio = peRadio;
    }

    public BigDecimal getPbRadio() {
        return pbRadio;
    }

    public void setPbRadio(BigDecimal pbRadio) {
        this.pbRadio = pbRadio;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public Date getListDate() {
        return listDate;
    }

    public void setListDate(Date listDate) {
        this.listDate = listDate;
    }

    public BigDecimal getIssuePrice() {
        return issuePrice;
    }

    public void setIssuePrice(BigDecimal issuePrice) {
        this.issuePrice = issuePrice;
    }

    public BigDecimal getFirstOpenPrice() {
        return firstOpenPrice;
    }

    public void setFirstOpenPrice(BigDecimal firstOpenPrice) {
        this.firstOpenPrice = firstOpenPrice;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getCirculationMarketValue() {
        return circulationMarketValue;
    }

    public void setCirculationMarketValue(BigDecimal circulationMarketValue) {
        this.circulationMarketValue = circulationMarketValue;
    }
}