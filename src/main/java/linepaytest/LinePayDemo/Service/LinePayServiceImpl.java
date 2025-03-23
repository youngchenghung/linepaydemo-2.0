package linepaytest.LinePayDemo.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import linepaytest.LinePayDemo.Dao.LinePayDao;
import linepaytest.LinePayDemo.Model.CartItem;
import linepaytest.LinePayDemo.Model.LinePayConfirm;
import linepaytest.LinePayDemo.Model.LinePayRequest;
import linepaytest.LinePayDemo.Model.LinePayResponse;


@Component
public class LinePayServiceImpl implements LinePayService {

    @Autowired
    private LinePayDao linePayDao;

    
    // LinePay API #1付款請求
    @Override
    public LinePayResponse initiatePayment(List<CartItem> cartItems, String jwtToken) {
        // 取得購物車商品
        int totalAmount = cartItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum(); // 計算總金額

        System.out.println("Total amount: " + totalAmount);

        // 組裝 LinePayRequest 中 Package 資訊
        LinePayRequest.Package packageInfo = new LinePayRequest.Package();
        packageInfo.setId("package_id");
        packageInfo.setName("Shop Cart");
        packageInfo.setProducts(cartItems);
        packageInfo.setAmount(totalAmount);
        
        // 組裝 LinePayRequest 中 RedirectUrls 資訊
        String encodedToken = URLEncoder.encode(jwtToken, StandardCharsets.UTF_8);
        LinePayRequest.RedirectUrls redirectUrls = new LinePayRequest.RedirectUrls();
        redirectUrls.setConfirmUrl("http://localhost:8080/redirect?token=" + encodedToken);
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");

        // 組裝 LinePayRequest
        LinePayRequest linePayRequest = new LinePayRequest();
        String orderId = UUID.randomUUID().toString(); // 產生 orderId
        linePayRequest.setAmount(totalAmount);
        linePayRequest.setCurrency("TWD");
        linePayRequest.setOrderId(orderId);
        linePayRequest.setPackages(List.of(packageInfo)); // 將 packageInfo 加入 packages
        linePayRequest.setRedirectUrls(redirectUrls); // 將 redirectUrls 加入 redirectUrls

        return linePayDao.initiatePayment(linePayRequest); // 呼叫 LinePayDao 的 initiatePayment 方法
    }

    // LinePay API #2付款確認
    @Override
    public String confirmPayment(String transactionId, List<CartItem> cartItems) {
        // 取得購物車商品
        int totalAmount = cartItems.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum(); // 計算總金額
        System.out.println("Total amount: " + totalAmount);
        
        // 建立 LinePayConfirm 物件，設定 setAmount, setCurrency 資料
        LinePayConfirm linePayConfirm = new LinePayConfirm();
        linePayConfirm.setAmount(totalAmount);
        linePayConfirm.setCurrency("TWD");
        System.out.println("linePayConfirm: " + linePayConfirm);

        return linePayDao.confirmTransaction(transactionId, linePayConfirm);
    }
    
    // LinePay API #3查詢付款狀態
    @Override
    public String getPaymentStatus(String transactionId) {

        return linePayDao.getPaymentStatus(transactionId);
    }
    
}
