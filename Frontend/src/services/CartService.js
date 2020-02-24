import Socket from "../util/Socket";
import { cartEPs } from "../Config.json";
import Axios from "axios";
import Cookies from "js-cookie";

const { retrieveEP, insertEP, placeEP, historyEP } = cartEPs;

async function retrieve()
{
    const { common } = Axios.defaults.headers;
    common["email"] = Cookies.get("email");
    const payload = {
        email: common["email"]
    };

    return await Socket.POST(retrieveEP, payload);
}

async function insert(movie_id, quantity)
{
    const { common } = Axios.defaults.headers;
    common["email"] = Cookies.get("email");

    const payload = {
        email: common["email"],
        movie_id: movie_id,
        quantity: quantity
    }

    return await Socket.POST(insertEP, payload)
}

async function orderPlace()
{
    const { common } = Axios.defaults.headers;
    common["email"] = Cookies.get("email");

    const payload = {
        email: common["email"]
    }

    return await Socket.POST(placeEP, payload)

}

async function history()
{
    const { common } = Axios.defaults.headers;
    common["email"] = Cookies.get("email");

    const payload = {
        email: common["email"]
    }

    return await Socket.POST(historyEP, payload)}

export default {
    retrieve, insert, orderPlace, history
};