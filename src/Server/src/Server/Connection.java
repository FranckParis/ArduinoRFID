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

public class Connection /*extends Thread*/ {

    //Attributes
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private JsonAdapter jsonAdapter;
    private int id;
    private String masterKey;
    private boolean masterKeyChecked;

    //Constructor
    public Connection (Socket socket, int id, String masterKey, boolean masterKeyChecked){

        this.socket = socket;
        this.jsonAdapter = new JsonAdapter();
        this.id = id;
        this.masterKey = masterKey;
        this.masterKeyChecked = masterKeyChecked;

        try {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = new PrintWriter(this.socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Connection instantiation error.");
        }
    }

    //Methods
    public boolean run(){

        //Connection
        this.connect();

        //Check ID
        this.checkIdCard();

        return masterKeyChecked;
    }

    public void connect(){
        //Connection ready
        System.out.println("New connection at "+ socket.getInetAddress() + " : " + socket.getLocalPort());
    }

    public void checkIdCard(){
        try {
            System.out.println("Connection id : "+ this.id);
            System.out.println("Master Key Checked : " + masterKeyChecked);

            JsonObject obj = new JsonParser().parse(input.readLine()).getAsJsonObject();
            Key keyObject = this.jsonAdapter.createKeyFromJson(obj);
            FileReaderUtil fileReaderUtil = new FileReaderUtil("src/Server/src/Ressources/keys.txt");

            //Key found in file
            if(fileReaderUtil.checkInFile(keyObject.getUID())){

                //Key is master
                if(keyObject.getUID().equals(this.masterKey)){
                    System.out.println("Master key seen. Waiting to register key.\n");
                    masterKeyChecked = true;
                    output.print("2.");
                }
                //Key is not master
                else{
                    System.out.println("Answering card positively.\n");
                    output.print("1.");
                }
            }
            //Key not found in file
            else{
                //Master key has been seen, add key
                if(masterKeyChecked){
                    System.out.println("Adding new key to file\n");
                    fileReaderUtil.addInFile(keyObject.getUID());
                    masterKeyChecked = false;
                    output.print("1.");
                }
                //Master key not seen, rejected
                else{
                    System.out.println("Answering card negatively.\n");
                    output.print("0.");
                }
            }
            output.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
