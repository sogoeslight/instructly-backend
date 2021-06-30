package com.sogoeslight.instructly.user

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@CacheConfig(cacheNames = ["User"])
class UserService(private val repository: UserRepository) {

    fun getAll(pageable: Pageable): Page<UserDto> =
        repository
            .findAll(pageable)
            .map(User::toDto)

    @Cacheable(value = ["User"])
    fun getUserByUUID(id: UUID): UserDto =
        repository.findById(id)
            .orElseThrow { IllegalArgumentException("No user with id $id") }.toDto()

    @CacheEvict(value = ["User"], key = "#id")
    fun updateUser(id: UUID, userUpdate: UserDto): UserDto =
        repository.findByIdOrNull(id)?.apply {
            email = userUpdate.email
            phoneNumber = userUpdate.phoneNumber
            firstName = userUpdate.firstName
            lastName = userUpdate.lastName
            userpicPath = userUpdate.userpicPath
        }?.let {
            repository.save(it)
        }?.toDto()
            ?: throw IllegalArgumentException("No user with id $id")

    @CacheEvict(value = ["User", "Preferences", "Instructor", "Student", "Vehicle"])
    fun deleteUser(id: UUID): Unit =
        repository.deleteById(id)

}

private fun User.toDto() = UserDto(
    id,
    email,
    userType,
    phoneNumber,
    firstName,
    lastName,
    userpicPath,
    preferences
)