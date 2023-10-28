package lk.ijse.Roosalu.dto;

import lombok.*;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Setter
public class User {
    private String user_id;
    private String password;
    private String re_enter_password;
    private String user_name;
    private String user_email;
}
