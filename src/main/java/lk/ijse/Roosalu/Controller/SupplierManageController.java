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
import lk.ijse.Roosalu.dto.Supplier;
import lk.ijse.Roosalu.dto.User;
import lk.ijse.Roosalu.dto.tm.SupplierTM;
import lk.ijse.Roosalu.model.SupplierModel;

import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;

public class SupplierManageController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public TextField txtSupplierSearch;

    @FXML
    private JFXTextField txtSupplierId;

    @FXML
    private JFXTextField txtSupplierName;

    @FXML
    private JFXTextField txtSupplierAddress;

    @FXML
    private JFXTextField txtSupplierNIC;

    @FXML
    private JFXTextField txtSupplierContactNo;

    @FXML
    private TableView<SupplierTM> tblSupplier;

    @FXML
    private TableColumn<?, ?> colSupplierId;

    @FXML
    private TableColumn<?, ?> colSupplierName;

    @FXML
    private TableColumn<?, ?> colSupplierAddress;

    @FXML
    private TableColumn<?, ?> colSuppierNic;

    @FXML
    private TableColumn<?, ?> colSupplierContactNo;

    @FXML
    private TableColumn<?, ?> colSupplierCompany;

    @FXML
    private JFXTextField txtSupplierCompany;
    private ObservableList<SupplierTM> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getAll();
        setCellValueFactory();
    }

    private void setCellValueFactory() {
        colSupplierId.setCellValueFactory(new PropertyValueFactory<>("supplier_id"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplier_name"));
        colSupplierCompany.setCellValueFactory(new PropertyValueFactory<>("supplier_company"));
        colSupplierAddress.setCellValueFactory(new PropertyValueFactory<>("supplier_address"));
        colSuppierNic.setCellValueFactory(new PropertyValueFactory<>("supplier_nic"));
        colSupplierContactNo.setCellValueFactory(new PropertyValueFactory<>("supplier_contact_No"));
    }

    private void getAll() {
        try {
            observableList = FXCollections.observableArrayList();
            List<Supplier> supplierList = SupplierModel.getAll();

            for (Supplier supplier : supplierList) {
                observableList.add(new SupplierTM(
                        supplier.getSupplier_id(),
                        supplier.getSupplier_name(),
                        supplier.getSupplier_company(),
                        supplier.getSupplier_Address(),
                        supplier.getSupplier_nic(),
                        supplier.getSupplier_contact_no()
                ));
            }
            tblSupplier.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }

    }

    @FXML
    void btnAddSupplierOnAction(ActionEvent event) throws SQLException {
        Supplier supplier = new Supplier(txtSupplierId.getText(),txtSupplierName.getText(),txtSupplierCompany.getText(),txtSupplierAddress.getText(),txtSupplierNIC.getText(),txtSupplierContactNo.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO supplier " + "VALUE (?,?,?,?,?,?)");
            pstm.setString(1, supplier.getSupplier_id());
            pstm.setString(2, supplier.getSupplier_name() );
            pstm.setString(3, supplier.getSupplier_company());
            pstm.setString(4, supplier.getSupplier_Address());
            pstm.setString(5, supplier.getSupplier_nic());
            pstm.setString(6, supplier.getSupplier_contact_no());

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Supplier Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Supplier Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    @FXML
    void btnDeleteSupplierOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("DELETE from supplier WHERE supplier_Id= ?");

            pstm.setString(1,txtSupplierId.getText());

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Supplier Deleted", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }


    @FXML
    void btnUpdateSupplierOnAction(ActionEvent event) throws SQLException {
        Supplier supplier = new Supplier(txtSupplierId.getText(),txtSupplierName.getText(),txtSupplierCompany.getText(),txtSupplierAddress.getText(),txtSupplierNIC.getText(),txtSupplierContactNo.getText());
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE User SET " + "Supplier_Name= ?,Supplier_Comapany= ?,Supplier_Address= ?,Supplier_Nic= ?,Supplier_Contact_No=? WHERE Supplier_Id = ?");

            pstm.setString(2, supplier.getSupplier_name());
            pstm.setString(3, supplier.getSupplier_company());
            pstm.setString(4, supplier.getSupplier_Address());
            pstm.setString(5, supplier.getSupplier_nic());
            pstm.setString(6, supplier.getSupplier_contact_no());
            pstm.setString(1, supplier.getSupplier_id());


            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"Supplier Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try agin", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @FXML
    void txtSearchSupplierOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM supplier WHERE supplier_Id= ? ");

            pstm.setString(1,txtSupplierSearch.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()) {

                txtSupplierId.setText(resultSet.getString(1));
                txtSupplierName.setText(resultSet.getString(2));
                txtSupplierCompany.setText(resultSet.getString(3));
                txtSupplierAddress.setText(resultSet.getString(4));
                txtSupplierNIC.setText(resultSet.getString(5));
                txtSupplierContactNo.setText(resultSet.getString(6));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        }
    private void clearAll() {
        txtSupplierId.setText(null);
        txtSupplierName.setText(null);
        txtSupplierCompany.setText(null);
        txtSupplierAddress.setText(null);
        txtSupplierNIC.setText(null);
        txtSupplierContactNo.setText(null);
    }

    }
