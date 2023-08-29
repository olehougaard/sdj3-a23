package dk.via.cars;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RunTestDaoServer {
    public static void main(String[] args) throws Exception {
        DatabaseHelper<CarDTO> helper = new DatabaseHelper<>("jdbc:postgresql://localhost:5432/postgres?currentSchema=car_base", "postgres", "password");
        helper.executeUpdate("DELETE FROM car");
        helper.executeUpdate("INSERT INTO car VALUES(?, ?, ?, ?, ?)", "AV 41 213", "Ford", 2014, 4999.99, "EUR");
        CarDAOServer carDAOServer = new CarDAOServer(helper);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("carDao", carDAOServer);
        System.out.println("Test DAO server running");
    }
}
