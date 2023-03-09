/**
 * @author Clémence and Camélia
 */

package Graphics;

import Logistique.Client;
import Logistique.Configuration;
import Logistique.Destination;

import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * The class OrthonormalPlan is used to display a solution found graphically in order to visualize and check it
 *
 */
public class OrthonormalPlan extends JPanel {

    private final int pointSize = 10;
    private final Color pointColorClient = Color.RED;
    private final Color pointColorDepot = Color.GREEN;
    private Configuration config;

    private int screenWidth;
    private int screenHeight;
    private final int multiplier = 10;

    /**
     * Constructor of the class OrthonormalPlan
     * @param config the configuration used to draw the plan
     */
    public OrthonormalPlan(Configuration config) {
        this.config = config;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        screenWidth = screenSize.width;
        screenHeight = 800;

    }

    /**
     * Create plan and add points
     * @param g the graphic object
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        int centerX = config.getCentralDepot().getLocalisation().getX();
        int centerY = config.getCentralDepot().getLocalisation().getY();

        // Translate the origin to the center of the screen
        int translateX = screenWidth/2 - centerX*multiplier;
        int translateY = screenWidth/2 + centerY;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(translateX, translateY);

        // Clients
        for (Client client : config.getListClients()) {
            drawPoint(g, client, centerX + client.getLocalisation().getX() * multiplier, centerY -
                    client.getLocalisation().getY() * multiplier, pointColorClient);
        }

        //Depot
        drawPoint(g, config.getCentralDepot(), centerX + config.getCentralDepot().getLocalisation().getX() * multiplier,
                centerY - config.getCentralDepot().getLocalisation().getY() * multiplier, pointColorDepot);

    }

    /**
     * @param g the graphic object
     * @param d the client or depot to draw
     * @param x the x coordinate of the destination
     * @param y the y coordinate of the destination
     * @param color the color of the point
     */
    private void drawPoint(Graphics g, Destination d, int x, int y, Color color) {
        g.setColor(color);
        g.drawOval(x - pointSize / 2, y - pointSize / 2, pointSize, pointSize);
        g.fillOval(x - pointSize / 2, y - pointSize / 2, pointSize, pointSize);
        String text = d.getIdName();
        int textWidth = g.getFontMetrics().stringWidth(text);
        int textHeight = g.getFontMetrics().getAscent();
        g.drawString(text, x + (20 - textWidth) / 2, y + (20 + textHeight) / 2);
    }

    /**
     * Configure the scrollPane
     * @param scrollPane the scrollPane to configure
     * @return the scrollPane configured
     */
    private JScrollPane configureScrollPane(JScrollPane scrollPane) {
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(500);
        scrollPane.getVerticalScrollBar().setBlockIncrement(3000);
        return scrollPane;
    }

    /**
     * Get the 4 most top, bottom, left and right clients
     */
    private void getXAndYCenter()
    {
        Client clientMostTop = config.getListClients().get(0);
        Client clientMostBottom = config.getListClients().get(0);
        Client clientMostLeft = config.getListClients().get(0);
        Client clientMostRight = config.getListClients().get(0);
        for(Client client: config.getListClients())
        {
            if(client.getLocalisation().getY() > clientMostTop.getLocalisation().getY())
            {
                clientMostTop = client;
            }
            if(client.getLocalisation().getY() < clientMostBottom.getLocalisation().getY())
            {
                clientMostBottom = client;
            }
            if(client.getLocalisation().getX() < clientMostLeft.getLocalisation().getX())
            {
                clientMostLeft = client;
            }
            if(client.getLocalisation().getX() > clientMostRight.getLocalisation().getX())
            {
                clientMostRight = client;
            }
        }

        System.out.println("clientMostTop: " + clientMostTop.getIdName());
        System.out.println("clientMostBottom: " + clientMostBottom.getIdName());
        System.out.println("clientMostLeft: " + clientMostLeft.getIdName());
        System.out.println("clientMostRight: " + clientMostRight.getIdName());
    }

    /**
     * Display the plan when main is running
     */
    public void display() {
        JFrame frame = new JFrame("Data"+config.getFileNumber());
        getXAndYCenter();

        // Create a JPanel
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Add the JPanel to the JFrame
        OrthonormalPlan orthonormalPlan = new OrthonormalPlan(config);
        panel.add(orthonormalPlan, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(this.screenWidth, this.screenHeight+500));

        // Add a scroll bar to the JPanel
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane = configureScrollPane(scrollPane);

        // Configure the JFrame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(this.screenWidth, this.screenHeight);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(scrollPane); // Ajouter le JScrollPane à la place du panneau
        frame.setVisible(true);
    }



}