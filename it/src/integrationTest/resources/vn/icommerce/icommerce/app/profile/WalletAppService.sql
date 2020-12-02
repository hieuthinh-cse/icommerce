INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone)
VALUES (2000000001, '0909009001', 'SELLER', 'INACTIVE', 'full_name_02', 'MALE', 'tuanlt2',
        'tuanlt2', '00000000-0000-0000-0000-000000000000', '0909009001');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone, bank_link_status, email)
VALUES (10000111, '0937111111', 'INDIVIDUAL', 'ACTIVE', 'full name 1', 'MALE', 'User', 'User',
        '00000000-0000-0000-0000-000000000000', '0937111111', 'UNLINK', 'hungpc@sendo.vn');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id)
VALUES (10000113, '0937111117', 'INDIVIDUAL', 'ACTIVE', 'full name 3', 'MALE', 'User3', 'User3',
        '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone)
VALUES (10000112, '0937111112', 'INDIVIDUAL', 'INACTIVE', 'full name 1', 'MALE', 'User', 'User',
        '00000000-0000-0000-0000-000000000000', '0937111112');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone, bank_link_status)
VALUES (10000114, '0937111114', 'INDIVIDUAL', 'ACTIVE', 'full name 1', 'MALE', 'User', 'User',
        '00000000-0000-0000-0000-000000000000', '0937111114', 'ACTIVE');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone, bank_link_status,
                   available_balance)
VALUES (10000115, '0937111115', 'INDIVIDUAL', 'ACTIVE', 'full name 1', 'MALE', 'User', 'User',
        '00000000-0000-0000-0000-000000000000', '0937111115', 'UNLINK', 100000);


INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id, otp_phone, bank_link_status,
                   frozen_balance)
VALUES (10000116, '0937111116', 'INDIVIDUAL', 'ACTIVE', 'full name 1', 'MALE', 'User', 'User',
        '00000000-0000-0000-0000-000000000000', '0937111116', 'UNLINK', 100000);

INSERT INTO merchant(merchant_id, merchant_name, created_by, updated_by)
VALUES ('10000027', 'Test Merchant', 'operator', 'operator');

INSERT INTO wallet_link(wallet_link_id, wallet_id, user_id, merchant_id, created_by, updated_by)
VALUES ('26a9e208-c556-43c5-b28e-7437d09aa9be', 10000111, '102342', '10000026', 'System', 'System');