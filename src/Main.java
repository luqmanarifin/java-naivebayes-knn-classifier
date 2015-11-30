/**
 * Main.java
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  static Instance model;
  static String name_relation;

  // [atribute]
  static ArrayList<String> names;
  static HashMap<String, Integer> map_names;

  // [atribute][value]
  static ArrayList<ArrayList<String>> values;
  static ArrayList<HashMap<String, Integer>> map_values;

  // [kelas][atribute][value]
  static ArrayList<ArrayList<ArrayList<Integer>>> cl;
  static ArrayList<Integer> sums;

  static BufferedReader reader;

  /**
   * nambahin atribut ke model
   * @param str nama atribut
   */
  public static void addAttribute(String str) {
    map_names.put(str, names.size());
    names.add(str);
    map_values.add(new HashMap<String, Integer>());
    values.add(new ArrayList<String>());
  }

  /**
   * nambahin value dari atribut ke model
   * @param str nama atribut, harus sudah pernah di addAttribute sebelumnya
   * @param val nama value dari atribut
   */
  public static void addValues(String str, String val) {
    int at = map_names.get(str);
    map_values.get(at).put(val, values.get(at).size());
    values.get(at).add(val);
  }

  public static void init() {
    names = new ArrayList<String>();
    map_names = new HashMap<String, Integer>();
    values = new ArrayList<ArrayList<String>>();
    map_values = new ArrayList<HashMap<String, Integer>>();
  }

  public static void read_header(String str) {
    try {
      System.out.println(System.getProperty("user.dir"));
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
  public static ArrayList<ArrayList<String>> prepare_naive_bayes(String atr) {
    ArrayList<ArrayList<String>> instances = new ArrayList<ArrayList<String>>();
    // [kelas][atribut][value]
    cl = new ArrayList<ArrayList<ArrayList<Integer>>>();
    // [kelas]
    sums = new ArrayList<Integer>();

    int at = map_names.get(atr);
    String buf;
    try {
      while ((buf = reader.readLine()) != null) {
        String[] val = buf.split(",\\s*");
        ArrayList<String> push = new ArrayList<String>();
        for(String it : val) {
          push.add(it);
        }
        instances.add(push);
      }
    } catch (Exception e) {

    }
    return instances;
  }

  public static void entry_instances(ArrayList<ArrayList<String>> instances, String atr) {
    int at = map_names.get(atr);
    for(int i = 0; i < values.get(at).size(); i++) {
      cl.add(new ArrayList<ArrayList<Integer>>());
      for(int j = 0; j < names.size(); j++) {
        cl.get(i).add(new ArrayList<Integer>());
        for(int k = 0; k < values.get(j).size(); k++) {
          cl.get(i).get(j).add(0);
        }
      }
      sums.add(0);
    }

    for(int i = 0; i < instances.size(); i++) {
      String clazz = instances.get(i).get(at);
      int cval = map_values.get(at).get(clazz);
      for(int j = 0; j < names.size(); j++) {
        if(j == at) continue;
        int ival = map_values.get(j).get(instances.get(i).get(j));
        cl.get(cval).get(j).set(ival, cl.get(cval).get(j).get(ival) + 1);
      }
      sums.set(cval, sums.get(cval) + 1);
    }
  }

  public static void debug_bayes(String atr) {
    int at = map_names.get(atr);
    int tot = 0;
    for(int i = 0; i < sums.size(); i++) {
      tot += sums.get(i);
    }
    System.out.println("Ada " + tot + " total instances");
    for(int i = 0; i < sums.size(); i++) {
      System.out.println(sums.get(i) + " masuk pada kelas " + values.get(at).get(i));
      for(int j = 0; j < names.size(); j++) {
        if(j == at) continue;
        System.out.println("Pada atribut " + names.get(j) + "....");
        for(int k = 0; k < values.get(j).size(); k++) {
          System.out.println(cl.get(i).get(j).get(k) + " instance memiliki value " + values.get(j).get(k));
        }
      }
      System.out.println();
    }
  }

  /**
   * ngelakuin naive bayes
   * @param atr nama kelas
   * @param ins instance dari kelas, pastikan format sesuai dengan arff
   *            misal akan dicari '?' dari instance (3, ?, sunny, yes, no)
   *            maka array ins harus berisi = {"3", "?", "sunny", "yes", "no"}
   */
  public static void do_naive_bayes(String atr, ArrayList<String> ins) {
    int at = map_names.get(atr);
    double[] prob = new double[sums.size()];
    int tot = 0;
    for(int i = 0; i < sums.size(); i++) {
      prob[i] = sums.get(i);
      tot += sums.get(i);
    }

    for(int i = 0; i < sums.size(); i++) {
      System.out.println("Kelas " + values.get(at).get(i) + " dikategorikan dengan peluang :");
      System.out.print(sums.get(i) + "/" + tot + " ");
      for(int j = 0; j < names.size(); j++) {
        if(j == at) continue;
        System.out.print("* ");
        int ival = map_values.get(j).get(ins.get(j));
        System.out.print(cl.get(i).get(j).get(ival) + "/" + sums.get(i) + " ");
        prob[i] *= (double) cl.get(i).get(j).get(ival) / sums.get(i);
      }
      System.out.println();
      System.out.println("= " + (prob[i] / tot));
      System.out.println();
    }
    double best = -1e100;
    int p = -1;
    for(int i = 0; i < sums.size(); i++) {
      if(prob[i] > best) {
        best = prob[i];
        p = i;
      }
    }
    System.out.println();
    System.out.println("Dipilih kelas " + values.get(at).get(p) + " dengan peluang " + (best / tot));
    System.out.println();
  }

  public static void main(String[] args) {
    init();
    read_header("car.data");
    String class_attributes = "values";
    ArrayList<ArrayList<String>> instances = prepare_naive_bayes(class_attributes);
    entry_instances(instances, class_attributes);
    //debug_bayes(class_attributes);

    String[] s = {"med","vhigh","3","4","big","med","acc"};
    ArrayList<String> a = new ArrayList<String> (Arrays.asList(s));
    do_naive_bayes(class_attributes, a);
    /*
    for(ArrayList<String> ins : instances) {
      do_naive_bayes(class_attributes, ins);
    }
    */
  }
}
