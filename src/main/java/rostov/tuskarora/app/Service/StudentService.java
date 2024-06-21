package rostov.tuskarora.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository repository;

    public List<Student> findAll() {
        return repository.findAll();
    }

    public Student findByFullName(String fullName) {
        return repository.findByFullName(fullName).orElseThrow();
    }

    public List<Student> findByGroup(String group) {
        return repository.findByStudentGroup(group).orElseThrow();
    }

    public void updateStudentById(Student newStudent, long id) {

        Student student = repository.findById(id).orElseThrow();

        if ((!(newStudent.getFullName() == null || newStudent.getFullName().isEmpty()))) student.setFullName(newStudent.getFullName());

        if ((!(newStudent.getStudentGroup() == null || newStudent.getStudentGroup().isEmpty()))) student.setStudentGroup(newStudent.getStudentGroup());

        student.setGreenCount(newStudent.getGreenCount());

        student.setYellowCount(newStudent.getYellowCount());

        student.setRedCount(newStudent.getRedCount());

        repository.updateStudentById(student.getFullName(), student.getStudentGroup(), student.getGreenCount(), student.getYellowCount(), student.getRedCount(),  id);
    }
    public void deleteStudentById(long id) {
        repository.deleteStudentById(id);
    }
}
