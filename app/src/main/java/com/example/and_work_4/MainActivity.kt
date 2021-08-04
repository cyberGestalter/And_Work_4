package com.example.and_work_4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val customWatchView: AnalogWatchView = findViewById(R.id.custom_watch)
        customWatchView.hourStroke = resources.getDimension(R.dimen.new_stroke)
        customWatchView.hourArrowColor = ContextCompat.getColor(this, R.color.new_color)
    }
}