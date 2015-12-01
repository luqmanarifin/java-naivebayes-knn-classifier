import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GUIApp {

    // Swing attributes
    private JTabbedPane typeTab;
    private JPanel implementationPanel;
    private JPanel appPanel;
    private JPanel statPanel;
    private JPanel settingsPanel;
    private JButton open;
    private JComboBox algoCombo;
    private JComboBox trainMethodCombo;
    private JTable table1;
    private JComboBox attrCombo;
    private JButton startButton;
    private JEditorPane editorPane1;
    private JPanel mainPanel;
    private JFileChooser fileChooser;

    // Attributes
    private String name_relation;
    private double tp=0,tn=0,fp=0,fn=0;
    private double accuracy_total=0,precision_total=0,recall_total=0;
    private double tp_total=0,tn_total=0,fp_total=0,fn_total=0;

    // [atribute]
    private ArrayList<String> names;
    private HashMap<String, Integer> map_names;

    // [atribute][value]
    private ArrayList<ArrayList<String>> values;
    private ArrayList<HashMap<String, Integer>> map_values;

    // [kelas][atribute][value]
    private ArrayList<ArrayList<ArrayList<Integer>>> cl;
    private ArrayList<Integer> sums;

    private BufferedReader reader;
    private File file;

    private ByteArrayOutputStream output;

    public GUIApp() {
        // Komponen Swing
        fileChooser = new JFileChooser();
        open.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadData();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                start();
            }
        });

        algoCombo.addItem("K-NN");
        algoCombo.addItem("Naive Bayes");
        trainMethodCombo.addItem("Full");
        trainMethodCombo.addItem("10 Fold");

        // Inisialisasi Output
        PrintStream printer,old;
        output = new ByteArrayOutputStream();
        printer = new PrintStream(output);
        old = System.out;
        System.setOut(printer);


    }

    private void start() {

        // Inisialisasi nilai statistik
        tp = 0;
        tn = 0;
        fp = 0;
        fn = 0;
        init();
        readHeader(file.getAbsolutePath());
        String class_attributes = (String)attrCombo.getSelectedItem();
        ArrayList<ArrayList<String>> instances = prepare_naive_bayes(class_attributes);
        entry_instances(instances, class_attributes);

        int[] set = new int[instances.size()];
        for (int j=0;j<set.length;j++){
            set[j]=1;
        }

        System.out.println("\nFULLSET TRAINING");
        for(ArrayList<String> ins : instances) {
            System.out.println("============================");
            System.out.println("solver.Instance:" + ins.toString());
            do_naive_bayes(class_attributes, ins,set);

        }
        double accuracy = (tp + tn) / (tp+tn+fp+fn);
        System.out.println("Accuracy: "+ accuracy);
        double precision = (tp) / (tp+fp);
        System.out.println("Precision: "+ precision);
        double recall = (tp) / (tp+fn);
        System.out.println("Recall: "+ recall);

        editorPane1.setText(output.toString());

    }

    public void init() {
        names = new ArrayList<String>();
        map_names = new HashMap<String, Integer>();
        values = new ArrayList<ArrayList<String>>();
        map_values = new ArrayList<HashMap<String, Integer>>();
    }

    public void addValues(String str, String val) {
        int at = map_names.get(str);
        map_values.get(at).put(val, values.get(at).size());
        values.get(at).add(val);
    }

    public void addAttribute(String str) {
        map_names.put(str, names.size());
        names.add(str);
        map_values.add(new HashMap<String, Integer>());
        values.add(new ArrayList<String>());
    }

    public void readHeader(String str) {
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

    public ArrayList<ArrayList<String>> prepare_naive_bayes(String atr) {
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

    public void entry_instances(ArrayList<ArrayList<String>> instances, String atr) {
        int at = map_names.get(atr);
        sums = new ArrayList<>();
        cl = new ArrayList<>();
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

    public void entry(ArrayList<ArrayList<String>> instances, String atr, int[] trainingset) {
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

    public void do_naive_bayes(String atr, ArrayList<String> ins, int[] trainingset) {
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
    public void doFullTrainingSet(){
        tp = 0;
        tn = 0;
        fp = 0;
        fn = 0;
        init();
        String class_attributes = "play";
        readHeader("weather.nominal.arff");
        ArrayList<ArrayList<String>> instances = prepare_naive_bayes(class_attributes);
        entry_instances(instances, class_attributes);

        int[] set = new int[instances.size()];
        for (int j=0;j<set.length;j++){
            set[j]=1;
        }

        System.out.println("\nFULLSET TRAINING");
        for(ArrayList<String> ins : instances) {
            System.out.println("============================");
            System.out.println("solver.Instance:" + ins.toString());
            do_naive_bayes(class_attributes, ins,set);
        }
        double accuracy = (tp + tn) / (tp+tn+fp+fn);
        System.out.println("Accuracy: "+ accuracy);
        double precision = (tp) / (tp+fp);
        System.out.println("Precision: "+ precision);
        double recall = (tp) / (tp+fn);
        System.out.println("Recall: "+ recall);

    }
    public void ten(){
        //TEN FOLD CROSS VALIDATION TRAINING
        System.out.println("\nTEN FOLD CROSS VALIDATION TRAINING");
        // variables
        //Ten Fold training set for weather.nominal.arff
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
            readHeader("weather.nominal.arff");
            String class_attributes = "play";
            ArrayList<ArrayList<String>> instances2 = prepare_naive_bayes(class_attributes);
            entry(instances2, class_attributes,set1[i]);


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

    public void loadData(){
        int i=0;
        init();

        //Inisialisasi tabel
        DefaultTableModel model = new DefaultTableModel();
        table1.setModel(model);

        // Pilih File
        int retval = fileChooser.showOpenDialog(null);
        if (retval == JFileChooser.APPROVE_OPTION){
            file = fileChooser.getSelectedFile();
        }

        // Load Attribute name
        readHeader(file.getAbsolutePath());

        // Insert data ke GUI
        attrCombo.removeAllItems();
        for (String name : names){
            model.addColumn(name);
            attrCombo.addItem(name);
        }

        ArrayList<ArrayList<String>> rows = prepare_naive_bayes((String)attrCombo.getSelectedItem());
        entry_instances(rows, (String)attrCombo.getSelectedItem());

        for (ArrayList<String> row : rows){
            i = 0;
            String[] rowdatatemp = new String[names.size()];
            for (String attrval : row){
                rowdatatemp[i] = attrval;
                i++;
            }
            model.addRow(rowdatatemp);
        }

    }


    public static void main(String[] args) {

        // L&F sistem
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }

        // Inisialisasi
        GUIApp guiApp = new GUIApp();
        JFrame frame = new JFrame("JAVA K-NN & Naive Bayes Classifier");
        frame.setContentPane(guiApp.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation((1366/2)-frame.getWidth()/2, (768/2)-frame.getHeight()/2);
        frame.setVisible(true);




    }
}
