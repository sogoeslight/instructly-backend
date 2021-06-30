package com.sogoeslight.instructly.preferences

import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional
@CacheConfig(cacheNames = ["Preferences"])
class PreferencesService(private val repository: PreferencesRepository) {

    @Cacheable(value = ["Preferences"])
    fun getPreferencesByUUID(id: UUID): PreferencesDto =
        repository.findById(id)
            .orElseThrow { IllegalArgumentException("No preferences with id $id") }.toDto()

    @CachePut(value = ["Preferences", "User"])
    fun createDefaultPreferences(id: UUID): PreferencesDto =
        repository.save(Preferences(id)).toDto()

    @CacheEvict(value = ["Preferences", "User"], key = "#id")
    fun updatePreferences(id: UUID, preferencesUpdate: PreferencesDto): PreferencesDto =
        repository.findByIdOrNull(id)?.apply {
            theme = preferencesUpdate.theme
            uiLanguage = preferencesUpdate.uiLanguage
            calendarDefaultDisplayedWeeks = preferencesUpdate.calendarDefaultDisplayedWeeks
            routeTrackingEnabled = preferencesUpdate.routeTrackingEnabled
        }?.let {
            repository.save(it)
        }?.toDto()
            ?: throw IllegalArgumentException("No preferences with id $id")

}

private fun Preferences.toDto() = PreferencesDto(
    id,
    theme,
    uiLanguage,
    calendarDefaultDisplayedWeeks,
    routeTrackingEnabled
)