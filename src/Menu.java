import javax.persistence.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Menu")
public class Menu implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int menuId;

    @Basic
    private String menuName;

    @OneToMany(mappedBy = "menu")
    private List<Product> products = new ArrayList<>();

    public Menu() {}

    public Menu( String menuName) {
        this.menuName = menuName;
    }

    public void addProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.contains(product)) {
            products.add(product);
            product.setMenu(this);
        }
    }

    public void removeProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (!products.contains(product)) {
            products.remove(product);
            product.removeMenu(this);
        }
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        if (this.menuName == null || this.menuName.trim().isEmpty()) {
            throw new IllegalArgumentException("Error, Name cannot be null or empty");
        }
        this.menuName = menuName;
    }

    public List<Product> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return menuName;
    }

}
