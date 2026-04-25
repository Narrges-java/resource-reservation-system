package controller;
import dto.BookingRequestDto;
import dto.BookingResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.BookingService;

import java.util.List;

@RestController
@RequestMapping("/api/booking")
public class BookingController {
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // POST /bookings
    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking (@RequestBody BookingRequestDto bookingRequestDto){
        BookingResponseDto createdBookingResponse = bookingService.createBooking(bookingRequestDto);
        return new ResponseEntity<>(createdBookingResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}/cancel")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable (name = "id") long id){
       BookingResponseDto canceledBooking = bookingService.cancelBooking(id);
        return new ResponseEntity<>(canceledBooking, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BookingResponseDto>> findBookingsByDateRange (@RequestParam("start") String start,@RequestParam("end") String end ){
        java.time.LocalDateTime startDateTime = java.time.LocalDateTime.parse(start);
        java.time.LocalDateTime endDateTime = java.time.LocalDateTime.parse(end);

        List<BookingResponseDto> booking = bookingService.findBookingsByDateRange(startDateTime,endDateTime);
        return new ResponseEntity<>(booking,HttpStatus.OK);

    }

}