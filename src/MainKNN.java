import java.util.*;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.String;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class MainKNN {
	public static void main (String args[]){
		/*String[][] Dataset = new String[28][5];
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
		Dataset[14][0] = "sunny"; Dataset[14][1] = "hot"; Dataset[14][2] = "high"; Dataset[14][3] = "false"; Dataset[14][4] = "no";
		Dataset[15][0] = "sunny"; Dataset[15][1] = "hot"; Dataset[15][2] = "high"; Dataset[15][3] = "true"; Dataset[15][4] = "no";
		Dataset[16][0] = "overcast"; Dataset[16][1] = "hot"; Dataset[16][2] = "high"; Dataset[16][3] = "false"; Dataset[16][4] = "yes";
		Dataset[17][0] = "rainy"; Dataset[17][1] = "mild"; Dataset[17][2] = "high"; Dataset[17][3] = "false"; Dataset[17][4] = "yes";
		Dataset[18][0] = "rainy"; Dataset[18][1] = "cool"; Dataset[18][2] = "normal"; Dataset[18][3] = "false"; Dataset[18][4] = "yes";
		Dataset[19][0] = "rainy"; Dataset[19][1] = "cool"; Dataset[19][2] = "normal"; Dataset[19][3] = "true"; Dataset[19][4] = "no";
		Dataset[20][0] = "overcast"; Dataset[20][1] = "cool"; Dataset[20][2] = "normal"; Dataset[20][3] = "true"; Dataset[20][4] = "yes";
		Dataset[21][0] = "sunny"; Dataset[21][1] = "mild"; Dataset[21][2] = "high"; Dataset[21][3] = "false"; Dataset[21][4] = "no";
		Dataset[22][0] = "sunny"; Dataset[22][1] = "cool"; Dataset[22][2] = "normal"; Dataset[22][3] = "false"; Dataset[22][4] = "yes";
		Dataset[23][0] = "rainy"; Dataset[23][1] = "mild"; Dataset[23][2] = "normal"; Dataset[23][3] = "false"; Dataset[23][4] = "yes";
		Dataset[24][0] = "sunny"; Dataset[24][1] = "mild"; Dataset[24][2] = "normal"; Dataset[24][3] = "true"; Dataset[24][4] = "yes";
		Dataset[25][0] = "overcast"; Dataset[25][1] = "mild"; Dataset[25][2] = "high"; Dataset[25][3] = "true"; Dataset[25][4] = "yes";
		Dataset[26][0] = "overcast"; Dataset[26][1] = "hot"; Dataset[26][2] = "normal"; Dataset[26][3] = "false"; Dataset[26][4] = "yes";
		Dataset[27][0] = "rainy"; Dataset[27][1] = "mild"; Dataset[27][2] = "high"; Dataset[27][3] = "true"; Dataset[27][4] = "no";
		KnnSolver KnnS = new KnnSolver(Dataset, 28, 5);*/
		Matriks m = new Matriks();
		try {
            m = m.createMatrixFromFile("weather.nominal.arff",5,14);
        } catch (Exception e) {
            e.printStackTrace();
        }
		int baris = m.getJumlahInstance();
		int kolom = m.getJumlahAtribut();
		String[][] datas = m.getMatriks();
		System.out.println("Test: " + datas[0][0]);
		KnnSolver KnnS = new KnnSolver(datas, baris, kolom);
		System.out.println("accuration: " + KnnS.solveCrossFold(3));
		//System.out.println("accuration: " + KnnS.solveFullSet(3));
	}
}
