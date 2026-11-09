import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. Prefix function of p. pi[i] is the length of the longest proper prefix of p[0..i]
     * that is also a suffix of p[0..i]. Example: "ACGAC" gives [0, 0, 0, 1, 2].
     */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (p.charAt(i) == p.charAt(k)) k++;
            pi[i] = k;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions of p in text, in increasing order. Overlaps count.
     * The text pointer only moves forward.
     */
    public static int[] findAll(String text, String p) {
        int n = text.length(), m = p.length();
        if (m == 0 || m > n) return new int[0];
        int[] pi = prefixFunction(p);
        int[] found = new int[n - m + 1];
        int count = 0, k = 0;
        for (int i = 0; i < n; i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) k = pi[k - 1];
            if (text.charAt(i) == p.charAt(k)) k++;
            if (k == m) {
                found[count++] = i - m + 1;
                k = pi[m - 1];
            }
        }
        return Arrays.copyOf(found, count);
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the k letters a[i..i+k-1] also occur in b.
     */
    public static int sharedWords(String a, String b, int k) {
        // TODO Mission 4
        return -1;
    }
}
