import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GUI_Graph extends JPanel{
	
	/*
	 * Klasse zur grafischen Ausgabe eines Graphen.
	 */
	
		private static final long serialVersionUID = 8585089991162884765L;

		static final GUI_Graph GRAPHICS = new GUI_Graph();
		
		static final Events events = new Events();
		
		private static BufferedImage BACKGROUND;
		
		GUI_Graph(){
			try {
				BACKGROUND = ImageIO.read(getClass().getClassLoader().getResourceAsStream("BACKGROUND.jpg"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		private static int vertex_distance = 50;
		
		/*
		 * Setzen des Abstandes der einzelnen Knoten
		 */		
		static void setVertexDistance(int vertex_distance){
			if(vertex_distance < 1) return;
				GUI_Graph.vertex_distance = vertex_distance;
		}
		
		private static int vertex_size = 50;
		
		private static int vertex_frame_size = vertex_size/5;
		
		/*
		 * Setzen der Größe eines einzelnen Knoten
		 */		
		static void setVertexSize(int vertex_size){
			if(vertex_size < 5 ) return;
				GUI_Graph.vertex_size = vertex_size;
				GUI_Graph.vertex_frame_size = vertex_size/5;
		}
		
		static int getVertexSize(){
			return vertex_size;
		}
		
		private static float scroll_speed = 1;
		
		/*
		 * Setzen der Geschwindigkeit
		 * mit der man sich im Graphen
		 * bewegen kann
		 */		
		static void setScrollSpeed(float scroll_speed_in_percent){
			if(scroll_speed_in_percent < 1 || scroll_speed_in_percent > 100)
				return;
			GUI_Graph.scroll_speed = 0.02f*scroll_speed_in_percent;
		}
		
		private static float zoom_speed = 1.1f;
		
		/*
		 * Setzen der Geschwindigkeit
		 * mit der man herein- und heraus-zoomen kann.

		 */		
		static void setZoomSpeed(float zoom_speed_percent){
			if(zoom_speed_percent < 1 || zoom_speed_percent > 100)
				return;
			zoom_speed = 0.002f*zoom_speed_percent+1;
		}
		
		private static boolean show_vertex_value = true;
		
		/*
		 * Wechsel zwischen Anzeigen des Knotenwertes und nicht Anzeigen
		 */		
		static void toggleShowVertexValue(){
			show_vertex_value = !show_vertex_value;
		}
		
		private static boolean show_vertex_index = true;
		
		/*
		 * Wechsel zwischen Anzeigen der Knotenummern und nicht Anzeigen
		 */	
		static void toggleShowVertexIndex(){
			show_vertex_index = !show_vertex_index;
		}
			
		
		static final boolean VERTEX_CIRCLE_FORMATION = false;
		static final boolean VERTEX_SQUARE_FORMATION = true;
		
		private static boolean vertex_formation = VERTEX_SQUARE_FORMATION;
		
		/*
		 * Die Formation in denen die Knoten geordnet sind wird geändert
		 */
		
		static void changeVertexFormation(){
			vertex_formation = !vertex_formation;
			positionVertices();
		}
		
		static void setVertexFormation(boolean formation){
			vertex_formation = formation;
			positionVertices();
		}
		
	
	private static Graph displayed_graph = new Graph();
	
	static final int MAX_VERTEX_COUNT = 10000, MAX_EDGE_COUNT = 10000;
	
	static boolean is_graph_too_big = false;
	
	/*
	 * Graph in das GUI geladen. Knoten
	 * werden Positionen zugeordnet.
	 */
	static void displayGraph(Graph g){
		GUI_Frame.status.setText("");
		displayed_graph = g;
		if(!isGraphTooBig(g))
		positionVertices();
		is_graph_too_big = isGraphTooBig(g);
	}
	
	static void displayGraphWithoutReposition(Graph g){
		
		if(isGraphTooBig(g))
			return;
		
		for(Vertex vertex : g.getVertices()){
			int[] pos = vertices_positions.remove(displayed_graph.getVertex(vertex.getNumber()));
			vertices_positions.put(vertex,Arrays.copyOf(pos, pos.length));
		}
		
		displayed_graph = g;
		is_graph_too_big = isGraphTooBig(g);
	}
	
	private static boolean isGraphTooBig(Graph graph){
		return graph.getVertices().size() >= MAX_VERTEX_COUNT || graph.getEdges().size() >= MAX_EDGE_COUNT;
	}
	
	static Graph getGraph(){
		return displayed_graph;
	}
	
	
	private static final HashMap<Vertex,int[]> vertices_positions = new HashMap<Vertex,int[]>();
	
	private static HashMap<Vertex,Color> marked_vertices = new HashMap<Vertex,Color>();	
	private static HashMap<Edge,Color> marked_edges = new HashMap<Edge,Color>();
	
	static void markVertex(Vertex ver, Color color){
		marked_vertices.put(ver,color);
	}
	
	static void markEdge(Edge edge, Color color){
		marked_edges.put(edge, color);
	}
	
	static void clearMarkings(){
		marked_vertices.clear();
		marked_edges.clear();
	}
	
	private static int matrix_size;
	
	static int getMatrixSize(){
		return matrix_size;
	}
	
	/*
	 * Positionieren aller Knoten
	 */	
	static void positionVertices(){
		vertices_positions.clear();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(displayed_graph.getVertices());
		
		if(vertex_formation == VERTEX_SQUARE_FORMATION){
			matrix_size =(int) (Math.pow(Math.sqrt(vertices.size())*vertex_distance,2));
			int sg =(int) (Math.sqrt(vertices.size())); //Seitengröße
			
			for(int i = 0; i < vertices.size(); i++){
				int row = i/sg;
				int column = i%sg;
				vertices_positions.put(vertices.get(i),new int[] {column*vertex_distance,row*vertex_distance});
			}
			
			if(matrix_size < vertex_size*5)
				zoom_level = vertex_size*5;
			else
				zoom_level = (int) (Math.sqrt(matrix_size));
		}
		
		else if(vertex_formation == VERTEX_CIRCLE_FORMATION){
			double radius = (vertices.size()*(vertex_distance+vertex_size))/(2*Math.PI); //u = (knotendichte+knotengrosse)*knotenanzahl | r = u/(2*pi)
			matrix_size = (int) (Math.pow(radius*2,2));
			double gaps = 2*Math.PI/(float)vertices.size();
			int middlepoint_x = (int) radius;
			int middlepoint_y = (int) radius;
			for(int i = 0;i < vertices.size(); i++){
				int x = (int) (middlepoint_x+Math.cos(i*gaps)*radius);
				int y = (int) (middlepoint_y+Math.sin(i*gaps)*radius);
				vertices_positions.put(vertices.get(i), new int[] {x,y});
			}
				zoom_level = (int) (radius*2);			
		}
		
		x_scroll = 0;
		y_scroll = 0;
	
	}
	
	static void setVertexPositionXY(float value,int x, int y){
		vertices_positions.put(displayed_graph.addVertex(value), new int[] {x*vertex_distance,y*vertex_distance});
	}
	
	static void updateMatrixSize(){
		matrix_size = (int) (Math.pow(vertices_positions.size(), 2));
	}
	
	static void paint(){
		GRAPHICS.repaint();
	}
	
	/*
	 * Malt eine Kante
	 */
	private void drawEdge(Graphics g,Vertex target,float factor_X, float factor_Y, int source_x, int source_y){
		Graphics2D g2 = (Graphics2D) g;
		int[] target_coordinates = vertices_positions.get(target);
		double target_x =(int) ((target_coordinates[0]-x_scroll)*factor_X+(vertex_size*factor_X/2));
		double target_y =(int) ((target_coordinates[1]-y_scroll)*factor_Y+(vertex_size*factor_Y/2));
		double radius = (vertex_size/2+vertex_frame_size/2)*factor_X;
		double vec_x = target_x-source_x; //create vector from source to target
		double vec_y = target_y-source_y; //create vector from source to target
		double vec_length = Math.sqrt(vec_x*vec_x+vec_y*vec_y);
		if(vec_length != 0){
			vec_x /= vec_length; //make vector length 1
			vec_y /= vec_length; //make vector length 1
			target_x = source_x+(vec_length-radius)*vec_x;
			target_y = source_y+(vec_length-radius)*vec_y;
			g2.drawLine(source_x,source_y,(int) target_x,(int)target_y);
			int[] arrow_head_x = new int[3];
			int[] arrow_head_y = new int[3];
			arrow_head_x[0] = (int)target_x;
			arrow_head_y[0] = (int)target_y;
			final double arrow_factor = 0.5;
			arrow_head_x[1] = (int) (target_x+(-vec_x+vec_y/2)*(radius*arrow_factor));
			arrow_head_y[1] = (int) (target_y+(-vec_y-vec_x/2)*(radius*arrow_factor));
			arrow_head_x[2] = (int) (target_x+(-vec_x-vec_y/2)*(radius*arrow_factor));
			arrow_head_y[2] = (int) (target_y+(-vec_y+vec_x/2)*(radius*arrow_factor));

			g2.fillPolygon(arrow_head_x, arrow_head_y, 3);
		}
		else{
			g2.drawLine(source_x,source_y,(int)target_x,(int)target_y);
		}		
	}
	
	/*
	 * Malen des gesamten Graphen
	 */	
	public void paintComponent(Graphics g){
		g.clearRect(0,0,getWidth(),getHeight());
		g.drawImage(BACKGROUND,0,0,getWidth(), getHeight(),null);
		
		if(displayed_graph == null || is_graph_too_big)
			return;
		
		HashSet<Vertex> vertices = displayed_graph.getVertices();
		float factor_X = getWidth()/zoom_level;
		float factor_Y = getHeight()/zoom_level;
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setStroke(new BasicStroke(vertex_frame_size/2*factor_X));
			
		
					
			for(Vertex ver : vertices){
				
				int[] coordinates = vertices_positions.get(ver);
				int screen_x =(int) ((coordinates[0]-x_scroll)*factor_X+(vertex_size*factor_X/2));
				int screen_y =(int) ((coordinates[1]-y_scroll)*factor_Y+(vertex_size*factor_Y/2));
			
				if(!marked_vertices.containsKey(ver))
					g.setColor(Color.BLACK);
				else
					g.setColor(marked_vertices.get(ver));
				
				HashSet<Edge> outgoing_edges = ver.getOutgoingEdges();
				for(Edge i : outgoing_edges){
					if(!i.isInverseEdge()){
						if(marked_edges.containsKey(i))
							g.setColor(marked_edges.get(i));
						else
							g.setColor(Color.BLACK);
					drawEdge(g,i.getTargetVertex(),factor_X,factor_Y,screen_x,screen_y);
					}
				}
				
			}
		
		for(Vertex ver : vertices){
			
			int[] coordinates = vertices_positions.get(ver);
			
			if(!new Rectangle((int)x_scroll,(int)y_scroll,(int)zoom_level,(int)zoom_level).intersects(new Rectangle(coordinates[0],coordinates[1],vertex_size,vertex_size)))
				continue;
			
			int screen_x =(int) ((coordinates[0]-x_scroll)*factor_X);
			int screen_y =(int) ((coordinates[1]-y_scroll)*factor_Y);
			
			boolean vertex_marked = marked_vertices.containsKey(ver);
			
			if(vertex_marked)
				g.setColor(marked_vertices.get(ver));
			else
				g.setColor(Color.WHITE);
			g.fillOval(screen_x,screen_y,(int) (vertex_size*factor_X),(int) (vertex_size*factor_Y));
			g.setColor(Color.BLACK);
			g2.setStroke(new BasicStroke(vertex_frame_size*factor_X/2));
			g.drawOval(screen_x, screen_y,(int) ( vertex_size*factor_X),(int) (vertex_size*factor_Y));
	
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial",Font.PLAIN,(int) (vertex_frame_size*factor_X)));
			if(show_vertex_index)
			g.drawString("Nr. "+ver.getNumber(),(int) (screen_x+vertex_size*factor_X/4),(int) (screen_y+vertex_size*factor_X/3));
			if(show_vertex_value)
			g.drawString(""+ver.getValue(),screen_x+(int) ( vertex_size*factor_X/3),screen_y+(int) (vertex_size*factor_X*0.6));
				
		}
		
	}
	
	
	 private static float zoom_level = 1f;
	
	 static float getZoomLevel(){
		 return zoom_level;
	 }
	 
	 /*
	  * Hereinzoomen
	  */ 
	 static void zoom_in(boolean kamera_access){
		if(camera_animation.is_camera_animating() && !kamera_access)
			return;
		float old_zoom_level = zoom_level;
		zoom_level/=zoom_speed;
		if(zoom_level < vertex_size)
			zoom_level = vertex_size;
		x_scroll+= (old_zoom_level-zoom_level)/2;
		y_scroll+= (old_zoom_level-zoom_level)/2;
	}
	
	 /*
	  * Herauszoomen
	  */ 
	static void zoom_out(boolean kamera_access){
		if(camera_animation.is_camera_animating() && !kamera_access)
			return;
		float old_zoom_level = zoom_level;
		zoom_level*=zoom_speed;
		if(zoom_level > matrix_size){
			zoom_level = matrix_size;
		}
		x_scroll-= (zoom_level-old_zoom_level)/2;
		y_scroll-= (zoom_level-old_zoom_level)/2;
	}
	
	private static float y_scroll;
	
	private static float x_scroll;
	
	/*
	 * Verschiebungen des Graphen
	 */	
	static void scroll_right(){
		float percent = (float)GRAPHICS.getWidth()/100;
		float factor = zoom_level/(float)GRAPHICS.getWidth();
		x_scroll += scroll_speed*percent*factor;
	}
	
	static void scroll_left(){
		float percent = (float)GRAPHICS.getWidth()/100;
		float factor = zoom_level/(float)GRAPHICS.getWidth();
		x_scroll -= scroll_speed*percent*factor;
	}
	
	static void scroll_down(){
		float percent = (float)GRAPHICS.getHeight()/100;
		float factor = zoom_level/(float)GRAPHICS.getHeight();
		y_scroll += scroll_speed*percent*factor;
	}
	
	static void scroll_up(){
		float percent = (float)GRAPHICS.getHeight()/100;
		float factor = zoom_level/(float)GRAPHICS.getHeight();
		y_scroll += -scroll_speed*percent*factor;
	}
	
	/*
	 * Kamera wird auf einen Knoten zentriert
	 */
	private static void visit_vertex(Vertex vertex){
		if(!displayed_graph.getVertices().contains(vertex))
			return;
		
		int[] vertex_position = vertices_positions.get(vertex);
		x_scroll = vertex_position[0];
		y_scroll = vertex_position[1];
		zoom_level = vertex_size;
		float old_zoom_level = zoom_level;
		zoom_level*=10;
		if(zoom_level > matrix_size){
			zoom_level = matrix_size;
		}
		x_scroll -= (zoom_level-old_zoom_level)/2;
		y_scroll -= (zoom_level-old_zoom_level)/2;
		
	}
	
	static void visit_vertex_user(){
		int vertex_number = Integer.parseInt(JOptionPane.showInputDialog("Knotennummer eingeben."));
		HashSet<Vertex> vertices = displayed_graph.getVertices();
		for(Vertex ver : vertices){
			if(ver.getNumber() == vertex_number){
				visit_vertex(ver);
				return;
			}
		}
	}
	
	//Kamerafahrt: Herauszoomen
	static void max_zoom(){
		new Thread(new camera_animation((byte)0)).start();
	}
	
	//Kamerafahrt: Hereinzoomen
	static void min_zoom(){
		new Thread(new camera_animation((byte)1)).start();
	}
	
}

class camera_animation implements Runnable{

	/*
	 * Klasse für Kamerafahrten
	 */	
	private static final int zoom_tempo = 50;
	
	/*
	 * Kamera Konstanten
	 * 
	 * MAX_ZOOM: Die Kamera zoomt soweit raus wie möglich
	 * 
	 * MIN_ZOOM: Die Kamera zoomt so nah wie möglich
	 */
	
	private static final byte MAX_ZOOM = 0;
	private static final byte MIN_ZOOM = 1;
	
	private final byte modus;
	
	camera_animation(byte modus){
		this.modus = modus;
	}
	
	private static boolean running;
	
	static boolean is_camera_animating(){
		return running;
	}
	
	private void max_zoom(){
		
		while(GUI_Graph.getZoomLevel() < GUI_Graph.getMatrixSize()){
			
			try{
				GUI_Graph.zoom_out(true);
				GUI_Graph.paint();
				Thread.sleep(zoom_tempo);
			}
			catch(Exception e){e.printStackTrace();}	
			
		}		
	}
	
	private void min_zoom(){
		
		while(true){
		if(GUI_Graph.getZoomLevel() > GUI_Graph.getVertexSize()*2){			
			try{
				GUI_Graph.zoom_in(true);
				GUI_Graph.paint();
				Thread.sleep(zoom_tempo);
			}
			catch(Exception e){e.printStackTrace();}
		}
		else
			return;
		
		}
	}
	
	public void run() {
		if(running)
			return;
		
		running = true;
		
		switch(modus){
		case MAX_ZOOM:
			max_zoom();
			break;
		case MIN_ZOOM:
			min_zoom();
			break;
		}
		
		running = false;
	}
}

class Events implements MouseMotionListener, MouseWheelListener{
	
	/*
	 * Klasse zum Interagieren mit dem Graph-Viewer
	 */
	
	private static int[] last_point = new int[2];
	private static byte ticks = 0;
		
	/*
	 * Bewegen im Graphen
	 */	
	public void mouseDragged(MouseEvent e) {
		ticks++;
		if(ticks < 3)
			return;
		
		int diff_x = Math.abs(last_point[0]-e.getX());
		int diff_y = Math.abs(last_point[1]-e.getY());
		ticks = 0;
			
		if(diff_x > 1){
			
		if(e.getX()-last_point[0] < 0)
			GUI_Graph.scroll_right();
		else
			GUI_Graph.scroll_left();
		
			}
			
			if(diff_y > 1){		
				if(e.getY()-last_point[1] > 0)
					GUI_Graph.scroll_up();
				else
					GUI_Graph.scroll_down();		
			}
		
		last_point[0] = e.getX();
		last_point[1] = e.getY();
		
		GUI_Graph.paint();
		
	}

	public void mouseMoved(MouseEvent e) {}

	//MouseWheel Event
	
	/*
	 * Zooms
	 */
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation() == -1)
			GUI_Graph.zoom_in(false);
		else
			GUI_Graph.zoom_out(false);
		
		GUI_Graph.paint();
	}
	
}

