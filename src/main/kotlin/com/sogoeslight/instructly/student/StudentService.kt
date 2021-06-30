package com.sogoeslight.instructly.student

import com.sogoeslight.instructly.preferences.PreferencesService
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@CacheConfig(cacheNames = ["Student"])
class StudentService(
    private val repository: StudentRepository,
    private val preferencesService: PreferencesService
) {

    fun getAll(pageable: Pageable): Page<StudentDto> =
        repository
            .findAll(pageable)
            .map(Student::toDto)

    @Cacheable(value = ["Student"])
    fun getStudentByUUID(id: UUID): StudentDto =
        repository.findById(id)
            .orElseThrow { IllegalArgumentException("No student with id $id") }.toDto()

    @CachePut(value = ["Student", "User"])
    fun createStudent(student: CreateStudentDto): StudentDto {
        val newStudent = repository.save(
            Student(
                email = student.email,
                phoneNumber = student.phoneNumber,
                firstName = student.firstName,
                lastName = student.lastName,
                userpicPath = student.userpicPath,
                currentBalance = student.currentBalance
            )
        ).toDto()

        preferencesService.createDefaultPreferences(newStudent.id)

        return newStudent
    }

    @CacheEvict(
        value = ["User", "Student"],
        key = "#id"
    )
    fun updateStudent(id: UUID, studentUpdate: StudentDto): StudentDto =
        repository.findByIdOrNull(id)?.apply {
            email = studentUpdate.email
            phoneNumber = studentUpdate.phoneNumber
            firstName = studentUpdate.firstName
            lastName = studentUpdate.lastName
            userpicPath = studentUpdate.userpicPath
            currentBalance = studentUpdate.currentBalance
        }?.let {
            repository.save(it)
        }?.toDto()
            ?: throw IllegalArgumentException("No student with id $id")

    //TODO REMOVE
    @CacheEvict(value = ["User", "Preferences", "Student"])
    fun deleteStudent(id: UUID): Unit =
        repository.deleteById(id)

}

private fun Student.toDto() = StudentDto(
    id,
    email,
    userType,
    phoneNumber,
    firstName,
    lastName,
    userpicPath,
    currentBalance
)