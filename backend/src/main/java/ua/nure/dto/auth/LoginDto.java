package ua.nure.dto.auth;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class LoginDto {

    private String email;
    private String password;

}
