package lk.ijse.Roosalu.Controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {

    public AnchorPane root;
    public AnchorPane loadFormContext;
    public Label lblDate;
    public Label lblTime;
    private AnchorPane Controllpane1;

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // TimeNow();
       // lblDate.setText(String.valueOf(LocalDate.now()));
        ControllArea.setVisible(false);
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/DashBoardView.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
        initClock();
    }
    public AnchorPane ControllArea;

    public void btnManageAgentsOnActions(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/AgentManage.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/EmployeeManage.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }


    public void btnManageUserOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/UserManage.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageAttendanceOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/MarkAttendance.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageOrderOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageOrder.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageRawMaterialOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/RawMaterialManagment.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageProductionOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/ManageProduction.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void btnManageSupplierOnAction(ActionEvent actionEvent) throws IOException {

    }

    public void btnManageItemOnAction(ActionEvent actionEvent) throws IOException {

    }

    public void btnManageAssetsOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/AssestsManage.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }


    /*public void btnDashBoradOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/DashBoardView.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }*/

    public void btnDashBoardOnAction(ActionEvent actionEvent) throws IOException {
        ControllArea.setVisible(true);
        Parent fxml = FXMLLoader.load(getClass().getResource("/view/DashBoardView.fxml"));
        ControllArea.getChildren().removeAll();
        ControllArea.getChildren().setAll(fxml);
    }

    public void imgLogoutOnMouseClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader loader= new FXMLLoader(getClass().getResource("/view/LoginForm.fxml"));
        AnchorPane anchorPane = loader.load();
        Scene scene = new Scene(anchorPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
        root.getScene().getWindow().hide();

    }
    private void initClock() {

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            lblTime.setText(LocalDateTime.now().format(formatter));

            SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            lblDate.setText(formatter2.format(date));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


}
