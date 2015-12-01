package solver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ASUS PC on 01/12/2015.
 */
public class Data {
    // [atribute]
    private ArrayList<String> names;
    private HashMap<String, Integer> map_names;

    // [atribute][value]
    private ArrayList<ArrayList<String>> values;
    private ArrayList<HashMap<String, Integer>> map_values;

    // [kelas][atribute][value]
    private ArrayList<ArrayList<ArrayList<Integer>>> cl;
    private ArrayList<Integer> sums;

    // File arff
    private File file;

    public void readHeader(String str) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(new File(str)));
            String buf;
            while((buf = reader.readLine()) != null) {
                if(buf.startsWith("@data")) {
                    break;
                }
                if(buf.startsWith("@relation")) {
                    buf.replaceFirst("@relation", "");  // remove relation string
                    buf = buf.trim();                   // remove trailing spaces
                    name_relation = buf.substring(1, buf.length() - 1);
                } else if(buf.startsWith("@attribute")) {
                    String pattern = "\\@attribute\\s(.*)\\s\\{(.*)\\}";
                    Pattern p = Pattern.compile(pattern);
                    Matcher m = p.matcher(buf);
                    if(m.find()) {
                        String name = m.group(1);
                        String val = m.group(2);
                        String[] values = val.split(",\\s*");
                        addAttribute(name);
                        for(String it : values) {
                            addValues(name, it);
                        }
                    }
                }
            }
        } catch(Exception e) {
            System.out.println("gagal");
        }

    }
}
