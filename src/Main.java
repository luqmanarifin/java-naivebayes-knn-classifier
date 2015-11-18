/**
 * Main.java
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    static Instance model;
    static ArrayList<Instance> datasets;

    public static void init() {
        model = new Instance();
        model.addAttribute("buying");
        model.addAttribute("maint");
        model.addAttribute("doors");
        model.addAttribute("persons");
        model.addAttribute("lug_boot");
        model.addAttribute("safety");
    }

    public static void main(String[] args) {
        init();
        try {
            BufferedReader buf = new BufferedReader(new FileReader("car.data"));

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
