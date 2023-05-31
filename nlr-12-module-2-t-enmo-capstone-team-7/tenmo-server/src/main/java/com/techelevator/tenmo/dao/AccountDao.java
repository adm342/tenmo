package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    List<Account> getAllAccounts();

   void subtractAccountBalance(BigDecimal amountToSubtract, int accountId);

    void addAccountBalance(BigDecimal amountToAdd, int accountId);

    BigDecimal getBalance(int accountId);

    public int findAccountIdByUserId(int userId);

}
