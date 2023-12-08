import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

public class MinCut_MaxFlow {

	/*
	 * Die optimale Teilmenge wird in einem Graphen bestimmt.
	 */
	static void derive_max_closure(Graph graph) {
		// �berf�hrung von Closure-Problem zu Max-Flow-Problem
		Vertex[] source_and_target = closure_problem_to_max_flow(graph);
		// Max-Flow Problem l�sen
		get_residual_graph(graph, source_and_target[0], source_and_target[1]);
		// Knoten auf Seite des Quellknotens nach minimalen Schnitt finden und
		// kaufen
		buyClosure(graph, source_and_target[0]);
		// Die hinzugef�gten Knoten, die nur n�tig waren um die Aufgabe zu l�sen,
		// werden wieder aus dem Graph entfernt
		graph.removeVertex(source_and_target[0]);
		graph.removeVertex(source_and_target[1]);
	}

	/*
	 * �berf�hrung in ein Max-Flow Problem. Die hinzuf�gten Knoten, werden
	 * zur�ckgeliefert.
	 */
	private static Vertex[] closure_problem_to_max_flow(Graph graph) {
		ArrayList<Vertex> old_list = new ArrayList<Vertex>(graph.getVertices());
		// Quellknoten wird hinzugef�gt
		Vertex source = graph.addVertex(0);
		// Zielknoten wird hinzugef�gt
		Vertex target = graph.addVertex(0);
		for (Vertex vertex : old_list) {

			// Kapazit�t aller urspr�nglichen Kanten des Graphen wird auf unendlich
			// gesetzt
			for (Edge edge : vertex.getIncomingEdges())
				edge.setCapacityInfinity();
			for (Edge edge : vertex.getOutgoingEdges())
				edge.setCapacityInfinity();

			float vertex_abs_value = Math.abs(vertex.getValue());
			// Alle positiven Knoten erhalten eine Kante, mit der Kapazit�t gleich dem
			// Knotengewicht, kommend von dem Quellknoten
			if (vertex.getValue() >= 0)
				graph.addEdge(source, vertex, vertex_abs_value, false, true);
			// Alle negativen Knoten erhalten eine Kante, mit Kapazit�t gleich dem
			// Absolutwert des Knotengewichtes, gerichtet zum Zielknoten
			else
				graph.addEdge(vertex, target, vertex_abs_value, false, true);

		}
	// Der Quell- und Zielknoten wird zur�ckgegeben
		return new Vertex[] { source, target }; 
																						
	}

	/*
	 * Anwendung des Edmond-Karp Algorithmus, um Residualgraph zu bilden.
	 */
	private static void get_residual_graph(Graph graph, Vertex source, Vertex target) {

		// Erzeugen von Gegenkanten mit der Initialkapazit�t 0
		for (Edge edge : new ArrayList<Edge>(graph.getEdges())) {
			graph.addEdge(edge.getTargetVertex(), edge.getSourceVertex(), 0, false, true);
		}

		// Es wird ein k�rzester m�glicher Weg vom Quellknoten zum Zielknoten
		// gesucht
		ArrayList<Vertex> shortest_path = new ArrayList<Vertex>(shortest_path(source, target));
		// Ist die Liste des k�rzesten Weges leer, dann existiert kein erlaubter Weg
		// mehr und der Algorithmus terminiert
		while (!shortest_path.isEmpty()) {

			float min_capacity = Integer.MAX_VALUE;

			// Es wird die geringste Kapazit�t aller Kanten des gefundenen k�rzesten
			// Weges ermittelt
			for (int i = 0; i < shortest_path.size() - 1; i++) {
				Vertex edge_source = shortest_path.get(i);
				Vertex edge_target = shortest_path.get(i + 1);
				Edge path_edge = edge_source.getOutgoingEdge(edge_target);
				if (!path_edge.isCapacityInfinite() && path_edge.getCapacity() < min_capacity)
					min_capacity = path_edge.getCapacity();
			}

			// Die geringste Kapazit�t wird der Kapazit�t aller Kanten des Weges
			// subtrahiert und zu den jeweiligen Gegenkanten addiert
			for (int i = 0; i < shortest_path.size() - 1; i++) {

				Vertex edge_source = shortest_path.get(i);
				Vertex edge_target = shortest_path.get(i + 1);
				Edge out = edge_source.getOutgoingEdge(edge_target);

				edge_source = shortest_path.get(i + 1);
				edge_target = shortest_path.get(i);
				Edge invers = edge_source.getOutgoingEdge(edge_target);

				if (!out.isCapacityInfinite())
					out.setCapacity(out.getCapacity() - min_capacity);
				if (!invers.isCapacityInfinite())
					invers.setCapacity(invers.getCapacity() + min_capacity);
			}

			// Ein k�rzester Weg wird nach der �nderung der Kapazit�ten erneut
			// bestimmt
			shortest_path = new ArrayList<Vertex>(shortest_path(source, target));
		}
		/*
		 * Ist das Abbruchskriterium der while-Schleife erf�llt, existiert kein
		 * m�glicher Weg mehr von Quell- zum Zielknoten. Damit wurde der
		 * Residualgraph vollst�ndig erstellt.
		 */
	}

	/*
	 * Es wird eine Liste von Knoten gebildet, die vom Quellknoten des
	 * Residualgraph aus erreichbar sind.
	 */
	private static HashSet<Vertex> getVerticesByMinCut(Vertex source) {
		// Speichert alle erreichbaren Knoten
		HashSet<Vertex> visited_vertices = new HashSet<Vertex>();
		Stack<Vertex> next_vertices = new Stack<Vertex>();
		ArrayList<Vertex> iteration_list = new ArrayList<Vertex>();
		iteration_list.add(source);
		visited_vertices.add(source);

		/*
		 * Eine Breitensuche wird ausgef�hrt. Es werden jedoch Knoten nur �ber
		 * Kanten besucht, die eine Restkapazit�t von > 0 besitzen.
		 */
		while (!iteration_list.isEmpty()) {

			for (Vertex vertex : iteration_list) {
				for (Edge edge : vertex.getOutgoingEdges()) {
					if (visited_vertices.contains(edge.getTargetVertex()))
						continue;
					if (!(edge.isCapacityInfinite() || edge.getCapacity() > 0))
						continue;
					next_vertices.push(edge.getTargetVertex());
					visited_vertices.add(edge.getTargetVertex());
				}
			}

			iteration_list.clear();
			iteration_list.addAll(next_vertices);
			next_vertices.clear();
		}
		// Da Quellknoten nicht im urspr�nglichen Graphen existiert, kann dieser
		// auch nicht gekauft werden
		visited_vertices.remove(source);
		return visited_vertices;

	}

	/*
	 * Kauft alle Knoten, die sich in der Liste befinden, die �ber die Methode
	 * getVeritcesByMinCut bestimmt werden.
	 */
	private static void buyClosure(Graph graph, Vertex source) {
		for (Vertex vertex : new ArrayList<Vertex>(getVerticesByMinCut(source)))
			graph.buyVertex(vertex);
	}

	/*
	 * Findet einen k�rzesten Weg zwischen zwei Knoten. Falls der Startknoten
	 * gleich dem Zielknoten ist, wird eine leere Liste z�ckgegeben. Wurde kein
	 * k�rzester Weg gefunden, wird ebenfalls eine leere Liste zur�ckgegeben. Beim
	 * Finden eines Weges wird eine Liste, welche die Knoten des gelaufenen Weges
	 * enth�lt, zur�ckgegeben.
	 */
	private static Stack<Vertex> shortest_path(Vertex source, Vertex target) {
		Stack<Vertex> path = new Stack<Vertex>();
		if (source == target)
			return path;
		// Aufrufliste, die �ber die Breitensuche startend vom Quellknoten, entsteht
		HashMap<Vertex, Vertex> parentMapSource = new HashMap<Vertex, Vertex>();
		// Aufrufliste, die �ber die Breitensuche startend vom Zielknoten, entsteht
		HashMap<Vertex, Vertex> parentMapTarget = new HashMap<Vertex, Vertex>();
		// Breitensuchen startend vom Quell- und Zielknoten werden ausgef�hrt. Der
		// Knoten, wo sich die Suchen treffen, wird erhalten
		Vertex bfs_meetup = bfs(source, target, parentMapSource, parentMapTarget);
		// Haben sich die beiden Breitensuchen nicht getroffen, existiert kein Weg,
		// was dazu f�hrt, dass eine leere Liste zur�ckgegeben wird
		if (bfs_meetup == null)
			return path;

		// �ber Backtracking wird der Weg vom Quellknoten zum Treffknoten bestimmt
		Vertex child = parentMapSource.get(bfs_meetup);
		path.add(bfs_meetup);
		while (child != null) {
			path.add(0, child);
			child = parentMapSource.get(child);
		}

		// Der Weg wird durch die Knoten vom Treffknoten zum Zielknoten erg�nzt
		child = parentMapTarget.get(bfs_meetup);
		while (child != null) {
			path.push(child);
			child = parentMapTarget.get(child);
		}

		return path;
	}

	/*
	 * Methode findet mit einer bidirektionalen Breitensuche den k�rzesten Weg
	 * zwischen zwei Knoten. Die Methode gibt den Knoten zur�ck an welchem sich
	 * die beiden Breitensuche getroffen haben. Ist der Knoten 'null' wurde kein
	 * Weg zwischen zwei Knoten gefunden.
	 */
	private static Vertex bfs(Vertex source, Vertex target, HashMap<Vertex, Vertex> parentMapSource,
			HashMap<Vertex, Vertex> parentMapTarget) {
		// Listen von Knoten, die die Breitensuche w�hrend eines Schrittes absuchen
		ArrayList<Vertex> iteration_list_source = new ArrayList<Vertex>();
		ArrayList<Vertex> iteration_list_target = new ArrayList<Vertex>();
		// Quell- und Zielknoten werden im ersten Schritt iteriert
		iteration_list_source.add(source);
		iteration_list_target.add(target);
		parentMapSource.put(source, null);
		parentMapTarget.put(target, null);

		while (true) {
			// Eine Iteration der Breitensuche startend vom Quellknoten
			Vertex bfs_meetup = bfsSourceToTargetIteration(iteration_list_source, parentMapSource, parentMapTarget);
			// Wurde ein Knoten gefunden, den die Suche startend vom Zielknoten
			// gefunden hat, wird dieser zur�ckgeliefert
			if (bfs_meetup != null)
				return bfs_meetup;
			// Hat die Suche startend vom Quellknoten keinen neuen, noch nicht
			// gefundenen Knoten gefunden, existiert kein Weg
			if (iteration_list_source.isEmpty())
				break;
			// Eine Iteration der Breitensuche startend vom Zielknoten
			bfs_meetup = bfsTargetToSourceIteration(iteration_list_target, parentMapSource, parentMapTarget);
			// Wurde ein Knoten gefunden, den die Suche startend vom
			// Quellknotengefunden hat, wird dieser zur�ckgeliefert
			if (bfs_meetup != null)
				return bfs_meetup;
			// Hat die Suche startend vom Zielknoten keinen neuen, noch nicht
			// gefundenen Knoten gefunden, existiert kein Weg
			if (iteration_list_target.isEmpty())
				break;
		}

		return null;
	}

	/*
	 * Breitensuche startend von Quellknoten. Iteriert alle Knoten, die sich in
	 * einer Liste befinden. Die Liste wird am Ende mit den Knoten gef�llt, die
	 * w�hrend der Suche gefunden wurden. Findet diese Suche einen Knoten, der
	 * bereits in der Breitensuche startend vom Zielknoten gefunden wurde, wird
	 * dieser zur�ckgeliefert.
	 */
	private static Vertex bfsSourceToTargetIteration(ArrayList<Vertex> iteration_list,
			HashMap<Vertex, Vertex> parentMapSource, HashMap<Vertex, Vertex> parentMapTarget) {

		// Speichert alle Knoten, die w�hrend der Iteration gefunden werden
		Stack<Vertex> in_queue = new Stack<Vertex>();

		for (Vertex vertex : iteration_list) {
			// Alle Zielknoten der ausgehenden, nicht ausgelasteten Kanten, die noch
			// nicht besucht wurden, werden besucht
			for (Edge edge : vertex.getOutgoingEdges()) {
				// �berpr�fung, ob die Kante noch nicht ausgelastet ist
				if (!edge.isCapacityInfinite() && edge.getCapacity() <= 0)
					continue;
				Vertex outgoing = edge.getTargetVertex();
				if (parentMapSource.containsKey(outgoing))
					continue;
				parentMapSource.put(outgoing, vertex);
				in_queue.push(outgoing);
				// �berpr�fung, ob Knoten bereits von Breitensuche, startend vom
				// Zielknoten, gefunden wurde
				if (parentMapTarget.containsKey(outgoing))
					return outgoing;
			}

		}

		// Die Iterationliste wird mit den Knoten gef�llt, die w�hrend der Iteration
		// gefunden wurden
		iteration_list.clear();
		iteration_list.addAll(in_queue);
		in_queue.clear();

		return null;
	}

	/*
	 * Breitensuche startend von Zielknoten. Iteriert alle Knoten, die sich in
	 * einer Liste befinden. Die Liste wird am Ende mit den Knoten gef�llt, die
	 * w�hrend der Suche gefunden wurden. Findet diese Suche einen Knoten, der
	 * bereits in der Breitensuche startend vom Quellknoten gefunden wurde, wird
	 * dieser zur�ckgeliefert.
	 */
	private static Vertex bfsTargetToSourceIteration(ArrayList<Vertex> iteration_list,
			HashMap<Vertex, Vertex> parentMapSource, HashMap<Vertex, Vertex> parentMapTarget) {

		// Speichert alle Knoten, die w�hrend der Iteration gefunden werden
		Stack<Vertex> in_queue = new Stack<Vertex>();

		for (Vertex vertex : iteration_list) {

			// Alle Quellknoten der eingehenden, nicht ausgelasteten Kanten, die noch
			// nicht besucht wurden, werden besucht
			for (Edge edge : vertex.getIncomingEdges()) {
				// �berpr�fung, ob die Kante noch nicht ausgelastet ist
				if (!edge.isCapacityInfinite() && edge.getCapacity() <= 0)
					continue;
				Vertex incoming = edge.getSourceVertex();
				if (parentMapTarget.containsKey(incoming))
					continue;
				parentMapTarget.put(incoming, vertex);
				in_queue.push(incoming);
				// �berpr�fung, ob Knoten bereits von Breitensuche startend vom
				// Zielknoten gefunden wurde
				if (parentMapSource.containsKey(incoming))
					return incoming;
			}

		}

		// Die Iterationliste wird mit den Knoten gef�llt, die w�hrend der Iteration
		// gefunden wurden
		iteration_list.clear();
		iteration_list.addAll(in_queue);
		in_queue.clear();

		return null;
	}

}
