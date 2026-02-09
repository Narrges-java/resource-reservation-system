package entity;

import entity.enums.BookingType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(EnumType.STRING)
    private BookingType status;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Resource resource;

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(BookingType status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public BookingType getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public Resource getResource() {
        return resource;
    }
}
