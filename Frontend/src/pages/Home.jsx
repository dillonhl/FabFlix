import React, { Component } from "react";
import "../css/style.css";

class Home extends Component {
  state = {};
  render() {
    return (
      <div className = "container">
        <h1>FabFlix</h1>
        <p>Login or create an account to start</p> 
      </div>
    );
  }
}

export default Home;
