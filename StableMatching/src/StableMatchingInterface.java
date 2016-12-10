public interface StableMatchingInterface {

  // A problem instance consists of the following data.

  // A nonnegative integer n is given.

  // There are n men and n women. The men are numbered from 0 to n-1,
  // and the women are numbered in the same manner.

  // Each of the men ranks the women in decreasing order of preference.
  // Thus, if m is a man, then menPrefs[m] is an array of n elements,
  // whose first element menPrefs[m][0] is m's most desirable woman, and
  // whose last element menPrefs[m][n-1] is m's least desirable woman.

  // Symmetrically, each of the women ranks the men in decreasing order
  // of preference. Thus, if w is a woman, then womenPrefs[w] is an
  // array of n elements, whose first element womenPrefs[w][0] is w's
  // most desirable man, and whose last element womenPrefs[w][n-1] is
  // w's least desirable man.

  public int[] constructStableMatching (
    int n,
    int[][] menPrefs,
    int[][] womenPrefs
  );

  // The method constructStableMatching must return a n-element array
  // "bride".  For each man m, the element bride[m] is the woman with
  // whom m is matched.  This array must represent a stable matching.

}
