package utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;

import static page.BasePage.sleep;

/**
 * @author zhanghongjie11
 * @date 2022/3/2 4:40 PM
 * @description
 */
@Slf4j
public class AdbUtil {

    /**
     * adb 连接设备
     */
    public static Boolean connectDevice(String deviceName) {
        String cmd = "adb connect " + deviceName;
        log.info("连接设备：" + cmd);
        CmdUtil.exec(cmd);
        sleep(2);
        List<String> deviceUdid = AdbUtil.getDeviceUdid();
        if (!deviceUdid.contains(deviceName)) {
            log.error("连接设备: " + deviceName + " 失败");
            return Boolean.FALSE;
        }

        log.info("连接设备: " + deviceName + " 成功");
        return Boolean.TRUE;
    }

    /**
     * 断开设备
     */
    public static void disconnectDevice() {
        // 获取手机型号
        String cmd = "adb disconnect";
        log.info("断开设备：" + cmd);
        CmdUtil.exec(cmd);
        log.info("断开所有设备");
    }

    /**
     * 查看端口占用信息
     */
    public static String getPortInfo(int port) {
        String cmd = "lsof -i tcp:" + port;
        log.info("查看系统端口占用：" + cmd);
        String exec = CmdUtil.exec(cmd);
        log.info("系统端口: " + port + " 占用信息：" + exec);
        return exec;
    }

    /**
     * 杀掉占用端口的进程
     */
    public static void killPid(int port) {
        String cmd = "lsof -t -i tcp:" + port;
        log.info("查看占用端口的PID：" + cmd);
        String pids = CmdUtil.exec(cmd);
        String[] split = pids.split("\n");
        log.info("占用端口: " + port + "的PID：" + JSON.toJSONString(split));

        for (String pid : split) {
            cmd = "kill " + pid;
            log.info("杀掉进程：" + cmd);
            CmdUtil.exec(cmd);
        }
    }

    /**
     * 获取已连接设备的UDID
     */
    public static ArrayList<String> getDeviceUdid() {
        String cmd = "adb devices";
        log.info("获取已连接设备的UDID：" + cmd);
        String adb_devices = CmdUtil.exec(cmd);
        Assert.assertNotNull(adb_devices, "执行shell命令获取设备列表为空");
        ArrayList<String> devices = new ArrayList<>();
        String[] split = adb_devices.split("\n");
        for (String str : split) {
            if (!str.contains("List of devices attached")) {
                String[] strings = str.split("\t");
                for (String string : strings) {
                    if (!string.equals("device")) {
                        devices.add(string);
                    }
                }
            }
        }
        log.info("获取系统已连接的手机：" + devices.toString());
        return devices;
    }

    /**
     * 获取手机型号
     */
    public static String getPhoneModel(String phoneUrl) {
        String cmd = "adb -s " + phoneUrl + " shell getprop ro.product.model";
        log.info("获取手机型号：" + cmd);
        String model = StringUtils.remove(CmdUtil.exec(cmd), "\n");
        log.info("获取设备：" + phoneUrl + " 型号：" + model);
        return model;
    }

    /**
     * 获取手机版本
     */
    public static String getPhoneVersion(String phoneUrl) {
        String cmd = "adb -s " + phoneUrl + " shell getprop ro.build.version.release";
        log.info("获取手机版本：" + cmd);
        String version = StringUtils.remove(CmdUtil.exec(cmd), "\n");
        log.info("获取设备：" + phoneUrl + " 系统版本：" + version);
        return version;
    }

    public static void main(String[] args) {

//        System.out.println(getPhoneModel("11.50.74.204:9445"));
//        System.out.println(getPhoneVersion("11.50.74.204:9445"));
    }

}
