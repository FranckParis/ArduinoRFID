package Server.src.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socket;

    public Server(int port) {
        try {
            this.socket = new ServerSocket(port);

        } catch (IOException e)
        {
            System.err.println("Could not listen on port: "+port);
            System.exit(1);
        }
    }

    public void run(){
        System.out.println("Server running. Waiting for connection");
        int cpt = 1;
        while(true) {
            try {
                //Connecting to server
                Socket socket = this.socket.accept();

                //Server.Connection detected
                Connection c = new Connection(socket, cpt);
                (new Thread(() -> c.run())).start();
                cpt++;
                //c.run();

            } catch (IOException e) {
                System.out.println("Server.Connection failed");
                e.printStackTrace();
            }
        }
    }

    //Main
    public static void main (String[] args){
        Server srv = new Server(3586);
        srv.run();
    }
}

