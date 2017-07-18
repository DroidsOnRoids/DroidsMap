package pl.droidsonroids.droidsmap.glide

import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder
import com.caverock.androidsvg.SVG


class SvgBitmapTranscoder : ResourceTranscoder<SVG, PictureDrawable> {
    override fun transcode(toTranscode: Resource<SVG>): Resource<PictureDrawable> {
        val svg = toTranscode.get()
        val picture = svg.renderToPicture()
        return SimpleResource(PictureDrawable(picture))
    }

    val id: String
        get() = ""
}
