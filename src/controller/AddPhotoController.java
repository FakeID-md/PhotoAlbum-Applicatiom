package controller;

import entity.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import manage.UserStore;

import java.io.File;
import java.io.IOException;

public class AddPhotoController {

    @FXML
    private TextField name,key,value;

    @FXML
    private Button add,cancel;

    private FileChooser photoChooser;

    private Photo uploadPhoto;

    @FXML
    public void initialize(){
        photoChooser = new FileChooser();
        photoChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("photo","*.jpg","*.png"));
        uploadPhoto = new Photo();
    }

    @FXML
    public void cancel(ActionEvent event){
        returnToPhoto();
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void upload(ActionEvent event){
        File file = photoChooser.showOpenDialog(null);
        if(file != null){
            uploadPhoto.setFile(file);
        }

    }

    private void returnToPhoto() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(AddPhotoController.class.getResource("/fxml/photo.fxml"));
            stage.setScene(new Scene(root,1110,820));
            stage.setTitle("Photo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void add(ActionEvent event){
        String photoName = name.getText().trim();
        if(photoName.isEmpty() || uploadPhoto.getFile() == null){
            alert("Please check photo name or file");
        } else{
            uploadPhoto.setPhotoName(photoName);
            String tagKey = key.getText().trim();
            String tagValue = value.getText().trim();
            if(tagKey.isEmpty() || tagValue.isEmpty()){
                alert("Tag can not be empty.");
            }else{
                uploadPhoto.addTag(tagKey,tagValue);
            }
            if(!UserStore.getInstance().addPhoto(uploadPhoto)){
                alert("Photo name is exist, Please change.");
            }else{
                returnToPhoto();
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
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
