INSERT INTO places.country (id, code, name) VALUES (1, 'ENG', 'England'), (2, 'CAN', 'Canada'),
(3, 'FRA', 'France'), (4, 'UKR', 'Ukraine'),(7, 'SP', 'Spain'), (8, 'GR', 'Germany'), (16, 'Ug', 'Uganda');

INSERT INTO places.city (id, name, country_id) VALUES (1, 'London', 1), (2, 'Glasgow', 1), (3, 'Liverpool', 1), (4, 'Manchester', 1),
(5, 'Edinburgh', 1), (6, 'Quebec', 2), (7, 'Toronto', 2), (8, 'Victoria', 2), (9, 'Edmonton', 2)
, (10, 'Marseille', 3)
, (11, 'Lyon', 3)
, (12, 'Paris', 3)
, (13, 'Strasbourg', 3)
, (14, 'Mykolaiv', 4)
, (15, 'Odessa', 4)
, (16, 'Kiev', 4)
, (17, 'Kherson', 4)
, (18, 'Kharkiv', 4)
, (19, 'Lviv', 4)
, (20, 'Poltava', 4)
, (21, 'Madrid', 7)
, (23, 'San-Hose', 7)
, (25, 'Uga-uga', 16)
, (27, 'Rooty', 2)
, (32, 'Ugeria', 16);

INSERT INTO places.type (id, name) VALUES (1, 'Cafe')
,(2, 'Restaurant')
,(3, 'Museums')
,(4, 'Zoo')
,(5, 'Theater')
,(6, 'Cinema')
,(7, 'Amusement park')
,(8, 'Pub')
,(9, 'Fastfood')
,(10, 'Hotel')
,(11, 'Castle  ')
,(12, 'Famous building')
,(16, 'Club')
,(22, 'Library');

INSERT INTO places.place (id, name, city_id, type_id) VALUES (1, 'Multiplex', 14, 6)
,(2, 'Grifel', 14, 1)
,(3, 'Cooper', 8, 6)
,(4, 'Marshall', 12, 2)
,(5, 'Pig Bob', 7, 8)
,(6, 'Big Ben', 1, 12)
,(7, 'Richard', 1, 2)
,(8, 'Kievo-Pecherska Lavra', 16, 12)
,(9, 'National Opera House of Ukraine', 16, 5)
,(10, 'Prado Museum', 21, 3)
,(12, 'Chateau dIf', 10, 11)
,(13, 'Caprica', 14, 16)
,(14, 'Mac Dac', 14, 9)
,(15, 'tyuuiu', 1, 8)
,(17, 'Willy-park', 13, 7)
,(20, 'Nature museum', 6, 3)
,(22, 'Ugando-do-do', 25, 8)
,(23, 'Hottelll', 4, 10)
,(24, 'Once in Poltava', 20, 5)
,(25, 'Poltavas zoo', 20, 4)
,(26, 'Uga cafe', 25, 1)
,(27, 'Super pooper', 8, 22)
,(29, 'Lala pub', 16, 8)
,(30, 'Corara', 21, 1)
,(31, 'Guarenti', 11, 5);

INSERT INTO places.person (id, name, password) VALUES (1, 'Admin', '$2a$08$/2PpJnoVY2TytUDGwTRzpuwrxwS9RI2hkHqu2ZqtQd2Kd7X1mih9C')
,(2, 'Max', '$2a$08$3hosleZ6j64.UEcuPVa8E.2TQ.kcyn8PU3n/tyfcRXcOlpvAAwR26')
,(3, 'Dasha', '$2a$08$AA3aPDYFu90a9//0UCyqxupmBpI7mbNz998BGG7QI6x5dCismSjgy')
,(4, 'Vasya', '$2a$08$a9flrxP8HAXGwgSQYshMhu7nbHSP7K354VJguG8X1TmNGtsxJ.fE.')
,(5, 'Ugando', '$2a$08$7Wfg/q7yJQa18mPS6HUkqumf46bxTJcl5QRuDFzGW31BtOwRYrfrS')
,(7, 'Kek', '$2a$08$RhmJsGkbqdzpYpKo.ytsO.NtUyWPm0vSXweBjPipGWlbZOOUGaxTG');

INSERT INTO places.person_role (person_id, roles) VALUES (2, 'USER')
, (1, 'ADMIN')
, (3, 'USER')
, (4, 'USER')
, (5, 'USER');

INSERT INTO places.review (id, comment, rating, person_id, place_id) VALUES (1, 'Comments !!!!!!!!!!!!!!!', 7, 1, 2)
, (2, 'The best place i`ve ever been !', 9, 1, 4)
, (3, 'Bad seats :(', 4, 1, 1)
, (4, 'Cool atmosphere )) ', 6, 1, 3)
, (5, 'Interesting place where you can find awesome people and talk about everything  that you want !!! But, beer kinda bullshit.... ', 5, 1, 5)
, (6, 'Atmospheric place and bell sounds really strong', 8, 2, 6)
, (8, 'Interesting place that belongs to the UNESCO World Heritage Sites. ', 7, 3, 8)
, (9, 'Perfect actors and atmosphere !!!!!!!!!!!', 9, 3, 9)
, (10, 'A truly world-class museum! Has a collection of more than 5,000 paintings and I spend two days to see all of them :(', 6, 3, 10)
, (11, 'Castle on island, what can be better ?))', 10, 2, 12)
, (12, 'WTF club.
Next time I better go to the gym . ', 2, 4, 13)
, (13, 'At this place make the ugliest coffee I''ve ever tasted.', 2, 1, 14)
, (14, 'I love to buy delicious MacFlure here, in hot summer evening after my work  ))', 8, 3, 14)
, (15, 'yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy place is bullshit', 6, 1, 15)
, (16, 'Fun place!', 7, 1, 17)
, (17, 'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrr', 4, 1, 7)
, (18, 'Cool coffee and tea )', 7, 1, 7)
, (19, 'Perfect pub... uhh...', 9, 2, 22)
, (20, 'I felt scary when i been here last time, this is really old hotel.', 3, 1, 23)
, (22, 'Coolest place i`ve ever been in Ugando!!', 8, 1, 22)
, (23, 'Ldlldldldldl', 2, 1, 24)
, (24, 'The best park at the world !!!!!!!!!!', 10, 1, 17)
, (25, 'Tuuuuuuuuu', 3, 1, 15)
, (28, 'Perfect cafe with unforgettable atmosphere !', 10, 3, 26)
, (29, 'It was terrible ((', 5, 7, 24)
, (30, 'EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE', 2, 1, 7);
