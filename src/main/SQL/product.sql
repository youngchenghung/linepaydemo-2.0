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

INSERT INTO category (categoryName, description) VALUES
('書籍', '各種類型的書籍'),
('電子產品', '各種電子設備和配件'),
('家居用品', '家庭用品與裝飾品');

-- 書籍類 (categoryId = 1)
INSERT INTO product (productName, description, price, categoryId, stockQuantity, imageUrl) VALUES
('Java 程式設計入門', '適合初學者的 Java 教學書籍', 500, 1, 100, 'java_book.jpg'),
('Spring Boot 企業應用', '深入探討 Spring Boot 的企業應用', 800, 1, 50, 'spring_boot_book.jpg'),
('設計模式詳解', '學習軟體設計模式的經典書籍', 650, 1, 30, 'design_patterns.jpg'),
('資料結構與演算法', '適用於面試的演算法書籍', 700, 1, 40, 'algorithms_book.jpg'),
('數據庫設計最佳實踐', 'SQL 與 NoSQL 數據庫設計原則', 750, 1, 25, 'database_book.jpg'),
('Python 網頁爬蟲', '學習如何使用 Python 進行網頁爬取', 550, 1, 60, 'python_crawler.jpg'),
('機器學習基礎', '人工智慧與機器學習入門書籍', 900, 1, 20, 'ml_basics.jpg'),
('Linux 伺服器管理', '學習 Linux 伺服器的管理與維護', 720, 1, 35, 'linux_admin.jpg'),
('前端開發 HTML/CSS/JS', '現代前端技術大全', 600, 1, 50, 'frontend_book.jpg'),
('DevOps 實戰', '自動化與持續集成的 DevOps 方法', 880, 1, 15, 'devops_book.jpg');

-- 電子產品類 (categoryId = 2)
INSERT INTO product (productName, description, price, categoryId, stockQuantity, imageUrl) VALUES
('MacBook Pro 14"', '蘋果最新 M3 晶片筆記型電腦', 45000, 2, 10, 'macbook_pro.jpg'),
('iPhone 15 Pro', '蘋果最新款旗艦手機', 35000, 2, 20, 'iphone_15_pro.jpg'),
('Sony WH-1000XM5 耳機', '頂級降噪藍牙耳機', 9800, 2, 30, 'sony_wh1000xm5.jpg'),
('Apple Watch Series 9', '最新一代智慧手錶', 13500, 2, 25, 'apple_watch_s9.jpg'),
('GoPro Hero 12', '運動攝影機，適合戶外拍攝', 15000, 2, 15, 'gopro_hero12.jpg'),
('Nintendo Switch OLED', '任天堂最新遊戲主機', 12000, 2, 40, 'switch_oled.jpg'),
('Samsung Galaxy S24 Ultra', '三星旗艦級智慧手機', 36000, 2, 20, 'galaxy_s24_ultra.jpg'),
('Razer 機械鍵盤', '電競專用 RGB 鍵盤', 5200, 2, 35, 'razer_keyboard.jpg'),
('Sony 4K 電視 55吋', '支援 HDR 的超高清電視', 32000, 2, 10, 'sony_4k_tv.jpg'),
('AirPods Pro 2', '蘋果無線耳機，主動降噪', 7800, 2, 50, 'airpods_pro2.jpg');

-- 家居用品類 (categoryId = 3)
INSERT INTO product (productName, description, price, categoryId, stockQuantity, imageUrl) VALUES
('智能掃地機器人', '全自動智能掃地機', 12000, 3, 15, 'robot_vacuum.jpg'),
('北歐風格書架', '簡約風格的木質書架', 4500, 3, 20, 'bookshelf.jpg'),
('人體工學辦公椅', '長時間工作不累的辦公椅', 6800, 3, 10, 'ergonomic_chair.jpg'),
('電動升降桌', '可調高度的升降桌', 9800, 3, 8, 'standing_desk.jpg'),
('智能 LED 燈泡', '可透過手機控制的智能燈泡', 1200, 3, 50, 'smart_lightbulb.jpg'),
('記憶棉枕頭', '符合人體工學的舒適枕頭', 1500, 3, 40, 'memory_pillow.jpg'),
('雙人床墊', '高品質記憶棉雙人床墊', 22000, 3, 5, 'memory_foam_mattress.jpg'),
('空氣清淨機', '有效過濾 PM2.5 的空氣清淨機', 7500, 3, 30, 'air_purifier.jpg'),
('電子體重計', '精準測量體重與 BMI', 2000, 3, 25, 'digital_scale.jpg'),
('全自動咖啡機', '專業級的咖啡機，沖泡完美咖啡', 18000, 3, 12, 'coffee_machine.jpg');
