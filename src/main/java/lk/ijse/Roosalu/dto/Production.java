package lk.ijse.Roosalu.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Production {
    private String production_id;
    private String date;
    private int quantity;
    private double unit_price;
}
