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

            this.extract();
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

    // this method is used to extract all information from a file and format it to fit the data units
    // if file is encrypted, another specialized method is called
    // throws exception if something goes wrong while writing to file
    private void extract() throws IOException {

        if (this.isEncoded) {

            this.encryptedContent = this.extractEncrypted();
        }
        else {

            this.extractDecrypted(this.file, this.units);
        }
    }

    // this method extracts all data from a file and fills the collections data units with said data
    // throws exception if something goes wrong while writing to file
    private void extractDecrypted(File f, ArrayList<DataUnit> u) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader(f)); // reading mechanism for file
        Pattern start = Pattern.compile("[a-zA-Z0-9_)]+ \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\\}"); // marker for end of a unit
        String line; // a line in the file
        Matcher matchStart; // matches a line from file with start charcter
        Matcher matchEnd; // matches a line from file with end charcter
        boolean inDataUnit = false; // checks if a line is in a data unit or not (between {, })
        int lineCounter = 0;

        while ((line = br.readLine()) != null) { // do the following if the line doesn't hold an empty value

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++;

            if (matchStart.find()) { // if start character is found in line, add that line as a new unit

                u.add(new DataUnit(line));
                inDataUnit = true;
                continue;
            }
            else if (matchEnd.find()) { // if end character is found in line, go to next line

                inDataUnit = false;
                continue;
            }
            else if (!inDataUnit) { // to check if formatting is correct, if text is found outside unit => informs user
                String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
                throw new IOException(msg);
            }
            else { // else add line as a new data fragment to the last added unit

                int lastAddedUnit = u.size() - 1; // get index of last added unit
                u.get(lastAddedUnit).addTo(line); // access last unit, added new fragment
            }
        }

        br.close(); // closed connection to file
    }

    // this method is used to get content of a file if that file appears encrypted
    // throws exception if some formatting is incorrect
    private String extractEncrypted() throws IOException {

        BufferedReader br = new BufferedReader(new FileReader()); // reading mechanism
        StringBuilder content = new StringBuilder(); // used because concatinating strings take longer
        String line;

        while ((line = br.readLine()) != null) { // while line doesn't hold an empty value, add line to contents

            content.append(line);
            content.append("\n");
        }

        br.close();
        this.extractContent(content.toString(), this.units); // extracts decrypted contents from encrypted contents
        return content.toString();
    }

    // this method is used to fill data units with data from the encrypted content by first decrypting it
    // and then formatting it
    // throws exception if some formatting is incorrect
    private void extractContent(String source, ArrayList<DataUnit> u) throws IOException {

        String decryptedContent = this.internalDecrypt(source);
        String[] content = decryptedContent.split("\\n");
        Pattern start = Pattern.compile("[a-zA-Z0-9_)]+ \\{"); // marker for beginning of a new unit
        Pattern end = Pattern.compile("\\}"); // marker for end of a unit
        String line; // a line in the file
        Matcher matchStart; // matches a line from file with start charcter
        Matcher matchEnd; // matches a line from file with end charcter
        boolean inDataUnit = false; // checks if a line is in a data unit or not (between {, })
        int lineCounter = 0;

        for (String line : content) {

            matchStart = start.matcher(line);
            matchEnd = end.matcher(line);
            lineCounter++;

            if (matchStart.find()) { // if start character is found in line, add that line as a new unit

                u.add(new DataUnit(line));
                inDataUnit = true;
                continue;
            }
            else if (matchEnd.find()) { // if end character is found in line, go to next line

                inDataUnit = false;
                continue;
            }
            else if (!inDataUnit) { // to check if formatting is correct, if text is found outside unit => informs user
                String msg = "Formatting Error In Line: " + lineCounter + "\nIn Collection :" + this.getPath();
                throw new IOException(msg);
            }
            else { // else add line as a new data fragment to the last added unit

                int lastAddedUnit = u.size() - 1; // get index of last added unit
                u.get(lastAddedUnit).addTo(line); // access last unit, added new fragment
            }
        }
    }

    // this method is used to decrypt content without decrypting anything in the file
    private String internalDecrypt(String str) {

        String newStr = "";
        String[] arr0 = str.split("\\s");
        for (String s : arr0) {
            newStr += s;
        }

        String[] arr = this.splitToNChar(newStr, 3);
        int val;
        newStr = "";
        for (String s : arr) {

            val = Integer.parseInt(s);
            char c = (char) val;
            newStr += c;
        }

        return newStr;
    }

    // this method is used by the one above to seperate a string of words into a list of 3 character-strings
    private String[] splitToNChar(String text, int size) {
        ArrayList<String> parts = new ArrayList<>();

        int length = text.length();
        for (int i = 0; i < length; i += size) {
            parts.add(text.substring(i, Math.min(length, i + size)));
        }
        return parts.toArray(new String[0]);
    }
}
