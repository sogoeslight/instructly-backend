-- noinspection SqlInsertValues

INSERT INTO app_user
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574531', 'Student', 'johnnystudent@gmail.com', '+37122222222',
        'Johnny', 'Dawson', 'path/to/pic', localtimestamp, localtimestamp);
INSERT INTO student
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574531', DEFAULT);
INSERT INTO preferences
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574531', 'Light', 'Latvian', 'Two', 'false');

-- noinspection SqlInsertValues

INSERT INTO app_user
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574532', 'Instructor', 'johnathaninstructor@gmail.com', '+37133333333',
        'Johnathan', 'Dawkins', 'anotherpath/to/pic', localtimestamp, localtimestamp);
INSERT INTO instructor
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574532', 'Im an instructor', 'Riga center', ARRAY ['English'], 0, DEFAULT);
INSERT INTO preferences
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574532', 'Light', 'Latvian', 'Two', 'false');
INSERT INTO vehicle
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574533', 'fd7e1b90-31c2-4d68-b5c3-c96233574532', 'B', 'AA-1234', 'Audi', 'A4',
        '2017', 'Automatic', 'All');

INSERT INTO app_user
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574534', 'Instructor', 'michaelinstructor@gmail.com', '+37144444444',
        'Michael', 'Dawkins', 'anotherpath/to/pic', localtimestamp, localtimestamp);
INSERT INTO instructor
VALUES ('fd7e1b90-31c2-4d68-b5c3-c96233574534', 'Im an instructor', 'Riga center', ARRAY ['English'], 0, DEFAULT);