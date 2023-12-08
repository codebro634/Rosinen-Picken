import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JOptionPane;

public class Graph_Editing_Control {

	static Graph graph_to_solve = new Graph();
	
	static File graph_source = null;
	
	static boolean in_algorithm = false;
	
	/*
	 * Lässt Nutzer eine Datei auswählen
	 * und lädt diese ins Programm.
	 */
	static void import_graph(){
		GUI_Frame.status.setText("");
			graph_to_solve = Import_Graph.import_graph();
    	GUI_Graph.displayGraph(graph_to_solve);
    	if(GUI_Graph.is_graph_too_big){
    		JOptionPane.showMessageDialog(null, "Der Graph ist zu groß, um ihn im GUI anzuzeigen. Er kann jedoch trotzdem gelöst werden.");
    	}
    	GUI_Graph.paint();
    	GUI_Frame.updateLists();
	}
	
	/*
	 * Legt einen Graphen fest, der
	 * bearbeitet und angezeigt werden soll.
	 */
	static void setGraph(Graph graph){
		graph_source = null;
		Graph_Editing_Control.graph_to_solve = graph;
		GUI_Graph.displayGraph(graph);
		GUI_Graph.paint();
		GUI_Frame.updateLists();
	}
	
	private static boolean isThreadRunning(){
		return solve_thread.running;
	}
	
	/*
	 * Algorithmus abbrechen, um wieder 
	 * in den Homescreen zu gelangen.
	 */
	static void cancel(){
		if(isThreadRunning()) return;
		if(JOptionPane.showConfirmDialog(null,"Möchten sie wirklich den Algorithmus abbrechen? (Der Ursprungsgraph wird wiederhergestellt)") != 0) return;		
		if(graph_source != null){
		graph_to_solve = Import_Graph.readGraph(graph_source);
		GUI_Graph.displayGraph(graph_to_solve);
		GUI_Graph.paint();
		}		
		GUI_Frame.frame_state_1_setup();
	}
	
	static final byte ALGORITHM_SPEED_INSTANT = 0;
	static final byte ALGORITHM_SPEED_STEP = 1;
	
	private static byte algorithm_speed = ALGORITHM_SPEED_STEP;
	
	/*
	 * Auswahl, ob Algorithmus schrittweise
	 * oder sofort vollständig ausgeführt werden soll
	 */
	static void setAlgorithmSpeed(byte speed){
		algorithm_speed = speed;
	}
	
	static final byte ALGORITHM_MODE_NONE_YET = -1;
	static final byte ALGORITHM_MODE_NONE = 0;
	static final byte ALGORITHM_MODE_CYCLES = 1;
	static final byte ALGORITHM_MODE_RULES = 2;
	static final byte ALGORITHM_MODE_CLOSURE = 3;
	static final byte ALGORITHM_MODE_EDGES = 4;
		
	//Speichert, welches Verfahren auf den Graphen angewendet werden soll
	static byte algorithm_mode = ALGORITHM_MODE_NONE_YET;
	
	static boolean best_closure_bought = false;
	
	/*
	 * Auswahl und Start des Zyklen entfernen Algorithmus
	 */	
	static void clickCycles(){
		if(isThreadRunning()) return;
		if(best_closure_bought){
			JOptionPane.showMessageDialog(null, "Die optimale Teilmenge wurde bereits gefunden!");
			return;
		}
		
		if(algorithm_speed == ALGORITHM_SPEED_INSTANT){
			Remove_Cycles.removeAllCycles(graph_to_solve);
			algorithm_mode = ALGORITHM_MODE_NONE;
			GUI_Graph.paint();
			GUI_Frame.updateLists();
		}
		else{
			
			if(algorithm_mode != ALGORITHM_MODE_NONE && algorithm_mode != ALGORITHM_MODE_NONE_YET){
				JOptionPane.showMessageDialog(null, "Es läuft bereits ein Algorithmus, beenden sie diesen zuerst.");
				return;
			}
			
			algorithm_mode = ALGORITHM_MODE_CYCLES;
			GUI_Frame.b1.setBackground(Color.GREEN);
			GUI_Frame.b3.setBackground(Color.GRAY);
			GUI_Frame.b4.setBackground(Color.GRAY);
			GUI_Frame.b7.setBackground(Color.GRAY);
		}
	}
	
	/*
	 * Auswahl und Start der Regeln (1a,1b,2a,2b), die den Graphen vereinfachen
	 */	
	static void clickRules(){
		if(isThreadRunning()) return;
		if(best_closure_bought){
			JOptionPane.showMessageDialog(null, "Die optimale Teilmenge wurde bereits gefunden!");
			return;
		}
		
		if(algorithm_speed == ALGORITHM_SPEED_INSTANT){
			Shorten_Rules.apply_rules(graph_to_solve);
			algorithm_mode = ALGORITHM_MODE_NONE;
			GUI_Graph.paint();
			GUI_Frame.updateLists();
		}
		else{
			
			if(algorithm_mode != ALGORITHM_MODE_NONE && algorithm_mode != ALGORITHM_MODE_NONE_YET){
				JOptionPane.showMessageDialog(null, "Es läuft bereits ein Algorithmus, beenden sie diesen zuerst.");
				return;
			}
			
			algorithm_mode = ALGORITHM_MODE_RULES;
			GUI_Frame.b1.setBackground(Color.GRAY);
			GUI_Frame.b3.setBackground(Color.GREEN);
			GUI_Frame.b4.setBackground(Color.GRAY);
			GUI_Frame.b7.setBackground(Color.GRAY);
		}
	}
	
	/*
	 * Auswahl und Start, dass redundante Kanten entfernt werden sollen
	 */
	static void clickEdges(){
		if(isThreadRunning()) return;
		if(best_closure_bought){
			JOptionPane.showMessageDialog(null, "Die optimale Teilmenge wurde bereits gefunden!");
			return;
		}
		
		if(algorithm_speed == ALGORITHM_SPEED_INSTANT){
			Remove_Redundant_Edges.removeRedundantEdges(graph_to_solve);
			algorithm_mode = ALGORITHM_MODE_NONE;
			GUI_Graph.paint();
			GUI_Frame.updateLists();
		}
		else{
			
			if(algorithm_mode != ALGORITHM_MODE_NONE && algorithm_mode != ALGORITHM_MODE_NONE_YET){
				JOptionPane.showMessageDialog(null, "Es läuft bereits ein Algorithmus, beenden sie diesen zuerst.");
				return;
			}
			
			algorithm_mode = ALGORITHM_MODE_EDGES;
			GUI_Frame.b1.setBackground(Color.GRAY);
			GUI_Frame.b3.setBackground(Color.GRAY);
			GUI_Frame.b4.setBackground(Color.GRAY);
			GUI_Frame.b7.setBackground(Color.GREEN);
		}
	}
	
	/*
	 * Auswahl und Start, dass die wertvollste verbleibende Teilmenge bestimmt werden soll
	 */	
	static void clickClosure(){
		if(isThreadRunning()) return;
		if(best_closure_bought){
			JOptionPane.showMessageDialog(null, "Die optimale Teilmenge wurde bereits gefunden!");
			return;
		}
		
		if(algorithm_speed == ALGORITHM_SPEED_INSTANT){

				if(!best_closure_bought){
				MinCut_MaxFlow.derive_max_closure(graph_to_solve);
			algorithm_mode = ALGORITHM_MODE_NONE;
			best_closure_bought = true;
			GUI_Graph.paint();
			GUI_Frame.updateLists();
				}
				else
					JOptionPane.showMessageDialog(null, "Die beste Teilmenge kann nur einmal gekauft werden.");
		}
		else{
			
			if(algorithm_mode != ALGORITHM_MODE_NONE && algorithm_mode != ALGORITHM_MODE_NONE_YET){
				JOptionPane.showMessageDialog(null, "Es läuft bereits ein Algorithmus, beenden sie diesen zuerst.");
				return;
			}
			
			algorithm_mode = ALGORITHM_MODE_CLOSURE;
			GUI_Frame.b1.setBackground(Color.GRAY);
			GUI_Frame.b3.setBackground(Color.GRAY);
			GUI_Frame.b4.setBackground(Color.GREEN);
			GUI_Frame.b7.setBackground(Color.GRAY);
		}
	}
	
	static HashMap<Vertex,Color> vertices_color = new HashMap<Vertex,Color>();
	static HashMap<Edge,Color> edges_color = new HashMap<Edge,Color>();
	static boolean fill_colors = false; static Graph old_graph;
	
	/*
	 * Ein einzelner Schritt des ausgewählten Algorithmus wird ausgeführt
	 */
	static void doStep(){
		if(isThreadRunning()) return;
		if(algorithm_mode == ALGORITHM_MODE_NONE || algorithm_mode == ALGORITHM_MODE_NONE_YET){
			JOptionPane.showMessageDialog(null, "Es wurde noch kein Algorithmus ausgewählt.");
			return;
		}
		
		fill_colors = true;
		
		if(best_closure_bought){
			GUI_Graph.displayGraphWithoutReposition(graph_to_solve);
			
			GUI_Frame.updateLists();
			
			GUI_Graph.paint();
			
			JOptionPane.showMessageDialog(null, "Die optimale Teilmenge wurde gefunden.");
			
			return;
		}
		
		
		GUI_Graph.clearMarkings();
		
		old_graph = graph_to_solve.visual_copy();
		
		HashSet<Vertex> old_vertices = new HashSet<Vertex>(old_graph.getVertices());
		HashSet<Edge> old_edges = new HashSet<Edge>(old_graph.getEdges());
		
		vertices_color.clear();
		edges_color.clear();
		
		GUI_Graph.displayGraphWithoutReposition(old_graph);
		
		switch(algorithm_mode){
		case ALGORITHM_MODE_RULES:			
			if(!Shorten_Rules.apply_rules_step(graph_to_solve)){
				JOptionPane.showMessageDialog(null, "Regeln anwenden beendet.");
				algorithm_mode = ALGORITHM_MODE_NONE;
				resetButtonColors();
			}
			break;
		case ALGORITHM_MODE_CYCLES:
			if(!Remove_Cycles.removeCycleStep(graph_to_solve)){
				JOptionPane.showMessageDialog(null, "Zyklen entfernen beendet.");
				algorithm_mode = ALGORITHM_MODE_NONE;
				resetButtonColors();
			}
			break;
		case ALGORITHM_MODE_CLOSURE:
			MinCut_MaxFlow.derive_max_closure(graph_to_solve);
			best_closure_bought = true;
			resetButtonColors();
			break;			
		case ALGORITHM_MODE_EDGES:
			if(!Remove_Redundant_Edges.removeRedundantEdgesStep(graph_to_solve)){
				JOptionPane.showMessageDialog(null, "Redundante Kanten Entfernen wurde beendet.");
				algorithm_mode = ALGORITHM_MODE_NONE;
				resetButtonColors();
			}
			break;
		}

		//Färbt Kanten in Kopie, die durch Verfahren entfernt wurden
	for(Edge edge : graph_to_solve.getEdges()){
		Vertex source = old_graph.getVertex(edge.getSourceVertex().getNumber());
		if(source == null) continue;
		old_edges.remove(source.getOutgoingEdge(old_graph.getVertex(edge.getTargetVertex().getNumber())));
	}
	
		for(Vertex ver : graph_to_solve.getVertices())
			old_vertices.remove(old_graph.getVertex(ver.getNumber()));
		
		//Färbt Knoten in Kopie, die durch Verfahren entfernt wurden
		if(old_vertices.size() > 0 && algorithm_mode != ALGORITHM_MODE_EDGES){
			for(Vertex ver : old_vertices)
			GUI_Graph.markVertex(ver,vertices_color.get(ver));
		}
		
		for(Edge edge : old_edges)
			GUI_Graph.markEdge(edge,Color.RED);
		
		if(!(old_vertices.size() > 0 || old_edges.size() > 0))
		GUI_Graph.displayGraphWithoutReposition(graph_to_solve);
		
		GUI_Frame.updateLists();
		
		GUI_Graph.paint();
		
		fill_colors = false;
	}
	
	private static void resetButtonColors(){
		GUI_Frame.b1.setBackground(null);
		GUI_Frame.b3.setBackground(null);
		GUI_Frame.b4.setBackground(null);
		GUI_Frame.b7.setBackground(null);
	}
	
	/*
	 * Der Graph wird sofort vollständig bearbeitet indem alle Verfahren in der richtigen Reihenfolge angewendet werden.
	 * Alle zu kaufenden Knoten werden ermittelt
	 * und ausgegeben. Die Lösung wird in eine Datei geschrieben.
	 */
	static void instaSolve(){
		 //Neuer Thread damit Programmstatus während des Programms aktualisiert werden kann
			new Thread(new solve_thread()).start();
	}
	
	/*
	 * Der Ursprungszustand des Graphen wird wiederhergestellt
	 */
	static void reset(){
		if(graph_source != null){
			graph_to_solve = Import_Graph.readGraph(graph_source);
			GUI_Graph.displayGraph(graph_to_solve);
			GUI_Graph.paint();
			GUI_Frame.frame_state_2_setup();
			GUI_Frame.updateLists();
			resetButtonColors();
		}
		else
			JOptionPane.showMessageDialog(null,"Ein generierter Graph kann nicht resettet werden.");
		setup();
	}

	/*
	 * Initialisierung
	 */
	private static void setup(){
		algorithm_mode = ALGORITHM_MODE_NONE_YET;
		best_closure_bought = false;
		algorithm_speed = ALGORITHM_SPEED_STEP;
		Graph.reset_statistics();
	}
	
	static void start(){
		setup();
	}
	
}

class solve_thread implements Runnable{

	static boolean running;
	/*
	 * Bestimmt die optimale Teilmenge im von GUI geladenen 
	 * Graphen und schreibt die Lösung in eine Datei.
	 */
	public void run() {
		if(running) return;
		running = true;
		if(Graph_Editing_Control.algorithm_mode != Graph_Editing_Control.ALGORITHM_MODE_NONE_YET){
			JOptionPane.showMessageDialog(null,"Sie können diese Funktion nur dann verwenden, wenn der Graph noch nicht bearbeitet wurde [RESET]");
		}
		else{
			Graph_Editing_Control.best_closure_bought = true;
			//Aufruf der Lösungsmethode
			Solve_Graph.buyOptimalClosure(Graph_Editing_Control.graph_to_solve,true);
			GUI_Frame.status.setText("Lösung gefunden.\nKnotenanzahl: "+Graph_Editing_Control.graph_to_solve.getBoughtVertices().size()+"\nMengenwert: "+Graph_Editing_Control.graph_to_solve.getTotalAmountOfBoughtVertices());
			GUI_Graph.paint();
			Graph_Editing_Control.algorithm_mode = Graph_Editing_Control.ALGORITHM_MODE_NONE;
			GUI_Frame.updateLists();
			Graph_Editing_Control.algorithm_mode = Graph_Editing_Control.ALGORITHM_MODE_NONE;
		}
		running = false;
	}
	
}
