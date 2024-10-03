package nl.han.devops.dto;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name="users")
public class UserDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String password_hash;

    @Getter
    @Setter
    private String token;

    @Getter
    private int role_id;
}
