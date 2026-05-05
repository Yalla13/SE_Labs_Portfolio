package com.jk.railreserve.repository;


import com.jk.railreserve.model.Seat;
import com.jk.railreserve.model.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findByStatusAndLockExpirationBefore(SeatStatus status, LocalDateTime time);
}