
public class Edge implements Comparable<Edge> {

	private String toCity;
	private int weight;
	
	public Edge(String v, int weight) {
		this.toCity=v;
		this.weight=weight;
	}

	@Override
	public int compareTo(Edge o) {
		return weight-o.weight;    
	}

	public String getToCity() {
		return toCity;
	}

	public int getWeight() {
		return weight;
	}

}
