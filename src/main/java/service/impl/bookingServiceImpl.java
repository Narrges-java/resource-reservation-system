package service.impl;

import entity.BaseEntity;
import entity.Booking;
import entity.Resource;
import entity.enums.BookingType;
import exception.*;
import org.springframework.stereotype.Service;
import repository.BookingRepository;
import repository.ResourceRepository;
import service.BookingService;
import java.time.LocalDateTime;
import java.util.List;

public class bookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository ;
    private final ResourceRepository resourceRepository;


    public bookingServiceImpl(BookingRepository bookingRepository, ResourceRepository resourceRepository) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
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

    //checking Activate Resource is false or true
    private void checkActivate (Resource resource){
        if (!resource.isActive()){
            throw new ResourceInactiveException("Resource is inactive and cannot be booked");
        }
    }

    private Resource validateResourceExists(Booking booking) {

        if (booking.getResource() == null) {
            throw new IllegalArgumentException("Resource must not be null");
        }
        Long resourceId = booking.getResource().getId();
        return resourceRepository.findById(resourceId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resource not found"));
    }




    // create Booking
    @Override
    public Booking createBooking(Booking booking){
        validateTimeRange(booking);
        Resource resource = validateResourceExists(booking);
        booking.setResource(resource);
        checkActivate(resource);
        checkOverlap(booking);
        booking.setStatus(BookingType.RESERVED);
        return bookingRepository.save(booking);
    }

    // اینجا میایم چک میکنیم که رزروی که میخوایم کنسل کنیم وجود داره یا نه و اونرو کنسل میکنیم
    @Override
    public Booking cancelBooking(Long bookingId) {
       Booking booking= bookingRepository.findById(bookingId).orElseThrow(()-> new BookingNotFoundException("booking not found please try again! "));
        if (booking.getStatus() != BookingType.RESERVED){
            throw new InvalidBookingStateException("this booking is cancel before you do this!");
        }
        booking.setStatus(BookingType.CANCELLED);
        return  bookingRepository.save(booking);
    }


    @Override
    public List<Booking> findBookingsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)){
            throw new InvalidBookingTimeException("this is not correct for finding");
        }
        return bookingRepository.findBookingsByDateRange(startTime,endTime);
    }

}
