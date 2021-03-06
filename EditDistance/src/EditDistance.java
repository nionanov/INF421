import java.util.*;

public class EditDistance implements EditDistanceInterface {

    int c_i, c_d, c_r;
    int MAX = Integer.MAX_VALUE;
    int UNDEF = -1;
        
    public EditDistance(int c_i, int c_d, int c_r){
        this.c_i = c_i;
        this.c_d = c_d;
        this.c_r = c_r;
    }
   
    public int[][] editDistance(String s1, String s2) {
        /* To be completed in Q1 */
    	int m = s1.length();
    	int n = s2.length();
    	int[][] d = new int[m+1][n+1];
//For Q2    	
    	for(int i = 0; i <= m; i++)
    		for(int j = 0; j <= n; j++)
    			d[i][j] = UNDEF;
    	

//For Q1    	
//    	for(int i = 0; i <= m; i++)
//    		d[i][0] = i*c_d;
//    	for(int i = 0; i <= n; i++)
//    		d[0][i] = i*c_i;
//    	
//        for(int i = 1; i <= m; i++)
//        	for(int j = 1; j <= n; j++)
//        		if(s1.charAt(i-1) == s2.charAt(j-1))
//        			d[i][j] = d[i-1][j-1];
//        		else
//        			d[i][j] = Math.min(Math.min(d[i-1][j] + c_d, d[i][j-1] + c_i), d[i-1][j-1]+c_r);
//        
    	/* To be optimized in Q2 */
    	editDistanceAux(s1, m, s2, n, d);
        return d;
    }


    public void editDistanceAux(String s1, int i, String s2, int j, int[][]d) {
        /* To be completed in Q2 in order to optimize editDistance */
    	if(d[i][j]!=UNDEF) return;    	
    	if(( i == 0 ) || (j == 0))
    	{
    		d[i][j] = c_i*j +c_d*i;
    		return;
    	}
    	editDistanceAux(s1, i-1, s2, j-1, d);
    	int min;
    	
    	if(s1.charAt(i-1) == s2.charAt(j-1))
    		d[i][j] = d[i-1][j-1];
    	else
    	{
    		min = d[i-1][j-1]+c_r;
    		if(c_d < min)
    		{
    			editDistanceAux(s1, i-1, s2, j, d);
    			if(d[i-1][j]+c_d < min)
    				min = d[i-1][j]+c_d;
    		}
    		if(c_i < min)
    		{
    			editDistanceAux(s1, i, s2, j-1, d);
    			if(d[i][j-1] +c_i < min)
    				min = d[i][j-1] +c_i; 
    		}
    		
    		d[i][j] = min;
    	}
    }

    public List<String> minimalEdits(String s1, String s2) {
        /* To be completed (for extra credit) in Q3 */
    	ArrayList<String> p = new ArrayList<String>();
    	int[][]d = editDistance(s1,s2);
    	int i = s1.length();
    	int j = s2.length();
    	
    	while((i > 0) || (j > 0)) 
    	{
    		if(i == 0)
    		{
    			while(j > 0 ){
    				p.add(0,"insert(0," + s2.charAt(j-1) + ")");
    				j--;}
    		}
    		else if(j == 0)
    		{
    			while(i > 0)
    			{
    				p.add(0,"delete(0)");
    				i--;
    			}
    		}
    		else if(s1.charAt(i-1) == s2.charAt(j-1))
    		{ 
    			j --;
    			i --;
    			continue;
    		}
    		else if(d[i][j] == d[i-1][j-1] + c_r)
    		{
    			p.add(0, "replace(" + (j-1) + "," + s2.charAt(j-1) + ")");
    			i--; 
    			j--;
    			continue;
    		}
    		else if(d[i][j] == d[i][j-1] + c_i)
    		{
    			p.add(0,"insert(" + (j-1) + "," + s2.charAt(j-1) + ")");
    			j --;
    			continue;
    		}
    		else if(d[i][j] == d[i-1][j] + c_d)
    		{
    			p.add(0,"delete(" + j + ")");
    			i --;
    			continue;
    		}
    	}
    	System.out.println(p.size());
        return p;
    }

}
