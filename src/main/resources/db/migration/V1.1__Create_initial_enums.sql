DO
$$
    DECLARE
    BEGIN
        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'user_type') THEN
            CREATE TYPE user_type AS ENUM (
                'Student',
                'Instructor',
                'Admin'
                );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'ui_language') THEN
            CREATE TYPE "ui_language" AS ENUM
                (
                    'Latvian',
                    'Russian',
                    'English'
                    );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'theme') THEN
            CREATE TYPE theme AS ENUM (
                'Dark',
                'Light'
                );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'gearbox') THEN
            CREATE TYPE gearbox AS ENUM (
                'Manual',
                'Automatic'
                );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'wheel_drive') THEN
            CREATE TYPE wheel_drive AS ENUM (
                'Front',
                'Rear',
                'All'
                );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'weeks_displayed') THEN
            CREATE TYPE weeks_displayed AS ENUM (
                'One',
                'Two',
                'Four'
                );
        END IF;

        IF NOT EXISTS(SELECT *
                      FROM pg_type typ
                               INNER JOIN pg_namespace nsp
                                          ON nsp.oid = typ.typnamespace
                      WHERE nsp.nspname = current_schema()
                        AND typ.typname = 'vehicle_category') THEN
            CREATE TYPE vehicle_category AS ENUM (
                'A1',
                'A2',
                'A',
                'AM',
                'B1',
                'B',
                'C1',
                'C',
                'D1',
                'D',
                'BE',
                'C1E',
                'CE',
                'D1E',
                'DE'
                );
        END IF;
    END
$$
LANGUAGE plpgsql;

CREATE CAST (character varying as user_type) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as ui_language) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as theme) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as gearbox) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as wheel_drive) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as weeks_displayed) WITH INOUT AS IMPLICIT;
CREATE CAST (character varying as vehicle_category) WITH INOUT AS IMPLICIT;