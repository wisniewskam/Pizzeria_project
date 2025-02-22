import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;


public class Main extends Application {

    private static StandardServiceRegistry registry = null;
    private static SessionFactory sessionFactory = null;

    public static void main(String[] args) {

        // Sprawdzenie połączenia z bazą SQLite
        String url = "jdbc:sqlite:mas_db.db";
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("Połączenio poprawnie z bazą");
            }
        } catch (SQLException e) {
            System.out.println("Błąd połączenia: " + e.getMessage());
        }

        // Konfiguracja Hibernate i sesja
        try {
            registry = new StandardServiceRegistryBuilder().configure().build();
            sessionFactory = new Configuration().configure("hibernate.cfg.xml")
                    .buildSessionFactory();
            System.out.println("Hibernate SessionFactory utworzone!");
        } catch (Exception e) {
            System.err.println("Błąd konfiguracji Hibernate: " + e.getMessage());
            if (registry != null) {
                StandardServiceRegistryBuilder.destroy(registry);
            }
        }

        // saveDataToDb(); // dane już są w tabelach
        launch(args);

        if (sessionFactory != null) {
            sessionFactory.close();
            System.out.println("Zamknięto SessionFactory.");
        }
    }

    // Zapisanie danych do bazy danych
    private static void saveDataToDb() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Employee e1 = new Employee( "a", "b");
        Customer c1 = new Customer("ABC SA");

        Menu menu1 = new Menu("Salty Pizza");
        Menu menu2 = new Menu("Napoje");
        Menu menu3 = new Menu("Desery");
        Menu menu4 = new Menu("Sweet Pizza");

        Product pizza1 = new Product("MARGHERITA", 30,  new ArrayList<>(Arrays.asList("sos pomidorowy", "mozzarella")) );
        Product pizza2 = new Product("3CHEESE", 35,  new ArrayList<>(Arrays.asList("sos kremowy", "mozzarella", "cheddar", "pleśniowy")) );
        Product pizza3 = new Product("PEPPERONI", 40,  new ArrayList<>(Arrays.asList("sos pomidorowy", "pepperoni")) );

        Product beverage1 = new Product("WODA GAZOWANA", 10,  new ArrayList<>(Arrays.asList("woda")) );
        Product beverage2 = new Product("HERBATA ZIMOWA", 12,  new ArrayList<>(Arrays.asList("herbata czarna", "cukier", "przyprawa")) );
        Product beverage3 = new Product("KAWA PIERNIKOWA", 15,  new ArrayList<>(Arrays.asList("espresso", "mleko", "syrop")) );

        Product dessert1 = new Product("TIRAMISU", 20,  new ArrayList<>(Arrays.asList("biszkopty", "cukier", "mascarpone")) );
        Product dessert2 = new Product("PANNA COTTA", 25,  new ArrayList<>(Arrays.asList("śmietana", "sos truskawkowy")) );
        Product dessert3 = new Product("CANNOLI", 20,  new ArrayList<>(Arrays.asList("ciasto", "ricotta", "czekolada")) );

        Product sweetp1 = new Product("CHOCO", 20,  new ArrayList<>(Arrays.asList("czekolada", "pianki")));
        Product sweetp2 = new Product("FRUITS", 25,  new ArrayList<>(Arrays.asList("maliny", "truskawki", "banany", "wiśnie")));
        Product sweetp3 = new Product("CIASTECZKA", 20,  new ArrayList<>(Arrays.asList("ciasteczka", "bita śmietana")) );

        menu1.addProduct(pizza1);
        menu1.addProduct(pizza2);
        menu1.addProduct(pizza3);

        menu2.addProduct(beverage1);
        menu2.addProduct(beverage2);
        menu2.addProduct(beverage3);

        menu3.addProduct(dessert1);
        menu3.addProduct(dessert2);
        menu3.addProduct(dessert3);

        menu4.addProduct(sweetp1);
        menu4.addProduct(sweetp2);
        menu4.addProduct(sweetp3);

        pizza1.setEmployee(e1);
        pizza2.setEmployee(e1);
        pizza3.setEmployee(e1);
        beverage1.setEmployee(e1);
        beverage2.setEmployee(e1);
        beverage3.setEmployee(e1);
        sweetp1.setEmployee(e1);
        sweetp2.setEmployee(e1);
        sweetp3.setEmployee(e1);
        dessert1.setEmployee(e1);
        dessert2.setEmployee(e1);
        dessert3.setEmployee(e1);

        session.saveOrUpdate(menu1);
        session.saveOrUpdate(menu2);
        session.saveOrUpdate(menu3);
        session.saveOrUpdate(menu4);

        session.saveOrUpdate(pizza1);
        session.saveOrUpdate(pizza2);
        session.saveOrUpdate(pizza3);
        session.saveOrUpdate(beverage1);
        session.saveOrUpdate(beverage2);
        session.saveOrUpdate(beverage3);
        session.saveOrUpdate(dessert1);
        session.saveOrUpdate(dessert2);
        session.saveOrUpdate(dessert3);
        session.saveOrUpdate(sweetp1);
        session.saveOrUpdate(sweetp2);
        session.saveOrUpdate(sweetp3);


        session.saveOrUpdate(e1);
        session.saveOrUpdate(c1);

        session.getTransaction().commit();
        session.close();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/order_view.fxml"));
        Parent root = loader.load();
        root.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        primaryStage.setTitle("Salty&Sweet - Złóż zamówienie");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
     }
}