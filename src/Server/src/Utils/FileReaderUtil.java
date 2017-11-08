package Utils;

import java.io.*;

public class FileReaderUtil {

    private FileReader fileReader;
    private File file;
    private BufferedReader bufferedReader;

    public FileReaderUtil(String fileName) {
        try {
            this.file = new File(fileName);
            this.fileReader = new FileReader(file);
            this.bufferedReader = new BufferedReader(fileReader);

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            e.printStackTrace();
        }
    }

    public boolean checkInFile(String key){

        StringBuffer stringBuffer = new StringBuffer();
        String line;
        Boolean keyFound = false;

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
            System.out.println("Key found in file");
        }
        else{
            System.out.println("Key not found in file");
        }

        return keyFound;
    }

}
