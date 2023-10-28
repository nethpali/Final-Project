package lk.ijse.Roosalu.dto.tm;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AssetsTM {
    private String id;
    private String name;
    private String description;
    private int quantity;
    private double value;
}
