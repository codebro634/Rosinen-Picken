import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Remove_Cycles {

	/*
	 * Entfernt alle Zyklen aus einem Graphen
	 */
	static void removeAllCycles(Graph graph) {
		HashSet<Vertex> whiteSet = new HashSet<Vertex>(graph.getVertices());
		HashSet<Vertex> blackSet = new HashSet<Vertex>();
		// Entfernt solange einen Zyklus aus dem Graphen wie dieser noch Zyklen
		// enth�lt
		while (removeCycleIteration(graph, whiteSet, blackSet));
	}

	/*
	 * Nur f�r GUI: Entfernt einen Zyklus aus einem Graphen.
	 */
	static boolean removeCycleStep(Graph graph) {
		return removeCycleIteration(graph, new HashSet<Vertex>(graph.getVertices()), new HashSet<Vertex>());
	}

	/*
	 * Findet und entfernt einen Zyklus aus einem Graphen. Liefert zur�ck, ob ein
	 * Zyklus entfernt werden konnte. Knoten, die als nicht zu einem Zyklus
	 * geh�rend identifiziert wurden, werden gespeichert. Knoten, die noch nicht
	 * untersucht wurden, werden gespeichert.
	 */
	private static boolean removeCycleIteration(Graph graph, HashSet<Vertex> stillWhite, HashSet<Vertex> ardyBlack) {
		// Alle unerforschten Knoten
		HashSet<Vertex> whiteSet = stillWhite;
		// Alle Knoten, die gerade erforscht werden
		HashSet<Vertex> graySet = new HashSet<Vertex>();
		// Alle Knoten, die nicht zu einem Zyklus geh�ren
		HashSet<Vertex> blackSet = ardyBlack;
		// Speichert welcher Knoten von welchem Knoten aufgerufen wurde (f�r
		// Backtracking)
		HashMap<Vertex, Vertex> parentMap = new HashMap<Vertex, Vertex>();

		// Der Algorithmus l�uft solange bis alle Knoten erforscht wurden
		while (whiteSet.size() > 0) {
			// Ein Knoten aus der unerforschten Liste wird gew�hlt
			Vertex current = whiteSet.iterator().next();
			// Tiefensuche probiert einen Zyklus zu finden
			Vertex cycle_end = dfs(null, current, whiteSet, graySet, blackSet, parentMap);
			// cycle_end ist null, wenn kein Zyklus gefunden wurde
			if (cycle_end != null) {
				// Alle Knoten des Zyklus werden zum Knoten, der als Ende des Zyklus
				// markiert wurde zusammengef�gt
				removeCycleFromGraphWithMap(graph, cycle_end, parentMap);
				// Alle Knoten, die sich noch in der grauen Liste befinden, werden
				// wieder in die wei�e Liste verschoben
				for (Vertex gray : new ArrayList<Vertex>(graySet)) {
					if (graph.getVertices().contains(gray))
						moveVertex(gray, graySet, whiteSet);
				}
				Graph.statistics_cycles++;
				// Falls ein Zyklus gefunden wurde wird abgebrochen und dies
				// entsprechend zur�ckgegeben
				return true;
			}
		}
		// R�ckgabe, dass kein Zyklus gefunden wurde
		return false;
	}

	/*
	 * Fasst einen Zyklus mittels Backtracking zusammen
	 */
	private static void removeCycleFromGraphWithMap(Graph graph, Vertex cycle_end, HashMap<Vertex, Vertex> parentMap) {
		// Liste der Knoten, die verschmolzen werden. Initialgr��e ist die
		// Graphengr��e, da der Zyklus nicht gr��er als der Graph sein kann
		ArrayList<Vertex> merge_list = new ArrayList<Vertex>(graph.getVertices().size());

		// Backtracking
		Vertex child = parentMap.get(cycle_end);
		while (child != cycle_end) {
			merge_list.add(child);
			child = parentMap.get(child);
		}
		// Liste mit den Zyklusknoten wird als Argument an die Methode zum
		// Verschmelzen dieser geliefert
		graph.mergeVertices(cycle_end, merge_list);
	}

	/*
	 * Rekursive Tiefensuche nach einem Zyklus.
	 */
	private static Vertex dfs(Vertex parent, Vertex current_vertex, HashSet<Vertex> whiteSet, HashSet<Vertex> graySet,
			HashSet<Vertex> blackSet, HashMap<Vertex, Vertex> parentMap) {
		// Eintrag in die Backtrackingliste
		parentMap.put(current_vertex, parent);
		// Knoten wird in die Liste der zu erforschenden Knoten verschoben
		moveVertex(current_vertex, whiteSet, graySet);
		for (Edge edge : current_vertex.getOutgoingEdges()) {
			// Die Zielknoten aller ausgehenden Kanten werden untersucht
			Vertex neighbor = edge.getTargetVertex();
			// Falls der gefundene Knoten sich in der schwarzen Liste befindet, wurde
			// dieser bereits erforscht und ist daher uninteressant
			if (blackSet.contains(neighbor)) {
				continue;
			}
			// Falls sich der gefundene Knoten schon in der Liste der zu erforschenden
			// Knoten befindet (graue Liste), hei�t es, dass ein Zyklus existiert
			if (graySet.contains(neighbor)) {
				parentMap.put(neighbor, current_vertex);
				return neighbor;
			}
			// Hier befindet sich der angrenzende Knoten in der wei�en Liste.
			// Die Tiefensuche wird von diesem aus weiter fortgef�hrt.
			Vertex cycle_end = dfs(current_vertex, neighbor, whiteSet, graySet, blackSet, parentMap);
			if (cycle_end != null)
				return cycle_end;
		}
		// An dieser Stelle geh�rt der untersuchte Knoten nicht zu einem
		// Zyklus und wird daher in die schwarze Liste verschoben
		moveVertex(current_vertex, graySet, blackSet);
		return null;
	}

	/*
	 * Verschiebt einen Knoten aus einer Liste in eine andere Liste. Der Knoten
	 * wird aus einer Liste entfernt und einer anderen Liste hinzugef�gt.
	 */
	private static void moveVertex(Vertex vertex, HashSet<Vertex> sourceSet, HashSet<Vertex> destinationSet) {
		sourceSet.remove(vertex);
		destinationSet.add(vertex);
	}

}
