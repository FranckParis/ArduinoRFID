package Server.src.Server;

import Server.src.Ressources.Key;
import Server.src.Utils.FileReaderUtil;
import Server.src.Utils.JsonAdapter;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Connection extends Thread {

    //Attributes
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private JsonAdapter jsonAdapter;
    private FileReaderUtil fileReaderUtil;
    private int id;

    //Constructor
    public Connection (Socket socket, int id){

        this.socket = socket;
        this.jsonAdapter = new JsonAdapter();
        this.fileReaderUtil = new FileReaderUtil("src/Server/src/Ressources/keys.txt");
        this.id = id;

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
        System.out.println("New connection at "+ socket.getInetAddress() + " : " + socket.getLocalPort());
    }

    public void checkIdCard(){
        try {
            System.out.println("Connection id : "+ this.id);
            JsonObject obj = new JsonParser().parse(input.readLine()).getAsJsonObject();
            Key keyObject = this.jsonAdapter.createKeyFromJson(obj);

            if(this.fileReaderUtil.checkInFile(keyObject.getUID())){
                System.out.println("Answering card.\n");
                output.print("1.");
            }
            else{
                System.out.println("Answering card.\n");
                output.print("0.");
            }
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
