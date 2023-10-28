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
import javafx.scene.layout.AnchorPane;
import lk.ijse.Roosalu.CrudUtil.Regex;
import lk.ijse.Roosalu.CrudUtil.TextFields;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.Order;
import lk.ijse.Roosalu.dto.Production;
import lk.ijse.Roosalu.dto.RawMaterial;
import lk.ijse.Roosalu.dto.tm.ProductionTM;
import lk.ijse.Roosalu.model.OrderModel;
import lk.ijse.Roosalu.model.ProductionModel;
import lk.ijse.Roosalu.model.RawMaterialModel;
import lombok.SneakyThrows;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageProductionController implements Initializable {

    public TableColumn <?,?> colOrderId;
    public ComboBox <String>cmbOrderId;
    public DatePicker dpDate;
    public Button btnPlaceOrder;
    public TableColumn colProdutUnitPrice;
    public JFXTextField txtUnitPrice;

    @FXML
    private DatePicker Dob;
    @FXML
    private JFXTextField txtRawMaterialId;
    public TableColumn <?,?> colProductionDate;
    public TableColumn <?,?> colProductionqty;
    public JFXTextField txtProductionDate;
    @FXML
    private TableColumn<?, ?> ColRawMateriallqty;

    @FXML
    private Label lblDate;

    @FXML
    private JFXTextField txtProductionId;

    @FXML
    private JFXTextField txtproductionQty;

    @FXML
    private TextField txtProductionSearch;

    @FXML
    private TableView<ProductionTM> tblProduction;

    @FXML
    private TableColumn<?, ?> colProductionId;

    @FXML
    private TableColumn<?, ?> colRawMaterialId;

    @FXML
    private TableColumn<?, ?> Colqty;

    @FXML
    private TableColumn<?, ?> colDate;

    private JFXTextField txtOrderId;

    @FXML
    private JFXTextField txtAgentId;

    @FXML
    private ComboBox<String> cmbrawmaterialId;
    private ObservableList<ProductionTM> observableList;
    private String rawmaterialid;
    public static ArrayList<RawMaterial> rawMaterialArrayList= new ArrayList();
    private String OrderId;
    private ArrayList<Order> OrderArrayList;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
        loadComboBox();


        //lblDate.setText(String.valueOf(LocalDate.now()));
    }

    private void setCellValueFactory() {
        colProductionId.setCellValueFactory(new PropertyValueFactory<>("production_id"));
        colProductionDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colProductionqty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colProdutUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));

    }
    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<Production> productionList = ProductionModel.getAll();

            for (Production production: productionList) {
                observableList.add(new ProductionTM(
                        production.getProduction_id(),
                        production.getDate(),
                        production.getQuantity(),
                        production.getUnit_price()

                ));
            }
            tblProduction.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    @FXML
    void btnProductionSaveOnAction(ActionEvent event) {
        if (!isValidated()){
            new Alert(Alert.AlertType.ERROR,"").show();
            return;
        }
        LocalDate date = dpDate.getValue();
        Production production=new Production(txtProductionId.getText(),String.valueOf(dpDate),Integer.parseInt(txtproductionQty.getText()),Double.parseDouble(txtUnitPrice.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO product(Product_Id,Date,Quantity,Unit_Price) VALUE (?,?,?,?)");
            pstm.setString(1, production.getProduction_id());
            pstm.setString(2, String.valueOf(date));
            pstm.setInt(3, production.getQuantity());
            pstm.setDouble(4,production.getUnit_price());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Production Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Production Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnProductionUpdateOnAction(ActionEvent event) {
        LocalDate date = dpDate.getValue();
        Production production=new Production(txtProductionId.getText(),String.valueOf(dpDate),Integer.parseInt(txtproductionQty.getText()),Double.parseDouble(txtUnitPrice.getText()));
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE product SET " + "Date= ?,Quantity = ?, Unit_Price=? WHERE Product_Id = ?");

            pstm.setString(1, String.valueOf(date));
            pstm.setInt(   2, production.getQuantity());
            pstm.setDouble(3,production.getUnit_price());
            pstm.setString(4, production.getProduction_id());
            ;


            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"product Updated", ButtonType.OK).show();
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
    void btnProductionDeleteOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from product WHERE Product_id = ?");

            pstm.setString(1,txtProductionId.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Production Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();

    }

    @FXML
    void btnManageSearchOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM product WHERE Product_Id= ? ");

            pstm.setString(1,txtProductionSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {
                txtProductionId.setText(resultSet.getString(1));
                dpDate.setValue(LocalDate.parse(resultSet.getString(2)));
                txtproductionQty.setText(resultSet.getString(3));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void cmbRawMaterialIdOnAction(ActionEvent event) throws SQLException {
        rawmaterialid= (String) cmbrawmaterialId.getValue();
        try {
            RawMaterial rawMaterial= RawMaterialModel.search(rawmaterialid);
            txtRawMaterialId.setText(rawmaterialid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    public void cmbOrderIdOnAction(ActionEvent actionEvent) {
        OrderId= (String) cmbOrderId.getValue();
        try {
            Order order= OrderModel.search(OrderId);
            cmbOrderId.setValue(OrderId);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }
    public void loadComboBox(){
    }
    private void clearAll() {
        txtProductionId.setText(null);
        dpDate.setValue(null);
        txtproductionQty.setText(null);
        txtUnitPrice.setText(null);

    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {
    }

    public void txtProductUnitPrice(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice);
    }

    public void txtProductionId(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INVOICE,txtProductionId);
    }

    public void txtproductionqty(KeyEvent keyEvent) {
        Regex.setTextColor(TextFields.INTEGER,txtproductionQty);
    }
    public boolean isValidated(){
        if (!Regex.setTextColor(TextFields.INVOICE,txtProductionId))return false;
        if (!Regex.setTextColor(TextFields.DOUBLE,txtUnitPrice))return false;
        if (!Regex.setTextColor(TextFields.INTEGER,txtproductionQty))return false;
        return true;
    }
}
