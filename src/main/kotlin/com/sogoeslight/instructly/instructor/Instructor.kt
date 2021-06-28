package com.sogoeslight.instructly.instructor

import com.fasterxml.jackson.annotation.JsonFormat
import com.sogoeslight.instructly.user.User
import com.sogoeslight.instructly.user.UserType
import com.sogoeslight.instructly.vehicle.Vehicle
import com.vladmihalcea.hibernate.type.array.ListArrayType
import org.hibernate.annotations.Type
import org.hibernate.annotations.TypeDef
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Repository
interface InstructorRepository : JpaRepository<Instructor, UUID>

const val EMAIL_REGEX =
    "(?:[a-z0-9!#\$%&''*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&''*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

@Entity
@Cacheable
@Table(name = "instructor")
@TypeDef(name = "list-array", typeClass = ListArrayType::class)
class Instructor(
    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    override val id: UUID = UUID.randomUUID(),

    @Column(name = "email")
    @field:Pattern(regexp = EMAIL_REGEX)
    override var email: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    override var userType: UserType = UserType.Instructor,

    @Column(name = "phone_number")
    @field:Pattern(regexp = "^\\+?\\d{1,16}\$")
    override var phoneNumber: String,

    @Column(name = "first_name")
    @field:Size(min = 2, max = 32)
    override var firstName: String,

    @Column(name = "last_name")
    @field:Size(min = 0, max = 32)
    override var lastName: String? = null,

    @Column(name = "userpic_path")
    override var userpicPath: String? = null,

    @field:Size(max = 256)
    @Column(name = "self_description")
    var selfDescription: String? = null,

    @field:Size(min = 1, max = 256)
    @Column(name = "start_location")
    var startLocation: String,

    @Column(name = "language")
    @Type(type = "list-array")
    var language: List<String>,

    @Column(name = "grade")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var grade: Double,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @OneToMany(
        mappedBy = "instructor",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL]
    )
    @Type(type = "list-array")
    var vehicles: List<Vehicle> = mutableListOf()

) : User(id, userType = UserType.Instructor) {
    init {
        vehicles.forEach { it.instructor = this }
    }
}
