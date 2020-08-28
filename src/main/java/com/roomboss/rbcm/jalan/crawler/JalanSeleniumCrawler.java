package com.roomboss.rbcm.jalan.crawler;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JalanSeleniumCrawler {

    public static final String JALAN_URL = "https://wwws.jalan.net/yw/ywp0100/ywt0100LoginTop.do";
    public static final String USR = "Y64351";
    public static final String PASS = "";

    private static final TreeMap<LocalDate, Integer> caps = new TreeMap<>();
    private static final String JALAN_SCREENSHOTS_UBUNTU = "/home/barun/temp/screenshots/";
    private static final String CHROME_DRIVER_PATH_UBUNTU = "/usr/bin/chromedriver";
    private static final String JALAN_SCREENSHOTS_MAC = "/Users/barunmishra/temp/screenshots/";
    private static final String CHROME_DRIVER_PATH_MAC = "/usr/local/Caskroom/chromedriver/85.0.4183.83/chromedriver";

    static {
        caps.put(LocalDate.parse("2020-12-01"),1);
        caps.put(LocalDate.parse("2020-12-31"),1);
    }

    public void crawl() throws IOException {
        System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH_UBUNTU);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors", "--silent");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.get(JALAN_URL);
        login(driver);
        setAvailability(driver);
//        setRates(driver);
        this.takeScreenshot(driver, JALAN_SCREENSHOTS_UBUNTU + "s.png");
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
        if(isCapsRange(caps)) {
            driver.findElement(By.cssSelector(" img[alt='室数カレンダー']")).click();
            setCapsForMonth(driver,caps.firstKey().getYear(), caps.firstKey().getMonth().getValue(), caps.firstEntry().getValue());
        }
        else {
            AtomicInteger capCounter = new AtomicInteger();
            caps.forEach((date, val) -> {
                String dateString = date.toString();
                capCounter.getAndIncrement();
                selectSearchDate(driver, dateString);
                String path = ".//a[contains(@onclick, '"+date+"')]";
//          highLighterMethod(driver,driver.findElement(By.xpath(".//a[contains(@onclick, '2021/03/30')]")));
                driver.findElement(By.xpath(path)).click();
                setCapsForDate(driver, capCounter, val);
            });
        }


    }

    private boolean isCapsRange(TreeMap<LocalDate, Integer> caps) {
        return caps.size() == 2
                && caps.firstKey().getMonth() == caps.lastKey().getMonth()
                && caps.firstKey().getDayOfMonth() == 1 && caps.lastKey().getDayOfMonth() >= 28
                && caps.firstEntry().getValue().equals(caps.lastEntry().getValue());
    }

    private void setCapsForMonth(WebDriver driver, Integer year, Integer month, Integer val) {
        String mainWindow= driver.getWindowHandle();
        Set<String> s1= driver.getWindowHandles();
        for (String childWindow : s1) {
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                Select selectYear = new Select(driver.findElement(By.name("year")));
                selectYear.selectByValue(year.toString());
                Select seleMonth = new Select(driver.findElement(By.name("month")));
                seleMonth.selectByValue(month.toString());
                driver.findElement(By.cssSelector(" img[alt='切替']")).click();
                driver.switchTo().alert().accept();
                driver.findElements(By.name("remRoomCnt"))
                        .forEach(capsTextBox -> {
                            capsTextBox.clear();
                            capsTextBox.sendKeys(val.toString());
                        });
                driver.findElement(By.cssSelector(" img[title='変更内容を確認する']")).click();
                driver.findElement(By.cssSelector(" img[alt='変更する']")).click();
                driver.switchTo().window(mainWindow);
                try {
                    this.takeScreenshot(driver, JALAN_SCREENSHOTS_UBUNTU + "s_month.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setCapsForDate(WebDriver driver, AtomicInteger capCounter, Integer val) {
        String mainWindow= driver.getWindowHandle();
        Set<String> s1= driver.getWindowHandles();
        for (String childWindow : s1) {
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                driver.findElement(By.name("supplyRoomCntString")).clear();
                driver.findElement(By.name("supplyRoomCntString")).sendKeys(val.toString());
                driver.findElement(By.cssSelector(" img[title='変更内容を確認する']")).click();
                driver.findElement(By.cssSelector(" img[alt='変更する']")).click();
                driver.switchTo().window(mainWindow);
                try {
                    this.takeScreenshot(driver, JALAN_SCREENSHOTS_UBUNTU + "s" + capCounter +".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void selectSearchDate(WebDriver driver, String date) {
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
