package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.data.PersonModel;

public class PeopleResponseModel extends ResponseModel {
    @JsonProperty("people")
    private PersonModel[] people;

    public PeopleResponseModel() {}

    public PersonModel[] getPeople() {
        return people;
    }

    public void setPeople(PersonModel[] people) {
        this.people = people;
    }
}
