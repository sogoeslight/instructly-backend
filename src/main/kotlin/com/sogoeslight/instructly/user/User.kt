package com.sogoeslight.instructly.user

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.sogoeslight.instructly.audit.AuditModel
import com.sogoeslight.instructly.instructor.Instructor
import com.sogoeslight.instructly.preferences.Preferences
import com.sogoeslight.instructly.student.Student
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
interface UserRepository : JpaRepository<User, UUID>

@Entity
@Table(name = "app_user")
@Inheritance(strategy = InheritanceType.JOINED)
@JsonSubTypes(
    JsonSubTypes.Type(value = Student::class, name = "student"),
    JsonSubTypes.Type(value = Instructor::class, name = "instructor")
)
abstract class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @Type(type = "pg-uuid")
    open val id: UUID,

    @Column(name = "email")
    open var email: String = "",

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    open var userType: UserType,

    @Column(name = "phone_number")
    open var phoneNumber: String = "",

    @Column(name = "first_name")
    open var firstName: String = "",

    @Column(name = "last_name")
    open var lastName: String? = null,

    @Column(name = "userpic_path")
    open var userpicPath: String? = null,

    @OneToOne(mappedBy = "user", orphanRemoval = true, fetch = FetchType.LAZY)
    open val preferences: Preferences? = null
) : AuditModel()
