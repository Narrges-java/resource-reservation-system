package service.impl;

import entity.BaseEntity;
import entity.Booking;
import exception.InvalidBookingTimeException;
import org.springframework.stereotype.Service;
import repository.BookingRepository;
import service.BookingService;
import java.time.LocalDateTime;
import java.util.List;

public class bookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository ;

    public bookingServiceImpl(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }
    private void validateTimeRange(Booking booking) {
        if(!booking.getStartTime().isBefore( booking.getEndTime()) ){
            throw new InvalidBookingTimeException("Start time must be before end time");
        }}
    @Override
    public Booking createBooking(Booking booking){
        validateTimeRange(booking);
        return bookingRepository.save(booking);
    }

    @Override
    public Booking cancelBooking(Booking booking) {
        return null;
    }

    @Override
    public List<Booking> findBookingsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return null;
    }

}
