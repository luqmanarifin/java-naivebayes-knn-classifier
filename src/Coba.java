/**
 * Created by Luqman A. Siswanto on 19/11/2015.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Coba {

  public static void main(String[] args) {
    String a = "@attribute play {yes, no}";
    String pattern = "\\@attribute\\s(.*)\\s\\{(.*)\\}";
    Pattern p = Pattern.compile(pattern);

    Matcher m = p.matcher(a);

    if(m.find()) {
      System.out.println(m.group(0));
    }
  }

}
