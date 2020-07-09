package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import viz.commonlib.pointseekbar.PointInfo

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pointSeekBar.pointList = mutableListOf(
            PointInfo(10, desc = "进度10"),
            PointInfo(20, desc = "进度20"),
            PointInfo(35, desc = "进度35"),
            PointInfo(55, desc = "进度55"),
            PointInfo(83, desc = "进度83")
        )
    }
}