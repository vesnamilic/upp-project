INSERT INTO scientific_area(id, deleted, name) VALUES (1, false, 'Scientific Area 1');
INSERT INTO scientific_area(id, deleted, name) VALUES (2, false, 'Scientific Area 2');
INSERT INTO scientific_area(id, deleted, name) VALUES (3, false, 'Scientific Area 3');
INSERT INTO authority(id, name) VALUES (1, 'ROLE_ADMINISTRATOR');
INSERT INTO authority(id, name) VALUES (2, 'ROLE_REVIEWER');
INSERT INTO authority(id, name) VALUES (3, 'ROLE_EDITOR');
INSERT INTO authority(id, name) VALUES (4, 'ROLE_REG_USER');

INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (500, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Vesna', 'Milic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'vesnamilic');
INSERT INTO user_authorities(id, authority_id) VALUES (500, 1);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (501, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Milica', 'Milic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewereditor');
INSERT INTO user_authorities(id, authority_id) VALUES (501, 2);
INSERT INTO user_authorities(id, authority_id) VALUES (501, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (502, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Petar', 'Komnenic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer');
INSERT INTO user_authorities(id, authority_id) VALUES (502, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (503, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Marijana', 'Matkovski', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'editor');
INSERT INTO user_authorities(id, authority_id) VALUES (503, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (504, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Marijana', 'Klosnjaji', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'editor2');
INSERT INTO user_authorities(id, authority_id) VALUES (504, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (505, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Andrea', 'Babic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer2');
INSERT INTO user_authorities(id, authority_id) VALUES (505, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (506, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Tamara', 'Grozdanic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'mr', 'author1');
INSERT INTO user_authorities(id, authority_id) VALUES (506, 4);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (507, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Vladimir', 'Micunovic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'author2');
INSERT INTO user_authorities(id, authority_id) VALUES (507, 4);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (508, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Maja', 'Micunovic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'author3');
INSERT INTO user_authorities(id, authority_id) VALUES (508, 4);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (509, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Reviewer', '3', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer3');
INSERT INTO user_authorities(id, authority_id) VALUES (509, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (510, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Reviewer', '4', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer4');
INSERT INTO user_authorities(id, authority_id) VALUES (510, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (511, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Reviewer', '5', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer5');
INSERT INTO user_authorities(id, authority_id) VALUES (511, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (512, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Editor', '3', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'editor3');
INSERT INTO user_authorities(id, authority_id) VALUES (512, 3);
INSERT INTO user_authorities(id, authority_id) VALUES (512, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (513, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Editor', '4', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'editor4');
INSERT INTO user_authorities(id, authority_id) VALUES (513, 3);

INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (501, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (501, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (501, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (502, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (503, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (504, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (505, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (506, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (506, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (507, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (508, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (508, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (509, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (509, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (509, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (510, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (510, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (511, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (511, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (512, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (512, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (512, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (513, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (513, 3);


INSERT INTO magazine(id, email, isssn, approved, deleted, name, payment_method, main_editor_id, price) VALUES (500, 'test@gmail.com', 'issn1', true, false, 'Magazine 1', 'AUTHORS', 504, 50);
INSERT INTO magazine(id, email, isssn, approved, deleted, name, payment_method, main_editor_id, price) VALUES (501, 'test@gmail.com', 'issn1', true, false, 'Magazine 2', 'READERS', 503, 20);
INSERT INTO magazine(id, email, isssn, approved, deleted, name, payment_method, main_editor_id, price) VALUES (502, 'test@gmail.com', 'issn1', true, false, 'Magazine 3', 'AUTHORS', 512, 10);
INSERT INTO magazine(id, email, isssn, approved, deleted, name, payment_method, main_editor_id, price) VALUES (503, 'test@gmail.com', 'issn1', true, false, 'Magazine 4', 'READERS', 513, 15);
INSERT INTO magazine(id, email, isssn, approved, deleted, name, payment_method, main_editor_id, price) VALUES (504, 'test@gmail.com', 'issn1', true, false, 'Magazine 5', 'AUTHORS', 504, 18);

INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (500, 3);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (500, 1);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (500, 2);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (501, 3);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (501, 2);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (502, 1);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (502, 3);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (503, 3);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (504, 3);
INSERT INTO magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (504, 2);


INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 501);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 502);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 509);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 510);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 511);

INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (501, 509);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (501, 510);

INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (502, 509);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (502, 511);

INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (503, 501);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (503, 512);

INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (504, 505);
INSERT INTO magazine_reviewers(magazine_id, reviewers_id) VALUES (504, 509);

INSERT INTO magazine_editors(magazine_id, editors_id) VALUES (500, 513);
INSERT INTO magazine_editors(magazine_id, editors_id) VALUES (501, 512);

INSERT INTO magazine_issue(id,"number", publication_date, published, magazine_id) VALUES (500,1, '2020-03-10 08:21:59.429', false , 500);
INSERT INTO magazine_issue(id,"number", publication_date, published, magazine_id) VALUES (501,1, '2020-03-10 08:21:59.429', false , 501);
INSERT INTO magazine_issue(id,"number", publication_date, published, magazine_id) VALUES (502,1, '2020-03-10 08:21:59.429', false , 502);
INSERT INTO magazine_issue(id,"number", publication_date, published, magazine_id) VALUES (503,1, '2020-03-10 08:21:59.429', false , 503);
INSERT INTO magazine_issue(id,"number", publication_date, published, magazine_id) VALUES (504,1, '2020-03-10 08:21:59.429', false , 504);

INSERT INTO membership(id, payed_until, magazine_id) VALUES (500, '2020-03-10 08:21:59.429', 500);
INSERT INTO membership(id, payed_until, magazine_id) VALUES (501, '2020-03-10 08:21:59.429', 504);

INSERT INTO registered_user_memberships(registered_user_id, memberships_id) VALUES (507, 500);
INSERT INTO registered_user_memberships(registered_user_id, memberships_id) VALUES (506, 501);