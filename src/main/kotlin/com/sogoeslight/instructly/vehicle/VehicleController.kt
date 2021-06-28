package com.sogoeslight.instructly.vehicle

import com.sogoeslight.instructly.instructor.InstructorDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/vehicles")
class VehicleController(private val service: VehicleService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<VehicleDto> =
        service.getAll(pageable)

    @GetMapping("/{id}")
    fun getVehicleById(@PathVariable id: UUID): VehicleDto =
        service.getVehicleByUUID(id)

    @PostMapping("/{instructorID}")
    @ResponseStatus(HttpStatus.CREATED)
    fun createVehicle(
        @PathVariable instructorID: UUID,
        @Valid @RequestBody vehicle: CreateVehicleDto
    ): InstructorDto =
        service.createVehicleAndAssignToInstructor(instructorID, vehicle)

    @PutMapping("/{id}")
    fun updateVehicleById(
        @PathVariable id: UUID,
        @Valid @RequestBody updateRequest: VehicleDto
    ): VehicleDto =
        service.updateVehicle(id, updateRequest)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteVehicle(@PathVariable id: UUID): Unit =
        service.deleteVehicle(id)
}