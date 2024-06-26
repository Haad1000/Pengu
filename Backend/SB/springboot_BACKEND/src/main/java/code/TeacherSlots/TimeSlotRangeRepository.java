package code.TeacherSlots;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TimeSlotRangeRepository extends JpaRepository<TimeSlotRange, Long> {

    // ... existing methods ...

    @Query("SELECT ts FROM TimeSlotRange ts WHERE ts.teacher.id = :teacherId AND ts.isAvailable = true")
    List<TimeSlotRange> findAvailableSlotsByTeacher(@Param("teacherId") Integer teacherId);

    @Query("SELECT ts FROM TimeSlotRange ts JOIN FETCH ts.teacher")
    List<TimeSlotRange> findAllWithTeacher();
}

