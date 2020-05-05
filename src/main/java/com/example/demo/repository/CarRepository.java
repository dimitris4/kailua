package com.example.demo.repository;

import com.example.demo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Repository
public class CarRepository {

    @Autowired
    private JdbcTemplate template;


    public List<Car> fetchAll() {
        return template.query("SELECT car_id, car_registration_number, car_first_registration, car_odometer, " +
                "status_description, brand_name, model_name, rental_type_name, fueL_type\n" +
                "FROM car INNER JOIN brand USING (brand_id)\n" +
                "         INNER JOIN model USING (model_id)\n" +
                "         INNER JOIN fuel USING (fuel_id)\n" +
                "         INNER JOIN status USING (status_id)\n" +
                "         INNER JOIN rental_type USING (rental_type_id) " +
                "ORDER BY status_id, rental_type_name", new CarRowMapper());
    }

    // add new
    public int addCar(final Car car, int brandId, int modelId, int fuelId, int rentalTypeId, int statusId) {
        final String sql = "INSERT INTO car (car_id, car_registration_number, car_first_registration, car_odometer," +
                " fuel_id, model_id, brand_id, rental_type_id, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, car.getCarId());
                ps.setString(2, car.getRegistrationNumber());
                java.sql.Date sqlDate = Date.valueOf(car.getFirstRegistration());
                ps.setDate(3, sqlDate);
                ps.setInt(4, car.getOdometer());
                ps.setInt(5, fuelId);
                ps.setInt(6, modelId);
                ps.setInt(7, brandId);
                ps.setInt(8, rentalTypeId);
                ps.setInt(9, statusId);
                return ps;
            }
        }, holder);

        int newCarId = holder.getKey().intValue();
        return newCarId;
    }

    // find by ID
    public Car findById(int id) {
        return template.queryForObject("SELECT car_id, car_registration_number, car_first_registration, car_odometer, " +
                "status_description, brand_name, model_name, rental_type_name, fueL_type\n" +
                "FROM car INNER JOIN brand USING (brand_id)\n" +
                "         INNER JOIN model USING (model_id)\n" +
                "         INNER JOIN fuel USING (fuel_id)\n" +
                "         INNER JOIN rental_type USING (rental_type_id)" +
                "         INNER JOIN status USING (status_id) " +
                "WHERE car_id = ? ", new Object[]{id}, new CarRowMapper());
    }

    // delete car
    public Boolean deleteCar(int carId) {
        String sql1 = "DELETE FROM contract WHERE car_id = ? ";
        template.update(sql1, carId);
        String sql2 = "DELETE FROM car " +
                      "WHERE car_id = ? ";
        template.update(sql2, carId);
        return true;
    }

    // update
    public Car updateCar(Car car, int brandId, int modelId, int fuelId, int rentalTypeId, int statusId) {
        System.out.println("car= " + car);
        String updateStatement = "UPDATE car " +
                                 "SET car_registration_number = ?, car_first_registration = ?, car_odometer = ?, " +
                                 " fuel_id = ?, model_id = ?, brand_id = ?, rental_type_id = ?, status_id = ? " +
                                 "WHERE car_id = ?";

        template.update(updateStatement, car.getRegistrationNumber(), car.getFirstRegistration(), car.getOdometer(), fuelId,
                modelId, brandId, rentalTypeId, statusId, car.getCarId());

        return car;
    }

    // add new brand to brand table
    public int addBrand(final Car car) {
        final String sql = "INSERT INTO brand (brand_name) VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, car.getBrand());
                return ps;
            }
        }, holder);

        int brandId = holder.getKey().intValue();
        return brandId;
    }

    // add new model to model table
    public int addModel(final Car car) {
        final String sql = "INSERT INTO model (model_name) VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, car.getModel());
                return ps;
            }
        }, holder);

        int modelId = holder.getKey().intValue();
        return modelId;
    }

    public List<String> findAllBrands() {
        String sql = "SELECT brand_name FROM brand";
        List<String> brands = template.queryForList(sql, String.class);
        return brands;
    }

    public List<String> findAllModels() {
        String sql = "SELECT model_name FROM model";
        List<String> models = template.queryForList(sql, String.class);
        return models;
    }

    public List<String> findAllFuelTypes() {
        String sql = "SELECT fuel_type FROM fuel";
        List<String> fuels = template.queryForList(sql, String.class);
        return fuels;
    }

    public List<String> findAllRentalTypes() {
        String sql = "SELECT rental_type_name FROM rental_type ORDER BY rental_type_name";
        List<String> rentalTypes = template.queryForList(sql, String.class);
        System.out.println(rentalTypes);
        return rentalTypes;
    }

    public List<String> findAllStatuses() {
        String sql = "SELECT status_description FROM status ORDER BY status_description";
        List<String> statuses = template.queryForList(sql, String.class);
        return statuses;
    }

    public int findBrandIdByBrandName(String brand) {
        String sql = "SELECT brand_id FROM brand WHERE brand_name = ?";
        return template.queryForObject(sql, new Object[]{brand}, Integer.class);
    }


    public int findModelIdByModelName(String model) {
        String sql = "SELECT model_id FROM model WHERE model_name = ?";
        return template.queryForObject(sql, new Object[]{model}, Integer.class);
    }

    public int findFuelIdByFuelName(String fuel) {
        String sql = "SELECT fuel_id FROM fuel WHERE fuel_type = ?";
        return template.queryForObject(sql, new Object[]{fuel}, Integer.class);
    }

    public int findRentalTypeIdByRentalTypeName(String rentalType) {
        String sql = "SELECT rental_type_id FROM rental_type WHERE rental_type_name = ?";
        return template.queryForObject(sql, new Object[]{rentalType}, Integer.class);
    }

    public int findStatusIdByStatusDescription(String status) {
        String sql = "SELECT status_id FROM status WHERE status_description = ?";
        return template.queryForObject(sql, new Object[]{status}, Integer.class);
    }


    public List<Car> findAvailableCars(Contract contract) {
        LocalDate fromDate = contract.getStartDate();
        LocalDate toDate = contract.getEndDate();
        // System.out.println(fromDate + " " + toDate);
        Date sqlFromDate = Date.valueOf(fromDate);
        Date sqlToDate = Date.valueOf(toDate);
        // System.out.println(sqlFromDate + " " + sqlToDate);
        String sql=
                "SELECT DISTINCT rental_type_name, car_id, car_registration_number, brand_name, model_name, " +
                "car_first_registration, car_odometer, fuel_type, status_description " +
                "FROM car LEFT JOIN contract USING (car_id)" +
                "INNER JOIN brand USING (brand_id)" +
                "INNER JOIN model using (model_id)" +
                "INNER JOIN fuel using (fuel_id)" +
                "INNER JOIN rental_type USING (rental_type_id)" +
                "INNER JOIN status USING (status_id) " +
                "WHERE status_id = 1 AND car_id NOT IN" +
                "(" +
                    "SELECT car_id " +
                    "FROM car LEFT JOIN contract USING (car_id)" +
                    "WHERE (contract_start_time <= ? AND contract_end_time >= ?) " +
                    "OR (contract_start_time >= ? AND contract_end_time <= ?)" +
                    "OR (contract_start_time >= ? AND contract_end_time >= ? AND contract_start_time <= ?)" +
                    "OR (contract_start_time <= ? AND contract_end_time <= ? AND contract_end_time >= ?)" +
                ")" +
                "ORDER BY rental_type_name";
        List<Car> result = template.query(sql, new Object[] {sqlFromDate, sqlToDate, sqlFromDate, sqlToDate, sqlFromDate,
                sqlToDate, sqlFromDate, sqlFromDate, sqlToDate, sqlToDate}, new CarRowMapper());
        System.out.println(result);
        return result;
    }

    public List<Integer> findCarsWithActiveContracts() {
        String sql = "SELECT car_id " +
                     "FROM contract " +
                     "WHERE CURRENT_DATE() < contract_start_time";
        List<Integer> carIds = template.queryForList(sql, Integer.class);
        return carIds;
    }
}

class CarRowMapper implements RowMapper<Car> {
    @Override
    public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
        Car car = new Car();
        car.setCarId(rs.getInt("car_id"));
        car.setRegistrationNumber(rs.getString("car_registration_number"));
        Date sqlDate = rs.getDate("car_first_registration");
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        car.setFirstRegistration(localDate);
        car.setOdometer(rs.getInt("car_odometer"));
        car.setBrand(rs.getString("brand_name"));
        car.setModel(rs.getString("model_name"));
        car.setRentalType(rs.getString("rental_type_name"));
        car.setFuel(rs.getString("fuel_type"));
        car.setStatus(rs.getString("status_description"));
        return car;
    }
}

