-- role
INSERT INTO role(role_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('ADM', 'System Administrator', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');

-- privilege
INSERT INTO privilege(privilege_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('REA_PRO', 'Read Product', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');
INSERT INTO privilege(privilege_name, description, created_at, updated_at, created_by, updated_by)
VALUES ('WRI_PRO', 'Write Product', '2020-04-01 07:00:00', '2020-04-01 07:00:00', '', '');

-- role_privilege
INSERT INTO role_privilege(role_name, privilege_name, created_at, created_by)
VALUES ('ADM', 'REA_PRO', '2020-04-01 07:00:00', '');
INSERT INTO role_privilege(role_name, privilege_name, created_at, created_by)
VALUES ('ADM', 'WRI_PRO', '2020-04-01 07:00:00', '');

-- product
INSERT INTO product (product_id,product_name,product_price,product_status,created_at,updated_at,created_by,updated_by)
VALUES	 (100000927,'string',0,NULL,'2020-12-04 17:14:30.286','2020-12-04 17:14:30.340',NULL,NULL);

-- buyer
INSERT INTO buyer (buyer_id,email,"password",buyer_name,status,created_by,updated_by)
VALUES
	 (330263927,'abc@xyz.com','$argon2id$v=19$m=4096,t=3,p=1$MiQzNYNKSAU/a8KounlPlQ$NsKedzaiT7XGYLVgtHgBQ/YThgEougliIvovZPWNMAg','string','ACTIVE','System','System');

-- shopping_cart
INSERT INTO shopping_cart (shopping_cart_id,buyer_id,status) VALUES
	 (866385412,330263927,'PROCESSING');