package com.sogoeslight.instructly.student

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.sogoeslight.instructly.annotations.IntegrationTest
import com.sogoeslight.instructly.preferences.CalendarDefaultDisplayedWeeks
import com.sogoeslight.instructly.preferences.Preferences
import com.sogoeslight.instructly.preferences.Theme
import com.sogoeslight.instructly.preferences.UILanguage
import com.sogoeslight.instructly.user.User
import com.sogoeslight.instructly.user.UserType
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityManager


const val ID = "fd7e1b90-31c2-4d68-b5c3-c96233574531"
const val URL = "/api/v1/students/"

@IntegrationTest
internal class StudentControllerTest constructor(
    val mockMvc: MockMvc,
    val em: EntityManager,
    val mapper: ObjectMapper
) {

    private val studentDtoJson = """{
        "id":"""" + UUID.fromString(ID) + """",
        "email":"some@thing.com",
        "userType":"Student",
        "phoneNumber":"+37111111111",
        "firstName":"John",
        "lastName":"Doe",
        "userpicPath":null,
        "currentBalance":900.25
        }
    """

    private val studentDto = StudentDto(
        id = UUID.fromString(ID),
        email = "some@thing.com",
        userType = UserType.Student,
        phoneNumber = "+37111111111",
        firstName = "John",
        lastName = "Doe",
        userpicPath = null,
        currentBalance = BigDecimal(900.25)
    )

    private val createStudentDto = CreateStudentDto(
        email = "some@thing.com",
        phoneNumber = "+37187654321",
        firstName = "John",
        lastName = "Doe",
        userpicPath = null,
        currentBalance = BigDecimal(5.0)
    )

    @Nested
    inner class Update {
        @Test
        fun `response status is Ok`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = studentDtoJson
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
                content = studentDtoJson
            }.andExpect {
                jsonPath("$.firstName", equalTo(studentDto.firstName))
                jsonPath("$.lastName", equalTo(studentDto.lastName))
                jsonPath("$.email", equalTo(studentDto.email))
                jsonPath("$.userType", equalTo(UserType.Student.toString()))
                jsonPath("$.phoneNumber", equalTo(studentDto.phoneNumber))
                jsonPath("$.userpicPath", equalTo(studentDto.userpicPath))
                jsonPath("$.currentBalance", equalTo(studentDto.currentBalance.toString()))
            }
        }

        @Test
        fun `record in DB correctly`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = studentDtoJson
            }.andExpect {
                val studentFromDb = em.find(Student::class.java, UUID.fromString(ID))

                jsonPath("$.firstName", equalTo(studentFromDb.firstName))
                jsonPath("$.lastName", equalTo(studentFromDb.lastName))
                jsonPath("$.email", equalTo(studentFromDb.email))
                jsonPath("$.userType", equalTo(UserType.Student.toString()))
                jsonPath("$.phoneNumber", equalTo(studentFromDb.phoneNumber))
                jsonPath("$.userpicPath", equalTo(studentFromDb.userpicPath))
                jsonPath("$.currentBalance", equalTo(studentFromDb.currentBalance.toString()))
            }
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `response status is Created`() {
            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createStudentDto)
            }.andExpect {
                status {
                    isCreated()
                }
            }
        }

        @Test
        fun `response body is correct`() {
            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createStudentDto)
            }.andExpect {
                jsonPath("$.firstName", equalTo(createStudentDto.firstName))
                jsonPath("$.lastName", equalTo(createStudentDto.lastName))
                jsonPath("$.email", equalTo(createStudentDto.email))
                jsonPath("$.phoneNumber", equalTo(createStudentDto.phoneNumber))
                jsonPath("$.userpicPath", equalTo(createStudentDto.userpicPath))
                jsonPath("$.currentBalance", equalTo(createStudentDto.currentBalance.toString()))
            }
        }

        @Test
        fun `new record in student table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM student").singleResult as BigInteger

            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createStudentDto)
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM student").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
        }

        @Test
        fun `new record in student table with correct data`() {
            val response = mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createStudentDto)
            }.andReturn().response

            val idFromResponse = UUID.fromString(JsonPath.read(response.contentAsString, "$.id"))

            val studentFromDb = em.find(Student::class.java, idFromResponse)
            val preferencesFromDb = em.find(Preferences::class.java, idFromResponse)

            assertThat(studentFromDb.firstName).isEqualTo(createStudentDto.firstName)
            assertThat(studentFromDb.lastName).isEqualTo(createStudentDto.lastName)
            assertThat(studentFromDb.email).isEqualTo(createStudentDto.email)
            assertThat(studentFromDb.userType).isEqualTo(UserType.Student)
            assertThat(studentFromDb.phoneNumber).isEqualTo(createStudentDto.phoneNumber)
            assertThat(studentFromDb.userpicPath).isEqualTo(createStudentDto.userpicPath)
            assertThat(studentFromDb.currentBalance).isEqualTo(createStudentDto.currentBalance)
            assertThat(preferencesFromDb.theme).isEqualTo(Theme.Light)
            assertThat(preferencesFromDb.uiLanguage).isEqualTo(UILanguage.Latvian)
            assertThat(preferencesFromDb.calendarDefaultDisplayedWeeks).isEqualTo(CalendarDefaultDisplayedWeeks.Two)
            assertThat(preferencesFromDb.routeTrackingEnabled).isFalse()
        }

        @Test
        fun `new record in app_user table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createStudentDto)
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
        }
    }

    @Nested
    inner class Get {
        @Test
        fun `content type is correct`() {
            mockMvc.get(URL) {
                accept = MediaType.valueOf("application/vnd.instructly.api+json")
            }.andExpect {
                content {
                    contentType(MediaType.valueOf("application/vnd.instructly.api+json"))
                }
            }
        }

        @Test
        fun `all response status is Ok`() {
            mockMvc.get(URL)
                .andExpect {
                    status {
                        isOk()
                    }
                }
        }

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
                    val studentFromDb = em.find(Student::class.java, UUID.fromString(ID))

                    jsonPath("$.firstName", equalTo(studentFromDb.firstName))
                    jsonPath("$.lastName", equalTo(studentFromDb.lastName))
                    jsonPath("$.email", equalTo(studentFromDb.email))
                    jsonPath("$.userType", equalTo(UserType.Student.toString()))
                    jsonPath("$.phoneNumber", equalTo(studentFromDb.phoneNumber))
                    jsonPath("$.userpicPath", equalTo(studentFromDb.userpicPath))
                    jsonPath("$.currentBalance", equalTo(studentFromDb.currentBalance.toString()))
                }
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `response status is No Content`() {
            mockMvc.delete(URL + ID)
                .andExpect {
                    status {
                        isNoContent()
                    }
                }
        }

        @Test
        fun `amount of records in student table decreased`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM student").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM student").singleResult as BigInteger

            assertEquals(countBeforeInsert.dec(), countAfterInsert)
        }

        @Test
        fun `amount of records in app_user table decreased`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            assertEquals(countBeforeInsert.dec(), countAfterInsert)
        }

        @Test
        fun `amount of records in preferences table decreased`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM preferences").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM preferences").singleResult as BigInteger

            assertEquals(countBeforeInsert.dec(), countAfterInsert)
        }

        @Test
        fun `removed record in student table returns null`() {
            mockMvc.delete(URL + ID)

            val student = em.find(Student::class.java, UUID.fromString(ID))

            assertThat(student).isNull()
        }

        @Test
        fun `removed record in app_user table returns null`() {
            mockMvc.delete(URL + ID)

            val student = em.find(User::class.java, UUID.fromString(ID))

            assertThat(student).isNull()
        }

        @Test
        fun `removed record in preferences table returns null`() {
            mockMvc.delete(URL + ID)

            val preferences = em.find(Preferences::class.java, UUID.fromString(ID))

            assertThat(preferences).isNull()
        }
    }
}