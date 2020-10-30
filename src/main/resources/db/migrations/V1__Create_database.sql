DROP TABLE IF EXISTS `quote`;

CREATE TABLE quote (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    quote_key VARCHAR(16),
    quote_date DATE,
    quote_value VARCHAR(16),
    UNIQUE(quote_key, quote_date)
);