

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.io.*;

/**
 * This is the Graph utility class.
 * @author Prachi
 */
public class Graph {
	private HashMap<Integer, List<Integer>> myAdjList;
	private HashMap<List<Integer>,Integer> myWeightList;
	private HashMap<Integer, Boolean> visited;
	private ArrayList<Integer> shortestPath;
	private ArrayList<Edge> edges;
	private ArrayList<Edge> finalPath;
	private int myNumVertices;
	private int myNumEdges;

	/**
	 * Construct empty Graph
	 */
	public Graph() {
		myAdjList = new HashMap<Integer, List<Integer>>();
		visited = new HashMap<Integer, Boolean>();
		edges = new ArrayList<Edge>();
		myWeightList = new HashMap<List<Integer>,Integer>();
		finalPath = new ArrayList<Edge>();
		shortestPath = new ArrayList<Integer>();
		myNumVertices = myNumEdges = 0;
	}

	public HashMap<Integer, List<Integer>> getMyAdjList() {
		return myAdjList;
	}

	private boolean hasVertex(int node) {
		return myAdjList.containsKey(node);
	}

	private boolean hasEdge(int from, int to) {
		if (!hasVertex(from) || !hasVertex(to))
			return false;
		return myAdjList.get(from).contains(to);
	}

	private void addEdge(int from, int to, int weight) {
		if (hasEdge(from, to))
			return;
		myNumEdges += 1;

		if (!hasVertex(from)){
			myAdjList.put(from, new ArrayList<Integer>());
			myNumVertices++;
		}

		if(!hasVertex(to)){
			myAdjList.put(to, new ArrayList<Integer>());
			myNumVertices++;
		}

		myAdjList.get(from).add(to);
		myAdjList.get(to).add(from);
		ArrayList<Integer> vertex = new ArrayList<Integer>();
		vertex.add(to);
		vertex.add(from);
		myWeightList.put(vertex, weight);
		Edge obj = new Edge(from, to, weight);
		edges.add(obj);
	}

	private class EdgeWeightComparator implements Comparator<Edge> {
		public int compare(Edge egd1, Edge edg2) {
			return egd1.getWeight() - edg2.getWeight();
		}
	}

	/**
	 * Creates a minimum spanning tree of the connected undirected graph with
	 * minimum weight. It first sorts all the edges and forms a minimum spanning
	 * tree containing all the vertices
	 * @return It returns ArrayList of edges in minimum spanning tree
	 */
	public ArrayList<Edge> minSpanTree(){
		ArrayList<Integer> table = new ArrayList<Integer>(myAdjList.keySet());
		Collections.sort(edges,new EdgeWeightComparator());
		int cost = 0;
		executeSpanTree(edges.get(0).getFrom(),new ArrayList<Integer>(),table,cost);
		return finalPath;
	}

	private void executeSpanTree(int from, ArrayList<Integer> fromArray, ArrayList<Integer> unSettlednodes, int cost){
		if(fromArray.isEmpty()){
			fromArray.add(from);
			unSettlednodes.remove((Object)from);
		}
		for(int i = 0; i<edges.size();i++){
			int newfrom = edges.get(i).getFrom();
			int newto = edges.get(i).getTo();

			if(fromArray.contains(newfrom)&&fromArray.contains(newto)){
				continue;
			}

			if(fromArray.contains(newfrom) && (!fromArray.contains(newto))){
				Edge edge = new Edge(newfrom, newto);
				edge = getEdgeWeight(edge);
				finalPath.add(edge);
				cost = cost + edges.get(i).getWeight();
				fromArray.add(newto);
				unSettlednodes.remove((Object)newto);
			}
			else if((!fromArray.contains(newfrom))&&fromArray.contains(newto)){
				Edge edge = new Edge(newfrom, newto);
				edge = getEdgeWeight(edge);
				finalPath.add(edge);
				cost = cost + edges.get(i).getWeight();
				fromArray.add(newfrom);
				unSettlednodes.remove((Object)newfrom);
			}
			else{
				ArrayList<Edge> minCostPath = new ArrayList<Edge>();
				for(int k = 0;k<fromArray.size();k++){
					ArrayList<Edge> edgeList1 =  findshortpath(fromArray.get(k), newfrom);
					ArrayList<Edge> edgeList2 = findshortpath(fromArray.get(k), newto);
					if(minCostPath.isEmpty()){
						if(findTotalCostOfEdges(edgeList1) > findTotalCostOfEdges(edgeList2)){
							minCostPath.addAll(edgeList2);
						}else{
							minCostPath.addAll(edgeList1);
						}
					}else{
						if(findTotalCostOfEdges(minCostPath) >findTotalCostOfEdges(edgeList1)){
							minCostPath.clear();
							minCostPath.addAll(edgeList1);
						}
						
						if(findTotalCostOfEdges(minCostPath) >findTotalCostOfEdges(edgeList2)){
							minCostPath.clear();
							minCostPath.addAll(edgeList2);
						}
					}
				}

				for(Edge minCostEdge : minCostPath){
					if(!fromArray.contains(minCostEdge.getFrom())){
						fromArray.add(minCostEdge.getFrom());
						unSettlednodes.remove((Object)minCostEdge.getFrom());						
					}
					if(!fromArray.contains(minCostEdge.getTo())){
						fromArray.add(minCostEdge.getTo());
						unSettlednodes.remove((Object)minCostEdge.getTo());						
					}
				}
				finalPath.addAll(minCostPath);
			}

			if(unSettlednodes.isEmpty()){
				break;
			}
		}
		if(!unSettlednodes.isEmpty()){
			executeSpanTree(fromArray.get(0), fromArray, unSettlednodes,cost);
		}
	}

	private Edge getEdgeWeight(Edge edge) {
		ArrayList<Integer> pair = new ArrayList<Integer>();
		pair.add(edge.getFrom());
		pair.add(edge.getTo());
		ArrayList<Integer> secPair = new ArrayList<Integer>();
		secPair.add(edge.getTo());
		secPair.add(edge.getFrom());
		if(myWeightList.containsKey(pair)||myWeightList.containsKey(secPair)){
			int edgeCost = 0;
			if(myWeightList.get(pair) == null){
				edgeCost = myWeightList.get(secPair);
			}
			else{
				edgeCost = myWeightList.get(pair);						
			}
			edge.setWeight(edgeCost);
		}
		return edge;
	}

	/**
	 * To find shortest path between two vertex
	 * @param startingVertex
	 * @param endingVertex
	 * @return If path exists function returns ArrayList otherwise null if no path exists, 
	 */

	public ArrayList<Edge> findshortpath(int startingVertex, int endingVertex){
		findAllPaths(startingVertex , endingVertex ,new ArrayList<Integer>());
		ArrayList<Edge> minCostPath = converttoArrayofEdges(shortestPath);
		shortestPath.clear();
		return minCostPath;
	}

	@SuppressWarnings("unchecked")
	private void findAllPaths(int from , int to , ArrayList<Integer> fromArray){
		if(fromArray.contains(from)){
			fromArray.clear();
			return;
		}
		fromArray.add(from);
		if(from == to){
			if(shortestPath.isEmpty()){
				shortestPath.addAll(fromArray);
			}else if(findCostofPath(fromArray) < findCostofPath(shortestPath)){
				shortestPath.clear();
				shortestPath.addAll(fromArray);
			}
			return;
		}
		for(int i = 0; i<myAdjList.get(from).size();i++){
			ArrayList<Integer> farrayclone = (ArrayList<Integer>) fromArray.clone();
			findAllPaths(myAdjList.get(from).get(i), to, farrayclone);
		}
	}
	
	private ArrayList<Edge> converttoArrayofEdges(ArrayList<Integer> paths){
		ArrayList<Edge> edgeList = new ArrayList<Edge>();
		if(paths.size() > 1){
			for(int i=1 ; i < paths.size() ; i++){
				Edge edge = new Edge (paths.get(i-1), paths.get(i));
				edge = getEdgeWeight(edge);
				edgeList.add(edge);
			}
		}
		return edgeList;
	}

	private int findCostofPath(ArrayList<Integer> path) {
		int costOfPath = 0;
		for(int i=1 ; i < path.size() ; i++){
			Edge edge = new Edge (path.get(i-1), path.get(i));
			edge = getEdgeWeight(edge);
			costOfPath = costOfPath + edge.getWeight();
		}
		return costOfPath;
	}
	
	private int findTotalCostOfEdges(ArrayList<Edge> path) {
		int costOfPath = 0;
		for(int i=1 ; i < path.size() ; i++){
			Edge edge = path.get(i);
			costOfPath = costOfPath + edge.getWeight();
		}
		return costOfPath;
	}

	/**
	 * Given two vertices it checks if the vertices are reachable
	 * @param startingVertex 
	 * @param endingVertex vertex 
	 * @return
	 */
	public boolean isReachable(int startingVertex, int endingVertex)
	{
		if(!hasVertex(startingVertex) || !hasVertex(endingVertex)){
			return false;
		}

		for(int i = 0; i<myAdjList.get(startingVertex).size();i++){
			visited.put(startingVertex, true);
			if(myAdjList.get(startingVertex).get(i)== endingVertex){
				visited.clear();
				return true;
			}			
		}

		for(int i = 0; i<myAdjList.get(startingVertex).size();i++){
			int newfrom = myAdjList.get(startingVertex).get(i);
			if(visited.get(newfrom) == null){
				visited.put(newfrom, false);
			}
			if(!(visited.get(newfrom))){
				if(isReachable(newfrom, endingVertex)){
					return true;
				}
			}
		}
		visited.clear();
		return false;
	}

	public void printGraph() {
		// print out graph again by iterating over vertices and edges
		HashMap<Integer, List<Integer>> adjList = getMyAdjList();
		Iterator<Entry<Integer,List<Integer>>> iter = 
				adjList.entrySet().iterator();

		while(iter.hasNext()){
			Map.Entry<Integer, List<Integer>> entry = iter.next();

			System.out.println( "Vertex : " + entry.getKey());

			List<Integer> list = entry.getValue();
			for(Integer node: list){
				System.out.print( " " + node);
			}
			System.out.println();
		}
	}

	/**
	 * The function reads the file from the given file location and creates a graph
	 * @param fileLocation - Location of the file containing the graph
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void readGraph(String fileLocation)
			throws FileNotFoundException, IOException {
		FileReader file = new FileReader(fileLocation);
		BufferedReader reader = new BufferedReader(file);

		String str;
		while(((str = reader.readLine())!=null))
		{
			if(!str.contains("%"))
			{
				int weight = 1;
				String[] vertices = str.split(" ");
				int vertex_1 = Integer.parseInt(vertices[0]) ;
				int vertex_2 = Integer.parseInt(vertices[1]) ;
				if(vertices.length>2){
					weight = Integer.parseInt(vertices[2]);
				}
				addEdge(vertex_1,vertex_2,weight);
			}
		}
		reader.close();
	}


}
