package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Class used to represent albums
 */
public class Album implements Serializable {
    //name of album
    private String albumName;

    //Photos in the album
    private List<Photo> photos;

    /**
     * Construction method to create a new photo album
     * @param albumName name of album
     */
    public Album(String albumName){
        this.albumName = albumName;
        photos = new ArrayList<>();
    }

    /**
     * Get the name of the album
     * @return  the name of the album
     */
    public String getAlbumName() {
        return albumName;
    }

    /**
     * Set the name of the album
     * @param albumName name of album
     */
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    /**
     * get photos in album
     * @return
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * Determine whether you can add an album
     * @param name
     * @return true if false not
     */
    public boolean addPhoto(String name){
        for(Photo photoTemp : photos){
            if(photoTemp.getPhotoName().equals(name)){
                return false;
            }
        }
        return true;
    }

    /**
     * add photo to this album
     * @param photo
     */
    public void addPhoto(Photo photo){
        photos.add(photo);
    }

    /**
     * get number of photos in this album
     * @return
     */
    public int getPhotosNum(){
        return  photos.size();
    }

    /**
     * get from date
     * @return
     */
    public Calendar getFromDate() {
        if(!photos.isEmpty()){
            Collections.sort(photos);
            return photos.get(0).getCalendar();
        }
        return null;
    }

    /**
     * get to date
     * @return
     */
    public Calendar getToDate() {
        if(!photos.isEmpty()){
            Collections.sort(photos);
            return photos.get(photos.size()-1).getCalendar();
        }
        return null;
    }

    /**
     * remove photo
     * @param photo
     */
    public void removePhoto(Photo photo){
        photos.remove(photo);
    }
}
