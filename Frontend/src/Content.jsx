import React, { Component } from "react";
import { Route, Switch } from "react-router-dom";

import Login from "./pages/Login";
import BrowseMovies from "./pages/BrowseMovies";
import MoviesSearch from "./pages/MoviesSearch";
import MovieDetail from "./pages/MovieDetail";
import Home from "./pages/Home";
import Cart from "./pages/Cart";
import Homepage from "./pages/Homepage";
import Register from "./pages/Register"
import OrderHistory from "./pages/OrderHistory"

class Content extends Component {
  render() {
    const { handleLogIn } = this.props;

    return (
      <div className="content">
        <Switch>
          <Route path="/login"
            component={props => <Login handleLogIn={handleLogIn} {...props} />}
          />
          <Route path="/register" component={Register} />
          <Route path="/movie/browse" component={BrowseMovies} />
          <Route path="/movie/search" component={MoviesSearch} />
          <Route path={`/movie/get/:id?`} component={MovieDetail} />
          <Route path="/cart" component={Cart} />
          <Route path="/order/history" component={OrderHistory}/>
          <Route path="/home" component={Homepage} />
          <Route path="/" component={Home} />
        </Switch>
      </div>
    );
  }
}

export default Content;
