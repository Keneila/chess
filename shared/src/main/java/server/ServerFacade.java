package server;

import com.google.gson.Gson;
import model.*;
import service.ErrorMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collection;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void deleteDB() throws ErrorMessage {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData user) throws ErrorMessage {
        var path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(LoginRequest user) throws ErrorMessage {
        var path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public void logout(String token) throws ErrorMessage {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public int createGame(CreateGameRequest req) throws ErrorMessage {
        var path = "/game";
        return this.makeRequest("POST", path, null, null);
    }
    public Collection<GameData> listGames(String token) throws ErrorMessage {
        var path = "/game";
        return this.makeRequest("GET", path, null, null);
    }

    public void joinGame(String token, String playerColor, int gameID) throws ErrorMessage {
        var path = "/game";
        record joinRequest(String token, String playerColor, int gameID){}
        this.makeRequest("PUT", path, new joinRequest(token,playerColor,gameID), null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ErrorMessage {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ErrorMessage(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ErrorMessage {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ErrorMessage(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
