public class Edge {

	/*
	 * Diese Klasse repr�sentiert eine Kante. Wenn eine Kapazit�t gesetzt wird,
	 * entspricht dies einer gewichteten Kante
	 */

	/*
	 * Speichert, ob die Kapazit�t dieser Kante Unendlich betr�gt.
	 */
	private boolean isCapacityInfinite;

	/*
	 * NUR F�R GUI! Wurde diese Kante als R�ckkante markiert. Diese Speicherung
	 * wird ben�tigt, da R�ckkanten nicht gemalt werden sollen.
	 */
	private boolean isInverseEdge;

	/*
	 * Die Kapazit�t dieser Kante.
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
	 * Konstruktor f�r ungewichtete Kante: Festlegung von wo die Kante startet und
	 * wohin sie gerichtet ist. Dieser Konstruktor wird nur benutzt, wenn die
	 * Eigenschaften, die eine gewichtete Kante ausmachen, nicht von N�ten sind.
	 */
	Edge(Vertex source_vertex, Vertex target_vertex) {
		this.source_vertex = source_vertex;
		this.target_vertex = target_vertex;
	}

	/*
	 * Konstruktor einer gewichteten Kante: Es wird festgelegt von wo die Kante
	 * startet und worauf sie gerichtet ist. Ihre Kapazit�t, ob ihr Wert Unendlich
	 * ist, und ob sie eine R�ckkante ist, wird festgelegt.
	 */
	Edge(Vertex source_vertex, Vertex target_vertex, float capacity, boolean isCapacityInfinite, boolean isInverseEdge) {
		this.source_vertex = source_vertex;
		this.target_vertex = target_vertex;
		this.capacity = capacity;
		this.isCapacityInfinite = isCapacityInfinite;
		this.isInverseEdge = isInverseEdge;
	}

	/*
	 * NUR F�R GUI! Gibt zur�ck ob, diese Kante eine R�ckkante ist.
	 */
	boolean isInverseEdge() {
		return isInverseEdge;
	}

	/*
	 * Die Kapazit�t der Kante wird zur�ckgegeben. Ist die Kapazit�t der Kante
	 * unendlich wird ein Fehler ausgegeben.
	 */
	float getCapacity() throws UnsupportedOperationException {
		if (isCapacityInfinite)
			throw new UnsupportedOperationException("[getWeight] the capacity of the edge is inifinite");
		return capacity;
	}

	/*
	 * Die Kapazit�t der Kante wird gesetzt. Ist die Kapazit�t der Kante
	 * unendlich, kann ihr Wert nicht mehr umge�ndert werden und ein Fehler wird
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
	 * Gibt zur�ck, ob die Kapazit�t der Kante unendlich betr�gt.
	 */
	boolean isCapacityInfinite() {
		return isCapacityInfinite;
	}

	/*
	 * Setzt die Kapazit�t der Kante auf Unendlich.
	 */
	void setCapacityInfinity() {
		isCapacityInfinite = true;
	}

	/*
	 * Gibt den Knoten zur�ck, aus welchem diese Kante aussgeht.
	 */
	Vertex getSourceVertex() {
		return source_vertex;
	}

	/*
	 * Gibt den Knoten zur�ck, auf welchen diese Kante gerichtet ist.
	 */
	Vertex getTargetVertex() {
		return target_vertex;
	}

}