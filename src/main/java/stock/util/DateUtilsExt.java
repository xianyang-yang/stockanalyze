package stock.util;

import java.text.ParseException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * create on 2018/8/4 下午3:38
 *
 * @author xianyang.yxy
 */

public class DateUtilsExt {
    public static java.sql.Date parseToSqlDate(String str) {
        if (StringUtils.isEmpty(str) || "--".equals(str.trim())) {
            return null;
        }
        try {
            Date date = DateUtils.parseDate(str, "yyyy-MM-dd");
            return new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
