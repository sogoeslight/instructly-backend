package com.sogoeslight.instructly.user

import com.sogoeslight.instructly.annotations.IntegrationTest
import com.sogoeslight.instructly.preferences.Preferences
import com.sogoeslight.instructly.student.Student
import assertk.assertThat
import assertk.assertions.isNull
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.put
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityManager

const val ID = "fd7e1b90-31c2-4d68-b5c3-c96233574531"
const val URL = "/api/v1/users/"

@IntegrationTest
internal class UserControllerTest constructor(
    val mockMvc: MockMvc,
    val em: EntityManager
) {

    private val userDtoJson = """{
        "id":"""" + UUID.fromString(ID) + """",
        "email":"some@thing.com",
        "userType":"Student",
        "phoneNumber":"+37111111111",
        "firstName":"John",
        "lastName":"Doe",
        "userpicPath":null,
        "preferences":null
        }    
    """

    private val updatedUserDto = UserDto(
        id = UUID.fromString(com.sogoeslight.instructly.student.ID),
        email = "some@thing.com",
        userType = UserType.Student,
        phoneNumber = "+37111111111",
        firstName = "John",
        lastName = "Doe",
        userpicPath = null,
        preferences = null
    )

    @Nested
    inner class Update {
        @Test
        fun `response status is Ok`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = userDtoJson
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
                content = userDtoJson
            }.andExpect {
                jsonPath("$.firstName", Matchers.equalTo(updatedUserDto.firstName))
                jsonPath("$.lastName", Matchers.equalTo(updatedUserDto.lastName))
                jsonPath("$.email", Matchers.equalTo(updatedUserDto.email))
                jsonPath("$.userType", Matchers.equalTo(UserType.Student.toString()))
                jsonPath("$.phoneNumber", Matchers.equalTo(updatedUserDto.phoneNumber))
                jsonPath("$.userpicPath", Matchers.equalTo(updatedUserDto.userpicPath))
            }
        }
    }

    @Nested
    inner class Get {
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
                    val usersFromDb =
                        em.find(Student::class.java, UUID.fromString(ID))

                    jsonPath("$.firstName", Matchers.equalTo(usersFromDb.firstName))
                    jsonPath("$.lastName", Matchers.equalTo(usersFromDb.lastName))
                    jsonPath("$.email", Matchers.equalTo(usersFromDb.email))
                    jsonPath("$.userType", Matchers.equalTo(UserType.Student.toString()))
                    jsonPath("$.phoneNumber", Matchers.equalTo(usersFromDb.phoneNumber))
                    jsonPath("$.userpicPath", Matchers.equalTo(usersFromDb.userpicPath))
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

            val user = em.find(User::class.java, UUID.fromString(ID))

            assertThat(user).isNull()
        }

        @Test
        fun `removed record in preferences table returns null`() {
            mockMvc.delete(URL + ID)

            val preferences = em.find(Preferences::class.java, UUID.fromString(ID))

            assertThat(preferences).isNull()
        }
    }
}