import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
public class Dessert extends Product {
    private double amountOfSugar;
    private static float dessertTax = 0.23F;

    public Dessert() {}

    public Dessert(String name, float basePrice, ArrayList<String> ingredients) {
        super(name, basePrice, ingredients);
    }

    public double getAmountOfSugar() {
        return amountOfSugar;
    }

    public void setAmountOfSugar(double amountOfSugar) {
        this.amountOfSugar = amountOfSugar;
    }

    public static float getDessertTax() {
        return dessertTax;
    }

    public static void setDessertTax(float dessertTax) {
       if(dessertTax < 0 || dessertTax > 1) {
           throw new IllegalArgumentException("Dessert tax should be between 0 and 1");
       }
       Dessert.dessertTax = dessertTax;
    }

    @Override
    public float getFinalPrice() {
        return getBasePrice() * (1 + dessertTax);
    }
}
