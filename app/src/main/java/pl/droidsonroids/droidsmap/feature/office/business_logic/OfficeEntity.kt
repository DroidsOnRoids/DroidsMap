package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.model.Entity

data class OfficeEntity(
        val centerLatitude: Double = 0.0,
        val centerLongitude: Double = 0.0,
        val leftTopCornerLatitude: Double = 0.0,
        val leftTopCornerLongitude: Double = 0.0
) : Entity()