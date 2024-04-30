package com.example.testandroid2.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.text.NumberFormat

fun getNumberRupiah(number: Int?): String {
    if(number == null) return "Rp. 0"
    return "Rp ${NumberFormat.getIntegerInstance().format(number)}"
}

fun getBase64FromUri(uri: Uri?, contentResolver: ContentResolver): String =
    uri?.let { uriNotNull ->
        val imageStream: InputStream? = contentResolver.openInputStream(uriNotNull)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        encodeImage(selectedImage)
    }.toString()

private fun encodeImage(bm: Bitmap): String {
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
    val b: ByteArray = baos.toByteArray()
    return Base64.encodeToString(b, Base64.DEFAULT)
}

fun convertStringToBitMap(encodeString: String): Bitmap? {
    val encodeByte = Base64.decode(encodeString, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
}