import React, { Component } from "react";
import MovieService from "../services/MovieService"
import "../css/style.css";
import CartService from "../services/CartService";

class MovieDetail extends Component {
  state = {
    data: {},
    resultCode: 0,
    quantity: 0
  };

  updateField = ({ target }) => {
    const { name, value } = target;

    this.setState({ [name]: value });
  };

  handleSubmit = e => {
      e.preventDefault();
      const {quantity} = this.state;

      CartService.insert(this.props["match"]["params"]["id"], quantity);
  }
  render() {
    const { data, resultCode, quantity } = this.state;
    console.log(this.props["match"]["params"]["id"]); // gets movie_id from uri
    if (resultCode === 0)
    {
      MovieService.getMovieDetail(this.props["match"]["params"]["id"])
      .then(response => {
        console.log(response);
        // if statement?
        this.setState({data:JSON.parse(response["headers"]["message"])["movie"],
                      resultCode: JSON.parse(response["headers"]["message"])["resultCode"]});
        console.log(data);


    });
    }

    return (
      <div>
        <h2 className = "detailTitle">{data.title}</h2>
        <img className = "detailImage" src ={(`https://image.tmdb.org/t/p/w500${data.poster_path}`)}/>
        <p>Director: {data.director}</p>
        <p>Year: {data.year}</p>
        <p>Rating: {data.rating}</p>
        <p>Overview: {data.overview}</p>

        <form className="cart-form" onSubmit={this.handleSubmit}>
            <label>Quantity</label>
            <input className="quantity-input"
              type="number"
              name="quantity"
              value={quantity}
              onChange={this.updateField}
            ></input>
            <button className="add-to-cart-btn">Add to cart</button>
        </form>
      </div>
    );
  }
}

export default MovieDetail;
