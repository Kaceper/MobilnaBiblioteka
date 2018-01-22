package pl.umk.mat.kacp3r.mobilnabiblioteka.model;

import io.realm.RealmObject;

public class ImageLinks extends RealmObject
{
    private String smallThumbnail;

    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
