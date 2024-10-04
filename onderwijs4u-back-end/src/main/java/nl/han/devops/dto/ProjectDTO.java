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
    private int projectid;
    @Getter
    @Setter
    private int studentid;
    private String name;
    private String description;
    private int period;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
}
