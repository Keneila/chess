
import server.Server;

public class ServerMain {
    public static void main(String[] args) {
        Server s = new Server();
        s.run(8080);
    }
}