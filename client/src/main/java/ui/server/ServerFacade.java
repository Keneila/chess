package ui.server;

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
import java.util.HashMap;

public class ServerFacade {
    private final String serverUrl;
    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void deleteDB() throws Exception {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null, null);
    }

    public AuthData register(UserData user) throws Exception {
        var path = "/user";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public AuthData login(LoginRequest user) throws Exception {
        var path = "/session";
        return this.makeRequest("POST", path, user, null, AuthData.class);
    }

    public void logout(String token) throws Exception {
        var path = "/session";
        this.makeRequest("DELETE", path, null, token, null);
    }

    public int createGame(String gameName, String token) throws Exception {
        var path = "/game";
        record gameN (String gameName){};
        record gameIdClass (int gameID){};
        return this.makeRequest("POST", path, new gameN(gameName), token, gameIdClass.class ).gameID();
    }
    public Collection<GameData> listGames(String token) throws Exception {
        var path = "/game";
        return this.makeRequest("GET", path, null, token, ListGamesResponse.class).games();
    }

    public void joinGame(String token, String playerColor, int gameID) throws Exception {
        var path = "/game";
        this.makeRequest("PUT", path, new JoinGameRequest(playerColor,gameID), token, null);
    }

    private <T> T makeRequest(String method, String path, Object requestBody, String token, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (token != null && !token.isEmpty()) {
                http.setRequestProperty("authorization", token);
            }
            writeBody(requestBody, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, Exception {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            http.getErrorStream();
            throw new Exception("http failure: " + status);
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
