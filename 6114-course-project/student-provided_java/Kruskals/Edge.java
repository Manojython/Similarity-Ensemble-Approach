package Kruskals;

import java.lang.Comparable;

// Utilzied to represent a Graph edge 
public class Edge implements Comparable<Edge> {
    public int src, dest;
    public int srcSample, destSample;
    public double weight;

    // Comparator function used for sorting edges  
    // based on their weight 
    public int compareTo(Edge compareEdge) {
    	double difference = this.weight-compareEdge.weight;
    	
    	if (difference > 0) {
    		return 1;
    	} else if (difference < 0) {
    		return -1;
    	} else {
    		return 0;
    	}
    } 
}; 