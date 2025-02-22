import javax.persistence.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

enum DeliveryType {Delivery, EcoDelivery, ContactlessDelivery}

@Entity
public class Delivery implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int deliveryId;

    // Kontener asocjacja z atrybutem
    @OneToMany
    private static List<TimeOfDelivery> timesOfDelivery = new ArrayList<>();

    @Basic
    private String noteForDeliveryMan = "Product(s) should be delivered contactless.";

    @Basic
    private String noteForWaiter = "Pack in eco package";

    @Basic
    private boolean isEcoPackage;

    @Basic
    private boolean isContactless;

    @Basic
    private String addressOfDelivery;

    // EnumSet overlapping
    @CollectionTable
    private EnumSet<DeliveryType> deliveryType = EnumSet.of(DeliveryType.Delivery);


    public Delivery() {}

    // Konstruktor
    public Delivery(EnumSet<DeliveryType> deliveryType, String addressOfDelivery) {
        this.deliveryType = deliveryType;
        this.addressOfDelivery = addressOfDelivery;
    }

    // Utowrzenie i usunięcie typów dostawy do dziedziczenia
    public void makeEcoDelivery() {
        this.isEcoPackage = true;
        this.deliveryType.add(DeliveryType.EcoDelivery);
    }

    public void removeEcoDelivery() {
        this.isEcoPackage = false;
        this.deliveryType.remove(DeliveryType.EcoDelivery);
    }

    public void makeContactlessDelivery() {
        this.isContactless = true;
        this.deliveryType.add(DeliveryType.ContactlessDelivery);
    }

    public void removeContactlessDelivery() {
        this.isContactless = false;
        this.deliveryType.remove(DeliveryType.ContactlessDelivery);
    }

    // Dodanie i usunięcie czasu dostawy do asocjacji z atrybutem
    public void addTimeOfDelivery(TimeOfDelivery timeOfDelivery) {
        if (timeOfDelivery == null || timeOfDelivery.getDelivery() != this) {
            throw new IllegalArgumentException("Time Of Delivery cannot be null or is not related to this Order");
        }
        if (!timesOfDelivery.contains(timeOfDelivery)) {
            timesOfDelivery.add(timeOfDelivery);
            timeOfDelivery.setDelivery(this);
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

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    // gettery i settery overlapping
    public String getNoteForDeliveryMan() {
        if (!this.deliveryType.contains(DeliveryType.ContactlessDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Contactless Delivery");
        }
        return noteForDeliveryMan;
    }

    public void setNoteForDeliveryMan(String noteForDeliveryMan) {
        if (!this.deliveryType.contains(DeliveryType.ContactlessDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Contactless Delivery");
        }
        this.noteForDeliveryMan=noteForDeliveryMan;
    }

    public boolean isEcoPackage() {
        if(!this.deliveryType.contains(DeliveryType.EcoDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Eco");
        }
        return isEcoPackage;
    }

    public void setEcoPackage(boolean ecoPackage) {
        if(!this.deliveryType.contains(DeliveryType.EcoDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Eco");
        }
        this.isEcoPackage = ecoPackage;
    }

    public String getNoteForWaiter() {

        return noteForWaiter;
    }

    public void setNoteForWaiter(String noteForWaiter) {
        if(noteForWaiter == null || noteForWaiter.trim().isEmpty()) {
            throw new IllegalArgumentException("NoteForWaiter cannot be null");
        }
        this.noteForWaiter = noteForWaiter;
    }

    public boolean isContactless() {
        if(!this.deliveryType.contains(DeliveryType.ContactlessDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Contactless");
        }
        return isContactless;
    }

    public void setContactless(boolean contactless) {
        if(!this.deliveryType.contains(DeliveryType.ContactlessDelivery)) {
            throw new IllegalArgumentException("Delivery Type is not Contactless");
        }
        this.isContactless = contactless;
    }

    public String getAddressOfDelivery() {
        return addressOfDelivery;
    }

    public void setAddressOfDelivery(String addressOfDelivery) {
        if(addressOfDelivery == null || addressOfDelivery.trim().isEmpty()) {
            throw new IllegalArgumentException("AddressOfDelivery cannot be null or empty");
        }
        this.addressOfDelivery = addressOfDelivery;
    }

    public EnumSet<DeliveryType> getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(EnumSet<DeliveryType> deliveryType) {
        this.deliveryType = deliveryType;
    }
}
