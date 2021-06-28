package com.sogoeslight.instructly.student

import com.fasterxml.jackson.annotation.JsonFormat
import com.sogoeslight.instructly.user.UserType
import java.math.BigDecimal
import java.util.*

class StudentDto(
    val id: UUID,
    val email: String,
    val userType: UserType,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String?,
    val userpicPath: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val currentBalance: BigDecimal
)

class CreateStudentDto(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String?,
    val userpicPath: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    val currentBalance: BigDecimal = BigDecimal(0)
)