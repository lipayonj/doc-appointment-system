MERGE INTO users (id, email, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username) KEY(id) VALUES (1, 'jerwin@gmail.com', true, true, true, true, '$2a$10$kRsaRpNhs1rtFL9ImQZhXe9q5/xZ1zX3yjbOUilo0b0B4p75MllB.', 'jerwin@gmail.com');
MERGE INTO users (id, email, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username) KEY(id) VALUES (2, 'l@gmail.com', true, true, true, true, '$2a$10$pRXXabRGF6sRJ8XuV48quu.xAbiB2kj2NF7x6Yjok3DBlHyggKNlC', 'l@gmail.com');
MERGE INTO users (id, email, is_account_non_expired, is_account_non_locked, is_credentials_non_expired, is_enabled, password, username) KEY(id) VALUES (3, 'oje@gmail.com', true, true, true, true, '$2a$10$Fd2pk/mYggEvqtAyPjVH1OIUO9z4UWI4.Zp/yZ1BChl/K/JY2Pgh6', 'oje@gmail.com');

MERGE INTO week_schedules (id, appointment_duration) KEY(id) VALUES (1, 0);

MERGE INTO doctors (id, address, date_of_birth, description, first_name, gender, last_name, start_practice_date, telephone_number, user_id, week_schedule_id) KEY(id) VALUES (1, 'Tanauan, Leyte', '2020-02-06 00:00:00', 'Fulltime', 'Jerwin', 'MALE', 'Lipayon', '2020-02-09 00:00:00', '1234567890', 1, 1);

MERGE INTO patients (id, date_of_birth, date_of_enrollment, first_name, gender, is_insured, last_name, telephone_number, doctor_id, user_id) KEY(id) VALUES (1,'2020-02-06 00:00:00', NULL, 'Jerwin', 'MALE', true, 'Lipayon', '1234567890', 1, 2);
MERGE INTO patients (id, date_of_birth, date_of_enrollment, first_name, gender, is_insured, last_name, telephone_number, doctor_id, user_id) KEY(id) VALUES (2, '2020-02-07 00:00:00', NULL, 'Oje', 'MALE', true, 'Lipayon', '1234567890', 1, 3);

MERGE INTO appointments (id, date, date_from, date_to, description, doctor_id, patient_id) KEY(id) VALUES (1, '2020-02-03', '08:00:00', '10:00:00', 'First Day Appointment', 1, 1);
MERGE INTO appointments (id, date, date_from, date_to, description, doctor_id, patient_id) KEY(id) VALUES (2, '2020-02-04', '08:00:00', '10:00:00', 'Second Day Appointment', 1, 1);
MERGE INTO appointments (id, date, date_from, date_to, description, doctor_id, patient_id) KEY(id) VALUES (3, '2020-02-11', '08:00:00', '10:00:00', 'Third Day Appointment', 1, 1);
MERGE INTO appointments (id, date, date_from, date_to, description, doctor_id, patient_id) KEY(id) VALUES (4, '2020-02-04', '10:00:00', '12:00:00', 'First Day Appointment', 1, 2);
MERGE INTO appointments (id, date, date_from, date_to, description, doctor_id, patient_id) KEY(id) VALUES (5, '2020-02-11', '13:00:00', '14:00:00', 'Second Day Appointment', 1, 2);

MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (7, 'SUNDAY', '00:00:00', '00:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (1, 'MONDAY', '17:00:00', '08:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (2, 'TUESDAY', '17:00:00', '08:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (3, 'WEDNESDAY', '17:00:00', '08:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (4, 'THURSDAY', '17:00:00', '08:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (5, 'FRIDAY', '17:00:00', '08:00:00', 1);
MERGE INTO day_schedules (id, day_of_week, end_time, start_time, week_schedule_id) KEY(id) VALUES (6, 'SATURDAY', '17:00:00', '08:00:00', 1);

MERGE INTO roles (id, authority) KEY(id) VALUES (1, 'ROLE_DOCTOR');
MERGE INTO roles (id, authority) KEY(id) VALUES (2, 'ROLE_PATIENT');

MERGE INTO users_roles (user_id, role_id) KEY(user_id, role_id) VALUES (1, 1);
MERGE INTO users_roles (user_id, role_id) KEY(user_id, role_id) VALUES (2, 2);
MERGE INTO users_roles (user_id, role_id) KEY(user_id, role_id) VALUES (3, 2);