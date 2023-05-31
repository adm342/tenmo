package com.techelevator.tenmo.model;

public class TenmoActions {

    private String username;
    private int userid;
    private double balance;

    public TenmoActions(String username, int userid, double balance) {
        this.username = username;
        this.userid = userid;
        this.balance = balance;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String currentBalance(){
        return userid + ": $" + balance;
    }
}
