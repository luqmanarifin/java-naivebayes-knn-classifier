package solver;

public class KnnSolver {
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
	public float solveFullSet(int x, int atributkelas){
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
				}else if(getJarak(j,Dataset[i],atributkelas) < getJarak(closeData[getMaksJarak(x,i,atributkelas)],Dataset[i],atributkelas)){
					closeData[getMaksJarak(x,i,atributkelas)] = j;
				}
				//System.out.println(nData+" "+i+">"+closeData[k-1]+"-"+getJarak(closeData[k-1],Dataset[i],atributkelas));
			}
			/*for (int j = 0; j < x; j++){
				System.out.println(i+">"+closeData[j]+"-"+getJarak(closeData[j],Dataset[i],atributkelas));
			}
			System.out.println(getClass(x,atributkelas));*/
			if (getClass(x,atributkelas) == Dataset[i][atributkelas]) right++;
		}
		return right/nData;
	}
	public float solveCrossFold(int x, int atributkelas){
		float right = 0;
		int y = nData/10;
		int z = nData%10;
		int yz = 1;
		int yx = 0;
		int fold;
		if (nData < 10) fold = nData;
		else fold = 10;
		for (int i = 0; i < fold; i++){
			if (yx == z) yz = 0;
			//System.out.println("Fold-"+i);
			closeData = new int[x];
			int nDataJarak = 0;
			int l = 0;
			for (int j = i*y+yx; j <= i*y+yx+y-1+yz; j++){
				//System.out.println(j);
				for (int k = 0; k < nData; k++){
					if (k<i*y+yx || k > i*y+yx+y-1+yz){
						if (nDataJarak < x){
						closeData[l] = k;
						l++;
						nDataJarak++;
						}else if(getJarak(k,Dataset[j],atributkelas) < getJarak(closeData[getMaksJarak(x,j,atributkelas)],Dataset[j],atributkelas)){
							closeData[getMaksJarak(x,j,atributkelas)] = k;
						}
					}
				}
				/*for (int k = 0; k < x; k++){
					System.out.println(j+">"+closeData[k]+"-"+getJarak(closeData[k],Dataset[j],atributkelas));
				}
				System.out.println(getClass(x,atributkelas));*/
				if (getClass(x,atributkelas) == Dataset[j][atributkelas]) right++;
			}
			if (yx<z) yx++;
		}
		return right/nData;
	}
	private int getMaksJarak(int x, int y, int atributkelas){
		int maksJarak = 0;
		int index = 0;
		for (int i = 1; i < x; i++){
			if (getJarak(y,Dataset[closeData[i]],atributkelas) > getJarak(y,Dataset[closeData[index]],atributkelas)){
				maksJarak = getJarak(y,Dataset[closeData[i]],atributkelas);
				index = i;
			}
		}
		return index;
	}		
	private int getJarak(int x, String[] Data, int atributkelas){
		int jarak = 0;
		for (int i = 0; i < attr; i++){
			if ((Data[i] != Dataset[x][i]) && (i != atributkelas)){
				jarak++;
			}
		}
		return jarak;
	}
	private String getClass(int x, int atributkelas){
		String className[] = new String[0];
		int classMember[] = new int[0];
		String temp1[] = new String[0];
		int temp2[] = new int[0];
		for(int i = 0; i < x; i++){
			if (isExist(Dataset[closeData[i]][atributkelas],className) == -1){
				className = new String[temp1.length + 1];
				classMember = new int[temp2.length + 1];
				for (int j = 0; j < temp1.length; j++){
					className[j] = temp1[j];
					classMember[j] = temp2[j];
				}
				className[temp1.length] = Dataset[closeData[i]][atributkelas];
				classMember[temp2.length] = 1;
				temp1 = new String[className.length];
				temp2 = new int[classMember.length];
				for (int j = 0; j < className.length; j++){
					temp1[j] = className[j];
					temp2[j] = classMember[j];
				}
			}else{
				classMember[isExist(Dataset[closeData[i]][atributkelas],className)]++;
				temp2[isExist(Dataset[closeData[i]][atributkelas],className)]++;
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
}
