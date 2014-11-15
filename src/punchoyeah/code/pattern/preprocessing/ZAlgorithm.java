package punchoyeah.code.pattern.preprocessing;

import java.util.ArrayList;

public class ZAlgorithm {
	
	static final char SEPERATOR = '`';
	
	/** 
	 * @param s, length=n
	 * @return Zi(1<=i<=n)
	 * 
	 */
	private static int[] calZValue(String s) {
		if(s.length() <= 1) return null;
		
		int len = s.length();
		int[] Z = new int[len];

		int right=0, left=0;
		Z[0] = 0;
		
		for(int k=1; k<len; k++) {			
			// r(k-1) < k
			if(right<k) {
				Z[k] = naiveComp(s, 0, k);
				if(Z[k] > 0) {
					left = k;
					right = k+Z[k]-1;
				}
			} else { // r(k-1) >= i, i.e. k is contained in s[l(k-1)...r(k-1)]
				int beta = right - k + 1;
				int _k 	 = k - left;
				if(Z[_k] < beta) {
					Z[k] = Z[_k];
				} else {
					Z[k] = beta + naiveComp(s, beta, right+1);
					left = k;
					right = k+Z[k]-1;
				}
			}			
		}
		
		return Z;
	}
	
	/**
	 *
	 * @param s
	 * @param idx1
	 * @param idx2
	 * @return compare one by one since s[idx1] and s[idx2], get maximum matching length
	 * 
	 */
	private static int naiveComp(String s, int idx1, int idx2) {
		if(idx1 == idx2)
			return -1;
		
		int i=(idx1 < idx2) ? idx1 : idx2;
		int j=(idx1 > idx2) ? idx1 : idx2;
		
		int len = s.length();
		int same = 0;
		while((j<len) && (s.charAt(i)==s.charAt(j))) {
			i++;
			j++;
			same++;
		}
		return same;
	}
	
	
	/**
	 * Space is saved, let P.len=n T.len=m, 
	 * then only use O(n) to store the z values
	 * O(m) time
	 * 
	 * @param P
	 * @param T
	 * @return
	 */
	public static ArrayList<Integer> efficientMatch(String P, String T) {
		ArrayList<Integer> occurrences = new ArrayList<Integer>();
		
		if(P == null || P == "" || 
		   T == null || T == "")
			return occurrences;
		
		String full = P + SEPERATOR + T;
		int p_len = P.length();

		int[] ZP = new int[p_len];
		ZP[0] = 0;
		
		int z = 0;
		int l = 0, r = 0;		
		for(int i=1; i<full.length(); i++) {			
			if(r < i) {
				z = naiveComp(full, 0, i);
				if (z > 0) {
					l = i;
					r = i + z - 1;
				} 
			} else {
				int beta = r - i + 1;
				int _k	 = i - l;
			  
				if(ZP[_k] < beta) {
					z = ZP[_k];
				} else {
					z = beta + naiveComp(full, beta, r+1);
					l = i;
					r = i + z - 1;
				}
			}			
			
			if(i<p_len) {
				ZP[i] = z;
 			} else if (z == p_len) {
 				occurrences.add(i-(P.length()+1));
 			}
		}
		
		return occurrences;
	}
	
	/**
	 * Space is saved, let P.len=n T.len=m, 
	 * then use O(m) to store the z values
	 * O(m) time
	 * 
	 * @param P
	 * @param T
	 * @return all occurrences of P in T
	 * 
	 */
	public static ArrayList<Integer> match(String P, String T) {
		ArrayList<Integer> occurrences = new ArrayList<Integer>();
		
		if(P == null || P == "" || 
		   T == null || T == "")
			return occurrences;
		
		String full = P + SEPERATOR + T;
		int[] Z = calZValue(full);
		for(int i=P.length()+1; i<full.length(); i++) {
			if(Z[i] == P.length()) {
				occurrences.add(i-(P.length()+1));
			}
		}
		return occurrences;
	}
	
	
	public static void main(String[] args) {
		String s = "aabcaabxaaz";
		int[] z = calZValue(s);
		for(int i=0; i<z.length; i++) {
			System.out.print("Z("+ (i+1) +")=" + z[i] + "  ");
		}
		System.out.println();
		
		String s2 = "abxyabxz`xabxyabxyabxz";
		int[] z2 = calZValue(s2);
		for(int i=0; i<z2.length; i++) {
			System.out.print("Z("+ (i+1) +")=" + z2[i] + "  ");
		}
		System.out.println();
		
		String P="abxyabxz";
		//String T="xabxyabxyabxz";
		String T= "xabxyabxyabxztwewabxyawewrabxyabxzdsfsf";
		ArrayList<Integer> occurrences = match(P, T);
		
		System.out.println("-- First Method -->");
		if(!occurrences.isEmpty()) {
			System.out.print("P occurs in T:");
			for(Integer index : occurrences) {
				System.out.print("    " + index);
			}
			System.out.println();
		}

		occurrences = efficientMatch(P, T);
		System.out.println("-- Space Efficient Method -->");
		if(!occurrences.isEmpty()) {
			System.out.print("P occurs in T:");
			for(Integer index : occurrences) {
				System.out.print("    " + index);
			}
			System.out.println();
		}
	}

}
