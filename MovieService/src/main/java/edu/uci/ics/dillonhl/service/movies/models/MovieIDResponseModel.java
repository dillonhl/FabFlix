package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MovieIDResponseModel extends ResponseModel {
    @JsonProperty(value = "movie", required = true)
    private MovieModel movie;

    public MovieModel getMovie() {
        return movie;
    }

    public void setMovie(MovieModel movie) {
        this.movie = movie;
    }
}
