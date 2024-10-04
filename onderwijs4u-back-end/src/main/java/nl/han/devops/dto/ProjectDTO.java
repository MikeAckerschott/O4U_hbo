package nl.han.devops.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="projects")
public class ProjectDTO {
    @Id
    private int project_id;
    @Getter
    @Setter
    private int student_id;
    private String name;
    private String description;
    private int period;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
