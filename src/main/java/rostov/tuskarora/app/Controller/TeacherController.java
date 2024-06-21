package rostov.tuskarora.app.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rostov.tuskarora.app.Config.JwtCore;
import rostov.tuskarora.app.DTO.SigninRequest;
import rostov.tuskarora.app.Main;
import rostov.tuskarora.app.Model.Note;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Model.Teacher;
import rostov.tuskarora.app.Repository.TeacherRepository;
import rostov.tuskarora.app.Service.NoteService;
import rostov.tuskarora.app.Service.StudentService;
import rostov.tuskarora.app.Service.TeacherService;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class TeacherController {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private NoteService noteService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtCore jwtCore;

    @PostMapping("/register/teacher")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> register(@RequestBody Teacher teacher) {

        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        Optional<Teacher> teacherExists = teacherRepository.findByEmail(teacher.getEmail());

        if (teacherExists.isEmpty()) return ResponseEntity.ok(teacherRepository.save(teacher));

        return ResponseEntity.ok("Такой пользователь уже существует");
    }

    @PostMapping("/login")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> login(@RequestBody SigninRequest signinRequest) {

        if (teacherRepository.findByEmail(signinRequest.getEmail()).isPresent()) {

            Teacher builtTeacher = teacherRepository.findByEmail(signinRequest.getEmail()).get();
            builtTeacher.setPassword(passwordEncoder.encode(builtTeacher.getPassword()));

            String jwt = jwtCore.generateToken(builtTeacher);
            Main.currentTeacher = builtTeacher;

            return ResponseEntity.ok(jwt);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/authorized/logout")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> logout() {
        Main.currentTeacher = null;
        return ResponseEntity.ok().build();
    }

    @PostMapping("/authorized/teacher/updateInfo")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> updateInfo(@RequestBody Teacher teacher) {

        try {
            if ((!(teacher.getPassword() == null || teacher.getPassword().isEmpty()))) teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
            teacherService.updateTeacherById(teacher, Main.currentTeacher.getId());
            log.info("Успешное обновление данных пользователя " + Main.currentTeacher.getUsername() + ".");
            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при обновлении данных");
        }

    }

    @PostMapping("/authorized/teacher/deleteAccount")
    @CrossOrigin(origins = "http://localhost:5173")
    public ResponseEntity<?> deleteAccount() {

        try {
            List<Note> notes = noteService.findByTeacher();
            for (Note note : notes) {
                Student student = note.getStudent();
                    switch (note.getWeight()) {
                        case "Замечание":
                            if(student.getGreenCount() > 0) student.setGreenCount(student.getGreenCount() - 1);
                            studentService.updateStudentById(student, note.getStudent().getId());
                            break;
                        case "Предупреждение":
                            if(student.getYellowCount() > 0) student.setYellowCount(student.getYellowCount() - 1);
                            studentService.updateStudentById(student, note.getStudent().getId());
                            break;
                        case "Выговор":
                            if(student.getRedCount() > 0) student.setRedCount(student.getRedCount() - 1);
                            studentService.updateStudentById(student, note.getStudent().getId());
                            break;
                        default:
                            break;
                    }
            }
            noteService.deleteNoteByTeacher(Main.currentTeacher);
            log.info("Все записи пользователя " + Main.currentTeacher.getUsername() + " были удалены.");
            teacherService.deleteTeacherById(Main.currentTeacher.getId());
            log.info("Успешное удаление пользователя " + Main.currentTeacher.getUsername() + ".");
            Main.currentTeacher = null;

            return ResponseEntity.ok().build();
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.ok("Возникла ошибка при удалении данных");
        }
    }
}
