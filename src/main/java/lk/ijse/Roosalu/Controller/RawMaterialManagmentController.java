package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Agent;
import lk.ijse.Roosalu.dto.Order;
import lk.ijse.Roosalu.dto.RawMaterial;
import lk.ijse.Roosalu.dto.tm.RawMaterialTM;
import lk.ijse.Roosalu.model.AgentModel;
import lk.ijse.Roosalu.model.OrderModel;
import lk.ijse.Roosalu.model.RawMaterialModel;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class RawMaterialManagmentController implements Initializable {

    public JFXTextField txtUnitPrice;

    public TableColumn colUnitPrice;
    public TableColumn colAgentId;
    public ComboBox cmbAgentId;
    @FXML
    private TextField txtSearchRawMaterail;

    @FXML
    private JFXTextField txtRawMaterialId;

    @FXML
    private JFXTextField txtRawMaterialQty;

    @FXML
    private TableView<RawMaterialTM> tblRawMaterial;

    @FXML
    private TableColumn<?, ?> colRawMaterialId;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colRawMaterialType;

    @FXML
    private TableColumn<?, ?> colRawMaterialqty;

    @FXML
    private ComboBox<String> cmbRawMaterialType;
    @FXML
    private JFXTextField txtOrderId;
    @FXML
    private JFXTextField txtAgentId;

    @FXML
    private ComboBox<String> cmbOrderId;
    private ObservableList<RawMaterialTM> observableList;
    private String OrderId;
    public static ArrayList<Order> OrderArrayList= new ArrayList();
    private String agentId;
    public static ArrayList<Agent> AgentArrayList= new ArrayList();

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
        ObservableList<String> list = FXCollections.observableArrayList("linen","cotton","Satin","Lace","Leather");
        cmbRawMaterialType.setItems(list);
        loadAgentIdComboBox();
    }
    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<RawMaterial> rawMaterialList = RawMaterialModel.getAll();

            for (RawMaterial rawMaterial: rawMaterialList) {
                observableList.add(new RawMaterialTM(
                        rawMaterial.getRaw_material_id(),
                        rawMaterial.getType(),
                        rawMaterial.getQuantity(),
                        rawMaterial.getUnit_price(),
                        rawMaterial.getAgent_id()
                ));
            }
            tblRawMaterial.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }
    private void setCellValueFactory() {
        colRawMaterialId.setCellValueFactory(new PropertyValueFactory<>("raw_material_id"));
        colRawMaterialType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colRawMaterialqty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colAgentId.setCellValueFactory(new PropertyValueFactory<>("agent_id"));

    }

    @FXML
    void btnRawMaterialAddOnAction(ActionEvent event) {
        RawMaterial rawMaterial = new RawMaterial(txtRawMaterialId.getText(),(String) cmbRawMaterialType.getValue(),Integer.parseInt(txtRawMaterialQty.getText()),Double.parseDouble(txtUnitPrice.getText()),(String) cmbAgentId.getValue());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO rawmaterial" + " VALUE (?,?,?,?,?)");
            pstm.setString(1, rawMaterial.getRaw_material_id());
            pstm.setString(2, rawMaterial.getType());
            pstm.setInt(3,    rawMaterial.getQuantity());
            pstm.setDouble(4,rawMaterial.getUnit_price());
            pstm.setString(5, rawMaterial.getAgent_id());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"rawmaterial Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"rawmaterial Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        ClearAll();
    }

    @FXML
    void btnRawMaterialUpdateOnAction(ActionEvent event) {
        RawMaterial rawMaterial = new RawMaterial(txtRawMaterialId.getText(),(String) cmbRawMaterialType.getValue(),Integer.parseInt(txtRawMaterialQty.getText()),Double.parseDouble(txtUnitPrice.getText()),(String)cmbAgentId.getValue());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE rawmaterial SET " + "Type= ?,Quantity= ?,Unit_Price= ?,Agent_Id=? WHERE Raw_Material_Id= ?");
            pstm.setString(1, rawMaterial.getType());
            pstm.setInt(2,    rawMaterial.getQuantity());
            pstm.setDouble(3, rawMaterial.getUnit_price());
            pstm.setString(4, rawMaterial.getAgent_id());
            pstm.setString(5, rawMaterial.getRaw_material_id());


            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"RawMaterial Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    @FXML
    void btnRawMaterialDeleteOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from rawmaterial" +" WHERE Raw_Material_Id= ?");

            pstm.setString(1,txtRawMaterialId.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"RawMaterial Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    @FXML
    void txtRawMaterialSearchOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM rawmaterial WHERE Raw_Material_Id= ? ");

            pstm.setString(1,txtSearchRawMaterail.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                txtRawMaterialId.setText(resultSet.getString(1));
                cmbRawMaterialType.setValue(resultSet.getString(2));
                txtRawMaterialQty.setText(resultSet.getString(3));
                cmbOrderId.setValue(resultSet.getString(4));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void loadAgentIdComboBox() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<String> agentViewOb = FXCollections.observableArrayList();
            AgentArrayList= AgentModel.View();
            System.out.println(AgentArrayList);
            for (Agent agent:AgentArrayList) {
                agentViewOb.add(agent.getId());
            }
            cmbAgentId.setItems(agentViewOb);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
       // load();
    }
    public void cmbAgentIdOnAction(ActionEvent actionEvent) {
        agentId= (String) cmbAgentId.getValue();
        try {
            Agent agent= AgentModel.search(agentId);
            /*txtAgentName.setText(agent.getName());
            txtAgentCompany.setText(agent.getCompany());*/
            txtAgentId.setText(agentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    private void ClearAll() {
       txtRawMaterialId.setText(null);
       cmbRawMaterialType.setValue(null);
       txtRawMaterialQty.setText(null);
       txtUnitPrice.setText(null);
       cmbAgentId.setValue(null);
    }
}
