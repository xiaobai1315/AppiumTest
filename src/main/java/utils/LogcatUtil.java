package utils;

import lombok.extern.log4j.Log4j;
import security.Utils.TimerUtil;

import java.io.*;

/**
 * @author zhanghongjie11
 * @date 2021/11/18 5:42 下午
 * @description
 */
@Log4j
public class LogcatUtil {

    public static String path = System.getProperty("user.dir") + "/appCrashLog/log.txt";
    public static String crashLogPath = System.getProperty("user.dir") + "/appCrashLog/crashLog.txt";

    public static void getAppCrashlog() {
        getLogfile();
        getCrashLog();
    }

    // 清空设备的日志
    public static void clearDeviceLog() {
        CmdUtil.exec("adb logcat -c");
    }

    // 通过adb指令获取崩溃日志
    private static void getLogfile() {
        try {
            String cmd = "adb logcat -v time *:E > " + path;
            new Thread(() -> {
                log.info("start get adb logcat");
                CmdUtil.exec(cmd);
            }).start();

            TimerUtil.sleep(1);

            String stopCmd = "kill -9 $(ps -ef | grep 'adb logcat' | sed -n 1p | awk '{print $2}')";
            new Thread(() -> {
                log.info("stop adb logcat");
                CmdUtil.exec(stopCmd);
            }).start();
        }catch (Exception e) {
            log.error("获取APP日志失败");
            e.printStackTrace();
        }
    }

    // 读取崩溃日志，获取崩溃信息
    private static void getCrashLog() {

        File file = new File(path);
        File crashFile = new File(crashLogPath);
        String pid = null;

        BufferedReader reader = null;
        BufferedWriter writer = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            writer = new BufferedWriter(new FileWriter(crashFile));

            writer.write("-------------------------------------------------------\n");
            writer.write(getDeviceInfo() + "\n");
            writer.write("-------------------------------------------------------\n");

            String line = null;
            while ( (line = reader.readLine()) != null) {
                if (line.contains("FATAL EXCEPTION: main")) {

                    // 崩溃日志开始
                    writer.write("\n# begging of crash --- >>>\n");
                    writer.write(line + "\n");
                    // 获取崩溃的PID
                    pid = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
                    log.info("崩溃线程PID：" + pid);
                }else if(pid != null && line.contains(pid)) {
                    writer.write(line + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("读取APP崩溃日志失败");
        }finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("close file reader error");
                }
            }

            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("close file writer error");
                }
            }
        }
    }

    // 获取设备信息
    private static String getDeviceInfo() {
        String manufacturer = CmdUtil.exec("adb shell getprop ro.product.manufacturer").replace("\n", "");
        String model = CmdUtil.exec("adb shell getprop ro.product.model").replace("\n", "");
        String version = CmdUtil.exec("adb shell getprop ro.build.version.release").replace("\n", "");
        String sdkVersion = CmdUtil.exec("adb shell getprop ro.build.version.sdk").replace("\n", "");

        String output = "DeviceInfo: " + manufacturer + " " + model + " | Android " + version + " (API " + sdkVersion + ")";
        log.info("手机信息： " + output);
        return output;
    }

    public static void main(String[] args) {
        getCrashLog();
    }
}
