package com.sogoeslight.instructly.user

//import org.springframework.security.access.annotation.Secured
//import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid


//@Secured
@RestController
@RequestMapping("/api/v1/users")
class UserController(private val service: UserService) {

    //@Secured
    @GetMapping
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getAll(pageable: Pageable): Page<UserDto> =
        service.getAll(pageable)

    @GetMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    fun getUserById(@PathVariable id: UUID): UserDto =
        service.getUserByUUID(id)

    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    fun updateUserById(
        @PathVariable id: UUID,
        @Valid @RequestBody updateRequest: UserDto
    ): UserDto =
        service.updateUser(id, updateRequest)

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: UUID): Unit =
        service.deleteUser(id)
}