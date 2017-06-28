package pl.droidsonroids.droidsmap.feature.office.business_logic

import pl.droidsonroids.droidsmap.model.Entity

data class OfficeEntity(val centerLatitude: Float = 0.0f, val centerLongitude: Float = 0.0f, val leftTopCornerLatitude: Float = 0.0f, val leftTopCornerLongitude: Float = 0.0f) : Entity()