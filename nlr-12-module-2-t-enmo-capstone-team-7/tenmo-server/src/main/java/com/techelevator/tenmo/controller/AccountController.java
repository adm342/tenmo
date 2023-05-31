package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class AccountController {

    private final JdbcAccountDao accountDao;
    private final JdbcTransferDao transferDao;
    private final JdbcUserDao userDao;

    public AccountController(JdbcAccountDao accountDao, JdbcTransferDao transferDao, JdbcUserDao userDao) {
        this.accountDao = accountDao;
        this.transferDao = transferDao;
        this.userDao = userDao;
    }


    //1.Get current balance
    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getBalance(Principal principal) {
        String username = principal.getName();
        BigDecimal balance = accountDao.getBalance(userDao.findIdByUsername(username));
        return balance;
    }

//    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
//    public BigDecimal getBalanceById(@PathVariable int accountId, Principal principal) {
//        String username = principal.getName();
//        int userId = userDao.findIdByUsername(username);
//        accountId = accountDao.findAccountIdByUserId(userId);
//        return accountDao.getBalanceById(accountId);
//    }

    //works
    @RequestMapping(path = "/account/listOfUsers", method = RequestMethod.GET)
    public List<Account> userAccounts(Principal principal) {
        String userName = principal.getName();
        int id = userDao.findIdByUsername(userName);
        List<Account> userList = accountDao.getAllAccounts();
        return accountDao.getAllAccounts();
    }


    //works step 2 view all transfers by id
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> AllTransfersByUserId(Integer userId, Principal principal) {
        List<Transfer> transferList = null;
        String username = principal.getName();
        userId = userDao.findIdByUsername(username);
        try {
            transferList = transferDao.getAllTransfersByUserId(userId);
        } catch (IllegalStateException ex) {
            System.out.println(ex.getMessage());
        }
        return transferList;
    }


    //works - 3. Get pending transfers by user id
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(path = "/transfer/pending/{userId}", method = RequestMethod.GET)
    public List<Transfer> listPendingTransfers(@PathVariable int userId, Principal principal) {
        String username = principal.getName();
        userId = userDao.findIdByUsername(username);
        return transferDao.getPendingTransfersByUserId(userId);
    }


    //works
    @RequestMapping(path = "/transfer/accept/{transferId}", method = RequestMethod.PUT)
    public void acceptTransfer(Transfer accepttransfer, @PathVariable int transferId, BigDecimal amountToSend, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        int accountId = accountDao.findAccountIdByUserId(userId);
        transferId = accountDao.findAccountIdByUserId(userId);
        transferDao.updateApproved(transferId);
        amountToSend = BigDecimal.valueOf(50.00);
        accountDao.addAccountBalance(amountToSend, accountId);
        accountDao.subtractAccountBalance(amountToSend, accepttransfer.getAccountFromId());
    }

    //not working also a bonus question #9
    @RequestMapping(path = "/transfer/deny/{denyTransferId}", method = RequestMethod.PUT)
    public void denyTransfer(@PathVariable int denyTransferId, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        int accountId = accountDao.findAccountIdByUserId(userId);
        denyTransferId = accountDao.findAccountIdByUserId(userId);
        transferDao.updateDenied(denyTransferId);
    }


    //    //column out of range error ** this is actually part of the optional use cases #7
    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public void requestTransfer(@RequestBody Transfer transfer) {
        transferDao.createRequest(transfer);
    }

    //CREATE SEND
    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public void sendTransfer(@RequestBody Transfer transfer) {
        transferDao.createSend(transfer);
        accountDao.addAccountBalance(transfer.getAmount(), transfer.getAccountToId());
        accountDao.subtractAccountBalance(transfer.getAmount(), transfer.getAccountFromId());
    }





}
