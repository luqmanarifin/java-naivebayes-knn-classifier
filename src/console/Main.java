package console; /**
 * console.Main.java
 */

import solver.Instance;
import solver.KnnSolver;
import solver.Matriks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
  static Instance model;
  static String name_relation;
  static double tp=0,tn=0,fp=0,fn=0;
  static double accuracy_total=0,precision_total=0,recall_total=0;
  static double tp_total=0,tn_total=0,fp_total=0,fn_total=0;

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
//      System.out.println(System.getProperty("user.dir"));
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

  public static void entry(ArrayList<ArrayList<String>> instances, String atr, int[] trainingset) {
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
      if (trainingset[i] == 1) {
        String clazz = instances.get(i).get(at);
        int cval = map_values.get(at).get(clazz);
        for (int j = 0; j < names.size(); j++) {
          if (j == at) continue;
          int ival = map_values.get(j).get(instances.get(i).get(j));
          cl.get(cval).get(j).set(ival, cl.get(cval).get(j).get(ival) + 1);
        }
        sums.set(cval, sums.get(cval) + 1);
      }
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
  public static void do_naive_bayes(String atr, ArrayList<String> ins, int[] trainingset) {
    int at = map_names.get(atr);
    double[] prob = new double[sums.size()];
    int tot = 0;
    for(int i = 0; i < sums.size(); i++) {
      prob[i] = sums.get(i);
      tot += sums.get(i);
    }

    for(int i = 0; i < sums.size(); i++) {
      System.out.println("\nKelas " + values.get(at).get(i) + " dikategorikan dengan peluang :");
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

    // Hitung tp, tn, fp, fn
    if (values.get(at).get(p).equals(ins.get(at))){
      if (ins.get(at).equals("yes")){
        tp++;
        tp_total++;
      }
      else if (ins.get(at).equals("no")){
        tn++;
        tn_total++;
      }
    }
    else {
      if (ins.get(at).equals("yes")){
        fn++;
        fn_total++;
      }
      else if (ins.get(at).equals("no")){
        fp++;
        fp_total++;
      }
    }

    System.out.println("Dipilih kelas " + values.get(at).get(p) + " dengan peluang " + (best / tot));
    System.out.println();
  }
  public static void doFullTrainingSet(){
    tp = 0;
    tn = 0;
    fp = 0;
    fn = 0;
    init();
    String class_attributes = "play";
    read_header("weather.nominal.arff");
    ArrayList<ArrayList<String>> instances = prepare_naive_bayes(class_attributes);
    entry_instances(instances, class_attributes);

//    debug_bayes(class_attributes);
//    String[] s = {"sunny", "mild", "normal", "TRUE"};
//    ArrayList<String> a = new ArrayList<String> (Arrays.asList(s));
//    do_naive_bayes(class_attributes, a);


    int[] set = new int[instances.size()];
    for (int j=0;j<set.length;j++){
      set[j]=1;
    }
    System.out.println("\nFULLSET TRAINING");
    for(ArrayList<String> ins : instances) {
      System.out.println("============================");
      System.out.println("solver.Instance:" + ins.toString());
      do_naive_bayes(class_attributes, ins,set);
//      System.out.println("tp:"+tp);
//      System.out.println("tn:"+tn);
//      System.out.println("fp:"+fp);
//      System.out.println("fn:"+fn);
    }
    double accuracy = (tp + tn) / (tp+tn+fp+fn);
    System.out.println("Accuracy: "+ accuracy);
    double precision = (tp) / (tp+fp);
    System.out.println("Precision: "+ precision);
    double recall = (tp) / (tp+fn);
    System.out.println("Recall: "+ recall);

  }
  public static void ten(){
    //TEN FOLD CROSS VALIDATION TRAINING
    System.out.println("\nTEN FOLD CROSS VALIDATION TRAINING");
    // variables
    //Ten Fold training set for weather.nominal.arff
    int[][] set1 = {{0,0,1,1,1,1,1,1,1,1,1,1,1,1},
                    {1,1,0,0,1,1,1,1,1,1,1,1,1,1},
                    {1,1,1,1,0,0,1,1,1,1,1,1,1,1},
                    {1,1,1,1,1,1,0,0,1,1,1,1,1,1},
                    {1,1,1,1,1,1,1,1,0,1,1,1,1,1},
                    {1,1,1,1,1,1,1,1,1,0,1,1,1,1},
                    {1,1,1,1,1,1,1,1,1,1,0,1,1,1},
                    {1,1,1,1,1,1,1,1,1,1,1,0,1,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,0,1},
                    {1,1,1,1,1,1,1,1,1,1,1,1,1,0}
    };

    for(int i=0;i<10;i++) {
      System.out.println("============================");
      System.out.println("Fold ke-"+(i+1));

      init();
      read_header("weather.nominal.arff");
      String class_attributes = "play";
      ArrayList<ArrayList<String>> instances2 = prepare_naive_bayes(class_attributes);
      entry(instances2, class_attributes,set1[i]);

//      for (int n=0;n<instances2.size();n++){
//        System.out.println(instances2.get(n).toString());
//      }

      for (int k=0;k<set1[i].length;k++){
        //System.out.println("length: "+set1.length);
        if (set1[i][k] == 0){
          //System.out.println("K: "+k);
          ArrayList<String> ins = instances2.get(k);
          System.out.println("solver.Instance:" + ins.toString());
          do_naive_bayes(class_attributes, ins, set1[0]);

        }
      }
      System.out.println("Fold-"+(i+1)+" Stats");
      System.out.println("tp:"+tp);
      System.out.println("tn:"+tn);
      System.out.println("fp:"+fp);
      System.out.println("fn:"+fn);

      double accuracy = (tp + tn) / (tp+tn+fp+fn);
      accuracy_total += accuracy;
      System.out.println("Accuracy: "+ accuracy);
      double precision = (tp) / (tp+fp);
      precision_total += precision;
      System.out.println("Precision: "+ precision);
      double recall = (tp) / (tp+fn);
      recall_total+=recall;
      System.out.println("Recall: "+ recall);
      System.out.println("============================");
    }
    System.out.println("Average Stats:");
    System.out.println("Accuracy: "+ (accuracy_total/10));
    System.out.println("Precision: "+ (precision_total/10));
    System.out.println("Recall: "+ (recall_total/10));
    System.out.println();
    System.out.println("tp: "+ tp_total);
    System.out.println("tn: "+ tn_total);
    System.out.println("fp: "+ (fp_total));
    System.out.println("fn: "+ (fn_total));
  }

  public static void main(String[] args) {


    Matriks m = new Matriks();
    int baris = 0;
    int kolom = 0;
    int pilihan = 0;

    System.out.println("K-NN Classifier");
    System.out.println("1. 10-fold cross validation");
    System.out.println("2. Full training");
    System.out.println("Naive Bayes Classifier");
    System.out.println("3. 10-fold cross validation");
    System.out.println("4. Full training");
    System.out.println("Masukkan pilihan: ");
    Scanner scan = new Scanner(System.in);
    pilihan = scan.nextInt();

    if (pilihan == 1) { //KNN ten fold cross validation
      String[][] Dataset = new String[14][5];
      Dataset[0][0] = "sunny"; Dataset[0][1] = "hot"; Dataset[0][2] = "high"; Dataset[0][3] = "false"; Dataset[0][4] = "no";
      Dataset[1][0] = "sunny"; Dataset[1][1] = "hot"; Dataset[1][2] = "high"; Dataset[1][3] = "true"; Dataset[1][4] = "no";
      Dataset[2][0] = "overcast"; Dataset[2][1] = "hot"; Dataset[2][2] = "high"; Dataset[2][3] = "false"; Dataset[2][4] = "yes";
      Dataset[3][0] = "rainy"; Dataset[3][1] = "mild"; Dataset[3][2] = "high"; Dataset[3][3] = "false"; Dataset[3][4] = "yes";
      Dataset[4][0] = "rainy"; Dataset[4][1] = "cool"; Dataset[4][2] = "normal"; Dataset[4][3] = "false"; Dataset[4][4] = "yes";
      Dataset[5][0] = "rainy"; Dataset[5][1] = "cool"; Dataset[5][2] = "normal"; Dataset[5][3] = "true"; Dataset[5][4] = "no";
      Dataset[6][0] = "overcast"; Dataset[6][1] = "cool"; Dataset[6][2] = "normal"; Dataset[6][3] = "true"; Dataset[6][4] = "yes";
      Dataset[7][0] = "sunny"; Dataset[7][1] = "mild"; Dataset[7][2] = "high"; Dataset[7][3] = "false"; Dataset[7][4] = "no";
      Dataset[8][0] = "sunny"; Dataset[8][1] = "cool"; Dataset[8][2] = "normal"; Dataset[8][3] = "false"; Dataset[8][4] = "yes";
      Dataset[9][0] = "rainy"; Dataset[9][1] = "mild"; Dataset[9][2] = "normal"; Dataset[9][3] = "false"; Dataset[9][4] = "yes";
      Dataset[10][0] = "sunny"; Dataset[10][1] = "mild"; Dataset[10][2] = "normal"; Dataset[10][3] = "true"; Dataset[10][4] = "yes";
      Dataset[11][0] = "overcast"; Dataset[11][1] = "mild"; Dataset[11][2] = "high"; Dataset[11][3] = "true"; Dataset[11][4] = "yes";
      Dataset[12][0] = "overcast"; Dataset[12][1] = "hot"; Dataset[12][2] = "normal"; Dataset[12][3] = "false"; Dataset[12][4] = "yes";
      Dataset[13][0] = "rainy"; Dataset[13][1] = "mild"; Dataset[13][2] = "high"; Dataset[13][3] = "true"; Dataset[13][4] = "no";
      KnnSolver KnnS = new KnnSolver(Dataset, 14, 5);
      for (int i = 0; i < 14; i++) {
        for (int j = 0; j < 5; j++) {
          System.out.print(Dataset[i][j] + " ");
        }
        System.out.println();
      }
      System.out.println("accuration: " + KnnS.solveCrossFold(1,4));
    } else if (pilihan == 2){ //KNN Full Training
      System.out.println("Masukkan baris: ");
      Scanner scan2 = new Scanner(System.in);
      baris = scan2.nextInt();

      System.out.println("Masukkan kolom: ");
      Scanner scan3 = new Scanner(System.in);
      kolom = scan3.nextInt();

      try {
        m = m.createMatrixFromFile("weather.nominal.arff",baris,kolom);
      } catch (Exception e) {
        e.printStackTrace();
      }
      //int baris = m.getJumlahInstance();
      //int kolom = m.getJumlahAtribut();
      String[][] datas = m.getMatriks();
      for (int i = 0; i < baris; i++) {
        for (int j = 0; j < kolom; j++) {
          System.out.print(datas[i][j] + " ");
        }
        System.out.println();
      }

      KnnSolver KnnS = new KnnSolver(datas, baris, kolom);
      System.out.println("accuration: " + KnnS.solveFullSet(1,4));
    }
    else if (pilihan == 3){ // Naive Bayes 10 Fold Cross Validation
      ten();
    }
    else if (pilihan == 4){ // Naive Bayes Full Training Set
      doFullTrainingSet();
    }
  }
}

