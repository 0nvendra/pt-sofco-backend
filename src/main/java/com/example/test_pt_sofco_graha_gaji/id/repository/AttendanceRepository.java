package com.example.test_pt_sofco_graha_gaji.id.repository;

import com.example.test_pt_sofco_graha_gaji.id.model.Attendance;
import com.example.test_pt_sofco_graha_gaji.id.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findByUser(User user);
    List<Attendance> findByAttendanceDate(LocalDate date);
    Optional<Attendance> findByUserAndAttendanceDate(User user, LocalDate date);
}
