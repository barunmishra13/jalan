package com.roomboss.rbcm.jalan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class JalanAvailabilityService extends JalanBaseService {


    public void setAvailability(TreeMap<LocalDate, Integer> caps, String property, String roomType) throws Exception {
        login(property);
        driver.findElement(By.className("adjustment")).click();
        if (isCapsRange(caps)) {
            driver.findElement(By.cssSelector(" img[alt='室数カレンダー']")).click();
            setCapsForMonth(driver, caps.firstKey().getYear(), caps.firstKey().getMonth().getValue(), caps.firstEntry().getValue());
        } else {
            AtomicInteger capCounter = new AtomicInteger();
            caps.forEach((date, val) -> {
                String dateString = date.toString();
                capCounter.getAndIncrement();
                selectSearchDate(driver, dateString);
                String path = ".//a[contains(@onclick, '" + date + "')]";
//          highLighterMethod(driver,driver.findElement(By.xpath(".//a[contains(@onclick, '2021/03/30')]")));
                driver.findElement(By.xpath(path)).click();
                setCapsForDate(driver, capCounter, val);
            });
        }
        this.takeScreenshot(JALAN_SCREENSHOTS_UBUNTU + "s.png");
    }

    private boolean isCapsRange(TreeMap<LocalDate, Integer> caps) {
        return caps.size() == 2
                && caps.firstKey().getMonth() == caps.lastKey().getMonth()
                && caps.firstKey().getDayOfMonth() == 1 && caps.lastKey().getDayOfMonth() >= 28
                && caps.firstEntry().getValue().equals(caps.lastEntry().getValue());
    }

    private void setCapsForMonth(WebDriver driver, Integer year, Integer month, Integer val) throws Exception {
        String mainWindow = driver.getWindowHandle();
        Set<String> s1 = driver.getWindowHandles();
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
                boolean hasError = driver.findElements(By.className("srrmod005")).size() > 0;
//                System.out.println(driver.findElement()..getText());
                if(!hasError) {
                    driver.findElement(By.cssSelector(" img[alt='変更する']")).click();
                    driver.switchTo().window(mainWindow);
                    this.takeScreenshot(JALAN_SCREENSHOTS_UBUNTU + "s_month.png");
                }
                else {
                    throw new Exception("Error while updating");
                }
            }
        }
    }

    private void setCapsForDate(WebDriver driver, AtomicInteger capCounter, Integer val) {
        String mainWindow = driver.getWindowHandle();
        Set<String> s1 = driver.getWindowHandles();
        for (String childWindow : s1) {
            if (!mainWindow.equalsIgnoreCase(childWindow)) {
                driver.switchTo().window(childWindow);
                driver.findElement(By.name("supplyRoomCntString")).clear();
                driver.findElement(By.name("supplyRoomCntString")).sendKeys(val.toString());
                driver.findElement(By.cssSelector(" img[title='変更内容を確認する']")).click();
                driver.findElement(By.cssSelector(" img[alt='変更する']")).click();
                driver.switchTo().window(mainWindow);
                this.takeScreenshot(JALAN_SCREENSHOTS_UBUNTU + "s" + capCounter + ".png");
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
        seleDay.selectByValue(String.valueOf(Integer.parseInt(day) - 1));
        driver.findElement(By.cssSelector(" img[alt='切替']")).click();
    }

}
