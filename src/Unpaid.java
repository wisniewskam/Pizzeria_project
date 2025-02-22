import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Unpaid extends Order {

    @OneToMany
    private Set<Order> orders = new HashSet<>();

    public Unpaid() {}

    public Unpaid(String datOfOrder, Product product, int orderId) {
        super(datOfOrder, product, orderId);
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void addOrders(Order order) throws Exception {
        if (!orders.contains(order)) {
            orders.add(order);

            if (order.getUnpaid() != this) {
                order.setUnpaid(this);
            }
        }
    }

    public void removeOrder(Order order) {
        if (orders.contains(order)) {
            orders.remove(order);
        }
    }

    @Override
    public String toString() {
        return "Unpaid";
    }

}
