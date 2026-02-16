package service;

import entity.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingService {
    Booking createBooking(Booking bookingId);

    Booking cancelBooking (Booking booking);

    List<Booking> findBookingsByDateRange(LocalDateTime startTime,LocalDateTime endTime);


}
