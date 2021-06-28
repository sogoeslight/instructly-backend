package com.sogoeslight.instructly.vehicle

import com.jayway.jsonpath.JsonPath
import com.sogoeslight.instructly.instructor.Instructor
import com.sogoeslight.instructly.instructor.InstructorDto
import com.sogoeslight.instructly.user.UserType
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers
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


const val ID = "fd7e1b90-31c2-4d68-b5c3-c96233574533"
const val INSTRUCTOR_WO_VEHICLE_ID = "fd7e1b90-31c2-4d68-b5c3-c96233574534"
const val URL = "/api/v1/vehicles/"

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = ["classpath:populateWithTestData.sql"], executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
internal class VehicleControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val em: EntityManager
) {

    private val instructorWoVehicleDto = InstructorDto(
        id = UUID.fromString(INSTRUCTOR_WO_VEHICLE_ID),
        email = "michaelinstructor@gmail.com",
        userType = UserType.Instructor,
        phoneNumber = "+37144444444",
        firstName = "Michael",
        lastName = "Dawkins",
        userpicPath = "anotherpath/to/pic",
        selfDescription = "Im an instructor",
        startLocation = "Riga center",
        language = listOf("English"),
        grade = 0.0,
        isActive = true,
        vehicles = mutableListOf()
    )

    private val instructorWithVehicleDto = InstructorDto(
        id = UUID.fromString(INSTRUCTOR_WO_VEHICLE_ID),
        email = "michaelinstructor@gmail.com",
        userType = UserType.Instructor,
        phoneNumber = "+37144444444",
        firstName = "Michael",
        lastName = "Dawkins",
        userpicPath = "anotherpath/to/pic",
        selfDescription = "Im an instructor",
        startLocation = "Riga center",
        language = listOf("English"),
        grade = 0.0,
        isActive = true,
        vehicles = listOf(
            VehicleDto(
                UUID.randomUUID(),
                VehicleCategory.C,
                "AA-1234",
                "Mercedes",
                "220",
                2005,
                Gearbox.Manual,
                WheelDrive.All
            )
        )
    )

    private val vehicleDto = VehicleDto(
        UUID.fromString(ID),
        VehicleCategory.B,
        "QQ-777",
        "BMW",
        "320",
        2006,
        Gearbox.Automatic,
        WheelDrive.Rear
    )

    private val vehicleDtoJson = """
        {
            "id": """" + UUID.fromString(ID) + """",
            "vehicleCategory": "B",
            "regPlate": "QQ-777",
            "manufacturer": "BMW",
            "model": "320",
            "productionYear": "2006",
            "gearBox": "Automatic",
            "wheelDrive": "Rear"
        }
        """

    private val createVehicleDto = CreateVehicleDto(
        UUID.randomUUID(),
        VehicleCategory.C,
        "AA-1234",
        "Mercedes",
        "220",
        2005,
        Gearbox.Manual,
        WheelDrive.All
    )

    private val createVehicleDtoJson = """
        {
            "id": """" + UUID.randomUUID() + """",
            "vehicleCategory": "C",
            "regPlate": "AA-1234",
            "manufacturer": "Mercedes",
            "model": "220",
            "productionYear": "2005",
            "gearBox": "Manual",
            "wheelDrive": "All"
        }
        """

    @Nested
    inner class Update {
        @Test
        fun `response status is Ok`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = vehicleDtoJson
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
                content = vehicleDtoJson
            }.andExpect {
                jsonPath("$.vehicleCategory", Matchers.equalTo(vehicleDto.vehicleCategory.toString()))
                jsonPath("$.regPlate", Matchers.equalTo(vehicleDto.regPlate))
                jsonPath("$.manufacturer", Matchers.equalTo(vehicleDto.manufacturer))
                jsonPath("$.model", Matchers.equalTo(vehicleDto.model))
                jsonPath("$.productionYear", Matchers.equalTo(vehicleDto.productionYear?.toInt()))
                jsonPath("$.gearBox", Matchers.equalTo(vehicleDto.gearBox.toString()))
                jsonPath("$.wheelDrive", Matchers.equalTo(vehicleDto.wheelDrive.toString()))
            }
        }

        @Test
        fun `record in DB correctly`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = vehicleDtoJson
            }.andExpect {
                val vehicleFromDb = em.find(Vehicle::class.java, UUID.fromString(ID))

                jsonPath("$.vehicleCategory", Matchers.equalTo(vehicleFromDb.vehicleCategory.toString()))
                jsonPath("$.regPlate", Matchers.equalTo(vehicleFromDb.regPlate))
                jsonPath("$.manufacturer", Matchers.equalTo(vehicleFromDb.manufacturer))
                jsonPath("$.model", Matchers.equalTo(vehicleFromDb.model))
                jsonPath("$.productionYear", Matchers.equalTo(vehicleFromDb.productionYear?.toInt()))
                jsonPath("$.gearBox", Matchers.equalTo(vehicleFromDb.gearBox.toString()))
                jsonPath("$.wheelDrive", Matchers.equalTo(vehicleFromDb.wheelDrive.toString()))
            }
        }

        @Test
        fun `instructor record in DB correctly`() {
            mockMvc.put(URL + ID) {
                contentType = MediaType.APPLICATION_JSON
                content = vehicleDtoJson
            }.andExpect {
                val vehicleFromDb = em.find(Vehicle::class.java, UUID.fromString(ID))

                jsonPath("$.vehicleCategory", Matchers.equalTo(vehicleFromDb.vehicleCategory.toString()))
                jsonPath("$.regPlate", Matchers.equalTo(vehicleFromDb.regPlate))
                jsonPath("$.manufacturer", Matchers.equalTo(vehicleFromDb.manufacturer))
                jsonPath("$.model", Matchers.equalTo(vehicleFromDb.model))
                jsonPath("$.productionYear", Matchers.equalTo(vehicleFromDb.productionYear?.toInt()))
                jsonPath("$.gearBox", Matchers.equalTo(vehicleFromDb.gearBox.toString()))
                jsonPath("$.wheelDrive", Matchers.equalTo(vehicleFromDb.wheelDrive.toString()))
            }
        }
    }

    @Nested
    inner class Create {
        @Test
        fun `response status is Created`() {
            mockMvc.post(URL + INSTRUCTOR_WO_VEHICLE_ID) {
                contentType = MediaType.APPLICATION_JSON
                content = createVehicleDtoJson
            }.andExpect {
                status {
                    isCreated()
                }
            }
        }

        @Test
        fun `response body is correct`() {
            mockMvc.post(URL + INSTRUCTOR_WO_VEHICLE_ID) {
                contentType = MediaType.APPLICATION_JSON
                content = createVehicleDtoJson
            }.andExpect {
                jsonPath("$.firstName", Matchers.equalTo(instructorWoVehicleDto.firstName))
                jsonPath("$.lastName", Matchers.equalTo(instructorWoVehicleDto.lastName))
                jsonPath("$.email", Matchers.equalTo(instructorWoVehicleDto.email))
                jsonPath("$.userType", Matchers.equalTo(UserType.Instructor.toString()))
                jsonPath("$.phoneNumber", Matchers.equalTo(instructorWoVehicleDto.phoneNumber))
                jsonPath("$.userpicPath", Matchers.equalTo(instructorWoVehicleDto.userpicPath))
                jsonPath("$.selfDescription", Matchers.equalTo(instructorWoVehicleDto.selfDescription))
                jsonPath("$.startLocation", Matchers.equalTo(instructorWoVehicleDto.startLocation))
                jsonPath("$.language", Matchers.equalTo(instructorWoVehicleDto.language))
                jsonPath("$.grade", Matchers.equalTo(instructorWoVehicleDto.grade))
                jsonPath("$.isActive", Matchers.equalTo(instructorWoVehicleDto.isActive))
                jsonPath(
                    "$.vehicles[0].vehicleCategory",
                    Matchers.equalTo(instructorWithVehicleDto.vehicles[0].vehicleCategory.toString())
                )
                jsonPath("$.vehicles[0].regPlate", Matchers.equalTo(instructorWithVehicleDto.vehicles[0].regPlate))
                jsonPath(
                    "$.vehicles[0].manufacturer",
                    Matchers.equalTo(instructorWithVehicleDto.vehicles[0].manufacturer)
                )
                jsonPath("$.vehicles[0].model", Matchers.equalTo(instructorWithVehicleDto.vehicles[0].model))
                jsonPath(
                    "$.vehicles[0].productionYear",
                    Matchers.equalTo(instructorWithVehicleDto.vehicles[0].productionYear?.toInt())
                )
                jsonPath(
                    "$.vehicles[0].gearBox",
                    Matchers.equalTo(instructorWithVehicleDto.vehicles[0].gearBox.toString())
                )
                jsonPath(
                    "$.vehicles[0].wheelDrive",
                    Matchers.equalTo(instructorWithVehicleDto.vehicles[0].wheelDrive.toString())
                )
            }
        }

        @Test
        fun `new record in vehicle table`() {
            val countBeforeInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            mockMvc.post(URL + INSTRUCTOR_WO_VEHICLE_ID) {
                contentType = MediaType.APPLICATION_JSON
                content = createVehicleDtoJson
            }

            val countAfterInsert = em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            assertEquals(countBeforeInsert.inc(), countAfterInsert)
        }

        @Test
        fun `new record in vehicle table with correct data`() {
            val response = mockMvc.post(URL + INSTRUCTOR_WO_VEHICLE_ID) {
                contentType = MediaType.APPLICATION_JSON
                content = createVehicleDtoJson
            }.andReturn().response

            val instructorIdFromResponse = UUID.fromString(JsonPath.read(response.contentAsString, "$.id"))
            val vehicleIdFromResponse = UUID.fromString(JsonPath.read(response.contentAsString, "$.vehicles[0].id"))

            val instructorFromDb = em.find(Instructor::class.java, instructorIdFromResponse)
            val vehiclesFromDb = em.find(Vehicle::class.java, vehicleIdFromResponse)

            assertThat(instructorFromDb.vehicles[0].vehicleCategory).isEqualTo(createVehicleDto.vehicleCategory)
            assertThat(instructorFromDb.vehicles[0].regPlate).isEqualTo(createVehicleDto.regPlate)
            assertThat(instructorFromDb.vehicles[0].manufacturer).isEqualTo(createVehicleDto.manufacturer)
            assertThat(instructorFromDb.vehicles[0].model).isEqualTo(createVehicleDto.model)
            assertThat(instructorFromDb.vehicles[0].productionYear).isEqualTo(createVehicleDto.productionYear)
            assertThat(instructorFromDb.vehicles[0].gearBox).isEqualTo(createVehicleDto.gearBox)
            assertThat(instructorFromDb.vehicles[0].wheelDrive).isEqualTo(createVehicleDto.wheelDrive)

            assertThat(vehiclesFromDb.vehicleCategory).isEqualTo(createVehicleDto.vehicleCategory)
            assertThat(vehiclesFromDb.regPlate).isEqualTo(createVehicleDto.regPlate)
            assertThat(vehiclesFromDb.manufacturer).isEqualTo(createVehicleDto.manufacturer)
            assertThat(vehiclesFromDb.model).isEqualTo(createVehicleDto.model)
            assertThat(vehiclesFromDb.productionYear).isEqualTo(createVehicleDto.productionYear)
            assertThat(vehiclesFromDb.gearBox).isEqualTo(createVehicleDto.gearBox)
            assertThat(vehiclesFromDb.wheelDrive).isEqualTo(createVehicleDto.wheelDrive)
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
                    val vehicleFromDb = em.find(Vehicle::class.java, UUID.fromString(ID))

                    jsonPath("$.vehicleCategory", Matchers.equalTo(vehicleFromDb.vehicleCategory.toString()))
                    jsonPath("$.regPlate", Matchers.equalTo(vehicleFromDb.regPlate))
                    jsonPath("$.manufacturer", Matchers.equalTo(vehicleFromDb.manufacturer))
                    jsonPath("$.model", Matchers.equalTo(vehicleFromDb.model))
                    jsonPath("$.productionYear", Matchers.equalTo(vehicleFromDb.productionYear?.toInt()))
                    jsonPath("$.gearBox", Matchers.equalTo(vehicleFromDb.gearBox.toString()))
                    jsonPath("$.wheelDrive", Matchers.equalTo(vehicleFromDb.wheelDrive.toString()))
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
        fun `amount of records in vehicle table decreased`() {
            val countBeforeInsert =
                em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            mockMvc.delete(URL + ID)

            val countAfterInsert =
                em.createNativeQuery("SELECT COUNT(*) FROM vehicle").singleResult as BigInteger

            assertEquals(countBeforeInsert.dec(), countAfterInsert)
        }

        @Test
        fun `removed record in vehicle table returns null`() {
            mockMvc.delete(URL + ID)

            val vehicle = em.find(Vehicle::class.java, UUID.fromString(ID))

            assertThat(vehicle).isNull()
        }
    }
}