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

INSERT INTO account(account_id, email, password, account_name, role_name, created_at,
                    updated_at, created_by, updated_by)
VALUES ('00000000-0000-0000-0000-000000000000', 'admin@icommerce.vn',
        '$argon2id$v=19$m=4096,t=3,p=1$IY5eWqE64TcKsdNBOIDSew$PueFKwzqUhExkeaIXGKtyWrMqgwZg834rkqG4yUBxcY',
        'Nguyễn Hieu Thinh', 'ADM', '2020-11-08 07:25:00',
        '2020-04-01 07:00:00', '', '');