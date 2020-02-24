package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class MovieSearchResponseModel extends ResponseModel {
    @JsonProperty("movies")
    private MovieModel[] movies;

    public MovieSearchResponseModel() {}

    public MovieModel[] getMovies() {
        return movies;
    }

    public void setMovies(MovieModel[] movies)
    {
        if (movies == null)
        {
            this.setResult(Result.INTERNAL_SERVER_ERROR);
        }
        else if (movies.length == 0)
        {
            this.setResult(Result.NO_MOVIE_FOUND);
        }
        else
        {
            this.setResult(Result.FOUND_MOVIES);
        }

        this.movies = movies;
    }

}
