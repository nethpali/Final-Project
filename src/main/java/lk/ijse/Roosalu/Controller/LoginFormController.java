package lk.ijse.Roosalu.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

    public class LoginFormController {

        public TextField txtUsername;

        @FXML
        private Button btnLogin;
        @FXML
        private PasswordField txtPassword;

        @FXML
        void btnLoginOnAction(ActionEvent event) throws IOException {
            if (txtUsername.getText().equalsIgnoreCase("Admin") & txtPassword.getText().equalsIgnoreCase("1234")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/DashBoard.fxml"));
                AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/DashBoard.fxml"));
                Scene scene = new Scene(anchorPane);
                DashBoardController controller = loader.getController();
               // controller.setTxtName(txtUsername);

                Stage stage = (Stage) btnLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Login Form");
                stage.centerOnScreen();

            } else {
                txtUsername.setStyle("-fx-background-color: red");
                txtPassword.setStyle("-fx-background-color: red");
            }

        }

        public void btnBackOnAction(ActionEvent actionEvent) throws IOException {
            Parent anchorPane = FXMLLoader.load(getClass().getResource("/view/WelcomePage.fxml"));
            Stage stage = (Stage) btnLogin.getScene().getWindow();

            stage.setScene(new Scene(anchorPane));
            stage.setTitle("welcome Page");
            stage.centerOnScreen();

        }
    }
