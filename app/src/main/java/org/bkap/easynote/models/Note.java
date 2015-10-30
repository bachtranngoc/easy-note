package org.bkap.easynote.models;

public class Note {

    public Note() {}

    public Note(String title, String content, long addedDate) {
        this.title = title;
        this.content = content;

        this.addedDate = addedDate;
    }

    public Note(String title, String content, String image, String sound, long addedDate) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.sound = sound;
        this.addedDate = addedDate;
    }

    private String title;
    private String content;
    private String image;
    private String sound;
    private long addedDate;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }
}
