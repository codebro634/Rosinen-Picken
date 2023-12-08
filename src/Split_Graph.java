
import java.util.HashSet;
import java.util.Stack;

public class Split_Graph {

	/*
	 * Ein Graph wird in einzelne zusammenhängende Graphen unterteilt.
	 */
	static Stack<Graph> splitGraph(Graph graph) {
		Stack<Graph> graphs = new Stack<Graph>();

		HashSet<Vertex> unexplored_vertices = new HashSet<Vertex>(graph.getVertices());

		while (unexplored_vertices.size() > 0) {
			Graph part_graph = new Graph();
			// Ein noch unerforschter Knoten wird ausgewählt
			Vertex source = unexplored_vertices.iterator().next();
			// Alle Knoten, die mit dem gewählten Knoten zusammehängen, werden
			// ermittelt
			HashSet<Vertex> connected_vertices = getConnectedVertices(source);
			// Aus allen gefundenen zusammenhängenden Knoten wird ein Teilgraph
			// gebildet
			for (Vertex vertex : connected_vertices) {
				unexplored_vertices.remove(vertex);
				part_graph.addVertex(vertex.getValue(), vertex.getNumber());
			}
			for (Vertex vertex : connected_vertices) {
				for (Edge edge : vertex.getOutgoingEdges())
					part_graph.addEdge(part_graph.getVertex(edge.getSourceVertex().getNumber()),
							part_graph.getVertex(edge.getTargetVertex().getNumber()));
			}

			Graph.statistics_split++;
			graphs.push(part_graph);
		}
		return graphs;
	}

	/*
	 * Liefert alle Knoten zurück, die mit einem Knoten zusammenhängen. Dies
	 * geschieht mittels Breitensuche
	 */
	private static HashSet<Vertex> getConnectedVertices(Vertex source) {
		HashSet<Vertex> visited = new HashSet<Vertex>();

		HashSet<Vertex> iteration_list = new HashSet<Vertex>();
		Stack<Vertex> in_queue = new Stack<Vertex>();

		iteration_list.add(source);
		visited.add(source);

		while (!iteration_list.isEmpty()) {

			for (Vertex vertex : iteration_list) {

				for (Edge edge : vertex.getOutgoingEdges()) {
					Vertex outgoing = edge.getTargetVertex();
					if (visited.contains(outgoing))
						continue;
					visited.add(outgoing);
					in_queue.push(outgoing);
				}

				for (Edge edge : vertex.getIncomingEdges()) {
					Vertex incoming = edge.getSourceVertex();
					if (visited.contains(incoming))
						continue;
					visited.add(incoming);
					in_queue.push(incoming);
				}

			}

			iteration_list.clear();
			iteration_list.addAll(in_queue);
			in_queue.clear();

		}

		return visited;

	}

}
