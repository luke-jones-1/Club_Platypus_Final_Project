package app;

import models.*;
import org.apache.log4j.BasicConfigurator;
import org.flywaydb.core.Flyway;
import org.sql2o.Sql2o;
import org.sql2o.converters.UUIDConverter;
import org.sql2o.quirks.PostgresQuirks;
import spark.ModelAndView;
import spark.Spark;

import java.util.HashMap;
import java.util.UUID;


import static spark.Spark.*;
//import static spark.route.HttpMethod.get;

public class Main {

    public static void main(String[] args) {
        BasicConfigurator.configure();

        port(getHerokuAssignedPort());

        Flyway flyway = Flyway.configure().dataSource("jdbc:postgresql://\n" +
                "ec2-23-21-70-39.compute-1.amazonaws.com:5432/cgi_platypi", "ubzionusddizka",
                "09c8b457c4bb87d1a8d63baf2cbf2876c46857bcc2cc762729cb6a5ab036e110").load();
        flyway.migrate();

        Sql2o sql2o = new Sql2o("jdbc:postgresql://\n" +
                "ec2-23-21-70-39.compute-1.amazonaws.com:5432/cgi_platypi", "ubzionusddizka",
                "09c8b457c4bb87d1a8d63baf2cbf2876c46857bcc2cc762729cb6a5ab036e110", new PostgresQuirks() {
            {
                // make sure we use default UUID converter.
                converters.put(UUID.class, new UUIDConverter());
            }
        });

        Model model = new Sql2oModel(sql2o);
        UserModel userModel = new Sql2oModel(sql2o);

        //Sign in methods

        get("/", (req, res) -> "Hello World");



        post("/sign-in", (req,res) -> {

            String password = req.queryParams("password");
            String email = req.queryParams("email");

            if(userModel.verifyUser(email, password)) {
                res.redirect("/posts");
            }
            res.redirect("/");
            return null;
        });


        //Sign up methods

        Spark.get("/sign-up", (req, res) -> {
            HashMap users = new HashMap();
            return new ModelAndView(users, "templates/sign-up.vtl");
        }, new VelocityTemplateEngine());

        post("/sign-up", (req,res) -> {
            String first_name = req.queryParams("first_name");
            String last_name = req.queryParams("last_name");
            String password = req.queryParams("password");
            String email = req.queryParams("email");

            if (model.doesEmailExist(email)) {
                res.redirect("/sign-up");
            } else {
                userModel.createUser(first_name, last_name, password, email);
                res.redirect("/posts");
            }
            return null;
        });
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
