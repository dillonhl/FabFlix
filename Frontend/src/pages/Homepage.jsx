import React, { Component } from "react";
import { NavLink } from "react-router-dom";
import "../css/style.css";


class Homepage extends Component {
  state = {};

  routeToBrowse() {
    let path = "/movie/browse"
    this.props.history.push(path)
  };

  render() {
    return (
      <div>
        <h1 className= "homeLogo">FabFlix</h1>

        <NavLink className="nav-link nav-link-browse" to="/movie/browse">
            Search For Movies Using Keywords
            </NavLink>

        <NavLink className="nav-link nav-link-search" to="/movie/search">
            Search For Movies Using Movie Details
            </NavLink>

        <NavLink className ="history-link" to="/order/history">
          View order history
        </NavLink>

        
      </div>
    );
  }
}

export default Homepage;
