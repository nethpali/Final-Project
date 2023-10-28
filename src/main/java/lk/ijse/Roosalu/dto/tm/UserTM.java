package lk.ijse.Roosalu.dto.tm;

import lombok.*;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Setter
public class UserTM {
    private String user_id;
    private String password;
    private String re_enter_password;
    private String user_name;
    private String user_email;
}
