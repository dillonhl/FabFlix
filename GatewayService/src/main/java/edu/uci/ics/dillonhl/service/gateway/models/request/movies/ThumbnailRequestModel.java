package edu.uci.ics.dillonhl.service.gateway.models.request.movies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
