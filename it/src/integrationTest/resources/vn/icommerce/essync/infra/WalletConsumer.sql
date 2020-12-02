INSERT INTO merchant(merchant_id, merchant_name, created_by, updated_by)
VALUES(1, 'merchant_01', 'tuanlt2', 'tuanlt2');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, full_name, gender, created_by, updated_by,
                  fee_schedule_id)
VALUES(2000000001, '0970000001', 'INDIVIDUAL', 'INACTIVE', 'full_name_01', 'FEMALE', 'created_by_01', 'updated_by_01',
      '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet(wallet_id, wallet_phone, wallet_type, wallet_status, user_name, encrypted_pincode,
                  otp_phone, bank_link_status, kyc_level, description,
                  full_name, email, dob, gender, address,
                  available_balance, frozen_balance, cashback_balance,
                  created_by, updated_by, created_at, updated_at,
                  fee_schedule_id)
VALUES(2000000002, '0970000002', 'SELLER', 'ACTIVE', 'user_name_02', 'encrypted_pincode_02',
      '0970000007', 'ACTIVE', '4', 'description_02',
      'full_name_02', 'email_02@sendo.vn', '1990-10-28 00:00:00.000000', 'MALE', 'quận 7, thành phố Hồ Chí Minh',
      2000000000, 20000000, 2000000,
      'created_by_02', 'updated_by_02', '2019-12-27 10:28:00.000000', '2019-12-30 17:30:15.020000',
      '00000000-0000-0000-0000-000000000000');

INSERT INTO wallet_link(wallet_link_id, wallet_id, merchant_id,
                        user_id, created_by, updated_by,
                        created_at, updated_at)
VALUES('20000002-0000-0000-0000-000000000001', 2000000002, 1,
       'linked_user_id_01', 'wallet_link_created_by_01', 'wallet_link_updated_by_01',
       '2020-01-01 00:00:00.000000', '2020-01-01 01:01:01.001000');

INSERT INTO wallet_link(wallet_link_id, wallet_id, merchant_id,
                        user_id, created_by, updated_by,
                        created_at, updated_at)
VALUES('20000002-0000-0000-0000-000000000002', 2000000002, 1,
       'linked_user_id_02', 'wallet_link_created_by_02', 'wallet_link_updated_by_02',
       '2020-02-02 00:00:00.000000', '2020-02-02 02:02:02.002000');