package lk.ijse.Roosalu.model;

import lk.ijse.Roosalu.CrudUtil.CrudUtil;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Agent;
import lk.ijse.Roosalu.dto.Supplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    public static boolean add(Supplier supplier) throws SQLException, ClassNotFoundException {
        boolean i =CrudUtil.execute("INSERT INTO Supplier VALUES(?,?,?,?,?,?)",
                supplier.getSupplier_id(),
                supplier.getSupplier_name(),
                supplier.getSupplier_company(),
                supplier.getSupplier_Address(),
                supplier.getSupplier_nic(),
                supplier.getSupplier_contact_no()
        );
        System.out.println(i);
        return i;
    }
    public static List<Supplier> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM supplier";

        List<Supplier> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Supplier(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getString(6)
            ));
        }
        return data;
    }



    public static Supplier search(String supplierId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM Supplier WHERE Supplier_Id='" + supplierId + "'");
        System.out.println(rst);
        if (rst.next()) {
            return new Supplier(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5),rst.getString(6));
        }
        return null;
    }

    public static ArrayList<Supplier> View() throws SQLException {
        ArrayList<Supplier> supplierArrayList=new ArrayList<>();
        ResultSet rst=CrudUtil.execute("SELECT * FROM supplier");


        while(rst.next()){
            supplierArrayList.add(
                    new Supplier(rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4),rst.getString(5), rst.getString(6))
            );
        }
        return supplierArrayList;
    }
    }
