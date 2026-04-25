package service;

import dto.BookingRequestDto;
import dto.BookingResponseDto;
import entity.Booking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface BookingService {

    // create Booking
    BookingResponseDto createBooking(BookingRequestDto bookingRequestDto );

    BookingResponseDto cancelBooking(Long bookingId);

    List<BookingResponseDto> findBookingsByDateRange(LocalDateTime startTime,LocalDateTime endTime);


}
