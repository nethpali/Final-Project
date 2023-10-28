package lk.ijse.Roosalu.Controller;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.Roosalu.CrudUtil.Service;
import lk.ijse.Roosalu.db.DBConnection;
import lk.ijse.Roosalu.dto.*;
import lk.ijse.Roosalu.dto.tm.OrderTM;
import lk.ijse.Roosalu.model.AgentModel;
import lk.ijse.Roosalu.model.OrderModel;
import lk.ijse.Roosalu.model.ProductionModel;
import lombok.SneakyThrows;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

public class ManageOrderController implements Initializable {
    private final static String URL = "jdbc:mysql://localhost:3306/roosalu";
    private final static Properties props = new Properties();

    static {
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
    }

    public Label txtOrder1Id;
    public TextField txtSearchOrder;
    public AnchorPane root2;
    public TableColumn colDate;
    public JFXTextField txtOrderDate;
    public JFXTextField txtOrderDelete;
    public TableColumn colProductId;
    public TableColumn colUnitPrice;
    public TableColumn colTotal;
    public TableColumn colqty;
    public JFXTextField txtUnitPrice;
    public ComboBox cmbProductId;
    public JFXTextField txtqty;
    public JFXTextField txtqtyonHand;
    public TextField txtqty1;
    public TextField txtBillOrder;
    public Label lblNetTotal;

    //public DatePicker dpOrderDeadLine;
    //public Label lblOrderId;
    @FXML
    private Label txtDate;

    @FXML
    private JFXTextField txtOrderId;

    @FXML
    private ComboBox<String> cmbAgentId;

    @FXML
    private JFXTextField txtAgentName;

    @FXML
    private JFXTextField txtAgentCompany;

    @FXML
    private JFXTextField txtOrderDescription;

    @FXML
    private DatePicker dpOrderDeadLine;

    @FXML
    private JFXTextField txtAgentId;

    @FXML
    private TableView<Object> tblOrder;

    @FXML
    private TableColumn<?, ?> colAgentId;

    @FXML
    private TableColumn<?, ?> colOrderId;

    @FXML
    private TableColumn<?, ?> colDescription;

    @FXML
    private TableColumn<?, ?> colDeadline;


    private String agentId;
    public static ArrayList<Agent> agentView= new ArrayList();
    private AnchorPane ControllArea;
    public static ArrayList<Order> OrderView= new ArrayList();
    ArrayList<Order> orderView=new ArrayList<>();
    private ObservableList<Object> observableList;
    private String productId;
    private ArrayList<Production> productView=new ArrayList<>();
    private JFXTextField txtProductionId;


    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAgentIdComboBox();
        loadProductIdComboBox();
        txtOrderDate.setText(String.valueOf(LocalDate.now()));

        colOrderId.setCellValueFactory(new PropertyValueFactory<>("order_id"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colAgentId.setCellValueFactory(new PropertyValueFactory<>("agent_id"));
        colProductId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unit_price"));
        colqty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));


        load();
    }

    private void getAll(){
        try {
            observableList = FXCollections.observableArrayList();
            List<Order> orderList = OrderModel.getAll();

            for (Order order:orderList) {
                observableList.add(new OrderTM(
                        order.getOrder_id(),
                        order.getDate(),
                        order.getAgent_id(),
                        order.getProduct_id(),
                        order.getUnit_price(),
                        order.getQuantity(),
                        order.getTotal()
                ));
            }
            tblOrder.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query Error!!").show();
        }
    }

    public void btnAddOrderOnAction(ActionEvent actionEvent) {
        String oid = txtOrderId.getText();;
        String date = txtOrderDate.getText();
        String aid = txtAgentId.getText();
        String pid= String.valueOf(cmbProductId.getValue());
        Double unit_price= Double.valueOf(txtUnitPrice.getText());
        int qty= Integer.parseInt(txtqty.getText());
        Double total = unit_price * qty;


        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("INSERT INTO orders(Order_Id,Date,Agent_Id,Product_Id,Unit_Price,Quantity, Total) VALUE (?,?,?,?,?,?,?)");
            pstm.setString(1, oid);
            pstm.setString(2, date);
            pstm.setString(3, aid);
            pstm.setString(4,pid);
            pstm.setString(5, String.valueOf(unit_price));
            pstm.setString(6, String.valueOf(qty));
            pstm.setString(7, String.valueOf(total));

            int add = pstm.executeUpdate();

            if (add>0 ){
                new Alert(Alert.AlertType.CONFIRMATION,"Order Added", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Order Not Saved", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
        clearAll();
    }

    private void clearAll() {
        txtOrderId.setText(null);
        txtOrderDate.setText(null);
        txtAgentId.setText(null);
        cmbProductId.setValue(null);
        txtUnitPrice.setText(null);
        txtqty.setText(null);

    }

    public void loadAgentIdComboBox() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<String> agentViewOb = FXCollections.observableArrayList();
            agentView= AgentModel.View();
            System.out.println(agentView);
            for (Agent agent:agentView) {
                agentViewOb.add(agent.getId());
            }
            cmbAgentId.setItems(agentViewOb);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        load();
    }

    @FXML
    void cmbAgentIdOnAction(ActionEvent event) {
        agentId= (String) cmbAgentId.getValue();
        try {
            Agent agent=AgentModel.search(agentId);
            txtAgentName.setText(agent.getName());
            txtAgentCompany.setText(agent.getCompany());
            txtAgentId.setText(agentId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddOnAction(ActionEvent actionEvent) throws IOException {
        txtOrderDate.setText(Service.setDate());
        txtAgentId.setText(agentId);
        URL resource = getClass().getResource("/view/AgentManage.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        root2.getChildren().clear();
        root2.getChildren().add(load);

        load();
    }
    @FXML
    void btnOrderUpdateOnAction(ActionEvent event) {
        String oid = txtOrderId.getText();
        String date = txtOrderDate.getText();
        String aid = txtAgentId.getText();
        String pid= String.valueOf(cmbProductId.getValue());
        Double unit_price= Double.valueOf(txtUnitPrice.getText());
        int qty= Integer.parseInt(txtqty.getText());
        Double total = unit_price * qty;

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("UPDATE orders SET " + "Date= ?,Agent_Id= ?,Product_Id=?,Unit_Price=?,Quantity=?,Total=? WHERE Order_Id = ?");


            pstm.setString(1, date);
            pstm.setString(2, aid);
            pstm.setString(3, oid);
            pstm.setString(4,pid);
            pstm.setString(5, String.valueOf(unit_price));
            pstm.setString(6, String.valueOf(total));
            pstm.setString(7, String.valueOf(qty));

            int add = pstm.executeUpdate();
            if (add > 0){
                new Alert(Alert.AlertType.CONFIRMATION,"order Updated", ButtonType.OK).show();
            }else {
                new Alert(Alert.AlertType.WARNING,"Try again", ButtonType.OK).show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        getAll();
    }

    public void load(){
        try {
            ObservableList<Object> OrderViewOb = FXCollections.observableArrayList();
            orderView= OrderModel.View();
            System.out.println(orderView);
            for (Order order:orderView) {
                OrderViewOb.add(order);
            }
            tblOrder.setItems(OrderViewOb);
        } catch (SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnOrderSearchOnAction(ActionEvent event) {
        try {
            Connection connection = DBConnection.getInstance().getConnection();
            PreparedStatement pstm = connection.prepareStatement("SELECT * FROM orders WHERE Order_Id= ? ");

            pstm.setString(1,txtSearchOrder.getText());

            ResultSet resultSet = pstm.executeQuery();
            while (resultSet.next()){
                txtOrderId.setText(resultSet.getString(1));
                txtOrderDate.setText(resultSet.getString(2));
                txtAgentId.setText(resultSet.getString(3));
                txtProductionId.setText(resultSet.getString(4));
                txtUnitPrice.setText(resultSet.getString(5));
                txtqty.setText(resultSet.getString(6));

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void LoadOnAction(ActionEvent actionEvent) {
        load();
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) {
        String OrderID = txtOrderDelete.getText();

        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Optional<ButtonType> result = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();

        if (result.orElse(no) == yes) {
            try (Connection con = DriverManager.getConnection(URL, props)) {
                String sql = "DELETE FROM orders WHERE Order_Id = ?";
                PreparedStatement pstm = con.prepareStatement(sql);
                pstm.setString(1, OrderID);
                pstm.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        load();
        txtOrderId.setText(null);
    }
    public void loadProductIdComboBox() throws SQLException, ClassNotFoundException {
        try {
            ObservableList<String> ProductViewOb = FXCollections.observableArrayList();
            productView= ProductionModel.View();
            System.out.println(productView);
            for (Production production:productView) {
                ProductViewOb.add(production.getProduction_id());
            }
            cmbProductId.setItems(ProductViewOb);
        } catch (SQLException e ) {
            e.printStackTrace();
        }
        load();
    }

    public void cmbloadProductIdOnAction(ActionEvent actionEvent) {
        productId= (String) cmbProductId.getValue();
        try {
            Production production= ProductionModel.search(productId);
            txtUnitPrice.setText(String.valueOf(production.getUnit_price()));
            txtqty.setText(String.valueOf(production.getQuantity()));
            txtProductionId.setText(productId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void btnAddTocartOnAction(ActionEvent actionEvent) {
    }

    public void btnPlaceOrderOnAction(ActionEvent actionEvent) {

    }

    public void btnCreateBillOnAction(ActionEvent actionEvent) throws JRException, SQLException {
        String id = txtBillOrder.getText();

        JasperDesign jasDesign = JRXmlLoader.load("src/main/resources/Report/PlaceOrder.jrxml");
        JasperReport jasReport = JasperCompileManager.compileReport(jasDesign);

        Map<String, Object> data = new HashMap<>();
        data.put("OrderID",id);
        data.put("NetTotal",NetTotalCalculate(id));

        JasperPrint jasPrint = JasperFillManager.fillReport(jasReport, data, DBConnection.getInstance().getConnection());
        JasperViewer.viewReport(jasPrint,false);
    }

    String NetTotalCalculate(String id) throws SQLException {
        try (Connection con = DriverManager.getConnection(URL, props)) {
            double tot=0;

            String sql = "SELECT SUM(Total) FROM orders WHERE Order_Id=?";

            PreparedStatement pstm = con.prepareStatement(sql);
            pstm.setString(1,id);

            ResultSet affectedRows = pstm.executeQuery();
            System.out.println(affectedRows);
            while (affectedRows.next()){
                double c = affectedRows.getInt(1);
                tot=tot+c;
            }
            return String.valueOf(tot);
        }
    }
}

