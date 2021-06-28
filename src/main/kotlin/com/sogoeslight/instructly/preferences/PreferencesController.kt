package com.sogoeslight.instructly.preferences

import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/api/v1/preferences")
class PreferencesController(private val service: PreferencesService) {

    @GetMapping("/{id}")
    fun getPreferencesById(@PathVariable id: UUID): PreferencesDto =
        service.getPreferencesByUUID(id)

    @PutMapping("/{id}")
    fun updatePreferencesById(
        @PathVariable id: UUID,
        @RequestBody updateRequest: PreferencesDto
    ): PreferencesDto =
        service.updatePreferences(id, updateRequest)
}