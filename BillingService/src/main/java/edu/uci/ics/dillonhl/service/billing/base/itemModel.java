package edu.uci.ics.dillonhl.service.billing.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class itemModel {
    @JsonProperty(value = "email", required = true)
    private String email;
    @JsonProperty(value = "movie_id", required = true)
    private String movie_id;
    @JsonProperty(value = "quantity", required = true)
    private int quantity;
    @JsonProperty(value = "unit_price", required = true)
    private float unit_price;
    @JsonProperty(value = "discount", required = true)
    private float discount;
    @JsonProperty(value = "sale_date")
    private String sale_date;

    private String movie_title;
    private String backdrop_path;

    private String poster_path;

    public itemModel() {}

    public itemModel( @JsonProperty(value="email", required=true)String email,
                      @JsonProperty(value = "unit_price", required=true) Float unit_price,
                      @JsonProperty(value="discount", required=true) Float discount,
                      @JsonProperty(value="quantity", required=true) Integer quantity,
                      @JsonProperty(value="movie_id", required=true) String movie_id,
                      @JsonProperty(value="movie_title", required = true) String movie_title,
                      @JsonProperty(value="backdrop_path") String backdrop_path,
                      @JsonProperty(value="poster_path") String poster_path) {
        this.email = email;
        this.unit_price = unit_price;
        this.discount = discount;
        this.quantity = quantity;
        this.movie_id = movie_id;
        this.movie_title = movie_title;
        this.backdrop_path = backdrop_path;
        this.poster_path = poster_path;
    }

    public String getEmail() {
        return email;
    }

    public Float getUnit_price() {
        return unit_price;
    }

    public Float getDiscount() {
        return discount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public String getMovie_title() {
        return movie_title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getSale_date() {
        return sale_date;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setUnit_price(float unit_price) {
        this.unit_price = unit_price;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public void setSale_date(String sale_date) {
        this.sale_date = sale_date;
    }

    public void setMovie_title(String movie_title) {
        this.movie_title = movie_title;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }
}
