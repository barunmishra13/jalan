package com.roomboss.rbcm.jalan.crawler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class JalanSeleniumCrawler {

    public static final String JALAN_URL = "https://wwws.jalan.net/yw/ywp0100/ywt0100LoginTop.do";
    public static final String USR = "Y64351";
    public static final String PASS = "";

    private static final Map<String, String> caps = new HashMap<>();

    static {
        caps.put("2021/03/30","1");
        caps.put("2021/03/31","2");
    }

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
//        setRates(driver);
        this.takeScreenshot(driver,"/Users/barunmishra/temp/screenshots/s.png");
        System.out.println(driver.getTitle());
    }

    private void setRates(WebDriver driver) {
        driver.findElement(By.className("adjustment")).click();
        driver.findElement(By.cssSelector(" img[alt='プランの期間を延長する']")).click();
        driver.findElement(By.linkText("12月")).click();

//        highLighterMethod(driver,driver.findElement(By.linkText("12月")));
    }

    private void setAvailability(WebDriver driver) {
        driver.findElement(By.className("adjustment")).click();
        caps.forEach((date, val) -> {
            String[] d = date.split("/");
            String year = d[0];
            String month = d[1];
            String day = d[2];
            Select seleYear = new Select(driver.findElement(By.name("changeFromYear")));
            seleYear.selectByValue(year);
            Select seleMonth = new Select(driver.findElement(By.name("changeFromMonth")));
            seleMonth.selectByValue(String.valueOf(Integer.parseInt(month)));
            Select seleDay = new Select(driver.findElement(By.name("changeFromDay")));
            seleDay.selectByValue(String.valueOf(Integer.parseInt(day)-1));
            driver.findElement(By.cssSelector(" img[alt='切替']")).click();
            String path = ".//a[contains(@onclick, '"+date+"')]";
        highLighterMethod(driver,driver.findElement(By.xpath(".//a[contains(@onclick, '2021/03/30')]")));
//            String path = ".//a[contains(@onclick, '"+date+"')]";
            driver.findElement(By.xpath(path)).click();
            String mainWindow=driver.getWindowHandle();
            Set<String> s1=driver.getWindowHandles();
            for (String childWindow : s1) {
                if (!mainWindow.equalsIgnoreCase(childWindow)) {
                    driver.switchTo().window(childWindow);
                    driver.findElement(By.name("supplyRoomCntString")).clear();
                    driver.findElement(By.name("supplyRoomCntString")).sendKeys(val);
                    driver.findElement(By.cssSelector(" img[title='変更内容を確認する']")).click();
                    driver.findElement(By.cssSelector(" img[alt='変更する']")).click();
                    driver.switchTo().window(mainWindow);
                }
            }
        });

    }

    public void highLighterMethod(WebDriver driver, WebElement element){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'background: green; border: 3px solid blue;');", element);
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
