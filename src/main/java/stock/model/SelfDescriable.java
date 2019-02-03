package stock.model;

import com.alibaba.fastjson.JSON;

/**
 * create on 2018/8/4 下午3:48
 *
 * @author xianyang.yxy
 */

public class SelfDescriable {
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
