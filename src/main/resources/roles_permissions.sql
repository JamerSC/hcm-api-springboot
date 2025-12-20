/*
-- Query: SELECT * FROM hcm_test_db.permissions
LIMIT 0, 500

-- Date: 2025-12-20 19:18
*/
INSERT INTO `` (`id`,`description`,`name`) VALUES (1,'Can view users','VIEW_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (2,'Can add new users','CREATE_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (3,'Can update users','UPDATE_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (4,'Can archive users','ARCHIVE_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (5,'Can change password users','CHANGE_PASSWORD_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (6,'Can logout users','LOGOUT_USERS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (7,'Can view employees','VIEW_EMPLOYEES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (8,'Can view profile','VIEW_PROFILE');
INSERT INTO `` (`id`,`description`,`name`) VALUES (9,'Can add new employees','CREATE_EMPLOYEES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (10,'Can update employees','UPDATE_EMPLOYEES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (11,'Can archive employees','ARCHIVE_EMPLOYEES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (12,'Can unarchived or restore employees','UNARCHIVED_EMPLOYEES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (13,'Can view applicants - recruitment module','VIEW_APPLICANTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (14,'Can view applications - recruitment module','VIEW_APPLICATIONS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (15,'Can update applications - recruitment module','UPDATE_APPLICATIONS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (16,'Can approve applications - recruitment module','APPROVE_APPLICATIONS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (17,'Can reject applications - recruitment module','REJECT_APPLICATIONS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (18,'Can change application status to initial qualifications - recruitment module','INITIAL_QUALIFICATION');
INSERT INTO `` (`id`,`description`,`name`) VALUES (19,'Can change application status to first inteview - recruitment module','FIRST_INTERVIEW');
INSERT INTO `` (`id`,`description`,`name`) VALUES (20,'Can change application status to second inteview - recruitment module','SECOND_INTERVIEW');
INSERT INTO `` (`id`,`description`,`name`) VALUES (21,'Can change application status to contract proposal -  recruitment module','CONTRACT_PROPOSAL');
INSERT INTO `` (`id`,`description`,`name`) VALUES (22,'Can change application status to contract signed -  recruitment module','CONTRACT_SIGNED');
INSERT INTO `` (`id`,`description`,`name`) VALUES (23,'Can change application status to hire applicant -  recruitment module','HIRE_APPLICANTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (24,'Can view departments','VIEW_DEPARTMENTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (25,'Can add departments','CREATE_DEPARTMENTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (26,'Can update departments','UPDATE_DEPARTMENTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (27,'Can archive departments','ARCHIVE_DEPARTMENTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (28,'Can unarchived/restore departments','UNARCHIVED_DEPARTMENTS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (29,'Can view jobs','VIEW_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (30,'Can add jobs','CREATE_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (31,'Can update jobs','UPDATE_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (32,'Can change status of jobs to open','OPEN_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (33,'Can change status of jobs to close','CLOSE_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (34,'Can change status of jobs to filled','FILL_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (35,'Can archive jobs','ARCHIVE_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (36,'Can unarchived/restore jobs','UNARCHIVED_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (37,'Can view leaves','VIEW_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (38,'Can create leaves','CREATE_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (39,'Can cancel leaves','CANCEL_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (40,'Can view my leaves','VIEW_MY_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (41,'Can update leaves','UPDATE_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (42,'Can approve leaves','APPROVE_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (43,'Can reject leaves','REJECT_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (44,'Can archive leaves','ARCHIVE_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (45,'Can unarchived/restore leaves','UNARCHIVED_LEAVES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (46,'Can view hcm dashboard','VIEW_DASHBOARD');
INSERT INTO `` (`id`,`description`,`name`) VALUES (47,'Can view attendances','VIEW_ATTENDANCES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (48,'Can view my attendances','VIEW_MY_ATTENDANCES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (49,'Can check in attendances','CHECK_IN_ATTENDANCES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (50,'Can check out attendances','CHECK_OUT_ATTENDANCES');
INSERT INTO `` (`id`,`description`,`name`) VALUES (51,'Applicants can view open jobs','VIEW_OPEN_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (52,'Applicants can apply jobs','APPLY_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (53,'Applicants can view applied jobs','VIEW_APPLIED_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (54,'Applicants can withdraw applied jobs','WITHDRAW_APPLIED_JOBS');
INSERT INTO `` (`id`,`description`,`name`) VALUES (55,'Applicants can viwe its profile','MY_APPLICANT_PROFILE');
INSERT INTO `` (`id`,`description`,`name`) VALUES (56,'Applicants can update its profile','UPDATE_APPLICANT_PROFILE');
INSERT INTO `` (`id`,`description`,`name`) VALUES (57,'Applicants can upload resume ','UPLOAD_RESUME');
INSERT INTO `` (`id`,`description`,`name`) VALUES (58,'Applicants can delete its own account','DELETE_ACCOUNT');
