package pl.umk.mat.kacp3r.mobilnabiblioteka.model;

import io.realm.RealmObject;

public class Categories extends RealmObject
{
    private String category;

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }
}
