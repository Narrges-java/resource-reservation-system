package service.impl;

import dto.BookingRequestDto;
import dto.BookingResponseDto;
import entity.Booking;
import entity.Resource;
import entity.enums.BookingType;
import exception.*;
import org.modelmapper.ModelMapper;
import repository.BookingRepository;
import repository.ResourceRepository;
import service.BookingService;
import java.time.LocalDateTime;
import java.util.List;

public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository ;
    private final ResourceRepository resourceRepository;
    private ModelMapper mapper;



    public BookingServiceImpl(BookingRepository bookingRepository, ResourceRepository resourceRepository ,ModelMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.resourceRepository = resourceRepository;
        this.mapper = mapper;
    }

    /// mapping requestEntity to requestDTO
    public BookingRequestDto mapToRequestDto (Booking booking){
        BookingRequestDto bookingRequestDto = mapper.map(booking, BookingRequestDto.class);
        if (booking.getResource() != null){
            bookingRequestDto.setResourceId(booking.getResource().getId());
        }else {
            throw new RuntimeException("Resource nonexistence  please try again!");
        }
        return bookingRequestDto;
    }

    /// mapping requestDTO to requestEntity
    public Booking mapToRequest (BookingRequestDto bookingRequestDto){
        Booking booking = mapper.map(bookingRequestDto, Booking.class);
        Resource resource = resourceRepository.findById(bookingRequestDto.getResourceId())
                .orElseThrow(() -> new RuntimeException("Resource not found"));
        booking.setResource(resource);
        return booking;
    }

    /// mapping responseEntity to responseDTO
    public BookingResponseDto mapToResponseDto (Booking booking){
        BookingResponseDto bookingResponseDto = mapper.map(booking, BookingResponseDto.class);
        if (booking.getResource() != null){
            bookingResponseDto.setResourceName(booking.getResource().getName());
        }else {
            throw new RuntimeException("ResourceName nonexistence please try again!");
        }
        return bookingResponseDto;
    }

    /// mapping responseDto to responseEntity
    public Booking mapToResponse(BookingResponseDto bookingResponseDto){
        Booking booking =mapper.map(bookingResponseDto, Booking.class);
        if (bookingResponseDto.getResourceName() != null && !bookingResponseDto.getResourceName().isEmpty()){
            Resource resource = resourceRepository.findByName(bookingResponseDto.getResourceName())
                    .orElseThrow(() -> new RuntimeException("ResourceName not true"));
            booking.setResource(resource);
        }
        return booking;
    }
//-------------------------------------------------------------------------------------------------------------------------------//

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


//-----------------------------------------------------------------------------------------------------------------------------//
    // create Booking
    @Override
    public BookingResponseDto createBooking(BookingRequestDto bookingRequestDto){
        Booking booking = mapToRequest(bookingRequestDto);
        validateTimeRange(booking);
        Resource resource = validateResourceExists(booking);
        booking.setResource(resource);
        checkActivate(booking.getResource());
        checkOverlap(booking);
        booking.setStatus(BookingType.RESERVED);
        Booking saveBooking = bookingRepository.save(booking);
        return mapToResponseDto(saveBooking);
    }

    // اینجا میایم چک میکنیم که رزروی که میخوایم کنسل کنیم وجود داره یا نه و اونرو کنسل میکنیم
    @Override
    public BookingResponseDto cancelBooking(Long bookingId) {
       Booking booking= bookingRepository.findById(bookingId).orElseThrow(
               ()-> new BookingNotFoundException("booking not found please try again! "));
        if (booking.getStatus() != BookingType.RESERVED){
            throw new InvalidBookingStateException("this booking is cancel before you do this!");
        }
        booking.setStatus(BookingType.CANCELLED);
        Booking cancelBooking = bookingRepository.save(booking);
        return  mapToResponseDto(cancelBooking);
    }


    @Override
    public List<BookingResponseDto> findBookingsByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        if (!startTime.isBefore(endTime)){
            throw new InvalidBookingTimeException("this is not correct for finding");
        }
        List<Booking> bookings =bookingRepository.findBookingsByDateRange(startTime,endTime);
        List<BookingResponseDto> bookingResponseDtos  = new java.util.ArrayList<>();

        for (Booking booking :bookings){
            bookingResponseDtos.add(mapToResponseDto(booking));
        }
        return bookingResponseDtos;
    }

}
