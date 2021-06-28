package com.sogoeslight.instructly.instructor

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/instructors")
class InstructorController(private val service: InstructorService) {

    @GetMapping
    fun getAll(pageable: Pageable): Page<InstructorDto> =
        service.getAll(pageable)

    @GetMapping("/{id}")
    fun getInstructorById(@PathVariable id: UUID): InstructorDto =
        service.getInstructorByUUID(id)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createInstructor(
        @Valid @RequestBody instructor: CreateInstructorDto
    ): InstructorDto =
        service.createInstructor(instructor)

    @PutMapping("/{id}")
    fun updateInstructorById(
        @PathVariable id: UUID,
        @Valid @RequestBody updateRequest: InstructorDto
    ): InstructorDto =
        service.updateInstructor(id, updateRequest)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteInstructor(@PathVariable id: UUID): Unit =
        service.deleteInstructor(id)
}