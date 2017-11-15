package Server.src.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileReaderUtil {

    private File file;

    public FileReaderUtil(String fileName) {

        this.file = new File(fileName);
    }

    public boolean checkInFile(String key){

        StringBuffer stringBuffer = new StringBuffer();
        String line;
        Boolean keyFound = false;
        FileReader fileReader = null;

        try {
            fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        //Reading file to find key
        try {
            while ((line = bufferedReader.readLine()) != null && !keyFound) {
                if(line.equals(key)) {
                    keyFound = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(keyFound){
            System.out.println("Key found in file : " + key);
        }
        else{
            System.out.println("Key not found in file : " + key);
        }

        try {
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return keyFound;
    }

    public void addInFile(String key){
        try {
            Files.write(Paths.get("src/Server/src/Ressources/keys.txt"), (key+"\n").getBytes(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
