package com.roomboss.rbcm.jalan.crawler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

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
        login(driver);
        setAvailability(driver);
        this.takeScreenshot(driver,"/Users/barunmishra/temp/screenshots/s.png");
        System.out.println(driver.getTitle());
    }

    private void setAvailability(WebDriver driver) {
        driver.findElement(By.className("adjustment")).click();
        Select seleYear = new Select(driver.findElement(By.name("changeFromYear")));
        seleYear.selectByValue("2021");
        Select seleMonth = new Select(driver.findElement(By.name("changeFromMonth")));
        seleMonth.selectByValue("3");
        Select seleDay = new Select(driver.findElement(By.name("changeFromDay")));
        seleDay.selectByValue("30");
        driver.findElement(By.cssSelector(" img[alt='切替']")).click();
    }

    private void login(WebDriver driver) {
        driver.findElement(By.name("usrId"));
        driver.findElement(By.name("usrId")).clear();
        driver.findElement(By.name("usrId")).sendKeys(USR);
        driver.findElement(By.name("usrPwd")).clear();
        driver.findElement(By.name("usrPwd")).sendKeys(PASS);
        driver.findElement(By.className("btn")).click();
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
