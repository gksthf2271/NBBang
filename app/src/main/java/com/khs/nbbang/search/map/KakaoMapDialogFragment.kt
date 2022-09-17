package com.khs.nbbang.search.map

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.khs.nbbang.R
import com.khs.nbbang.base.BaseDialogFragment
import com.khs.nbbang.databinding.FragmentMapBinding
import com.khs.nbbang.search.response.DocumnetModel
import com.khs.nbbang.utils.LogUtil
import net.daum.mf.map.api.CameraUpdateFactory
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class KakaoMapDialogFragment(private val mPlaceData: DocumnetModel) : BaseDialogFragment(DIALOG_TYPE.TYPE_MAP){
    lateinit var mBinding: FragmentMapBinding
    private lateinit var mMapView : MapView
    private val DEBUG = false

    companion object {
        @Volatile
        private var instance: KakaoMapDialogFragment? = null

        @JvmStatic
        fun getInstance(mPlaceData: DocumnetModel): KakaoMapDialogFragment =
            instance ?: synchronized(this) {
                instance
                    ?: KakaoMapDialogFragment(mPlaceData).also {
                        instance = it
                    }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentMapBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        createMapView()
        mBinding.apply {
            txtResultTitle.text = mPlaceData.place_name
            txtResultAddress.text = mPlaceData.address_name
            txtResultCategory.text = mPlaceData.category_group_name + " "
            txtResultPhoneNumber.text = mPlaceData.phone
            imgClose.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun createMapView() {
        LogUtil.iLog(LOG_TAG, TAG_CLASS, "createMapView(...)")
        try {
            mMapView = MapView(requireActivity()).apply {
                setMapViewEventListener {
                    LogUtil.iLog(LOG_TAG, TAG_CLASS, "onMapViewInitialized(...)")
                    createMarker()
                    show()
                }
                mapType = MapView.MapType.Standard
            }
            mBinding.containerMap.addView(mMapView)
        } catch (e: UnsatisfiedLinkError) {
            LogUtil.eLog(LOG_TAG, TAG_CLASS, "failed Kakao Maps Loading! > $e")
            requireActivity().setResult(RESULT_SEARCH_FINISH, Intent().apply {
                putExtra(RESULT_MSG,"잠시후 다시 이용해주세요.")
            })
            requireActivity().finish()
        }
    }

    private fun createMarker() {
        LogUtil.iLog(LOG_TAG, TAG_CLASS, "createMarker(...)")
        var point = MapPoint.mapPointWithGeoCoord(mPlaceData.y.toDouble(), mPlaceData.x.toDouble())

        val height = 50
        val width = 50
        val bitmapdraw = requireContext().getDrawable(R.drawable.app_icon) as BitmapDrawable
        val b = bitmapdraw.bitmap
        val smallMarker = Bitmap.createScaledBitmap(b, width, height, false)

        var customMarker = MapPOIItem().apply {
            itemName = mPlaceData.place_name
            tag = 1
            mapPoint = point
            markerType = MapPOIItem.MarkerType.CustomImage
//            customImageResourceId = R.drawable.app_icon
            customImageBitmap = smallMarker
            isCustomImageAutoscale = true
            setCustomImageAnchor(0.5f, 1.0f)
        }
        mMapView.apply {
            addPOIItem(customMarker)
            selectPOIItem(customMarker, true)
            setMapCenterPoint(point, false)
        }
    }

    fun show() {
        LogUtil.iLog(LOG_TAG, TAG_CLASS, "show(...)")
        var mapPoint = MapPoint.mapPointWithGeoCoord(mPlaceData.y.toDouble(), mPlaceData.x.toDouble())
        mMapView.moveCamera(CameraUpdateFactory.newMapPoint(mapPoint, 2f))
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                if (this.isAdded) {
                    dismiss()
                    return true
                }
            }
        }
        return false
    }
}