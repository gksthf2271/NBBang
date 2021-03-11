package com.khs.nbbang.history

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.khs.nbbang.R
import com.khs.nbbang.databinding.CviewPieChartBinding
import com.khs.nbbang.utils.NumberUtils

class PieChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    val TAG = this.javaClass.simpleName
    var mBinding: CviewPieChartBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mBinding = CviewPieChartBinding.inflate(inflater, this,true)
        initPie()
    }

    private val mColorList = intArrayOf(
        ContextCompat.getColor(context, R.color.point_1),
        ContextCompat.getColor(context, R.color.point_2),
        ContextCompat.getColor(context, R.color.point_3),
        ContextCompat.getColor(context, R.color.point_4),
        ContextCompat.getColor(context, R.color.point_5),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_1),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_2),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_3),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_4),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_5),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_6),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_7),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_8),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_9),
        ContextCompat.getColor(context, R.color.history_pie_chart_color_10)
    )

    private fun initPie() {
        mBinding.pieChart.apply {
            setUsePercentValues(true)
            setExtraOffsets(5f, 10f, 5f, 5f)

            dragDecelerationFrictionCoef = 0.95f
            description.isEnabled = false

            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)

            setCenterTextSize(10f)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(50)

            holeRadius = 38f
            transparentCircleRadius = 41f

            setDrawCenterText(true)

            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true

            setDrawEntryLabels(true)

            animateY(1400, Easing.EaseInOutQuad)

            legend.apply {
                verticalAlignment = Legend.LegendVerticalAlignment.TOP
                horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
                orientation = Legend.LegendOrientation.VERTICAL
                setDrawInside(false)
                isEnabled = false
            }
            setEntryLabelColor(Color.WHITE)
            setEntryLabelTextSize(12f)
        }
    }

    fun setData(pieEntryList: ArrayList<PieEntry>, totalPrice: Int) {
        val dataSet = PieDataSet(pieEntryList, "")

        dataSet.setDrawIcons(false)

        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(50f, 80f)
        dataSet.selectionShift = 5f

        dataSet.colors.addAll(mColorList.toMutableList())

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(mBinding.pieChart))
        data.setValueTextSize(10f)
        data.setValueTextColor(Color.WHITE)
        mBinding.pieChart.apply {
            setData(data)
            highlightValues(null)
            animateY(1000, Easing.EaseInCubic)
            centerText = "총 지출 : ${NumberUtils().makeCommaNumber(totalPrice)}"
            invalidate()
        }
    }
}