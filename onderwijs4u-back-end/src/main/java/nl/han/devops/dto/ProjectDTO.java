package nl.han.devops.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="projects")
public class ProjectDTO {
    @Id
    private int project_id;
    private int student_id;
    private String name;
    private String description;
    private int period;
    private String created_at;
    private String updated_at;
}
