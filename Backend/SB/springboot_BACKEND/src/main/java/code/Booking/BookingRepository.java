package code.Booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

    Optional<Booking> findBookingByID(int id);
}
