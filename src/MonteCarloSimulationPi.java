import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.util.Random;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.SwingConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;
import javax.swing.JTextPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;

public class MonteCarloSimulationPi {

    private JFrame frmEstimatePi;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MonteCarloSimulationPi window = new MonteCarloSimulationPi();
                    window.frmEstimatePi.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * Create the application.
     */
    public MonteCarloSimulationPi() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmEstimatePi = new JFrame();
        frmEstimatePi.setBackground(new Color(220, 220, 220));
        frmEstimatePi.getContentPane().setBackground(new Color(220, 220, 220));
        frmEstimatePi.setTitle("Estimate Pi Using A Monte Carlo Simulation");
        frmEstimatePi.setResizable(false);
        frmEstimatePi.setBounds(100, 100, 600, 700);
        frmEstimatePi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmEstimatePi.getContentPane().setLayout(null);
        
        SimulationPanel diagram = new SimulationPanel();
        int dSize = 550;
        int center = (int) (frmEstimatePi.getWidth() / 2) - (dSize/2);
        diagram.setBounds(center, 100, dSize, dSize);
        frmEstimatePi.getContentPane().add(diagram);
        
        JLabel lblNumberOfPoints = new JLabel("Points =");
        lblNumberOfPoints.setHorizontalAlignment(SwingConstants.LEFT);
        lblNumberOfPoints.setFont(new Font("Consolas", Font.BOLD, 11));
        lblNumberOfPoints.setBounds(224, 70, 50, 20);
        frmEstimatePi.getContentPane().add(lblNumberOfPoints);
        
        JLabel lblPi = new JLabel(" \u03C0 =");
        lblPi.setHorizontalAlignment(SwingConstants.LEFT);
        lblPi.setFont(new Font("Consolas", Font.BOLD, 11));
        lblPi.setBounds(10, 70, 30, 20);
        frmEstimatePi.getContentPane().add(lblPi);
        
        JTextPane txtDescription = new JTextPane();
        txtDescription.setBackground(Color.WHITE);
        txtDescription.setEditable(false);
        txtDescription.setBounds(50, 70, 150, 20);
        frmEstimatePi.getContentPane().add(txtDescription);
        
        SpinnerModel sm = new SpinnerNumberModel(0,0,2000000,2); //Sets the limit for the spinner
        JSpinner inputNumPoints = new JSpinner(sm);
        inputNumPoints.setToolTipText("Number of points to randomly generate");
        inputNumPoints.setBounds(284, 69, 150, 20);
        frmEstimatePi.getContentPane().add(inputNumPoints);
        
        JButton btnRun = new JButton("Run Simulation");
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int points = (int) inputNumPoints.getValue();
                diagram.runSimulation(txtDescription,points);
            }
        });
        btnRun.setFont(new Font("Consolas", Font.PLAIN, 12));
        btnRun.setBounds(444, 70, 140, 20);
        frmEstimatePi.getContentPane().add(btnRun);
    }
    //Creates the diagram and runs the simulation
    class SimulationPanel extends JPanel {
        //Set up initial diagram
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.darkGray);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
            g2.drawOval(0, 0, this.getWidth()-1, this.getHeight()-1);
        }
        //Run the simulation using points entered
        public void runSimulation(JTextPane txtPane,int points) {
            final int width = this.getWidth()-1;
            final int height = this.getHeight()-1;
            //x and y of the center
            final int cX = width/2; 
            final int cY = height/2;
            //random x and y
            int rX,rY;
            int inside = 0;
            double PiEstimate = 0;
            String output;
            
            //Initialize the objects needed
            Graphics g = this.getGraphics();
            Graphics2D g2 = (Graphics2D) g;
            Random randGen = new Random();
            StyledDocument doc = txtPane.getStyledDocument();
            
            //Add the random points
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(cX, cY, cX, cY);
            for(int i = 0; i < points; i++) {
                rX = randGen.nextInt(width-1)+1;
                rY = randGen.nextInt(height-1)+1;
                
                //If inside of the circle when x1 is circle radius x: (x1-x2)^2 + (y1-y2)^2 <= r^2
                if(Math.pow(cX-rX, 2) + Math.pow(cY-rY, 2) <= Math.pow(cX, 2)) {
                    g2.setColor(Color.blue);
                    inside++;
                } else {
                    g2.setColor(Color.orange);
                }
                g2.drawLine(rX, rY, rX, rY);
            }
            PiEstimate = (double)(4.0*inside) / points;
            output = Double.toString(PiEstimate);
            
            //Output the solution
            try {
                doc.insertString(0, output, null);
            } catch (BadLocationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
