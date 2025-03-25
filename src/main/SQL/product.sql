CREATE TABLE category
(
    categoryId INT PRIMARY KEY AUTO_INCREMENT,
    categoryName VARCHAR(50) UNIQUE NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE product
(
    productId INT PRIMARY KEY AUTO_INCREMENT,
    productName VARCHAR(100) NOT NULL,
    description TEXT,
    price INT NOT NULL CHECK (price >= 0),
    categoryId INT NULL,
    stockQuantity INT NOT NULL CHECK (stockQuantity >= 0),
    imageUrl TEXT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, 
    FOREIGN KEY (categoryId) REFERENCES category(categoryId) ON DELETE SET NULL
);

CREATE TABLE cart
(
    cartId INT PRIMARY KEY AUTO_INCREMENT,
    memberId INT,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (memberId) REFERENCES member(memberId) ON DELETE CASCADE
);

CREATE TABLE cartItem
(
    cartItemId INT PRIMARY KEY AUTO_INCREMENT,
    cartId INT NOT NULL,
    productId INT NOT NULL,
    quantity INT NOT NULL CHECK (quantity > 0),
    price INT NOT NULL CHECK (price >= 0),
    FOREIGN KEY (cartId) REFERENCES cart(cartId) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES product(productId) ON DELETE CASCADE
);

CREATE TABLE `order`
(
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    memberId INT NOT NULL,
    totalAmount INT NOT NULL CHECK (totalAmount >= 0),
    status ENUM('PENDING', 'PAID', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING',
    paymentMethod ENUM('LINE_PAY', 'CREDIT_CARD', 'CASH_ON_DELIVERY') NOT NULL,
    transactionId VARCHAR(50) UNIQUE,
    createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (memberId) REFERENCES member(memberId) ON DELETE CASCADE
);

CREATE TABLE orderItem
(
    orderItemId INT PRIMARY KEY AUTO_INCREMENT,
    orderId INT NOT NULL,
    productId INT NOT NULL,
    productName VARCHAR(100) NOT NULL,
    imageUrl TEXT,
    quantity INT NOT NULL CHECK (quantity > 0),
    price INT NOT NULL CHECK (price >= 0),
    FOREIGN KEY (orderId) REFERENCES `order`(orderId) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES product(productId) ON DELETE CASCADE
);

drop table category;
drop table product;
drop table cart;
drop table cart_item;
drop table `order`;
drop table order_item;