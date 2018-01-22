package pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Layer
{
    @SerializedName("layerId")
    @Expose
    private String layerId;

    @SerializedName("volumeAnnotationsVersion")
    @Expose
    private String volumeAnnotationsVersion;

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getVolumeAnnotationsVersion() {
        return volumeAnnotationsVersion;
    }

    public void setVolumeAnnotationsVersion(String volumeAnnotationsVersion) {
        this.volumeAnnotationsVersion = volumeAnnotationsVersion;
    }
}