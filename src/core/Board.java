package core;

import java.util.ArrayList;

public class Board {

    private ArrayList<Player> players;
    private ArrayList<Village> villages;
    private ArrayList<Region> regions;
    private BagResources bagResources;
    private BagOrders bagOrders;
    private int nbTurn;
    private int currentPlayer;

    public Board() {
        nbTurn = 1;
        currentPlayer = 0;
        players = new ArrayList<>();
        villages = new ArrayList<>();
        regions = new ArrayList<>();
        bagResources = new BagResources();
        bagOrders = new BagOrders();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Village> getVillages() {
        return villages;
    }

    public ArrayList<Region> getRegions() {
        return regions;
    }

    public Village getVillageById(int id) {

        Village result = null;

        for (Village village : villages) {
            if (village.getId() == id) {
                result = village;
            }
        }

        return result;
    }

    public Region getRegionById(int id) {

        Region result = null;

        for (Region region : regions) {
            if (region.getId() == id) {
                result = region;
            }
        }

        return result;
    }

    public int getNbTurn() {
        return nbTurn;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public BagResources getBagResources() {
        return bagResources;
    }

    public BagOrders getBagOrders() {
        return bagOrders;
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    public void addVillage(Village v) {
        villages.add(v);
    }

    public void addRegion(Region r) {
        regions.add(r);
    }

    private void transaction(Player p) {
        Village village = p.getPosition();
        int value = 0;
        Resource resSelected = null;

        //Pour faire une Commande
        if (village.getResources().isEmpty() && village.getOrder() != null) {

            ArrayList<Resource> resourceNew = new ArrayList<>();
            ArrayList<Resource> resourcesCopy = new ArrayList<>();
            int nbResourcesInOrder = 0;
            int counterForPlayer = 0;

            nbResourcesInOrder = village.getOrder().getResources().size();

            for (Resource res : p.getResources()) {
                resourcesCopy.add(res);
            }

            for (Resource res : village.getOrder().getResources()) {
                int k = 0;
                boolean resourceGiven = false;
                while (k < resourcesCopy.size() && resourceGiven == false) {
                    if (resourcesCopy.get(k).getType().equals(res.getType())) {
                        resourcesCopy.remove(res);
                        resourceNew.add(res);
                        counterForPlayer++;
                        resourceGiven = true;
                        System.out.println("nbCounter = " + counterForPlayer + "/" + nbResourcesInOrder);
                    }
                    k++;
                }
            }

            //Si le joueur a bien honoré sa commande
            if (counterForPlayer == nbResourcesInOrder) {
                //Pour chaque resource on l'enlève au joueur et rajoute dans le sac 
                for (Resource res : resourceNew) {
                    p.removeResource(p.getSpecificResource(res.getType()));
                    this.getBagResources().addResource(res);
                }
                village.removeOrder();
                p.setCompletedOrder(true);
            }
        } //Pour prendre resources du village
        else if (village.getResources().size() > 0 && village.getOrder() == null) {
            value = village.getResources().get(0).getValue();
            for (Resource villResource : village.getResources()) {
                if (villResource.getValue() <= value) {
                    value = villResource.getValue();
                    resSelected = villResource;
                }
            }
            if (resSelected != null) {
                p.addResource(resSelected);
                village.removeResource(resSelected);
            }
        }
    }

    private void delegation(Player p, Action action) {
        // Verification commande completée, et moins de 2 actions
        if (p.asCompletedOrder() && p.getNbTransactionDone() < 2) {
            p.setNbTransactionDone(p.getNbTransactionDone() + 1);

            // Id de la region contenu dans l'action delegation
            Region choice = getRegionById(action.getId());
            Integer nbDeleg = 0;

            // Type du village => nombre de delegation dispo
            switch (p.getPosition().getType()) {
                case house:
                    nbDeleg = 1;
                    break;
                case temple:
                    nbDeleg = 2;
                    break;
                case monastery:
                    nbDeleg = 3;
                    break;
            }

            // Ajout des delegations
            choice.addDelegations(p, nbDeleg);
            p.addDelegations(choice, nbDeleg);

        } else {
            System.out.println("Vous devez avoir complété une commande, et moins de 2 transactions.");
        }
    }

    private void offering(Player p) {
        // Verification commande completée, et moins de 2 actions
        if (p.asCompletedOrder() && p.getNbTransactionDone() < 2) {
            p.setNbTransactionDone(p.getNbTransactionDone() + 1);

            // Verification : pas de stupa deja placée
            if (p.getPosition().getStupa() == null) {
                p.putStupa();
            } else {
                System.out.println("Il y a déjà une stupa sur ce village.");
            }

        } else {
            System.out.println("Vous devez avoir complété une commande, et moins de 2 transactions.");
        }
    }

    public void executeActions() {

        // Initialisation des joueurs avant les actions
        for (Player p : players) {
            p.setCompletedOrder(false);
            p.setNbTransactionDone(0);
        }

        // Actions
        for (int i = 0; i < 6; i++) {
            for (Player p : players) {
                switch (p.getAction(i).getType()) {
                    case stone:
                        if (p.getPosition().getDestVillage(Road.stone) != null) {
                            p.move(p.getPosition().getDestVillage(Road.stone));
                        }
                        break;
                    case soil:
                        if (p.getPosition().getDestVillage(Road.soil) != null) {
                            p.move(p.getPosition().getDestVillage(Road.soil));
                        }
                        break;
                    case ice:
                        if (p.getPosition().getDestVillage(Road.ice) != null) {
                            p.move(p.getPosition().getDestVillage(Road.ice));
                        }
                        break;
                    case offering:
                        offering(p);
                        break;
                    case transaction:
                        transaction(p);
                        break;
                    case pause:
                        break;
                    case delegation:
                        delegation(p, p.getAction(i));
                        break;
                }
            }
        }

        if (nbTurn % 4 == 0) {
            calculateInventory();
        }
        nbTurn++;
    }

    private void calculateInventory() {
        ArrayList<Resource.Type> resourceType = new ArrayList<>();
        resourceType.add(Resource.Type.sel);
        resourceType.add(Resource.Type.orge);
        resourceType.add(Resource.Type.the);
        resourceType.add(Resource.Type.jade);
        resourceType.add(Resource.Type.or);

        resourceType.stream().map((type) -> {
            Player winner = players.get(0);
            for (Player p : players) {
                if (p.getNbResources(type) > winner.getNbResources(type)) {
                    winner = p;
                }
            }
            return winner;
        }).forEachOrdered((winner) -> {
            winner.setEconomicScore(winner.getEconomicScore() + 3);
        });
    }

    private void calculatePoliticalScore() {
        // TODO
    }

    private void calculateReligiousScore() {
        // TODO
    }

    void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }
}
