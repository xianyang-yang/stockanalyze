package stock.util;

/**
 * create on 2018/8/4 下午5:20
 *
 * @author xianyang.yxy
 */

public class StockCodeUtils {

    private static final String SHANGHAI_PREFIX = "sh";
    private static final String SHENZHEN_PREFIX = "sz";

    public static String covertToNeteaseCode(String code) {
        if (code.startsWith(SHANGHAI_PREFIX) || code.startsWith(SHENZHEN_PREFIX)) {
            code = code.substring(2, code.length());
        }
        if (code.startsWith("6")) {
            code = "0" + code;
        } else {
            code = "1" + code;
        }
        return code;
    }
    public static String getCodeNum(String code) {
        if (code.startsWith(SHANGHAI_PREFIX) || code.startsWith(SHENZHEN_PREFIX)) {
            code = code.substring(2, code.length());
        }
        return code;
    }

    public static String covertToTencentCode(String code) {
        if (code.startsWith(SHANGHAI_PREFIX) || code.startsWith(SHENZHEN_PREFIX)) {
            return code;
        }
        if (code.startsWith("6")) {
            code = SHANGHAI_PREFIX + code;
        } else {
            code = SHENZHEN_PREFIX + code;
        }
        return code;
    }
}
