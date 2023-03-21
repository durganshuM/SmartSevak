package com.example.smartsevak

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

class QRActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    private lateinit var generateButton: Button
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qractivity)

        textView = findViewById(R.id.cardNumberView)
        generateButton = findViewById(R.id.generate_button)
        imageView = findViewById(R.id.qrcode_image)

        generateButton.setOnClickListener {
            val textToEncode = textView.text.toString()
            if (textToEncode.isNotEmpty()) {
                val bitmap = generateQRCode(textToEncode)
                imageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun generateQRCode(textToEncode: String): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        try {
            val bitMatrix: BitMatrix =
                qrCodeWriter.encode(textToEncode, BarcodeFormat.QR_CODE, 200, 200)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    bmp.setPixel(x, y, if (bitMatrix.get(x, y)) ContextCompat.getColor(this, R.color.black) else ContextCompat.getColor(this, R.color.white))
                }
            }
            return bmp
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}