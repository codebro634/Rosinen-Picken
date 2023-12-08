import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Remove_Redundant_Edges {

	/*
	 * Entfernt alle redundanten Kanten des Graphen. Es wird zurückgeliefert, ob
	 * mindestens eine redundante Kante entfernt wurde.
	 */
	static boolean removeRedundantEdges(Graph graph) {
		boolean removed_edge = false;
		// Alle Kanten werden iteriert
		for (Edge edge : new ArrayList<Edge>(graph.getEdges())) {
			// Liefert die Methode isEdgeRedundant wahr, dann ist die Kante redundant
			// und wird entfernt
			if (isEdgeRedundant(edge)) {
				graph.removeEdge(edge);
				Graph.statistics_edges++;
				removed_edge = true;
			}
		}
		return removed_edge;
	}

	/*
	 * Nur für GUI: Entfernt genau eine redundante Kante aus dem Graph.
	 */
	static boolean removeRedundantEdgesStep(Graph graph) {
		for (Edge edge : new ArrayList<Edge>(graph.getEdges())) {
			if (isEdgeRedundant(edge)) {
				graph.removeEdge(edge);
				return true;
			}
		}
		return false;
	}

	/*
	 * Liefert ob eine Kante redundant ist. Es wird der Quellknoten der Kante
	 * untersucht. Von hier wird eine Breitensuche über alle anderen ausgehenden
	 * Kanten durchgeführt und geschaut, ob der Zielknoten der zu untersuchenden
	 * Kante erreicht wird.
	 */
	private static boolean isEdgeRedundant(Edge suspected_edge) {
		HashSet<Vertex> visited_vertices = new HashSet<Vertex>();
		HashSet<Vertex> iteration_list = new HashSet<Vertex>();
		Stack<Vertex> in_queue = new Stack<Vertex>();

		visited_vertices.add(suspected_edge.getSourceVertex());
		// Festelegung der Startknoten der Breitensuche
		for (Edge edge : suspected_edge.getSourceVertex().getOutgoingEdges()) {
			if (edge == suspected_edge)
				continue; // Die zu untersuchende Kante wird ausgelassen
			iteration_list.add(edge.getTargetVertex());
		}

		/*
		 * Breitensuche solange bis entweder keine neuen Knoten mehr gefunden werden
		 * oder bis der Zielknoten der zu untersuchenden Kante gefunden wird
		 */
		while (!iteration_list.isEmpty()) {
			for (Vertex vertex : iteration_list) {
				if (visited_vertices.contains(vertex))
					continue;
				visited_vertices.add(vertex);
				for (Edge edge : vertex.getOutgoingEdges()) {
					// Ist der gefundene Knoten der Zielknoten der zu untersuchenden
					// Kante, ist die zu untersuchende Kante redundant
					if (edge.getTargetVertex() == suspected_edge.getTargetVertex())
						return true;
					in_queue.push(edge.getTargetVertex());
				}
			}
			iteration_list.clear();
			iteration_list.addAll(in_queue);
			in_queue.clear();
		}
		// Hier wurde nicht der Zielknoten der zu untersuchenden Kante nicht
		// gefunden
		// Die Kante ist daher nicht redundant
		return false;
	}

}
