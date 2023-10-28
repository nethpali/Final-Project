
package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import lk.ijse.Roosalu.CrudUtil.Regex;
import lk.ijse.Roosalu.CrudUtil.TextFields;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.User;
import lk.ijse.Roosalu.dto.tm.UserTM;
import lk.ijse.Roosalu.model.UserModel;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

public class UserManageController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TextField txtSearch;

    @FXML
    private JFXTextField txtUserId;

    @FXML
    private JFXTextField txtUserNewPassword;

    @FXML
    private JFXTextField txtReEnterPassword;

    @FXML
    private JFXTextField txtUserName;

    @FXML
    private JFXTextField txtUserEmail;

    @FXML
    private TableView<UserTM> tblUser;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colNewPassword;

    @FXML
    private TableColumn<?, ?> colReEnterPassword;

    @FXML
    private TableColumn<?, ?> colUserName;

    @FXML
    private TableColumn<?, ?> colUserEmail;
    private ObservableList<UserTM> observableList;
    private boolean isMatchId=false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
    }

    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<User> userlist = UserModel.getAll();

            for (User user: userlist) {
                observableList.add(new UserTM(
                        user.getUser_id(),
                        user.getPassword(),
                        user.getRe_enter_password(),
                        user.getUser_name(),
                        user.getUser_email()
                ));
            }
            tblUser.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }
    private void setCellValueFactory() {
        colUserId.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        colNewPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        colReEnterPassword.setCellValueFactory(new PropertyValueFactory<>("re_enter_password"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("user_name"));
        colUserEmail.setCellValueFactory(new PropertyValueFactory<>("user_email"));
    }

    @FXML
    void btnUserAddOnAction(ActionEvent event) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }
        User user = new User(txtUserId.getText(),txtUserNewPassword.getText(),txtReEnterPassword.getText(),txtUserName.getText(),txtUserEmail.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO User " + "VALUE (?,?,?,?,?)");
            pstm.setString(1,user.getUser_id());
            pstm.setString(2,user.getPassword() );
            pstm.setString(3,user.getRe_enter_password());
            pstm.setString(4,user.getUser_name());
            pstm.setString(5,user.getUser_email());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"User Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"User Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnUserUpdateOnAction(ActionEvent event) throws SQLException {
        User user = new User(txtUserId.getText(),txtUserNewPassword.getText(),txtReEnterPassword.getText(),txtUserName.getText(),txtUserEmail.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE User SET " + "New_Password= ?,Re_Enter_Password= ?,User_Name= ?,User_Email= ?WHERE User_Id = ?");

            pstm.setString(1,user.getPassword());
            pstm.setString(2,user.getRe_enter_password());
            pstm.setString(3,user.getUser_name());
            pstm.setString(4,user.getUser_email());
            pstm.setString(5,user.getUser_id());


            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"User Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    @FXML
    void btnUserDeleteOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from User WHERE User_Id = ?");

            pstm.setString(1,txtUserId.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"User Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM User WHERE User_Id= ? ");

            pstm.setString(1,txtSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                txtUserId.setText(resultSet.getString(1));
                txtUserNewPassword.setText(resultSet.getString(2));
                txtReEnterPassword.setText(resultSet.getString(3));
                txtUserName.setText(resultSet.getString(4));
                txtUserEmail.setText(resultSet.getString(5));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void clearAll() {
        txtUserId.setText(null);
        txtUserNewPassword.setText(null);
        txtReEnterPassword.setText(null);
        txtUserEmail.setText(null);
        txtUserName.setText(null);
    }

    public void txtUserId(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INVOICE,txtUserId);
    }

    public void txtNewPassword(KeyEvent keyEvent) {
       // Regex.setTextColor(TextFields.INVOICE,txt);
    }

    public void txtReEnterPassword(KeyEvent keyEvent) {
    }

    public void txtUserName(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.NAME,txtUserName);
    }

    public void txtUserEmail(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.EMAIL,txtUserEmail);
    }
    public boolean isValidated(){
        if (!Regex.setTextColor(TextFields.INVOICE,txtUserId))return false;
        if (!Regex.setTextColor(TextFields.NAME,txtUserName))return false;
        if (!Regex.setTextColor(TextFields.EMAIL,txtUserEmail))return false;
        return true;
    }
}
