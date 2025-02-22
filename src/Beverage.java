
import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class Beverage extends Product {
    private static float beverageTax = 0.23F;

    public Beverage() {}

    public Beverage(String name, float basePrice, ArrayList<String> ingredients) {
        super(name, basePrice, ingredients);
    }

    public static float getBeverageTax() {
        return beverageTax;
    }

    public static void setBeverageTax(float beverageTax) {
        if(beverageTax > 1 || beverageTax < 0) {
            throw new IllegalArgumentException("Beverage tax should be between 0 and 1");
        }
        Beverage.beverageTax = beverageTax;
    }

    @Override
    public float getFinalPrice() {
        return getBasePrice() * (1 + beverageTax);
    }
}
