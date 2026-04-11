-- Create Attendances table
CREATE TABLE IF NOT EXISTS attendances (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    username VARCHAR(50) NOT NULL,
    attendance_date DATE NOT NULL DEFAULT CURRENT_DATE,
    attendance_time_in TIME NOT NULL DEFAULT CURRENT_TIME,
    attendance_time_in_latitude DECIMAL(9, 6) NOT NULL,
    attendance_time_in_longitude DECIMAL(9, 6) NOT NULL,
    attendance_time_out TIME,
    attendance_time_out_latitude DECIMAL(9, 6),
    attendance_time_out_longitude DECIMAL(9, 6),
    photo_url TEXT,
    is_valid BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
-- Indices for performance
CREATE INDEX IF NOT EXISTS idx_attendances_user_id ON attendances(user_id);
CREATE INDEX IF NOT EXISTS idx_attendances_date ON attendances(attendance_date);
-- Triggers
DROP TRIGGER IF EXISTS trg_attendances_updated_at ON attendances;
CREATE TRIGGER trg_attendances_updated_at BEFORE
UPDATE ON attendances FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();