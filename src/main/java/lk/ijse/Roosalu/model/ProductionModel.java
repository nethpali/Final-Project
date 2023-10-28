package lk.ijse.Roosalu.model;

import lk.ijse.Roosalu.CrudUtil.CrudUtil;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Agent;
import lk.ijse.Roosalu.dto.Production;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductionModel {
    public static List<Production> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM product";

        List<Production> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Production(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getInt(3),
                    resultSet.getDouble(4)
            ));
        }
        return data;
    }

    public static Production search(String ProductId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM product WHERE product_Id='" + ProductId + "'");
        System.out.println(rst);
        if (rst.next()) {
            return new Production(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4));
        }
        return null;
    }

    public static ArrayList<Production> View() throws SQLException {
        ArrayList<Production> productionArrayList = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM product");


        while (rst.next()) {
            productionArrayList.add(
                    new Production(rst.getString(1), rst.getString(2), rst.getInt(3), rst.getDouble(4))
            );
        }
        return productionArrayList;
    }
    }
