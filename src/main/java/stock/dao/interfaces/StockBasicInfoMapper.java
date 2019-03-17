package stock.dao.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import stock.model.StockBasicInfo;

/**
 * 股票基本信息查询dao
 *
 * @author xianyang.yxy
 */
public interface StockBasicInfoMapper {
    /**
     * 保存股票信息
     *
     * @param record 股票信息
     * @return
     */
    int save(StockBasicInfo record);

    /**
     * 保存或更新股票信息(如果已存在)
     *
     * @param record 股票信息
     * @return
     */
    int saveOrUpdate(StockBasicInfo record);

    /**
     * 批量保存股票信息
     *
     * @param list
     * @return
     */
    int batchSave(List<StockBasicInfo> list);

    /**
     * 按股票代码查询股票信息
     *
     * @param code 股票代码
     * @return
     */
    StockBasicInfo findByCode(@Param(value = "code") String code);

    /**
     * 查询所有股票信息
     *
     * @param offset 分页偏移量
     * @param limit  每页记录数
     * @return
     */
    List<String> listAllStockCodes(@Param(value = "offset") int offset, @Param(value = "limit") int limit);

    /**
     * 按股票代码选择性更新股票信息
     *
     * @param record
     * @return
     */
    int updateSelectiveByCode(StockBasicInfo record);

    /**
     * 按股票代码删除股票信息
     *
     * @param code 股票代码
     * @return 删除成功数
     */
    int deleteByCode(@Param(value = "code") String code);
}