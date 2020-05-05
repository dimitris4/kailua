package com.example.demo.repository;

import com.example.demo.model.Contract;
import com.example.demo.model.PhoneNumber;
import com.example.demo.model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Repository
public class RenterRepository {

    @Autowired
    private JdbcTemplate template;

    @Transactional
    public List<Renter> findAll() {
        return template.query("SELECT renter_id, first_name, last_name, email, driver_license_number, driver_since, " +
                                "street, building, zip_code, city, country_name FROM renter " +
                                "INNER JOIN address ON address.address_id = renter.renter_address " +
                                "INNER JOIN zip USING (zip_id)" +
                                "INNER JOIN country USING (country_id)", new RenterRowMapper());
    }

    @Transactional
    public Renter findById(int id) {
        return template.queryForObject(
                "select renter_id, first_name, last_name, email, driver_license_number, driver_since, street, " +
                        "building, zip_code, city, country_name " +
                        "FROM renter\t\n" +
                        "    INNER JOIN  address ON renter.renter_address = address.address_id" +
                        "    INNER JOIN zip USING (zip_id)" +
                        "    INNER JOIN country USING (country_id)" +
                        " WHERE renter_id = ?",
                new Object[]{id}, new RenterRowMapper());
    }


    public int addCountry(final Renter renter) {
        final String sql = "INSERT INTO country (country_name) VALUES (?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, renter.getCountry());
                return ps;
            }
        }, holder);

        int countryId = holder.getKey().intValue();
        return countryId;
    }


    public int addZip(final Renter renter, int countryId) {
        final String sql = "INSERT INTO zip (zip_code, city, country_id) VALUES (?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, renter.getZip());
                ps.setString(2, renter.getCity());
                ps.setInt(3, countryId);
                return ps;
            }
        }, holder);

        int newZipId = holder.getKey().intValue();
        return newZipId;
    }


    public int addAddress(final Renter renter, int zipId) {
        final String sql = "INSERT INTO address (street, building, zip_id) VALUES (?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, renter.getStreet());
                ps.setInt(2, renter.getBuilding());
                ps.setInt(3, zipId);
                return ps;
            }
        }, holder);

        int addressId = holder.getKey().intValue();
        return addressId;
    }


    public int addRenter(final Renter renter, int addressId) {
        final String sql = "INSERT INTO renter (first_name, last_name, email, driver_license_number, driver_since, " +
                            "renter_address) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, renter.getFirstName());
                ps.setString(2, renter.getLastName());
                ps.setString(3, renter.getEmail());
                ps.setString(4, renter.getDriverLicenseNumber());
                java.sql.Date sqlDate = Date.valueOf(renter.getSinceDate());
                ps.setDate(5, sqlDate);
                ps.setInt(6, addressId);
                return ps;
            }
        }, holder);

        int newUserId = holder.getKey().intValue();
        return newUserId;
    }


    public void addPhoneNumber(final Renter renter) {
        final String sql = "INSERT INTO phone_number (renter_id, phone) VALUES (?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, renter.getRenterId());
                ps.setString(2, renter.getTelephone());
                return ps;
            }
        }, holder);
    }

    public List<String> findAllCountries() {
        String sql = "SELECT country_name FROM country";
        List<String> countries = template.queryForList(sql, String.class);
        return countries;
    }


    public List<String> findAllZips() {
        String sql = "SELECT zip_code FROM zip";
        List<String> zips = template.queryForList(sql, String.class);
        return zips;
    }

    public Integer findCountryId(String country) {
        String sql = "SELECT country_id FROM country WHERE country_name = ?";
        return template.queryForObject(sql, new Object[]{country}, Integer.class);
    }

    public Integer findZipId(String zip) {
        String sql = "SELECT zip_id FROM zip WHERE zip_code = ? LIMIT 1";
        return template.queryForObject(sql, new Object[]{zip}, Integer.class);
    }

    public Renter updateRenter(int id, Renter renter) {

        String updateStatement = "UPDATE renter r, address a, zip z, country c SET first_name = ?, last_name = ?, " +
                "email = ?, driver_license_number = ?, driver_since = ?, street = ?, building = ?, zip_code = ?, city = ?, country_name = ?" +
                "WHERE a.address_id = r.renter_address AND z.zip_id = a.address_id AND z.country_id = c.country_id AND renter_id = ?;";

        template.update(updateStatement, renter.getFirstName(), renter.getLastName(), renter.getEmail(),
                renter.getDriverLicenseNumber(), renter.getSinceDate(), renter.getStreet(), renter.getBuilding(),
                renter.getZip(), renter.getCity(), renter.getCountry(), id);

        return renter;
    }

    public Boolean deleteRenter(int id) {
        String sql1 = "DELETE FROM contract WHERE renter_id = ?";
        template.update(sql1, id);
        String sql2 = "DELETE FROM phone_number WHERE renter_id = ?";
        template.update(sql2, id);
        String sql3 = "DELETE renter, address FROM renter INNER JOIN address " +
                     "WHERE renter.renter_address = address.address_id AND renter.renter_id = ?";
        template.update(sql3, id);
        return true;
    }


    public List<PhoneNumber> findPhoneNumbersByRenterId(int renterId) {
        return template.query
                ("SELECT phone_id, phone FROM phone_number WHERE renter_id = ?",
                        new Object[] { renterId },
                        new PhoneNumberRowMapper());
    }


    public PhoneNumber findPhoneNumberById(int phoneId) {
        return template.queryForObject(
                "SELECT phone_id, phone " +
                    "FROM phone_number " +
                    "WHERE phone_id= ?;",
                new Object[]{phoneId}, new PhoneNumberRowMapper());
    }

    public void addTelephone(Renter renter, PhoneNumber phoneNumber) {
        template.update(
                "INSERT INTO phone_number (renter_id, phone) VALUES (?, ?)",
                renter.getRenterId(), phoneNumber.getPhone()
        );
    }

    public PhoneNumber updatePhoneNumber(int phoneId, PhoneNumber phoneNumber) {
        String updateStatement = "UPDATE phone_number SET phone = ? WHERE phone_id = ?";
        template.update(updateStatement, phoneNumber.getPhone(), phoneId);
        return phoneNumber;
    }

    public void deletePhoneNumber(int phoneId) {
        String sql1 = "DELETE FROM phone_number WHERE phone_id = ?";
        template.update(sql1, phoneId);
    }

    public List<Contract> findContractsByRenterId(int renterId) {
        return template.query
                ("SELECT renter_id, contract_id, contract_start_time, contract_end_time, contract_max_km, contract_actual_km " +
                        "FROM contract " +
                        "WHERE renter_id = ?",
                        new Object[] { renterId },
                        new ContractRowMapper());
    }

    public Contract findContractById(int contractId) {
        return template.queryForObject(
                "SELECT contract_id, contract_start_time, contract_end_time, contract_max_km, contract_actual_km " +
                    "FROM contract " +
                    "WHERE contract_id= ? ",
                new Object[]{contractId}, new ContractRowMapper());
    }

    public void updateContract(int contractId, Contract contract) {
        String updateStatement = "UPDATE contract SET contract_start_time = ?, contract_end_time = ?, contract_max_km = ?," +
                "contract_actual_km = ? WHERE contract_id = ?";
        template.update(updateStatement, contract.getStartDate(), contract.getEndDate(), contract.getMaxKm(),
                contract.getActualKm(), contractId);
    }

    public List<Integer> findRenterIdsWithActiveContracts() {
        String sql = "SELECT renter_id " +
                     "FROM contract " +
                     "WHERE CURRENT_DATE() < contract_start_time";
        List<Integer> renterIds = template.queryForList(sql, Integer.class);
        return renterIds;
    }

    public List<Renter> findByDriverLicenseNumber(String driverLicenseNumber) {
        String str = "%" + driverLicenseNumber + "%";
        return template.query
                ("SELECT renter_id, first_name, last_name, email, driver_license_number, driver_since, street, building, zip_code, city, country_name \n" +
                                "FROM renter INNER JOIN address ON address.address_id = renter.renter_address INNER JOIN zip USING (zip_id) INNER JOIN country USING (country_id) " +
                                "WHERE driver_license_number LIKE ? ;",
                        new Object[] { str },
                        new RenterRowMapper());
    }
}

class RenterRowMapper implements RowMapper<Renter> {
    @Override
    public Renter mapRow(ResultSet rs, int rowNum) throws SQLException {
        Renter renter = new Renter();
        renter.setRenterId(rs.getInt("renter_id"));
        renter.setFirstName(rs.getString("first_name"));
        renter.setLastName(rs.getString("last_name"));
        renter.setEmail(rs.getString("email"));
        renter.setDriverLicenseNumber(rs.getString("driver_license_number"));
        Date sqlDate = rs.getDate("driver_since");
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        renter.setSinceDate(localDate);
        renter.setStreet(rs.getString("street"));
        renter.setZip(rs.getString("zip_code"));
        renter.setBuilding(rs.getInt("building"));
        renter.setCity(rs.getString("city"));
        renter.setCountry(rs.getString("country_name"));
        return renter;
    }
}

class PhoneNumberRowMapper implements RowMapper<PhoneNumber> {
    @Override
    public PhoneNumber mapRow(ResultSet rs, int rowNum) throws SQLException {
        PhoneNumber phoneNumber = new PhoneNumber();
        phoneNumber.setPhoneId(rs.getInt("phone_id"));
        phoneNumber.setPhone(rs.getString("phone"));
        return phoneNumber;
    }
}

class ContractRowMapper implements RowMapper<Contract> {
    @Override
    public Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
        Contract contract = new Contract();
        contract.setContractId(rs.getInt("contract_id"));
        Date sqlDate = rs.getDate("contract_start_time");
        LocalDate localDate = Date.valueOf(String.valueOf(sqlDate)).toLocalDate();
        contract.setStartDate(localDate);
        Date sqlDate2 = rs.getDate("contract_end_time");
        LocalDate localDate2 = Date.valueOf(String.valueOf(sqlDate2)).toLocalDate();
        contract.setEndDate(localDate2);
        contract.setMaxKm(rs.getInt("contract_max_km"));
        contract.setActualKm(rs.getInt("contract_actual_km"));
        Renter renter = new Renter();
        renter.setRenterId(rs.getInt("renter_id"));
        contract.setRenter(renter);
        return contract;
    }
}
