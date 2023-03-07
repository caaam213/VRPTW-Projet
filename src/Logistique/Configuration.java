package Logistique;

import java.io.*;
import java.util.ArrayList;

public class Configuration {

    private ArrayList<Client> listClients = new ArrayList<Client>();
    private Depot centralDepot;
    private Vehicule truck;

    public Configuration(String FileNumber) {
        openAndInitializeFile(FileNumber);
    }

    private StringBuffer openFile(String fileNumber) {
        File file=new File("./data/data"+fileNumber+".vrp");
        BufferedReader br= null;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuffer dataStringBuffered=new StringBuffer();
            String line;
            while((line=br.readLine())!=null)
            {
                dataStringBuffered.append(line);
                dataStringBuffered.append("\n");
            }
            br.close();
            return dataStringBuffered;

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void openAndInitializeFile(String fileNumber){
        StringBuffer dataStringBuffered = openFile(fileNumber);
        String[] linesData = dataStringBuffered.toString().split("\n");
        getDepotFromFile(linesData);
        getClientsFromFile(linesData);
        getTruckFromFile(linesData);
    }

    private void getTruckFromFile(String[] linesData) {
        String line = new String();
        line = linesData[6];
        String[] elements = line.split(" ");
        int capacity = Integer.parseInt(elements[1]);
        truck = new Vehicule(capacity);
    }

    private int[] convertInfoIntoInt(String[] elements)
    {
        int[] elementsInt = new int[elements.length];
        for(int i=1;i< elements.length;i++)
        {
            elementsInt[i] = Integer.parseInt(elements[i]);
        }
        return elementsInt;
    }

    private void getDepotFromFile(String[] linesData) {
        String line = new String();
        line = linesData[9];
        String[] elements = line.split(" ");
        System.out.println(elements.length);
        int[] content = convertInfoIntoInt(elements);
        GPSCoordinates depotLocalisation = new GPSCoordinates(content[1], content[2]);
        centralDepot = new Depot(depotLocalisation, elements[0], content[3], content[4]);
    }

    private void getClientsFromFile(String[] linesData) {
        for ( int i = 12; i< linesData.length; i++) {
            String line;
            line = linesData[i];
            String[] elements = line.split(" ");
            int[] content = convertInfoIntoInt(elements);
            GPSCoordinates clientLocalisation = new GPSCoordinates(content[1], content[2]);
            Client c = new Client(clientLocalisation, elements[0], content[3], content[4], content[5], content[6]);
            listClients.add(c);
        }
    }

    public ArrayList<Client> getListClients() {
        return listClients;
    }

    public Depot getCentralDepot() {
        return centralDepot;
    }

    public Vehicule getTruck() {
        return truck;
    }

    public void displayClient()
    {
        System.out.println(centralDepot.toString());
        System.out.println();
        for(Client client:listClients)
        {
            System.out.println(client.toString());
            System.out.println();

        }
        System.out.println();
        System.out.println(truck.toString());
    }
}
