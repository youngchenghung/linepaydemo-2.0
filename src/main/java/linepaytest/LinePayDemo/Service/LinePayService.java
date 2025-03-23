package linepaytest.LinePayDemo.Service;

import linepaytest.LinePayDemo.Model.LinePayConfirm;
import linepaytest.LinePayDemo.Model.LinePayResponse;
import linepaytest.LinePayDemo.Model.CartItem;

import java.util.List;


public interface LinePayService {

    // LinePay API #1付款請求
    LinePayResponse initiatePayment(List<CartItem> cartItems, String jwtToken);

    // LinePay API #2付款確認
    String confirmPayment(String transactionId , List<CartItem> cartItems);

    // LinePay API #3查詢付款狀態
    String getPaymentStatus(String transactionId);
    
}
