package com.sogoeslight.instructly.preferences

import java.util.*

class PreferencesDto(
    val id: UUID,
    val theme: Theme,
    val uiLanguage: UILanguage,
    val calendarDefaultDisplayedWeeks: CalendarDefaultDisplayedWeeks,
    val routeTrackingEnabled: Boolean
)