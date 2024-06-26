package code.TeacherSlots;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TimeSlotRange> availableSlots = new ArrayList<>();

    // Constructor, getters, and setters...

    public Slot() {
    }

    public Slot(LocalDate date, List<TimeSlotRange> availableSlots) {
        this.date = date;
        this.availableSlots = availableSlots;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TimeSlotRange> getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(List<TimeSlotRange> availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", date=" + date +
                ", availableSlots=" + availableSlots +
                '}';
    }
}