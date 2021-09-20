package controller;

import entity.Album;
import entity.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import manage.UserStore;

import java.io.IOException;
import java.util.List;


public class AdminController {
	@FXML
    private ListView<String> userList;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button create,delete,logOut;


    @FXML
    public void initialize() {
        userList.setItems(UserStore.getInstance().getUserInfos());
    }

    @FXML
    public void onCreate() {
        String name = userNameField.getText().trim();
        String password = passwordField.getText().trim();
        if (name.isEmpty() || password.isEmpty()) {
            alert("Name or Password can not be empty.");
        } else {
            User user = UserStore.getInstance().getUser(name);
            if (user != null) {
                alert("User already exists");
            }else{
                UserStore.getInstance().add(name,password);
//                list.setItems(DataManager.getInstance().getUsers());
                clear();
            }
        }

    }

    public void clear(){
        userNameField.clear();
        passwordField.clear();
    }

    @FXML
    public void onDelete(){
        String userSelection = userList.getSelectionModel().getSelectedItem();
//        List<Album> albums = DataManager.getInstance().getAlbumList(user);
//        for(Album album : albums){
//            DataManager.getInstance().removePhotoList(album.getPhotos());
//        }
//        DataManager.getInstance().removeAlbumList(albums);
        if(userSelection == null || userSelection.isEmpty()){
            alert("Please select user.");
        } else {
            String[] infos = userSelection.split(" ");
            UserStore.getInstance().delete(infos[0]);
//            list.setItems(DataManager.getInstance().getUsers());
        }
    }

    @FXML
    public void logOut(ActionEvent event){
        try {
            UserStore.getInstance().save();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(AdminController.class.getResource("/fxml/login.fxml"));
            primaryStage.setScene(new Scene(root, 631, 428));
            primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    try {
                        UserStore.getInstance().save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
            primaryStage.setTitle("Album");
            primaryStage.show();
            Stage stage = (Stage) logOut.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.titleProperty().set("Error");
        alert.headerTextProperty().set(message);
        alert.showAndWait();
    }
}
