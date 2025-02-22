import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Paid extends Order{

    @OneToMany
    private Set<Order> orders = new HashSet<>();

    public Paid() {}

    public Paid(String datOfOrder, Product product, int orderId) {
        super(datOfOrder, product, orderId);
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void addOrders(Order order) throws Exception {
        if (!orders.contains(order)) {
            orders.add(order);

            if (order.getPaid() != this) {
                order.setPaid(this);
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
        return "Paid";
    }
}
