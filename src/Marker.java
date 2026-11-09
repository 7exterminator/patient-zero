import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    private static final long BASE = 131;
    private static final long MOD = 1_000_000_007L;

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
     * Mission 4. Rabin-Karp. The number of positions i in a such that the k letters a[i..i+k-1]
     * also occur in b. Hashes of all windows of b go into a map. For every window of a the hash is
     * rolled in O(1), looked up, and the letters are compared when a hash is found.
     */
    public static int sharedWords(String a, String b, int k) {
        if (k <= 0 || a.length() < k || b.length() < k) return 0;
        long top = 1;
        for (int i = 1; i < k; i++) top = top * BASE % MOD;

        Map<Long, List<Integer>> windows = new HashMap<>();
        long h = hash(b, k);
        windows.computeIfAbsent(h, x -> new ArrayList<>()).add(0);
        for (int i = k; i < b.length(); i++) {
            h = roll(h, b.charAt(i - k), b.charAt(i), top);
            windows.computeIfAbsent(h, x -> new ArrayList<>()).add(i - k + 1);
        }

        int count = 0;
        h = hash(a, k);
        for (int i = 0; ; i++) {
            List<Integer> same = windows.get(h);
            if (same != null) {
                for (int j : same) {
                    if (equalWords(a, i, b, j, k)) { count++; break; }
                }
            }
            if (i + k >= a.length()) break;
            h = roll(h, a.charAt(i), a.charAt(i + k), top);
        }
        return count;
    }

    private static long hash(String s, int k) {
        long h = 0;
        for (int i = 0; i < k; i++) h = (h * BASE + s.charAt(i)) % MOD;
        return h;
    }

    private static long roll(long h, char out, char in, long top) {
        long v = (h - out * top % MOD + MOD) % MOD;
        return (v * BASE + in) % MOD;
    }

    private static boolean equalWords(String a, int i, String b, int j, int k) {
        for (int t = 0; t < k; t++) {
            if (a.charAt(i + t) != b.charAt(j + t)) return false;
        }
        return true;
    }
}
