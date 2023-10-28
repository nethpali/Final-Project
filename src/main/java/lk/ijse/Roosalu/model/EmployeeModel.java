package lk.ijse.Roosalu.model;

import lk.ijse.Roosalu.CrudUtil.CrudUtil;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Agent;
import lk.ijse.Roosalu.dto.Employee;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    public static List<Employee> getAll() throws SQLException {
        Connection con = DBConnection.getInstance().getConnection();
        String sql = "SELECT * FROM Employee";

        List<Employee> data = new ArrayList<>();

        ResultSet resultSet = con.createStatement().executeQuery(sql);
        while (resultSet.next()) {
            data.add(new Employee(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getString(5),
                    resultSet.getDouble(6)
            ));
        }
        return data;
    }

    public static Employee search(String employeeId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT * FROM employee WHERE Employee_Id='" + employeeId + "'");
        System.out.println(rst);
        if (rst.next()) {
            return new Employee(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getDouble(6));
        }
        return null;
    }

    public static ArrayList<Employee> View() throws SQLException {
        ArrayList<Employee> employeeArrayList = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT * FROM employee");


        while (rst.next()) {
            employeeArrayList.add(
                    new Employee(rst.getString(1), rst.getString(2), rst.getString(3), rst.getString(4), rst.getString(5), rst.getDouble(6))
            );
        }
        return employeeArrayList;
    }
}
