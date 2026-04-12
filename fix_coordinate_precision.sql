-- Fix coordinate column precision
-- DECIMAL(9,6) hanya muat 3 digit sebelum desimal (max: 999.999999)
-- Longitude seperti 117.165098 butuh minimal DECIMAL(11,8)
ALTER TABLE attendances
    ALTER COLUMN attendance_time_in_latitude TYPE DECIMAL(11, 8),
    ALTER COLUMN attendance_time_in_longitude TYPE DECIMAL(11, 8),
    ALTER COLUMN attendance_time_out_latitude TYPE DECIMAL(11, 8),
    ALTER COLUMN attendance_time_out_longitude TYPE DECIMAL(11, 8);
