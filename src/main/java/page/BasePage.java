package page;

import consts.FrameworkConst;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.PointOption;
import javafx.scene.web.WebView;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import po.PageElement;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author zhanghongjie11
 * @date 2021/10/25 5:15 下午
 * @description
 */
@Slf4j
public abstract class BasePage {

    public abstract BasePage build(AndroidDriver driver);

    public static WebElement findElement(AndroidDriver driver, PageElement element) {
        return webDriverWait(driver, element);
    }

    // 点击元素
    public static void clickElement(AndroidDriver driver, PageElement element) {
        log.info("点击: " + element.getDesc());
        findElement(driver, element).click();
    }

    public static List<WebElement> findElements(AndroidDriver driver, PageElement element) {
        List<WebElement> elements = driver.findElements(element.getBy(), element.getPath());
        return new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public static Boolean isElementExist(AndroidDriver driver, PageElement element) {
        try {
            WebElement elem = driver.findElement(element.getBy(), element.getPath());
            return elem.isDisplayed();
        }catch (NoSuchElementException e) {
            return false;
        }
    }

    public void getContext(AndroidDriver driver) {
        Set contextHandles = driver.getContextHandles();
        for (Object contextHandle : contextHandles) {
            System.out.println(contextHandle.toString());
        }
        System.out.println(driver.getContext().toString());
    }

    // 隐式等待元素出现
    public static WebElement webDriverWait(AndroidDriver driver, PageElement element) {
        log.info("查找元素: " + element.getDesc() + " " + element.getPath());
        try {
            WebElement expect = driver.findElement(element.getBy(), element.getPath());
            return new WebDriverWait(driver, 8).until(ExpectedConditions.visibilityOf(expect));
        }catch (TimeoutException e) {
            screenshot(driver, element.getDesc());
            throw new BaseBusinessException("查找 " + element.getDesc() + " 超时");
        }catch (NoSuchElementException e) {
            screenshot(driver, element.getDesc());
            throw new BaseBusinessException("控件 " + element.getDesc() + " 不存在");
        }catch (Exception e) {
            e.printStackTrace();
            throw new BaseBusinessException("查找控件" + element.getDesc() + "失败");
        }
    }

    // 隐式等待元素出现
    public static void tapPoint(AndroidDriver driver, int x, int y) {
        TouchAction ta = new TouchAction(driver);
        ta.press(PointOption.point(x, y)).release().perform();
    }

    // 休眠5S
    public static void sleep(long senond) {
        // 这种休眠方式不起作用，用Thread.sleep管用
        //driver.manage().timeouts().implicitlyWait(senond, TimeUnit.SECONDS);
        try {
            Thread.sleep(senond * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // 屏幕截图
    public static void screenshot(AndroidDriver driver, String title) {
        String pathDir = FrameworkConst.path + "/screenshot/" + utils.FileUtil.getScreenshotDirName() + "/" + title + ".jpg";
        log.info("保存截图:" + pathDir);
        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);
        try {
            FileUtil.copyFile(screenshotAs, new File(pathDir));
        } catch (IOException e) {
            log.error("保存截图失败" + e.toString());
            e.printStackTrace();
        }
    }

    // 检测toast提示框
    public static WebElement toastMessage(AndroidDriver driver, String[] toastMessage) {
//        String xpath = ".//*[contains(@text,'"+ toastMessage + "')]";
        String info = "";
        for (int i = 0; i < toastMessage.length; i++) {
            info = info + "starts-with(@text,'"+ toastMessage[i] + "')";
            if (i != toastMessage.length - 1) {
                info += " or ";
            }
        }
        log.info("toast message :" + info);

        String xpath = ".//*[" + info + "]";
        log.info("根据xpath " + xpath + " 查找toast提示框");
        WebElement webElement = null;
        try {
            webElement = new WebDriverWait(driver, 8).until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
        }catch (TimeoutException e) {
            return null;
        }

        screenshot(driver, Arrays.toString(toastMessage));

        if (webElement == null) {
            log.info("未找到xpath= " + xpath + " 的toast提示框");
        }else {
            log.info("找到了xpath= " + xpath + " 的toast提示框");
        }
        return webElement;
    }

    /**
     * 键盘输入用户名密码
     * @param driver driver
     * @param keys 需要键入的文字
     */
    public static void sendKeys(AndroidDriver driver, PageElement pageElement, String keys) {
        WebElement element = findElement(driver, pageElement);
        log.info(pageElement.getDesc() +  "输入: " + keys);
        element.click();
        Actions action = new Actions(driver);
        action.sendKeys(keys).perform();
    }

    public static void sendKeys1(AndroidDriver driver, WebElement element, String keys) {
        element.click();
        String[] split = keys.split("");
        for (String s : split) {
            if(StringUtils.isNumeric(s)) {
                System.out.println(s + " is numeric");
                AndroidKey androidKey = AndroidKey.valueOf("DIGIT_" + s);
                driver.pressKey(new KeyEvent(androidKey));
            }else if(s.equals("_")){
                System.out.println(s + " is not numeric");
                driver.pressKey(new KeyEvent(AndroidKey.MINUS));
            }else {
                System.out.println(s + " is not numeric");
                driver.pressKey(new KeyEvent(AndroidKey.valueOf(s)));
            }
        }
    }

    // 断言 元素存在
    public static void assertElementExist(AndroidDriver driver, PageElement pageElement, String message) {
        WebElement webElement = webDriverWait(driver, pageElement);
        try {
            Assert.assertNotNull(webElement, message);
        }catch (AssertionError e) {
            log.error("元素存在断言失败， " + message);
            screenshot(driver, message);
            throw e;
        }
    }

    public static void assertNotNull(AndroidDriver driver, Object element, String message) {
        try {
            Assert.assertNotNull(element, message);
        }catch (AssertionError e) {
            log.error("元素不存在断言失败， " + message);
            screenshot(driver, message);
            throw e;
        }
    }

    // 查找指定标题的android.view.View是否存在
    public WebElement elementExist(AndroidDriver driver, String title) {
        List<WebElement> elements = findElements(driver, utils.FileUtil.getAndroidWidget("androidViewView"));
        for (WebElement element : elements) {
            if (element.getText().equals(title)) {
                log.info("内容为 " + title + " 的元素存在");
                return element;
            }
        }
        log.info("内容为 " + title + " 的元素不存在");
        screenshot(driver, title);
        return null;
    }

    public static void main(String[] args) {
//        System.out.println(portIsUse(4723));
    }
}
