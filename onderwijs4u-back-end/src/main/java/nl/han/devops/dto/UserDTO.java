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
    private int userid;

    @Getter
    @Setter
    private String email;

    @Getter
    @Setter
    private String passwordhash;

    @Getter
    @Setter
    private String token;

    @Getter
    private int roleid;
}
