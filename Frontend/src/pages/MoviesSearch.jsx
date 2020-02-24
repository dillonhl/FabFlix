import React, { Component } from "react";
import { NavLink } from "react-router-dom";

import MovieService from "../services/MovieService";
import "../css/common.css";
import "../css/style.css";


class MoviesSearch extends Component {
    state = {
      data: [], title: "", year: "",
      director: "", genre: "", 
      limit: "10", offset: "0",
      orderby: "title", direction: "asc"
    };


    updateField = ({ target }) => {
      const { name, value } = target;
  
      this.setState({ [name]: value });
    };

    handleSubmit = e => {
      e.preventDefault();
      const {title, year, director, genre, limit, offset, direction, orderby} = this.state;
  
      MovieService.search(title, year, director, genre,
        limit, offset, direction, orderby)
      .then(response => {
        console.log(response);
        // want to insert movies into data here
        this.setState({data:JSON.parse(response["headers"]["message"])["movies"]});
        
        
        //console.log(JSON.parse(this.state.data));
      })
  
    };

  render() {
    const { data, title, year, director, genre, limit, offset, orderby, direction } = this.state;

    return (
      <div>
        <h1>Movie Search</h1>
        <form onSubmit={this.handleSubmit}>
          <table>
            <tbody>
              <tr>
                <td><label className="labels">Title</label></td>
                <td><label className="labels">Director</label></td>
              </tr>
                <tr>
                  <td><input
                    className="inputs"
                    type="text"
                    name="title"
                    value={title}
                    onChange={this.updateField}
                    ></input> 
                  </td>
                  <td><input
                    className="inputs"
                    type="text"
                    name="director"
                    value={director}
                    onChange={this.updateField}
                    ></input>
                  </td>
                </tr>

              <tr>
                <td><label className="labels">Year</label></td>
                <td><label className="labels">Genre</label></td>
              </tr>
              <tr>
                <td><input
                      className="inputs"
                      type="text"
                      name="year"
                      value={year}
                      onChange={this.updateField}
                    ></input>
                </td>
                <td><input
                      className="inputs"
                      type="text"
                      name="genre"
                      value={genre}
                      onChange={this.updateField}
                    ></input> 
                </td>
              </tr>
          </tbody></table>

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

export default MoviesSearch;
