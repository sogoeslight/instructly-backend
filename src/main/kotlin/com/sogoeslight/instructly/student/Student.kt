package com.sogoeslight.instructly.student

import com.fasterxml.jackson.annotation.JsonFormat
import com.sogoeslight.instructly.user.User
import com.sogoeslight.instructly.user.UserType
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Repository
interface StudentRepository : JpaRepository<Student, UUID>

@Entity
@Table(name = "student")
class Student(
    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    override val id: UUID = UUID.randomUUID(),

    @Column(name = "email")
    @field:Pattern(regexp = "(?:[a-z0-9!#\$%&''*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&''*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    override var email: String,

    @Column(name = "user_type")
    @Enumerated(EnumType.STRING)
    override var userType: UserType = UserType.Student,

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

    @Column(name = "current_balance")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    var currentBalance: BigDecimal

) : User(id, userType = UserType.Student)
