package Metaheuristics.NeighborOperators;

import Logistics.Destination;
import Metaheuristics.MetaheuristicUtils;
import Metaheuristics.Road;
import Metaheuristics.Solution;
import Metaheuristics.Taboo.*;

/**
 * Cette classe représente l'opérateur d'échange de deux clients entre deux routes
 */
public class Exchange {

    /**
     * Cette méthode permet d'échanger deux clients d'une route
     * @param solution Solution à modifier
     * @param roadSelected Route à modifier
     * @param firstClient Premier client à échanger
     * @param secondClient Deuxième client à échanger
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return La solution modifiée
     */
    public static Result ExchangeIntra(Solution solution, int roadSelected, int firstClient, int secondClient, boolean timeConstraint,
                                       int chosenTransformation)
    {
        // Vérifier que les paramètres sont valides
        if (firstClient == secondClient)
        {
            return null;
        }

        if (firstClient == 0 || secondClient == 0)
        {
            return null;
        }

        if (firstClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1 || secondClient >= solution.getRoads().get(roadSelected).getDestinations().size()-1)
        {
            return null;
        }

        // On clone la solution et la route
        Road newRoad = solution.getRoads().get(roadSelected).clone();
        Solution candidate = solution.clone();



        // Récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(roadSelected).getDestinations().get(secondClient);

        // Echanger les destinations
        newRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newRoad.getDestinations().set(secondClient, firstClientDestinsation);

        // Mettre à jour la route
        candidate.getRoads().set(roadSelected, newRoad);

        // Vérifier que la route est valide
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, roadSelected, timeConstraint);

        if (candidate == null) {return null;}

        // On crée la transformation selon le type de transformation choisi
        Transformation transformation;
        if (chosenTransformation == 1)
        {
             transformation = new TransformationClientsId("exchangeIntra",
                    solution.getRoads().get(roadSelected).getDestinations().get(firstClient).getIdName(),
                    solution.getRoads().get(roadSelected).getDestinations().get(secondClient).getIdName());
        }
        else if (chosenTransformation == 2)
        {
            transformation = new TransformationClientsList("exchangeIntra",
                    solution.getRoads().get(roadSelected).returnListOfIdClient(),
                    candidate.getRoads().get(roadSelected).returnListOfIdClient());
        }
        else
        {
            transformation = new TransformationIndexes("exchangeIntra",
                    roadSelected,
                    firstClient,
                    secondClient);
        }

        // Mettre à jour la distance totale parcourue
        candidate.reCalculateTotalDistanceCovered();

        // Créer le résultat solution/transformation
        Result result = new Result(candidate, transformation);
        return result;
    }


    /**
     * Cette méthode permet d'échanger deux clients entre deux routes
     * @param solution Solution à modifier
     * @param firstClientRoad Première route à modifier
     * @param secondClientRoad Deuxième route à modifier
     * @param firstClient Premier client à échanger
     * @param secondClient Deuxième client à échanger
     * @param timeConstraint Contrainte de temps
     * @param chosenTransformation Transformation choisie
     * @return
     */
    public static Result ExchangeIntra(Solution solution, int firstClientRoad, int secondClientRoad, int firstClient,
                                       int secondClient, boolean timeConstraint, int chosenTransformation)
    {
        // Vérifier que les paramètres sont valides
        if (firstClientRoad == secondClientRoad)
        {
            return null;
        }

        if (firstClient == 0 || secondClient == 0)
        {
            return null;
        }

        if (firstClient >= solution.getRoads().get(firstClientRoad).getDestinations().size()-1 || secondClient >= solution.getRoads().get(secondClientRoad).getDestinations().size()-1)
        {
            return null;
        }

        if (solution.getRoads().get(firstClientRoad).getDestinations().size()<=3 && solution.getRoads().get(secondClientRoad).getDestinations().size()<=3)
        {
            return null;
        }

        Result result;

        // On clone les routes et la solution
        Road newFirstRoad = solution.getRoads().get(firstClientRoad).clone();
        Road newSecondRoad = solution.getRoads().get(secondClientRoad).clone();
        Solution candidate = solution.clone();

        // Récupérer les destinations des deux clients à échanger
        Destination firstClientDestinsation = solution.getRoads().get(firstClientRoad).getDestinations().get(firstClient);
        Destination secondClientDestinsation = solution.getRoads().get(secondClientRoad).getDestinations().get(secondClient);

        // Echanger les destinations
        newFirstRoad.getDestinations().set(firstClient, secondClientDestinsation);
        newSecondRoad.getDestinations().set(secondClient, firstClientDestinsation);

        // Mettre à jour les routes
        candidate.getRoads().set(firstClientRoad, newFirstRoad);
        candidate.getRoads().set(secondClientRoad, newSecondRoad);

        // Vérifier que les routes sont valides
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, firstClientRoad,timeConstraint );
        if (candidate == null) {return null;}
        candidate = MetaheuristicUtils.verifyIfRoadValid(candidate, secondClientRoad, timeConstraint);
        if (candidate == null) {return null;}

        // On recalcule la distance totale parcourue
        candidate.reCalculateTotalDistanceCovered();

        // On crée la transformation selon le type de transformation choisi
        Transformation transformation;

        if(chosenTransformation == 1)
        {
            transformation = new TransformationClientsId("exchangeInter",
                    solution.getRoads().get(firstClientRoad).getDestinations().get(firstClient).getIdName(),
                    solution.getRoads().get(secondClientRoad).getDestinations().get(secondClient).getIdName()
            );
        }
        else if(chosenTransformation == 2)
        {
            transformation = new TransformationClientsList("exchangeInter",
                    solution.getRoads().get(firstClientRoad).returnListOfIdClient(),
                    candidate.getRoads().get(firstClientRoad).returnListOfIdClient(),
                    solution.getRoads().get(secondClientRoad).returnListOfIdClient(),
                    candidate.getRoads().get(secondClientRoad).returnListOfIdClient()
            );
        }
        else
        {
            transformation = new TransformationIndexes("exchangeInter",
                    firstClientRoad,
                    secondClientRoad,
                    firstClient,
                    secondClient
            );
        }

        // On crée le résultat solution/transformation
        result = new Result(candidate, transformation);
        return result;
    }
}
