package manage;

import entity.Album;
import entity.Photo;
import entity.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserStore {

    private static UserStore userStore = null;

    private final String userFile = "src/file/user.ser";

    private List<User> users;

    private ObservableList<String> userInfos;

    private ObservableList<String> albumInfos;

    private ObservableList<String> photoInfos;

    private List<Album> albums;

    public User user;

    public Album album;

    private List<Photo> photos;

    private UserStore(){
        users = new ArrayList<>();
        photos = new ArrayList<>();
        read(userFile);
        userInfos = FXCollections.observableArrayList();
        for(User user : users){
            userInfos.add(user.getName()+" "+user.getPassword());
        }
    }

    public void initAlbum(){
        albums = user.getAlbums();
        albumInfos = FXCollections.observableArrayList();
        for(Album album : albums){
            albumInfos.add(album.getAlbumName());
        }
    }
    public ObservableList<String> getAlbumInfos() {
        return albumInfos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
    public ObservableList<String> getUserInfos() {
        return userInfos;
    }

    public static UserStore getInstance(){
        if(userStore == null){
            userStore = new UserStore();
        }
        return  userStore;
    }

    public void save() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(userFile));
        objectOutputStream.writeObject(users);
    }

    public void read(String fileName){
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            users = (ArrayList) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException i){

        }
    }

    private boolean containUserName(String name){
        for(User user : users){
            if(user.getName().equals(name)){
                return  true;
            }
        }
        return false;
    }
    public boolean add(String name,String password){
        if(containUserName(name)){
            return true;
        }
        User user = new User(name,password);
        users.add(user);
        userInfos.add(name+" "+password);
        return false;
    }

    public User getUser(String userName){
        for(User user : users){
            if(user.getName().equals(userName)){
                return user;
            }
        }
        return  null;
    }

    public void delete(String name){
        User user = getUser(name);
        if(user != null){
            users.remove(user);
            userInfos.remove(name+" "+user.getPassword());
        }
    }

    public boolean login(String name,String password){
        for(User user : users){
            if(user.getName().equals(name) && user.getPassword().equals(password)){
                setUser(user);
                return true;
            }
        }
        return false;
    }
    public void addAlbum(Album album){
        if(user.add(album)){
            albumInfos.add(album.getAlbumName());
            user.getAlbums().add(album);
        }
    }

    public Album getAlbum() {
        return album;
    }

    public Album getAlbum(String name) {
        for(Album album : albums){
            if(album.getAlbumName().equals(name)){
                return  album;
            }
        }
        return null;
    }

    public void editAlbum(String oldName, String newName){
        Album album = getAlbum(oldName);
        album.setAlbumName(newName);
        initAlbum();
    }

    public void removeAlbum(Album album){
        albumInfos.remove(album.getAlbumName());
        user.getAlbums().remove(album);
    }

    public boolean addPhoto(Photo photo){
        if(album.addPhoto(photo.getPhotoName())){
            photoInfos.add(album.getAlbumName());
            album.getPhotos().add(photo);
            return true;
        }
        return false;
    }

    public Photo getPhoto(String name) {
        for(Photo photo: photos){
            if(photo.getPhotoName().equals(name)){
                return photo;
            }
        }
        return null;
    }

    public void removePhoto(Photo photo){
        photoInfos.remove(photo.getPhotoName());
        album.getPhotos().remove(photo);
    }

    public void editPhoto(String oldName, String newName){
        Photo photo = getPhoto(oldName);
        photo.setPhotoName(newName);
        initAlbum();
    }

    public void initPhoto(){
        photos = album.getPhotos();
        photoInfos = FXCollections.observableArrayList();
        for(Photo photo : photos){
            photoInfos.add(photo.getPhotoName());
        }
    }

    public ObservableList<String> getPhotoInfos() {
        return photoInfos;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}

