package stock.model;

import java.math.BigDecimal;
import java.sql.Date;

public class StockFinancialReport extends SelfDescriable {
    private Long id;

    private String code;

    private String name;

    private String reportType;

    private Date reportDate;

    private Date standardDate;

    private BigDecimal profitBepsTotal;

    private BigDecimal profitBepsY2y;

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

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType == null ? null : reportType.trim();
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public Date getStandardDate() {
        return standardDate;
    }

    public void setStandardDate(Date standardDate) {
        this.standardDate = standardDate;
    }

    public BigDecimal getProfitBepsTotal() {
        return profitBepsTotal;
    }

    public void setProfitBepsTotal(BigDecimal profitBepsTotal) {
        this.profitBepsTotal = profitBepsTotal;
    }

    public BigDecimal getProfitBepsY2y() {
        return profitBepsY2y;
    }

    public void setProfitBepsY2y(BigDecimal profitBepsY2y) {
        this.profitBepsY2y = profitBepsY2y;
    }
}