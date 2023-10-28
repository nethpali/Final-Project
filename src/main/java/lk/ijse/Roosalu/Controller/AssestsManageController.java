package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
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
import lk.ijse.Roosalu.dto.Assest;
import lk.ijse.Roosalu.dto.tm.AgentTM;
import lk.ijse.Roosalu.dto.tm.AssetsTM;
import lk.ijse.Roosalu.model.AgentModel;
import lk.ijse.Roosalu.model.AssestModel;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
//import java.util.Date;

public class AssestsManageController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public Label lblDate;
    public Label lblTime;
    public TextField txtSearch;

    @FXML
        private Button btnSave;

        @FXML
        private Button btnUpdate;

        @FXML
        private Button btndelete;

        @FXML
        private JFXTextField txtassestid;

        @FXML
        private JFXTextField txtassestqty;

        @FXML
        private JFXTextField txtassestdescription;

        @FXML
        private JFXTextField txtassestname;

        @FXML
        private JFXTextField txtassetvalue;

        @FXML
        private JFXTextField txtassestdate;

        @FXML
        private TableView<AssetsTM> tblAssest;

        @FXML
        private TableColumn<?, ?> colUserId;

        @FXML
        private TableColumn<?, ?> colAssestId;

        @FXML
        private TableColumn<?, ?> colAssetsName;

        @FXML
        private TableColumn<?, ?> colassestQuantity;

        @FXML
        private TableColumn<?, ?> colAssestDescription;

        @FXML
        private TableColumn<?, ?> colAssestValue;

        @FXML
        private TableColumn<?, ?> colAssestDate;
        @FXML
        private ComboBox<?> cmbUserId;

    private ObservableList<AssetsTM> observableList;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
        //lblDate.setText(String.valueOf(LocalDate.now()));
        //TimeNow();
    }

    void getAll() throws SQLException {
        try {
            observableList = FXCollections.observableArrayList();
            List<Assest> assestList = AssestModel.getAll();

            for (Assest assest : assestList) {
                observableList.add(new AssetsTM(
                        assest.getId(),
                        assest.getName(),
                        assest.getDescription(),
                        assest.getQuantity(),
                        assest.getValue()
                ));
            }
            tblAssest.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    private void setCellValueFactory() {
        colAssestId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colAssetsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAssestDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colassestQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colAssestValue.setCellValueFactory(new PropertyValueFactory<>("value"));

    }

    @FXML
    void btnAssestSaveOnAction(ActionEvent event) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }
        Assest assest = new Assest(txtassestid.getText(),txtassestname.getText(),txtassestdescription.getText(),Integer.parseInt(txtassestqty.getText()),Double.parseDouble(txtassetvalue.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO assests " + "VALUE (?,?,?,?,?)");
            pstm.setString(1,assest.getId());
            pstm.setString(2,assest.getName());
            pstm.setString(3,assest.getDescription());
            pstm.setInt(   4,assest.getQuantity());
            pstm.setDouble(5,assest.getValue());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Assests Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Assests  Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    @FXML
        void btnAssesUpdateOnAction(ActionEvent event) throws SQLException {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }
        Assest assest = new Assest(txtassestid.getText(),txtassestname.getText(),txtassestdescription.getText(),Integer.parseInt(txtassestqty.getText()),Double.parseDouble(txtassetvalue.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE assests SET " + "Assests_Name= ?, Description= ?,Qunatity= ?, Value=? WHERE Assests_Id = ?");

            pstm.setString(1,assest.getName());
            pstm.setString(2, assest.getDescription());
            pstm.setInt(   3, assest.getQuantity());
            pstm.setDouble(4, assest.getValue());
            pstm.setString(5, assest.getId());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Assest Updated", ButtonType.OK).show();
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
    void btnAssetDeleteOnAction(ActionEvent event) throws SQLException {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from assests WHERE Assests_Id = ?");

            pstm.setString(1,txtassestid.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Assests Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }
    private void clearAll() {
        txtassestid.setText(null);
        txtassestname.setText(null);
        txtassestdescription.setText(null);
        txtassestqty.setText(null);
        txtassetvalue.setText(null);
    }

    public void btnAssestsSearchOnAction(ActionEvent actionEvent) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM assests WHERE Assests_Id= ? ");

            pstm.setString(1,txtSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                txtassestid.setText(resultSet.getString(1));
                txtassestname.setText(resultSet.getString(2));
                txtassestdescription.setText(resultSet.getString(3));
                txtassestqty.setText(resultSet.getString(4));
                txtassetvalue.setText(resultSet.getString(5));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void txtAssestId(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INVOICE,txtassestid);
    }

    public void txtAssestqty(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INTEGER,txtassestqty);
    }

    public void txtAssestDescription(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INVOICE,txtassestdescription);
    }

    public void txtAssestName(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.NAME,txtassestname);
    }

    public void txtAssestValue(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.DOUBLE,txtassetvalue);
    }
    public boolean isValidated(){
        if (!Regex.setTextColor(TextFields.INVOICE,txtassestname))return false;
        if (!Regex.setTextColor(TextFields.INTEGER,txtassestqty))return false;
        if (!Regex.setTextColor(TextFields.INVOICE,txtassestdescription))return false;
        if (!Regex.setTextColor(TextFields.NAME,txtassestname))return false;
        if (!Regex.setTextColor(TextFields.DOUBLE,txtassetvalue))return false;
        return true;
    }
}

