package com.sogoeslight.instructly.user

import com.sogoeslight.instructly.preferences.Preferences
import java.util.*

data class UserDto(
    val id: UUID,
    val email: String,
    val userType: UserType,
    val phoneNumber: String,
    val firstName: String,
    val lastName: String?,
    val userpicPath: String?,
    val preferences: Preferences?
)