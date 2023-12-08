import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Import_Graph {
	
	public static final String DIRECTORY = "graphen";
	
	/*
	 * L�sst Nutzer eine Datei ausw�hlen und �bergibt die File-Variable
	 * an die Einleseroutine weiter, merkt sich den Dateinamen und �bergibt den erzeugten Graph zur�ck.
	 */
	static Graph import_graph(){
		Runtime.getRuntime().gc();
		File file = null;
		 Graph graph = null;
		 //Datei �ber Benutzereingabe erhalten
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir")+"/"+DIRECTORY);
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION){ 
          file = fileChooser.getSelectedFile();
          //Ausgew�hlte Datei einlesen
          graph = readGraph(file);
        }
        else{
       	 JOptionPane.showMessageDialog(null, "Du hast keinen Graphen ausgew�hlt.");
       	 return Graph_Editing_Control.graph_to_solve;
        }
        
      
       if(graph != null){
    	   Graph_Editing_Control.graph_source = file;
    	   return graph;
       }
       else
    	   return Graph_Editing_Control.graph_to_solve;		
	}
	
	/*
	 * Liest einen Graph aus einer Datei ein.
	 */	
	static Graph readGraph(File file){
		//Einfacher Konsistenzcheck
		if (!file.getName().contains(".txt")) {
			JOptionPane.showMessageDialog(null, "Die ausgew�hlte Datei muss eine .txt Datei sein.");
			return null;
		}
		//Einfache Vorbelegung f�r die GUI
		if (file.getName().contains("Quadrat")) {
			GUI_Graph.setVertexFormation(GUI_Graph.VERTEX_SQUARE_FORMATION);
		} else if (file.getName().contains("Kreis") || file.getName().contains("Zufall")) {
			GUI_Graph.setVertexFormation(GUI_Graph.VERTEX_CIRCLE_FORMATION);
		}
		
		
		Graph graph = new Graph();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			int vertex_amount = -1, vertex_at = -1;
			//Zeilenweise interpretieren
			while (line != null) { 

				if (line.charAt(0) != '#') { //Kommetarzeilen ignorieren

					if (vertex_amount == -1) { //Warten auf Zeile mit Knotenanzahl
						vertex_amount = Integer.parseInt(line);
						vertex_at = 1;
					}
					else if (vertex_at != -1 && vertex_at <= vertex_amount) { //Knoten einlesen bis Knotenanzahl erreicht
						vertex_at++;
						 //Knoten in Graph einf�gen
						graph.addVertex((int) Float.parseFloat(line.split(" ")[1]), (int) Float.parseFloat(line.split(" ")[0]));
					}
					else if (vertex_at > vertex_amount) { //Kanten einlesen bis Ende der Datei erreicht ist
						String[] split = line.split(" ");
						if (!split[0].equals(split[1]))
							graph.addEdge(graph.getVertex((int) Float.parseFloat(split[0])), //Kante in Graph einf�gen
									graph.getVertex((int) Float.parseFloat(split[1])));
					}

				}

				line = reader.readLine();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception x) {
				x.printStackTrace();
			}
		}

		return graph;
	}
	
}
