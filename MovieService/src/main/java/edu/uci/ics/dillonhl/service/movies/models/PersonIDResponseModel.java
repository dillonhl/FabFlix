package edu.uci.ics.dillonhl.service.movies.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.uci.ics.dillonhl.service.movies.base.ResponseModel;
import edu.uci.ics.dillonhl.service.movies.models.data.PersonModel;

public class PersonIDResponseModel extends ResponseModel {
    @JsonProperty(value = "person", required = true)
    PersonModel person;

    public PersonIDResponseModel() {}

    public PersonModel getPerson() {
        return person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }
}
