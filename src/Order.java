import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE", discriminatorType = DiscriminatorType.STRING)
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int orderId;

    @ManyToOne
    private Customer customer;

    @Basic(optional = false)
    private String dateOfOrder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

    @ManyToOne
    private Employee employee;

    @ManyToMany
    private List<Product> product = new ArrayList<>();

    @OneToMany
    private static List<TimeOfDelivery> timesOfDelivery = new ArrayList<>();

    @Basic
    private Paid paid;

    @Basic
    private Unpaid unpaid;

    private static Map<String, Set<Object>> attributeValues = new HashMap<>();

    public Order() {}

    public Order(String datOfOrder, Product product, int orderId) {
        this.dateOfOrder = datOfOrder;
        product.addOrder(this);
        this.orderId = orderId;
    }

    public Order(String datOfOrder, Product product, int orderId, Paid paid) throws Exception {
        this.dateOfOrder = datOfOrder;
        product.addOrder(this);
        this.orderId = orderId;
        setPaid(paid);
    }

    public Order(String datOfOrder, Product product, int orderId, Unpaid unpaid) throws Exception {
        this.dateOfOrder = datOfOrder;
        product.addOrder(this);
        this.orderId = orderId;
        setUnpaid(unpaid);
    }

    public Paid getPaid() {
        return paid;
    }

    public void setPaid(Paid paid) throws Exception {
        if(this.unpaid != null){
            throw new Exception("Unpaid already set");
        }
        if(this.paid != null){
            this.paid.removeOrder(this);
        }
        this.paid = paid;
        this.paid.addOrders(this);
    }

    public Unpaid getUnpaid() {
        return unpaid;
    }

    public void setUnpaid(Unpaid unpaid) throws Exception {
        if(this.paid != null){
            throw new Exception("Paid already set");
        }
        if(this.unpaid != null){
            this.unpaid.removeOrder(this);
        }
        this.unpaid = unpaid;
        this.unpaid.addOrders(this);
    }

    public void addTimeOfDelivery(TimeOfDelivery timeOfDelivery) {
        if (timeOfDelivery == null || timeOfDelivery.getOrder() != this) {
            throw new IllegalArgumentException("Time Of Delivery cannot be null or is not related to this Order");
        }
        if (!timesOfDelivery.contains(timeOfDelivery)) {
            timesOfDelivery.add(timeOfDelivery);
        }
    }

    public void removeTimeOfDelivery(TimeOfDelivery timeOfDelivery) {
        if (timeOfDelivery == null) {
            throw new IllegalArgumentException("Time Of Delivery cannot be null");
        }
        if (!timesOfDelivery.contains(timeOfDelivery)) {
            timesOfDelivery.remove(timeOfDelivery);
        }
    }

    // Metody do asocjacji kwalifikowanej
    public void addCustomer(Customer customer) {
        if (this.customer == null) {
            this.customer = customer;
            customer.addOrder(this);
        }
    }

    public void removeCustomer(Customer customer) {
        if (customer != null) {
            customer.removeOrder(this);
        }
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("product cannot be nulla");
        }
        this.product.add(product);

    }

    public void removeProduct(Product product) {
        if (product != null) {
            product.removeOrder(this);
        }

    }

    // Asocjacja
    public void setEmployee(Employee employee) {
        if (this.employee == null) {
            this.employee = employee;
            employee.addOrder(this);
        }
    }

    public void removeEmployee(Employee employee) {
        if (employee != null) {
            employee.removeOrder(this);
        }
    }


    private void checkUniquenessOf(String attribute, Object value) throws Exception {
        String attributeKey = this.getClass().getName() + "." + attribute;
        if (attributeValues.containsKey(attributeKey)) {
            for (Object o : attributeValues.get(attributeKey)) {
                if (o.equals(value)) {
                    throw new Exception("Record does exist! Put unique value");
                }
            }
        } else {
            attributeValues.put(attributeKey, new HashSet<>());
        }
        attributeValues.get(attributeKey).add(value);
    }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(String dateOfOrder) {
        if(dateOfOrder == null) {
            throw new IllegalArgumentException("Error, date Of Order cannot be null");
        }
        this.dateOfOrder = dateOfOrder;
    }

    public float getTotalPrice() {
        float totalPrice = 0;
        for (Product product : product){
            totalPrice += product.getFinalPrice();
        }

        return totalPrice;
    }

    public static List<TimeOfDelivery> getTimesOfDelivery() {
        return timesOfDelivery;
    }


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        try {
            checkUniquenessOf("orderId", orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.orderId = orderId;
    }

    public List<Product> getProduct() {
        return product;
    }

    public void setProduct(List<Product> selectedProducts) {
        this.product = selectedProducts;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                '}';
    }

}
