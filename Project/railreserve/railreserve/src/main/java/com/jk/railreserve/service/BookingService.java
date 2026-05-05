package com.jk.railreserve.service;

import com.jk.railreserve.model.Booking;
import com.jk.railreserve.model.PaymentStatus;
import com.jk.railreserve.model.Seat;
import com.jk.railreserve.model.SeatStatus;
import com.jk.railreserve.repository.BookingRepository;
import com.jk.railreserve.repository.SeatRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final SeatRepository seatRepository;
    private final PaymentProcessor paymentProcessor;
    private final BookingRepository bookingRepository;

    public BookingService(SeatRepository seatRepository, PaymentProcessor paymentProcessor, BookingRepository bookingRepository) {
        this.seatRepository = seatRepository;
        this.paymentProcessor = paymentProcessor;
        this.bookingRepository = bookingRepository;
    }

    @Transactional
    public String lockSeat(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Such a seat was not found"));

        if (seat.getStatus() != SeatStatus.AVAILABLE) {
            return "Seat is already reserved";
        }

        seat.setStatus(SeatStatus.LOCKED);
        seat.setLockExpiration(LocalDateTime.now().plusMinutes(1));
        seatRepository.save(seat);

        return "Seat is reserved for 10 minut.";
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void autoReleaseExpiredSeats() {
        LocalDateTime now = LocalDateTime.now();
        List<Seat> expiredSeats = seatRepository.findByStatusAndLockExpirationBefore(SeatStatus.LOCKED, now);

        for (Seat seat : expiredSeats) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setLockExpiration(null);
            seatRepository.save(seat);
            System.out.println("Automatically release seat with ID: " + seat.getId());
        }
    }


    @Transactional
    public Booking finalizeBooking(Long seatId) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new RuntimeException("Seat does not exists"));

        if (seat.getStatus() != SeatStatus.LOCKED) {
            throw new IllegalStateException("You can not pay for locked seat!");
        }

        boolean paymentSuccess = paymentProcessor.process(50.0f);

        if (paymentSuccess) {
            seat.setStatus(SeatStatus.OCCUPIED);
            seat.setLockExpiration(null);
            seatRepository.save(seat);

            Booking booking = new Booking();
            booking.setSeat(seat);
            booking.setTimestamp(LocalDateTime.now());
            booking.setTotalAmount(50.0f);
            booking.setPaymentStatus(PaymentStatus.SUCCESS);

            return bookingRepository.save(booking);
        } else {
            throw new RuntimeException("Payment error");
        }
    }
}