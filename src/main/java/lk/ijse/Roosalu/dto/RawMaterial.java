package lk.ijse.Roosalu.dto;

import lombok.*;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
@Getter
public class RawMaterial {
 private String raw_material_id;
 private String type;
 private int quantity;
 private double unit_price;
 private String agent_id;
}
