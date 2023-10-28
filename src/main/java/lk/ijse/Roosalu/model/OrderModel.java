package lk.ijse.Roosalu.model;

import lk.ijse.Roosalu.CrudUtil.CrudUtil;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderModel {
    public static List<Order> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM orders";

        List<Order> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Order(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDouble(5),
                    resultSet.getInt(6),
                    resultSet.getDouble(7)

            ));
        }
        return data;
    }

    public static ArrayList<Order> View() throws SQLException, ClassNotFoundException {
        ArrayList<Order> orderView = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM `orders`");

        while (rst.next()) {
            orderView.add(
                    new Order(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getInt(6),rst.getDouble(7) )
            );

        }
        return orderView;
    }

    public static Order search(String orderId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM orders WHERE order_id='" + orderId + "'");
        System.out.println(rst);
        if (rst.next()) {
            return new Order(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getDouble(5), rst.getInt(6),rst.getDouble(7));
        }
        return null;
    }

    public static boolean update(Order order) throws SQLException, ClassNotFoundException {
        boolean i = CrudUtil.execute("UPDATE orders set description=? where order_id=?",
                order.getOrder_id()
        );
        System.out.println(i);
        return i;
    }
}
