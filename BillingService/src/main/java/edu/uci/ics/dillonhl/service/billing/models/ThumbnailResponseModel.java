package edu.uci.ics.dillonhl.service.billing.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.billing.base.ResponseModel;
import edu.uci.ics.dillonhl.service.billing.base.ThumbnailModel;

public class ThumbnailResponseModel extends ResponseModel {
    @JsonProperty("thumbnails")
    private ThumbnailModel[] thumbnails;

    @JsonProperty("message")
    private String message;

    @JsonProperty("resultCode")
    private int resultCode;

    public ThumbnailResponseModel() {}

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getResultCode() {
        return resultCode;
    }

    public ThumbnailModel[] getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailModel[] thumbnails) {
        this.thumbnails = thumbnails;
    }


}
