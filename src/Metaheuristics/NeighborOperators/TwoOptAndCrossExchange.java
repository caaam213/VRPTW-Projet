package Metaheuristics.NeighborOperators;

import Logistics.Destination;
import Utils.MetaheuristicUtils;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.*;

import java.util.ArrayList;

import static java.lang.Math.abs;

/**
 * Cette classe représente les opérateurs de voisinage TwoOpt et CrossExchange
 */
public class TwoOptAndCrossExchange {

    /**
     * Cette méthode permet d'inverser l'ordre des clients
     * @param list Liste de clients
     * @param i Indice du premier client
     * @param j Indice du deuxième client
     * @return La liste inversée
     */
    private static ArrayList<Destination> reverseList(ArrayList<Destination> list, int i, int j)
    {
        ArrayList<Destination> reversedList = new ArrayList<>();
        for (int k = 0; k < i; k++)
        {
            reversedList.add(list.get(k));
        }
        for (int k = j; k >= i; k--)
        {
            reversedList.add(list.get(k));
        }
        for (int k = j+1; k < list.size(); k++)
        {
            reversedList.add(list.get(k));
        }
        return reversedList;
    }


    /**
     * Cette méthode permet d'appliquer l'opérateur de voisinage TwoOpt
     * @param solution Solution à modifier
     * @param selectedRoad Route à modifier
     * @param indexClient1 Index du premier client
     * @param indexClient2 Index du deuxième client
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return La solution modifiée
     */
    public static Result runTwoOpt(Solution solution, int selectedRoad, int indexClient1, int indexClient2,
                                   boolean timeConstraint, int chosenTransformation)
    {

        // Vérifier que les paramètres sont valides
        if(abs(indexClient1 - indexClient2) <= 2)
        {
            return null;
        }

        if (indexClient2 < indexClient1)
        {
            int temp = indexClient1;
            indexClient1 = indexClient2;
            indexClient2 = temp;
        }

        if (indexClient1 == solution.getRoads().get(selectedRoad).getDestinations().size()-1)
        {
            return null;
        }

        if (indexClient2 == 0)
        {
            return null;
        }


        // on crée un voisin
        Solution neighbor = solution.clone();

        Road road = neighbor.getRoads().get(selectedRoad);


        ArrayList<Destination> destinations = road.getDestinations();


        // On va inverser l'ordre des clients entre indexClient1 et indexClient2
        destinations = reverseList(destinations, indexClient1+1, indexClient2-1);

        neighbor.getRoads().get(selectedRoad).setDestinations(destinations);

        // On vérifie que le voisin est valide
        neighbor = MetaheuristicUtils.verifyIfRoadValid(neighbor, selectedRoad, timeConstraint);


        if (neighbor == null) {return null;}

        // On crée un objet transformation selon le type de transformation choisi
        Transformation transformation;


        neighbor.reCalculateTotalDistanceCovered();
        if(chosenTransformation == 1)
        {
            transformation = new TransformationClientsId("TwoOpt",
                    solution.getRoads().get(selectedRoad).getDestinations().get(indexClient1).getIdName(),
                    solution.getRoads().get(selectedRoad).getDestinations().get(indexClient2).getIdName()
            );
        }
        else if(chosenTransformation == 2)
        {
            transformation = new TransformationClientsList("TwoOpt",
                    solution.getRoads().get(selectedRoad).returnListOfIdClient(),
                    neighbor.getRoads().get(selectedRoad).returnListOfIdClient()
            );
        }
        else
        {
            transformation = new TransformationIndexes("TwoOpt",
                    selectedRoad,
                    indexClient1,
                    indexClient2);
        }

        // On crée le résultat solution/transformation
        Result result = new Result(neighbor, transformation);
        return result;
    }


    /**
     * Cette méthode permet d'appliquer l'opérateur de voisinage CrossExchange
     * @param solution Solution à modifier
     * @param selectedRoad1 Route 1 à modifier
     * @param selectedRoad2 Route 2 à modifier
     * @param indexClient1 Index du premier client
     * @param indexClient2 Index du deuxième client
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return La solution modifiée
     */
    public static Result runCrossExchange(Solution solution, int selectedRoad1, int selectedRoad2, int indexClient1, int indexClient2,
                                          boolean timeConstraint, int chosenTransformation)
    {
        // Vérifier que les paramètres sont valides
        if(selectedRoad1 == selectedRoad2)
        {
            return null;
        }

        if (indexClient1 == 0 && indexClient2 == 0)
            return null;

        if (indexClient2>=solution.getRoads().get(selectedRoad2).getDestinations().size()-1 || indexClient1>=solution.getRoads().get(selectedRoad1).getDestinations().size()-1)
            return null;

        if(indexClient1==solution.getRoads().get(selectedRoad1).getDestinations().size()-2 && indexClient2==solution.getRoads().get(selectedRoad2).getDestinations().size()-2)
            return null;



        // On clone un voisin
        Solution neighbor = solution.clone();


        // On récupère les routes
        Road road1 = neighbor.getRoads().get(selectedRoad1);
        Road road2 = neighbor.getRoads().get(selectedRoad2);

        ArrayList<Destination> destinations1 = road1.getDestinations();
        ArrayList<Destination> destinations2 = road2.getDestinations();

        // On prend les clients de la route 1 entre 0 et indexClient1
        ArrayList<Destination> newDestinations1 = MetaheuristicUtils.getSubList(destinations1, 0, indexClient1);
        // Puis on prend les clients de la route 2 entre indexClient2 et la fin
        ArrayList<Destination> subList1NewClient1End = MetaheuristicUtils.getSubList(destinations2, indexClient2+1, destinations2.size()-1);
        newDestinations1.addAll(subList1NewClient1End);

        // On prend les clients de la route 2 entre 0 et indexClient2
        ArrayList<Destination> newDestinations2 = MetaheuristicUtils.getSubList(destinations2, 0, indexClient2);
        // Puis on prend les clients de la route 1 entre indexClient1 et la fin
        ArrayList<Destination> subListNewClient2End = MetaheuristicUtils.getSubList(destinations1, indexClient1+1, destinations1.size()-1);
        newDestinations2.addAll(subListNewClient2End);

        // On affecte ces routes à la solution
        neighbor.getRoads().get(selectedRoad1).setDestinations(newDestinations1);
        neighbor.getRoads().get(selectedRoad2).setDestinations(newDestinations2);

        // On vérifie les routes
        neighbor = MetaheuristicUtils.verifyIfRoadValid(neighbor, selectedRoad1, timeConstraint);
        if (neighbor == null) {return null;}

        neighbor = MetaheuristicUtils.verifyIfRoadValid(neighbor, selectedRoad2, timeConstraint);
        if (neighbor == null) {return null;}

        // On récupère les listes des clients des routes concernées car elles peuvent être potentiellement vides
        ArrayList<String> firstList = MetaheuristicUtils.getExistingRoad(neighbor, selectedRoad1);
        ArrayList<String> secondList = MetaheuristicUtils.getExistingRoad(neighbor, selectedRoad2);

        // On supprime les routes vides
        neighbor = MetaheuristicUtils.removeUselessRoad(neighbor);

        // On recalcule la distance totale
        neighbor.reCalculateTotalDistanceCovered();

        // On crée un objet transformation selon le type de transformation choisi
        Transformation transformation;

        if (chosenTransformation == 1)
        {
            transformation = new TransformationClientsId("CrossExchange",
                    solution.getRoads().get(selectedRoad1).getDestinations().get(indexClient1).getIdName(),
                    solution.getRoads().get(selectedRoad2).getDestinations().get(indexClient2).getIdName()
            );
        }
        else if (chosenTransformation == 2)
        {
            transformation = new TransformationClientsList("CrossExchange",
                    solution.getRoads().get(selectedRoad1).returnListOfIdClient(),
                    firstList,
                    solution.getRoads().get(selectedRoad2).returnListOfIdClient(),
                    secondList
            );
        }
        else
        {
            transformation = new TransformationIndexes("CrossExchange",
                    selectedRoad1,
                    selectedRoad2,
                    indexClient1,
                    indexClient2);
        }
        // On crée le résultat solution/transformation
        Result result = new Result(neighbor, transformation);
        return result;

    }



}
