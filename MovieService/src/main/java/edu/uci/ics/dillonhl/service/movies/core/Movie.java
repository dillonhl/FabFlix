package edu.uci.ics.dillonhl.service.movies.core;

import edu.uci.ics.dillonhl.service.movies.MoviesService;

import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.data.GenreModel;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;
import edu.uci.ics.dillonhl.service.movies.models.data.PersonModel;
import edu.uci.ics.dillonhl.service.movies.models.data.ThumbnailModel;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


public class Movie {
    public static MovieModel getMovie(String movie_id, int privilege)
    {
        try{
            String query = "SELECT * from movie" +
                        " WHERE movie_id = ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded searching for " + movie_id);
            System.err.println("**Privilege Result: " + privilege);

            if (rs.next())
            {
                if (rs.getBoolean("hidden") == true && privilege != 140) {
                    System.err.println("Movie hidden...... Cannot display. ");
                    return null;
                }
                MovieModel movie = new MovieModel();
                ServiceLogger.LOGGER.info("FOUND MOVIE:\n");
                movie.setTitle(rs.getString("title"));
                ServiceLogger.LOGGER.info("title: " + movie.getTitle());
                movie.setMovie_id(movie_id);
                movie.setYear(rs.getInt("year"));
                movie.setRating(rs.getFloat("rating"));
                movie.setNum_votes(rs.getInt("num_votes"));
                movie.setBudget(String.valueOf(rs.getInt("budget")));
                movie.setRevenue(String.valueOf(rs.getLong("revenue")));
                movie.setOverview(rs.getString("overview"));
                movie.setBackdrop_path(rs.getString("backdrop_path"));
                movie.setPoster_path(rs.getString("poster_path"));
                movie.setHidden(rs.getBoolean("hidden"));
                movie.setDirector(Person.getDirector(rs.getInt("director_id")));
                movie.setPeople(getPeopleFromMovie(movie_id));
                movie.setGenres(getGenres(movie_id));

                if (privilege != 140) {
                    System.err.println("Insufficient plvl......");
                    movie.setHidden(null);
                }

                return movie;
            }

            return null;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static MovieModel[] getMoviesFromPersonName(int person_id)
    {
        return null;
    }

    public static PersonModel[] getPeopleFromMovie(String movie_id)
    {
        try {
            String query = "SELECT * FROM person_in_movie pim" +
                    " JOIN person p ON pim.person_id = p.person_id" +
                    " WHERE pim.movie_id = ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded searching for people in movie.");

            PersonModel[] personModel = null;

            int count = 0;
            while (rs.next())
            {
                count++;
            }
            if (count > 0) {
                rs.beforeFirst();
                personModel = new PersonModel[count];
                int i = 0;
                while (rs.next()) {
                    ServiceLogger.LOGGER.info("Inserting " + rs.getString("name"));
                    personModel[i] = new PersonModel();
                    personModel[i].setPerson_id(rs.getInt("person_id"));
                    personModel[i].setName(rs.getString("name"));
                    i++;
                }
            }

            return personModel;

        }catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query failed.");
            return null;
        }
    }

    public static GenreModel[] getGenres(String movie_id)
    {
        try {
            String query = "SELECT * FROM genre_in_movie gim " +
                            " JOIN genre g ON gim.genre_id = g.genre_id" +
                            " WHERE gim.movie_id = ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1, movie_id);

            ServiceLogger.LOGGER.info("Trying query: " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded searching for genres.");

            GenreModel[] genreModel = null;
            int count = 0;
            while (rs.next())
            {
                count++;
            }

            System.err.println("COUNT: " + count);

            if (count > 0) {
                rs.beforeFirst();
                genreModel = new GenreModel[count];
                int i = 0;
                while (rs.next()) {
                    genreModel[i] = new GenreModel();
                    genreModel[i].setGenre_id(rs.getInt("genre_id"));
                    genreModel[i].setName(rs.getString("name"));
                    i++;
                }
            }

            for (int i = 0; i < count; i++)
            {
                System.err.println(genreModel);
            }
            return genreModel;

        }catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query failed.");
            return null;
        }
    }

    public static ThumbnailModel[] getMovieThumbnails(String[] movie_ids)
    {
        if (movie_ids.length == 0)
            return null;

        //ThumbnailModel[] thumbnails = new ThumbnailModel[movie_ids.length];
        ArrayList<ThumbnailModel> arrayList = new ArrayList<>();
        try {
            for (int i = 0; i < movie_ids.length; i++)
            {
                String query = "SELECT * FROM movie" +
                        " WHERE movie_id = ?;";
                PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
                ps.setObject(1, movie_ids[i]);
                ServiceLogger.LOGGER.info("Trying query: " + ps);
                ResultSet rs = ps.executeQuery();
                ServiceLogger.LOGGER.info("Query succeeded searching for thumbnails.");

                if (rs.next())
                {
                    System.err.println("Found Movie " + rs.getString("movie_id") + ": " + rs.getString("title"));
                    arrayList.add(new ThumbnailModel(rs.getString("movie_id"), rs.getString("title"),
                            rs.getString("backdrop_path"), rs.getString("poster_path")));
                }
                else
                {
                    System.err.println("Movie not found with id: " + movie_ids[i]);
                }
            }

            ThumbnailModel[] thumbnails = new ThumbnailModel[arrayList.size()];
            for (int i = 0; i < arrayList.size(); i++)
            {
                thumbnails[i] = arrayList.get(i);
            }

            return thumbnails;
        } catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query failed.");
            return null;
        }
    }

    public static MovieModel[] searchMovies(String q, Integer privilege)
    {
        try {
            MovieModel[] movies = null;
            String query = q;
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            ServiceLogger.LOGGER.info("Trying query " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            int count = 0;
            while (rs.next())
            {
                count++;
            }
            ServiceLogger.LOGGER.info("FOUND   " + count + "   MOVIES");
            if (count > 0)
            {
                rs.beforeFirst();
                movies = new MovieModel[count];

                int i = 0;
                while (rs.next())
                {
                    if (rs.getBoolean("hidden") == true && privilege != 140) {
                        System.err.println("Movie hidden...... Cannot display. ");
                        return null;
                    }
                    movies[i] = new MovieModel();
                    movies[i].setMovie_id(rs.getString("movie_id"));
                    movies[i].setTitle(rs.getString("title"));
                    movies[i].setYear(rs.getInt("year"));
                    movies[i].setDirector(rs.getString("name"));
                    movies[i].setRating(rs.getFloat("rating"));
                    movies[i].setBackdrop_path(rs.getString("backdrop_path"));
                    movies[i].setPoster_path(rs.getString("poster_path"));
                    movies[i].setHidden(rs.getBoolean("hidden"));

                    if (privilege != 140)
                    {
                        movies[i].setHidden(null);
                    }
                    i++;
                }
            }
            else
            {
                return null;
            }

            System.err.println("CHECK!");
            for (int j = 0; j < count; j++)
            {
                System.err.println("CHECKINSIDE");
                System.out.println("Movie " + j + " Title: " + movies[j].getTitle());
            }
            System.err.println("CHECK2!");
            return movies;

        } catch (SQLException e)
        {
            ServiceLogger.LOGGER.info("Query failed.");
            e.printStackTrace();
            return null;
        }
    }

    public static MovieModel[] browseMovies(String q, int privilege) {
        try{
            MovieModel[] movies = null;
            String query = q;
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);

            ServiceLogger.LOGGER.info("Trying query " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");
            int count = 0;
            while (rs.next())
            {
                count++;
            }
            ServiceLogger.LOGGER.info("FOUND   " + count + "   MOVIES");
            if (count > 0)
            {
                rs.beforeFirst();
                movies = new MovieModel[count];

                int i = 0;
                while (rs.next())
                {
                    if (rs.getBoolean("hidden") == true && privilege != 140) {
                        System.err.println("Movie hidden...... Cannot display. ");
                        return null;
                    }
                    movies[i] = new MovieModel();
                    movies[i].setMovie_id(rs.getString("movie_id"));
                    movies[i].setTitle(rs.getString("title"));
                    movies[i].setYear(rs.getInt("year"));
                    movies[i].setRating(rs.getFloat("rating"));
                    movies[i].setBackdrop_path(rs.getString("backdrop_path"));
                    movies[i].setPoster_path(rs.getString("poster_path"));
                    movies[i].setHidden(rs.getBoolean("hidden"));
                    movies[i].setDirector(Person.getDirector(rs.getInt("director_id")));

                    if (privilege != 140)
                    {
                        movies[i].setHidden(null);
                    }
                    i++;
                }
            }
            else
            {
                return null;
            }

            return movies;

        }catch (SQLException e)
        {
            System.err.println("Query failed...");
            e.printStackTrace();
            return null;
        }

    }

    public static String buildMovieQuery(String title, Integer year, String director,
                                         String genre, Boolean hidden, Integer limit, Integer offset,
                                         String orderby, String direction)
    {
        System.err.println("Building query...");
        String SELECT = "SELECT DISTINCT m.movie_id, m.title, m.year, m.director_id, m.rating, " +
                        "m.backdrop_path, m.poster_path, m.hidden, p.name";
        String FROM = " FROM movie m LEFT JOIN person p ON m.director_id = p.person_id" +
                                        " JOIN (SELECT movie_id, name as genre FROM genre g JOIN genre_in_movie gim on g.genre_id = gim.genre_id) ge ON ge.movie_id = m.movie_id";
        String WHERE = " WHERE 1=1";

        if (title != null || !title.isEmpty()) {
            WHERE += " && title LIKE '%" + title + "%'";
        }
        if (year != null) {
            WHERE += " && year = " + year;
        }
        if (director != null || !title.isEmpty()) {
            WHERE += " && name LIKE '%" + director + "%'";
        }
        if (genre != null || !title.isEmpty()) {
            WHERE += " && ge.genre = '" + genre + "'";
        }
        if (hidden != null) {
        }
         WHERE += " ORDER BY " + orderby + " " + direction + " LIMIT " + limit + " OFFSET " + offset;

        System.err.println("Query built: " + SELECT + FROM + WHERE);
        return SELECT + FROM + WHERE + ";";

    }

}
