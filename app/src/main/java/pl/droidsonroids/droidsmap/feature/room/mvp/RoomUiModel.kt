package pl.droidsonroids.droidsmap.feature.room.mvp

import pl.droidsonroids.droidsmap.base.UiModel
import pl.droidsonroids.droidsmap.feature.room.business_logic.RoomEntity


private const val PX_TO_METERS_FACTOR = 0.0333333333333f
private const val LATITUDE_TO_METERS_FACTOR = 111250.33305527679f
private const val LONGITUDE_TO_METERS_FACTOR = 70032.3813587852f

data class RoomUiModel(
        val roomHeightPx: Int,
        val roomWidthPx: Int,
        val relativeCenterXPositionPx: Double,
        val relativeCenterYPositionPx: Double,
        val imageUrl: String) : UiModel {
    companion object {
        fun from(entity: RoomEntity, imageUrl: String) = RoomUiModel(
                entity.roomHeightPx,
                entity.roomWidthPx,
                entity.relativeCenterXPosition,
                entity.relativeCenterYPosition,
                imageUrl
        )
    }

    fun getRoomHeightMeters() = roomHeightPx * PX_TO_METERS_FACTOR

    fun getRoomWidthMeters() = roomWidthPx * PX_TO_METERS_FACTOR

    fun getRelativeCenterLatitude(translationDegrees: Double): Double {
        val r = Math.sqrt(Math.pow(relativeCenterXPositionPx, 2.0) + Math.pow(relativeCenterYPositionPx, 2.0))
        val phiRadians = Math.atan((relativeCenterYPositionPx / relativeCenterXPositionPx))
        val translatedPhiRadians = (phiRadians + Math.toRadians(translationDegrees))
        return (r * Math.cos(translatedPhiRadians) * PX_TO_METERS_FACTOR.toDouble() / LATITUDE_TO_METERS_FACTOR)
    }

    fun getRelativeCenterLongitude(translationDegrees: Double): Double {
        val r = Math.sqrt(Math.pow(relativeCenterXPositionPx, 2.0) + Math.pow(relativeCenterYPositionPx, 2.0))
        val phiRadians = Math.atan((relativeCenterYPositionPx / relativeCenterXPositionPx))
        val translatedPhiRadians = (phiRadians + Math.toRadians(translationDegrees))
        return (r * Math.sin(translatedPhiRadians) * PX_TO_METERS_FACTOR.toDouble() / LONGITUDE_TO_METERS_FACTOR)
    }
}