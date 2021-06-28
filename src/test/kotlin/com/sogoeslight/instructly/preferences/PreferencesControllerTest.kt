package com.sogoeslight.instructly.preferences

import org.hamcrest.Matchers
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager

const val ID = "fd7e1b90-31c2-4d68-b5c3-c96233574531"
const val URL = "/api/v1/preferences/"

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:populateWithTestData.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
internal class PreferencesControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val em: EntityManager
) {

    private val preferencesDtoJson = """{
        "id":"""" + UUID.fromString(ID) + """",
        "theme":"Dark",
        "uiLanguage":"Russian",
        "calendarDefaultDisplayedWeeks":"One",
        "routeTrackingEnabled":"true"
        }
        """

    private val updatedPreferencesDto = PreferencesDto(
        id = UUID.fromString(ID),
        theme = Theme.Dark,
        uiLanguage = UILanguage.Russian,
        calendarDefaultDisplayedWeeks = CalendarDefaultDisplayedWeeks.One,
        routeTrackingEnabled = true
    )

    @Nested
    inner class Update {
        @Test
        fun `response status is Ok`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = preferencesDtoJson
            }.andExpect {
                status {
                    isOk()
                }
            }
        }

        @Test
        fun `response body is correct`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = preferencesDtoJson
            }.andExpect {
                jsonPath("$.theme", Matchers.equalTo(updatedPreferencesDto.theme.toString()))
                jsonPath("$.uiLanguage", Matchers.equalTo(updatedPreferencesDto.uiLanguage.toString()))
                jsonPath(
                    "$.calendarDefaultDisplayedWeeks",
                    Matchers.equalTo(updatedPreferencesDto.calendarDefaultDisplayedWeeks.toString())
                )
                jsonPath("$.routeTrackingEnabled", Matchers.equalTo(updatedPreferencesDto.routeTrackingEnabled))
            }
        }
    }

    @Nested
    inner class Get {

        @Test
        fun `response status is Ok`() {
            mockMvc.get(URL + ID)
                .andExpect {
                    status {
                        isOk()
                    }
                }
        }

        @Test
        fun `response body is correct`() {
            mockMvc.get(URL + ID)
                .andExpect {
                    val preferencesFromDb =
                        em.find(Preferences::class.java, UUID.fromString(ID))

                    jsonPath("$.theme", Matchers.equalTo(preferencesFromDb.theme.toString()))
                    jsonPath("$.uiLanguage", Matchers.equalTo(preferencesFromDb.uiLanguage.toString()))
                    jsonPath(
                        "$.calendarDefaultDisplayedWeeks",
                        Matchers.equalTo(preferencesFromDb.calendarDefaultDisplayedWeeks.toString())
                    )
                    jsonPath("$.routeTrackingEnabled", Matchers.equalTo(preferencesFromDb.routeTrackingEnabled))
                }
        }
    }
}