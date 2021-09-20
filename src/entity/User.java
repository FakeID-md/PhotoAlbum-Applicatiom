package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String name;

    private String password;

    private List<Album> albums;

    public User(String name,String password){
        this.name = name;
        this.password = password;
        this.albums = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public Album getAlbum(String name) {
        for(Album album : albums){
            if(album.getAlbumName().equals(name)){
                return  album;
            }
        }
        return null;
    }

    public boolean add(Album album){
        Album album1 = getAlbum(album.getAlbumName());
        if(album1 == null){
            return true;
        }
        return false;
    }

    public void addAlbum(Album album){
        if(add(album)){
            albums.add(album);
        }
    }

    public void remove(Album album){
        albums.remove(album);
    }
}

