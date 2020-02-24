import Socket from "../util/Socket";
import { movieEPs } from "../Config.json";

const { browseEP, movieSearchEP, movieidEP } = movieEPs;

async function browse(keywords, limit, offset, direction, orderby) {
  const newBrowsePath = browseEP + "/" + keywords + "?limit=" + limit
                    + "&offset=" + offset + "&direction=" + direction
                    + "&orderby=" + orderby;
  console.log(newBrowsePath); 

  return await Socket.GET(newBrowsePath);
}

async function search(title, year, director, genre,
  limit, offset, direction, orderby) {
    const newSearchPath = movieSearchEP + "?title=" + title + "&year=" + year
    + "&director=" + director + "&genre=" + genre
    + "&limit=" + limit + "&offset=" + offset 
    + "&direction=" + direction + "&orderby=" + orderby;
    
    console.log(newSearchPath);

    return await Socket.GET(newSearchPath);
  }

  async function getMovieDetail(movie_id) {
    const newPath = movieidEP + movie_id;

    console.log(newPath);

    return await Socket.GET(newPath);
  }

export default {
  browse, search, getMovieDetail
};
