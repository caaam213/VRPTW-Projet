import CallableFunctions.*;

/**
 * Autrices du code : PERVENCHE Clémence & MERAOUI Camélia
 */
public class Main {
    public static void displayExecutionTime(long duration)
    {
        // Convertir le temps
        int heures = (int) (duration / 3600000000000L);
        int minutes = (int) (duration / 60000000000L) % 60;
        int secondes = (int) (duration / 1000000000L) % 60;

        // Afficher le temps
        System.out.printf("Temps d'execution : %d heures, %d minutes, %d secondes", heures, minutes, secondes);
        System.out.println();
    }

    public static void main(String[] args) {
        long startTime = System.nanoTime();

        //////////////////////////////////////////////////////
        // TODO : Décommenter cette fonction pour tester la méthode génétique
        /*CallGenetic.runGenetic("101", false, true,
                60, 1000, 0.33f,
                20, false,false, true);*/

        // TODO : Décommenter cette fonction pour tester la méthode tabou
       /*CallTaboo.runTaboo("1202", false, true,1000,
                50,1);*/

        // TODO : Décommenter cette fonction pour avoir le nombre minimal de véhicules pour une configuration
        //CallTestConstraints.displayMinimalNumberOfVehicles("101");

        // TODO : Décommenter cette fonction pour tester la fonction de calcul de distance entre deux clients
        //CallTestConstraints.calculateDistanceBetweenTwoDestination();

        // TODO : Décommenter cette fonction pour tester la fonction de vérification des contraintes
        //CallTestConstraints.testIfConstraintsAreRespected();

        // TODO : Décommenter cette fonction pour tester le calcul des informations entre deux clients
        //CallTestConstraints.testCalculateInfos();

        // TODO : Décommenter cette fonction pour tester la fonction qui supprime les clients servis plus d'une fois
        //CallTestCrossover.testRemoveClientServedMoreThanOnce();

        // TODO : Décommenter cette fonction pour tester la fonction de crossover SBX
        //CallTestCrossover.testCrossoverSBX();

        // TODO : Décommenter cette fonction pour tester la fonction de crossover RBX
        //CallTestCrossover.testCrossoverRBX();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins Exchange-intra
        //CallTestNeighborsFunctions.generateAllExchangeIntra();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins Exchange-inter
        //CallTestNeighborsFunctions.generateAllExchangeInter();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins Relocate-intra
        //CallTestNeighborsFunctions.generateAllRelocateIntra();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins Relocate-inter
        //CallTestNeighborsFunctions.GenerateAllRelocateInter();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins 2-opt
        //CallTestNeighborsFunctions.generateAllTwoOpt();

        // TODO : Décommenter cette fonction pour tester la génération de tous les voisins cross-exchange
        //CallTestNeighborsFunctions.generateAllCrossExchange();

        // TODO : Décommenter cette fonction pour tester la création d'un hashset dans le cas où les solutions sont
        //  identiques
        //CallTestNeighborsFunctions.testHashSetCreation();

        // TODO: Décommenter cette fonction pour tester si les transformations sont correctes
        //CallTestNeighborsFunctions.testTransformations();

        //////////////////////////////////////////////////////

        long endTime = System.nanoTime();
        displayExecutionTime(endTime - startTime);

    }
}