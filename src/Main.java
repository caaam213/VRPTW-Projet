import Graphics.OrthonormalPlan;
import Logistique.Configuration;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Repere Orthonorme");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(new OrthonormalPlan());
        frame.setVisible(true);
        //FilesUtils.getClients("1101");
        /*Configuration config = new Configuration("101");
        System.out.println(config.toString());
        config.displayClient();*/
        //System.out.println(truck.toString());
        //System.out.println(centralDepot.toString());
        //listClients.forEach((client) -> System.out.println(client.toString()));
    }
}