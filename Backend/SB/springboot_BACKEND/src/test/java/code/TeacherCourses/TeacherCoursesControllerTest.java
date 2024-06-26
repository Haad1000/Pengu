package code.TeacherCourses;

import code.Subject.Subject;
import code.Subject.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TeacherCoursesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubjectRepository subjectRepository;

    @MockBean
    private TeacherRepository teacherRepository;

    private Teacher teacher;
    private Subject subject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        teacher = new Teacher(); // Assuming constructor initialization or setters
        subject = new Subject(); // Assuming constructor initialization or setters
        subject.setSubjectID(1);

        when(teacherRepository.getOne(anyInt())).thenReturn(teacher);
        when(subjectRepository.findBySubjectID(anyInt())).thenReturn(subject);
        when(subjectRepository.findById(anyInt())).thenReturn(Optional.of(subject));
        when(teacherRepository.findById(anyInt())).thenReturn(Optional.of(teacher));
    }



    @Test
    void updateTeacherSubjectsSuccess() throws Exception {
        mockMvc.perform(put("/subjects/{teach}/{subId}", teacher.getId(), subject.getSubjectID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Teacher's Subjects were successfully updated"));

        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void addTeachingSubjectSuccess() throws Exception {
        mockMvc.perform(post("/addTeachings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1, \"subjectRequest\":{\"subjectID\":1}}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Subject added successfully to the teacher's courses."));

        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void deletingTeacherCoursesSuccess() throws Exception {
        mockMvc.perform(delete("/delete/{teach}/{subId}", teacher.getId(), subject.getSubjectID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Subject was successfully deleted from teacher's course list"));

        verify(teacherRepository, times(1)).save(any(Teacher.class));
        verify(subjectRepository, times(1)).save(any(Subject.class));
    }

    @Test
    void deleteAllTeachersSuccess() throws Exception {
        when(teacherRepository.findAll()).thenReturn(Arrays.asList(new Teacher(), new Teacher()));
        mockMvc.perform(delete("/deleteAllTeachers"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));

        verify(teacherRepository, times(1)).deleteAll();
    }

    // Additional test cases can be added to cover negative scenarios and validations.
}