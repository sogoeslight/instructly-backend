package com.sogoeslight.instructly.student

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


@RestController
@RequestMapping(
    value = ["/api/v1/students"],
    produces = ["application/vnd.instructly.api+json"]
)
class StudentController(private val service: StudentService) {

    // https://opensource.zalando.com/restful-api-guidelines/
    @Operation(summary = "Get all students")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found students", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Did not find any students", content = [Content()])
        ]
    )
    @GetMapping
    fun getAll(pageable: Pageable): Page<StudentDto> =
        service.getAll(pageable)

    @Operation(summary = "Get student by ID")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Found student", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Did not find student with such ID", content = [Content()])
        ]
    )
    @GetMapping("/{id}")
    fun getStudentById(@PathVariable id: UUID): StudentDto =
        service.getStudentByUUID(id)

    @Operation(summary = "Create student")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Updated student", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(
                responseCode = "201", description = "Created student", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(
                responseCode = "202", description = "Request was accepted, but has not been finished yet", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])
        ]
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createStudent(
        @Valid @RequestBody student: CreateStudentDto
    ): StudentDto =
        service.createStudent(student)

    @Operation(summary = "Update student")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Updated student", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(
                responseCode = "201", description = "Created student", content = [
                    (Content(
                        mediaType = "application/json", array = (
                                ArraySchema(schema = Schema(implementation = StudentDto::class)))
                    ))]
            ),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()])
        ]
    )
    @PutMapping("/{id}")
    fun updateStudentById(
        @PathVariable id: UUID,
        @Valid @RequestBody updateRequest: StudentDto
    ): StudentDto =
        service.updateStudent(id, updateRequest)

    @Operation(summary = "Delete student")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "204", description = "Deleted student"),
            ApiResponse(responseCode = "400", description = "Bad request", content = [Content()]),
            ApiResponse(responseCode = "404", description = "Not found", content = [Content()])
        ]
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStudent(@PathVariable id: UUID): Unit =
        service.deleteStudent(id)
}