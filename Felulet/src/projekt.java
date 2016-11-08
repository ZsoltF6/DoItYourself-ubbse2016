import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.PaintEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.GroupLayout.Group;
public class projekt extends JPanel
					 implements ActionListener{
	public static String[] headers;
	protected static JButton button_add;
	protected static JTextField fgv_szoveg;
	protected static JLabel myLabel;
	
	// ebben tarolom el a fuggvenyeket amiket
	// majd egy comboboxban kiiratok
	static List array = new List();
	
	// ez a tarol tartalmazza az egeszet
	static Container myPane;
	
	// utanna pedig jonnek a JPanelek
	static JPanel myJPanel1;
	static JPanel myJPanel2;
	static JPanel myJPanel3;
	static JPanel myJPanel4;
	static JPanel myJPanel5;
	static projekt newContentPane;
	
	// ez a comboboxhoz tartozo valtozo
	public static JComboBox<String> jcombo;
	public static JLabel comboLabel;
	public static JButton button_draw;
	
	// szovegmezok a Diff Evolucios Algoritmusok adataihoz
	public static JTextField pop_szam;
	public static JTextField gen_szam;
	public static JTextField f;
	public static JTextField cf;
	
	// intervallumok
	public static JTextField also;
	public static JTextField felso;
	
	// kirajzolas
	static final int WIDTH = 50;
	static final int HEIGHT = 50; // Size of our example
	
	public static Component paint;
	
	public static JButton button_repaint;
	
	public projekt(){
		// alkoto elemek 1
	    button_add = new JButton("ADD");
		button_add.setActionCommand("add");
	    fgv_szoveg = new JTextField(20);
	    myLabel = new JLabel();
	    
	    // az elemek elrendezkedeset beallito fuggveny
	    setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
	    
	    // hozzaadas 1
	    add(button_add);
	    add(fgv_szoveg);
	    add(myLabel);
	    
	    // alkoto elemek 2
	    jcombo = new JComboBox<String>(
				array.getItems() 
			);
	    comboLabel = new JLabel("Valaszthato fuggvenyek:");
	    button_draw = new JButton("Draw");
	    button_draw.setActionCommand("draw");
	    
	    // Listenerek
	    button_add.addActionListener(this);
	    button_draw.addActionListener(this);
	    
	    // hozzaadas 2
	    add(comboLabel);
	    add(jcombo);
	    add(button_draw);
	}
	
	public void actionPerformed(ActionEvent e) {
		
		// ha megnyomom az ADD gombot
 	    if ("add".equals(e.getActionCommand())) {
 	    	array.add(fgv_szoveg.getText());
 	    	myLabel.setText(array.getItem(3).toString());
 	    	// jcombo tarolo frissitese
 	    	jcombo = new JComboBox<String>(
 					array.getItems() 
 				);
 	    	
 	    	//myJPanel1.removeAll();
 	    	
 	    	//add(jcombo);
 	    	//add(button_draw);
 	    }
 	    
 	    // ha megnyomom a Draw gombot
 	    if ("draw".equals(e.getActionCommand())){
 	    	
 	    }
 	    
 	    if("repaint".equals(e.getActionCommand())){
 	    	removeAll();
 	    	repaint();
 	    	newContentPane.add(button_add);
 	    	newContentPane.add(fgv_szoveg);
 	    	newContentPane.add(myLabel);
 	    	newContentPane.add(comboLabel);
 	    	newContentPane.add(jcombo);
 	    	newContentPane.add(button_draw);
 	    }
 	}
												// CHECKBOXOK
	private static JPanel getcheckbox() {
		JPanel p = new JPanel(new FlowLayout());
		p.setBorder(BorderFactory.createTitledBorder("Checkboxok:"));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		pop_szam = new JTextField(10);
		gen_szam = new JTextField(10);
		f = new JTextField(10);
		cf = new JTextField(10);
		
		p.add(new JLabel("Populacio szam: "));
		p.add(pop_szam);
		p.add(new JLabel("Generacio szam: "));
		p.add(gen_szam);
		p.add(new JLabel("F: "));
		p.add(f);
		p.add(new JLabel("CF: "));
		p.add(cf);
		
		return p;
		
	}
											// INTERVALLUMOK
	private static JPanel interval() {
		JPanel p = new JPanel(new FlowLayout());
		p.setBorder(BorderFactory.createTitledBorder("Intervallumok (e ketto kozott generaljuk a fgv valtozoit):"));
		p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
		
		also = new JTextField(10);
		felso = new JTextField(10);
		
		p.add(new JLabel("Intervallum also erteke: "));
		p.add(also);
		p.add(new JLabel("Intervallum felso erteke: "));
		p.add(felso);
		
		return p;
	}
				 			// INIT PANEL -> itt talalhatok a fofgv ben levo cuccok
   private static JPanel getInitPanel() {
      JPanel p = new JPanel(new FlowLayout());
      
      newContentPane = new projekt();
      p.add(newContentPane);
      
      p.setBorder(BorderFactory.createTitledBorder("Fuggveny hozzaadas/kirajzoltatas:"));
      return p;
   }
   
   private static JPanel minmax() {
	   JPanel p = new JPanel(new FlowLayout());
	   p.setBorder(BorderFactory.createTitledBorder("MinMax:"));
	   // elemek hozzaadasa
	   p.add(new JLabel("Cross over tipus: "));
	   p.add(new JComboBox());
	   p.add(new JLabel("Szelekcio ID: "));
	   p.add(new JComboBox());
	   
	   p.add(new JLabel("Alapertelmezetten min, bejelolve max"));
	   p.add(new JCheckBox("max"));
	   
	   return p;
   }
   
   							// Draw Panel -> itt kene megjelenjenek azoknak a fgvknek az abrazolasai, 
   							// amiket mi ki szeretnenk rajzoltatni 2D ben vagy 3D ben
   private static JPanel drawPanel() {
	   JPanel p = new JPanel(new FlowLayout());
	   p.setBorder(BorderFactory.createTitledBorder("Fuggveny kirajzolasa:"));
	   // a p valtozohoz adando cuccok
	   return p;
   }
   
   
   // =========================================== MAIN ===============================================================
   public static void main(String[] a) {
	   String fuggvenyek = "fg1;fg2;fg3";
	   headers = fuggvenyek.split(";");
	   
	   for (int i=0; i<headers.length; i++) {
	     array.add(headers[i]);
	   }
	   
      JFrame myFrame = new JFrame("FlowLayout Test");
      myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      myPane = myFrame.getContentPane();
      // ertekadas a JPanel valtozoknak
      myJPanel1 = getcheckbox();
      myJPanel2 = getInitPanel();
      myJPanel3 = interval();
      myJPanel4 = minmax();
      myJPanel5 = drawPanel();
      
      // a frameben levo elemek elrendezkedesenek beallitasa
      myPane.setLayout(new GridLayout(0,3));
      
      // a meglevo panelek hozzadasa
      myPane.add(myJPanel1);
      myPane.add(myJPanel2);
      myPane.add(myJPanel3);
      myPane.add(myJPanel4);
      myPane.add(myJPanel5);
      
      myFrame.pack();
      myFrame.setVisible(true);
      
   }
   
}
