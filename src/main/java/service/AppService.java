package service;

import enums.AppServerEnum;
import page.BasePage;
import java.util.List;

/**
 * @author zhanghongjie11
 * @date 2022/3/8 5:16 PM
 * @description
 */
public interface AppService {

    /**
     * 获取本地或远程手机设备
     */
    public List<String> getUUID(Boolean remoteDevice);

    /**
     * 初始化appium server 、初始化android driver、连接手机、获取android driver操作页面
     */
    public BasePage getAppPage(String uuid, AppServerEnum appServer);
}
