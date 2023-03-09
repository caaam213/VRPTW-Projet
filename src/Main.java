import Graphics.OrthonormalPlan;
import Logistique.Configuration;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        Configuration config = new Configuration("1101");
        OrthonormalPlan plan = new OrthonormalPlan(config);
        plan.display();


    }
}