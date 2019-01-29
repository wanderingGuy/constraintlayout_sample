package com.bftv.wanderingguy.constraint_layout_demo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import hugo.weaving.DebugLog

class MainActivity : AppCompatActivity() {

    @DebugLog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //placeholder test
        setContentView(R.layout.test_video_constraint)
//        placeholder.emptyVisibility = View.GONE
//        avatar.setOnClickListener {
//            placeholder.setContentId(avatar.id)
//        }
    }
}
