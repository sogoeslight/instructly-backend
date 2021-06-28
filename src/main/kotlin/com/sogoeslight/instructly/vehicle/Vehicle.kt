package com.sogoeslight.instructly.vehicle

import com.sogoeslight.instructly.audit.AuditModel
import com.sogoeslight.instructly.instructor.Instructor
import com.sogoeslight.instructly.validation.ValidYear
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Size

@Repository
interface VehicleRepository : JpaRepository<Vehicle, UUID>

@Entity
//@Cacheable
@Table(name = "vehicle")
class Vehicle(

    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    val id: UUID = UUID.randomUUID(),

    @ManyToOne(
        fetch = FetchType.LAZY,
        optional = false
    )
    @JoinColumn(name = "instructor_id")
    var instructor: Instructor? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_category")
    var vehicleCategory: VehicleCategory = VehicleCategory.B,

    @Column(name = "reg_plate")
    @field:Size(min = 2, max = 16)
    var regPlate: String = "",

    @Column(name = "manufacturer")
    @field:Size(min = 2, max = 32)
    var manufacturer: String = "",

    @Column(name = "model")
    @field:Size(min = 1, max = 32)
    var model: String = "",

    @Column(name = "production_year")
    @ValidYear
    var productionYear: Short? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "gearbox")
    var gearBox: Gearbox? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "wheel_drive")
    var wheelDrive: WheelDrive? = null

) : AuditModel()
