package rostov.tuskarora.app.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rostov.tuskarora.app.Model.Note;
import rostov.tuskarora.app.Model.Student;
import rostov.tuskarora.app.Model.Teacher;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    @Transactional
    @Modifying
    @Query("select n from Note n where n.teacher = ?1")
    Optional<List<Note>> findByTeacher(Teacher teacher);

    @Transactional
    @Modifying
    @Query("select n from Note n where n.student = ?1")
    Optional<List<Note>> findByStudent(Student student);

    @Transactional
    @Modifying
    @Query("update Note n set n.weight = ?1, n.feedbackText = ?2, n.teacher = ?3, n.student = ?4 where n.id = ?5")
    void updateNoteById(String weight, String feedbackText, Teacher teacher, Student student, long id);

    @Transactional
    void deleteNoteById(long id);

    @Transactional
    @Modifying
    @Query("delete Note n where n.teacher = ?1")
    void deleteNotesByTeacher(Teacher teacher);

    @Transactional
    @Modifying
    @Query("delete Note n where n.student = ?1")
    void deleteNotesByStudent(Student student);
}
