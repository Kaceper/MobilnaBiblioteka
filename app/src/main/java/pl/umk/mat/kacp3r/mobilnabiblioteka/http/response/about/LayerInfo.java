package pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LayerInfo
{
    @SerializedName("layers")
    @Expose
    private List<Layer> layers = null;

    public List<Layer> getLayers() {
        return layers;
    }

    public void setLayers(List<Layer> layers) {
        this.layers = layers;
    }
}