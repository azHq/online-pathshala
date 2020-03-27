package com.example.onlinepathshala;

public class Transaction_History_Item {
    public String id;
    public String sender_id;
    public String sender_type;
    public String receiver_id;
    public String receiver_type;
    public String fee_type;
    public String transaction_type;
    public String amount;
    public String debit_credit;
    public String payment_method;
    public String date;

    public Transaction_History_Item(String id, String sender_id, String sender_type, String receiver_id, String receiver_type, String fee_type, String transaction_type, String amount, String debit_credit, String payment_method, String date) {
        this.id = id;
        this.sender_id = sender_id;
        this.sender_type = sender_type;
        this.receiver_id = receiver_id;
        this.receiver_type = receiver_type;
        this.fee_type = fee_type;
        this.transaction_type = transaction_type;
        this.amount = amount;
        this.debit_credit = debit_credit;
        this.payment_method = payment_method;
        this.date = date;
    }
}
