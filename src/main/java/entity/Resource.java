package entity;

import entity.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "\"resource\"")
public class Resource extends BaseEntity {
    private String name;
    @Enumerated(EnumType.STRING)
    private ResourceType type;
    private Integer capacity;
    private boolean active =true;

    @OneToMany(mappedBy = "resource")
    private List<Booking> bookings = new ArrayList<>();


    public void setName(String name) {
        this.name = name;
    }

    public void setType(ResourceType type) {
        this.type = type;
   }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public String getName() {
        return name;
    }

    public ResourceType getType() {
        return type;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public boolean isActive() {
        return active;
    }

    public List<Booking> getBookings() {
        return bookings;
    }
}
