CREATE TABLE oauth2_member
(
    oauth2_member_id INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    provider         VARCHAR(256) NOT NULL,
    provider_id      VARCHAR(256) NOT NULL,
    name             VARCHAR(256),
    email            VARCHAR(256),
    access_token     TEXT,
    expires_at       TIMESTAMP
);