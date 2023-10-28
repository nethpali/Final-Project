package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Roosalu.CrudUtil.Service;
import lk.ijse.Roosalu.dto.*;
import lk.ijse.Roosalu.dto.tm.AttendanceTM;
import lk.ijse.Roosalu.model.AttendanceModel;
import lk.ijse.Roosalu.model.EmployeeModel;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class MarkAttendanceController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TableView tblMarkAttendance;
    public Label lblAttendanceId;
    @FXML
    private AnchorPane ControllPane;

    @FXML
    private ComboBox<String> cmbEmployeeId;

    @FXML
    private TextField txtSearchMonthlySalary;

    @FXML
    private JFXTextField txtAttendanceId;

    @FXML
    private TableView<?> tblMonthlySalary;

    @FXML
    private TableColumn<?, ?> colAttendanceId;

    @FXML
    private TableColumn<?, ?> colEmployeeId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTime;

    @FXML
    private TableColumn<?, ?> colAbsentPresent;

    @FXML
    private JFXTextField txtDate;

    @FXML
    private JFXTextField txtTime;

    @FXML
    private RadioButton rbnPresent;
    @FXML
    private JFXTextField txtemployeeid;

    @FXML
    private RadioButton rbnAbsent;
    private ObservableList<AttendanceTM> observableList;
    String attendanceStatus;
    static SimpleDateFormat timeFormat;
    static SimpleDateFormat dayFormat;
    boolean isMatchedStaffId;
    public static ArrayList<Attendance> attendsView= new ArrayList();
    String EmployeeId;
    public static ArrayList<Employee> employeeArrayList= new ArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtDate.setText(Service.setDate());
        txtTime.setText(Service.setTime());
        getAll();
        setCellValueFactory();
        loadComboBox();
    }

    private void setCellValueFactory() {

        colEmployeeId .setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        colAttendanceId.setCellValueFactory(new PropertyValueFactory<>(" attendance_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        colAbsentPresent.setCellValueFactory(new PropertyValueFactory<>("absent_present"));

    }

    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<Attendance> attendanceList = AttendanceModel.getAll();

            for (Attendance attendance:attendanceList) {
                observableList.add(new AttendanceTM(
                        attendance.getEmployee_id(),
                        attendance.getAttendance_id(),
                        attendance.getDate(),
                        attendance.getTime(),
                        attendance.getAbsent_present()
                ));
            }
            tblMarkAttendance.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }


    @FXML
    void rbnAbsentOnAction(ActionEvent event) {
        if(rbnAbsent.isSelected()){
            attendanceStatus="Present";
        }else{
            attendanceStatus="Absent";
        }
        System.out.println(attendanceStatus);
    }

    String CheckAttendance(){
        boolean present = rbnPresent.isSelected();
        System.out.println(present);

        if (present){
            return "Present";
        }else {
            return "Absent";
        }
    }

    @FXML
    void btnSaveAttendanceOnAction(ActionEvent event) throws SQLException {
        String AID = lblAttendanceId.getText();
        String EID = cmbEmployeeId.getValue();
        String date = txtDate.getText();
        String time = txtTime.getText();


        try(Connection con = DriverManager.getConnection(URL,props)){
            String sql = "INSERT INTO employee_attendance(Employee_Id ,Attendance_Id, Date, Time, absent_present) VALUES(?,?,?,?,?)";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,EID);
            pstm.setString(2,AID);
            pstm.setString(3, String.valueOf(LocalDate.now()));
            pstm.setString(4, String.valueOf(LocalTime.now()));
            pstm.setString(5, CheckAttendance());

            try {
                int affectedRows = pstm.executeUpdate();
                if (affectedRows > 0) {
                    new Alert(Alert.AlertType.CONFIRMATION, "Attendance Added !!").show();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }

    }
    public void btnLoadAttendanceOnAction(ActionEvent actionEvent) {
        ObservableList<Attendance> employeeview = FXCollections.observableArrayList();
        try {
            attendsView= AttendanceModel.View();
            for (Attendance attendance:attendsView) {
                employeeview.add(attendance);
            }
            tblMarkAttendance.setItems(employeeview);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void loadComboBox(){
        try {
            ObservableList<String> employeeViewOb = FXCollections.observableArrayList();
            employeeArrayList= EmployeeModel.View();
            System.out.println(employeeArrayList);
            for (Employee employee:employeeArrayList) {
                employeeViewOb.add(employee.getEmployee_id());
            }
            cmbEmployeeId.setItems(employeeViewOb);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
    }
    public void cmbloadEmployeeId(ActionEvent actionEvent) {
        EmployeeId = (String) cmbEmployeeId.getValue();
        try {
            Employee employee = EmployeeModel.search(EmployeeId);
            txtemployeeid.setText(EmployeeId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void clearAll() {
        txtAttendanceId.setText(null);
        cmbEmployeeId.setValue(null);
    }

    private void generateNextOrderId() throws SQLException {
        String nextId = AttendanceModel.generateNextOrderId();
        lblAttendanceId.setText(nextId);
    }
    @FXML
    void btnSearchAttendanceOnAction(ActionEvent event) {

    }

    public void rbnPresentOnAction(ActionEvent actionEvent) {

    }
}
