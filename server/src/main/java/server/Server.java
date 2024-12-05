package server;

import dataaccess.*;
import model.*;
import com.google.gson.Gson;
import server.websocket.WebSocketHandler;
import service.ErrorMessage;
import service.Message;
import spark.*;
import service.Service;

import java.util.Collection;
import java.util.HashMap;

public class Server {
    private  Service service;
    private WebSocketHandler webSocketHandler;

    public int run(int desiredPort) {
        UserDAO userDAO = new MemUserDAO();
        GameDAO gameDAO = new MemGameDAO();
        AuthDAO authDAO = new MemAuthDAO();
        try {
            if (true) {
                userDAO = new SQLUserDAO();
                gameDAO = new SQLGameDAO();
                authDAO = new SQLAuthDAO();
            }
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        service = new Service(userDAO,authDAO,gameDAO);
        webSocketHandler = new WebSocketHandler(service, userDAO,authDAO,gameDAO);
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/ws", webSocketHandler);
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteDB);
        Spark.post("/user", this::register);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.post("/game", this::createGame);
        Spark.get("/game", this::listGames);
        Spark.put("/game", this::joinGame);
        Spark.exception(ErrorMessage.class, this::exeptionHandler);
        //Spark.exception();
        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object joinGame(Request req, Response res) {
        var token = req.headers("authorization");
        var info = new Gson().fromJson(req.body(), JoinGameRequest.class);
        try{
            service.joinGame(token, info.playerColor(), info.gameID());
            return new Gson().toJson(new HashMap<>());
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
    }

    private Object listGames(Request req, Response res) {
        var token = req.headers("authorization");
        try{
            Collection<GameData> data = service.listGames(token);
            return new Gson().toJson(new ListGamesResponse(data));
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
    }

    private Object createGame(Request req, Response res) {
        var token = req.headers("authorization");
        var gameReq = new Gson().fromJson(req.body(), CreateGameRequest.class);
        try{
            int gameID = service.createGame(new CreateGameRequest(gameReq.gameName(), token));
            var id = new HashMap<String, Integer>();
            id.put("gameID",gameID);
            return new Gson().toJson(id);
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
    }

    private Object logout(Request req, Response res) {
        var token = req.headers("authorization");
        try{
            service.logout(token);
            return new Gson().toJson(new HashMap<>());
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
    }

    private Object login(Request req, Response res) {
        var user = new Gson().fromJson(req.body(), LoginRequest.class);
        try{
            AuthData r = service.login(user);
            return new Gson().toJson(r);
        } catch (ErrorMessage e){
            res.status(e.getCode());
            Message err = new Message(e.getMessage());
            return new Gson().toJson(err);
        }
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
