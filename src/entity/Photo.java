package entity;

import java.io.File;
import java.io.Serializable;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Class representing photos
 */
public class Photo implements Serializable,Comparable<Photo>{

    //Used to represent the date
    private Calendar calendar = Calendar.getInstance();

    //name of photo
    private String photoName;

    //tag map
    private Map<String, String> photoTags;

    //file of photo
    private File file;

    /**
     * Create an empty photo
     */
    public Photo(){
        this.photoTags = new HashMap<>();
    }

    /**
     * Create a new photo object
     * @param photo
     */
    public Photo(Photo photo){
        this.photoName = photo.getPhotoName();
        this.file = photo.getFile();
        this.photoTags = photo.getPhotoTags();
        this.calendar = photo.getCalendar();
    }

    /**
     * Create a photo object
     * @param photoName
     * @param file
     */
    public Photo(String photoName,File file){
        calendar.setTimeInMillis(file.lastModified());
        this.photoName = photoName;
        this.photoTags = new HashMap<>();
        this.file = file;
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getPhotoName() {
        return photoName;
    }

    public Map<String, String> getPhotoTags() {
        return photoTags;
    }

    public void addTag(String tagName, String tagValue) {
        photoTags.put(tagName,tagValue);
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
        calendar.setTimeInMillis(file.lastModified());
    }

    public boolean removeTag(String tagName) {
        if(photoTags.containsKey(tagName)){
            photoTags.remove(tagName);
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(Photo o) {
        return getCalendar().compareTo(o.getCalendar());
    }
}

