package com.sogoeslight.instructly.vehicle

import com.sogoeslight.instructly.instructor.InstructorDto
import com.sogoeslight.instructly.instructor.InstructorService
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
@CacheConfig(cacheNames = ["Vehicle"])
class VehicleService(
    private val repository: VehicleRepository,
    private val instructorService: InstructorService,
) {

    fun getAll(pageable: Pageable): Page<VehicleDto> = repository.findAll(pageable).map { it.toDto() }

    @Cacheable(value = ["Vehicle"])
    fun getVehicleByUUID(id: UUID): VehicleDto =
        repository.findById(id)
            .orElseThrow { IllegalArgumentException("No Vehicle with id $id") }.toDto()

    @CacheEvict(
        value = ["Instructor", "Vehicle"],
        key = "#vehicle.id"
    )
    fun createVehicleAndAssignToInstructor(instructorID: UUID, vehicle: CreateVehicleDto): InstructorDto {
        val instructor = instructorService.getInstructorByUUID(instructorID)
        instructor.vehicles += vehicle.toDto()
        return instructorService.updateInstructor(instructorID, instructor)
    }

    @CacheEvict(
        value = ["Instructor", "Vehicle"],
        key = "#id"
    )
    fun updateVehicle(id: UUID, VehicleUpdate: VehicleDto): VehicleDto =
        repository.findByIdOrNull(id)?.apply {
            vehicleCategory = VehicleUpdate.vehicleCategory
            regPlate = VehicleUpdate.regPlate
            manufacturer = VehicleUpdate.manufacturer
            model = VehicleUpdate.model
            productionYear = VehicleUpdate.productionYear
            gearBox = VehicleUpdate.gearBox
            wheelDrive = VehicleUpdate.wheelDrive
        }?.let {
            repository.save(it)
        }?.toDto()
            ?: throw IllegalArgumentException("No vehicle with id $id")

    @CacheEvict(value = ["Instructor", "Vehicle"])
    fun deleteVehicle(id: UUID): Unit =
        repository.deleteById(id)

}

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

fun CreateVehicleDto.toDto() = VehicleDto(
    id,
    vehicleCategory,
    regPlate,
    manufacturer,
    model,
    productionYear,
    gearBox,
    wheelDrive
)
