package lk.ijse.Roosalu.dto.tm;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class ProductionTM {
    private String production_id;
    private String date;
    private int quantity;
    private double unit_price;
}
