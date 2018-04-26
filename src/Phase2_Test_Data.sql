insert into customer values ('01d00',  'Hanish',  23,   'Male');
insert into customer values ('95766', 'Michael',  23,   'Male');
insert into customer values ('e8607',    'Aziz',  22,   'Male');
insert into customer values ('f846e',   'Riley',  28, 'Female');
insert into customer values ('30885', 'Natasha',  24, 'Female');

insert into Hotel values (1000,     'Hilton', 'Chicago', 'MichiganAve', '100', 'IL', 6172829990);
insert into Hotel values (1001,     'Hilton',  'Dallas', 'OklahomaAve', '101', 'TX', 6172810920);
insert into Hotel values (1002,     'Hilton',      'LA',     'RodeoDr', '103', 'CA', 9134569990);
insert into Hotel values (1003,      'Hyatt', 'Chicago', 'MichiganAve', '200', 'IL', 6172982990);
insert into Hotel values (1004,      'Hyatt',  'Dallas', 'OklahomaAve', '201', 'TX', 6172283920);
insert into Hotel values (1005,      'Hyatt',      'LA',     'RodeoDr', '203', 'CA', 9131029990);
insert into Hotel values (1006, 'HansEtFonz', 'Chicago', 'MichiganAve', '300', 'IL', 6171029990);
insert into Hotel values (1007, 'HansEtFonz',  'Dallas', 'OklahomaAve', '301', 'TX', 6172875860);
insert into Hotel values (1008, 'HansEtFonz',      'LA',     'RodeoDr', '303', 'CA', 9134100690);

insert into Room values ( 'King',  4);
insert into Room values ( 'Queen', 3);
insert into Room values ( 'Twin',  2);
insert into Room values ( 'Suite', 6);

insert into Hotel_Room values (1000,     'Hilton', 'Suite',  13);
insert into Hotel_Room values (1001,     'Hilton', 'King',   35);
insert into Hotel_Room values (1003,      'Hyatt', 'Suite', 13);
insert into Hotel_Room values (1006, 'HansEtFonz', 'King',  26);

insert into Reservation values ('1000000', '01d00', 6, 0, 1, 1, 2018, 1, 2, 2018, 'Hilton', 1000, 'Suite');
insert into Reservation values ('1000001', '95766', 3, 0, 1, 2, 2018, 1, 3, 2018, 'Hilton', 1001,  'King');
insert into Reservation values ('1000002', 'e8607', 4, 0, 1, 3, 2018, 1, 4, 2018,  'Hyatt', 1003, 'Suite');
insert into Reservation values ('1000003', 'f846e', 2, 0, 1, 4, 2018, 1, 5, 2018,  'Hyatt', 1004, 'Suite');

insert into Date_List values (1, 1,  2018);
insert into Date_List values (1, 2,  2018);
insert into Date_List values (1, 3,  2018);
insert into Date_List values (1, 4,  2018);
insert into Date_List values (1, 5,  2018);
insert into Date_List values (1, 6,  2018);
insert into Date_List values (1, 7,  2018);
insert into Date_List values (1, 8,  2018);
insert into Date_List values (1, 9,  2018);
insert into Date_List values (1, 10, 2018);

insert into Price_Info values ('Hilton', 1000, 'Suite', 1, 1,  2018, 150, 20);
insert into Price_Info values ('Hilton', 1001, 'Suite', 1, 2,  2018, 145, 19);
insert into Price_Info values ('Hilton', 1002,  'King', 1, 3,  2018,  90, 22);
insert into Price_Info values ( 'Hyatt', 1003, 'Suite', 1, 4,  2018, 200, 15);
insert into Price_Info values ( 'Hyatt', 1004, 'Suite', 1, 5,  2018, 210, 12);

