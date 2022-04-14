package utils;

import org.yaml.snakeyaml.Yaml;
import po.PageElement;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * @author zhanghongjie11
 * @date 2021/11/8 4:16 下午
 * @description
 */
public class FileUtil {

    private static String screenshotDirName;

    public static String getScreenshotDirName() {
        if (screenshotDirName == null) {
            DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
            screenshotDirName = dateFormat.format(new Date());
        }
        return screenshotDirName;
    }

    /**
     * 获取device.yaml文件信息
     */
    public static ArrayList<String> getPhonePortList() {
        Yaml yaml = new Yaml();
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream("device.yaml");
        Map<String, Object> map = yaml.loadAs(in, Map.class);

        ArrayList<String> devices = new ArrayList<>();
        map.forEach((key, value) -> {
            Object o = map.get(key);
            JSONArray objects = JSONObject.parseArray(JSON.toJSONString(o));
            for (Object object : objects) {
                devices.add(object.toString());
            }
        });

        return devices;
    }

    /**
     * AndroidWidget.yaml 获取page element
     * @param elementName 园区标签
     */
    private static PageElement getPageElement(String fileName, String elementName) {
        Yaml yaml = new Yaml();
        InputStream in = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        Map<String, Object> map = yaml.loadAs(in, Map.class);
        return JSON.parseObject(JSON.toJSONString(map.get(elementName)), PageElement.class);
    }

    /**
     * 获取系统组件
     */
    public static PageElement getAndroidWidget(String element) {
        return getPageElement("AndroidWidget.yaml", element);
    }

    /**
     * 获取系统组件
     */
    public static PageElement getLoginPageElement(String element) {
        return getPageElement("loginPageElement.yaml", element);
    }

    /**
     * 获取司秘达页面元素
     */
    public static PageElement getDriverPageElement(String element) {
        return getPageElement("driverPageElement.yaml", element);
    }

    /**
     * 获取星园页面元素
     */
    public static PageElement getParkPageElement(String element) {
        return getPageElement("parkPageElement.yaml", element);
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println(getParkPageElement("carCheckCommitButton"));
    }
}
