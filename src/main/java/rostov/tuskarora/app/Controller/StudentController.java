package rostov.tuskarora.app.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rostov.tuskarora.app.Main;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Model.Teacher;
import rostov.tuskarora.app.Repository.StudentRepository;
import rostov.tuskarora.app.Service.NoteService;
import rostov.tuskarora.app.Service.StudentService;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentService studentService;
    @Autowired
    private NoteService noteService;

    @GetMapping("/authorized/findAllStudents")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Student>> findAllStudents() {
        return ResponseEntity.ok(studentService.findAll());
    }

    @GetMapping("/authorized/findByFullName/{fullName}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<Student> findAllStudentsByFullName(@PathVariable("fullName") String fullName) {
        return ResponseEntity.ok(studentService.findByFullName(fullName));
    }

    @GetMapping("/authorized/findAllByGroup/{group}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Student>> findAllStudentsByGroup(@PathVariable("group") String group) {
        return ResponseEntity.ok(studentService.findByGroup(group));
    }

    @PostMapping("/authorized/student/new")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> addStudent(@RequestBody Student student) {

        try {
            studentRepository.save(student);
            log.info("Успешное добавление студента " + student.getFullName() + ".");
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при добавлении студента");
        }

    }

    @PostMapping("/authorized/student/update/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updateStudentInfo(@PathVariable("id") long id, @RequestBody Student student) {

        try {
            studentService.updateStudentById(student, id);
            log.info("Успешное обновление данных студента " + student.getFullName() + ".");
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при обновлении данных студента");
        }

    }

    @PostMapping("/authorized/student/delete/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") long id) {

        try {
            noteService.deleteNoteByStudent(studentRepository.findById(id).get());
            log.info("Все записи со студентом " + Main.currentTeacher.getUsername() + " были удалены.");
            studentService.deleteStudentById(id);
            log.info("Успешное удаление студента " + Main.currentTeacher.getUsername() + ".");
            Main.currentTeacher = null;

            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при удалении данных студента");
        }

    }
}
