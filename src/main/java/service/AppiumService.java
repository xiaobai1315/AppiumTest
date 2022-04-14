package service;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * @author zhanghongjie11
 * @date 2022/3/4 10:09 AM
 * @description
 */
public interface AppiumService {

    public AppiumDriverLocalService startAppiumServer(String ip, int port, DesiredCapabilities cap);

    public void stopServer();

    public AndroidDriver initAndroidDriver(int port, DesiredCapabilities caps);

    public DesiredCapabilities desiredCaps(String appName, String appPackage, String appActivity, String phoneUrl, String platformVersion);
}
