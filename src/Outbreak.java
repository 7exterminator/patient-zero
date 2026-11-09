import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. The tree is rooted in person 0.
 */
public class Outbreak {

    /** Fills parent[] and returns the people in BFS order from person 0 (a parent comes before its children). */
    private static int[] bfsOrder(int n, int[][] nb, int[] parent) {
        int[] order = new int[n];
        Arrays.fill(parent, -1);
        int head = 0, tail = 0;
        order[tail++] = 0;
        while (head < tail) {
            int v = order[head++];
            for (int u : nb[v]) {
                if (u != parent[v]) {
                    parent[u] = v;
                    order[tail++] = u;
                }
            }
        }
        return order;
    }

    /**
     * Tested people with the smallest total cost, so that every link has at least one tested person.
     * take[v]: the cheapest cost of the subtree of v when v is tested.
     * skip[v]: the cheapest cost of the subtree of v when v is not tested (all children must be tested).
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        if (n == 0) return new boolean[0];
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        long[] take = new long[n], skip = new long[n];
        for (int idx = n - 1; idx >= 0; idx--) {
            int v = order[idx];
            take[v] += cost[v];
            int p = parent[v];
            if (p >= 0) {
                take[p] += Math.min(take[v], skip[v]);
                skip[p] += take[v];
            }
        }
        boolean[] tested = new boolean[n];
        tested[0] = take[0] <= skip[0];
        for (int idx = 1; idx < n; idx++) {
            int v = order[idx];
            tested[v] = !tested[parent[v]] || take[v] <= skip[v];
        }
        return tested;
    }

    /**
     * The number of links on the longest path in the tree. For every person the two longest ways down
     * are kept; the best path through the person is their sum.
     */
    public static int longestChain(int n, int[][] nb) {
        if (n == 0) return 0;
        int[] parent = new int[n];
        int[] order = bfsOrder(n, nb, parent);
        int[] first = new int[n], second = new int[n];
        int best = 0;
        for (int idx = n - 1; idx >= 0; idx--) {
            int v = order[idx];
            best = Math.max(best, first[v] + second[v]);
            int p = parent[v];
            if (p >= 0) {
                int len = first[v] + 1;
                if (len > first[p]) { second[p] = first[p]; first[p] = len; }
                else if (len > second[p]) second[p] = len;
            }
        }
        return best;
    }
}
