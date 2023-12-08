import java.util.Stack;

public class Solve_Graph {

	/*
	 * Kombiniert alle implementierten Verfahren wie in der Doku genannten
	 * Reihenfolge, um die maximale Teilmenge zu bestimmen. Die Lösung wird
	 * ausgegeben.
	 */
	public static void buyOptimalClosure(Graph graph, boolean update_gui_during_algorithm) {
		long start_time = System.currentTimeMillis();
		if (update_gui_during_algorithm)
			GUI_Frame.status.setText("Einfache Vereinfachungsregeln werden angewendet.");
		// Vereinfachungsregeln werden einmal angewendet um Anzahl der zu
		// erstellenden Teilgraphen zu verringern
		Shorten_Rules.apply_rules(graph);
		if (update_gui_during_algorithm)
			GUI_Frame.status.setText("Zusammenhängende Graphen\n werden ermittelt.");
		// Der Graph wird in einzelne zusammenhängende Graphen unterteilt
		Stack<Graph> split_graphs = Split_Graph.splitGraph(graph);
		if (update_gui_during_algorithm)
			GUI_Frame.status.setText("Graphen werden abgearbeitet.\nAnzahl: " + split_graphs.size());
		for (Graph split_graph : split_graphs) {
			// Alle Zyklen werden aus den Teilgraphen entfernt
			Remove_Cycles.removeAllCycles(split_graph);
			// Vereinfachungsregeln und redundante Kante abwechselnd auf Teilgraphen
			// anwenden
			while (true) {
				// Alle Vereinfachungsregeln solange auf Teilgraphen anwenden bis kein
				// Knoten mehr entfernt wird
				Shorten_Rules.apply_rules(split_graph);
				// Alle redundanten Kanten werden aus dem verbleibenden Teilgraph
				// entfernt
				// Wenn mindestens eine Kante entfernt wurde, wird wieder versucht die
				// Vereinfachungsregeln anzuwenden
				// andernfalls wird die Schleife beendet
				if (!Remove_Redundant_Edges.removeRedundantEdges(split_graph))
					break;
			}
			// Die optimale Teilmenge des verbleibenden Teilgraphen wird ermittelt
			MinCut_MaxFlow.derive_max_closure(split_graph);
			// Alle zu kaufenden Knoten des Teilgraphen werden auch im Originalgraphen
			// gekauft
			for (Integer to_buy : split_graph.getBoughtVertices())
				graph.buyVertex(graph.getVertex(to_buy));
		}
		System.out.println("Laufzeit: " + (System.currentTimeMillis() - start_time) + "ms");
		// Lösungsausgabe
		Export_Solution.printSolution(graph);
	}

}
