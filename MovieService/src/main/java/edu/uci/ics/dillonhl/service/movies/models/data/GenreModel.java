package edu.uci.ics.dillonhl.service.movies.models.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GenreModel {
    @JsonProperty("genre_id")
    private int genre_id;
    @JsonProperty("name")
    private String name;

    public GenreModel() {}

    public int getGenre_id() {
        return genre_id;
    }

    public void setGenre_id(int genre_id) {
        this.genre_id = genre_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
