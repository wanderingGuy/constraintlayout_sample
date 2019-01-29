package com.bftv.wanderingguy.constraint_layout_demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.support.constraint.ConstraintSet
import android.support.constraint.ConstraintLayout
import android.widget.Button
import android.util.DisplayMetrics

/**
 * 代码布局示例
 */
class ProgramActivity : AppCompatActivity() {

    private lateinit var mOkBtn: Button
    private lateinit var mCancelBtn: Button
    private lateinit var mConstraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mConstraintLayout = ConstraintLayout(this)

        mOkBtn = Button(this)
        mOkBtn.id = View.generateViewId()
        mOkBtn.text = "确定"
        mOkBtn.setOnClickListener {
            add(mConstraintLayout)
        }
        mConstraintLayout.addView(mOkBtn)

        mCancelBtn = Button(this)
        mCancelBtn.id = View.generateViewId()
        mCancelBtn.text = "取消"
        mConstraintLayout.addView(mCancelBtn)

        val set = program()
        set.applyTo(mConstraintLayout)
        setContentView(mConstraintLayout)
    }

    /**
     * 添加一个view 至少需指定约束条件和宽高
     */
    private fun add(layout: ConstraintLayout) {
        val set = ConstraintSet()
        set.clone(layout)

        val btn = Button(this)
        btn.text = "新的button"
        btn.id = View.generateViewId()
        layout.addView(btn)
        set.connect(btn.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0)
        set.connect(btn.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.connect(btn.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0)
        set.connect(btn.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 0)
        set.constrainWidth(btn.id, ConstraintSet.WRAP_CONTENT)
        set.constrainHeight(btn.id, ConstraintSet.WRAP_CONTENT)

        set.applyTo(layout)
    }

    /**
     * 通过代码创建约束条件
     */
    private fun program(): ConstraintSet {
        val set = ConstraintSet()
        //int startID, int startSide, int endID, int endSide, int margin
        set.connect(mOkBtn.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, dpToPx(8))
        set.connect(mOkBtn.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, dpToPx(8))
        set.connect(mOkBtn.id, ConstraintSet.RIGHT, mCancelBtn.id, ConstraintSet.LEFT)
        //限定大小
        set.constrainWidth(mOkBtn.id, ConstraintSet.MATCH_CONSTRAINT)
        set.constrainHeight(mOkBtn.id, ConstraintSet.WRAP_CONTENT)

        set.connect(mCancelBtn.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, dpToPx(8))
        set.connect(mCancelBtn.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0)
        set.constrainWidth(mCancelBtn.id, ConstraintSet.WRAP_CONTENT)
        set.constrainHeight(mCancelBtn.id, ConstraintSet.WRAP_CONTENT)
        return set
    }

    fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}