import java.io.*;
import java.util.*;


class Main{
	
	static void Ex1_1(ShortestPathTree tree){
		//Print out all possible positions after traveling 1h
		int t1 = 3600000;
		HashSet<Long> valid_points = new HashSet<Long>();
		for(Long v: tree.dist.keySet())
		{
			System.out.println("lol");
			Long u = tree.pred.get(v);
			//System.out.println("lol");
			System.out.println(tree.dist.get(v));
			if((tree.dist.get(v) > t1) && (tree.dist.get(u) < t1))
			{
				valid_points.add(u);
			}
		}
		System.out.println(valid_points);
	}

	public static void main(String[] args){
		String input_source = "/Users/Shepard/Desktop/Documents/Polytechnique/INF421/Project/RoadNetworks/data/idf.in";
		Graph net = new Graph(input_source); 
		System.out.println(net.Edges.size() + net.Vertices.size());
		//System.out.println(net.Vertices);
		//System.out.println("lol");
		//ShortestPathTree tree = new ShortestPathTree(net, net.Vertices.iterator().next());
//		System.out.println(tree.dist);
//		System.out.println(tree.pred);
		//Ex1_1(tree);

	}
}


class ShortestPathTree{
	HashMap<Long, Long> dist;
	HashMap<Long, Long> pred;
	//HashSet<Long> settledNodes;
	PriorityQueue<Long> unsettledNodes;
	long root;

	class Vertex_comp implements Comparator<Long>{
		public int compare(Long a,Long b){
			return dist.get(a) < dist.get(b) ? -1: 1;
		}
	}
	
	
	ShortestPathTree(Graph graph,Long root)
	{
		this.root = root; 
		//this.Vertices = new ArrayList<Vertex>(graph.Vertices);
		//this.Edges = new ArrayList<Edge>(graph.Edges);
		
		dist = new HashMap<Long, Long>();
		pred = new HashMap<Long, Long>();
		Vertex_comp vc = new Vertex_comp();
		unsettledNodes = new PriorityQueue<Long>(1, vc);
		int nulls = 0;
		//settledNodes = new HashSet<Long>();
		//Neigbours !!!
		//settledNodes.add(root);
		dist.put(root, new Long(0));
		final long inf = Integer.MAX_VALUE;
		
		for( Long v : graph.Vertices){
			if(!v.equals(root))
			{
				dist.put(v, inf);
				pred.put(v, null);
			}
		}
		//System.out.println(root);
		//System.out.println(graph.Neighbours.get(root));
		
		unsettledNodes.add(root);
		while(!unsettledNodes.isEmpty())
		{
//			System.out.println("lol");
			 Long u = unsettledNodes.poll();
			 //System.out.println(u);

			 //System.out.println(graph.Neighbours.get(u));
			 if(graph.Neighbours.containsKey(u))
			 {
				 for (Long v : graph.Neighbours.get(u))
				 {
					 String key = u.toString() + ' '+ v.toString();
					 long alt = dist.get(u) + graph.Edges.get(key);
					 if(alt < dist.get(v))
					 {
							 dist.replace(v, alt);
							 pred.replace(v, u);
						 
						 if(!unsettledNodes.contains(v))
							 unsettledNodes.add(v);
					 } 
				 }
			 }
		}
		for(Long v : graph.Vertices)
			if(pred.get(v) == null)
				nulls++;
		System.out.println(nulls);
	}
}

class Graph{
	HashSet<Long> Vertices; // Here we're gonna stock the id's of the Vertices (not to carry around the coordinates)
	HashMap<String, Long> Edges; 
	HashMap<Long, ArrayList<Integer>> Coordinates; // Coordinates corresponding to the id's of the Vertices (to visualize the afterwards)
	HashMap<Long, ArrayList<Long>> Neighbours; // Array of adjacent vertices corresponding to each vertex 
	//Graph(){} 
	Graph(String input_source)
	{
		Vertices = new HashSet<Long>();
		Edges = new HashMap<String, Long>();
		Neighbours = new HashMap<Long, ArrayList<Long>>();
		Coordinates = new HashMap<Long, ArrayList<Integer>>();
		
		//int edge_id = 0; // id of the edge just in case
		String thisLine;
		String[] bt;		
		try{
	         BufferedReader br = new BufferedReader(new FileReader(input_source));
	         while ((thisLine = br.readLine()) != null) {
	        	 bt = thisLine.split(" ");
	        	 if(bt[0].equals("v")) // found a Vertex
	        	 {
	        		 ArrayList<Integer> tmp = new ArrayList<Integer>();
	        		 tmp.add(Integer.parseInt(bt[2]));
	        		 tmp.add(Integer.parseInt(bt[3]));
	        		 Vertices.add(Long.parseLong(bt[1])); // add it to the set of vertices
	        		 Coordinates.put(Long.parseLong(bt[1]), tmp);
	             }
	        	 else if(bt[0].equals("a"))
	        	 {
	        		 Edges.put((bt[1] + ' ' +bt[2]), Long.parseLong(bt[3]));
	        		 if(Neighbours.containsKey((Long.parseLong(bt[1]))))
	        		 {
	        			 Neighbours.get(Long.parseLong(bt[1])).add(Long.parseLong(bt[2]));
	        		 }
	        		 else
	        		 {
	        			 ArrayList<Long> tmp = new ArrayList<Long>();
	        			 tmp.add(Long.parseLong(bt[2]));
	        			 Neighbours.put(Long.parseLong(bt[1]), tmp);
	        		 }
	        	 }
	         }
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}