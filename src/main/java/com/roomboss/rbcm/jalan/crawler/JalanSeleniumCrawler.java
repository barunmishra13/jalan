package com.roomboss.rbcm.jalan.crawler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JalanSeleniumCrawler {

    public static final String JALAN_URL = "https://wwws.jalan.net/yw/ywp0100/ywt0100LoginTop.do";

    public void crawl() throws IOException {
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setJavascriptEnabled(true);
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/local/share/phantomjs-1.9.8-linux-x86_64/bin/phantomjs");
        caps.setCapability("takesScreenshot", true);
        String [] phantomJsArgs = {"--ignore-ssl-errors=true"};
        caps.setCapability(
                PhantomJSDriverService.PHANTOMJS_GHOSTDRIVER_CLI_ARGS,
                phantomJsArgs);
        PhantomJSDriver driver = new PhantomJSDriver(caps);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(JALAN_URL);
        driver.findElement(By.name("usrId"));
        driver.findElement(By.name("usrId")).clear();
        driver.findElement(By.name("usrId")).sendKeys("rogai");
        driver.findElement(By.name("usrPwd")).clear();
        driver.findElement(By.name("usrPwd")).sendKeys("Fiorentina2014!");
        driver.findElement(By.linkText("ログイン")).click();
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
        this.takeScreenshot(driver,"/home/barun/temp/screenshots/s.png");
        System.out.println(driver.getTitle());
    }

    public void takeScreenshot(WebDriver webdriver, String fileWithPath) throws IOException {
        File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(fileWithPath));
//        TakesScreenshot scrShot =((TakesScreenshot)webdriver);
//        File srcFile=scrShot.getScreenshotAs(OutputType.FILE);
//        File destFile=new File(fileWithPath);
//        FileUtils.copyFile(srcFile, destFile);
    }

    public static void main(String[] args) throws IOException {
        JalanSeleniumCrawler jalanSeleniumCrawler = new JalanSeleniumCrawler();
        jalanSeleniumCrawler.crawl();
    }

}
