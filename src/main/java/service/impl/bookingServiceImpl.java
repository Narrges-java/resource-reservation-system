package service.impl;

import entity.BaseEntity;
import entity.Booking;
import entity.enums.BookingType;
import exception.BookingConflictException;
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

    // check TimeRange and compare is possible or not
    private void validateTimeRange(Booking booking) {
        if(!booking.getStartTime().isBefore( booking.getEndTime()) ){
            throw new InvalidBookingTimeException("Start time must be before end time");
        }}

    // check overlap new booking with the oder booking(Reserved booking)
    private void checkOverlap(Booking booking){
        List<Booking> overlapping = bookingRepository.findOverlappingBooking(
                booking.getResource().getId(),
                BookingType.RESERVED,
                booking.getStartTime(),
                booking.getEndTime()
        );
        if (!overlapping.isEmpty()){
            throw new BookingConflictException("Booking overlaps with existing reservations!");
        }
    }

    // create Booking
    @Override
    public Booking createBooking(Booking booking){
        validateTimeRange(booking);
        checkOverlap(booking);
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
