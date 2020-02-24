package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.models.data.ThumbnailModel;

public class ThumbnailRequestModel {
    @JsonProperty("movie_ids")
    private String[] movie_ids;

    public String[] getMovie_ids() {
        return movie_ids;
    }

    public ThumbnailRequestModel(){}

    @JsonCreator
    public ThumbnailRequestModel(String[] movie_ids)
    {
        this.movie_ids = movie_ids;
    }

    public void setMovie_ids(String[] movie_ids) {
        this.movie_ids = movie_ids;
    }
}
