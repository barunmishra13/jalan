package com.roomboss.rbcm.jalan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.TreeMap;

@SpringBootTest
class JalanApplicationTests {

	@Autowired
	private JalanAvailabilityService jalanAvailabilityService;

	@Autowired
	private JalanReservationService jalanReservationService;

	@Test
	void testAvailability() throws Exception {
		TreeMap<LocalDate, Integer> caps = new TreeMap<>();
		caps.put(LocalDate.parse("2021-01-01"),0);
		caps.put(LocalDate.parse("2021-01-31"),0);
		jalanAvailabilityService.setAvailability(caps, "Y64351", null);
	}

	@Test
	void testGetBooking() throws Exception {
		jalanReservationService.fetchRecentReservations(null, "Y64351", "6XAGGFVH");
	}

}
