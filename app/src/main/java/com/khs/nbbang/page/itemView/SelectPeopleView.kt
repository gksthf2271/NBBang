package com.khs.nbbang.page.itemView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.khs.nbbang.R
import com.khs.nbbang.user.Member
import com.khs.nbbang.utils.DisplayUtils
import com.khs.nbbang.utils.GlideUtils
import kotlinx.android.synthetic.main.cview_text_people.view.*

class SelectPeopleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.cview_text_people, this)
        initView()
    }

    fun initView() {

    }

    fun setViewSize(horizontalRowCount : Int) {
        val viewSize = DisplayUtils().getItemViewSize(context, horizontalRowCount)
        layoutParams = LayoutParams(viewSize, viewSize)
    }

    fun setMember(member : Member) {
        tag = member
        checkbox_name.text = member.name
        checkbox_name.isChecked = false
        layout_root.setOnClickListener {
            selectCircleView()
        }
        GlideUtils().drawMemberProfile(img_profile, member, null)
    }

    fun selectCircleView() {
        checkbox_name.isChecked = !checkbox_name.isChecked
        layout_profile.isSelected = !layout_profile.isSelected
    }
}