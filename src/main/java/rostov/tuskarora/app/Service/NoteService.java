package rostov.tuskarora.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rostov.tuskarora.app.Main;
import rostov.tuskarora.app.Model.Note;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Model.Teacher;
import rostov.tuskarora.app.Repository.NoteRepository;
import rostov.tuskarora.app.Repository.StudentRepository;

import java.util.List;

@Service
public class NoteService {

    @Autowired
    NoteRepository repository;
    @Autowired
    StudentRepository studentRepository;

    public List<Note> findByTeacher() {
        return repository.findByTeacher(Main.currentTeacher).orElseThrow();
    }
    public List<Note> findByStudent(String studentName) {
        Student student = studentRepository.findByFullName(studentName).orElseThrow();
        return repository.findByStudent(student).orElseThrow();
    }
    public void updateNoteById(Note newNote, long id) {
        Note note = repository.findById(id).orElseThrow();

        if ((!(newNote.getWeight() == null || newNote.getWeight().isEmpty()))) note.setWeight(newNote.getWeight());

        if ((!(newNote.getFeedbackText() == null || newNote.getFeedbackText().isEmpty()))) note.setFeedbackText(newNote.getFeedbackText());

        repository.updateNoteById(note.getWeight(), note.getFeedbackText(), note.getTeacher(), note.getStudent(), id);
    }
    public void deleteNoteById(long id) {
        repository.deleteNoteById(id);
    }
    public void deleteNoteByTeacher(Teacher teacher) {
        repository.deleteNotesByTeacher(teacher);
    }
    public void deleteNoteByStudent(Student student) {
        repository.deleteNotesByStudent(student);
    }
}
