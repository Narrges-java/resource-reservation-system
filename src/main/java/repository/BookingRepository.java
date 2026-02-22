package repository;

import entity.Booking;
import entity.enums.BookingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {

    @Query("""
          SELECT b FROM Booking b
          WHERE b.resource.id = :resourceId
          AND b.status = :status
          AND b.startTime < :endTime
          AND b.endTime > :startTime
""")
    List<Booking> findOverlappingBooking(
            @Param("resourceId") Long resourceId,
            @Param("status") BookingType status,
            @Param("startTime")LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
            );

}
