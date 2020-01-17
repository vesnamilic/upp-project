INSERT INTO scientific_area(id, deleted, name) VALUES (1, false, 'Scientific Area 1');
INSERT INTO scientific_area(id, deleted, name) VALUES (2, false, 'Scientific Area 2');
INSERT INTO scientific_area(id, deleted, name) VALUES (3, false, 'Scientific Area 3');
INSERT INTO authority(id, name) VALUES (1, 'ROLE_ADMINISTRATOR');
INSERT INTO authority(id, name) VALUES (2, 'ROLE_REVIEWER');
INSERT INTO authority(id, name) VALUES (3, 'ROLE_EDITOR');
INSERT INTO authority(id, name) VALUES (4, 'ROLE_AUTHOR');
INSERT INTO authority(id, name) VALUES (5, 'ROLE_REG_USER');

INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (500, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Vesna', 'Milic', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'vesnamilic');
INSERT INTO user_authorities(id, authority_id) VALUES (500, 1);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (501, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Editor', 'Reviewer', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewereditor');
INSERT INTO user_authorities(id, authority_id) VALUES (501, 2);
INSERT INTO user_authorities(id, authority_id) VALUES (501, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (502, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Reviewer1', 'Reviewer1', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer');
INSERT INTO user_authorities(id, authority_id) VALUES (502, 2);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (503, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Editor1', 'Edito1', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'editor');
INSERT INTO user_authorities(id, authority_id) VALUES (503, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (504, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Editor2', 'Edito2', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', false, 'dr', 'editor2');
INSERT INTO user_authorities(id, authority_id) VALUES (504, 3);
INSERT INTO registered_user(id, city, country, deleted, email, enabled, first_name, last_name, last_password_reset_date, password, requested_reviewer_role, title, username) VALUES (505, 'Vrbas', 'Serbia' , false ,'wesnamilic@gmail.com', true, 'Reviewer2', 'Reviewer2', '2020-01-15 22:31:37.831', '$2a$10$iTpvtpMx5Yum2KlCttKND.WoFTpPT3TPrsXxXzo9coFyhUSiYYw7a', true, 'dr', 'reviewer2');
INSERT INTO user_authorities(id, authority_id) VALUES (505, 2);

INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (501, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (501, 2);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (502, 1);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (503, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (504, 3);
INSERT INTO registered_user_scientific_areas(registered_user_id, scientific_areas_id) VALUES (505, 3);


INSERT INTO magazine(id, isssn, approved, deleted, name, payment_method, main_editor_id) VALUES (500, 'issn1', true, false, 'Magazine 1', 'authors', 504);
INSERT INTO public.magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (500, 3);
INSERT INTO public.magazine_scientific_areas(magazine_id, scientific_areas_id) VALUES (500, 1);
INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 501);
INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id) VALUES (500, 502);

