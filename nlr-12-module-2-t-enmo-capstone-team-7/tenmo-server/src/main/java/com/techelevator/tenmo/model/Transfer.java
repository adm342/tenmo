package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

public class Transfer {

    @Min(value = 1, message = "transferId cannot be blank.")
    private int transferId;
    @Min(value = 1, message = "transferId cannot be less than 1.")
    @Max(value = 2, message = "transferTypeId cannot be higher than 2")
    private int transferTypeId;
    @Min(value = 1, message = "TransferStatusId must be between 1-3")
    @Max(value = 3, message = "TransferStatusId must be between 1-3")
    private int transferStatusId;
    @Min(value = 1, message = "accountFromId cannot be blank.")
    private int accountFromId;
    @Min(value = 1, message = "accountToId cannot be blank.")
    private int accountToId;
    @Min(value = 1, message = "BigDecimal cannot be blank.")
    private BigDecimal amount;


    public Transfer() {
    }


    public Transfer(int transferId, int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal amount) {
        this.transferId = transferId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
