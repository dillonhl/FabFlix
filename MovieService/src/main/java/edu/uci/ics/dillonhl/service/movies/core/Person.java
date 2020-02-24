package edu.uci.ics.dillonhl.service.movies.core;

import edu.uci.ics.dillonhl.service.movies.MoviesService;
import edu.uci.ics.dillonhl.service.movies.base.Result;
import edu.uci.ics.dillonhl.service.movies.logger.ServiceLogger;
import edu.uci.ics.dillonhl.service.movies.models.data.MovieModel;
import edu.uci.ics.dillonhl.service.movies.models.data.PersonModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Person {
    public static PersonModel searchPersonID(int person_id)
    {
        try
        {
            String query = "SELECT * from person" +
                         " LEFT JOIN gender g on person.gender_id = g.gender_id" +
                        " WHERE person_id = ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1, person_id);

            ServiceLogger.LOGGER.info("Trying query: "  + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                ServiceLogger.LOGGER.info("Found person");
                PersonModel person = new PersonModel();
                person.setPerson_id(person_id);
                person.setName(rs.getString("name"));
                person.setGender(rs.getString("gender_name"));
                person.setBirthday(rs.getString("birthday"));
                person.setDeathday(rs.getString("deathday"));
                person.setBiography(rs.getString("biography"));
                person.setBirthplace(rs.getString("birthplace"));
                person.setPopularity(rs.getFloat("popularity"));
                person.setProfile_path(rs.getString("profile_path"));

                return person;
            }
            return null;
        } catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query failed.");
            return null;
        }
    }

    public static String getDirector(int director_id)
    {
        try {
            String query = "SELECT name FROM person" +
                            " WHERE person_id = ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1,director_id);

            ServiceLogger.LOGGER.info("Trying query " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            if (rs.next())
            {
                return rs.getString("name");
            }
            else
            {
                return null;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static MovieModel[] searchPersonMovies(String name, int limit, int offset, String orderby, String direction, int privilege)
    {
        try {
            String query = "SELECT * FROM movie m JOIN " +
                            "(SELECT name, movie_id FROM person p JOIN person_in_movie pim on p.person_id = pim.person_id) pe" +
            " ON m.movie_id = pe.movie_id" +
            " WHERE pe.name = ? " +
            " ORDER BY " + orderby + " " + direction + " LIMIT ? OFFSET ?;";
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ps.setObject(1, name);
            ps.setObject(2, limit);
            ps.setObject(3, offset);

            ServiceLogger.LOGGER.info("Trying query " + ps);
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query succeeded.");

            MovieModel[] movies = null;
            int count = 0;
            while (rs.next())
            {
                count++;
            }
            if (count > 0)
            {
                movies = new MovieModel[count];
                rs.beforeFirst();
                int i = 0;
                while (rs.next())
                {
                    if (rs.getBoolean("hidden") == true && privilege != 140) {
                        System.err.println("Movie hidden...... Cannot display. ");
                    }
                    else {
                        System.err.println("Inserting Movie: " + rs.getString("title"));
                        movies[i] = new MovieModel();
                        movies[i].setMovie_id(rs.getString("movie_id"));
                        movies[i].setTitle(rs.getString("title"));
                        movies[i].setYear(rs.getInt("year"));
                        movies[i].setRating(rs.getFloat("rating"));
                        movies[i].setBackdrop_path(rs.getString("backdrop_path"));
                        movies[i].setPoster_path(rs.getString("poster_path"));
                        movies[i].setHidden(rs.getBoolean("hidden"));
                        if (privilege != 140) {
                            movies[i].setHidden(null);
                        }
                        i++;
                    }
                }
            }
            return movies;

        }catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public static PersonModel[] searchPeople(String q)
    {
        try {
            String query = q;
            PreparedStatement ps = MoviesService.getCon().prepareStatement(query);
            ServiceLogger.LOGGER.info("Trying query");
            ResultSet rs = ps.executeQuery();
            ServiceLogger.LOGGER.info("Query Succeeded");
            PersonModel[] people;
            int count = 0;
            while (rs.next()){
                count++;
            }
            if (count > 0)
            {
                rs.beforeFirst();
                people = new PersonModel[count];
                int i = 0;
                while (rs.next())
                {
                    System.err.println("Found person: " + rs.getString("name"));
                    people[i] = new PersonModel();
                    people[i].setPerson_id(rs.getInt("person_id"));
                    people[i].setName(rs.getString("name"));
                    people[i].setBirthday(rs.getString("birthday"));
                    people[i].setPopularity(rs.getFloat("popularity"));
                    people[i].setProfile_path(rs.getString("profile_path"));
                    i++;
                }
                return people;
            }

            return people = new PersonModel[]{};
        }catch (SQLException e)
        {
            e.printStackTrace();
            ServiceLogger.LOGGER.info("Query failed");
            return null;
        }
    }

    public static String buildPeopleQuery(String name, String birthday, String title,
                                          Integer limit, Integer offset, String orderby, String direction)
    {
        System.err.println("Building people query...");

        String SELECT = "SELECT person.person_id, name, birthday, popularity, profile_path";
        String FROM = " FROM person ";
        String WHERE = " WHERE 1=1";
        if (name != null && !name.isEmpty()) {
            WHERE += " && name LIKE '%" + name + "%'";
        }
        if (birthday != null && !birthday.isEmpty()){
            WHERE += " && birthday = '" + birthday +"'";
        }
        if (title != null && !title.isEmpty()){
            FROM += " JOIN (SELECT pim.person_id FROM person_in_movie pim JOIN movie m ON " +
                    "pim.movie_id = m.movie_id WHERE title LIKE '%" + title + "%')" +
                    " title ON person.person_id = title.person_id";
        }

        WHERE += " ORDER BY " + orderby + " " + direction + " LIMIT " + limit + " OFFSET " + offset + ";";

        System.err.println(SELECT + FROM + WHERE);
        return SELECT + FROM + WHERE;
    }

}
