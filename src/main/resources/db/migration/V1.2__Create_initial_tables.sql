CREATE extension IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS app_user
(
    id           uuid PRIMARY KEY,
    user_type    user_type                                                                 NOT NULL,
    email        email                                                                     NOT NULL UNIQUE,
    phone_number phone_number                                                              NOT NULL UNIQUE,
    first_name   text CHECK (char_length(first_name) > 2 AND char_length(first_name) < 32) NOT NULL,
    last_name    text CHECK (char_length(last_name) > 0 AND char_length(last_name) < 32),
    userpic_path text,
    created_at   timestamp,
    modified_at  timestamp
);

CREATE TABLE IF NOT EXISTS student
(
    id              uuid PRIMARY KEY REFERENCES app_user (id) ON DELETE CASCADE,
    current_balance decimal(5, 2) DEFAULT 0 NOT NULL
);

CREATE TABLE IF NOT EXISTS instructor
(
    id               uuid PRIMARY KEY REFERENCES app_user (id) ON DELETE CASCADE,
    self_description text CHECK (char_length(self_description) < 256),
    start_location   text CHECK (char_length(start_location) > 0 AND char_length(start_location) < 64) NOT NULL,
    language         text[]                                                                            NOT NULL,
    grade            decimal(3, 2),
    is_active        bool                                                                              NOT NULL DEFAULT true
);

CREATE TABLE IF NOT EXISTS preferences
(
    id                         uuid PRIMARY KEY REFERENCES app_user (id) ON DELETE CASCADE,
    theme                      theme           NOT NULL,
    ui_language                ui_language     NOT NULL,
    calendar_def_display_weeks weeks_displayed NOT NULL,
    route_tracking_enabled     bool            NOT NULL DEFAULT false,
    created_at                 timestamp,
    modified_at                timestamp
);

CREATE TABLE IF NOT EXISTS vehicle
(
    id               uuid PRIMARY KEY,
    instructor_id    uuid REFERENCES instructor (id) ON DELETE CASCADE,
    vehicle_category vehicle_category                                                              NOT NULL,
    reg_plate        text CHECK (char_length(reg_plate) > 2 AND char_length(reg_plate) < 16)       NOT NULL,
    manufacturer     text CHECK (char_length(manufacturer) > 2 AND char_length(manufacturer) < 32) NOT NULL,
    model            text CHECK (char_length(model) > 1 AND char_length(model) < 32)               NOT NULL,
    production_year  int CHECK (production_year > 1990 AND
                                production_year <= date_part('year', current_date) + 1)            NOT NULL,
    gearbox          gearbox                                                                       NOT NULL,
    wheel_drive      wheel_drive                                                                   NOT NULL,
    created_at       timestamp,
    modified_at      timestamp
);

CREATE TABLE IF NOT EXISTS week_option
(
    id            uuid PRIMARY KEY,
    instructor_id uuid NOT NULL REFERENCES instructor (id),
    created_at    timestamp,
    modified_at   timestamp
);

CREATE TABLE IF NOT EXISTS lesson_option
(
    id               uuid PRIMARY KEY,
    instructor_id    uuid             NOT NULL REFERENCES instructor (id),
    duration_minutes smallint         NOT NULL,
    cost             money            NOT NULL,
    vehicle_category vehicle_category NOT NULL,
    created_at       timestamp,
    modified_at      timestamp
);

CREATE TABLE IF NOT EXISTS day_option
(
    id               uuid PRIMARY KEY,
    lesson_option_id uuid NOT NULL REFERENCES lesson_option (id),
    instructor_id    uuid NOT NULL REFERENCES instructor (id),
    week_option_id   uuid NOT NULL REFERENCES week_option (id),
    created_at       timestamp,
    modified_at      timestamp
);

CREATE TABLE IF NOT EXISTS review
(
    id            uuid PRIMARY KEY,
    instructor_id uuid                                       NOT NULL REFERENCES instructor (id),
    student_id    uuid                                       NOT NULL REFERENCES student (id),
    date          date                                       NOT NULL,
    grade         smallint CHECK (grade >= 0 AND grade <= 5) NOT NULL,
    comment       text CHECK (char_length(comment) < 512)    NOT NULL,
    created_at    timestamp,
    modified_at   timestamp
);

CREATE TABLE IF NOT EXISTS lesson
(
    id               uuid PRIMARY KEY,
    lesson_option_id uuid      NOT NULL REFERENCES lesson_option (id),
    student_id       uuid      NOT NULL REFERENCES student (id),
    is_booked        bool DEFAULT false,
    datetime         timestamp NOT NULL,
    drive_route      json,
    created_at       timestamp,
    modified_at      timestamp
);

CREATE TABLE IF NOT EXISTS mistake
(
    id          uuid PRIMARY KEY,
    lesson_id   uuid                                     NOT NULL REFERENCES lesson (id),
    comment     text CHECK (char_length(comment) <= 150) NOT NULL,
    audio_path  text,
    coordinates json,
    created_at  timestamp,
    modified_at timestamp
);
