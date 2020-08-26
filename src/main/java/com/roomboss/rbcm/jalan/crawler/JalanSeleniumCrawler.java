package com.roomboss.rbcm.jalan.crawler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class JalanSeleniumCrawler {

    public static final String JALAN_URL = "https://wwws.jalan.net/yw/ywp0100/ywt0100LoginTop.do";
    public static final String USR = "";
    public static final String PASS = "";

    public void crawl() throws IOException {
        String chromeDriverPath = "/usr/local/Caskroom/chromedriver/85.0.4183.83/chromedriver" ;
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(JALAN_URL);
        driver.findElement(By.name("usrId"));
        driver.findElement(By.name("usrId")).clear();
        driver.findElement(By.name("usrId")).sendKeys(USR);
        driver.findElement(By.name("usrPwd")).clear();
        driver.findElement(By.name("usrPwd")).sendKeys(PASS);
        driver.findElement(By.className("btn")).click();
        File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String screenshotBase64 = ((TakesScreenshot)driver).getScreenshotAs(OutputType.BASE64);
        this.takeScreenshot(driver,"/Users/barunmishra/temp/screenshots/s.png");
        System.out.println(driver.getTitle());
    }

    public void takeScreenshot(WebDriver webdriver, String fileWithPath) throws IOException {
        File scrFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
        FileUtils.copyFile(scrFile, new File(fileWithPath));
    }

    public static void main(String[] args) throws IOException {
        JalanSeleniumCrawler jalanSeleniumCrawler = new JalanSeleniumCrawler();
        jalanSeleniumCrawler.crawl();
    }

}
