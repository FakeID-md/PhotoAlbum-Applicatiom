package controller;

import entity.Album;
import entity.Photo;
import entity.User;
import javafx.beans.value.ObservableValue;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NoAdminController {

    @FXML
    private ListView<String> albums;

    @FXML
    private TextField albumName,fromDate,toDate,numberOfPhotos;

    @FXML
    private Button enter,logOut;

    private User user;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @FXML
    public void initialize() {
        UserStore.getInstance().initAlbum();
        albums.setItems(UserStore.getInstance().getAlbumInfos());
        user = UserStore.getInstance().getUser();
        fromDate.setEditable(false);
        toDate.setEditable(false);
        numberOfPhotos.setEditable(false);
        albums.getSelectionModel().selectedItemProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
                    Album album = UserStore.getInstance().getAlbum(newValue);
                    if(album != null){
                        albumName.setText(album.getAlbumName());
                        fromDate.setText(album.getFromDate() == null ? "" : format.format(album.getFromDate().getTime()));
                        toDate.setText(album.getToDate() == null ? "" : format.format(album.getToDate().getTime()));
                        numberOfPhotos.setText(album.getPhotosNum()+"");
                    }
                });
    }


    @FXML
    public void addAlbum(ActionEvent event){
        TextInputDialog inputDialog = new TextInputDialog("album");;
        inputDialog.setHeaderText(null);
        inputDialog.setContentText("Enter album name:");
        Optional<String> result = inputDialog.showAndWait();
        if (result.isPresent()){
            String name = result.get();
            Album album = new Album(name);
            if(user.add(album)){
                UserStore.getInstance().addAlbum(album);
            }else{
                alert("Album already exists.");
            }
        }
    }

    @FXML
    public void deleteAlbum(ActionEvent event){
        String selectName = albums.getSelectionModel().getSelectedItem();
        if(selectName == null || selectName.isEmpty()){
            alert("Please select album.");
        } else {
            Album album = user.getAlbum(selectName);
            UserStore.getInstance().removeAlbum(album);
        }
    }

    @FXML
    public void editAlbum(ActionEvent event){
        String selectName = albums.getSelectionModel().getSelectedItem();
        if(selectName == null || selectName.isEmpty()){
            alert("Please select album.");
        } else {
            String newName = albumName.getText().trim();
            Album album = user.getAlbum(selectName);
            if(!newName.isEmpty() && !album.getAlbumName().equals(newName)){
                UserStore.getInstance().editAlbum(album.getAlbumName(),newName);
            } else{
                alert("Album already exists.");
            }
        }
        albums.setItems(UserStore.getInstance().getAlbumInfos());
    }

    @FXML
    public void enterAlbum(ActionEvent event){
        String selectName = albums.getSelectionModel().getSelectedItem();
        if(selectName == null || selectName.isEmpty()){
            alert("Please select album.");
        } else {
            Album album = user.getAlbum(selectName);
            UserStore.getInstance().setAlbum(album);
            toPhotoPage();
        }

    }

    @FXML
    public void logOut(ActionEvent event){
        try {
            UserStore.getInstance().save();
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(NoAdminController.class.getResource("/fxml/login.fxml"));
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

    private void toPhotoPage(){
        Stage primaryStage = new Stage();
        try {
            Parent root = FXMLLoader.load(NoAdminController.class.getResource("/fxml/photo.fxml"));
            primaryStage.setScene(new Scene(root,1110,820));
            primaryStage.setTitle("Photo");
            primaryStage.show();
            Stage stage = (Stage) enter.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
