package Graphics;

import Logistique.Client;
import Logistique.Configuration;
import Metaheuristique.Road;
import Metaheuristique.Solution;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.util.Hashtable;
import java.util.Random;

public class SolutionVisualization {
    static final int MULTIPLIER = 10;
    static final int HEIGHTANDWEIGHTVERTEX = 20;

    static final int WIDTHFRAME = 1000;
    static final int HEIGHTFRAME = 1000;

    static int nbRoadsMaxToDisplay = -1; // The number of roads to display for each client if -1, display all the roads

    /**
     * Generate a random color in hexadecimal
     * @return a random color in hexadecimal
     */
    public static String generateRandomColor() {
        Random random = new Random();
        // Generate random integers in range 0 to 255
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        //Create a new color using the integers as RGB values
        Color color = new Color(r, g, b);

        // Convert the color to hexadecimal
        String hex = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        return hex;
    }


    /**
     * Configure the style of the vertexes
     * @param graph the graph
     * @return the style of the vertexes
     */
    private static Hashtable<String, Object> ConfigurateVertexStyle(mxGraph graph) {

        Hashtable<String, Object> vertexStyle = new Hashtable<String, Object>();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#C3D9FF");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        return vertexStyle;
    }

    /**
     * Display the graph of the solution in a frame
     * @param config : the configuration
     * @param solution : the solution to display
     */
    public static void DisplayGraph(Configuration config, Solution solution) {
        // Create a frame
        final JFrame frame = new JFrame();
        frame.setSize(WIDTHFRAME, HEIGHTFRAME);
        JPanel panel = new JPanel();
        panel.setSize(frame.getMaximumSize().width,
                frame.getMaximumSize().height);

        // Create a graph
        final mxGraph graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        // Set the style of the vertexes
        mxStylesheet stylesheet = graph.getStylesheet();
        stylesheet.setDefaultVertexStyle(ConfigurateVertexStyle(graph));

        try {
            // Create the vertex array
            Object[] vertexes = new Object[101];
            int i = 1;
            vertexes[0] = config.getCentralDepot();
            for (Client client : config.getListClients()) {
                Object vertex = graph.insertVertex(parent, null, client.getIdName(), client.getLocalisation().getX()*MULTIPLIER, client.getLocalisation().getY()*MULTIPLIER, HEIGHTANDWEIGHTVERTEX, HEIGHTANDWEIGHTVERTEX);
                vertexes[i] = vertex;
                i++;
            }

            // Create the edges between the vertexes according to the solution

            for (Road road : solution.getRoads()) {
                String color = generateRandomColor();
                for (i = 0; i < road.getDestinations().size() - 1; i++) {
                    String d1 = road.getDestinations().get(i).getIdName();
                    String d2 = road.getDestinations().get(i + 1).getIdName();
                    int d1Index = d1.substring(0).equals("d") ? 0 : Integer.parseInt(d1.substring(1, d1.length()));
                    int d2Index = d2.substring(0).equals("d") ? 0 : Integer.parseInt(d2.substring(1, d2.length()));
                    graph.insertEdge(parent, null, "", vertexes[d1Index], vertexes[d2Index], "strokeColor="+color);
                }
                nbRoadsMaxToDisplay--;
                if (nbRoadsMaxToDisplay == 0) {
                    break;
                }
            }

            // Layout the graph
            mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
            layout.setLevelDistance(40);
            layout.setNodeDistance(30);
            layout.setEdgeRouting(false);
            layout.setUseBoundingBox(false);
            layout.execute(graph.getDefaultParent());

        } finally {
            // Update the graph
            graph.getModel().endUpdate();
        }
        // Create the graph component
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);

        graphComponent.setFoldingEnabled(true);

        // Add the graph component to the frame
        panel.setLayout(new BorderLayout());
        panel.add(graphComponent, BorderLayout.CENTER);

        // Display the frame
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
