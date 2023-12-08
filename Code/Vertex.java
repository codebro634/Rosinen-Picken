import java.util.HashMap;
import java.util.HashSet;

public class Vertex {

	/*
	 * Speichert den Wiedererkennungswert des Knoten. Die Variable ist
	 * unveränderlich, da ein Knoten seinen Wiedererkennungswert nicht ändern
	 * kann.
	 */
	private final int number;

	/*
	 * Speichert den aktuellen Wert des Knoten.
	 */
	private float value;

	/*
	 * Da sich der Wert des Knoten z.B durch Verschmelzen ändern kann, wird sich
	 * der Originalwert gemerkt.
	 */
	private final float original_value;

	/*
	 * Konstruktor: Initialwert: 'value' Wiedererkennungswert: 'number'
	 */
	Vertex(float value, int number) {
		this.value = value;
		original_value = value;
		this.number = number;
	}

	/*
	 * Der Originalwert des Knotens wird zurückgegeben.
	 */
	float getOriginalValue() {
		return original_value;
	}

	/*
	 * Der Wert des Knotens wird gesetzt.
	 */
	void changeValue(float value) {
		this.value = value;
	}

	/*
	 * Gibt den Wert des Knotens zurück.
	 */
	float getValue() {
		return value;
	}

	/*
	 * Der Wiedererkennungswert des Knoten wird zurückgegeben.
	 */
	int getNumber() {
		return number;
	}

	/*
	 * Eine Liste, die alle Knoten speichert, welche zu diesem Knoten hinzugefügt
	 * wurden.
	 */
	private HashSet<Vertex> mergedVertices = new HashSet<Vertex>();

	/*
	 * Gibt eine Liste wieder, welche alle zu diesem Knoten zusammengefügten
	 * Knoten enthält.
	 */
	HashSet<Vertex> getMergedVertices() {
		return mergedVertices;
	}

	/*
	 * Ein Set, das alle ausgehenden Kanten dieses Knotens speichert.
	 */
	private HashSet<Edge> outgoing_edges = new HashSet<Edge>();

	/*
	 * Speichert alle Zielknoten aller ausgehenden Kanten und das dazugehörige
	 * Kantenobjekt (Performance)
	 */
	private HashMap<Vertex, Edge> outgoing_vertices = new HashMap<Vertex, Edge>();

	/*
	 * Eine Liste aller ausgehenden Kanten wird zurückgegeben.
	 */
	HashSet<Edge> getOutgoingEdges() {
		return outgoing_edges;
	}

	/*
	 * Es wird zurückgegeben, ob dieser Knoten eine Kante besitzt, die auf einen
	 * bestimmten Knoten gerichtet ist.
	 */
	boolean outgoingVerticesContainsVertex(Vertex vertex) {
		return outgoing_vertices.containsKey(vertex);
	}

	/*
	 * Rückggabe der Kante kommend von diesem Knoten und gerichtet auf einen
	 * bestimmten Knoten falls vorhanden
	 */
	Edge getOutgoingEdge(Vertex vertex) {
		return outgoing_vertices.get(vertex);
	}

	/*
	 * Eine Kante, die diesen Knoten als Quellknoten hat, wird in die zugehörigen
	 * Listen eingetragen. Diese Methode wird und darf nur über die addEdge
	 * Methode aus der Graph-Klasse aufgerufen werden.
	 */
	void addOutgoingEdge(Edge edge) {
		outgoing_edges.add(edge);
		outgoing_vertices.put(edge.getTargetVertex(), edge);
	}

	/*
	 * Entfernt eine Kante, die diesen Knoten als Quellknoten hat, aus den
	 * zugeörigen Listen. Diese Methode wird darf nur über die Methode removeEdge
	 * aus der Graph-Klasse aufgerufen werden.
	 */
	void removeOutgoingEdge(Edge edge) {
		outgoing_edges.remove(edge);
		outgoing_vertices.remove(edge.getTargetVertex());
	}

	/*
	 * Liste, die alle Kanten speichert, die diesen Knoten als Zielknoten haben.
	 */
	private HashSet<Edge> incoming_edges = new HashSet<Edge>();

	/*
	 * Speichert alle Quellknoten aller eingehenden Kanten und das dazugehörige
	 * Kantenobjekt (Performance)
	 */
	private HashMap<Vertex, Edge> incoming_vertices = new HashMap<Vertex, Edge>();

	/*
	 * Liste aller eingehenden Kanten wird zurückgegeben.
	 */
	HashSet<Edge> getIncomingEdges() {
		return incoming_edges;
	}

	/*
	 * Rückggabe, ob eine Kante kommend von einem bestimmten Knoten, gerichtet auf
	 * diesen Knoten existiert
	 */
	boolean incomingVerticesContainsVertex(Vertex vertex) {
		return incoming_vertices.containsKey(vertex);
	}

	/*
	 * Rückggabe der Kante, die von einem bestimmten Knoten kommt und auf diesen
	 * Knoten gerichtet ist.
	 */
	Edge getIncomingEdge(Vertex vertex) {
		return incoming_vertices.get(vertex);
	}

	/*
	 * Eine Kante, gerichtet auf diesen Knoten ,wird in die zugehörigen Listen
	 * gespeichert. Diese Methode wird und darf nur über die addEdge Methode aus
	 * der DGraph-Klasse aufgerufen werden.
	 */
	void addIncomingEdge(Edge edge) {
		incoming_edges.add(edge);
		incoming_vertices.put(edge.getSourceVertex(), edge);
	}

	/*
	 * Eine Kante gerichtet auf diesen Knoten wird aus den zugehörigen Listen
	 * entfernt. Diese Methode wird und darf nur über die removeEdge Methode aus
	 * der DGraph-Klasse aufgerufen werden.
	 */
	void removeIncomingEdge(Edge edge) {
		incoming_edges.remove(edge);
		incoming_vertices.remove(edge.getSourceVertex());
	}

}
