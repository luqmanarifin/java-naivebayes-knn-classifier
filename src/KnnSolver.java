import java.util.*;

class KnnSolver {
	private String Dataset[][];
	private int attr;
	private int nData;
	private int closeData[];
	public KnnSolver(String[][] DS, int _nData, int _attr){
		Dataset = new String[_nData][_attr];
		for (int i = 0; i < _nData; i++){
			for (int j = 0; j < _attr; j++){
				Dataset[i][j] = DS[i][j];
			}
		}
		nData = _nData;
		attr = _attr;
	}
	public float solveFullSet(int x){
		float right = 0;
		for (int i = 0; i < nData; i++){
			closeData = new int[x];
			int nDataJarak = 0;
			int k = 0;
			for (int j = 0; j < nData; j++){
				if (nDataJarak < x){
					closeData[k] = j;
					k++;
					nDataJarak++;
				}else if(getJarak(j,Dataset[i]) < getJarak(getMaksJarak(x,i),Dataset[i])){
					closeData[getMaksJarak(x,i)] = j;
				}
			}
			for (int j = 0; j < x; j++){
				System.out.println(i+">"+closeData[j]+"-"+getJarak(closeData[j],Dataset[i]));
			}
			System.out.println(getClass(x));
			if (getClass(x) == Dataset[i][attr - 1]) right++;
		}
		return right/nData;
	}
	public float solveCrossFold(int x){
		float right = 0;
		int y = nData/10;
		int z = nData%10;
		int yz = 1;
		int yx = 0;
		for (int i = 0; i < 9; i++){
			if (yx == z) yz = 0;
			System.out.println("Fold-"+i);
			closeData = new int[x];
			int nDataJarak = 0;
			int l = 0;
			for (int j = i*y+yx; j <= i*y+yx+y-1+yz; j++){
				System.out.println(j);
				for (int k = 0; k < nData; k++){
					if (k<i*y+yx || k > i*y+yx+y-1+yz){
						if (nDataJarak < x){
						closeData[l] = k;
						l++;
						nDataJarak++;
						}else if(getJarak(k,Dataset[j]) < getJarak(getMaksJarak(x,j),Dataset[j])){
							closeData[getMaksJarak(x,j)] = k;
						}
						//System.out.println(i+"."+j+">"+k);
					}
				}
				for (int k = 0; k < x; k++){
					System.out.println(j+">"+closeData[k]+"-"+getJarak(closeData[k],Dataset[j]));
				}
				System.out.println(getClass(x));
				if (getClass(x) == Dataset[j][attr - 1]) right++;
			}
			if (yx<z) yx++;
		}
		System.out.println("Fold-9");
		closeData = new int[x];
		int nDataJarak = 0;
		int l = 0;
		for (int j = 9*y+yx; j < nData; j++){
			for (int k = 0; k < nData; k++){
				if (k < 9*y+yx){
					if (nDataJarak < x){
					closeData[l] = k;
					l++;
					nDataJarak++;
					}else if(getJarak(k,Dataset[j]) < getJarak(getMaksJarak(x,j),Dataset[j])){
						closeData[getMaksJarak(x,j)] = k;
					}
					//System.out.println(i+"."+j+">"+k);
				}
			}
			for (int k = 0; k < x; k++){
				System.out.println(j+">"+closeData[k]+"-"+getJarak(closeData[k],Dataset[j]));
			}
			System.out.println(getClass(x));
			if (getClass(x) == Dataset[j][attr - 1]) right++;
		}
		return right/nData;
	}
	private int getMaksJarak(int x, int y){
		int maksJarak = 0;
		int index = 0;
		for (int i = 1; i < x; i++){
			if (getJarak(y,Dataset[closeData[i]]) > getJarak(y,Dataset[closeData[index]])){
				maksJarak = getJarak(y,Dataset[closeData[i]]);
				index = i;
			}
		}
		return index;
	}		
	private int getJarak(int x, String[] Data){
		int jarak = 0;
		for (int i = 0; i < attr - 1; i++){
			if (Data[i] != Dataset[x][i]){
				jarak++;
			}
		}
		return jarak;
	}
	private String getClass(int x){
		String className[] = new String[0];
		int classMember[] = new int[0];
		String temp1[] = new String[0];
		int temp2[] = new int[0];
		for(int i = 0; i < x; i++){
			if (isExist(Dataset[closeData[i]][attr-1],className) == -1){
				className = new String[temp1.length + 1];
				classMember = new int[temp2.length + 1];
				for (int j = 0; j < temp1.length; j++){
					className[j] = temp1[j];
					classMember[j] = temp2[j];
				}
				className[temp1.length] = Dataset[closeData[i]][attr-1];
				classMember[temp2.length] = 1;
				temp1 = new String[className.length];
				temp2 = new int[classMember.length];
				for (int j = 0; j < className.length; j++){
					temp1[j] = className[j];
					temp2[j] = classMember[j];
				}
			}else{
				classMember[isExist(Dataset[closeData[i]][attr-1],className)]++;
				temp2[isExist(Dataset[closeData[i]][attr-1],className)]++;
			}
		}
		int index = 0;
		int max = classMember[0];
		for (int i = 1; i < classMember.length; i++){
			if(classMember[i] > max){
				max = classMember[i];
				index = i;
			}
		}
		return className[index];
	}
	private int isExist(String x, String[] y){
		int index = -1;
		for (int i = 0; i < y.length; i++){
			if (y[i] == x) index = i;
		}
		return index;
	}
	/*private boolean isExist(String x, String[] y){
		boolean found = false;
		for (int i = 0; i < y.length; i++){
			if (y[i] == x) found = true;
		}
		return found;
	}*/
}
