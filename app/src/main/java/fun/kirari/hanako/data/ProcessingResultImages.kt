package `fun`.kirari.hanako.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toHistoryBase64(maxDimension: Int = 1280, quality: Int = 82): String {
    val scaled = scaleDownIfNeeded(maxDimension)
    val output = ByteArrayOutputStream()
    scaled.compress(Bitmap.CompressFormat.JPEG, quality, output)
    if (scaled !== this) {
        scaled.recycle()
    }
    return Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
}

fun String.decodeHistoryBitmap(): Bitmap? {
    return runCatching {
        val bytes = Base64.decode(this, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }.getOrNull()
}

private fun Bitmap.scaleDownIfNeeded(maxDimension: Int): Bitmap {
    val longestEdge = maxOf(width, height)
    if (longestEdge <= maxDimension) return this
    val scale = maxDimension.toFloat() / longestEdge.toFloat()
    val targetWidth = (width * scale).toInt().coerceAtLeast(1)
    val targetHeight = (height * scale).toInt().coerceAtLeast(1)
    return Bitmap.createScaledBitmap(this, targetWidth, targetHeight, true)
}
