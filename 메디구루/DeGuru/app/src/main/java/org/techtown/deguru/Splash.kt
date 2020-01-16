package org.techtown.deguru

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
