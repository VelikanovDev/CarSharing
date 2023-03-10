package carsharing.DAOImpl;

import carsharing.Database;
import carsharing.DAO.CustomerDAO;
import carsharing.model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    @Override
    public Customer get(int id) {
        Customer customer = null;
        String sql = "SELECT id, name, rented_car_id FROM carsharing.customer WHERE id = ?";

        try (Connection conn = Database.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int cusId = rs.getInt("id");
                String name = rs.getString("name");
                int rentedCarId = rs.getInt("rented_car_id");
                customer = new Customer(cusId, name, rentedCarId);
            }

            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customer;
    }

    @Override
    public List<Customer> getAll() {
        String sql = "SELECT id, name, rented_car_id FROM carsharing.customer";
        List<Customer> list = new ArrayList<>();

        try (Connection conn = Database.getConnection();
             Statement state = conn.createStatement();
             ResultSet rs = state.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int rentedCarId = rs.getInt("rented_car_id");
                list.add(new Customer(id, name, rentedCarId));
            }

            Database.closeStatement(state);
            Database.closeResultSet(rs);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list;
    }

    @Override
    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS customer (
                    id INT UNSIGNED PRIMARY KEY NOT NULL AUTO_INCREMENT,
                    name VARCHAR(255) UNIQUE NOT NULL,
                    rented_car_id INT UNSIGNED DEFAULT NULL,
                    FOREIGN KEY (rented_car_id) REFERENCES CAR(id)
                    ON DELETE SET NULL
                    ON UPDATE CASCADE
                );""";

        try (Connection conn = Database.getConnection()) {
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql);

            Database.closeStatement(statement);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void dropTable() {
        String sql = "DROP TABLE IF EXISTS customer";
        try (Connection conn = Database.getConnection()) {
            Statement statement = conn.createStatement();

            statement.executeUpdate(sql);
            Database.closeStatement(statement);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void insert(Customer customer) {
        String sql = "INSERT INTO carsharing.customer (name) VALUES(?)";

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, customer.getName());
            ps.executeUpdate();

            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(Customer customer, int carId) {

        String sql = "UPDATE carsharing.customer SET rented_car_id = ? WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, carId);
            ps.setInt(2, customer.getId());
            ps.executeUpdate();

            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateRentedCarId(Customer customer) {
        String sql = "UPDATE carsharing.customer SET rented_car_id = NULL WHERE id = ?";

        try (Connection conn = Database.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, customer.getId());
            ps.executeUpdate();

            Database.closePreparedStatement(ps);
            Database.closeConnection(conn);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}