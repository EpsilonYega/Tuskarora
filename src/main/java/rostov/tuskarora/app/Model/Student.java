package rostov.tuskarora.app.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String fullName;
    @Column
    private String studentGroup;
    @Column
    private int greenCount = 0;
    @Column
    private int yellowCount = 0;
    @Column
    private int redCount = 0;
}
