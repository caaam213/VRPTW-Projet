package Logistics;

import java.io.*;
import java.util.ArrayList;

/**
 * Va créer une configuration d'un fichier afin de pouvoir faire des opérations dessus
 */
public class Configuration {

    private ArrayList<Client> clientsList = new ArrayList<Client>();
    private Depot centralDepot;
    private Vehicle truck;

    /**
     * Constructeur qui prend en paramètre un numéro de fichier
     * @param FileNumber
     */
    public Configuration(String FileNumber) {
        openAndInitializeFile(FileNumber);
    }


    /**
     * Cette fonction va permettre d'ouvrir le fichier qui finit par <fileNumber>
     * @param fileNumber : Numéro du fichier
     * @return une chaine de caractère de type StringBuffer contenant toutes les infos sur la configuration
     */
    private StringBuffer openFile(String fileNumber) {
        File file = new File("./data/data" + fileNumber + ".vrp");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            StringBuffer dataStringBuffered = new StringBuffer();
            String line;
            // Récupération de toutes les lignes
            while ((line = br.readLine()) != null) {
                dataStringBuffered.append(line);
                dataStringBuffered.append("\n");
            }
            br.close();
            return dataStringBuffered;

        } catch (FileNotFoundException e) {
            System.out.println("Ce fichier n'existe pas");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Cette fonction va ouvrir le fichier puis initialiser les différentes variables de cette classe
     * @param fileNumber
     */
    private void openAndInitializeFile(String fileNumber) {
        StringBuffer dataStringBuffered = openFile(fileNumber);
        String[] linesData = dataStringBuffered.toString().split("\n");
        getDepotFromFile(linesData);
        getClientsFromFile(linesData);
        getTruckFromFile(linesData);
    }

    /**
     * Cette fonction va extraire les informations liées aux véhicules
     * @param linesData
     */
    private void getTruckFromFile(String[] linesData) {
        String line;
        line = linesData[6];
        String[] elements = line.split(" ");
        int capacity = Integer.parseInt(elements[1]);
        truck = new Vehicle(capacity);
    }

    /**
     * Cette fonction va convertir les informations en entier
     * @param elements : Les éléments à convertir
     * @return Un tableau d'entiers contenant les éléments convertis
     */
    private int[] convertInfoIntoInt(String[] elements) {
        int[] elementsInt = new int[elements.length];
        for (int i = 1; i < elements.length; i++) {
            elementsInt[i] = Integer.parseInt(elements[i]);
        }
        return elementsInt;
    }

    /**
     * Cette fonction va extraire les informations liées au dépôt
     * @param linesData : Les lignes du fichier
     */
    private void getDepotFromFile(String[] linesData) {
        String line = linesData[9];
        String[] elements = line.split(" ");
        int[] content = convertInfoIntoInt(elements);
        GPSCoordinates depotLocalisation = new GPSCoordinates(content[1], content[2]);
        centralDepot = new Depot(depotLocalisation, elements[0], content[3], content[4]);
    }

    /**
     * Cette fonction va extraire les informations liées aux clients
     * @param linesData : Les lignes du fichier
     */
    private void getClientsFromFile(String[] linesData) {
        for (int i = 12; i < linesData.length; i++) {
            String line;
            line = linesData[i];
            String[] elements = line.split(" ");
            int[] content = convertInfoIntoInt(elements);
            GPSCoordinates clientLocalisation = new GPSCoordinates(content[1], content[2]);
            Client c = new Client(clientLocalisation, elements[0], content[3], content[4], content[5], content[6]);
            clientsList.add(c);
        }
    }

    /**
     *
     * @return la liste des clients
     */
    public ArrayList<Client> getClientsList() {
        return clientsList;
    }

    /**
     * @return la liste des noms des clients
     */
    public ArrayList<String> getListClientsName() {
        ArrayList<String> listClientsName = new ArrayList<String>();
        for (Client client : clientsList) {
            listClientsName.add(client.getIdName());
        }
        return listClientsName;
    }

    /**
     * @return le dépôt central
     */
    public Depot getCentralDepot() {
        return centralDepot;
    }

    /**
     * @return le véhicule
     */
    public Vehicle getTruck() {
        return truck;
    }

    /**
     * Retourne le nombre de véhicules minimal pour servir tous les clients basé sur la capacité du véhicule
     * @return le nombre de véhicules minimal
     */
    public int getNumberOfMinimalVehicles() {
        int numberOfMinimalVehicles = 0;
        //On va se baser que sur la capacité
        int capacity = truck.getCapacity();
        int totalDemand = 0;

        for (Client client : clientsList) {
            totalDemand += client.getDemand();
        }

        numberOfMinimalVehicles = totalDemand / capacity;
        if (totalDemand % capacity != 0) {
            numberOfMinimalVehicles += 1;
        }
        return numberOfMinimalVehicles;

    }



}
