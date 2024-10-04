package nl.han.devops.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="crebos")
public class CrebosDTO {
    @Id
    private String crebonumber;

    @Getter
    @Setter
    private String description;

    @Getter
    @Setter
    private String created_at;

    @Getter
    @Setter
    private String updated_at;
}
