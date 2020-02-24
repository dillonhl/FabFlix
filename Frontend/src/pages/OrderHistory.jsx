import React, { Component } from "react";
import CartService from "../services/CartService"
import "../css/style.css";

class OrderHistory extends Component {
  state = {
    data: [],
    resultCode: 0,
    total: 0
  };

  render() {
    const { data, resultCode } = this.state;
    if (resultCode === 0)
    {
      CartService.history()
      .then(response => {
        console.log(response);
        // if statement?
        this.setState({data:JSON.parse(response["headers"]["message"])["items"],
                      resultCode: JSON.parse(response["headers"]["message"])["resultCode"]});

    });
    }

    return (
      <div>
        <h1 className ="logo">Order History</h1>
        

        <table className="table">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Avengers: Endgame</td>
                    <td>10</td>
                    <td>126.60</td>
                </tr>
            </tbody>
        </table>

        <h2>No time to finish, but I did get it to go through the gateway and into the backend. Ran out of time to parse through everything</h2>
        </div>

    );
  }
}

export default OrderHistory;