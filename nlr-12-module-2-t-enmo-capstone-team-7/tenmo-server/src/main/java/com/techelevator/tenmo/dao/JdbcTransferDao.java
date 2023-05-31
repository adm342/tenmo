package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;


    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //2 view all transfers by id
    @Override
    public List<Transfer> getAllTransfersByUserId(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "select transfer.transfer_id, transfer.transfer_type_id, transfer.transfer_status_id, transfer.account_from, transfer.account_to, transfer.amount from transfer " +
                " where account_from = ? or account_to = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next())
            transferList.add(mapRowToTransfer(results));
        return transferList;
    }




    //3
    @Override
    public List<Transfer> getPendingTransfersByUserId(int userId) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "select transfer.transfer_id, tenmo_user.username, transfer_type.transfer_type_desc, transfer_status.transfer_status_desc, transfer.amount  from transfer " +
                "JOIN transfer_type ON transfer_type.transfer_type_id = transfer.transfer_type_id " +
                "Join transfer_status ON transfer_status.transfer_status_id = transfer.transfer_status_id " +
                "JOIN account ON transfer.account_from = account.account_id " +
                "JOIN tenmo_user ON account.user_id = tenmo_user.user_id " +
                "Where transfer.transfer_status_id = 1 AND transfer.account_to = ? OR transfer.account_from = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next())
            transferList.add(mapRowToTransfer(results));
        return transferList;
    }


    @Override
    public void updateApproved(int transferId) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = 2 " +
                "WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transferId);
    }

    @Override
    public void updateDenied(int transferId) {
        String sql = "UPDATE transfer " +
                "SET transfer_status_id = 3 " +
                "WHERE transfer_id = ?";
        jdbcTemplate.update(sql, transferId);
    }

    //    This is actually part of the optional use cases #7
    @Override
    public void createRequest(Transfer transfer) {
        String sql = "insert into transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "Values (?, ?, ?, ?, ?)" +
                "Returning transfer_id";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFromId(), transfer.getAccountToId(), transfer.getAmount());
    }

    ////CREATE SEND
    @Override
    public void createSend(Transfer transfer) {
        String sql = "insert into transfer(transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                "Values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFromId(),
                transfer.getAccountToId(), transfer.getAmount());
    }

    //w
    @Override
    public int findTransferIdByAccountId(int accountId) {
        if (accountId < 0) throw new IllegalArgumentException("usedId Cannot Be Less Than 0");
        int transferId;
        try {
            transferId = jdbcTemplate.queryForObject("SELECT transfer_id FROM transfer WHERE account_id = ?", int.class, accountId);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new NumberFormatException("AccountId " + accountId + " was not found.");
        }
        return transferId;
    }


    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFromId(rowSet.getInt("account_from"));
        transfer.setAccountToId(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getBigDecimal("amount"));
        return transfer;
    }

}
