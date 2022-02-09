import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;

public abstract class GraphOfCities {
	//Two graphs for the part 1 and part 2.
	static Map<String, LinkedList<Edge>> leftGraph = new HashMap<String, LinkedList<Edge>>();   //graph of part 1. City is mapped to list of adjacent cities.
	static Map<String, LinkedList<Edge>> rightGraph = new HashMap<String, LinkedList<Edge>>();  //graph of part 2(honeymoon). City is mapped to list of adjacent cities.
	
	/**
	 * creates city in the graph of part 1 if it doesn't exist.
	 * @param city
	 */
	public static void addLeftCity(String city) {
		if(!leftGraph.containsKey(city)) {
			leftGraph.put(city, new LinkedList<Edge>());
		}	
	}
	/**
	 * creates city in the graph of part 2 if it doesn't exist.
	 * @param city
	 */
	public static void addRightCity(String city) {
		if(!rightGraph.containsKey(city)) {                
			rightGraph.put(city, new LinkedList<Edge>());
		}
	}
	/**
	 * for part 1, adds adjacent city with weight to the adjacent city list of city1.(directed graph)
	 * @param city1
	 * @param city2
	 * @param weight
	 */
	public static void addEdgeToLeft(String city1,String city2, int weight) {
		leftGraph.get(city1).add(new Edge(city2,weight));
	}
	/**
	 * for part 2, adds adjacent city with weight to the adjacent city list of both cities.(undirected graph)
	 * @param city1
	 * @param city2
	 * @param weight
	 */
	public static void addEdgeToRight(String city1,String city2, int weight) {
		rightGraph.get(city1).add(new Edge(city2,weight));
		rightGraph.get(city2).add(new Edge(city1,weight));
	}
	/**
	 * finding shortest distance and path from cityOfMecnun to cityOfLeyla
	 * @param cityOfMecnun
	 * @param cityOfLeyla
	 * @param timeOfFather
	 * @return  path from Mecnun to Leyla if it exists and return value of honeymoon() if Mecnun arrives Leyla in time.
	 */
	public static String shortestDistanceToLeyla(String cityOfMecnun, String cityOfLeyla, int timeOfFather) {
		Map<String,Integer> distance = new HashMap<String,Integer>(leftGraph.size()); // City is mapped to shortest distance to it.
		Map<String, String> parent = new HashMap<String, String>();  // City is mapped to parent city. 
		for(String city: leftGraph.keySet()) {
			distance.put(city, Integer.MAX_VALUE);  // Distance of all cities to source is like infinity at the beginning.
			parent.put(city, "");
		}
		distance.replace(cityOfMecnun, 0);
		parent.replace(cityOfMecnun, "-1");  // Source is cityOfMecnun. Therefore, parent of it is -1.
		PriorityQueue<Edge> queue = new PriorityQueue<>();
		queue.add(new Edge(cityOfMecnun, 0));  //Source is cityOfMecnun. Therefore, distance from source to cityOfMecnun is 0.
		
		while(!queue.isEmpty()) {
            Edge e = queue.poll();
            for (Edge e2 :leftGraph.get(e.getToCity())) {
                if (distance.get(e.getToCity())+ e2.getWeight() < distance.get(e2.getToCity())) {
                	distance.replace(e2.getToCity(), distance.get(e.getToCity())+ e2.getWeight());
                	parent.replace(e2.getToCity(), e.getToCity());
                    queue.add(new Edge(e2.getToCity(),distance.get(e2.getToCity())));
                }
            }
        }
		if(distance.get(cityOfLeyla)==Integer.MAX_VALUE) {  //Mecnun doesn't reach Leyla because cityOfLeyla is not connected.
			return "-1\n-1";
		}
		else if(distance.get(cityOfLeyla)>timeOfFather) {   //Mecnun reaches Leyla but doesn't marry because of time limit.
			String path = cityOfLeyla;
			String i = cityOfLeyla;
			while(!i.equals(cityOfMecnun)) {
				path = parent.get(i) + " " +path ;
				i=parent.get(i);
			}
			return path+"\n-1";  
		}
		else {     //Mecnun reaches and marries Leyla, honeymoon()
			String path = cityOfLeyla;
			String i = cityOfLeyla;
			while(!i.equals(cityOfMecnun)) {
				path = parent.get(i) + " " +path ;
				i=parent.get(i);
			}
			return path+"\n"+honeymoon(cityOfLeyla);   
		}
	}
	
	private static Map<String, Integer> cityWithMinWeight = new HashMap<String,Integer>(); // City is mapped to minimum weight of edge connected to. 
	private static Map<String, Boolean> cityInMST = new HashMap<String,Boolean>();  // If city is set to minimum spanning tree, it is mapped to true.
	/**
	 * finding sum of weight of edges in minimum spanning tree of part 2
	 * @param cityOfLeyla
	 * @return if no honeymoon -2, otherwise tax paid by Leyla and Mecnun 
	 */
	public static int honeymoon(String cityOfLeyla) {
		int taxPaidPerOne=0;		
		for(String city:rightGraph.keySet()) {
			cityWithMinWeight.put(city, Integer.MAX_VALUE);
			cityInMST.put(city, false);
		}
		cityWithMinWeight.replace(cityOfLeyla, 0);
		PriorityQueue<Edge> cities = new PriorityQueue<Edge>();  
		cities.add(new Edge(cityOfLeyla,0));
		
		while(!cities.isEmpty()) {
			Edge e = cities.poll();
			if(!cityInMST.get(e.getToCity())) {
				cityInMST.replace(e.getToCity(), true);
				for(Edge e2 :rightGraph.get(e.getToCity())){
					if(e2.getWeight()<cityWithMinWeight.get(e2.getToCity()) && !cityInMST.get(e2.getToCity())){
						cityWithMinWeight.replace(e2.getToCity(), e2.getWeight());
						cities.add(e2);
					}
				}
			}
		}
		if(cityWithMinWeight.values().contains(Integer.MAX_VALUE)) { // no honeymoon because there is at least one city not connected.
			return -2;
		}
		else {   //honeymoon
			for(int w:cityWithMinWeight.values()) {
				taxPaidPerOne+=w;
			}
			return taxPaidPerOne*2;
		}
	}	
}
