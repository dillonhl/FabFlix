import React, { Component } from "react";
import CartService from "../services/CartService"
import { NavLink } from "react-router-dom";

import "../css/style.css";

class Cart extends Component {
  state = {
    data: [],
    resultCode: 0,
    total: 0
  };

  handleCheckout = e => {
    e.preventDefault();
    CartService.orderPlace().then(
      response=> {
        console.log(response);
        window.open(JSON.parse(response["headers"]["message"])["approve_url"])

      }
    )
  }

  render() {
    const { data, resultCode, total } = this.state;
    if (resultCode === 0)
    {
      CartService.retrieve()
      .then(response => {
        console.log(response);
        // if statement?
        this.setState({data:JSON.parse(response["headers"]["message"])["items"],
                      resultCode: JSON.parse(response["headers"]["message"])["resultCode"]});

    });
    }

    return (
      <div>
        <h1 className ="logo">Cart</h1>

        {data == null &&
          <h2>No Items in Cart</h2>}

        {data !== null && 
          <table className="table">
            <thead>
              <tr>
                <td>Thumbnail</td>
                <td>Movie</td>
                <td>Price</td>
                <td>Quantity</td>
                <td>Total</td>
              </tr>
            </thead>
            <tbody>
              {data.map(entry => (
                <tr align="right">
                  <td><img className="thumbnail" src ={(`https://image.tmdb.org/t/p/w200${entry.poster_path}`)}/></td>

                  <td>{entry.movie_title}</td>
                  <td>${entry.unit_price}</td>
                  <td>{entry.quantity}</td>
                  <td>{(entry.quantity * entry.unit_price).toFixed(2)}</td>
                </tr>
                
              ))}
            </tbody>
          </table>}

          <button className="checkout-btn" 
          onClick={this.handleCheckout}>Checkout</button>


        <NavLink className ="history-link" to="/order/history">
          View order history
        </NavLink>
      </div>
    );
  }
}

export default Cart;
