package stock.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * create on 2018/8/1 下午10:50
 *
 * @author xianyang.yxy
 */

public class FileUtilsExt {
    public static List<String> readLines(String classPath) {
        URL url = FileUtilsExt.class.getClassLoader().getResource(classPath);
        File postDataFile = new File(url.getFile());
        try {
            return FileUtils.readLines(postDataFile, "utf-8");
        } catch (IOException e) {
            throw new RuntimeException("error readFile:" + classPath, e);
        }
    }
}
