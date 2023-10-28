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
import lk.ijse.Roosalu.dto.Agent;
import lk.ijse.Roosalu.dto.User;
import lk.ijse.Roosalu.dto.tm.AgentTM;
import lk.ijse.Roosalu.model.AgentModel;
import lombok.SneakyThrows;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.util.*;


public class AgentManageController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TextField txtSearch;
    public ComboBox cmbUserId;

    @FXML
    private JFXTextField txtUserId;
    @FXML
    private Button btnsaveAgent;

    @FXML
    private Button btnUpdateAgent;

    @FXML
    private Button btnAgentDelete;


    @FXML
    private JFXTextField txtAgentId;

    @FXML
    private JFXTextField txtAgentCompany;

    @FXML
    private JFXTextField txtAgentName;

    @FXML
    private JFXTextField txtAgentContactNo;

    @FXML
    private JFXTextField txtAgentEmail;

    @FXML
    private TableView<AgentTM> tblAgent;

    @FXML
    private TableColumn<?, ?> colUserId;

    @FXML
    private TableColumn<?, ?> colAgentId;

    @FXML
    private TableColumn<?, ?> colAgentCompany;

    @FXML
    private TableColumn<?, ?> colAgentName;

    @FXML
    private TableColumn<?, ?> colAgentContactNo;
    @FXML
    private TableColumn<?, ?> colAgentEmail;

    private ObservableList<AgentTM> observableList;
    String userid;
    public static ArrayList<User> userArrayList= new ArrayList();


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
        loadComboBox();
    }
    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<Agent> agentslist = AgentModel.getAll();

            for (Agent agent : agentslist) {
                observableList.add(new AgentTM(
                        agent.getId(),
                        agent.getEmail(),
                        agent.getName(),
                        agent.getContact_no(),
                        agent.getCompany()
                ));
            }
            tblAgent.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }

    }
    private void setCellValueFactory() {
        colAgentId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAgentEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAgentName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAgentContactNo.setCellValueFactory(new PropertyValueFactory<>("contact_no"));
        colAgentCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
    }

    @FXML
    void btnAddAgentOnAction(ActionEvent event) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }
        Agent agent = new Agent(txtAgentId.getText(),txtAgentEmail.getText(),txtAgentName.getText(),txtAgentContactNo.getText(),txtAgentCompany.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO agent " + "VALUE (?,?,?,?,?)");
            pstm.setString(1,agent.getId());
            pstm.setString(2, agent.getEmail());
            pstm.setString(3,agent.getName());
            pstm.setString(4,agent.getContact_no());
            pstm.setString(5,agent.getCompany());



            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Agent Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Agent Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();

    }

    @FXML
    void btnUpdateAgentOnAction(ActionEvent event) {
        Agent agent =new Agent(txtAgentId.getText(),txtAgentEmail.getText(),txtAgentName.getText(),txtAgentCompany.getText(),txtAgentContactNo.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE agent SET " + "Email= ?,Agent_Name = ?,Agent_Conatct_No= ?,Agent_Company = ? WHERE Agent_Id = ?");

            pstm.setString(1,agent.getEmail());
            pstm.setString(2,agent.getName());
            pstm.setString(3,agent.getContact_no());
            pstm.setString(4,agent.getCompany());
            pstm.setString(5,agent.getId());


            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Agent Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    @FXML
    void btnDeleteAgentOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from agent" +" WHERE Agent_Id = ?");

            pstm.setString(1,txtAgentId.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Agent Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    public void txtAgentSearchOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM agent WHERE Agent_Id= ? ");

            pstm.setString(1,txtSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                txtAgentId.setText(resultSet.getString(1));
                txtAgentEmail.setText(resultSet.getString(2));
                txtAgentName.setText(resultSet.getString(3));
                txtAgentContactNo.setText(resultSet.getString(4));
                txtAgentCompany.setText(resultSet.getString(5));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


    void rowOnMouseClicked(MouseEvent event) {
    }
    private void clearAll() {
        txtAgentId.setText(null);
        txtAgentEmail.setText(null);
        txtAgentName.setText(null);
        txtAgentContactNo.setText(null);
        txtAgentCompany.setText(null);
    }

   public void loadComboBox() throws SQLException, ClassNotFoundException {
    }
    public void cmbUserIdOnAction(ActionEvent actionEvent) {
    }

    public void rowOnMouseClicked(javafx.scene.input.MouseEvent mouseEvent) {
    }

    public void txtAgentId(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INVOICE,txtAgentId);
    }

    public void txtAgentCompany(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.NAME,txtAgentCompany);
    }

    public void txtAgentName(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.NAME,txtAgentName);
    }

    public void txtAgentContactNo(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.PHONE,txtAgentContactNo);
    }

    public void txtAgentEmail(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.EMAIL,txtAgentEmail);
    }
    public boolean isValidated(){
        if (!Regex.setTextColor(TextFields.INVOICE,txtAgentId))return false;
        if (!Regex.setTextColor(TextFields.NAME,txtAgentCompany))return false;
        if (!Regex.setTextColor(TextFields.NAME,txtAgentName))return false;
        if (!Regex.setTextColor(TextFields.PHONE,txtAgentContactNo))return false;
        if (!Regex.setTextColor(TextFields.EMAIL,txtAgentEmail))return false;
        return true;
    }
}
