package Metaheuristics.NeighborOperators;

import Logistics.Destination;
import Utils.MetaheuristicUtils;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.*;

import java.util.ArrayList;


/**
 * Cette classe représente l'opérateur de voisinage Relocate
 */
public class Relocate {


    /**
     * Cette méthode permet de déplacer un client d'une même route à un autre endroit
     * @param solution Solution à modifier
     * @param roadSelected Route à modifier
     * @param indexClient Index du client à déplacer
     * @param newIndexClient Nouvel index du client
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return La solution modifiée
     */
    public static Result RelocateIntra(Solution solution, int roadSelected, int indexClient, int newIndexClient, boolean timeConstraint,
    int chosenTransformation) {

        // Vérifier que les paramètres sont valides
        if(indexClient == newIndexClient)
        {
            return null;
        }

        if(indexClient == 0 || newIndexClient == 0)
        {
            return null;
        }

        if (indexClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1 || newIndexClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1)
        {
            return null;
        }

        // on récupère la route du trajet concerné
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        // On recupère la destination du client
        Destination arriveClient = newRoad.getDestinations().get(indexClient).clone();
        // on retire le client du trajet
        newRoad.getDestinations().remove(indexClient);
        newRoad.getDestinations().add(newIndexClient, arriveClient);
        // on crée un candidat
        Solution candidate = solution.clone();
        // on met à jour la route
        candidate.getRoads().set(roadSelected, newRoad);

        // on vérifie que la route est valide
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, roadSelected, timeConstraint);
        if (candidate == null) {return null;}

        // on met à jour la solution
        candidate.reCalculateTotalDistanceCovered();
        Transformation transformation;

        // on crée la transformation
        if(chosenTransformation==1)
        {
            transformation = new TransformationClientsId("relocateIntra",
                    solution.getRoads().get(roadSelected).getDestinations().get(indexClient).getIdName(),
                    solution.getRoads().get(roadSelected).getDestinations().get(newIndexClient).getIdName()
            );
        }
        else if(chosenTransformation==2)
        {
            transformation = new TransformationClientsList("relocateIntra",
                    solution.getRoads().get(roadSelected).returnListOfIdClient(),
                    candidate.getRoads().get(roadSelected).returnListOfIdClient());
        }
        else
        {
            transformation = new TransformationIndexes("relocateIntra",
                    roadSelected,
                    indexClient,
                    newIndexClient
            );
        }

        // On crée le résultat solution/transformation
        Result res = new Result(candidate, transformation);
        return res;
    }


    /**
     * Cette méthode permet de déplacer un client d'une route à une autre
     * @param solution Solution à modifier
     * @param firstClientRoad Route du premier client à déplacer
     * @param secondClientRoad Route où l'on va déplacer le client
     * @param indexClient Index du premier client à déplacer
     * @param newIndexClient Nouvel index du client
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return La solution modifiée
     */
    public static Result RelocateInter(Solution solution, int firstClientRoad, int secondClientRoad, int indexClient,
                                       int newIndexClient, boolean timeConstraint, int chosenTransformation)
    {
        // Vérifier que les paramètres sont valides
        if(indexClient >= solution.getRoads().get(firstClientRoad).getDestinations().size()-1 || indexClient == 0 || newIndexClient == 0)
        {
            return null;
        }

        if(firstClientRoad == secondClientRoad)
        {
            return null;
        }


        Result result;

        // on récupère la route du trajet concerné
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        // On recupère la destination du client
        Destination arriveFirstClient = newFirstRoad.getDestinations().get(indexClient);
        // on retire le client du trajet
        newFirstRoad.getDestinations().remove(indexClient);
        // on crée un candidat
        Solution candidate = solution.clone();
        // on ajoute le client à sa nouvelle position
        newSecondRoad.getDestinations().add(newIndexClient, arriveFirstClient);

        // On affecte la nouvelle route au candidat
        candidate.getRoads().set(firstClientRoad, newFirstRoad);
        candidate.getRoads().set(secondClientRoad, newSecondRoad);

        // on vérifie que les routes sont valides
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, firstClientRoad, timeConstraint);
        if (candidate == null) {return null;}
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, secondClientRoad, timeConstraint);
        if (candidate == null) {return null;}

        // On récupère les listes des clients des routes concernées car elles peuvent être potentiellement vides
        ArrayList<String> firstList = MetaheuristicUtils.getExistingRoad(candidate, firstClientRoad);
        ArrayList<String> secondList = MetaheuristicUtils.getExistingRoad(candidate, secondClientRoad);

        // On supprime les routes vides
        candidate = MetaheuristicUtils.removeUselessRoad(candidate);

        // On calcule la distance totale parcourue
        candidate.reCalculateTotalDistanceCovered();

        // On crée la transformation correspondante
        Transformation transformation;

        if(chosenTransformation == 1)
        {
            transformation = new TransformationClientsId("relocateInter",
                    solution.getRoads().get(firstClientRoad).getDestinations().get(indexClient).getIdName(),
                    solution.getRoads().get(secondClientRoad).getDestinations().get(newIndexClient).getIdName()
            );
        }
        else if(chosenTransformation == 2)
        {
            transformation = new TransformationClientsList("relocateInter",
                    solution.getRoads().get(firstClientRoad).returnListOfIdClient(),
                    firstList,
                    solution.getRoads().get(secondClientRoad).returnListOfIdClient(),
                    secondList
            );
        }
        else
        {
            transformation = new TransformationIndexes("relocateInter",
                    firstClientRoad,
                    secondClientRoad,
                    indexClient,
                    newIndexClient
            );
        }

        // On crée le résultat solution/transformation
        result = new Result(candidate, transformation);
        return result;
    }

}
