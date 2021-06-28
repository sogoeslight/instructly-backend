package com.sogoeslight.instructly.instructor

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import com.sogoeslight.instructly.preferences.CalendarDefaultDisplayedWeeks
import com.sogoeslight.instructly.preferences.Preferences
import com.sogoeslight.instructly.preferences.Theme
import com.sogoeslight.instructly.preferences.UILanguage
import com.sogoeslight.instructly.user.User
import com.sogoeslight.instructly.user.UserType
import com.sogoeslight.instructly.vehicle.*
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.*
import org.springframework.transaction.annotation.Transactional
import java.math.BigInteger
import java.util.*
import javax.persistence.EntityManager


const val ID = "fd7e1b90-31c2-4d68-b5c3-c96233574532"
const val VEHICLE_ID = "fd7e1b90-31c2-4d68-b5c3-c96233574533"
const val NEW_VEHICLE_ID = "fd7e1b90-31c2-4d68-b5c3-c96233574534"
const val URL = "/api/v1/instructors/"

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:populateWithTestData.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
internal class InstructorControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val em: EntityManager,
    val mapper: ObjectMapper
) {

    private val instructorDtoJson = """{
        "id":"""" + UUID.fromString(ID) + """",
        "email":"some@thing.com",
        "userType":"Instructor",
        "phoneNumber":"+37111111111",
        "firstName":"John",
        "lastName":"Doe",
        "userpicPath":null,
        "selfDescription":null,
        "startLocation":"Piemineklis",
        "language":["Russian"],
        "grade":4.2,
        "isActive":false,
        "vehicles":[{
            "id": """" + UUID.fromString(VEHICLE_ID) + """",
            "vehicleCategory": "C",
            "regPlate": "BA-4321",
            "manufacturer": "Mazda",
            "model": "3",
            "productionYear": "2010",
            "gearBox": "Manual",
            "wheelDrive": "Front"
        }]
        }
        """

    private val instructorDto = InstructorDto(
        id = UUID.fromString(ID),
        email = "some@thing.com",
        userType = UserType.Instructor,
        phoneNumber = "+37111111111",
        firstName = "John",
        lastName = "Doe",
        userpicPath = null,
        selfDescription = null,
        startLocation = "Piemineklis",
        language = listOf("Russian"),
        grade = 4.2,
        isActive = false,
        vehicles = listOf(
            VehicleDto(
                UUID.fromString(VEHICLE_ID),
                VehicleCategory.C,
                "BA-4321",
                "Mazda",
                "3",
                2010,
                Gearbox.Manual,
                WheelDrive.Front
            )
        )
    )

    private val createInstructorDto = CreateInstructorDto(
        id = UUID.randomUUID(),
        email = "some@thing.com",
        phoneNumber = "+37187654321",
        firstName = "John",
        lastName = "Doe",
        userpicPath = null,
        selfDescription = "I'm a very good instructor",
        startLocation = "Any",
        language = listOf("Russian"),
        vehicles = mutableListOf(
            CreateVehicleDto(
                UUID.fromString(NEW_VEHICLE_ID),
                VehicleCategory.B,
                "QQ-777",
                "BMW",
                "320",
                2006,
                Gearbox.Automatic,
                WheelDrive.Rear
            )
        )
    )

    private val createInstructorDtoJson = """{
        "id":"""" + UUID.fromString(ID) + """",
        "email":"some@thing.com",
        "userType":"Instructor",
        "phoneNumber":"+37187654321",
        "firstName":"John",
        "lastName":"Doe",
        "userpicPath":null,
        "selfDescription":"I'm a very good instructor",
        "startLocation":"Any",
        "language":["Russian"],
        "grade":4.2,
        "isActive":false,
        "vehicles":[{
            "id": """" + UUID.fromString(NEW_VEHICLE_ID) + """",
            "vehicleCategory": "B",
            "regPlate": "QQ-777",
            "manufacturer": "BMW",
            "model": "320",
            "productionYear": "2006",
            "gearBox": "Automatic",
            "wheelDrive": "Rear"
        }]
        }
    """

    @Nested
    inner class Update {
        @Test
        fun `response status is Ok`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = instructorDtoJson
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
                content = instructorDtoJson
            }.andExpect {
                jsonPath("$.firstName", equalTo(instructorDto.firstName))
                jsonPath("$.lastName", equalTo(instructorDto.lastName))
                jsonPath("$.email", equalTo(instructorDto.email))
                jsonPath("$.userType", equalTo(UserType.Instructor.toString()))
                jsonPath("$.phoneNumber", equalTo(instructorDto.phoneNumber))
                jsonPath("$.userpicPath", equalTo(instructorDto.userpicPath))
                jsonPath("$.selfDescription", equalTo(instructorDto.selfDescription))
                jsonPath("$.startLocation", equalTo(instructorDto.startLocation))
                jsonPath("$.language", equalTo(instructorDto.language))
                jsonPath("$.grade", equalTo(instructorDto.grade))
                jsonPath("$.isActive", equalTo(instructorDto.isActive))
                jsonPath(
                    "$.vehicles[0].vehicleCategory",
                    equalTo(instructorDto.vehicles[0].vehicleCategory.toString())
                )
                jsonPath("$.vehicles[0].regPlate", equalTo(instructorDto.vehicles[0].regPlate))
                jsonPath("$.vehicles[0].manufacturer", equalTo(instructorDto.vehicles[0].manufacturer))
                jsonPath("$.vehicles[0].model", equalTo(instructorDto.vehicles[0].model))
                jsonPath(
                    "$.vehicles[0].productionYear",
                    equalTo(instructorDto.vehicles[0].productionYear?.toInt())
                )
                jsonPath("$.vehicles[0].gearBox", equalTo(instructorDto.vehicles[0].gearBox.toString()))
                jsonPath("$.vehicles[0].wheelDrive", equalTo(instructorDto.vehicles[0].wheelDrive.toString()))
            }
        }

        @Test
        fun `record in DB correctly`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = instructorDtoJson
            }.andExpect {
                val instructorFromDb = em.find(Instructor::class.java, UUID.fromString(ID))

                jsonPath("$.firstName", equalTo(instructorFromDb.firstName))
                jsonPath("$.lastName", equalTo(instructorFromDb.lastName))
                jsonPath("$.email", equalTo(instructorFromDb.email))
                jsonPath("$.userType", equalTo(UserType.Instructor.toString()))
                jsonPath("$.phoneNumber", equalTo(instructorFromDb.phoneNumber))
                jsonPath("$.userpicPath", equalTo(instructorFromDb.userpicPath))
                jsonPath("$.selfDescription", equalTo(instructorFromDb.selfDescription))
                jsonPath("$.startLocation", equalTo(instructorFromDb.startLocation))
                jsonPath("$.language", equalTo(instructorFromDb.language))
                jsonPath("$.grade", equalTo(instructorFromDb.grade))
                jsonPath("$.isActive", equalTo(instructorFromDb.isActive))
            }
        }

        @Test
        fun `vehicle record in DB correctly`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = instructorDtoJson
            }.andExpect {
                val vehicleFromDb = em.find(Vehicle::class.java, UUID.fromString(VEHICLE_ID))

                jsonPath("$.vehicles[0].vehicleCategory", equalTo(vehicleFromDb.vehicleCategory.toString()))
                jsonPath("$.vehicles[0].regPlate", equalTo(vehicleFromDb.regPlate))
                jsonPath("$.vehicles[0].manufacturer", equalTo(vehicleFromDb.manufacturer))
                jsonPath("$.vehicles[0].model", equalTo(vehicleFromDb.model))
                jsonPath("$.vehicles[0].productionYear", equalTo(vehicleFromDb.productionYear?.toInt()))
                jsonPath("$.vehicles[0].gearBox", equalTo(vehicleFromDb.gearBox.toString()))
                jsonPath("$.vehicles[0].wheelDrive", equalTo(vehicleFromDb.wheelDrive.toString()))
            }
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `response status is Created`() {
            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createInstructorDto)
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
                content = mapper.writeValueAsString(createInstructorDto)
            }.andExpect {
                jsonPath("$.firstName", equalTo(createInstructorDto.firstName))
                jsonPath("$.lastName", equalTo(createInstructorDto.lastName))
                jsonPath("$.email", equalTo(createInstructorDto.email))
                jsonPath("$.userType", equalTo(UserType.Instructor.toString()))
                jsonPath("$.phoneNumber", equalTo(createInstructorDto.phoneNumber))
                jsonPath("$.userpicPath", equalTo(createInstructorDto.userpicPath))
                jsonPath("$.selfDescription", equalTo(createInstructorDto.selfDescription))
                jsonPath("$.startLocation", equalTo(createInstructorDto.startLocation))
                jsonPath("$.language", equalTo(createInstructorDto.language))
                jsonPath("$.grade", equalTo(createInstructorDto.grade))
                jsonPath("$.isActive", equalTo(createInstructorDto.isActive))
            }
        }

        @Test
        fun `new record in instructor table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM instructor").singleResult as BigInteger

            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createInstructorDto)
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM instructor").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
        }

        @Test
        fun `new record in instructor table with correct data`() {
            val response = mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createInstructorDto)
            }.andReturn().response

            val idFromResponse = UUID.fromString(JsonPath.read(response.contentAsString, "$.id"))
            val vehicleIdFromResponse = UUID.fromString(JsonPath.read(response.contentAsString, "$.vehicles[0].id"))

            val instructorFromDb = em.find(Instructor::class.java, idFromResponse)
            val preferencesFromDb = em.find(Preferences::class.java, idFromResponse)
            val vehiclesFromDb = em.find(Vehicle::class.java, vehicleIdFromResponse)

            assertThat(instructorFromDb.firstName).isEqualTo(createInstructorDto.firstName)
            assertThat(instructorFromDb.lastName).isEqualTo(createInstructorDto.lastName)
            assertThat(instructorFromDb.email).isEqualTo(createInstructorDto.email)
            assertThat(instructorFromDb.userType).isEqualTo(UserType.Instructor)
            assertThat(instructorFromDb.phoneNumber).isEqualTo(createInstructorDto.phoneNumber)
            assertThat(instructorFromDb.userpicPath).isEqualTo(createInstructorDto.userpicPath)
            assertThat(instructorFromDb.selfDescription).isEqualTo(createInstructorDto.selfDescription)
            assertThat(instructorFromDb.startLocation).isEqualTo(createInstructorDto.startLocation)
            assertThat(instructorFromDb.language).isEqualTo(createInstructorDto.language)
            assertThat(instructorFromDb.grade).isEqualTo(createInstructorDto.grade)
            assertThat(instructorFromDb.isActive).isEqualTo(createInstructorDto.isActive)
            assertThat(preferencesFromDb.theme).isEqualTo(Theme.Light)
            assertThat(preferencesFromDb.uiLanguage).isEqualTo(UILanguage.Latvian)
            assertThat(preferencesFromDb.calendarDefaultDisplayedWeeks).isEqualTo(CalendarDefaultDisplayedWeeks.Two)
            assertThat(preferencesFromDb.routeTrackingEnabled).isFalse
            assertThat(vehiclesFromDb.vehicleCategory).isEqualTo(createInstructorDto.vehicles[0].vehicleCategory)
            assertThat(vehiclesFromDb.regPlate).isEqualTo(createInstructorDto.vehicles[0].regPlate)
            assertThat(vehiclesFromDb.manufacturer).isEqualTo(createInstructorDto.vehicles[0].manufacturer)
            assertThat(vehiclesFromDb.model).isEqualTo(createInstructorDto.vehicles[0].model)
            assertThat(vehiclesFromDb.productionYear).isEqualTo(createInstructorDto.vehicles[0].productionYear)
            assertThat(vehiclesFromDb.gearBox).isEqualTo(createInstructorDto.vehicles[0].gearBox)
            assertThat(vehiclesFromDb.wheelDrive).isEqualTo(createInstructorDto.vehicles[0].wheelDrive)
        }

        @Test
        fun `new record in app_user table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = mapper.writeValueAsString(createInstructorDto)
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM app_user").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
        }

        @Test
        fun `new record in vehicle table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            mockMvc.post(URL) {
                contentType = MediaType.APPLICATION_JSON
                content = createInstructorDtoJson
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
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
                    val instructorFromDb = em.find(Instructor::class.java, UUID.fromString(ID))

                    jsonPath("$.firstName", equalTo(instructorFromDb.firstName))
                    jsonPath("$.lastName", equalTo(instructorFromDb.lastName))
                    jsonPath("$.email", equalTo(instructorFromDb.email))
                    jsonPath("$.userType", equalTo(UserType.Instructor.toString()))
                    jsonPath("$.phoneNumber", equalTo(instructorFromDb.phoneNumber))
                    jsonPath("$.userpicPath", equalTo(instructorFromDb.userpicPath))
                    jsonPath("$.selfDescription", equalTo(instructorFromDb.selfDescription))
                    jsonPath("$.startLocation", equalTo(instructorFromDb.startLocation))
                    jsonPath("$.language", equalTo(instructorFromDb.language))
                    jsonPath("$.grade", equalTo(instructorFromDb.grade))
                    jsonPath("$.isActive", equalTo(instructorFromDb.isActive))
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
        fun `amount of records in instructor table decreased`() {
            val countBeforeInsert =
                em.createNativeQuery("SELECT COUNT(*) FROM instructor").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert =
                em.createNativeQuery("SELECT COUNT(*) FROM instructor").singleResult as BigInteger

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
        fun `amount of records in vehicle table decreased`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

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
        fun `removed record in instructor table returns null`() {
            mockMvc.delete(URL + ID)

            val instructor = em.find(Instructor::class.java, UUID.fromString(ID))

            assertThat(instructor).isNull()
        }

        @Test
        fun `removed record in app_user table returns null`() {
            mockMvc.delete(URL + ID)

            val instructor = em.find(User::class.java, UUID.fromString(ID))

            assertThat(instructor).isNull()
        }

        @Test
        fun `removed record in vehicle table returns null`() {
            mockMvc.delete(URL + ID)

            val vehicle = em.find(Vehicle::class.java, UUID.fromString(ID))

            assertThat(vehicle).isNull()
        }

        @Test
        fun `removed record in preferences table returns null`() {
            mockMvc.delete(URL + ID)

            val preferences = em.find(Preferences::class.java, UUID.fromString(ID))

            assertThat(preferences).isNull()
        }
    }
}