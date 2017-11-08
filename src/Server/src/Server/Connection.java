package Server;

import Ressources.Key;
import Utils.FileReaderUtil;
import Utils.JsonAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection {

    //Attributes
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private JsonAdapter jsonAdapter;
    private FileReaderUtil fileReaderUtil;

    //Constructor
    public Connection (Socket socket){

        this.socket = socket;
        this.jsonAdapter = new JsonAdapter();
        this.fileReaderUtil = new FileReaderUtil("keys.txt");

        try {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection instantiation error.");
        }
    }

    //Methods
    public void run(){

        //Connection
        this.connect();

        //Check ID
        this.checkIdCard();

    }

    public void connect(){
        //Connection ready
        System.out.println("New connection to "+ socket.getInetAddress() + " : " + socket.getLocalPort());
    }

    public void checkIdCard(){
        try {
            JsonObject obj = new JsonParser().parse(input.readLine()).getAsJsonObject();
            Key keyObject = this.jsonAdapter.createKeyFromJson(obj);

            if(this.fileReaderUtil.checkInFile(keyObject.getUID())){
                System.out.println("Key found. Answering card.");
                output.println("Authentication Success : Access granted");
            }
            else{
                System.out.println("Key not found. Answering card.");
                output.println("Authentication Failed : Access refused");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
