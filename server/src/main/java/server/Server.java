package server;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import service.ErrorMessage;
import service.Message;
import spark.*;
import service.Service;

import java.util.HashMap;

public class Server {
    private  Service service;

    public int run(int desiredPort) {
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        service = new Service(userDAO,authDAO,gameDAO);
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteDB);
        Spark.post("/user", this::register);
        Spark.exception(ErrorMessage.class, this::exeptionHandler);
        //Spark.exception();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exeptionHandler(ErrorMessage em, Request req, Response res) {
        res.status(em.getCode());
    }

    private Object register(Request request, Response res) throws ErrorMessage {
        var user = new Gson().fromJson(request.body(), UserData.class);
        try {
            AuthData r = service.register(user);
            return new Gson().toJson(r);
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private Object deleteDB(Request rec, Response res){
        try {
            service.clearALL();
            return new Gson().toJson(new HashMap<>());
        } catch (ErrorMessage e){
            res.status(e.getCode());
            return new Gson().toJson(e);
        }
    }
}
