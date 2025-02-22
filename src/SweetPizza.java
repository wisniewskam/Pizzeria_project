
import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class SweetPizza extends Dessert implements IPizza {
    private float unhealthyProductTax = 0.05F;
    private float pizzaTax = 0.08F;

    public SweetPizza() {}

    public SweetPizza(String name, float basePrice, ArrayList<String> ingredients) {
        super(name, basePrice, ingredients);
    }

    public float getUnhealthyProductTax() {
        return unhealthyProductTax;
    }

    public void setUnhealthyProductTax(float unhealthyProductTax) {
        if(unhealthyProductTax < 0 || unhealthyProductTax > 1) {
            throw new IllegalArgumentException("Unhealthy Product Tax must be between 0 and 1");
        }
        this.unhealthyProductTax = unhealthyProductTax;
    }

    public float getPizzaTax() {
        return pizzaTax;
    }

    public void setPizzaTax(float pizzaTax) {
        if(pizzaTax < 0 || pizzaTax > 1) {
            throw new IllegalArgumentException("Pizza Tax must be between 0 and 1");
        }
        this.pizzaTax = pizzaTax;
    }

    // Polimorfizm
    @Override
    public float getFinalPrice() {
        return getBasePrice() + (1 + (getPizzaTax() * getUnhealthyProductTax()));
    }

    @Override
    public String toString() {
        return getName() + " - $" + getFinalPrice() + " - Sugar: " + getAmountOfSugar() + "g";
    }

}