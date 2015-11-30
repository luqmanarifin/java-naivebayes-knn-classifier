package solver;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ryanyonata
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.String;

public class Matriks 
{
	static BufferedReader reader;
	static String name_relation;
	private String[][] mat;
	private int jumlahatribut;
	private int jumlahinstance;

	public Matriks copyMatriks()
	{
		Matriks m = new Matriks();
		m.mat = new String[this.jumlahatribut][this.jumlahinstance];
		for (int i = 0; i < this.jumlahatribut; i++)
		{
			m.mat[i] = new String[this.jumlahinstance];
		}

		m.jumlahatribut = this.jumlahatribut;
		m.jumlahinstance = this.jumlahinstance;

		//Copy Isi
		for (int i = 0; i < this.jumlahatribut; i++)
		{
			for (int j = 0; j < this.jumlahinstance; j++)
			{
				m.mat[i][j] = this.mat[i][j];
			}
		}

		return m;
	}

	// Getter
	public int getJumlahAtribut()
	{
		return this.jumlahatribut;
	}

	public int getJumlahInstance()
	{
		return this.jumlahinstance;
	}

	public String[][] getMatriks()
	{
		return this.mat;
	}

	public String getElmt(int i, int j)
	{
		return this.mat[i][j];
	}

	// Setter
	public void setJumlahAtribut(int atr)
	{
		this.jumlahatribut = atr;
	}

	public void setJumlahInstance(int ins)
	{
		this.jumlahinstance = ins;
	}

	public void setMatriks(String[][] mnew)
	{
		mat = mnew;
	}

	public void printMatriks()
	{
		for (int i = 0; i < jumlahatribut; i++)
		{
            for (int j = 0; j < jumlahinstance; j++)
            {
				System.out.print(mat[i][j]);
				System.out.print(" ");
            }
            System.out.println("\n");
		}
    }

    public static int getJumlahAtributdarifile(String filename) throws Exception
    {
    	InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        String line;
        int nAtr = 0;
        while ((line = buffer.readLine()) != null) {
        	String[] vals = line.trim().split(",\\s*");

            nAtr = vals.length;
        }

        return nAtr;
    }

    public static int getJumlahInstancedarifile(String filename) throws Exception{
    	InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        String line;
        int nInstance = 0;
        while ((line = buffer.readLine()) != null) {
        	nInstance++;
        }

        return nInstance;
    }

    public static Matriks createMatrixFromFile(String filename, int nInstance, int nAtr) throws Exception
    {
    	String[][] matrix = null;
		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));
		System.out.println(filename);
		InputStream stream = ClassLoader.getSystemResourceAsStream(filename);
		BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));

        String line;

        int j = 0;

        while ((line = buffer.readLine()) != null) {
    		String[] vals = line.trim().split(",\\s*");
    		nAtr = vals.length;
       		if (matrix == null) {
           		matrix = new String[nInstance][nAtr];
        	}
        	//matrix = new String[nInstance][nAtr];

        	for (int col = 0; col < nAtr; col++) {
            	matrix[j][col] = vals[col];
        	}
        	j++;
        }
    
        Matriks matriksoriginal = new Matriks();
        matriksoriginal.setJumlahAtribut(nAtr);
        matriksoriginal.setJumlahInstance(nInstance);
        matriksoriginal.setMatriks(matrix);

        System.out.println("jumlah atribut: " + matriksoriginal.getJumlahAtribut());
        System.out.println("jumlah instance: " + matriksoriginal.getJumlahInstance());
        return matriksoriginal;
    }
}