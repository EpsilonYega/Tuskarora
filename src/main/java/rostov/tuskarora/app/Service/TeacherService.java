package rostov.tuskarora.app.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import rostov.tuskarora.app.Model.Teacher;
import rostov.tuskarora.app.Repository.TeacherRepository;

import java.util.Optional;

@Service
public class TeacherService implements UserDetailsService {
    @Autowired
    private TeacherRepository repository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Teacher> user = repository.findByEmail(username);
        if (user.isPresent()){
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(getRoles(userObj))
                    .build();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }
    private String[] getRoles(Teacher appUser){
        if (appUser.getRole() == null) {
            return new String[]{"USER"};
        }
        return appUser.getRole().split(",");
    }
    public void updateTeacherById(Teacher newData, long id) {

        Teacher teacher = repository.findById(id).orElseThrow();

        if ((!(newData.getUsername() == null || newData.getUsername().isEmpty()))) teacher.setUsername(newData.getUsername());

        if ((!(newData.getEmail() == null || newData.getEmail().isEmpty()))) teacher.setEmail(newData.getEmail());

        if ((!(newData.getPassword() == null || newData.getPassword().isEmpty()))) teacher.setPassword(newData.getPassword());

        if ((!(newData.getPost() == null || newData.getPost().isEmpty()))) teacher.setPost(newData.getPost());

        if ((!(newData.getRole() == null || newData.getRole().isEmpty()))) teacher.setRole(newData.getRole());

        repository.updateTeacherById(teacher.getUsername(), teacher.getEmail(), teacher.getPassword(), teacher.getPost(), teacher.getRole(), id);

    }

    public void deleteTeacherById(long id) {
        repository.deleteTeacherById(id);
    }
}
