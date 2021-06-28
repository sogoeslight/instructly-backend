package com.sogoeslight.instructly.preferences

import com.fasterxml.jackson.annotation.JsonIgnore
import com.sogoeslight.instructly.audit.AuditModel
import com.sogoeslight.instructly.user.User
import org.hibernate.annotations.Type
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Repository
interface PreferencesRepository : JpaRepository<Preferences, UUID>

@Entity
@Table(name = "preferences")
class Preferences(
    @Id
    @Column(name = "id")
    @Type(type = "pg-uuid")
    val id: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "theme")
    var theme: Theme = Theme.Light,

    @Enumerated(EnumType.STRING)
    @Column(name = "ui_language")
    var uiLanguage: UILanguage = UILanguage.Latvian,

    @Enumerated(EnumType.STRING)
    @Column(name = "calendar_def_display_weeks")
    var calendarDefaultDisplayedWeeks: CalendarDefaultDisplayedWeeks = CalendarDefaultDisplayedWeeks.Two,

    @Column(name = "route_tracking_enabled")
    var routeTrackingEnabled: Boolean = false,

    @JsonIgnore
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @PrimaryKeyJoinColumn(name = "id")
    val user: User? = null
) : AuditModel()
