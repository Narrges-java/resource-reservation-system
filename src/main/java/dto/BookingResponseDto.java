package dto;

import entity.enums.BookingType;
import lombok.Data;

import java.time.LocalDateTime;

public class BookingResponseDto {

    private Long id;
    private String ResourceName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BookingType status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String resourceName) {
        ResourceName = resourceName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public BookingType getStatus() {
        return status;
    }

    public void setStatus(BookingType status) {
        this.status = status;
    }
}
