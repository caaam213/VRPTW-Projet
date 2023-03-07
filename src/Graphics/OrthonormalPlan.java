package Graphics;

import Logistique.Client;
import Logistique.Configuration;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class OrthonormalPlan extends JPanel {

    private final int pointSize = 10;
    private final Color pointColorClient = Color.RED;
    private final Color pointColorDepot = Color.GREEN;


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Configuration config = new Configuration("101");
        int centerX = config.getCentralDepot().getLocalisation().getX()*10;
        int centerY = config.getCentralDepot().getLocalisation().getY()*10*2;


        // Clients

        for(Client client : config.getListClients())
        {
            g.setColor(pointColorClient);
            g.drawOval(centerX + client.getLocalisation().getX()*10 - pointSize / 2, centerY - client.getLocalisation().getY()*10 - pointSize / 2, pointSize, pointSize);
        }

        g.setColor(pointColorDepot);
        g.drawOval(centerX + config.getCentralDepot().getLocalisation().getX()*10 - pointSize / 2, centerY - config.getCentralDepot().getLocalisation().getY()*10 - pointSize / 2, pointSize, pointSize);


        // Dessiner les points aléatoires
        // Commenter ou supprimer cette boucle si vous voulez dessiner seulement le point (50, 100)
        /*
        Random rand = new Random();
        g.setColor(pointColor);
        for (int i = 0; i < nbPoints; i++) {
            int x = rand.nextInt(2 * lengthX) - lengthX;
            int y = rand.nextInt(2 * lengthY) - lengthY;
            g.drawOval(centerX + x - pointSize / 2, centerY - y - pointSize / 2, pointSize, pointSize);
        }
        */
    }
}
