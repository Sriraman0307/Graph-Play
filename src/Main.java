
import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {
		Graph G = new Graph();
		String fileLocation;
		if(args.length==0){
			System.out.println("Please enter valid file name");
			return;
		}
		fileLocation = args[0];
		if(fileLocation == null || fileLocation.equals("")){
			System.out.println("Please enter valid file name");
			return;
		}
		G.readGraph(fileLocation);

		//To Print out the graph uncomment the below line
		//G.printGraph();

		System.out.println("Is 11 Reachable from 7 ? ");
		System.out.println(G.isReachable(7,11));
		
		System.out.println("Shortest Path Edges between 7 and 11");
		System.out.println(G.findshortpath(7,11));
		
		System.out.println("Minimum Spanning Tree Edges");
		System.out.println(G.minSpanTree());
	}
}
