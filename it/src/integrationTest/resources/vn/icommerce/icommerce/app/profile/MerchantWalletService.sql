INSERT INTO merchant(merchant_id, merchant_name, created_by, updated_by)
VALUES (1, 'merchant_01', 'tuanlt2', 'tuanlt2');

INSERT INTO merchant(merchant_id, merchant_name, created_by, updated_by)
VALUES (2, 'merchant_02', 'tuanlt2', 'tuanlt2');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id)
VALUES (2000000001, '0970000001', 'INDIVIDUAL', 'INACTIVE', 'full_name_01', 'MALE', 'tuanlt2',
        'tuanlt2', '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id)
VALUES (2000000002, '0970000002', 'SELLER', 'ACTIVE', 'full_name_02', 'MALE', 'tuanlt2', 'tuanlt2',
        '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet_link(wallet_link_id, wallet_id, merchant_id, user_id, created_by, updated_by)
VALUES ('20000000-0000-0000-0000-002000000002', 2000000002, 1,
        'linked_user_id_01', 'tuanlt2', 'tuanlt2');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id)
VALUES (2000000004, '0970000004', 'MERCHANT', 'LOCK_BY_OPERATOR', 'full_name_04', 'MALE', 'tuanlt2',
        'tuanlt2', '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender,
                   created_by, updated_by, fee_schedule_id)
VALUES (2000000005, '0970000005', 'INTERNAL', 'LOCK_BY_SYSTEM', 'full_name_05', 'MALE', 'tuanlt2',
        'tuanlt2', '00000000-0000-0000-0000-000000000000');