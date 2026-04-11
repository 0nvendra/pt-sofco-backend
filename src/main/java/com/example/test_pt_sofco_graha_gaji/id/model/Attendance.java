package com.example.test_pt_sofco_graha_gaji.id.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "attendances")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(name = "attendance_date", nullable = false)
    private LocalDate attendanceDate;

    @Column(name = "attendance_time_in", nullable = false)
    private LocalTime attendanceTimeIn;

    @Column(name = "attendance_time_out")
    private LocalTime attendanceTimeOut;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "attendance_time_in_latitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal attendanceTimeInLatitude;

    @Column(name = "attendance_time_in_longitude", precision = 9, scale = 6, nullable = false)
    private BigDecimal attendanceTimeInLongitude;

    @Column(name = "attendance_time_out_latitude", precision = 9, scale = 6)
    private BigDecimal attendanceTimeOutLatitude;

    @Column(name = "attendance_time_out_longitude", precision = 9, scale = 6)
    private BigDecimal attendanceTimeOutLongitude;

    @Column(name = "is_valid")
    @Builder.Default
    private Boolean isValid = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
