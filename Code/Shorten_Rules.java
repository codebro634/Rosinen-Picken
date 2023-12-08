import java.util.ArrayList;
import java.util.HashSet;

public class Shorten_Rules {

	/*
	 * Iteriert den Graph unter Anwendung der Vereinfachungsregeln, solange bis
	 * keine Regel während einer gesamten Iteration mehr erfolgreich angewendet
	 * werden konnte. Gibt zurück, ob mindestens ein Knoten vereinfacht wurde.
	 */
	static boolean apply_rules(Graph g) {
		int initial_vertex_count = g.getVertices().size();
		// Liste der zu untersuchenden Knoten für die nächste Iteration.
		// Nach der ersten Iteration besteht diese nur noch aus Knoten, die
		// an Knoten angrenzten, auf welche erfolgreich eine Regel angewendet wurde
		HashSet<Vertex> iteration_list = new HashSet<Vertex>(g.getVertices());
		while (apply_rules_one_iteration(g, iteration_list))
			;
		return initial_vertex_count != g.getVertices().size();
	}

	/*
	 * Nur für GUI: Iteriert den Graphen genau einmal unter Anwendung der
	 * Vereinfachungsregeln
	 */
	static boolean apply_rules_step(Graph g) {
		return apply_rules_one_iteration(g, new HashSet<Vertex>(g.getVertices()));
	}

	/*
	 * Iteriert einen Graphen einmal und versucht auf jeden Knoten eine
	 * Vereinfachungsregel anzuwenden. Rückgabe ob mindestens eine Regel
	 * erfolgreich angewandt wurde.
	 */
	private static boolean apply_rules_one_iteration(Graph graph, HashSet<Vertex> iteration_list) {

		boolean change = false;

		// Liste in der alle Knoten gespeichert werden auf die die
		// Vereinfachungsregeln für die nächste Iteration angewendet werden sollen
		HashSet<Vertex> affectedVertices = new HashSet<Vertex>();

		for (Vertex vertex : iteration_list) {

			// Es ist möglich, dass ein zu iterierender Knoten nicht mehr im Graphen
			// existiert, da dieser eventuell schon durch eine
			// Regel vereinfacht wurde. Dieser Knoten kann deswegen auch nicht weiter
			// vereinfacht werden
			if (!graph.getVertices().contains(vertex))
				continue;

			/*
			 * Vereinfachungsregeln: Wurde eine Regel erfolgreich angewandt, wird
			 * markiert, dass eine Veränderung stattgefunden hat. Beim erfolgreichen
			 * Anwenden einer Regel auf einen Knoten, wird keine weitere Regel auf
			 * diesen angewendet, da dieser nicht mehr im Graphen existieren könnte
			 * (z.B. wurde gekauft). Alle Knoten bei denen eine Vereinfachungsregel
			 * anwendbar sein könnte dadurch, dass ein Knoten vereinfacht wurde,
			 * werden in einer Liste gespeichert.
			 */

			// Regel 1a anwenden
			if (applyRule1a(graph, vertex, affectedVertices)) {
				change = true;
				continue;
			}

			// Regel 1b anwenden
			if (applyRule1b(graph, vertex, affectedVertices)) {
				change = true;
				continue;
			}

			// Regel 2a anwenden
			if (applyRule2a(graph, vertex, affectedVertices)) {
				change = true;
				continue;
			}

			// Regel 2b anwenden
			if (applyRule2b(graph, vertex, affectedVertices)) {
				change = true;
				continue;
			}

		}

		iteration_list = new HashSet<Vertex>(affectedVertices);

		return change;

	}

	/*
	 * Regel 1a: Kaufen eines positiven Knoten mit keiner ausgehenden Kante
	 */
	private static boolean applyRule1a(Graph graph, Vertex vertex, HashSet<Vertex> affectedVertices) {
		// Überprüfung, ob Knoten positiv ist und keine ausgehenden Kanten besitzt
		if (vertex.getValue() >= 0 && vertex.getOutgoingEdges().size() == 0) {
			// Merken für nächste Iteration
			fillListWithConnectedVertices(graph, vertex, affectedVertices);
			// Kaufen und entfernen
			graph.buyVertex(vertex);
			Graph.statistics_rule_1a++;
			return true;
		}
		return false;
	}

	/*
	 * Regel 1b: Entfernen eines negativen Knoten mit keiner eingehenden Kante
	 */
	private static boolean applyRule1b(Graph graph, Vertex vertex, HashSet<Vertex> affectedVertices) {
		// Überprüfung, ob Knoten negativ ist und keine eingehenden Kanten besitzt
		if (vertex.getValue() <= 0 && vertex.getIncomingEdges().size() == 0) {
			// Merken für nächste Iteration
			fillListWithConnectedVertices(graph, vertex, affectedVertices);
			// Entfernen ohne kaufen
			graph.removeVertex(vertex);
			Graph.statistics_rule_1b++;
			return true;
		}
		return false;
	}

	/*
	 * Regel 2a: Zusammenfügen eines positiven Knotens mit nur einer ausgehenden
	 * Kante mit dem Zielknoten
	 */
	private static boolean applyRule2a(Graph graph, Vertex vertex, HashSet<Vertex> affectedVertices) {
		// Überprüfung, ob Knoten positiv ist und exakt eine ausgehende Kante
		// besitzt
		if (vertex.getValue() >= 0 && vertex.getOutgoingEdges().size() == 1) {
			// Merken für nächste Iteration
			fillListWithConnectedVertices(graph, vertex, affectedVertices);
			fillListWithConnectedVertices(graph, vertex.getOutgoingEdges().iterator().next().getTargetVertex(),
					affectedVertices);
			// Zusammenfügen
			ArrayList<Vertex> merge_list = new ArrayList<Vertex>();
			merge_list.add(vertex.getOutgoingEdges().iterator().next().getTargetVertex());
			graph.mergeVertices(vertex, merge_list);
			Graph.statistics_rule_2a++;
			return true;
		}
		return false;
	}

	/*
	 * Regel 2b: Zusammenfügen eines negativen Knotens mit nur einer eingehenden
	 * Kante mit dessen Quellknoten
	 */
	private static boolean applyRule2b(Graph graph, Vertex vertex, HashSet<Vertex> affectedVertices) {
		// Überprüfung, ob Knoten negativ ist und exakt eine eingehende Kante
		// besitzt
		if (vertex.getValue() <= 0 && vertex.getIncomingEdges().size() == 1) {
			// Merken für nächste Iteration
			fillListWithConnectedVertices(graph, vertex, affectedVertices);
			fillListWithConnectedVertices(graph, vertex.getIncomingEdges().iterator().next().getSourceVertex(),
					affectedVertices);
			// Zusammenfügen
			ArrayList<Vertex> merge_list = new ArrayList<Vertex>();
			merge_list.add(vertex.getIncomingEdges().iterator().next().getSourceVertex());
			graph.mergeVertices(vertex, merge_list);
			Graph.statistics_rule_2b++;
			return true;
		}
		return false;
	}

	/*
	 * Fügt einer Liste alle Knoten hinzu, die an einem Knoten angrenzen
	 */
	private static void fillListWithConnectedVertices(Graph graph, Vertex vertex, HashSet<Vertex> connected_list) {
		// Alle ausgehenden Knoten werden der Liste hinzugefügt
		for (Edge edge : vertex.getOutgoingEdges())
			connected_list.add(edge.getTargetVertex());
		// Alle eingehenden Knoten werden der Liste hinzugefügt
		for (Edge edge : vertex.getIncomingEdges())
			connected_list.add(edge.getSourceVertex());
	}
}
