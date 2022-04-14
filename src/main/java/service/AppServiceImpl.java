package service;

import consts.FrameworkConst;
import driverApp.DriverProperty;
import enums.AppServerEnum;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.remote.DesiredCapabilities;
import page.BasePage;
import parkApp.ParkProperty;
import utils.AdbUtil;
import utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhanghongjie11
 * @date 2022/3/8 5:16 PM
 * @description
 */
public class AppServiceImpl implements AppService{

    private static final AppiumService appiumService = new AppiumServerImpl();

    public List<String> getUUID(Boolean remoteDevice) {
        List<String> phoneUuid;

        if (remoteDevice) {
            ArrayList<String> phonePortList = FileUtil.getPhonePortList();
            phoneUuid = phonePortList.stream().map(port -> FrameworkConst.phoneIp + port).collect(Collectors.toList());
        }else {
            phoneUuid = AdbUtil.getDeviceUdid();
        }

        return phoneUuid;
    }

    public BasePage getAppPage(String uuid, AppServerEnum appServer) {
        if (appServer == AppServerEnum.PARK) {
            AppServerInfo parkServerInfo = login(uuid, appServer);
            return new ParkPage().build(parkServerInfo.getAndroidDriver());
        }else {
            AppServerInfo driverServerInfo = login(uuid, appServer);
            return new DriverPage().build(driverServerInfo.getAndroidDriver());
        }
    }


    private AppServerInfo login(String phoneUrl, AppServerEnum appServerEnum) {
        AppServerInfo driverServerInfo = initServer(phoneUrl, appServerEnum);
        LoginPage loginPage = new LoginPage().build(driverServerInfo.getAndroidDriver());

        if (appServerEnum == AppServerEnum.PARK) {
            loginPage.parkLogin(ParkProperty.getUserName(), ParkProperty.getPassword());
        }else {
            loginPage.driverLogin(DriverProperty.getUserName(), DriverProperty.getPassword());
        }
        return driverServerInfo;
    }

    /**
     * 从device.yaml获取设备端口，拼接设备URL
     * 连接赛博云测机器，检测是否连接成功，需要连接两天机器，司秘达和星园
     * 启动两个appium server，4723 4725 ，如果服务器端口占用，杀掉进程，重启启动
     * 初始化Android driver
     * remoteDevice : false:本地设备 true:远程设备
     */
    private AppServerInfo initServer(String phoneUrl, AppServerEnum appServer) {

        AdbUtil.connectDevice(phoneUrl);

        String phoneModel = AdbUtil.getPhoneModel(phoneUrl);
        String phoneVersion = AdbUtil.getPhoneVersion(phoneUrl);
        DesiredCapabilities caps = null;
        int port = 0;

        if (appServer == AppServerEnum.DRIVER) {
            caps = appiumService.desiredCaps(DriverProperty.getAppname(),
                    DriverProperty.getAppPackage(), DriverProperty.getppActivity(), phoneUrl, phoneVersion);
            port = FrameworkConst.driverServerPort;
        }

        if (appServer == AppServerEnum.PARK) {
            caps = appiumService.desiredCaps(ParkProperty.getAppname(),
                    ParkProperty.getAppPackage(), ParkProperty.getppActivity(), phoneUrl, phoneVersion);
            port = FrameworkConst.parkServerPort;
        }

        // 检查系统端口占用情况，如果占用，杀掉进程
        if (!StringUtils.isEmpty(AdbUtil.getPortInfo(port))) {
            AdbUtil.killPid(port);
        }

        // 启动appium server
        AppiumDriverLocalService service = appiumService.startAppiumServer(FrameworkConst.ip, port, caps);

        // 初始化Android driver
        AndroidDriver androidDriver = appiumService.initAndroidDriver(port, caps);

        return new AppServerInfo(phoneModel, phoneVersion, phoneUrl, caps, port, service, androidDriver);
    }
}
