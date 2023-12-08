import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Stack;

/*
 * Klasse Graph repräsentiert einen gewichteten gerichteten Graphen mit Kantenkapazitäten.
 */

public class Graph {

	/*
	 * Speichert einen Graphen in eine Datei.
	 */
	public static void save(Graph graph, String save_name) {
		// Ausgabestring erzeugen, indem alle Knoten und Kanten des Graphen iteriert
		// werden
		String[] content = new String[graph.getVertices().size() + graph.getEdges().size() + 4];
		content[0] = "#Anzahl der Knoten";
		content[1] = "" + graph.getVertices().size();
		content[2] = "# Knoten mit Gewichten";
		ArrayList<Integer> numbers = new ArrayList<Integer>(graph.getVertices().size());
		for (Vertex vertex : graph.getVertices())
			numbers.add(vertex.getNumber());
		Collections.sort(numbers);
		for (int i = 0; i < numbers.size(); i++)
			content[i + 3] = numbers.get(i) + " " + graph.getVertex(numbers.get(i)).getOriginalValue();
		content[graph.getVertices().size() + 3] = "#Kanten";
		ArrayList<Edge> edges = new ArrayList<Edge>(graph.getEdges());
		for (int i = 0; i < edges.size(); i++)
			content[graph.getVertices().size() + 4 + i] = edges.get(i).getSourceVertex().getNumber() + " "
					+ edges.get(i).getTargetVertex().getNumber();
		// Ausgabestring wird in Datei geschrieben
		writeFile(".\\" + Import_Graph.DIRECTORY + "\\" + save_name + ".txt", content);
	}

	/*
	 * Schreibt Text in eine Datei. Ist die angegebene Datei nicht vorhanden, wird
	 * diese erzeugt.
	 */
	private static void writeFile(String filename, String[] text) {

		if (filename == null || filename.equals("") || !filename.contains(".")) {
			System.out.println("An error occoured due to filename!");
			return;
		}

		if (text == null || text.length == 0) {
			System.out.println("An error occoured due to text content");
			return;
		}

		File levelFile = new File(filename);
		if (!levelFile.exists()) {
			try {
				levelFile.createNewFile();
				new File(filename).createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter writeFile = null;
		BufferedWriter writer = null;
		try {
			writeFile = new FileWriter(levelFile);
			writer = new BufferedWriter(writeFile);

			for (String s : text) {
				writer.write(s);
				writer.newLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
			}
		}
	}

	/*
	 * Statistiken, welche jederzeit mitgeführt werden.
	 */
	static int statistics_rule_1a = 0;
	static int statistics_rule_1b = 0;
	static int statistics_rule_2a = 0;
	static int statistics_rule_2b = 0;
	static int statistics_cycles = 0;
	static int statistics_split = 0;
	static int statistics_edges = 0;

	/*
	 * Statistiken werden via folgender Methode zurückgesetzt.
	 */
	static void reset_statistics() {
		statistics_rule_1a = 0;
		statistics_rule_1b = 0;
		statistics_rule_2a = 0;
		statistics_rule_2b = 0;
		statistics_cycles = 0;
		statistics_split = 0;
		statistics_edges = 0;
	}

	/*
	 * Jeder Knoten des Graphen besitzt einen unterschiedlichen
	 * Wiedererkennungswert. "max_number" speichert den Knoten mit dem zurzeit
	 * höchsten Wiedererkennungswert. Die Knoten werden fortlaufend nummeriert.
	 */
	private static int max_number = -1;

	/*
	 * "vertices" enthält alle Knoten des Graphen.
	 */
	private final HashSet<Vertex> vertices = new HashSet<Vertex>();

	/*
	 * "vertices_by_number" speichert die gleichen Knoten wie "vertices". Der
	 * Key-Wert enthält die Wiedererkennungswerte der einzelnen Knoten, um auf
	 * diese schnell zugreifen zu können.
	 */
	private final HashMap<Integer, Vertex> vertices_by_number = new HashMap<Integer, Vertex>();

	/*
	 * Alle Knoten des Graphen werden in Form eines HashSets wiedergegeben
	 */
	HashSet<Vertex> getVertices() {
		return vertices;
	}

	/*
	 * Die Methode gibt einen Knoten über dessen Wiedererkennungswert zurück. Null
	 * wird zurückgegeben, falls kein Knoten mit dem Wiedererkennungswert in dem
	 * Graphen vorhanden ist.
	 */
	Vertex getVertex(int number) {
		return vertices_by_number.get(number);
	}

	/*
	 * Fügt einen Knoten dem Graphen mit einem Initialgewicht zu und gibt diesem
	 * Knoten einen eindeutigen Wiedererkennungswert. Der zugefügte Knoten wird
	 * zurückgegeben.
	 */
	Vertex addVertex(float value) {
		Vertex ver = new Vertex(value, ++max_number);
		vertices.add(ver);
		vertices_by_number.put(max_number, ver);
		return ver;
	}

	/*
	 * Fügt dem Graphen einen Knoten mit einem Initialgewicht und einem
	 * festgelegten Wiedererkennungswert hinzu. Falls der angegebene
	 * Wiedererkennungswert bereits einem anderem Knoten zugehörig ist, wird eine
	 * Fehlermeldung geworfen. Der hinzugefügte Knoten wird als Objekt
	 * zurückgegeben.
	 */
	Vertex addVertex(float value, int number) throws IllegalArgumentException {
		if (vertices_by_number.containsKey(number))
			throw new IllegalArgumentException("Duplicate identifier");
		Vertex ver = new Vertex(value, number);
		vertices.add(ver);
		vertices_by_number.put(number, ver);
		if (max_number < number)
			max_number = number;
		return ver;
	}

	/*
	 * Ein Knoten wird gekauft und aus dem Graphen entfernt und der Liste, die die
	 * gekauften Knoten speichert, hinzugefügt. Abhängige Informationen (Kanten)
	 * werden in Methode 'removeVertex' mitbehandelt.
	 */
	void buyVertex(Vertex vertex) {
		removeVertex(vertex);
		bought_vertices.add(vertex);
		// Für GUI: Knoten wird grün eingefärbt
		if (Graph_Editing_Control.fill_colors)
			Graph_Editing_Control.vertices_color.put(Graph_Editing_Control.old_graph.getVertex(vertex.getNumber()),
					Color.GREEN);
	}

	/*
	 * Entfernt einen Knoten aus dem Graphen. Alle Kanten verbunden mit dem Knoten
	 * werden ebenfalls entfernt. Wurde der Knoten entfernt, ist sein
	 * Wiedererkennungswert wieder nutzbar.
	 */
	void removeVertex(Vertex vertex) {
		// Ausgehende Kanten entfernen
		for (Edge edg : new ArrayList<Edge>(vertex.getOutgoingEdges()))
			removeEdge(edg);
		// Eingehende Kanten entfernen
		for (Edge edg : new ArrayList<Edge>(vertex.getIncomingEdges()))
			removeEdge(edg);
		// Knoten aus Listen entfernen
		vertices.remove(vertex);
		vertices_by_number.remove(vertex.getNumber());
		// Für GUI: Knoten wird rot eingefärbt
		if (Graph_Editing_Control.fill_colors)
			Graph_Editing_Control.vertices_color.put(Graph_Editing_Control.old_graph.getVertex(vertex.getNumber()),
					Color.RED);
	}

	/*
	 * Verschmilzt eine Liste von Knoten mit einem Zielknoten. Es wird die Summe
	 * der Gewichte aller Knoten aus der Liste auf den Zielknoten addiert.
	 * Ebenfalls werden alle Kanten, verbunden mit einem der Knoten der Liste, nun
	 * mit dem Zielknoten verbunden.
	 */
	void mergeVertices(Vertex merge_target, ArrayList<Vertex> vertices) throws IllegalArgumentException {
		float values = 0;

		for (Vertex to_merge : vertices) {
			// Falls der Knoten mit sich selber verschmolzen werden soll, wird ein
			// Fehler ausgegeben
			if (to_merge == merge_target)
				throw new IllegalArgumentException("Zielknoten kann nicht mit sich selbst gemergt werden.");
			// Alte, eingehende Kanten von Knoten aus Liste auf Mergeknoten umrichten
			for (Edge edg : new ArrayList<Edge>(to_merge.getIncomingEdges())) {
				removeEdge(edg);
				if (edg.getSourceVertex() != merge_target)
					addEdge(edg.getSourceVertex(), merge_target);
			}
			// Alte, ausgehende Kanten von Knoten der Liste von Mergeknoten kommend,
			// erzeugen
			for (Edge edg : new ArrayList<Edge>(to_merge.getOutgoingEdges())) {
				removeEdge(edg);
				if (merge_target != edg.getTargetVertex())
					addEdge(merge_target, edg.getTargetVertex());
			}

			// Knotenwert von Knoten der Liste zum Wert aller Knoten der Liste
			// addieren
			values += to_merge.getValue();
			// Knoten wird zu Liste der gemergten Knoten des Mergeknotens hinzugefügt
			merge_target.getMergedVertices().add(to_merge);
			// Falls der zu mergende Knoten selber Produkt einer Verschmelzung war,
			// werden zu ihm gemergten Knoten in den Mergeknoten übertragen
			merge_target.getMergedVertices().addAll(to_merge.getMergedVertices());
			to_merge.getMergedVertices().clear(); // Speicherverbrauch Eingrenzung!
			// Entfernen des zu verschmelzenden Knotens aus dem Graphen
			removeVertex(to_merge);
			// Für GUI: Knoten wird blau eingefärbt
			if (Graph_Editing_Control.fill_colors)
				Graph_Editing_Control.vertices_color.put(Graph_Editing_Control.old_graph.getVertex(to_merge.getNumber()),
						Color.BLUE);
		}
		// Gesammelte Knotenwerte, werden auf Mergeknoten addiert
		merge_target.changeValue(merge_target.getValue() + values);
	}

	/*
	 * Eine Liste, die alle Kanten des Graphen speichert.
	 */
	private final HashSet<Edge> edges = new HashSet<Edge>();

	/*
	 * Eine Liste aller im Graph vorhandenen Kanten wird wiedergegeben.
	 */
	HashSet<Edge> getEdges() {
		return edges;
	}

	/*
	 * Eine ungewichtete Kante ausgehend von "source_vertex" und gerichtet auf
	 * "target_vertex" wird dem Graphen hinzufügt. Falls einer der beiden Knoten
	 * nicht im Graphen vorhanden ist oder wenn der Zielknoten dem Quellknoten
	 * entspricht, wird ein Fehler ausgegeben. Besteht bereits eine Kante
	 * ausgehend vom Quellknoten gerichtet zum Zielknoten, wird die Kante nicht
	 * erstellt. Die Methode gibt die hinzugefügte Kante als Objekt zurück.
	 */
	Edge addEdge(Vertex source_vertex, Vertex target_vertex) throws IllegalArgumentException {
		if (!vertices.contains(source_vertex) || !vertices.contains(target_vertex))
			throw new IllegalArgumentException("[addEdge] One of the selected vertices does not exist in the graph!");

		if (source_vertex == target_vertex)
			throw new IllegalArgumentException("[addEdge] source vertex equals target vertex");

		if (source_vertex.outgoingVerticesContainsVertex(target_vertex)) {
			return null;
		}

		Edge edge = new Edge(source_vertex, target_vertex);
		edges.add(edge);

		target_vertex.addIncomingEdge(edge);
		source_vertex.addOutgoingEdge(edge);

		return edge;
	}

	/*
	 * Eine gewichtete Kante wird ausgehend von "source_vertex" und gerichtet auf
	 * "target_vertex" wird dem Graphen hinzufügt. Die Kante muss ebenfalls als
	 * Rückkante markiert werden, da diese identifiziert werden müssen, um sie
	 * nicht zu malen. Falls einer der beiden Knoten nicht im Graphen vorhanden
	 * ist oder wenn der Zielknoten dem Quellknoten entspricht, wird ein Fehler
	 * ausgegeben. Besteht bereits eine Kante ausgehend vom Quellknoten gerichtet
	 * zum Zielknoten, wird die Kante nicht erstellt. Die Methode gibt die
	 * hinzugefügte Kante als Objekt zurück.
	 */
	Edge addEdge(Vertex source_vertex, Vertex target_vertex, float capacity, boolean isCapacityInfinite,
			boolean isInverseEdge) {
		if (!vertices.contains(source_vertex) || !vertices.contains(target_vertex))
			throw new IllegalArgumentException("[addEdge] One of the selected vertices does not exist in the graph!");

		if (source_vertex == target_vertex)
			throw new IllegalArgumentException("[addEdge] source vertex equals target vertex");

		if (source_vertex.outgoingVerticesContainsVertex(target_vertex)) {
			return null;
		}

		Edge edge = new Edge(source_vertex, target_vertex, capacity, isCapacityInfinite, isInverseEdge);
		edges.add(edge);

		target_vertex.addIncomingEdge(edge);
		source_vertex.addOutgoingEdge(edge);

		return edge;
	}

	/*
	 * Eine Kante wird aus dem Graph entfernt.
	 */
	void removeEdge(Edge edg) {
		edg.getSourceVertex().removeOutgoingEdge(edg); // Aus Quellknoten entfernen
		edg.getTargetVertex().removeIncomingEdge(edg); // Aus Zielknoten entfernen
		edges.remove(edg); // Aus Kantenliste entfernen
	}

	/*
	 * Die Liste speichert alle Knotenobjekte,die über die "to_buy" Methode
	 * entfernt wurden. Dementsprechend sind in der Liste alle als zu kaufen
	 * identifizierten Knoten enthalten.
	 */
	private final Stack<Vertex> bought_vertices = new Stack<Vertex>();

	/*
	 * Es wird eine Liste mit allen Wiedererkennungswerten der gekauften Knoten
	 * zurückgegeben.
	 */
	ArrayList<Integer> getBoughtVertices() {
		if (max_number < 0)
			max_number = 0;
		ArrayList<Integer> list = new ArrayList<Integer>(max_number);
		for (Vertex bought : bought_vertices) {
			list.add(bought.getNumber());
			for (Vertex merged : bought.getMergedVertices())
				list.add(merged.getNumber());
		}
		return list;
	}

	/*
	 * Die Methode gibt die Gesamtsumme der Knotengewichte aller gekauften Knoten
	 * wieder.
	 */
	double getTotalAmountOfBoughtVertices() {
		double amount = 0;
		for (Vertex vertex : bought_vertices)
			amount += vertex.getValue();
		return amount;
	}

	/*
	 * Es wird eine visuelle Kopie des Graphen zurückgegeben. Dieses ist keine
	 * vollständige Kopie des Graphen, sondern nur ein Objekt, welches mit
	 * GUI_Graph-Klasse gezeichnet werden kann.
	 */
	Graph visual_copy() {
		Graph graph = new Graph();
		// Knoten kopieren
		for (Entry<Integer, Vertex> entry : vertices_by_number.entrySet()) {
			graph.vertices_by_number.put(entry.getKey(),
					new Vertex(entry.getValue().getValue(), entry.getValue().getNumber()));
			graph.vertices.add(graph.vertices_by_number.get(entry.getKey()));
		}
		// Kanten kopieren
		for (Edge edge : edges) {
			graph.addEdge(graph.getVertex(edge.getSourceVertex().getNumber()),
					graph.getVertex(edge.getTargetVertex().getNumber()));
		}

		return graph;
	}

}
