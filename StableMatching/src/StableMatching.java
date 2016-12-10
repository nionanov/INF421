import java.util.*;


class StableMatching implements StableMatchingInterface{
	private static int find_index(int[] arr, int elem)
	{
		int res = -1;
		for(int i = 0; i < arr.length; i++)
		{
			if(arr[i] == elem)
			{
				res = i; 
				break;
			}
		}
		return res;
	}
	
	private static int[] transform_array(int[] arr)
	{
		int n = arr.length;
		int[] res = new int[n];
		for(int i = 0; i < n; i++)
		{
			res[arr[i]] = i;
		}
		return res;
	}
	
	public int[] constructStableMatching(int n, int[][] menPrefs, int[][] womenPrefs){
		
		int[] bride = new int[n];
		Stack<Integer> singles = new Stack<Integer>(); //stack of single guys
		for (int i = n-1; i >= 0; i--)
		{
			singles.push(i);//all single at the beginning
			bride[i] = -1;
		}
		
		
		Set<Integer> engaged = new HashSet<Integer>(); // engaged girls
		int[][] menPrefs_tr = new int[n][n];
		int[][] womenPrefs_tr = new int[n][n];
		int[] meufs = new int[n];
		int[] attempt = new int[n];
		
		for(int i = 0; i < n; i++)
		{
			womenPrefs_tr[i] = transform_array(womenPrefs[i]);
			meufs[i] = -1;
		}		
		
		int sing, cur, f;//single guy, current guy(if any) and a chick
		int sing_pref, cur_pref;
		while(!singles.empty())
		{
			sing = (int)singles.pop();
			for(int i = attempt[sing]; i < n; i++) //examine i-th preference of a sing guy
			{
				f = menPrefs[sing][i];
				attempt[sing]++;
				if(meufs[f] == -1)
				{
					bride[sing] = f;
					meufs[f] = sing;
					break;
				}
				else
				{
					cur = meufs[f];
					sing_pref = womenPrefs_tr[f][sing];
					cur_pref = womenPrefs_tr[f][cur];
					if(sing_pref < cur_pref)
					{
						bride[sing] = f;
						bride[cur] = -1;
						meufs[f] = sing;
						singles.push(cur);
						break;
					}
				}
			}
			
		}
		return bride;
	}
	
}