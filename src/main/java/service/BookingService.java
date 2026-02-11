package service;

import entity.Booking;
import java.time.LocalDateTime;
import java.util.List;


public interface BookingService {
    Booking createBooking(Booking booking);

    Booking cancelBooking (Booking booking);

    List<Booking> findBookingsByDateRange(LocalDateTime startTime,LocalDateTime endTime);


}
