package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zhanghongjie11
 * @date 2021/10/26 2:49 下午
 * @description
 */
public class PropertyUtil {

    public static final Properties properties = new Properties();

    // 加载properties文件
    public static void loadProperties(String filePath) {
        try (
                InputStream inputStream = Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("Can not find " + filePath);
            } else {
                properties.load(inputStream);
            }
        } catch (IOException e) {
            System.out.println("Can not load property " + filePath);
        }
    }

    public static String getProperties(String filePath, String key) {
        loadProperties(filePath);
        return properties.get(key).toString();
    }
}
