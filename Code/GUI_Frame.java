
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GUI_Frame extends JFrame{
	
	private static final long serialVersionUID = 1L;
			
	private static final int FRAME_WIDTH = 1100, FRAME_HEIGHT = 700;

	/*
	 * Programmeinstieg
	 */
	public static void main(String[] args){		
		frame = new GUI_Frame();
		frame_state_1_setup();
	}
	
	private static GUI_Frame frame;
	
	/*
	 * Erstellen des Fensters
	 */
	private GUI_Frame(){	
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(FRAME_WIDTH,FRAME_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setTitle("BwInf Runde 2 - Aufgabe 1 - Rosinen picken");
		
		GUI_Graph.GRAPHICS.addMouseMotionListener(GUI_Graph.events);
		GUI_Graph.GRAPHICS.addMouseWheelListener(GUI_Graph.events);
		
		setVisible(true);		
	}
	
	/*
	 * Wechseln zum Screen, in welchem der Algorithmus
	 * auf den Graphen angewendet werden kann
	 */	
	static void frame_state_2_setup(){
		
		Graph_Editing_Control.in_algorithm = true;
		
		Graph_Editing_Control.start();
		
		frame.getContentPane().removeAll();
		
		GUI_Graph.GRAPHICS.setBounds(310,100,460,460);
		frame.add(GUI_Graph.GRAPHICS);
		
		frame.programStatusSetup();
		
		frame.AlgorithmSpeedChoiceSetup();
		
		frame.verticesListDisplaySetup();
		
		frame.verticesDisplayChoicesSetup();
		
		frame.cameraMovementsSetup();
		
		frame.decorationsSetup();
		
		frame.ResetCancelButtonSetup();
		
		frame.boughtVerticesSetup();
		
		frame.graphViewingSpeedChoicesSetup();
		
		frame.AlgorithmChoiceSetup();
		
		frame.revalidate();
		frame.repaint();
	
	}
	
	/*
	 * Wechseln zum Screen, in welchem
	 * ein Graph ausgewählt werden kann.
	 */
	static void frame_state_1_setup(){
		
		Graph_Editing_Control.in_algorithm = false;
		
		frame.getContentPane().removeAll();
		
		GUI_Graph.GRAPHICS.setBounds(310,100,460,460);
		frame.add(GUI_Graph.GRAPHICS);
		
		frame.programStatusSetup();
		
		frame.graphViewingSpeedChoicesSetup();
		
		frame.verticesListDisplaySetup();
		
		frame.verticesDisplayChoicesSetup();
		
		frame.cameraMovementsSetup();
		
		frame.decorationsSetup();
		
		frame.PreAlgorithmChoicesSetup();
		
		frame.revalidate();
		frame.repaint();
	}
	
	/*
	 * Folgende Methoden fügen dem Fenster
	 * Elemente hinzu.
	 */
	
	private static JScrollPane to_buy, vertex_list;
	
	private static void updateVertexList(){
		frame.remove(vertex_list);
		frame.verticesListDisplaySetup();
		frame.revalidate();
		frame.repaint();
	}
	
	private static void updateBoughtList(){
		if(to_buy != null && Graph_Editing_Control.in_algorithm){
		frame.remove(to_buy);
		frame.boughtVerticesSetup();
		frame.revalidate();
		frame.repaint();
		}
	}
	
	static void updateLists(){
		updateVertexList();
		updateBoughtList();
	}
	
	public static JButton b1,b3,b4,b7;
	
	private void AlgorithmChoiceSetup(){
		b1 = new JButton("1) Zyklen");
		b3 = new JButton("2) Regeln");
		b4 = new JButton("4) Closures");
		JButton b5 = new JButton("Weiter");
		JButton b6 = new JButton("Alles sofort");
		b7 = new JButton("3) Redundanzen");

	    b1.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Graph_Editing_Control.clickCycles();
				}
	        });
		
		
	    b3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						Graph_Editing_Control.clickRules();
					}
		        });
		
		b4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph_Editing_Control.clickClosure();
			}
        });
		
		b5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph_Editing_Control.doStep();
			}
        });
		
		b6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph_Editing_Control.instaSolve();
			}
        });
		
		b7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Graph_Editing_Control.clickEdges();
			}
        });
		
		b1.setBounds(50,170,100,50);
		b3.setBounds(160,170,100,50);
		b7.setBounds(50,235,100,50);
		b5.setBounds(55,305,200,55);
		b6.setBounds(55,380,200,55);
		b4.setBounds(160,235,100,50);
		
		add(b1);
		add(b3);
		add(b4);
		add(b5);
		add(b6);
		add(b7);
	}
	
	private void boughtVerticesSetup(){
		ArrayList<Integer> vertices = new ArrayList<Integer>(Graph_Editing_Control.graph_to_solve.getBoughtVertices());
		String[] vertex_list = new String[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			vertex_list[i] = "Nr."+vertices.get(i);
		
		JList<String> list = new JList<String>(vertex_list);
		to_buy = new JScrollPane();
		to_buy.getViewport().add(list);
		to_buy.setBounds(100, 480, 100, 70);
		add(to_buy);
		
		Font font = new Font("Arial", Font.BOLD, 12);
		JLabel textLabel = new JLabel("Kaufenliste");
		textLabel.setFont(font);
		textLabel.setBounds(115, 435, 75, 50);
		
		add(textLabel);
		
	}
	
	private void AlgorithmSpeedChoiceSetup(){
		ButtonGroup group = new ButtonGroup();

        final JRadioButton rb1 = new JRadioButton("Sofort");
        final JRadioButton rb2 = new JRadioButton("Schrittweise",true);

        group.add(rb1);
        group.add(rb2);   

        rb1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	if(rb1.isSelected()) Graph_Editing_Control.setAlgorithmSpeed(Graph_Editing_Control.ALGORITHM_SPEED_INSTANT);
            	if(rb2.isSelected()) Graph_Editing_Control.setAlgorithmSpeed(Graph_Editing_Control.ALGORITHM_SPEED_STEP);
            }
        });
        
        rb2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
            	if(rb1.isSelected()) Graph_Editing_Control.setAlgorithmSpeed(Graph_Editing_Control.ALGORITHM_SPEED_INSTANT);
            	if(rb2.isSelected()) Graph_Editing_Control.setAlgorithmSpeed(Graph_Editing_Control.ALGORITHM_SPEED_STEP);
            }
        });
        
        rb1.setBounds(50,120,90,30);
        rb2.setBounds(150,120,110,30);
        
        add(rb1);
        add(rb2);
	}
	
	private void ResetCancelButtonSetup(){
		JButton jb1 = new JButton("Abbruch");
		JButton jb2 = new JButton("Reset");
		
		jb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	Graph_Editing_Control.reset();
            }
        });
		
		jb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	Graph_Editing_Control.cancel();
            }
        });
		
		jb1.setBounds(50, 50, 200, 50);
		jb2.setBounds(50, 570, 200, 50);
		
		add(jb1);
		add(jb2);
	}
	
	private void PreAlgorithmChoicesSetup(){
		JButton jb1 = new JButton("Zufälligen Graph");
		JButton jb2 = new JButton("Graph importieren");
		JButton jb3 = new JButton("Darstellung ändern");
		JButton jb4 = new JButton("Algorithmus starten");
		
		jb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	Graph random = Graph_generator.random_graph_by_user();
            	if(random != null){
            		Graph_Editing_Control.setGraph(random);
            	}
            }
        });
			
		
		jb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	Graph_Editing_Control.import_graph();
            }
        });
	
		jb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	GUI_Graph.changeVertexFormation();
            	GUI_Graph.paint();
            }
        });
			
		jb4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	frame_state_2_setup();
            }
        });
		
		jb1.setBounds(50, 50, 200, 50);
		jb2.setBounds(50, 270, 200, 50);
		jb3.setBounds(50, 160, 200, 50);
		jb4.setBounds(50, 380, 200, 50);
		
		add(jb1);
		add(jb2);
		add(jb3);
		add(jb4);
		
		final JSlider js1 = new JSlider(2,100,50); //dichte
		final JSlider js2 = new JSlider(5,100,50); //grosse
		
		js1.setMinorTickSpacing(5);
		js1.setMajorTickSpacing(10);
		js1.setPaintTicks(true);
		js1.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  GUI_Graph.setVertexDistance(js1.getValue());
		    	  GUI_Graph.positionVertices();
		    	  GUI_Graph.paint();
		      }
		    });
		
		js2.setMinorTickSpacing(5);
		js2.setMajorTickSpacing(10);
		js2.setPaintTicks(true);
		js2.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  GUI_Graph.setVertexSize(js2.getValue());
		    	  GUI_Graph.positionVertices();
		    	  GUI_Graph.paint();
		      }
		    });

		js2.setBounds(50,570,180,50);
		js1.setBounds(50,500,180,30);
		
		add(js1);
		add(js2);
		
		
		Font font = new Font("Arial", Font.BOLD, 12);
		JLabel tl1 = new JLabel("Knotengröße");
		JLabel tl2 = new JLabel("Knotendichte");
		
		tl1.setFont(font);
		tl2.setFont(font);
		
		tl1.setBounds(100,540, 75, 50);
		tl2.setBounds(100,460, 75, 50);
	
		add(tl1);
		add(tl2);

	}
	
	private void decorationsSetup(){
		Rechteck r1 = new Rechteck();
		Rechteck r2 = new Rechteck();
		Rechteck r3 = new Rechteck();
		Rechteck r4 = new Rechteck();
		Rechteck r5 = new Rechteck();
		Rechteck r6 = new Rechteck();
		Rechteck r7 = new Rechteck();
		Rechteck r8 = new Rechteck();
		
		r1.setBounds(0,0,getWidth(),35);
		r2.setBounds(0,getHeight()-63,getWidth(),35);
		r3.setBounds(275,35,35,getHeight()-70);
		r4.setBounds(275+460+35,35,35,getHeight()-70);
		r5.setBounds(310,35,460,70);
		r6.setBounds(310,555,460,85);
		r7.setBounds(0,35,35,getHeight());
		r8.setBounds(getWidth()-40,35,35,getHeight());
		
		add(r1);
		add(r2);
		add(r3);
		add(r4);
		add(r5);
		add(r6);
		add(r7);
		add(r8);
		
		Font font = new Font("Arial", Font.BOLD, 12);
		JLabel textLabel = new JLabel("Zoom-tempo");
		textLabel.setFont(font);
		textLabel.setBounds(835, 288, 75, 50);
		
		JLabel textLabel2 = new JLabel("Slide-tempo");
		textLabel2.setFont(font);
		textLabel2.setBounds(935, 288, 75, 50);
		
		JLabel textLabel3 = new JLabel("Knotenliste");
		textLabel3.setFont(font);
		textLabel3.setBounds(895, 355, 75, 50);
		
		JLabel textLabel4 = new JLabel("Programmstatus");
		textLabel4.setFont(font);
		textLabel4.setBounds(875,125,150,50);
		
		add(textLabel);
		add(textLabel2);
		add(textLabel3);
		add(textLabel4);
	}
	
	private void graphViewingSpeedChoicesSetup(){
		final JSlider js1 = new JSlider(0,100,50);
		final JSlider js2 = new JSlider(0,100,50);
		
		js1.setMinorTickSpacing(5);
		js1.setMajorTickSpacing(10);
		js1.setPaintTicks(true);
		js1.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  GUI_Graph.setZoomSpeed(js1.getValue());
		      }
		    });
			
		js2.setMinorTickSpacing(5);
		js2.setMajorTickSpacing(10);
		js2.setPaintTicks(true);
		js2.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent event) {
		    	  GUI_Graph.setScrollSpeed(js2.getValue());
		      }
		    });
		
		js1.setBounds(830,320,90,50);
		js2.setBounds(930,320,90,50);
		
		add(js1);
		add(js2);
	}
	
	private void verticesListDisplaySetup(){
		ArrayList<Vertex> vertices = new ArrayList<Vertex>(GUI_Graph.getGraph().getVertices());
		final String[] knoten_werte = new String[vertices.size()];
		for(int i = 0; i < vertices.size(); i++)
			knoten_werte[i] = "Nr."+vertices.get(i).getNumber()+" Wert: "+vertices.get(i).getValue();
		
		JList<String> list = new JList<String>(knoten_werte);
		vertex_list = new JScrollPane();
		vertex_list.getViewport().add(list);
		vertex_list.setBounds(830, 400, 200, 210);
		add(vertex_list);
	}
	
	public static JTextArea status = new JTextArea();
	
	private void programStatusSetup(){
		status.setEditable(false);
    status.setBounds(830,165,200,85);
		add(status);
	}
	
	private void verticesDisplayChoicesSetup(){
		final JCheckBox jcb1 = new JCheckBox("Knotenwert anzeigen",true);
		final JCheckBox jcb2 = new JCheckBox("Knotennummer anzeigen",true);
		
		jcb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
              GUI_Graph.toggleShowVertexValue();
              GUI_Graph.paint();
            }
        });
		
		jcb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	GUI_Graph.toggleShowVertexIndex();
            	GUI_Graph.paint();
            }
        });

		jcb1.setBounds(830,70,200,30);
		jcb2.setBounds(830,100,200,30);
		
		add(jcb1);
		add(jcb2);
	}
	
	private void cameraMovementsSetup(){
		JButton jb1 = new JButton("In");
		JButton jb2 = new JButton("Out");
		JButton jb3 = new JButton("Goto");
		
		jb1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	 GUI_Graph.min_zoom();
            }
        });
		
		jb2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	 GUI_Graph.max_zoom();
            }
        });
		
		jb3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
            	try{
            		 GUI_Graph.visit_vertex_user();
            		 GUI_Graph.paint();
            	}
            	catch(Exception ex){}
            }
        });
		
		jb1.setBounds(835,260,55,20);
		jb2.setBounds(905,260,55,20);
		jb3.setBounds(975,260,65,20);
		
		add(jb1);
		add(jb2);
		add(jb3);
	}

}

class Rechteck extends JPanel{

	private static final long serialVersionUID = 4651264511939491872L;
	
	public void paintComponent(Graphics g){
		g.fillRect(0,0,getWidth(),getHeight());
	}
	
}
