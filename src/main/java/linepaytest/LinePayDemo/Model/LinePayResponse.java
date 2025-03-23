package linepaytest.LinePayDemo.Model;

public class LinePayResponse {
    private String returnCode;
    private String returnMessage;
    private Info info;

    // Getter and Setter for returnCode
    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    // Getter and Setter for returnMessage
    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    // Getter and Setter for info
    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    // Nested class for Info
    public static class Info {
        private PaymentUrl paymentUrl;
        private String transactionId;
        private String orderId;

        // Getter and Setter for paymentUrl
        public PaymentUrl getPaymentUrl() {
            return paymentUrl;
        }

        public void setPaymentUrl(PaymentUrl paymentUrl) {
            this.paymentUrl = paymentUrl;
        }

        // Getter and Setter for transactionId
        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        // Getter and Setter for orderId
        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }

    // Nested class for PaymentUrl
    public static class PaymentUrl {
        private String web;
        private String app;

        // Getter and Setter for web
        public String getWeb() {
            return web;
        }

        public void setWeb(String web) {
            this.web = web;
        }

        // Getter and Setter for app
        public String getApp() {
            return app;
        }

        public void setApp(String app) {
            this.app = app;
        }
    }
}
