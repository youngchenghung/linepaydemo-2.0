package linepaytest.LinePayDemo.Model;

import java.util.List;

public class LinePayRequest {
    private int amount;
    private String currency;
    private String orderId;
    private List<Package> packages;
    private RedirectUrls redirectUrls;

    // Getter and Setter for amount
    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    // Getter and Setter for currency
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    // Getter and Setter for orderId
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // Getter and Setter for packages
    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }

    // Getter and Setter for redirectUrls
    public RedirectUrls getRedirectUrls() {
        return redirectUrls;
    }

    public void setRedirectUrls(RedirectUrls redirectUrls) {
        this.redirectUrls = redirectUrls;
    }

    // Nested class for Package
    public static class Package {
        private String id;
        private String name;
        private int amount;
        private List<CartItem> products;

        // Getter and Setter for id
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        // Getter and Setter for name
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        // Getter and Setter for amount
        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
        
        public List<CartItem> getProducts() {
            return products;
        }

        public void setProducts(List<CartItem> products) {
            this.products = products;
        }

    }

    // Nested class for RedirectUrls
    public static class RedirectUrls {
        private String confirmUrl;
        private String cancelUrl;

        // Getter and Setter for confirmUrl
        public String getConfirmUrl() {
            return confirmUrl;
        }

        public void setConfirmUrl(String confirmUrl) {
            this.confirmUrl = confirmUrl;
        }

        // Getter and Setter for cancelUrl
        public String getCancelUrl() {
            return cancelUrl;
        }

        public void setCancelUrl(String cancelUrl) {
            this.cancelUrl = cancelUrl;
        }
    }
}

