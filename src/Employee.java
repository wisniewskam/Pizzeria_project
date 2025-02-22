import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int employeeId;

    @Basic(optional = false)
    private String firstName;

    @Basic(optional = false)
    private String lastName;

    // Asocjacje
    @OneToMany
    private List<Product> products = new ArrayList<>();

    @OneToMany
    private List<Order> orders = new ArrayList<>();

    public Employee() {}

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.contains(product)) {
            products.add(product);
            product.setEmployee(this);
        }
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.contains(product)) {
            products.remove(product);
            product.removeEmployee(this);
        }
    }

    public void addOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (!orders.contains(order)) {
            orders.add(order);
            order.setEmployee(this);
        }
    }

    public void removeOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.contains(order)) {
            products.remove(order);
            order.removeEmployee(this);
        }
    }

    //Gettery i settery
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error, firstName cannot be null or empty");
        }
        this.firstName = firstName;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error, lastName cannot be null or empty");
        }
        this.lastName = lastName;
    }
}
