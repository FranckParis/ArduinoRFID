package Server.src.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket socket;
    private String masterKey;
    private boolean masterKeyChecked;

    public Server(int port, String masterKey) {
        try {
            this.socket = new ServerSocket(port);
            this.masterKey = masterKey;
            this.masterKeyChecked = false;

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
                Connection c = new Connection(socket, cpt, this.masterKey, masterKeyChecked);
                //(new Thread(() -> c.run())).start();
                cpt++;
                this.masterKeyChecked = c.run();

            } catch (IOException e) {
                System.out.println("Server.Connection failed");
                e.printStackTrace();
            }
        }
    }

    //Main
    public static void main (String[] args){
        Server srv = new Server(3586, "22.38.158.172");
        srv.run();
    }
}

