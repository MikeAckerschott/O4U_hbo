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
@Table(name="crebos")

public class CrebosDTO {
    @Id
    private String crebo_number;
    private String description;
    private String created_at;
    private String updated_at;
}
