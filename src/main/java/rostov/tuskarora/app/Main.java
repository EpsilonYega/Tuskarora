package rostov.tuskarora.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import rostov.tuskarora.app.Model.Teacher;

@SpringBootApplication
public class Main {
    public static Teacher currentTeacher;
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
    }
}
