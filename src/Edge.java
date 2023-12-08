public class Edge {

	/*
	 * Diese Klasse repräsentiert eine Kante. Wenn eine Kapazität gesetzt wird,
	 * entspricht dies einer gewichteten Kante
	 */

	/*
	 * Speichert, ob die Kapazität dieser Kante Unendlich beträgt.
	 */
	private boolean isCapacityInfinite;

	/*
	 * NUR FÜR GUI! Wurde diese Kante als Rückkante markiert. Diese Speicherung
	 * wird benötigt, da Rückkanten nicht gemalt werden sollen.
	 */
	private boolean isInverseEdge;

	/*
	 * Die Kapazität dieser Kante.
	 */
	private float capacity;

	/*
	 * Referenz auf Quellknoten der Kante. Der Knoten aus dem diese Kante ausgeht.
	 */
	private Vertex source_vertex;

	/*
	 * Referenz auf Zielknoten der Kante. Der Knoten, auf den diese Kante
	 * gerichtet ist.
	 */
	private Vertex target_vertex;

	/*
	 * Konstruktor für ungewichtete Kante: Festlegung von wo die Kante startet und
	 * wohin sie gerichtet ist. Dieser Konstruktor wird nur benutzt, wenn die
	 * Eigenschaften, die eine gewichtete Kante ausmachen, nicht von Nöten sind.
	 */
	Edge(Vertex source_vertex, Vertex target_vertex) {
		this.source_vertex = source_vertex;
		this.target_vertex = target_vertex;
	}

	/*
	 * Konstruktor einer gewichteten Kante: Es wird festgelegt von wo die Kante
	 * startet und worauf sie gerichtet ist. Ihre Kapazität, ob ihr Wert Unendlich
	 * ist, und ob sie eine Rückkante ist, wird festgelegt.
	 */
	Edge(Vertex source_vertex, Vertex target_vertex, float capacity, boolean isCapacityInfinite, boolean isInverseEdge) {
		this.source_vertex = source_vertex;
		this.target_vertex = target_vertex;
		this.capacity = capacity;
		this.isCapacityInfinite = isCapacityInfinite;
		this.isInverseEdge = isInverseEdge;
	}

	/*
	 * NUR FÜR GUI! Gibt zurück ob, diese Kante eine Rückkante ist.
	 */
	boolean isInverseEdge() {
		return isInverseEdge;
	}

	/*
	 * Die Kapazität der Kante wird zurückgegeben. Ist die Kapazität der Kante
	 * unendlich wird ein Fehler ausgegeben.
	 */
	float getCapacity() throws UnsupportedOperationException {
		if (isCapacityInfinite)
			throw new UnsupportedOperationException("[getWeight] the capacity of the edge is inifinite");
		return capacity;
	}

	/*
	 * Die Kapazität der Kante wird gesetzt. Ist die Kapazität der Kante
	 * unendlich, kann ihr Wert nicht mehr umgeändert werden und ein Fehler wird
	 * ausgegeben. Die Fehlermeldung kann nur beim Edmond-Karp auftreten, weil da
	 * unendliche Kantengewichte verwendet werden. Sollte die Fehlermeldung
	 * ausgegeben werden, existiert ein Fehler in der Implementierung.
	 */
	void setCapacity(float cap) {
		if (isCapacityInfinite)
			throw new UnsupportedOperationException(
					"[getWeight] edge capacity is already infinite and thus cannot be set to a rational number");
		capacity = cap;
	}

	/*
	 * Gibt zurück, ob die Kapazität der Kante unendlich beträgt.
	 */
	boolean isCapacityInfinite() {
		return isCapacityInfinite;
	}

	/*
	 * Setzt die Kapazität der Kante auf Unendlich.
	 */
	void setCapacityInfinity() {
		isCapacityInfinite = true;
	}

	/*
	 * Gibt den Knoten zurück, aus welchem diese Kante aussgeht.
	 */
	Vertex getSourceVertex() {
		return source_vertex;
	}

	/*
	 * Gibt den Knoten zurück, auf welchen diese Kante gerichtet ist.
	 */
	Vertex getTargetVertex() {
		return target_vertex;
	}

}