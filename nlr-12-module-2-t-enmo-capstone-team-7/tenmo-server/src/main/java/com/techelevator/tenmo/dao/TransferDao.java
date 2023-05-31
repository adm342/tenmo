package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {


    List<Transfer> getPendingTransfersByUserId(int userId);

    List<Transfer> getAllTransfersByUserId(int userId);

    void updateApproved(int transferId);

    void updateDenied(int transferId);

    void createRequest(Transfer transfer);

    void createSend(Transfer transferId);

    int findTransferIdByAccountId(int accountId);


}
