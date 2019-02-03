package stock.dao.interfaces;

import java.util.List;

import stock.model.StockFinancialReport;

public interface StockFinancialReportMapper {
    int deleteByPrimaryKey(Long id);

    int save(StockFinancialReport record);
    int batchSave(List<StockFinancialReport> records);

    StockFinancialReport selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockFinancialReport record);

    int updateByPrimaryKey(StockFinancialReport record);
}