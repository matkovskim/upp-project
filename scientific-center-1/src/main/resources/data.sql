INSERT INTO authority(id, name) VALUES (1, 'ROLE_REG_USER');
INSERT INTO authority(id, name) VALUES (2, 'ROLE_ADMIN');
INSERT INTO authority(id, name) VALUES (3, 'ROLE_REWIEWER');
INSERT INTO authority(id, name) VALUES (4, 'ROLE_EDITOR');

INSERT INTO public.scientific_area(id, name) VALUES (1, 'MajaOblast');

INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (1212, 'Kula', 'true', 'matkovskim@neobee.net', 'Matkovski', 'Marijana', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'false', 'Srbija', 'admin', 'admin');
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (2222, 'Kula', 'true', 'matkovskim@gmail.com', 'Hsad', 'Vesna', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'vesna', 'vesna');
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (3333, 'Kula', 'true', 'matkovskim@gmail.com', 'Nesto', 'Lasad', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'majak', 'majak');
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (4444, 'Kula', 'true', 'matkovskim@gmail.com', 'Msd', 'Msda', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'dkjahdkjhasdkjh', 'true', 'Srbija', 'bbbb', 'bbbbb');
INSERT INTO public.registred_user(id, city, confirmed, email, last_name, name, password, registration_code, reviewer, state, title, username) VALUES (5555, 'Kula', 'true', 'matkovskim@gmail.com', 'Dad', 'Dsa', '$2a$10$9vWGQ5BQTjEzQH6AIzAUXuTtWlLjVCX8w20CYrp8VzAUJbDaCO4vi', 'AAAssdasd', 'true', 'Srbija', 'aaaa', 'aaaa');

INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (1212, 2);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (2222, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (4444, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (5555, 3);
INSERT INTO public.registred_user_authorities(registred_user_id, authorities_id) VALUES (3333, 4);

INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (2222, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (3333, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (4444, 1);
INSERT INTO public.registred_user_scientific_area(registred_user_id, scientific_area_id) VALUES (5555, 1);
