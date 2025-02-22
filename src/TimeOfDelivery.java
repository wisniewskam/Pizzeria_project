import javax.persistence.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class TimeOfDelivery implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int timeId;

    @Basic
    private LocalDateTime startOfDeliveryTime;

    @Basic
    private LocalDateTime endOfDeliveryTime;

    @ManyToOne
    private Delivery delivery;

    @ManyToOne
    private Order order;

    public TimeOfDelivery() {}

    public TimeOfDelivery(LocalDateTime endOfDeliveryTime, Delivery delivery, Order order) {
        this.endOfDeliveryTime = endOfDeliveryTime;
        this.delivery = delivery;
        this.order = order;
        order.addTimeOfDelivery(this);
        delivery.addTimeOfDelivery(this);
    }

    public static void removeTimeOfDelivery(TimeOfDelivery timeOfDelivery) {
        if (timeOfDelivery != null) {
            timeOfDelivery.order.removeTimeOfDelivery(timeOfDelivery);
            timeOfDelivery.delivery.removeTimeOfDelivery(timeOfDelivery);
        }
    }

    public LocalDateTime getStartOfDeliveryTime() {
        return startOfDeliveryTime;
    }

    public void setStartOfDeliveryTime(LocalDateTime startOfDeliveryTime) {
        if(startOfDeliveryTime == null) {
            throw new IllegalArgumentException("Error, startOfDeliveryTime cannot be null");
        }
        this.startOfDeliveryTime = startOfDeliveryTime;
    }

    public LocalDateTime getEndOfDeliveryTime() {
        return endOfDeliveryTime;
    }

    // Ograniczenie własne - endOfDeliveryTime nie może być przed startOfDelivery
    public void setEndOfDeliveryTime(LocalDateTime endOfDeliveryTime) {
        if(endOfDeliveryTime == null) {
            throw new IllegalArgumentException("EndOfDeliveryTime cannot be null");
        }else if(endOfDeliveryTime.isBefore(startOfDeliveryTime)) {
            throw new IllegalArgumentException("EndOfDeliveryTime cannot be before startOfDeliveryTime");
        }
        this.endOfDeliveryTime = endOfDeliveryTime;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        if(delivery == null){
            throw new IllegalArgumentException("Delivery cannot be null");
        }
        this.delivery = delivery;
    }

    public int getTimeId() {
        return timeId;
    }

    public void setTimeId(int timeId) {
        this.timeId = timeId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }
        this.order = order;
    }

}
