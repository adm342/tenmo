package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Account> getAllAccounts() {
        List<Account> userAccounts = new ArrayList<>();
        String sql = "select account.user_id, account.account_id, balance FROM account;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            userAccounts.add(mapRowToAccount(results));
        }
        return userAccounts;
    }

    @Override
    public int findAccountIdByUserId(int userId) {
        if (userId < 0) throw new IllegalArgumentException("usedId Cannot Be Less Than 0");
        int accountId;
        try {
            accountId = jdbcTemplate.queryForObject("SELECT account_id FROM account WHERE user_id = ?", int.class, userId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new NumberFormatException("UserId " + userId + " was not found.");
        }
        return accountId;
    }

    @Override
    public void subtractAccountBalance(BigDecimal amountToSubtract, int accountId) {
        String sql = "UPDATE account " +
                "SET balance = balance - ? WHERE account_id = ?";
        jdbcTemplate.update(sql, amountToSubtract, accountId);
    }

    @Override
    public void addAccountBalance(BigDecimal amountToAdd, int accountId) {
        String sql = "UPDATE account " +
                "SET balance = balance + ? " +
                "WHERE account_id = ?";
        jdbcTemplate.update(sql, amountToAdd, accountId);
    }


    //1.Get current balance
    @Override
    public BigDecimal getBalance(int userId) {
        BigDecimal balance = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if(results.next()){
            balance = mapRowToAccount(results).getBalance();
        }
        return balance;
    }

//    private Account mapRowToAccountForList(SqlRowSet rowSet){
//        Account account = new Account();
//        account.setAccountId(rowSet.getInt("account_id"));
//        account.setUserId(rowSet.getInt("user_id"));
//        return account;
//    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();
        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }

}
