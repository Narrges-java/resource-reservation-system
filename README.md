# resource-reservation-system

## Features
- 📌 Manage resources (e.g. rooms, equipment, etc.)
- 👤 Manage users (resource consumers)
- 📅 Create and cancel bookings for resources
- ✅ Time range validation for bookings
- ⚠️ Conflict detection (avoid overlapping reservations)
- 🚫 Prevent booking on inactive resources
- 🎯 Clear separation of layers (Controller, Service, Repository, Entity, DTO)

## Tech Stack
- **Language:** Java  
- **Framework:** Spring Boot  
- **Architecture:** Layered (Controller → Service → Repository → Entity)  
- **Mapping:** ModelMapper (Entity ↔ DTO)  
- **Persistence:** Spring Data JPA  
- **Build Tool:** Maven
- **Database:** H2 (in-memory)
