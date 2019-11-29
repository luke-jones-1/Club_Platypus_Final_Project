CREATE TABLE chatlog(
     chat_id VARCHAR PRIMARY KEY,
     user_id VARCHAR,
     time_created VARCHAR,
     date_created VARCHAR,
     content VARCHAR,
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
 )