package LocalNavigation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;

/**
 * File logger based off of http://stackoverflow.com/questions/9120122/java-ascii-output-to-file
 *
 */
public class FileLogger {
    private String path;
    private File file;
    private PrintWriter fileWriter;
    
    public FileLogger(String path){
        this.path = path;
        file = new File(path);
        try {
            fileWriter = new PrintWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Writes a line to the ASCII data file
     * @param timestamp
     * @param translation_error
     * @param rotation_error
     */
    public void write(long timestamp, double translation_error, double rotation_error){
        fileWriter.println(timestamp + " " + translation_error + " " + rotation_error); 
    }

    /**
     * Writes a line to the ASCII data file
     * @param timestamp
     * @param translation_error
     * @param rotation_error
     */
    public void write(String timestamp, String translation_error, String rotation_error){
        fileWriter.println(timestamp + " " + translation_error + " " + rotation_error); 
    }
    /**
     * Closes the file after writing
     */
    public void closeFile(){
        fileWriter.close();
    }

    /**
     * Reads the specified file and prints it out to the console
     * @param path
     */
    public void readFile(String path){
        try {
            BufferedReader fi = new BufferedReader(new FileReader(path));
            String read = fi.readLine();
            System.out.println(read);
            fi.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        FileLogger test = new FileLogger("test.txt");
        for (Integer i = 0; i < 10; i++){
            test.write(i.toString(), i.toString(), i.toString());
        }
        
        test.closeFile();
    }
}