package stock.constants;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

/**
 * a股股票类型，包含上证A股/深证A股/创业板
 *
 * @author xianyang.yxy
 */
public enum StockCodeTypeEnum {
    /**
     * 上证A股
     */
    SH_STOCK("600","601"),
    /**
     * 深证A股
     */
    SZ_STOCK("000","001","002"),
    /**
     * 创业板
     */
    SECOND_BOARD_STOCK("300"),
    /**
     * a股，包含上证A股/深证A股/创业板
     */
    A_STOCK(SH_STOCK,SZ_STOCK,SECOND_BOARD_STOCK)
    ;

    private final Set<String> codePrefixs;

    StockCodeTypeEnum(String... codePrefixs) {
        this.codePrefixs = Sets.newHashSet(codePrefixs);
    }

    StockCodeTypeEnum(StockCodeTypeEnum... stockCodeTypeEnums) {
        Set<String> finalCodesPrefixs = new HashSet<>();
        for (StockCodeTypeEnum stockCodeType : stockCodeTypeEnums) {
            finalCodesPrefixs.addAll(stockCodeType.codePrefixs);
        }
        this.codePrefixs = finalCodesPrefixs;
    }

    public boolean match(String stockCode) {
        if (stockCode == null || stockCode.length() < 3) {
            return false;
        }

        return codePrefixs.contains(stockCode.substring(0, 3));
    }
}
