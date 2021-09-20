package controller;

import entity.Album;
import entity.Photo;
import entity.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import manage.UserStore;

import java.io.File;

public class LoginController {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button login;


    @FXML
    public void onLogin(ActionEvent event) {
        String name = userNameField.getText().trim();
        String password = passwordField.getText().trim();
        if (name.isEmpty() || password.isEmpty()) {
            alert("Name or Password can not be empty.");
        } else {
            if (name.equals("admin") && password.equals("admin")) {
                //Admin Subsystem
                Stage primaryStage = new Stage();
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/fxml/admin.fxml"));
                    primaryStage.setScene(new Scene(root, 580, 418));
                    primaryStage.setTitle("Admin");
                    primaryStage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Stage stage = (Stage) login.getScene().getWindow();
                stage.close();
            } else {
                //Non-admin User Subsystem
                if(UserStore.getInstance().login(name,password) || (name.equals("stock") && password.equals("stock"))){
                   if(name.equals("stock") && password.equals("stock")){
                       User user = new User("stock","stock");
                       Album album = new Album("stock");
                       File file1 =  new File("src/file/1.jpg");
                       File file2 =  new File("src/file/2.jpg");
                       File file3 =  new File("src/file/3.jpg");
                       File file4 =  new File("src/file/4.jpg");
                       File file5 =  new File("src/file/5.jpg");
                       Photo photo1 = new Photo("photo1",file1);
                       Photo photo2 = new Photo("photo2",file2);
                       Photo photo3 = new Photo("photo3",file3);
                       Photo photo4 = new Photo("photo4",file4);
                       Photo photo5 = new Photo("photo5",file5);
                       album.addPhoto(photo1);
                       album.addPhoto(photo2);
                       album.addPhoto(photo3);
                       album.addPhoto(photo4);
                       album.addPhoto(photo5);
                       user.addAlbum(album);
                       UserStore.getInstance().setUser(user);
                   }
                    Stage primaryStage = new Stage();
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/fxml/no-admin.fxml"));
                        primaryStage.setScene(new Scene(root, 1060, 762));
                        primaryStage.setTitle("No-Admin");
                        primaryStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Stage stage = (Stage) login.getScene().getWindow();
                    stage.close();
                }else{
                    alert("User name or password not correct.");
                }
            }
        }
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.titleProperty().set("Error");
        alert.headerTextProperty().set(message);
        alert.showAndWait();
    }
}
