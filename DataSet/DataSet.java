package data;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Random;

public class DataSet {

    private File file; // file representation of data set
    private ArrayList<DataUnit> units; // the content of a set
    private boolean isEmpty; // indicator to check if file is empty
    private boolean isEncrypted; // indicator to check if file is encrypted
    private String encryptedContent; // encryted verson of text content in file

    /* CONSTRUCTORS */

    // ctor, takes file path
    // throws exception if something goes wrong while creating or reading file
    public DataSet(String pathName) throws IOException {

        this.file = new File(pathName); // connect to file
        this.file.createNewFile(); // create file if it does not exist
        this.units = new ArratList<>(); // new unit list
        this.isEmpty = !(this.hasContent(this.file)); // checks if file has content or not, negated to suite use of filed

        if (!this.isEmpty()) { // if file isn't empty, get all info out and store it in unit list

            this.extract(this.file, this.units);
        }
    }

    /* INTERNAL */

    // this method is used to check if a file has any content whatsoever, used in conjuntion with this.extract
    // throws exception if something goes wrong with reading file
    private boolean hasContent(File f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

             result = true;
        }

        br.close();
        this.isEncoded = this.hasArtifact(f); // checks if file is encrypetd
        return result;
    }

    // this method is used for setting the isEncoded field by seeing if an artifact of length 2 chars is present in file
    // throws exception if something goes wrong with reading file
    private boolean hasArtifact(f) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file

        String line; // a line in the file
        boolean result = false;

        while ((line = br.readLine()) != null) { // result remains false until line doesn't hold an empty value

            result = line.contains("127"); // checks if first line has 127 in it
            break; // go out of loop cuz 127 is not found anywhere else in file other than in first line
        }

        br.close();
        return result;
    }
}
