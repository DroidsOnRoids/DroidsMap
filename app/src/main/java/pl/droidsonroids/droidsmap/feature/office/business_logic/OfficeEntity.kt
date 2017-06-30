package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.model.Entity

data class OfficeEntity(
        val centerLatitude: Double = 0.0,
        val centerLongitude: Double = 0.0,
        val topLeftCornerLatitude: Double = 0.0,
        val topLeftCornerLongitude: Double = 0.0,
        val mapViewportBearing: Double = 0.0,
        val translatedMapViewportBearing: Double = 0.0,
        val mapViewportConstraint: Double = 0.0
) : Entity()