package pl.umk.mat.kacp3r.mobilnabiblioteka.model;

import io.realm.RealmObject;

public class Authors extends RealmObject
{
    private String author;

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }
}
