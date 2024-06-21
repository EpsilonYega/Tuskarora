import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import rostov.tuskarora.app.Main;
import rostov.tuskarora.app.Model.Note;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Model.Teacher;
import rostov.tuskarora.app.Repository.NoteRepository;
import java.util.List;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Main.class)
public class JpaTest {
    @Autowired
    private NoteRepository noteRepository;
    private Note note;
    private Teacher teacher;
    private Student student;
    @BeforeEach
    public void init() {
        note = new Note();
        teacher = new Teacher();
        student = new Student();
    }
    @Test
    public void testCreate() {
        noteRepository.save(note);
    }
    @Test
    public void testRead() {
        testCreate();
        noteRepository.findAll();
    }
    @Test
    public void testFindByTeacher() {
        note.setTeacher(teacher);
        noteRepository.save(note);
        Optional<List<Note>> notes = noteRepository.findByTeacher(teacher);
        assertEquals(1, notes.get().size());
        assertEquals(note, notes.get().get(0));
    }
    @Test
    public void testFindByStudent() {
        note.setStudent(student);
        noteRepository.save(note);
        Optional<List<Note>> notes = noteRepository.findByStudent(student);
        assertEquals(1, notes.get().size());
        assertEquals(note, notes.get().get(0));
    }
    @Test
    public void testUpdate() {
        noteRepository.save(note);
        noteRepository.updateNoteById("Новый столбец", "Новая заметка", teacher, student, note.getId());
        Note updatedNote = noteRepository.findById(note.getId()).get();
        assertEquals("Новый столбец", updatedNote.getWeight());
        assertEquals("Новая заметка", updatedNote.getFeedbackText());
    }
    @Test
    public void testDelete() {
        noteRepository.save(note);
        noteRepository.deleteNoteById(note.getId());
        assertEquals(0, noteRepository.findAll().size());
    }
    @Test
    public void testDeleteNotesByTeacher() {
        note.setTeacher(teacher);
        noteRepository.save(note);
        noteRepository.deleteNotesByTeacher(teacher);
        assertEquals(0, noteRepository.findAll().size());
    }
    @Test
    public void testDeleteNotesByStudent() {
        note.setStudent(student);
        noteRepository.save(note);
        noteRepository.deleteNotesByStudent(student);
        assertEquals(0, noteRepository.findAll().size());
    }
}
