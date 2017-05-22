package core;

import java.util.ArrayList;

public class BagOrders {

    private ArrayList<Order> orders;

    public BagOrders() {
        orders = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            orders.add(new Order());
        }
    }

    /**
     * Constructeur par copie
     *
     * @param bo
     */
    public BagOrders(BagOrders bo) {
        this.orders = new ArrayList<>();
        bo.orders.forEach((order) -> {
            this.orders.add(new Order(order));
        });
    }

    /**
     * Pioche une carte commande aléatoire
     *
     * @return Order
     */
    public Order takeRandom() {
        int rand = (int) (Math.random() * orders.size());
        Order result = orders.get(rand);
        orders.remove(result);
        return result;
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
