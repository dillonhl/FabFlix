import React, { Component } from "react";

import Idm from "../services/Idm";

import "../css/common.css";

class Login extends Component {
  state = {
    email: "",
    password: "",
    resultCode: 0
  };

  handleSubmit = e => {
    e.preventDefault();

    const { handleLogIn } = this.props;
    const { email, password } = this.state;

    Idm.login(email, password)
      .then(response => {
        console.log(response);
        console.log(JSON.parse(response["headers"]["message"])["resultCode"]);
        if (JSON.parse(response["headers"]["message"])["resultCode"] === 120)
        {
          handleLogIn(email, JSON.parse(response["headers"]["message"])["session_id"]);
          this.props.history.push('/home');
        }
        
        this.setState({resultCode: JSON.parse(response["headers"]["message"])["resultCode"]});
      })
      .catch(error => console.log(error));
  };

  updateField = ({ target }) => {
    const { name, value } = target;

    this.setState({ [name]: value });
  };

  render() {
    const { email, password, resultCode } = this.state;

    return (
      <div>
        <h1>Login</h1>
        <form onSubmit={this.handleSubmit}>
          <label className="label">Email</label>
          <input
            className="input"
            type="email"
            name="email"
            value={email}
            onChange={this.updateField}
          ></input>
          <label className="label">Password</label>
          <input
            className="input"
            type="password"
            name="password"
            value={password}
            onChange={this.updateField}
          ></input>
          <button className="button">Login</button>
        </form>

        {resultCode === 14 && <p className="error"> Error logging in, try again.</p>}

      </div>
    );
  }
}

export default Login;
