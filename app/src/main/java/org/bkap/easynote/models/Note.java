package org.bkap.easynote.models;

public class Note {

    public Note() {}

    public Note(String title, String content, long addedDate) {
        this.title = title;
        this.content = content;
        this.addedDate = addedDate;
    }

    private String title;
    private String content;
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

    public long getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(long addedDate) {
        this.addedDate = addedDate;
    }
}
