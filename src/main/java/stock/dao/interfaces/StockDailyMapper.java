package stock.dao.interfaces;

import java.util.List;

import stock.model.StockDaily;

/**
 * create on 2018/7/31 下午9:26
 *
 * @author xianyang.yxy
 */
public interface StockDailyMapper {
    public int save(StockDaily stockDaily);
    public int batchSave(List<StockDaily> list);
}
