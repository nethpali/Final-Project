package lk.ijse.Roosalu.model;

import lk.ijse.Roosalu.CrudUtil.CrudUtil;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserModel {
    public static List<User> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM user";

        List<User> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new User(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5)

            ));
        }
        return data;
    }

    public static User search(String userid) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM user WHERE user_Id='" + userid + "'");
        System.out.println(rst);
        if (rst.next()) {
            return new User(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5));
        }
        return null;
    }

    public static ArrayList<User> View() throws SQLException {
        ArrayList<User> userArrayList = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM user");


        while (rst.next()) {
            userArrayList.add(
                    new User(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5))
            );
        }
        return userArrayList;
    }
}
