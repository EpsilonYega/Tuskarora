package rostov.tuskarora.app.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rostov.tuskarora.app.Main;
import rostov.tuskarora.app.Model.Note;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Repository.NoteRepository;
import rostov.tuskarora.app.Service.NoteService;
import rostov.tuskarora.app.Service.StudentService;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class NoteController {
    @Autowired
    private NoteRepository repository;
    @Autowired
    private NoteService service;
    @Autowired
    private StudentService studentService;

    @GetMapping("/authorized/findAllNotesByTeacher")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Note>> findAllNotesByTeacher() {
        return ResponseEntity.ok(service.findByTeacher());
    }

    @GetMapping("/authorized/findAllNotesByStudent/{studentName}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<List<Note>> findAllNotesByStudent(@PathVariable("studentName") String studentName) {
        return ResponseEntity.ok(service.findByStudent(studentName));
    }

    @PostMapping("/authorized/note/new")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> newNote(@RequestBody Note note) {
        note.setTeacher(Main.currentTeacher);
        try {
            Student newStudent = note.getStudent();
            switch (note.getWeight()) {
                case "Замечание":
                    newStudent.setGreenCount(newStudent.getGreenCount() + 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                case "Предупреждение":
                    newStudent.setYellowCount(newStudent.getYellowCount() + 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                case "Выговор":
                    newStudent.setRedCount(newStudent.getRedCount() + 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                default:
                    break;
            }
            repository.save(note);
            log.info("Успешное обновление записи.");
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при обновлении данных записи");
        }
    }

    @PostMapping("/authorized/note/update/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updateNote(@PathVariable("id") long id, @RequestBody Note note) {
        try {
            Note prevNote = repository.findById(id).get();
            log.info("Нашлась нота сложности: " + prevNote.getWeight() + " и контентом: " + prevNote.getFeedbackText());
            log.info("С фронта прилетела нота сложности: " + note.getWeight() + " и контентом: " + note.getFeedbackText());
            String prevWeight = prevNote.getWeight();
            Student student = note.getStudent();

            switch (prevWeight) {
                case "Замечание":
                    student.setGreenCount(student.getGreenCount() - 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                case "Предупреждение":

                    student.setYellowCount(student.getYellowCount() - 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                case "Выговор":
                    student.setRedCount(student.getRedCount() - 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                default:
                    break;
            }

            switch (note.getWeight()) {
                case "Замечание":
                    student.setGreenCount(student.getGreenCount() + 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                case "Предупреждение":
                    student.setYellowCount(student.getYellowCount() + 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                case "Выговор":
                    student.setRedCount(student.getRedCount() + 1);
                    studentService.updateStudentById(student, note.getStudent().getId());
                    break;
                default:
                    break;
            }
            service.updateNoteById(note, id);
            log.info("Успешное обновление записи.");
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при обновлении данных записи");
        }
    }

    @PostMapping("/authorized/note/delete/{id}")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteNote(@PathVariable("id") long id) {

        try {
            Note note = repository.findById(id).get();
            Student newStudent = note.getStudent();
            switch (note.getWeight()) {
                case "Замечание":
                    if(newStudent.getGreenCount() > 0) newStudent.setGreenCount(newStudent.getGreenCount() - 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                case "Предупреждение":
                    if(newStudent.getYellowCount() > 0) newStudent.setYellowCount(newStudent.getYellowCount() - 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                case "Выговор":
                    if(newStudent.getRedCount() > 0) newStudent.setRedCount(newStudent.getRedCount() - 1);
                    studentService.updateStudentById(newStudent, note.getStudent().getId());
                    break;
                default:
                    break;
            }
            service.deleteNoteById(id);
            log.info("Запись была удалена.");

            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при удалении данных записи");
        }

    }
}
