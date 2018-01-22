package pl.umk.mat.kacp3r.mobilnabiblioteka.http.response.about;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VolumeResponse
{
    @SerializedName("kind")
    @Expose
    private String kind;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("etag")
    @Expose
    private String etag;

    @SerializedName("selfLink")
    @Expose
    private String selfLink;

    @SerializedName("volumeInfo")
    @Expose
    private VolumeInfo volumeInfo;

    @SerializedName("layerInfo")
    @Expose
    private LayerInfo layerInfo;

    @SerializedName("saleInfo")
    @Expose
    private SaleInfo saleInfo;

    @SerializedName("accessInfo")
    @Expose
    private AccessInfo accessInfo;

    public String getKind()
    {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfo getVolumeInfo() {
        return volumeInfo;
    }

    public void setVolumeInfo(VolumeInfo volumeInfo) {
        this.volumeInfo = volumeInfo;
    }

    public LayerInfo getLayerInfo() {
        return layerInfo;
    }

    public void setLayerInfo(LayerInfo layerInfo) {
        this.layerInfo = layerInfo;
    }

    public SaleInfo getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(SaleInfo saleInfo) {
        this.saleInfo = saleInfo;
    }

    public AccessInfo getAccessInfo() {
        return accessInfo;
    }

    public void setAccessInfo(AccessInfo accessInfo) {
        this.accessInfo = accessInfo;
    }
}