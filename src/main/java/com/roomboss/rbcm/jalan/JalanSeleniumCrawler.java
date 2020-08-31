package com.roomboss.rbcm.jalan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class JalanSeleniumCrawler {

    private void setRates(WebDriver driver) {
        driver.findElement(By.className("adjustment")).click();
        driver.findElement(By.cssSelector(" img[alt='プランの期間を延長する']")).click();
        driver.findElement(By.linkText("12月")).click();

//        highLighterMethod(driver,driver.findElement(By.linkText("12月")));
    }


    public static void main(String[] args) {
    }

}
