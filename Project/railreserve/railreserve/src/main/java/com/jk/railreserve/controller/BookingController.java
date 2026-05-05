package com.jk.railreserve.controller;

import com.jk.railreserve.model.Booking;
import com.jk.railreserve.service.BookingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping("/lock/{seatId}")
    public String lockSeatForReservation(@PathVariable Long seatId) {
        return bookingService.lockSeat(seatId);
    }

    @PostMapping("/confirm/{seatId}")
    public Booking confirmAndPay(@PathVariable Long seatId) {
        return bookingService.finalizeBooking(seatId);
    }
}