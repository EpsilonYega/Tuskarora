package rostov.tuskarora.app.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rostov.tuskarora.app.Model.Student;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByFullName(String fullName);
    Optional<List<Student>> findByStudentGroup(String studentGroup);
    @Transactional
    @Modifying
    @Query("update Student s set s.fullName = ?1, s.studentGroup = ?2, s.greenCount = ?3, s.yellowCount = ?4, s.redCount = ?5 where s.id = ?6")
    void updateStudentById(String fullName, String group, int greenCount, int yellowCount, int redCount, long id);
    @Transactional
    void deleteStudentById(long id);
}
