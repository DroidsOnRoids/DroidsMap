package pl.droidsonroids.droidsmap.glide

import android.content.Context
import android.graphics.drawable.PictureDrawable
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream

@GlideModule
class SvgModule : AppGlideModule() {
    override fun registerComponents(context: Context?, registry: Registry?) {
        registry!!.register(SVG::class.java, PictureDrawable::class.java, SvgBitmapTranscoder())
                .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    // Disable manifest parsing to avoid adding similar modules twice.
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}
