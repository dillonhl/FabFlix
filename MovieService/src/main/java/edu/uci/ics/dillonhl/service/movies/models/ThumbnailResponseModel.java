package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.data.ThumbnailModel;

public class ThumbnailResponseModel extends ResponseModel {
    @JsonProperty("thumbnails")
    private ThumbnailModel[] thumbnails;

    public ThumbnailResponseModel() {}

    public ThumbnailModel[] getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailModel[] thumbnails) {
        this.thumbnails = thumbnails;
    }


}
