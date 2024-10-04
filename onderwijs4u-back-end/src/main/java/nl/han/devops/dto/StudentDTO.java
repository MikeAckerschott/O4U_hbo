package nl.han.devops.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="students")
public class StudentDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int student_id;
    private int user_id;
    private String student_number;
    private String first_name;
    private String tussenvoegsel;
    private String last_name;
    private String klas;
    private String cohort;
    private String crebo_number;
}
