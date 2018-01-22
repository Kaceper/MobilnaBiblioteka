package pl.umk.mat.kacp3r.mobilnabiblioteka.model;

import io.realm.RealmObject;

public class IndustryIdentifier extends RealmObject
{
    private String type;

    private String identifier;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
