import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

public class Export_Solution {

	/*
	 * Schreibt die Lösung der Aufgabe im gewünschten Format in eine Datei. Die
	 * Datei wird im selben Verzeichnis erstellt, wie die der eingelesenen Datei.
	 */
	static void printSolution(Graph graph) {
		// Aufbau des Ausgabestrings
		ArrayList<Integer> bought_vertices = graph.getBoughtVertices();
		Collections.sort(bought_vertices);

		StringBuilder solution_string_builder = new StringBuilder(bought_vertices.size() + 20);
		// Statistiken
		solution_string_builder.append("\n# Regel 1a: " + Graph.statistics_rule_1a);
		solution_string_builder.append("\n# Regel 1b: " + Graph.statistics_rule_1b);
		solution_string_builder.append("\n# Regel 2a: " + Graph.statistics_rule_2a);
		solution_string_builder.append("\n# Regel 2b: " + Graph.statistics_rule_2b);
		solution_string_builder.append("\n# Zyklen: " + Graph.statistics_cycles);
		solution_string_builder.append("\n# Redundante Kanten: " + Graph.statistics_edges);
		solution_string_builder.append("\n# Teilgraphen: " + Graph.statistics_split);
		// Anzahl der gekauften Knoten/Firmen
		solution_string_builder.append("\n# Anzahl der Knoten in der Teilmenge");
		solution_string_builder.append("\n" + bought_vertices.size());
		// Gesamtwert
		solution_string_builder.append("\n# Gesamtwert der Knoten");
		solution_string_builder.append("\n" + graph.getTotalAmountOfBoughtVertices());
		// Alle gekauften Knoten
		solution_string_builder.append("\n# Nummern der Knoten");
		for (int number_of_bought_vertex : bought_vertices)
			solution_string_builder.append("\n" + number_of_bought_vertex);

		String solution = solution_string_builder.toString();
		System.out.println(solution); // Ausgabe in Konsole

		if (Graph_Editing_Control.graph_source == null)
			return;

		// Ausgabe in Datei
		File output_file = new File(Graph_Editing_Control.graph_source.getAbsolutePath() + "_solution.txt");

		if (!output_file.exists()) {
			try {
				output_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter writeFile = null;
		BufferedWriter writer = null;
		try {
			writeFile = new FileWriter(output_file);
			writer = new BufferedWriter(writeFile);
			writer.write(solution);

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

		JOptionPane.showMessageDialog(null,
				"Eine Datei mit der Lösung wurde hier erstellt: " + output_file.getAbsolutePath());

	}

}
