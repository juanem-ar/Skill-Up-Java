INSERT INTO roles(id,creation_date,description,name,update_date) VALUES
    (1,NOW(), 'user roles', 'ROLE_USER', NOW()),
    (2,NOW(), 'admin roles', 'ROLE_ADMIN', NOW());
-- ROLE USER
INSERT INTO users(id,creation_date,deleted,email,first_name,last_name,password,update_date,role) VALUES
    (3,NOW(), false, 'user1@hotmail.com', 'Juan Manuel','Lopez','password123', NOW(),1),
    (4,NOW(), false, 'user2@hotmail.com', 'Juan Manuel','Lopez','password123', NOW(),1);
-- ROLE ADMIN
INSERT INTO users(id,creation_date,deleted,email,first_name,last_name,password,update_date,role) VALUES
    (5,NOW(), false, 'admin1@hotmail.com', 'Juan Manuel','Lopez','password123', NOW(),2),
    (6,NOW(), false, 'admin2@hotmail.com', 'Juan Manuel','Lopez','password123', NOW(),2);

INSERT INTO accounts(id,balance,creation_date,currency,deleted,transaction_limit,update_date,user_id) VALUES
    (7,200000,NOW(),'ARS',false,300000,NOW(),3),
    (8,200000,NOW(),'USD',false,1000,NOW(),3),
    (9,1000,NOW(),'ARS',false,300000,NOW(),4),
    (10,400,NOW(),'USD',false,1000,NOW(),4),
    (11,200000,NOW(),'ARS',false,300000,NOW(),5),
    (12,200000,NOW(),'USD',false,1000,NOW(),5),
    (13,1000,NOW(),'ARS',false,300000,NOW(),6),
    (14,400,NOW(),'USD',false,1000,NOW(),6);