package core;import java.util.ArrayList;import java.util.HashMap;import java.util.Map;public class Player {    private String color;    private int politicalScore;    private int religiousScore;    private int economicScore;    private int nbStupa;    private int nbDelegation;    private int nbTransactionDone;    private boolean completedOrder;    private Village currentPosition;    private ArrayList<Resource> resources;    private ArrayList<Action> actions;    private ArrayList<Village> resTakenVillage;    private HashMap<Region, Integer> delegations;    private int villageOrderId;    private int nbYacksOrder;    public Player(String color, Village v) {        this.color = color;        currentPosition = v;        politicalScore = 0;        religiousScore = 0;        economicScore = 0;        nbStupa = 5;        nbDelegation = 15;        completedOrder = false;        nbTransactionDone = 0;        resources = new ArrayList<>();        actions = new ArrayList<>();        resTakenVillage = new ArrayList<>();        delegations = new HashMap<>();    }        /**     * Constructeur par copie     * @param player      */    public Player(Player player){                this.actions = new ArrayList<>();        this.resTakenVillage = new ArrayList<>();                this.color = player.color;        this.completedOrder = player.completedOrder;                this.delegations = new HashMap<>(player.delegations.size());        for (Map.Entry<Region, Integer> entry : player.delegations.entrySet()) {            Region reg = new Region(entry.getKey());            Integer nbDelegations = entry.getValue();            this.delegations.put(reg, nbDelegations);        }        this.nbDelegation = player.nbDelegation;        this.nbStupa = player.nbStupa;        this.nbTransactionDone = player.nbTransactionDone;        this.nbYacksOrder = player.nbYacksOrder;        this.politicalScore = player.politicalScore;        this.economicScore = player.economicScore;        this.religiousScore = player.religiousScore;        this.villageOrderId = player.villageOrderId;                this.resources = new ArrayList<>();        for (Resource resource : player.resources) {            this.resources.add(new Resource(resource.getType()));        }            }        public void addResourceTaken(Village v){        resTakenVillage.add(v);    }        public boolean canTakeResource(){        return (!resTakenVillage.contains(currentPosition));    }        public void clearResTakenVillage(){        resTakenVillage.clear();    }        public HashMap<Region, Integer> getDelegations() {        return delegations;    }    public void setPoliticalScore(int politicalScore) {        this.politicalScore = politicalScore;    }    public void setReligiousScore(int religiousScore) {        this.religiousScore = religiousScore;    }    public void setEconomicScore(int economicScore) {        this.economicScore = economicScore;    }    public void addDelegations(Region r, Integer nb) {        nbDelegation -= nb;        delegations.put(r, nb);    }    public int getNbTransactionDone() {        return nbTransactionDone;    }    public int getNbStupa() {        return nbStupa;    }    public void setNbTransactionDone(int nbTransactionDone) {        this.nbTransactionDone = nbTransactionDone;    }    public void putStupa() {        if (nbStupa > 0 && currentPosition.getStupa() == null) {            nbStupa--;            currentPosition.setStupa(this);        } else {            System.out.println("Vous n'avez plus de Stupa.");        }    }    public boolean asCompletedOrder() {        return completedOrder;    }    public void setCompletedOrder(boolean completedOrder, int villageId, int nbYacksOrder) {        this.villageOrderId = villageId;        this.completedOrder = completedOrder;        this.nbYacksOrder = nbYacksOrder;    }    public int getVillageOrderId() {        return villageOrderId;    }    public int getNbYacksOrder() {        return nbYacksOrder;    }    public void addResource(Resource resource) {        this.resources.add(resource);    }    public void addAction(Action action) {        this.actions.add(action);    }    public int getPoliticalScore() {        return politicalScore;    }    public int getReligiousScore() {        return religiousScore;    }    public int getEconomicScore() {        return economicScore;    }    public String getColor() {        return color;    }    public Village getPosition() {        return currentPosition;    }    public void setPosition(Village v) {        currentPosition = v;    }    public void resetActions() {        actions.clear();    }    public Action getAction(int i) {        return actions.get(i);    }    public void move(Village dest) {        currentPosition = dest;    }    public ArrayList<Resource> getResources() {        return resources;    }    public int getNbResources(Resource.Type type) {        int result = 0;        for (Resource resource : resources) {            if (resource.getType().equals(type)) {                result++;            }        }        return result;    }    public void removeResource(Resource r) {        resources.remove(r);    }    public Resource getSpecificResource(Resource.Type type) {        for (Resource res : resources) {            if (res.getType().equals(type)) {                return res;            }        }        return null;    }    public void clearActions() {        actions.clear();    }    }