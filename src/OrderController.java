import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderController {

    @FXML
    private ListView<Menu> menuListView;

    @FXML
    private ListView<CheckBox> productListView;

    @FXML
    private ListView<CheckBox> orderListView;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Spinner<Integer> hourSpinner;

    @FXML
    private TextField addressField;

    @FXML
    private CheckBox ecoPackageCheckBox;

    @FXML
    private CheckBox contactlessCheckBox;

    private SessionFactory factory;
    private int orderId;
    private List<Product> selectedProducts;

    @FXML
    public void initialize() {
        factory = new Configuration().configure("hibernate.cfg.xml")
                .addAnnotatedClass(Menu.class)
                .addAnnotatedClass(Product.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(Delivery.class)
                .addAnnotatedClass(TimeOfDelivery.class)
                .buildSessionFactory();

        loadMenus();

        // Spinner do czasu dostawy 6 do 22
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 22, 20);
        hourSpinner.setValueFactory(valueFactory);

        menuListView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> handleMenuSelection()
        );

        // Wybór daty i blokowanie dat wcześniejszych niż dziś
        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(date.isBefore(LocalDate.now()));
            }
        });
    }

    // Wczytanie Menu
    private void loadMenus() {
        try (Session session = factory.openSession()) {
            session.beginTransaction();
            List<Menu> menus = session.createQuery("from Menu", Menu.class).getResultList();
            session.getTransaction().commit();
            menuListView.getItems().setAll(menus);
        }
    }

    // Wybór Menu i wyświetlenie Produktów
    @FXML
    private void handleMenuSelection() {
        Menu selectedMenu  =menuListView.getSelectionModel().getSelectedItem();
        if (selectedMenu == null) return;

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            selectedMenu = session.find(Menu.class, selectedMenu.getMenuId());
            Hibernate.initialize(selectedMenu.getProducts());
            session.getTransaction().commit();

            productListView.getItems().clear();

            for (Product product : selectedMenu.getProducts()) {
                CheckBox checkBox = new CheckBox(product.toString());
                checkBox.setUserData(product);
                productListView.getItems().add(checkBox);
            }
        }
    }

    // Dodanie produktów do listy do zamówienia
    @FXML
    private void addToOrder() {
        for (CheckBox checkBox : productListView.getItems()) {
            if (checkBox.isSelected()) {
                Product product = (Product) checkBox.getUserData();
                CheckBox orderCheckBox = new CheckBox(product.toString());
                orderCheckBox.setUserData(product);
                orderListView.getItems().add(orderCheckBox);
            }
        }
    }

    // Usunięcie wybranych z listy do zamówienia
    @FXML
    private void removeCheckedItems() {
        List<CheckBox> itemsToRemove = new ArrayList<>();

        for (CheckBox checkBox : orderListView.getItems()) {
            if (checkBox.isSelected()) {
                itemsToRemove.add(checkBox);
            }
        }

        if (itemsToRemove.isEmpty()) {
            showAlert("Błąd", "Nie wybrano żadnych produktów do usunięcia!");
            return;
        }

        orderListView.getItems().removeAll(itemsToRemove);
    }


    // Potwierdzenie szczegółów dostawy
    @FXML
    private void confirmDelivery() {
        LocalDate date = datePicker.getValue();
        int hour = hourSpinner.getValue();
        String address = addressField.getText();
        boolean ecoPackage = ecoPackageCheckBox.isSelected();
        boolean contactless = contactlessCheckBox.isSelected();

        if (date == null || address.isEmpty() || orderListView.getItems().isEmpty()) {
            showAlert("Błąd", "Proszę uzupełnić wszystkie wymagane dane i wybrać produkty.");
            return;
        }

        LocalDateTime selectedDateTime = LocalDateTime.of(date, LocalTime.of(hour, 0));
        LocalDateTime now = LocalDateTime.now();

        if (selectedDateTime.isBefore(now.plusHours(1))) {
            showAlert("Błąd", "Godzina dostawy musi być co najmniej godzinę późniejsza niż obecny czas.");
            return;
        }

        try (Session session = factory.openSession()) {
            session.beginTransaction();

            Order order = new Order();
            selectedProducts = new ArrayList<>();
            for (CheckBox item : orderListView.getItems()) {
                Product product = (Product) item.getUserData();
                selectedProducts.add(product);
            }

            for (Product product : selectedProducts) {
                order.addProduct(product);
                session.saveOrUpdate(product);
            }

            session.save(order);
            orderId = order.getOrderId();

            Delivery delivery = new Delivery();
            delivery.setAddressOfDelivery(address);

            if (ecoPackage) {
                delivery.getDeliveryType().add(DeliveryType.EcoDelivery);
            }

            if (contactless) {
                delivery.getDeliveryType().add(DeliveryType.ContactlessDelivery);
            }

            session.save(delivery);

            TimeOfDelivery time = new TimeOfDelivery();
            time.setStartOfDeliveryTime(now);
            time.setEndOfDeliveryTime(selectedDateTime);
            time.setDelivery(delivery);
            time.setOrder(order);

            session.save(time);
            session.getTransaction().commit();
        }

        askForPaymentStatus();
        showAlert("Sukces", "Dostawa została zapisana!");
        clearForm();
    }

    private void askForPaymentStatus() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Status płatności");
        alert.setHeaderText("Czy zamówienie zostało opłacone?");
        alert.setContentText("Wybierz odpowiednią opcję.");

        ButtonType paidButton = new ButtonType("Opłacono");
        ButtonType unpaidButton = new ButtonType("Nieopłacono");

        alert.getButtonTypes().setAll(paidButton, unpaidButton);

        Optional<ButtonType> result = alert.showAndWait();

        try (Session session = factory.openSession()) {
            session.beginTransaction();
            Order order = session.get(Order.class, orderId);
            if (order != null) {
                try {
                    if (result.isPresent() && result.get() == paidButton) {
                        Paid paid = new Paid();
                        paid.addOrders(order);
                        order.setPaid(paid);
                        session.save(paid);
                    } else {
                        Unpaid unpaid = new Unpaid();
                        unpaid.addOrders(order);
                        order.setUnpaid(unpaid);
                        session.save(unpaid);
                    }
                    session.update(order);
                } catch (Exception e) {
                    showAlert("Uwaga", "Platność zostanie sprawdzona przez pracownika w ciągu najbliższych dni.");
                }
            }
            session.getTransaction().commit();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        addressField.clear();
        datePicker.setValue(null);
        hourSpinner.getValueFactory().setValue(20);
        ecoPackageCheckBox.setSelected(false);
        contactlessCheckBox.setSelected(false);
        orderListView.getItems().clear();
        productListView.getItems().clear();
    }
}
