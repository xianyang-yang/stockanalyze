package stock.util;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * create on 2018/8/4 下午3:29
 *
 * @author xianyang.yxy
 */

public class PriceUtils {
    private static final Logger logger = LoggerFactory.getLogger(PriceUtils.class);

    public static BigDecimal toBigDecimal(String value) {
        try {
            return isEmptyValue(value) ? null : new BigDecimal(value);
        } catch (Exception e) {
            logger.error("error toBigDecimal:{}", value, e);
            throw e;
        }
    }

    public static BigDecimal convertRMB(String value) {
        try {
            return isEmptyValue(value) ? null : new BigDecimal(value.replace("元", ""));
        } catch (Exception e) {
            logger.error("error convertRMB:{}", value, e);
            throw e;
        }
    }

    private static boolean isEmptyValue(String value) {
        return StringUtils.isEmpty(value) || "None".equals(value)
            || "--".equals(value) || "--元".equals(value);
    }
}
