package fit.fancyday.model.user.dtos;

import lombok.Data;

@Data
public class RegisterDto {
    private String name;
    private String phone;
    private String password;
}
