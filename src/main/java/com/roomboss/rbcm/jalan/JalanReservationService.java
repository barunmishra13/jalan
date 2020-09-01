package com.roomboss.rbcm.jalan;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class JalanReservationService extends JalanBaseService {

    public void fetchRecentReservations(LocalDate date, String property, String reservationId) throws Exception {
        login(property);
        goToReservationDetails(reservationId);
        JalanReservationDetails reservationDetails = new JalanReservationDetails();
        List<WebElement> elementList = driver.findElements(By.className("ttl"));
        reservationDetails.setReservationId(elementList.get(1).getText());
        reservationDetails.setGuestName(elementList.get(2).getText());
        reservationDetails.setPhoneNumber(elementList.get(3).getText());
        reservationDetails.setZipCode(elementList.get(4).getText());
        reservationDetails.setAddress(elementList.get(5).getText());
        reservationDetails.setAgeGroup(elementList.get(6).getText());
        reservationDetails.setRatePlanName(elementList.get(11).getText());
        reservationDetails.setRoomType(elementList.get(12).getText());
        reservationDetails.setMealPlans(elementList.get(13).getText());
        extractBookingDetails(reservationDetails, elementList.get(14).getText());
        reservationDetails.setEmail(elementList.get(23).getText());
        reservationDetails.setHotelFee(getHotelFee());
        System.out.println(reservationDetails);
        this.takeScreenshot(JALAN_SCREENSHOTS_UBUNTU + "reservation.png");
//        elementList.forEach(e -> System.out.println(e.getText()));
        logout();
    }

    private BigDecimal getHotelFee() {
        List<WebElement> elementList2 = driver.findElements(By.className("txt"));
        return new BigDecimal(
                elementList2.get(0).getText().replace("   円  （税込・ サ込 ） ", "").replaceAll(",", "")
        );
    }

    private void extractBookingDetails(JalanReservationDetails reservationDetails, String billingDetails) {
        String[] details = billingDetails.split("\\r?\\n");
        reservationDetails.setCheckInTime(details[1]);
        reservationDetails.setCheckOutTime(details[3]);
        reservationDetails.setNumNights(getNumDetails(details[4], "泊"));
        reservationDetails.setNumRooms(getNumDetails(details[5], "部屋"));
        extractGuestCount(reservationDetails, details[6]);
    }

    private void extractGuestCount(JalanReservationDetails reservationDetails, String detail) {
        String[] guestCountDetails = detail.split("／");
        reservationDetails.setNumChildren(getChildCount(guestCountDetails[1]));
        reservationDetails.setNumAdults(getAdultCount(guestCountDetails[0]));
    }

    private int getAdultCount(String guestCountDetail) {
        return Integer.parseInt(guestCountDetail.split(" ")[3].split("（")[0].replace("人", ""));
    }

    private int getChildCount(String guestCountDetail) {
        return Integer.parseInt(guestCountDetail.split(" ")[1].replace("人", ""));
    }

    private int getNumDetails(String detail, String type) {
        String[] numDetails = detail.split("：");
        return Integer.parseInt(numDetails[1].replace(type, "").trim());
    }

    private void goToReservationDetails(String reservationId) {
        driver.findElement(By.className("management")).click();
        driver.findElement(By.cssSelector(" img[alt='予約者検索（変更・キャンセル）']")).click();
        driver.findElement(By.name("cond.rsvNo")).clear();
        driver.findElement(By.name("cond.rsvNo")).sendKeys(reservationId);
        driver.findElement(By.cssSelector(" img[alt='検索']")).click();
        if (!StringUtils.isEmpty(reservationId) && driver.findElement(By.linkText(reservationId)).isDisplayed()) {
            driver.findElement(By.linkText(reservationId)).click();
        }
    }

    public void fetchRecentCancellations(LocalDate date) {

    }

}
