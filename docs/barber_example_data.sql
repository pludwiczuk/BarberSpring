insert into public.users (id, email, first_name, last_name, password, role, username)
values  (1, 'mpiotrowski@example.com', 'Maciej', 'Piotrowski', '$2a$10$906SOq0Wzihk2EqS0wh.j.OJrLUbVSdTtPGyApXzqI0fWx77f5o.y', '2', 'mpiotrowski'),
        (2, 'dwysocki@example.com', 'Dominik', 'Wysocki', '$2a$10$jLFgN8LdZnCbEgeqgzNRKuePwamnMS6atSJLt8u16ni0X/4vCwZ/S', '2', 'dwysocki'),
        (3, 'gkalinowski@example.com', 'Gabriel', 'Kalinowski', '$2a$10$w1pVK0DEvqq3D22It9oEqOx8bxSyJdNlCE9zNPAheWCbu4BtyPAvi', '2', 'gkalinowski'),
        (4, 'agorecki@example.com', 'Arkadiusz', 'Górecki', '$2a$10$JQb3SkS5cyQoOqMjwO187uUzKUX/3R3KlEKmqxEdsd5Bz9Vb7b0.e', '2', 'agorecki'),
        (5, 'ejaworski@example.com', 'Edward', 'Jaworski', '$2a$10$ED1M3aSlBYqVMaQ4Ju/KG..Owf0OaejCAFQ8gWBWOLovf0mqp/1Q6', '2', 'ejaworski'),
        (6, 'ewitkowska@example.com', 'Ewelina', 'Witkowska', '$2a$10$XNWsn4LaxbBjdrrzqpvTGORY65KKM0kUdiJPZqr9Dt2r4.MNVNvnm', '2', 'ewitkowska'),
        (7, 'lszymanska@example.com', 'Łucja', 'Szymańska', '$2a$10$jP7REDCiWw2di7Z/RFUkieo7EJtswCuzMHkniJ5q1P/GeDEF4jA56', '2', 'lszymanska'),
        (8, 'mprzybylska@example.com', 'Marta', 'Przybylska', '$2a$10$mGeDwN7PjNpT5kd0yjhqRO9BjFBShx8.Wrs9I/SJ4HV0uzenL16mG', '2', 'mprzybylska'),
        (9, 'dkowalczyk@example.com', 'Diana', 'Kowalczyk', '$2a$10$epzH3pJPL3ExRjvUWb3VCufp8vwJQzwxghQW.Js5r1nSiXShrsvoS', '2', 'dkowalczyk'),
        (10, 'jmarciniak@example.com', 'Jola', 'Marciniak', '$2a$10$HeodIwYUmM1FOR1QsvT.0e3oaon43jmNWNd5PvbenptYlyyrleXy6', '2', 'jmarciniak'),
        (11, 'mkolodziej@example.com', 'Magda', 'Kołodziej', '$2a$10$5KSRPPgNYsyTfe3pHA6VBu9fH67JHHJIwqM5QvbvcfU15zSvhwjda', '1', 'mkolodziej'),
        (12, 'eszczepanska@example.com', 'Elżbieta', 'Szczepańska', '$2a$10$umgLaI18ruhZWIaeMecrjeSk1piNtm2eLjeycv2jDt0xq.JJjkO.6', '1', 'eszczepanska'),
        (13, 'tsawicka@example.com', 'Teresa', 'Sawicka', '$2a$10$a2enw2ZCLIDFhQsKjjJqTuJ0UfxjXSvHM0ner75F7smXyE5TLOSVW', '1', 'tsawicka'),
        (14, 'nrutkowski@example.com', 'Norbert', 'Rutkowski', '$2a$10$lL.jtSuxrwZ1NkMRvEB96unMBd62TIalt9k1bKjQnKQWfcpJ25dzq', '1', 'nrutkowski'),
        (15, 'jpietrzak@example.com', 'Jędrzej', 'Pietrzak', '$2a$10$FvTi757Sajyl4Ouxyq0EkuAPv/7PR5QrgFpLXbFnZSuB.i0qumKey', '1', 'jpietrzak');

        
insert into public.businesses (id, address_city, address_coutry, address_line1, address_line2, address_post_code, name)
values  (1, 'Lublin', 'Polska', 'ul. Borówkowa 93', '-', '20-142', 'Barber Lubelski'),
        (2, 'Poznań', 'Polska', 'ul. Bruzdowa 129', '-', '61-324', 'Kołodziej Fryzjerstwo');
        

insert into public.business_users (id, business_id, user_id)
values  (1, 1, 14),
        (2, 2, 11);
        

insert into public.locations (id, address_city, address_coutry, address_line1, address_line2, address_post_code, name, business_id)
values  (1, 'Lublin', 'Polska', 'ul. Borówkowa 93', '-', '20-142', 'Barber Lubelski', 1),
        (2, 'Poznań', 'Polska', 'ul. Bruzdowa 129', '-', '61-324', 'Salon fryzjerski "Niezły fryz"', 2),
        (3, 'Poznań', 'Polska', 'ul. Dziedzicka 1017', '-', '61-344', 'Salon fryzjerski "Kędziory"', 2);
        
        
insert into public.timetables (id, close_at, day_of_week, open_at, whole_day_closed, whole_day_open, location_id)
values  (14, '00:00:00', 6, '00:00:00', true, false, 2),
        (21, '00:00:00', 6, '00:00:00', true, false, 3),
        (8, '18:00:00', 0, '09:00:00', false, false, 2),
        (9, '18:00:00', 1, '09:00:00', false, false, 2),
        (10, '18:00:00', 2, '09:00:00', false, false, 2),
        (11, '18:00:00', 3, '09:00:00', false, false, 2),
        (12, '17:00:00', 4, '08:00:00', false, false, 2),
        (13, '15:00:00', 5, '09:00:00', false, false, 2),
        (20, '15:00:00', 5, '09:00:00', false, false, 3),
        (19, '18:00:00', 4, '08:00:00', false, false, 3),
        (18, '18:00:00', 3, '08:00:00', false, false, 3),
        (17, '18:00:00', 2, '08:00:00', false, false, 3),
        (16, '18:00:00', 1, '08:00:00', false, false, 3),
        (15, '18:00:00', 0, '08:00:00', false, false, 3),
        (7, '00:00:00', 6, '00:00:00', true, false, 1),
        (1, '16:00:00', 0, '08:00:00', false, false, 1),
        (2, '16:00:00', 1, '08:00:00', false, false, 1),
        (3, '16:00:00', 2, '08:00:00', false, false, 1),
        (4, '16:00:00', 3, '08:00:00', false, false, 1),
        (5, '16:00:00', 4, '08:00:00', false, false, 1),
        (6, '14:00:00', 5, '10:00:00', false, false, 1);
        

insert into public.services (id, description, duration, name, price, location_id)
values  (1, 'Strzyżenie klasyczne nożyczkami i maszynką', 30, 'Strzyżenie klasyczne', 5000, 1),
        (2, 'Strzyżenie maszynką', 20, 'Strzyżenie maszynką', 3000, 1),
        (3, 'Trymowanie + konturowanie brzytwą + pielęgnacja', 30, 'Strzyżenie brody', 5000, 1),
        (4, 'Strzyżenie włosów i brody', 60, 'Komplet', 9000, 1),
        (5, 'Strzyżenie męskie nożyczkami i maszynką', 25, 'Strzyżenie męskie', 4000, 3),
        (6, 'Strzyżenie damskie', 60, 'Strzyżenie damskie', 8000, 3),
        (7, 'SOMBRE na włosach naturalnych', 180, 'SOMBRE', 25000, 3),
        (8, 'Strzyżenie męskie nożyczkami i maszynką', 25, 'Strzyżenie męskie', 4000, 2),
        (9, 'Strzyżenie damskie', 60, 'Strzyżenie damskie', 9000, 2),
        (10, 'Upięcie okolicznościowe / ślubne', 90, 'Upięcie okolicznościowe / ślubne', 20000, 2);
        
        
insert into public.appointments (id, date, service_id, user_id, status)
values  (1, '2021-07-12 10:30:00.000000', 1, 1, 0),
        (2, '2021-07-12 11:00:00.000000', 4, 2, 0),
        (3, '2021-07-12 09:00:00.000000', 8, 3, 0),
        (4, '2021-07-12 17:00:00.000000', 8, 4, 0),
        (5, '2021-07-09 15:00:00.000000', 8, 5, 0),
        (6, '2021-07-12 11:00:00.000000', 9, 6, 0),
        (7, '2021-07-13 09:00:00.000000', 9, 7, 0),
        (8, '2021-07-09 12:00:00.000000', 9, 8, 0),
        (9, '2021-07-10 09:00:00.000000', 10, 9, 0),
        (10, '2021-07-17 10:30:00.000000', 10, 10, 0);
