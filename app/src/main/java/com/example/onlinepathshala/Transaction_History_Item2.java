package com.example.onlinepathshala;

public class Transaction_History_Item2 {

    public String transaction_type;
    public String amount;
    public String debit_credit;
    public String create_date;

    public Transaction_History_Item2(String transaction_type, String amount, String debit_credit, String create_date) {
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.debit_credit = debit_credit;
        this.create_date = create_date;
    }
}
