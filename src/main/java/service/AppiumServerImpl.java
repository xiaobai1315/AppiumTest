package service;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import utils.AdbUtil;

import java.net.URL;
import java.util.concurrent.TimeUnit;


import static page.BasePage.sleep;

/**
 * @author zhanghongjie11
 * @date 2022/3/2 3:23 PM
 * @description
 */
@Slf4j
public class AppiumServerImpl implements AppiumService{

    private static AppiumDriverLocalService service = null;

    /**
     * 开启appium server
     */
    public AppiumDriverLocalService startAppiumServer(String ip, int port, DesiredCapabilities cap) {

        AppiumServiceBuilder builder = new AppiumServiceBuilder();
        builder.withIPAddress(ip);
        builder.usingPort(port);
        builder.withCapabilities(cap);
        builder.withArgument(GeneralServerFlag.SESSION_OVERRIDE);
        builder.withArgument(GeneralServerFlag.LOG_LEVEL,"error");

        try {
            service = AppiumDriverLocalService.buildService(builder);
//            service = AppiumDriverLocalService.buildDefaultService();
            service.start();

            sleep(5);

            if (StringUtils.isEmpty(AdbUtil.getPortInfo(port))) {
                throw new Exception();
            }

            log.info("appium server " + ip + ":" + port + " 启动成功");

        }catch (Exception e) {
            stopServer();
            throw new BaseBusinessException("appium server " + ip + ":" + port + " 启动失败");
        }

        return service;
    }

    /**
     * 停止 appium server
     * 端口连接的远程设备
     */
    public void stopServer() {

        if (service == null) {
            return;
        }

        if (service.isRunning()) {
            service.stop();
        }
    }

    /**
     * 初始化Android Driver
     */
    public AndroidDriver initAndroidDriver(int port, DesiredCapabilities caps) {
        AndroidDriver<WebElement> androidDriver = null;
        String androidDriverUrl = "http://" + FrameworkConst.ip + ":" + port + "/wd/hub";

        try {
            log.info("初始化Android Driver：" + androidDriverUrl);
            androidDriver = new AndroidDriver<>(new URL(androidDriverUrl), caps);
            androidDriver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            stopServer();
            log.error(e.getLocalizedMessage());
            throw new BaseBusinessException("初始化Android Driver：" + androidDriverUrl + " 失败");
        }
        return androidDriver;
    }

    /**
     *  获取DesiredCapabilities
     * @param phoneUrl 连接远程设备时，phoneUrl是远程设备的URL，通过deviceName指定远程设备连接；连接本地设备时，通过udid连接
     */
    public DesiredCapabilities desiredCaps(String appName, String appPackage, String appActivity, String phoneUrl, String platformVersion) {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability("automationName", "appium");
        cap.setCapability("app", FrameworkConst.appPath + appName);
        cap.setCapability("deviceName", phoneUrl);
        cap.setCapability("platformName", "Android");
        cap.setCapability("platformVersion", platformVersion);
        cap.setCapability("udid", phoneUrl);
        cap.setCapability("appPackage", appPackage);
        cap.setCapability("appActivity", appActivity);
        cap.setCapability("unicodeKeyboard", true);
        cap.setCapability("resetKeyboard", true);
        cap.setCapability("noSign", true);
        cap.setCapability("automationName", "UiAutomator2");
        cap.setCapability("noReset", false);
        return cap;
    }

}
