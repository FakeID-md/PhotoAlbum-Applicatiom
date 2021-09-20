package controller;

import entity.Album;
import entity.Photo;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import manage.UserStore;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;

public class PhotoController {

    @FXML
    private ImageView photoView;

    @FXML
    private TextField caption,date,copyAlbum,moveAlbum,tagTxt,tagValue;

    @FXML
    private DatePicker fromDate,toDate;

    @FXML
    private TextArea tag;

    @FXML
    private RadioButton tagOrRadio,tagAndRadio,dateRadio;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @FXML
    private ListView<String> photos;

    @FXML
    private Button addPhoto,back,logout,editPhoto;

    private ObservableList<String> photoNames;

    private int index;

    private Photo selectPhoto;

    @FXML
    public void initialize() {
        UserStore.getInstance().initPhoto();
        photoNames = UserStore.getInstance().getPhotoInfos();
        photos.setItems(photoNames);
        date.setEditable(false);
        tag.setEditable(false);
        if(!photoNames.isEmpty()){
            photos.getSelectionModel().select(0);
            Photo photo = UserStore.getInstance().getPhoto(photoNames.get(0));
            if(photo != null){
                Image image = new Image("file:///"+photo.getFile().getAbsolutePath());
                photoView.setImage(image);
                caption.setText(photo.getPhotoName());
                date.setText(format.format(photo.getCalendar().getTime()));
                tag.setText(showPhotoTags(photo.getPhotoTags()));
            }
        }
        photos.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->{
            Photo photo = UserStore.getInstance().getPhoto(newValue);
            if(photo != null){
                index = photoNames.indexOf(newValue);
                Image image = new Image("file:///"+photo.getFile().getAbsolutePath());
                photoView.setImage(image);
                selectPhoto = photo;
                caption.setText(photo.getPhotoName());
                date.setText(format.format(photo.getCalendar().getTime()));
                tag.setText(showPhotoTags(photo.getPhotoTags()));
            }
        });
    }

    private String showPhotoTags(Map<String, String> tags){
        StringBuilder stringBuilder = new StringBuilder();
        for(String key : tags.keySet()){
            stringBuilder.append("Tag Name : "+ key+";   Tag Value : "+tags.get(key)+"\n");
        }
        return stringBuilder.toString();
    }

    @FXML
    public void addPhoto(ActionEvent event){
        toAddPhoto();
        Stage stage = (Stage) addPhoto.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void deletePhoto(ActionEvent event){
        String photoName = photos.getSelectionModel().getSelectedItem();
        if(photoName == null || photoName.isEmpty()){
            alert("Please select photo.","Error");
        } else {
            Photo photo = UserStore.getInstance().getPhoto(photoName);
            UserStore.getInstance().removePhoto(photo);
            if(photoNames.size() == 0){
                caption.setText("");
                date.setText("");
                tag.setText("");
                photoView.setImage(null);
            }
            reRender();
        }
    }

    private void reRender(){
        UserStore.getInstance().initPhoto();
        photoNames = UserStore.getInstance().getPhotoInfos();
        photos.setItems(photoNames);
    }

    @FXML
    public void next(ActionEvent event){
        if(photoNames.isEmpty() || index == photoNames.size()-1){
            alert("No next photo.","Error");
        } else{
            index++;
            photos.getSelectionModel().select(index);
            Photo photo = UserStore.getInstance().getPhoto(photoNames.get(index));
            selectPhoto = photo;
            Image image = new Image("file:///"+photo.getFile().getAbsolutePath());
            photoView.setImage(image);
            caption.setText(photo.getPhotoName());
            date.setText(format.format(photo.getCalendar().getTime()));
            tag.setText(showPhotoTags(photo.getPhotoTags()));
        }
    }

    @FXML
    public void previous(ActionEvent event){
        if(photoNames.isEmpty() || index == 0){
            alert("No previous picture","Error");
        } else{
            index--;
            photos.getSelectionModel().select(index);
            Photo photo = UserStore.getInstance().getPhoto(photoNames.get(index));
            selectPhoto = photo;
            Image image = new Image("file:///"+photo.getFile().getAbsolutePath());
            photoView.setImage(image);
            caption.setText(photo.getPhotoName());
            date.setText(format.format(photo.getCalendar().getTime()));
            tag.setText(showPhotoTags(photo.getPhotoTags()));
        }
    }

    @FXML
    public void editPhoto(ActionEvent event){
        String photoName = photos.getSelectionModel().getSelectedItem();
        if(photoName == null || photoName.isEmpty()){
            alert("Please select photo.","Error");
        } else {
            String name = caption.getText().trim();
            if(!name.equals(photoName)){
                if(UserStore.getInstance().getPhoto(name) != null){
                    alert("Photo name is exist!","Error");
                }else{
                    UserStore.getInstance().editPhoto(photoName,name);
                    reRender();
                }
            }
        }


    }

    @FXML
    public void addTag(ActionEvent event){
        String photoName = photos.getSelectionModel().getSelectedItem();
        if(photoName == null || photoName.isEmpty()){
            alert("Please select photo.","Error");
        } else {
            TextInputDialog inputDialog = new TextInputDialog("Add Tag");;
            inputDialog.setHeaderText(null);
            inputDialog.setContentText("Enter tag name and value(name:value):");
            Optional<String> result = inputDialog.showAndWait();
            if (result.isPresent()){
                String nameValue = result.get();
                String[] infos = nameValue.split(":");
                if(infos.length != 2){
                    alert("Please input format name:value","Error");
                }else{
                    Photo photo = UserStore.getInstance().getPhoto(photoName);
                    photo.addTag(infos[0],infos[1]);
                    tag.setText(showPhotoTags(photo.getPhotoTags()));
                }
            }
        }

    }

    @FXML
    public void back(ActionEvent event){
        Stage primaryStage = new Stage();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/no-admin.fxml"));
            primaryStage.setScene(new Scene(root, 1060, 762));
            primaryStage.setTitle("No-Admin");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = (Stage) back.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void logOut(ActionEvent event) throws IOException {
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
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void deleteTag(ActionEvent event){
        String photoName = photos.getSelectionModel().getSelectedItem();
        if(photoName == null || photoName.isEmpty()){
            alert("Please select photo.","Error");
        } else {
            Photo photo = UserStore.getInstance().getPhoto(photoName);
            TextInputDialog dialog = new TextInputDialog("Delete Tag");
            dialog.setTitle("Tag's Name");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter tag name:");
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()){
                String tagName = result.get();
                boolean isTrue = photo.removeTag(tagName);
                if(isTrue){
                    alert("Delete success.","Success");
                    tag.setText(showPhotoTags(photo.getPhotoTags()));
                }else{
                    alert("Tag not exist.","Error");
                }
            }
        }

    }

    private void alert(String message,String info) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.titleProperty().set(info);
        alert.headerTextProperty().set(message);
        alert.showAndWait();
    }


    @FXML
    public void copy(ActionEvent event){
        String copyName = copyAlbum.getText().trim();
        if(copyName.isEmpty()){
            alert("Please input copy album name","Error");
        } else{
            Album album = UserStore.getInstance().getAlbum(copyName);
            if(album == null || album.getAlbumName().equals(UserStore.getInstance().getAlbum().getAlbumName())){
                alert("Album is invalid.","Error");
            } else {
                String photoName = photos.getSelectionModel().getSelectedItem();
                if(photoName == null || photoName.isEmpty()){
                    alert("Please select photo.","Error");
                } else {
                    Photo photo = UserStore.getInstance().getPhoto(photoName);
                    album.addPhoto(new Photo(photo));
                    alert("Success.","Success");
                }
            }
        }
    }

    @FXML
    public void move(ActionEvent event){
        String moveName = moveAlbum.getText().trim();
        if(moveName.isEmpty()){
            alert("Please input move album name","Error");
        } else{
            Album album = UserStore.getInstance().getAlbum(moveName);
            if(album == null || album.getAlbumName().equals(UserStore.getInstance().getAlbum().getAlbumName())){
                alert("Album is invalid.","Error");
            } else {
                String photoName = photos.getSelectionModel().getSelectedItem();
                if(photoName == null || photoName.isEmpty()){
                    alert("Please select photo.","Error");
                } else {
                    Photo photo = UserStore.getInstance().getPhoto(photoName);
                    UserStore.getInstance().getAlbum().removePhoto(photo);
                    album.addPhoto(photo);
                    reRender();
                    alert("Success.","Success");
                }
            }
        }
    }

    @FXML
    public void search(ActionEvent event){
        if(tagOrRadio.isSelected()){
            String tagKeyString = tagTxt.getText().trim();
            String tagValueString = tagValue.getText().trim();
            if(tagKeyString.isEmpty() || tagValueString == null){
                alert("Please input info","Error");
            } else{
                List<Album> albums = UserStore.getInstance().getUser().getAlbums();
                List<Photo> photoList = new ArrayList<>();
                for(Album album : albums){
                    List<Photo> photosOfAlbum = album.getPhotos();
                    for(Photo photo : photosOfAlbum){
                        if((photo.getPhotoTags().containsKey(tagKeyString) || photo.getPhotoTags().containsValue(tagValueString))){
                            photoList.add(photo);
                        }
                    }
                }
                photoNames = FXCollections.observableArrayList();
                for(Photo photo : photoList){
                    photoNames.add(photo.getPhotoName());
                }
                photos.setItems(photoNames);
            }
        }  else if(tagAndRadio.isSelected()){
            String tagKeyString = tagTxt.getText().trim();
            String tagValueString = tagValue.getText().trim();
            if(tagKeyString.isEmpty() || tagValueString == null){
                alert("Please input info","Error");
            } else{
                List<Album> albums = UserStore.getInstance().getUser().getAlbums();
                List<Photo> photoList = new ArrayList<>();
                for(Album album : albums){
                    List<Photo> photosOfAlbum = album.getPhotos();
                    for(Photo photo : photosOfAlbum){
                        if((photo.getPhotoTags().containsKey(tagKeyString) && photo.getPhotoTags().containsValue(tagValueString))){
                            photoList.add(photo);
                        }
                    }
                }
                photoNames = FXCollections.observableArrayList();
                for(Photo photo : photoList){
                    photoNames.add(photo.getPhotoName());
                }
                photos.setItems(photoNames);
            }
        }else if(dateRadio.isSelected()){
            Date firstDate = Date.from(fromDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date secondDate = Date.from(toDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            if(fromDate.getValue() == null || toDate.getValue() == null) {
                alert("Please select Date","Error");
                return;
            }
            Calendar from = Calendar.getInstance();
            from.setTime(Date.from(fromDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            from.set(Calendar.HOUR_OF_DAY,23);
            from.set(Calendar.MINUTE,59);
            from.set(Calendar.SECOND,59);
            from.set(Calendar.MILLISECOND,999);
            Calendar to = Calendar.getInstance();
            to.setTime(Date.from(toDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            to.set(Calendar.HOUR_OF_DAY,23);
            to.set(Calendar.MINUTE,59);
            to.set(Calendar.SECOND,59);
            to.set(Calendar.MILLISECOND,999);
            List<Album> albums = UserStore.getInstance().getUser().getAlbums();
            List<Photo> photoList = new ArrayList<>();
            for(Album album : albums){
                List<Photo> photosOfAlbum = album.getPhotos();
                for(Photo photo : photosOfAlbum){
                    if(photo.getCalendar().after(from) && photo.getCalendar().before(to)){
                        photoList.add(photo);
                    }
                }
            }
            photoNames = FXCollections.observableArrayList();
            for(Photo photo : photoList){
                photoNames.add(photo.getPhotoName());
            }
            photos.setItems(photoNames);

        } else{
            alert("Please select tag or date","Error");
        }
    }

    private void toAddPhoto() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(AddPhotoController.class.getResource("/fxml/addPhoto.fxml"));
            stage.setScene(new Scene(root,660,530));
            stage.setTitle("Add Photo");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

