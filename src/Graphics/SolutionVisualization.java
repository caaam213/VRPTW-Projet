package Graphics;

import Logistics.Client;
import Logistics.Configuration;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import com.mxgraph.layout.mxCompactTreeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import com.mxgraph.view.mxStylesheet;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static java.lang.String.*;


public class SolutionVisualization {
    final int MULTIPLIER = 8;
    final int HEIGHTANDWEIGHTVERTEX = 20;

    final int WIDTHFRAME = 1000;
    final int HEIGHTFRAME = 1000;

    static mxGraph graph;

    static Object[] vertexes;

    // Nombre de routes à afficher. Si la valeur est -1, alors toutes les routes seront affichées
    static int nbRoadsMaxToDisplay = -1;

    /**
     * Constructeur de la visualisation graphique
     */
    public SolutionVisualization() {
        graph = new mxGraph();
        vertexes = new Object[0];
    }


    /**
     * Cette classe va permettre de générer une couleur aléatoire pour une route
     * @return un code couleur qui sera affecté à une route
     */
    public static String generateRandomColor() {
        Random random = new Random();
        // Générer des nombres aléatoires entre 0 et 255
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);

        //Créer une nouvelle couleur RGB
        Color color = new Color(r, g, b);

        // Convertir sous format hexa
        String hex = format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        return hex;
    }


    /**
     * Permet de configurer le style des sommets
     * @return le style des sommets
     */
    private static Hashtable<String, Object> ConfigureVertexStyle() {

        Hashtable<String, Object> vertexStyle = new Hashtable<String, Object>();
        vertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_ELLIPSE);
        vertexStyle.put(mxConstants.STYLE_FILLCOLOR, "#C3D9FF");
        vertexStyle.put(mxConstants.STYLE_STROKECOLOR, "#6482B9");
        return vertexStyle;
    }


    /**
     * Création d'une fenêtre et affichage du graphe
     * @param solution : La solution avec les routes à afficher
     * @param title : Titre de la fenêtre
     */
    public void DisplayGraph(Solution solution, String title) {
        Configuration config = solution.getConfig();

        // Créer une frame
        final JFrame frame = new JFrame();
        frame.setSize(WIDTHFRAME, HEIGHTFRAME);
        frame.setTitle(title);
        JPanel panel = new JPanel();
        panel.setSize(frame.getMaximumSize().width,
                frame.getMaximumSize().height);

        // Créer le graphe
        graph = new mxGraph();
        Object parent = graph.getDefaultParent();
        graph.getModel().beginUpdate();

        // Configurer le style des sommets
        mxStylesheet stylesheet = graph.getStylesheet();
        stylesheet.setDefaultVertexStyle(ConfigureVertexStyle());

        try {
            // Créer le tableau des sommets
            vertexes = new Object[101];
            int i = 1;
            Object vertex = graph.insertVertex(parent, null, config.getCentralDepot().getIdName(),
                    config.getCentralDepot().getLocalisation().getX()*MULTIPLIER,
                    config.getCentralDepot().getLocalisation().getY()*MULTIPLIER, HEIGHTANDWEIGHTVERTEX,
                    HEIGHTANDWEIGHTVERTEX,"fillColor=red");
            vertexes[0] = vertex;

            for (Client client : config.getClientsList()) {
                vertex = graph.insertVertex(parent, null, client.getIdName(),
                        client.getLocalisation().getX()*MULTIPLIER,
                        client.getLocalisation().getY()*MULTIPLIER, HEIGHTANDWEIGHTVERTEX, HEIGHTANDWEIGHTVERTEX);
                vertexes[i] = vertex;
                i++;
            }

            // Créer les arêtes entre chaque sommets selon la solution passée en paramètre

            for (Road road : solution.getRoads()) {
                String color = road.getColor();
                for (i = 0; i < road.getDestinations().size() - 1; i++) {
                    String d1 = road.getDestinations().get(i).getIdName();
                    String d2 = road.getDestinations().get(i + 1).getIdName();
                    int d1Index = d1.equals("d1") ? 0 : Integer.parseInt(d1.substring(1, d1.length()));
                    int d2Index = d2.equals("d1") ? 0 : Integer.parseInt(d2.substring(1, d2.length()));
                    graph.insertEdge(parent, null, "", vertexes[d1Index], vertexes[d2Index],
                            "strokeColor="+color);
                }
                nbRoadsMaxToDisplay--;
                if (nbRoadsMaxToDisplay == 0) {
                    break;
                }
            }

            // Configurer le graphe
            mxCompactTreeLayout layout = new mxCompactTreeLayout(graph);
            layout.setLevelDistance(40);
            layout.setNodeDistance(30);
            layout.setEdgeRouting(false);
            layout.setUseBoundingBox(false);
            layout.execute(graph.getDefaultParent());

        } finally {
            // Mettre à jour le graphe
            graph.getModel().endUpdate();
        }
        // Créer le composent pour le graphe
        final mxGraphComponent graphComponent = new mxGraphComponent(graph);

        graphComponent.setFoldingEnabled(true);

        // Ajout du composant crée dans la frame
        panel.setLayout(new BorderLayout());
        panel.add(graphComponent, BorderLayout.CENTER);

        // Afficher la frame
        frame.add(panel);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


    /**
     * Va mettre à jour la solution à chaque itération d'un algorithme
     * @param solution : Solution à afficher
     */
    public void updateGraphNode(Solution solution) {
        // Important : Mettre à jour la solution, sinon peut causer un comportement imprévisible du graphe
        graph.getModel().beginUpdate();
        // Récupération des arêtes
        Object[] edges = graph.getChildEdges(graph.getDefaultParent());

        // Listes des arêtes à supprimer car plus dans la solution
        List<Object> edgesToRemove = new ArrayList<>(Arrays.asList(graph.getChildEdges(graph.getDefaultParent())));

        for (Road road : solution.getRoads()) {
            String color = road.getColor();
            for (int i = 0; i < road.getDestinations().size() - 1; i++) {

                // Récupérer les destinations
                String d1 = road.getDestinations().get(i).getIdName();
                String d2 = road.getDestinations().get(i + 1).getIdName();
                int d1Index = d1.equals("d1") ? 0 : Integer.parseInt(d1.substring(1, d1.length()));
                int d2Index = d2.equals("d1") ? 0 : Integer.parseInt(d2.substring(1, d2.length()));


                boolean edgeExists = false;
                for (Object edge : edges) {
                    if (edge == null || !(edge instanceof mxCell)) {
                        continue;
                    }
                    // Récupérer les sommets aux extrémités d'une arête
                    mxCell source = (mxCell) graph.getModel().getTerminal(edge, true);
                    mxCell target = (mxCell) graph.getModel().getTerminal(edge, false);

                    // Si l'arête existe, alors on la supprime de la liste des arêtes à supprimer
                    if ((source == vertexes[d1Index] && target == vertexes[d2Index])) {
                        edgeExists = true;
                        edgesToRemove.remove(edge);
                        break;
                    }
                }

                // Si l'arête n'existe pas dans le graphe mais dans la solution alors on l'ajoute dans le graphe
                if (!edgeExists) {
                    graph.insertEdge(graph.getDefaultParent(), null, "", vertexes[d1Index], vertexes[d2Index],
                            "strokeColor="+color);
                }
            }
        }

        // Suppression des arêtes qui n'existent plus dans la solution
        ArrayList<Object> graphCells = new ArrayList<Object>(Arrays.asList(graph.getChildCells(graph.getDefaultParent())));
        for (int i = graphCells.size() - 1; i >= 0; i--) {
                if (edgesToRemove.contains(graphCells.get(i)))
                    graph.removeCells(new Object[]{graphCells.get(i)});
        }

        // Fin de la mise à jour
        graph.getModel().endUpdate();


    }



}
