package linepaytest.LinePayDemo.Dao;

import linepaytest.LinePayDemo.Model.LinePayConfirm;
import linepaytest.LinePayDemo.Model.LinePayRequest;
import linepaytest.LinePayDemo.Model.LinePayResponse;

public interface LinePayDao {
    
    // LinePay API #1付款請求
    LinePayResponse initiatePayment(LinePayRequest linePayRequest);

    // LinePay API #2付款確認
    String confirmTransaction(String transactionId, LinePayConfirm linePayConfirm);

    // LinePay API #3查詢付款狀態
    String getPaymentStatus(String transactionId);
    
}
