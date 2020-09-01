package com.roomboss.rbcm.jalan;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class JalanReservationDetails {
    private String reservationId;
    private String guestName;
    private String phoneNumber;
    private String zipCode;
    private String address;
    private String ageGroup;
    private String ratePlanName;
    private String roomType;
    private String reservationDate;
    private String email;
    private BigDecimal hotelFee;
    private String mealPlans;
    private String bookingDetails;
    private String checkInTime;
    private String checkOutTime;
    private int numAdults;
    private int numChildren;
    private int numNights;
    private int numRooms;
}
