package utils;

import lombok.extern.log4j.Log4j;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * @author zhanghongjie11
 * @date 2021/11/16 2:50 下午
 * @description
 */
@Log4j
public class CmdUtil {
    public static String exec(String cmd) {
        try {
            String[] cmdA = { "/bin/sh", "-c", cmd };
            Process process = Runtime.getRuntime().exec(cmdA);
            LineNumberReader br = new LineNumberReader(new InputStreamReader(
                    process.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            log.info("执行shell指令: " + cmd);
            while ((line = br.readLine()) != null) {
                log.info("  -> " + line);
                sb.append(line).append("\n");
            }
            return sb.toString();
        } catch (Exception e) {
            log.error("shell命令执行失败:" + e.toString());
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        String adb_devices = exec("/Users/zhanghongjie11/Library/Android/sdk/platform-tools/adb devices");
        System.out.println(adb_devices);
    }
}
