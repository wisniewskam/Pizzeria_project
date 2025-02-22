import javax.persistence.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "Product")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Basic
    private String name;

    @Basic
    private float basePrice;

    @ManyToOne
    @JoinColumn(name = "menuId")
    private Menu menu;

    @ManyToMany
    private List<Order> orders = new ArrayList<>();

    @ManyToOne
    private Employee employee;

    @Convert(converter = IngredientsConverter.class)
    private List<String> ingredients;

    public Product() {}

    public Product(String name, float basePrice, ArrayList<String> ingredients) {
        this.name = name;
        this.basePrice = basePrice;
        this.ingredients = ingredients;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
        if (menu != null && !menu.getProducts().contains(this)) {
            menu.addProduct(this);
        }
    }

    public void removeMenu(Menu menu){
        if(this.menu != null){
            this.menu.removeProduct(this);
        }
    }

    public void setEmployee(Employee employee){
        if(this.employee == null){
            this.employee = employee;
        }else{
            System.out.println("Product is already assigned to one employee");
        }
    }

    public void removeEmployee(Employee employee){
        if(this.employee != null){
            this.employee.removeProduct(this);
        }
    }

    //Metody do kompozycji
    public void addOrder(Order order){
        if(!orders.contains(order)){
            orders.add(order);
        }
    }

    public void removeOrder(Order order){
        if(orders.contains(order)){
            orders.remove(order);
        }
    }

    @ElementCollection
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public float getFinalPrice(){
        return basePrice;
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Error, Name cannot be null or empty");
        }
        this.name = name;
    }

    public float getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(float basePrice) {
        if(basePrice < 0.0F) {
            throw new IllegalArgumentException("Error, basePrice cannot be negative");
        }
        this.basePrice = basePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return name + " " + ingredients + "\t cena: PLN " + getBasePrice();
    }

    public void setOrder(Order order) {
        orders.add(order);
    }
}
