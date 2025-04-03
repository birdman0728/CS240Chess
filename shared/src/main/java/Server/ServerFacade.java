package Server;

import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import requestsandresults.*;
//import model.Pet;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest request){
        var path = "/user";
        return this.makeRequest("POST", path,null, request, RegisterResult.class);
    }

    public LoginResult login(LoginRequest request){
        var path = "/session";
        return this.makeRequest("POST", path,null, request, LoginResult.class);
    }

    public LogoutResult logout(LogoutRequest request){
        var path = "/session";
        return this.makeRequest("DELETE", path, request.authToken(), null, LogoutResult.class);
    }

    public ListResult listGames(ListRequest request){
        var path = "/game";
        return this.makeRequest("GET", path, request.authToken(), null, ListResult.class);
    }

    public CreateResult create(CreateRequest request){
        var path = "/game";
        return this.makeRequest("POST", path, request.authToken(), request, CreateResult.class);
    }

    public JoinResult join(JoinRequest request){
        var path = "/game";//TODO check on auth token for this
        return this.makeRequest("PUT", path, request.authToken(), request, JoinResult.class);
    }

    public ClearResult clear(){
        var path = "/db";
        return this.makeRequest("DELETE", path, null, null, ClearResult.class);
    }

    private <T> T makeRequest(String method, String path, String authToken, Object request, Class<T> responseClass){
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if(authToken != null){
                http.setRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
//            throw new DataAccessException(500, ex.getMessage());
            return null;
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException{
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
//            throw new DataAccessException(status, "failure: " + status);
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

