package code.TeacherSlots;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    // Query methods...
}