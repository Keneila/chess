package server;

import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import spark.*;

import java.util.HashMap;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::deleteDB);
        Spark.post("/user", this::register);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object register(Request request, Response response) {
        var user = new Gson().fromJson(request.body(), UserData.class);
        return new Gson().toJson(new AuthData("ergerged", user.username()));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
    private Object deleteDB(Request rec, Response res){
        res.status(200);
        return new Gson().toJson(new HashMap<>());
    }
}
