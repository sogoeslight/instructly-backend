package com.sogoeslight.instructly.instructor

import com.sogoeslight.instructly.user.UserType
import com.sogoeslight.instructly.vehicle.CreateVehicleDto
import com.sogoeslight.instructly.vehicle.VehicleDto
import java.util.*

class InstructorDto(
    val id: UUID,
    val email: String,
    val userType: UserType,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String?,
    val userpicPath: String?,
    val selfDescription: String?,
    val startLocation: String,
    val language: List<String>,
    val grade: Double,
    val isActive: Boolean,
    var vehicles: List<VehicleDto>
)

class CreateInstructorDto(
    val id: UUID = UUID.randomUUID(),
    val email: String,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String?,
    val userpicPath: String?,
    val selfDescription: String?,
    val startLocation: String,
    val language: List<String>,
    val grade: Double = 0.0,
    val isActive: Boolean = true,
    val vehicles: List<CreateVehicleDto>
)