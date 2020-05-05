package com.example.demo.repository;

import com.example.demo.model.Car;
import com.example.demo.model.Contract;
import com.example.demo.model.Renter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class ContractRepository {

    @Autowired
    private JdbcTemplate template;

    // fetch all
    public List<Contract> fetchAll() {
        return template.query("SELECT contract_id, renter_id, contract_start_time, contract_end_time, contract_max_km, contract_actual_km " +
                "FROM contract ORDER BY contract_start_time;", new ContractRowMapper());
    }

    // add new
    public int addContract(Contract theContract) {
        final String sql = "INSERT INTO contract (renter_id, car_id, contract_start_time, contract_end_time) VALUES (?, ?, ?, ?)";

        KeyHolder holder = new GeneratedKeyHolder();
        template.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, theContract.getRenter().getRenterId());
                ps.setInt(2, theContract.getCar().getCarId());
                java.sql.Date sqlDate = Date.valueOf(theContract.getStartDate());
                System.out.println("Inside add contract function in the contract repository after the conversion to SQL Date: " + sqlDate );

                ps.setDate(3, sqlDate);
                sqlDate = Date.valueOf(theContract.getEndDate());

                System.out.println("Inside add contract function in the contract repository after the conversion to SQL Date: " + sqlDate );

                ps.setDate(4, sqlDate);
                return ps;
            }
        }, holder);
        int newContractId = holder.getKey().intValue();
        return newContractId;
    }

    // find by ID
    public Contract findById(int id) {
        return template.queryForObject(
                "SELECT contract_id, renter_id, contract_start_time, contract_end_time, contract_max_km, contract_actual_km " +
                    "FROM contract " +
                    "WHERE contract_id = ? ", new Object[]{id}, new ContractRowMapper());
    }

    // update
    public void updateContract(Contract contract) {
        System.out.println("contract = " + contract);
        String updateStatement = "UPDATE contract " +
                                 "SET contract_end_time = ?, contract_max_km = ?, contract_actual_km = ? " +
                                 "WHERE contract_id = ?";

        template.update(updateStatement, contract.getEndDate(), contract.getMaxKm(), contract.getActualKm(), contract.getContractId());
    }

    public void delete(Contract contract) {
        String sql1 = "DELETE FROM contract WHERE contract_id = ? ";
        template.update(sql1, contract.getContractId());
    }
}
