INSERT INTO authority(id, name) VALUES (1, 'ROLE_REG_USER');
INSERT INTO authority(id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authority(id, name) VALUES (3, 'ROLE_REWIEWER');
INSERT INTO authority(id, name) VALUES (4, 'ROLE_EDITOR');
INSERT INTO authority(id, name) VALUES (5, 'ROLE_GUEST');

INSERT INTO public.scientific_area(id, name) VALUES (1, 'Matematika');
INSERT INTO public.scientific_area(id, name) VALUES (2, 'Fizika');
INSERT INTO public.scientific_area(id, name) VALUES (3, 'Knjizevnost');

--admin admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (1212, 'Kula', 'true', 'matkovskim@neobee.net', 'Matkovski', 'Marijana', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'false', 'Srbija', 'admin', 'admin');
--recenzent1 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (2222, 'Vrbas', 'true', 'matkovskim@gmail.com', 'Recenzent1', 'Recenzentovic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'recenzent', 'recenzent1');
--recenzent2 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (3333, 'Novi Sad', 'true', 'matkovskim@gmail.com', 'Recenzent2', 'Recenzic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'recenzent', 'recenzent2');
--recenzent3 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (4444, 'Beograd', 'true', 'matkovskim@gmail.com', 'Recenzent3', 'Rece', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'recenzent', 'recenzent3');
--recenzent3 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (7777, 'Beograd', 'true', 'matkovskim@gmail.com', 'Recenzent4', 'Rec', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'recenzent', 'recenzent4');
--urednik1 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (5555, 'Kucura', 'true', 'matkovskim@gmail.com', 'Urednik1', 'Urednikovic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'AAAssdasd', 'false', 'Srbija', 'urednik', 'urednik1');
--urednik2 admin
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (6666, 'Ruski Krstur', 'true', 'matkovskim@gmail.com', 'Urednik2', 'Urednikic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'AAAssdasd', 'false', 'Srbija', 'urednik', 'urednik2');
--korisnik
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (2323, 'Ruski Krstur', 'true', 'matkovskim@gmail.com', 'korisnik', 'Urednikic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'AAAssdasd', 'false', 'Srbija', 'korisnik', 'korisnik');
--gost
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (8907, 'Krstur', 'true', 'matkovskim@gmail.com', 'korisnik', 'Urednikic', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'AAAssdasd', 'false', 'Srbija', 'korisnik', 'gost');


INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (1212, 2);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (2222, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (4444, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (7777, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (3333, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (5555, 4);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (6666, 4);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (2323, 1);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (8907, 5);

INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (2222, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (3333, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (4444, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (5555, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (6666, 2);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (7777, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (2222, 2);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (4444, 2);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (3333, 2);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (2323, 2);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (5555, 3);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (6666, 3);


INSERT INTO public.magazine(id, isbn, activated, name, who_pays, main_editor_id, email) VALUES (111111, 'mmm-sadsad', true, 'magazineX', 'Citaoci', 5555, 'matkovskim@gmail.com');
INSERT INTO public.magazine(id, isbn, activated, name, who_pays, main_editor_id, email) VALUES (222222, 'mmm-dsff', true, 'magoija', 'Autori', 5555, 'test@gmail.com');

INSERT INTO public.magazine_scientific_area(magazine_id, scientific_area_id) VALUES (111111, 1);
INSERT INTO public.magazine_scientific_area(magazine_id, scientific_area_id) VALUES (111111, 2);
INSERT INTO public.magazine_scientific_area(magazine_id, scientific_area_id) VALUES (222222, 1);

INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id)VALUES (111111, 2222);
INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id)VALUES (111111, 3333);
INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id)VALUES (111111, 4444);
INSERT INTO public.magazine_reviewers(magazine_id, reviewers_id)VALUES (111111, 7777);

INSERT INTO public.magazine_editors(magazine_id, editors_id)VALUES (111111, 6666);