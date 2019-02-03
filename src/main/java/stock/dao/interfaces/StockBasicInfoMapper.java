package stock.dao.interfaces;

import java.util.List;

import stock.model.StockBasicInfo;

public interface StockBasicInfoMapper {
    int deleteByPrimaryKey(Long id);

    int save(StockBasicInfo record);
    int saveOrUpdate(StockBasicInfo record);

    int batchSave(List<StockBasicInfo> list);

    StockBasicInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(StockBasicInfo record);

    int updateByPrimaryKey(StockBasicInfo record);
}