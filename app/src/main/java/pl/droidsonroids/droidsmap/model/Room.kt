package pl.droidsonroids.droidsmap.model

private const val PX_TO_METERS_FACTOR = 0.0333333333333f
private const val LATITUDE_TO_METERS_FACTOR = 111250.33305527679f
private const val LONGITUDE_TO_METERS_FACTOR = 70032.3813587852f

data class Room(private val roomHeightPx: Float, private val roomWidthPx: Float, private val relativeCenterXPositionPx: Float,
                private val relativeCenterYPositionPx: Float, val tag: String, val imageResource: Int) {

    fun getRoomHeightMeters() = roomHeightPx * PX_TO_METERS_FACTOR

    fun getRoomWidthMeters() = roomWidthPx * PX_TO_METERS_FACTOR

    fun getRelativeCenterLatitude(translationDegrees: Float): Float {
        val r = Math.sqrt(Math.pow(relativeCenterXPositionPx.toDouble(), 2.0) + Math.pow(relativeCenterYPositionPx.toDouble(), 2.0)).toFloat()
        val phiRadians = Math.atan((relativeCenterYPositionPx / relativeCenterXPositionPx).toDouble()).toFloat()
        val translatedPhiRadians = (phiRadians + Math.toRadians(translationDegrees.toDouble())).toFloat()
        return (r.toDouble() * Math.cos(translatedPhiRadians.toDouble()) * PX_TO_METERS_FACTOR.toDouble() / LATITUDE_TO_METERS_FACTOR).toFloat()
    }

    fun getRelativeCenterLongitude(translationDegrees: Float): Float {
        val r = Math.sqrt(Math.pow(relativeCenterXPositionPx.toDouble(), 2.0) + Math.pow(relativeCenterYPositionPx.toDouble(), 2.0)).toFloat()
        val phiRadians = Math.atan((relativeCenterYPositionPx / relativeCenterXPositionPx).toDouble()).toFloat()
        val translatedPhiRadians = (phiRadians + Math.toRadians(translationDegrees.toDouble())).toFloat()
        return (r.toDouble() * Math.sin(translatedPhiRadians.toDouble()) * PX_TO_METERS_FACTOR.toDouble() / LONGITUDE_TO_METERS_FACTOR).toFloat()
    }
}