public class TransactionData {
    
    private long transactionId;
    private double amount;
    
    public TransactionData(long transactionId, double amount) {
        this.transactionId = transactionId;
        this.amount = amount;
    }
    
    public long getTransactionId() {
        return transactionId;
    }
    
    public double getAmount() {
        return amount;
    }
}
