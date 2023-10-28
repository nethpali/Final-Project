package lk.ijse.Roosalu.dto.tm;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderTM {
    private String order_id;
    private String date;
    private String agent_id;
    private String product_id;
    private Double unit_price;
    private int quantity;
    private double total;
}
