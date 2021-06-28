package com.sogoeslight.instructly.instructor

import com.sogoeslight.instructly.preferences.PreferencesService
import com.sogoeslight.instructly.user.UserType
import com.sogoeslight.instructly.vehicle.CreateVehicleDto
import com.sogoeslight.instructly.vehicle.Vehicle
import com.sogoeslight.instructly.vehicle.VehicleDto
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
@CacheConfig(cacheNames = ["Instructor"])
class InstructorService(
    private val repository: InstructorRepository,
    private val preferencesService: PreferencesService,
) {
    fun getAll(pageable: Pageable): Page<InstructorDto> = repository.findAll(pageable).map { it.toDto() }

    @Cacheable(value = ["Instructor"])
    fun getInstructorByUUID(id: UUID): InstructorDto =
        repository.findById(id)
            .orElseThrow { IllegalArgumentException("No instructor with id $id") }.toDto()

    @CachePut(value = ["Instructor", "User", "Vehicle"])
    fun createInstructor(instructor: CreateInstructorDto): InstructorDto =
        repository.save(
            Instructor(
                email = instructor.email,
                phoneNumber = instructor.phoneNumber,
                firstName = instructor.firstName,
                lastName = instructor.lastName,
                userpicPath = instructor.userpicPath,
                selfDescription = instructor.selfDescription,
                startLocation = instructor.startLocation,
                language = instructor.language,
                grade = instructor.grade,
                isActive = instructor.isActive,
                vehicles = instructor.vehicles.map { it.toEntity(instructor.toEntity()) }
            )
        ).toDto().also {
            preferencesService.createDefaultPreferences(it.id)
        }


    @CacheEvict(
        value = ["User", "Instructor", "Vehicle"],
        key = "#id"
    )
    fun updateInstructor(id: UUID, instructorUpdate: InstructorDto): InstructorDto =
        repository.findByIdOrNull(id)?.apply {
            email = instructorUpdate.email
            phoneNumber = instructorUpdate.phoneNumber
            firstName = instructorUpdate.firstName
            lastName = instructorUpdate.lastName
            userpicPath = instructorUpdate.userpicPath
            selfDescription = instructorUpdate.selfDescription
            startLocation = instructorUpdate.startLocation
            language = instructorUpdate.language
            grade = instructorUpdate.grade
            isActive = instructorUpdate.isActive
            vehicles = instructorUpdate.vehicles.map { it.toEntity(instructorUpdate.toEntity()) }
        }?.let {
            repository.save(it)
        }?.toDto()
            ?: throw IllegalArgumentException("No instructor with id $id")

    //TODO REMOVE
    @CacheEvict(value = ["Instructor"])
    fun deleteInstructor(id: UUID): Unit =
        repository.deleteById(id)

}

fun VehicleDto.toEntity(instructor: Instructor) = Vehicle(
    id,
    instructor,
    vehicleCategory,
    regPlate,
    manufacturer,
    model,
    productionYear,
    gearBox,
    wheelDrive
)

fun CreateVehicleDto.toEntity(instructor: Instructor) = Vehicle(
    id,
    instructor,
    vehicleCategory,
    regPlate,
    manufacturer,
    model,
    productionYear,
    gearBox,
    wheelDrive
)

fun InstructorDto.toEntity() = Instructor(
    id,
    email,
    userType,
    phoneNumber,
    firstName,
    lastName,
    userpicPath,
    selfDescription,
    startLocation,
    language,
    grade,
    isActive,
    emptyList()
)

fun CreateInstructorDto.toEntity() = Instructor(
    id,
    email,
    UserType.Instructor,
    phoneNumber,
    firstName,
    lastName,
    userpicPath,
    selfDescription,
    startLocation,
    language,
    grade,
    isActive,
    emptyList()
)

fun Instructor.toDto() = InstructorDto(
    id,
    email,
    userType,
    phoneNumber,
    firstName,
    lastName,
    userpicPath,
    selfDescription,
    startLocation,
    language,
    grade,
    isActive,
    vehicles.map { it.toDto() }
)

fun Vehicle.toDto() = VehicleDto(
    id,
    vehicleCategory,
    regPlate,
    manufacturer,
    model,
    productionYear,
    gearBox,
    wheelDrive
)