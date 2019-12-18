package Kruskals;

import java.util.*;
import java.lang.*;
import java.io.*;
import Utils.TableXY;
  
public class Graph {

    public static void main(String[] args) {
        (new Graph(1, 1)).Test();
    }

    public int V, E;   // V-> no. of vertices & E->no.of edges 
    public Edge edge[];// collection of all edges 
    private Edge result[];

    // Creates a graph with V vertices and E edges 
    public Graph(int v, int e) {
        V = v;
        E = e;
        edge = new Edge[E];
        result = new Edge[V - 1]; // Tnis will store the resultant MST

        for (int i = 0; i < E; i ++) {
            edge[i] = new Edge();
        }
    }

    // A utility function to find set of an element i 
    // (uses path compression technique) 
    private int find(Subset subsets[], int i) {
        // find root and make root as parent of i (path compression) 
        if (subsets[i].parent != i) {
            subsets[i].parent = find(subsets, subsets[i].parent);
        }

        return subsets[i].parent;
    }

    // A function that does union of two sets of x and y 
    // (uses union by rank) 
    private void union(Subset subsets[], int x, int y) {
        int xroot = find(subsets, x);
        int yroot = find(subsets, y);

        // Attach smaller rank tree under root of high rank tree 
        // (Union by Rank) 
        if (subsets[xroot].rank < subsets[yroot].rank) {
            subsets[xroot].parent = yroot;
        } else if (subsets[xroot].rank > subsets[yroot].rank) {
            subsets[yroot].parent = xroot;
        } else {
            // If ranks are same, then make one as root and increment 
            // its rank by one 
            subsets[yroot].parent = xroot;
            subsets[xroot].rank++;
        }
    }

    // The main function to construct MST using Kruskal's algorithm 
    public void kruskal() {
        for (int i = 0; i < result.length; i ++) {
            result[i] = new Edge();
        }

        // Step 1:  Sort all the edges in non-decreasing order of their 
        // weight.  If we are not allowed to change the given graph, we 
        // can create a copy of array of edges 
        Arrays.sort(edge);

        // Allocate memory for creating V ssubsets 
        Subset subsets[] = new Subset[V];
        for(int i = 0; i < subsets.length; i ++) {
            subsets[i] = new Subset();
        }

        // Create V subsets with single elements 
        for (int v = 0; v < V; v ++) {
            subsets[v].parent = v;
            subsets[v].rank = 0;
        }

        int index = 0; // Index used to pick next edge 
        int e = 0; // An index variable, used for result[] 

        // Number of edges to be taken is equal to V-1 
        while (e < V - 1) {
            // Step 2: Pick the smallest edge. And increment  
            // the index for next iteration 
            Edge next_edge = new Edge();
            next_edge = edge[index++];

            int x = find(subsets, next_edge.src);
            int y = find(subsets, next_edge.dest);

            // If including this edge does't cause cycle, 
            // include it in result and increment the index
            // of result for next edge
            if (x != y) {
                result[e++] = next_edge;
                union(subsets, x, y);
            }
            // Else discard the next_edge 
        }
    }

    public void print() {
        System.out.println("Following are the edges in " +  
                                     "the constructed MST");
        for (int i = 0; i < result.length; i ++) {
            System.out.println(result[i].src+" -- " +  
                   result[i].dest+" == " + result[i].weight);
        }
    }

    public void save() {
        String[][] data = new String[result.length][4];

        for (int i = 0; i < result.length; i ++) {
            data[i][0] = Integer.toString((int) result[i].srcSample);
            data[i][1] = Integer.toString((int) result[i].destSample);
            data[i][2] = Double.toString(result[i].weight);
            data[i][3] = "pp";
        }

        TableXY.write("Edges/edges.csv", 
            new String[]{"src", "dest", "weight", "interaction_type"}, data);
    }

    public void Test() {
        // setup test cases
        V = 4;
        E = 5;
        edge = new Edge[E];
        result = new Edge[V - 1]; // Tnis will store the resultant MST

        for (int i = 0; i < E; i ++) {
            edge[i] = new Edge();
        }

        edge[0].src = 0;
        edge[0].dest = 1;
        edge[0].weight = 10;
  
        // add edge 0-2 
        edge[1].src = 0;
        edge[1].dest = 2;
        edge[1].weight = 6;
  
        // add edge 0-3 
        edge[2].src = 0;
        edge[2].dest = 3;
        edge[2].weight = 5;
  
        // add edge 1-3 
        edge[3].src = 1;
        edge[3].dest = 3;
        edge[3].weight = 15;
  
        // add edge 2-3 
        edge[4].src = 2;
        edge[4].dest = 3;
        edge[4].weight = 4;

        kruskal();
        
        // assert that the result values are correct.
        assert result.length == 3 : "The size of the result should be no greater than 3";
        assert result[0].src == 2 && result[0].dest == 3 && result[0].weight == 4.0 : 
            "First element in path is incorrect";
        assert result[1].src == 0 && result[1].dest == 3 && result[1].weight == 5.0 : 
            "Second element in path is incorrect";
        assert result[2].src == 0 && result[2].dest == 1 && result[2].weight == 10.0 : 
            "Third element in path is incorrect";

        System.out.println("Graph test cases pass");
    }
}