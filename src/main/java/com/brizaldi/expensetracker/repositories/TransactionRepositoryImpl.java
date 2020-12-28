package com.brizaldi.expensetracker.repositories;

import com.brizaldi.expensetracker.domain.Transaction;
import com.brizaldi.expensetracker.exceptions.EtBadRequestException;
import com.brizaldi.expensetracker.exceptions.EtResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private static final String SQL_CREATE = "INSERT INTO ET_TRANSACTIONS (TRANSACTION_ID, CATEGORY_ID, USER_ID, " +
            "AMOUNT, NOTE, TRANSACTION_DATE) VALUES(NEXTVAL('ET_TRANSACTIONS_SEQ'), ?, ?, ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, " +
            "TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID  = ?";

    private static final String SQL_FIND_ALL = "SELECT TRANSACTION_ID, CATEGORY_ID, USER_ID, AMOUNT, NOTE, " +
            "TRANSACTION_DATE FROM ET_TRANSACTIONS WHERE USER_ID = ? AND CATEGORY_ID = ?";

    private static final String SQL_UPDATE = "UPDATE ET_TRANSACTIONS SET AMOUNT = ?, NOTE = ?, TRANSACTION_DATE = ? " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";

    private static final String SQL_DELETE = "DELETE FROM ET_TRANSACTIONS " +
            "WHERE USER_ID = ? AND CATEGORY_ID = ? AND TRANSACTION_ID = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<Transaction> findAll(Integer userId, Integer categoryId) {
        return jdbcTemplate.query(SQL_FIND_ALL, transactionRowMapper, userId, categoryId);
    }

    @Override
    public Transaction findById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        try {
            return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, transactionRowMapper, userId, categoryId, transactionId);
        } catch (Exception e) {
            throw new EtResourceNotFoundException("Transaction not found");
        }
    }

    @Override
    public Integer create(Integer userId, Integer categoryId, Double amount, String note, Long transactionDate) throws EtBadRequestException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, categoryId);
                ps.setInt(2, userId);
                ps.setDouble(3, amount);
                ps.setString(4, note);
                ps.setLong(5, transactionDate);
                return ps;
            }, keyHolder);
            return (Integer) keyHolder.getKeys().get("TRANSACTION_ID");
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void update(Integer userId, Integer categoryId, Integer transactionId, Transaction transaction) throws EtBadRequestException {
        try {
            jdbcTemplate.update(
                SQL_UPDATE,
                transaction.getAmount(),
                transaction.getNote(),
                transaction.getTransactionDate(),
                userId,
                categoryId,
                transactionId
            );
        } catch (Exception e) {
            throw new EtBadRequestException("Invalid request");
        }
    }

    @Override
    public void removeById(Integer userId, Integer categoryId, Integer transactionId) throws EtResourceNotFoundException {
        int count = jdbcTemplate.update(SQL_DELETE, userId, categoryId, transactionId);
        if (count == 0)
            throw new EtResourceNotFoundException("Transaction not found");
    }

    private RowMapper<Transaction> transactionRowMapper = ((resultSet, i) -> {
        return new Transaction(
            resultSet.getInt("TRANSACTION_ID"),
            resultSet.getInt("CATEGORY_ID"),
            resultSet.getInt("USER_ID"),
            resultSet.getDouble("AMOUNT"),
            resultSet.getString("NOTE"),
            resultSet.getLong("TRANSACTION_DATE")
        );
    });
}
