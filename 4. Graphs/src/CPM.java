import edu.princeton.cs.algorithms.AcyclicLP;
import edu.princeton.cs.algorithms.DirectedEdge;
import edu.princeton.cs.algorithms.EdgeWeightedDigraph;
import edu.princeton.cs.introcs.StdIn;
import edu.princeton.cs.introcs.StdOut;

public class CPM {

    public static void main(String[] args) {
        int n = StdIn.readInt();
        StdIn.readLine();
        edu.princeton.cs.algorithms.EdgeWeightedDigraph G;
        G = new EdgeWeightedDigraph(2 * n + 2);

        int s = 2 * n;
        int t = 2 * n + 1;

        for (int i = 0; i < n; i++) {
            String[] a = StdIn.readLine().split("\\s+");
            double duration = Double.parseDouble(a[0]);
            G.addEdge(new DirectedEdge(i, i + n, duration));
            G.addEdge(new DirectedEdge(s, i, 0.0));
            G.addEdge(new DirectedEdge(i + n, t, 0.0));
            for (int j = 1; j < a.length; j++) {
                int successor = Integer.parseInt(a[j]);
                G.addEdge(new DirectedEdge(i + n, successor, 0.0));
            }
        }

        AcyclicLP lp = new AcyclicLP(G, s);

        StdOut.println("Start time:");
        for (int i = 0; i < n; i++) {
            StdOut.printf("%4d: %5.1f\n", i, lp.distTo(i));
        }
        StdOut.printf("Finish time: %5.1f\n", lp.distTo(t));
    }
}