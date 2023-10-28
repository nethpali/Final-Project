package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.Roosalu.CrudUtil.Regex;
import lk.ijse.Roosalu.CrudUtil.TextFields;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Employee;
import lk.ijse.Roosalu.dto.tm.EmployeeTM;
import lk.ijse.Roosalu.model.EmployeeModel;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.ResourceBundle;

public class EmployeeManageController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public AnchorPane root3;


    @FXML
    private Button btnupdate;

    @FXML
    private Button btnsave;

    @FXML
    private Button btndelete;

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSalary;

    @FXML
    private JFXTextField txtemployeeid;

    @FXML
    private JFXTextField txtemployeenic;

    @FXML
    private JFXTextField txtemployeeaddress;

    @FXML
    private JFXTextField txtemployeename;

    @FXML
    private JFXTextField txtemployeecontactNo;

    @FXML
    private JFXTextField txtemployeeslarayperhour;

    @FXML
    private TableView<EmployeeTM> tblemployee;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colemployeeId;

    @FXML
    private TableColumn<?, ?> colemployeenic;

    @FXML
    private TableColumn<?, ?> colemployeeaddress;

    @FXML
    private TableColumn<?, ?> colemployeename;

    @FXML
    private TableColumn<?, ?> colemployeecontactNo;

    @FXML
    private TableColumn<?, ?> colemplyeesalaryperHour;

    @FXML
    private ComboBox<?> txtUserId;

    private ObservableList<EmployeeTM> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
    }

    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<Employee> employeeList = EmployeeModel.getAll();

            for (Employee employee : employeeList) {
                observableList.add(new EmployeeTM(
                        employee.getEmployee_id(),
                        employee.getEmployee_nic(),
                        employee.getEmployee_name(),
                        employee.getEmployee_address(),
                        employee.getEmployee_contact_no(),
                        employee.getEmployee_salary_per_hour()

                ));
            }
            tblemployee.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }
    private void setCellValueFactory() {
        colemployeeId.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        colemployeenic.setCellValueFactory(new PropertyValueFactory<>("employee_nic"));
        colemployeename.setCellValueFactory(new PropertyValueFactory<>("employee_name"));
        colemployeeaddress.setCellValueFactory(new PropertyValueFactory<>("employee_address"));
        colemployeecontactNo.setCellValueFactory(new PropertyValueFactory<>("employee_contact_no"));
        colemplyeesalaryperHour.setCellValueFactory(new PropertyValueFactory<>("employee_salary_per_hour"));
    }

    @FXML
    void btnCalculateOnAction(ActionEvent event) throws IOException, SQLException {
        Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/MarkAttendance.fxml"));
        Scene scene = new Scene(anchorPane);

        Stage stage = new Stage();
        stage.setTitle("Customer Manage");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @FXML
    void btnsaveEmployeeOnAction(ActionEvent event) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }

        Employee employee = new Employee(txtemployeeid.getText(),txtemployeenic.getText(),txtemployeename.getText(),txtemployeeaddress.getText(),txtemployeecontactNo.getText(),Double.parseDouble(txtemployeeslarayperhour.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO employee " + "VALUE (?,?,?,?,?,?)");

            pstm.setString(1,employee.getEmployee_id());
            pstm.setString(2, employee.getEmployee_nic());
            pstm.setString(3, employee.getEmployee_name());
            pstm.setString(4, employee.getEmployee_address());
            pstm.setString(5, employee.getEmployee_contact_no());
            pstm.setDouble(6, employee.getEmployee_salary_per_hour());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION," Employee Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Employee Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();

    }

    @FXML
    void btnUpdateEmployeeOnAction(ActionEvent event) throws SQLException {
        Employee employee = new Employee(txtemployeeid.getText(),txtemployeenic.getText(),txtemployeename.getText(),txtemployeeaddress.getText(),txtemployeecontactNo.getText(),Double.parseDouble(txtemployeeslarayperhour.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE employee SET " + "Employee_NIC= ?,Employee_Name= ?,Employee_Address= ?,Employee_Contact_No = ?,Employee_Salary_Per_hour=? WHERE Employee_Id = ?");


            pstm.setString(1, employee.getEmployee_nic());
            pstm.setString(2, employee.getEmployee_name());
            pstm.setString(3, employee.getEmployee_address());
            pstm.setString(4, employee.getEmployee_contact_no());
            pstm.setDouble(5, employee.getEmployee_salary_per_hour());
            pstm.setString(6,employee.getEmployee_id());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Employee Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    @FXML
    void btnDeleteEmployeeOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from employee WHERE Employee_Id = ?");

            pstm.setString(1,txtemployeeid.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Employee Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    public void btnEmployeeSearchOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM Employee WHERE Employee_Id= ? ");

            pstm.setString(1,txtSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){

                txtemployeeid.setText(resultSet.getString(1));
                txtemployeenic.setText(resultSet.getString(2));
                txtemployeename.setText(resultSet.getString(3));
                txtemployeeaddress.setText(resultSet.getString(4));
                txtemployeecontactNo.setText(resultSet.getString(5));
                txtemployeeslarayperhour.setText(resultSet.getString(6));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    private void clearAll() {
        txtemployeeid.setText(null);
        txtemployeenic.setText(null);
        txtemployeename.setText(null);
        txtemployeeaddress.setText(null);
        txtemployeecontactNo.setText(null);
        txtemployeeslarayperhour.setText(null);
    }

    public void txtEmployeeIdOnAction(KeyEvent keyEvent) {
      Regex.setTextColor(TextFields.INVOICE,txtemployeeid);
    }

    public void txtEmployeeNic(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.LANKAN_ID,txtemployeenic);
    }

    public void txtEmployeeAddress(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.ADDRESS,txtemployeeaddress);
    }

    public void txtEmployeeName(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.NAME,txtemployeename);
    }

    public void txtEmployeeContatctNo(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.PHONE,txtemployeecontactNo);
    }

    public void txtEmployeeSalaryPerHour(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.DOUBLE,txtemployeeslarayperhour);
    }
    public boolean isValidated(){
        if (!Regex.setTextColor(TextFields.INVOICE,txtemployeeid))return false;
        if (!Regex.setTextColor(TextFields.NAME,txtemployeename))return false;
        if (!Regex.setTextColor(TextFields.ADDRESS,txtemployeeaddress))return false;
        if (!Regex.setTextColor(TextFields.LANKAN_ID,txtemployeenic))return false;
        if (!Regex.setTextColor(TextFields.DOUBLE,txtemployeeslarayperhour))return false;
        if (!Regex.setTextColor(TextFields.PHONE,txtemployeecontactNo))return false;
        return true;
    }
}
