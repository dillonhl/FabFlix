import React, { Component, Fragment } from "react";
import { NavLink } from "react-router-dom";

import "./css/style.css";

class NavBar extends Component {
  render() {
    const { handleLogOut, loggedIn, handleCart } = this.props;

    return (
      <nav className="nav-bar">
    
        {!loggedIn && (
          <Fragment>
            <NavLink className="nav-link home-btn" to="/">
              FabFlix
            </NavLink>
            <NavLink className="nav-link reg-btn" to="/register">
              Register
            </NavLink>
            <NavLink className="nav-link login-btn" to="/login">
              Login
            </NavLink>
          </Fragment>
        )}
        {loggedIn && (
          <Fragment>
            <NavLink className="nav-link home-btn" to="/home">
             FabFlix
            </NavLink>
            <NavLink onClick={handleCart} className="nav-link cart-btn" to="/cart">
              Cart
            </NavLink>
            <NavLink onClick={handleLogOut} className="nav-button logout" to="/">
              Log Out
            </NavLink>
          </Fragment>
        )}
      </nav>
    );
  }
}

export default NavBar;
