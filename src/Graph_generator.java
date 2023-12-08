import java.util.Random;

import javax.swing.JOptionPane;

public class Graph_generator {

	/*
	 * Erzeugen eines zufälligen Test Graph
	 */
	static Graph random_graph_by_user(){
		Runtime.getRuntime().gc();
		int vertex_count,edge_count, seed;
		boolean contains_cycles;
		Graph random_graph = null;
		//Rahmenbedingungen für den zu erzeugenden Graph
		try{
			vertex_count = Integer.parseInt(JOptionPane.showInputDialog(null,"Knotenanzahl"));
			if(vertex_count >= GUI_Graph.MAX_VERTEX_COUNT){
				JOptionPane.showMessageDialog(null, "Ein Graph mit mehr als "+GUI_Graph.MAX_VERTEX_COUNT+" Knotenn wird nicht mehr grafisch ausgegeben, kann aber noch gelöst werden.");
			}
			edge_count = Integer.parseInt(JOptionPane.showInputDialog(null, "Kantenanzahl"));
			if(edge_count >= GUI_Graph.MAX_EDGE_COUNT){
				JOptionPane.showMessageDialog(null, "Ein Graph mit mehr als "+GUI_Graph.MAX_EDGE_COUNT+" Kanten wird nicht mehr grafisch ausgegeben, kann aber noch gelöst werden.");
			}
			contains_cycles = JOptionPane.showConfirmDialog(null, "Darf der Graph Zyklen enthalten?") == 0;
			seed = Integer.parseInt(JOptionPane.showInputDialog(null, "Seed"));
			//Graph erzeugen
			random_graph = create_random_graph(vertex_count, edge_count, contains_cycles,seed);
			//Graph speicher, falls erwünscht
			if(JOptionPane.showConfirmDialog(null, "Erzeugten Graphen speichern?") == 0){
				String save_name = JOptionPane.showInputDialog("Wie soll die Datei mit dem erzeugten Graphen heißen?");
				Graph.save(random_graph, save_name);
			}
		}
		catch(Exception ex){
			return null;
		}
		
		return random_graph;
	}
	
	/*
	 * Erzeugt einen zufälligen Graph 
	 * zu gegebenen Rahmenbedingungen.
	 */
	private static Graph create_random_graph(int vertices, int edges, boolean contains_cycles, int seed){
		Random rand = new Random(seed);
		
		Graph random_graph = new Graph();
		
		for(int i = 0; i < vertices; i++)
			random_graph.addVertex((int) (rand.nextFloat()*100-50),i);
		for(int i = 0; i < edges; i++){
			Vertex v1 = random_graph.getVertex((int) (rand.nextFloat()*vertices));
			Vertex v2;
			if(contains_cycles)
				v2 = random_graph.getVertex((int) (rand.nextFloat()*vertices));
			else
				v2 = random_graph.getVertex((int) (v1.getNumber()+rand.nextFloat()*(vertices-v1.getNumber())));
			if(v1 != v2)
			random_graph.addEdge(v1,v2);
		}

		return random_graph;
	}
	
	
}
