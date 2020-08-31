package com.roomboss.rbcm.jalan;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public abstract class JalanBaseService {

    private final String JALAN_URL = "https://wwws.jalan.net/yw/ywp0100/ywt0100LoginTop.do";
    private Map<String, JalanCredentials> jalanCredentialsMap = new HashMap<>();

    protected final String JALAN_SCREENSHOTS_UBUNTU = "/home/barun/temp/screenshots/";
    protected final String CHROME_DRIVER_PATH_UBUNTU = "/usr/bin/chromedriver";
    protected final String JALAN_SCREENSHOTS_MAC = "/Users/barunmishra/temp/screenshots/";
    protected final String CHROME_DRIVER_PATH_MAC = "/usr/local/Caskroom/chromedriver/85.0.4183.83/chromedriver";
    protected WebDriver driver;

    @PostConstruct
    public void initialize()
     {
        jalanCredentialsMap.put("Y64351", new JalanCredentials("Y64351", "1212heritage?"));
        setupChromeDriver();
    }

    private void setupChromeDriver() {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH_UBUNTU);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    private void highLighterMethod(WebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: green; border: 3px solid blue;');", element);
    }

    protected void login(WebDriver driver, String propertyCode) throws Exception {
        JalanCredentials jalanCredentials = jalanCredentialsMap.get(propertyCode);
        if (jalanCredentials != null) {
            driver.get(JALAN_URL);
            driver.findElement(By.name("usrId"));
            driver.findElement(By.name("usrId")).clear();
            driver.findElement(By.name("usrId")).sendKeys(jalanCredentials.userName);
            driver.findElement(By.name("usrPwd")).clear();
            driver.findElement(By.name("usrPwd")).sendKeys(jalanCredentials.password);
            driver.findElement(By.className("btn")).click();
        }
        else {
            throw new Exception("Property Not Set Up");
        }

    }

    protected void takeScreenshot(WebDriver webdriver, String fileWithPath) {
        File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(scrFile, new File(fileWithPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Data
    @AllArgsConstructor
    private class JalanCredentials {
        private String userName;
        private String password;
    }

}
