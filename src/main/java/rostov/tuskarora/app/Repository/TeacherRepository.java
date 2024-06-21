package rostov.tuskarora.app.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rostov.tuskarora.app.Model.Teacher;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    @Transactional
    @Modifying
    @Query("update Teacher t set t.username = ?1, t.email = ?2, t.password = ?3, t.post = ?4, t.role = ?5 where t.id = ?6")
    void updateTeacherById(String fullName, String email, String password, String post, String role, long id);
    @Transactional
    void deleteTeacherById(long id);
}
