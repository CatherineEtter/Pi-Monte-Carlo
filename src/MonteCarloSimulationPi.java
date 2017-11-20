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
    JSpinner inputNumPoints;
    JTextPane txtResult;

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
        frmEstimatePi.getContentPane().setForeground(new Color(0, 0, 0));
        frmEstimatePi.setBackground(new Color(220, 220, 220));
        frmEstimatePi.getContentPane().setBackground(new Color(211, 211, 211));
        frmEstimatePi.setTitle("Estimate Pi Using A Monte Carlo Simulation");
        frmEstimatePi.setResizable(false);
        frmEstimatePi.setBounds(100, 100, 580, 700);
        frmEstimatePi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmEstimatePi.getContentPane().setLayout(null);
        
        SimulationPanel diagram = new SimulationPanel();
        int dSize = 550;
        int center = (int) ((frmEstimatePi.getWidth()-1) - dSize)/2;
        diagram.setBounds(center, 110, dSize, dSize);
        diagram.setBackground(new Color(250,250,250));
        //RGB: 30,30,30
        frmEstimatePi.getContentPane().add(diagram);
        
        JLabel lblNumberOfPoints = new JLabel("Points =");
        lblNumberOfPoints.setForeground(new Color(0, 0, 0));
        lblNumberOfPoints.setHorizontalAlignment(SwingConstants.LEFT);
        lblNumberOfPoints.setFont(new Font("Consolas", Font.BOLD, 11));
        lblNumberOfPoints.setBounds(385, 50, 50, 20);
        frmEstimatePi.getContentPane().add(lblNumberOfPoints);
        
        JLabel lblPi = new JLabel(" \u03C0 =");
        lblPi.setForeground(new Color(0, 0, 0));
        lblPi.setHorizontalAlignment(SwingConstants.LEFT);
        lblPi.setFont(new Font("Consolas", Font.BOLD, 11));
        lblPi.setBounds(405, 18, 30, 20);
        frmEstimatePi.getContentPane().add(lblPi);
        
        String title = "Estimate Pi Using a Monte Carlo Simulation";
        String desc = "If <em>n</em> points are randomly placed in a square inscribed with a circle, then an estimation"
                + " for the value of pi can be computed by multiplying the number of <em>m</em> points in the circle by 4"
                + " and dividing the product by <em>n</em>.";
        JLabel lblDescription = new JLabel("<html><strong>" + title + "</strong><br><br>" + desc + "</html>");
        lblDescription.setForeground(new Color(0, 0, 0));
        lblDescription.setFont(new Font("Consolas", Font.PLAIN, 11));
        lblDescription.setBackground(new Color(230, 230, 250));
        lblDescription.setBounds(15, 10, 364, 91);
        frmEstimatePi.getContentPane().add(lblDescription);
        
        txtResult = new JTextPane();
        txtResult.setText("123");
        txtResult.setForeground(new Color(0,51,102));
        txtResult.setOpaque(false);
        txtResult.setToolTipText("The estimation of Pi");
        txtResult.setBackground(new Color(245, 245, 245));
        txtResult.setEditable(false);
        txtResult.setBounds(445, 18, 120, 20);
        frmEstimatePi.getContentPane().add(txtResult);
        
        SpinnerModel sm = new SpinnerNumberModel(0,0,2000000000,100); //Sets the limit for the spinner 2,000,000,000
        inputNumPoints = new JSpinner(sm);
        inputNumPoints.setToolTipText("Number of points to randomly generate");
        inputNumPoints.setBounds(445, 49, 120, 20);
        frmEstimatePi.getContentPane().add(inputNumPoints);
        
        JButton btnRun = new JButton("Run Simulation");
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                diagram.repaint();
            }
        });
        btnRun.setFont(new Font("Consolas", Font.PLAIN, 12));
        btnRun.setBounds(384, 81, 180, 20);
        frmEstimatePi.getContentPane().add(btnRun);
    }
    //Creates the diagram and runs the simulation
    class SimulationPanel extends JPanel {
        //Set up initial diagram
        public void paintComponent(Graphics g) {
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
            
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Random randGen = new Random();
            StyledDocument doc = txtResult.getStyledDocument();
            
            txtResult.setText("");
            //Render the square and the circle
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.darkGray);
            g2.setStroke(new BasicStroke(1));
            g2.drawRect(0, 0, this.getWidth()-1, this.getHeight()-1);
            g2.drawOval(0, 0, this.getWidth()-1, this.getHeight()-1);
            
            int points = (int) inputNumPoints.getValue();
            //Only run if points is a positive number
            if(points > 0) {
                g2.setStroke(new BasicStroke(1));
                //Calculate and draw the random points
                for(int i = 0; i < points; i++) {
                    rX = randGen.nextInt(width-1)+1;
                    rY = randGen.nextInt(height-1)+1;
                    
                    //If inside of the circle when x1 is circle radius x: (x1-x2)^2 + (y1-y2)^2 <= r^2
                    if(Math.pow(cX-rX, 2) + Math.pow(cY-rY, 2) <= Math.pow(cX, 2)) {;
                        g2.setColor(new Color(0,51,102));
                        inside++;
                    } else {
                        g2.setColor(new Color(252,191,73));
                    }
                    g2.drawLine(rX, rY, rX, rY);
                }
                PiEstimate = (double)(4.0*inside) / points; //Compute Pi Estimation
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
}
