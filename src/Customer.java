import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
public class Customer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;

    @Basic
    private String name;

    public Customer() {}

    public Customer(String name) {
        this.name = name;
    }

    // Asocjacja kwalifikowana
    @ElementCollection
    @MapKeyColumn(name = "orderId")
    private Map<Integer,Order> ordersQualif = new TreeMap<>();

    public void addOrder(Order order){
        if(order == null){
            throw new IllegalArgumentException("Record cannot be null");
        }
        if(!ordersQualif.containsKey(order.getOrderId())){
            ordersQualif.put(order.getOrderId(), order);
            order.addCustomer(this);
        }
    }

    // Usunięcie Order razem z kwalifikatorem
    public void removeOrder(Order order){
        if(order == null){
            throw new IllegalArgumentException("Record cannot be null");
        }
        if(ordersQualif.containsKey(order.getOrderId())){
            ordersQualif.remove(order.getOrderId());
            order.removeCustomer(this);
        }
    }

    // Kwalifikator
    public void addOrdersQualif(Order newOrder) {
        if(!ordersQualif.containsKey(newOrder.getOrderId())){
            ordersQualif.put(newOrder.getOrderId(), newOrder);
            newOrder.addCustomer(this);
        }
    }

    public Order findOrderQualif(String orderId) {
        if(!ordersQualif.containsKey(orderId)){
            throw new IllegalArgumentException("Order id  not found");
        }
        return ordersQualif.get(orderId);
    }

    // Gettery settery
    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Error, name cannot be null or empty");
        }
        this.name = name;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                '}';
    }

}
