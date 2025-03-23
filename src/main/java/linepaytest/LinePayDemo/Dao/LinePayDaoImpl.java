package linepaytest.LinePayDemo.Dao;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import linepaytest.LinePayDemo.Model.LinePayConfirm;
import linepaytest.LinePayDemo.Model.LinePayRequest;
import linepaytest.LinePayDemo.Model.LinePayResponse;

import java.util.UUID;

import org.apache.commons.codec.digest.HmacUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LinePayDaoImpl implements LinePayDao {

    @Autowired
    private RestTemplate restTemplate;

    // LinePay API #1付款請求
    @Override
    public LinePayResponse initiatePayment(LinePayRequest linePayRequest) {
        String url = "https://sandbox-api-pay.line.me/v3/payments/request";
        String requestUri = "/v3/payments/request";
        String nonce = UUID.randomUUID().toString();

        // 創建請求表頭
        HttpHeaders headers = createRequestHeaders(requestUri, linePayRequest, nonce);
        HttpEntity<LinePayRequest> request = new HttpEntity<>(linePayRequest, headers);
        System.out.println("request: " + request);

        // 發送 POST 請求並獲取響應
        ResponseEntity<LinePayResponse> response = restTemplate.postForEntity(url, request, LinePayResponse.class);
        return response.getBody();
    }
    
   // LinePay API #2付款確認
    @Override
    public String confirmTransaction(String transactionId, LinePayConfirm linePayConfirm) {
        String url = "https://sandbox-api-pay.line.me/v3/payments/" + transactionId + "/confirm";
        String confirmUrl = "/v3/payments/" + transactionId + "/confirm";
        String nonce = UUID.randomUUID().toString();

        // 創建請求表頭
        HttpHeaders headers = createConfirmHeaders(confirmUrl, linePayConfirm, nonce);
        HttpEntity<LinePayConfirm> request = new HttpEntity<>(linePayConfirm, headers);
        // System.out.println("request: " + request);

        // 發送 POST 請求並獲取響應
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        return response.getBody();
    }

    // LinePay API #3查詢付款狀態
    @Override
    public String getPaymentStatus(String transactionId) {
        String url = "https://sandbox-api-pay.line.me/v3/payments/requests/" + transactionId + "/check";
        String statusUrl = "/v3/payments/requests/" + transactionId + "/check";
        String nonce = UUID.randomUUID().toString();

        // 創建請求表頭
        HttpHeaders headers = createStatusHeaders(statusUrl, nonce);
        HttpEntity<String> request = new HttpEntity<>(headers);

        // 發送 GET 請求並獲取響應
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request,String.class);
        return response.getBody();
    }

    // 創建請求HTTP createRequestHeaders 表頭的方法
    private HttpHeaders createRequestHeaders(String requestUri, LinePayRequest linePayRequest, String nonce) {
        String ChannelSecret = "0d6163e4b515b1c3239889283b209594";

        // 設定表頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", "2006747634");
        headers.set("X-LINE-Authorization-Nonce", nonce);

        // 生成簽名
        String signature = requestSignature(ChannelSecret, requestUri, linePayRequest, nonce);
        headers.set("X-LINE-Authorization", signature);
        
        return headers;
    }
    
    // 生成 request 簽名的方法
    private String requestSignature(String ChannelSecret, String requestUri, LinePayRequest linePayRequest, String nonce) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(linePayRequest); // 將 LinePayRequest 物件轉換為 JSON 字串
            System.out.println("requestJson: " + requestJson);
    
            String data = encrypt(ChannelSecret, ChannelSecret + requestUri + requestJson + nonce); // 生成簽名
            // System.out.println("request signature: " + data);
            return data;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 創建請求HTTP createConfirmHeaders 表頭的方法
    private HttpHeaders createConfirmHeaders(String confirmUrl, LinePayConfirm linePayConfirm, String nonce) {
        String ChannelSecret = "0d6163e4b515b1c3239889283b209594";

        // 設定表頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", "2006747634");
        headers.set("X-LINE-Authorization-Nonce", nonce);

        // 生成 confirm 簽名
        String signature = confirmSignature(ChannelSecret, confirmUrl, linePayConfirm, nonce);
        headers.set("X-LINE-Authorization", signature);
        
        return headers;
    }

    // 生成 confirm 簽名的方法
    private String confirmSignature(String ChannelSecret, String confirmUrl, LinePayConfirm linePayConfirm, String nonce) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(linePayConfirm); // 將 LinePayConfirm 物件轉換為 JSON 字串
            System.out.println("requestJson: " + requestJson);
    
            String data = encrypt(ChannelSecret, ChannelSecret + confirmUrl + requestJson + nonce); // 生成簽名
            // System.out.println("confirm signature: " + data);
            return data;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 創建請求HTTP createStatusHeaders 表頭的方法
    private HttpHeaders createStatusHeaders(String statusUrl, String nonce) {
        String ChannelSecret = "0d6163e4b515b1c3239889283b209594";

        // 設定表頭
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-LINE-ChannelId", "2006747634");
        headers.set("X-LINE-Authorization-Nonce", nonce);

        // 生成 status 簽名
        String signature = statusSignature(ChannelSecret, statusUrl, nonce);
        headers.set("X-LINE-Authorization", signature);

        return headers;    
    }

    // 生成 status 簽名的方法
    private String statusSignature(String ChannelSecret, String statusUrl, String nonce) {
        String data = encrypt(ChannelSecret, ChannelSecret + statusUrl + nonce); // 生成簽名
        // System.out.println("status signature: " + data);
        return data;
    }

    // 加密方法
    @SuppressWarnings("deprecation")
    private String encrypt(final String keys, final String data) {
        return toBase64String(HmacUtils.getHmacSha256(keys.getBytes()).doFinal(data.getBytes()));
    }

    // 將字節數組轉換為 Base64 字符串的方法
    private String toBase64String(byte[] bytes) {
        byte[] byteArray = Base64.encodeBase64(bytes);
        return new String(byteArray);
    }
}
