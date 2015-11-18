import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Luqman A. Siswanto on 18/11/2015.
 */
public class Instance {
    // name of instance is guaranteed to be pairwise distinct
    public ArrayList<String> atr_names;
    public ArrayList<Object> atr_values;
    public HashMap<String, Integer> atr_map;
    public Object clazz;

    public Instance() {
        atr_names = new ArrayList<String>();
        atr_values = new ArrayList<Object>();
        atr_map = new HashMap<String, Integer>();
    }
    // add attribute by name
    public void addAttribute(String atr) {
        atr_map.put(atr, atr_names.size());
        atr_names.add(atr);
    }
    // add value of attribute by name of attribute and value
    public void addValue(String atr, Object o) {
        int at = atr_map.get(atr);
        addValue(at, o);
    }
    // add value of attribute by index of attribute and value
    public void addValue(int at, Object o) {
        atr_values.set(at, o);
    }
    // get value of attribute by index
    public Object getValue(int at) {
        return atr_values.get(at);
    }
    // get value of attribute by name of atribute
    public Object getValue(String atr) {
        int at = atr_map.get(atr);
        return getValue(at);
    }

    public Instance copy() {
        Instance c = new Instance();
        for(String it : this.atr_names) {
            c.atr_names.add(it);
        }
        c.atr_map = new HashMap<String, Integer>(this.atr_map);
        return c;
    }

    // for k-NN stuffs
    public int getDiff(Instance other) {
        int dif = 0;
        for(int i = 0; i < other.atr_names.size(); i++) {
            if(other.atr_values.get(i).equals(this.atr_values.get(i))) {
                dif++;
            }
        }
        return dif;
    }
}
