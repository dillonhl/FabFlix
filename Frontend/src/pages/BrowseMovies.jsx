import React, { Component } from "react";
import { NavLink } from "react-router-dom";

import MovieService from "../services/MovieService";
import "../css/style.css";


class BrowseMovies extends Component {
  state = {
    keywords: "",
    limit: "10",
    offset: "0",
    direction: "asc",
    orderby: "title",
    data: []
  };
  
  updateField = ({ target }) => {
    const { name, value } = target;

    this.setState({ [name]: value });
  };

  handleSubmit = e => {
    e.preventDefault();
    const {keywords, limit, offset, direction, orderby} = this.state;

    MovieService.browse(keywords, limit, offset, direction, orderby)
    .then(response => {
      console.log(response);
      // want to insert movies into data here
      this.setState({data:JSON.parse(response["headers"]["message"])["movies"]});
      
      
      //console.log(JSON.parse(this.state.data));
    })

  }

  render() {
    const { data, keywords, limit, offset, direction, orderby } = this.state;

    return (
      <div>
        <h1>Browse for Movies</h1>
        <form onSubmit={this.handleSubmit}>
          <label className="label">Keywords</label>
          <input
            className="input"
            type="text"
            name="keywords"
            value={keywords}
            onChange={this.updateField}
          ></input>
          <label className = "select-text">Limit</label>
          <select className = "select" name="limit" limit = {this.state.limit} onChange={this.updateField}>
            <option value="10">10</option>
            <option value="25">25</option>
            <option value="50">50</option>
            <option value="100">100</option>
          </select>

          <label className = "select-text">Offset</label>
          <select className = "select" name="offset" offset = {this.state.offset} onChange={this.updateField}>
            <option value="0">0</option>
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="15">15</option>
            <option value="20">20</option>
          </select>

          <label className = "select-text">Order By</label>
          <select className = "select" name="orderby" orderby = {this.state.orderby} onChange={this.updateField}>
            <option value="title">title</option>
            <option value="year">year</option>
            <option value="rating">rating</option>
          </select>

          <label className = "select-text">Direction</label>
          <select className = "select" name="direction" direction = {this.state.direction} onChange={this.updateField}>
            <option value="ASC">Ascending</option>
            <option value="DESC">Descending</option>
          </select>
          <button className="search-btn">Search</button>
        </form>

        {data.length !== 0 && <h2 className = "moviesHeader">Movies</h2>}

        <table className="table">
          <tbody>
            {data.length !== 0 && 
              data.map(entry => (
                <tr>
                  <NavLink to={`/movie/get/${entry.movie_id}`}>{entry.title} </NavLink>
                </tr>
              ))
            }
          </tbody>
          </table>
      </div>
    );
  }
}

export default BrowseMovies;
