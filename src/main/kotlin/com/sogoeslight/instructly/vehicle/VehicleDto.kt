package com.sogoeslight.instructly.vehicle

import java.util.*

class VehicleDto(
    val id: UUID,
    val vehicleCategory: VehicleCategory,
    val regPlate: String,
    val manufacturer: String,
    val model: String,
    val productionYear: Short?,
    val gearBox: Gearbox?,
    val wheelDrive: WheelDrive?
)

class CreateVehicleDto(
    val id: UUID = UUID.randomUUID(),
    val vehicleCategory: VehicleCategory,
    val regPlate: String,
    val manufacturer: String,
    val model: String,
    val productionYear: Short?,
    val gearBox: Gearbox?,
    val wheelDrive: WheelDrive?
)