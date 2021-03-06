package com.akexorcist.recyclerbackgroundsupportview

import android.content.Context
import android.content.res.ColorStateList
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.EdgeEffectFactory.DIRECTION_TOP
import com.akexorcist.recyclerbackgroundsupportview.databinding.ViewRecyclerBackgroundSupportBinding

open class RecyclerBackgroundSupportView : FrameLayout {
    private lateinit var binding: ViewRecyclerBackgroundSupportBinding

    private var adjustViewBounds: Boolean = false
    private var cropToPadding: Boolean = false
    private var scaleType: Int = -1
    private var resId: Int = -1
    private var gravity: Int = ScrollSupportImageView.DIRECTION_TOP
    private var tint: ColorStateList? = null

    constructor(context: Context) : super(context) {
        setup()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setup(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setup(attrs)
    }

    override fun dispatchSaveInstanceState(container: SparseArray<Parcelable>) {
        dispatchFreezeSelfOnly(container)
    }

    override fun dispatchRestoreInstanceState(container: SparseArray<Parcelable>) {
        dispatchThawSelfOnly(container)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is ChildSavedState) {
            super.onRestoreInstanceState(state)
            return
        }
        super.onRestoreInstanceState(state.superState)
        restoreInstanceState(state)
        onRestoreInstanceChildState(state)
    }

    override fun onSaveInstanceState(): Parcelable? {
        return saveInstanceState(super.onSaveInstanceState())
    }

    fun getImageView(): ImageView = binding.rbsvImageView

    fun getRecyclerView(): RecyclerView = binding.rbsvRecyclerView

    private fun setup(attrs: AttributeSet? = null) {
        binding = ViewRecyclerBackgroundSupportBinding.inflate(LayoutInflater.from(context), this, true)
        binding.rbsvImageView.setRecyclerView(binding.rbsvRecyclerView)
        attrs?.let {
            setupStyleables(attrs)
        }
    }

    private fun setupStyleables(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RecyclerBackgroundSupportView)
        adjustViewBounds = typedArray.getBoolean(R.styleable.RecyclerBackgroundSupportView_rbsv_adjustViewBounds, false)
        cropToPadding = typedArray.getBoolean(R.styleable.RecyclerBackgroundSupportView_rbsv_cropToPadding, false)
        scaleType = typedArray.getInt(R.styleable.RecyclerBackgroundSupportView_rbsv_scaleType, -1)
        resId = typedArray.getResourceId(R.styleable.RecyclerBackgroundSupportView_rbsv_src, -1)
        gravity = typedArray.getInt(R.styleable.RecyclerBackgroundSupportView_rbsv_gravity, ScrollSupportImageView.DIRECTION_TOP)
        tint = typedArray.getColorStateList(R.styleable.RecyclerBackgroundSupportView_rbsv_tint)
        typedArray.recycle()

        updateImageDirection()
        updateImageScaleType()
        updateImageDrawable()
        updateImageCropToPadding()
        updateImageAdjustViewBounds()
    }

    private fun updateImageDirection() {
        binding.rbsvImageView.setDirection(gravity)
    }

    private fun updateImageScaleType() {
        if (scaleType >= 0) {
            binding.rbsvImageView.scaleType = getScaleType(scaleType)
        }
    }

    private fun updateImageDrawable() {
        val drawable = ResourcesCompat.getDrawable(resources, resId, context.theme)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable?.setTintList(tint)
        }
        binding.rbsvImageView.setImageDrawable(drawable)
    }

    private fun updateImageCropToPadding() {
        binding.rbsvImageView.cropToPadding = cropToPadding
    }

    private fun updateImageAdjustViewBounds() {
        binding.rbsvImageView.adjustViewBounds = adjustViewBounds
    }

    private fun getScaleType(index: Int): ScaleType =
        arrayOf(
            ScaleType.MATRIX,
            ScaleType.FIT_XY,
            ScaleType.FIT_START,
            ScaleType.FIT_CENTER,
            ScaleType.FIT_END,
            ScaleType.CENTER,
            ScaleType.CENTER_CROP,
            ScaleType.CENTER_INSIDE
        )[index]

    private fun saveInstanceState(state: Parcelable?): Parcelable {
        val savedState: SavedState = onSaveInstanceChildState(SavedState(state)) as SavedState
        savedState.adjustViewBounds = adjustViewBounds
        savedState.cropToPadding = cropToPadding
        savedState.scaleType = scaleType
        savedState.resId = resId
        savedState.gravity = gravity
        savedState.tint = tint
        return savedState
    }

    private fun restoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        this.adjustViewBounds = savedState.adjustViewBounds
        this.cropToPadding = savedState.cropToPadding
        this.scaleType = savedState.scaleType
        this.resId = savedState.resId
        this.gravity = savedState.gravity
        this.tint = savedState.tint
    }

    private class SavedState : ChildSavedState {
        var adjustViewBounds: Boolean = false
        var cropToPadding: Boolean = false
        var scaleType: Int = -1
        var resId: Int = -1
        var gravity: Int = DIRECTION_TOP
        var tint: ColorStateList? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel, classLoader) {
            adjustViewBounds = parcel.readInt() != 0
            cropToPadding = parcel.readInt() != 0
            scaleType = parcel.readInt()
            resId = parcel.readInt()
            gravity = parcel.readInt()
            tint = parcel.readParcelable(ColorStateList::class.java.classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(if (adjustViewBounds) 1 else 0)
            out.writeInt(if (cropToPadding) 1 else 0)
            out.writeInt(scaleType)
            out.writeInt(resId)
            out.writeInt(gravity)
            out.writeParcelable(tint, flags)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.ClassLoaderCreator<SavedState> =
                object : Parcelable.ClassLoaderCreator<SavedState> {
                    override fun createFromParcel(source: Parcel, loader: ClassLoader): SavedState {
                        return SavedState(source, loader)
                    }

                    override fun createFromParcel(`in`: Parcel): SavedState? {
                        return null
                    }

                    override fun newArray(size: Int): Array<SavedState?> {
                        return arrayOfNulls(size)
                    }
                }
        }
    }

    private fun onSaveInstanceChildState(ss: ChildSavedState): Parcelable {
        ss.childrenStates = SparseArray()
        for (i in 0 until childCount) {
            val id = getChildAt(i).id
            if (id != 0) {
                ss.childrenStates?.let { savedChildrenState ->
                    val childrenState = SparseArray<Parcelable>()
                    getChildAt(i).saveHierarchyState(childrenState)
                    savedChildrenState.put(id, childrenState)
                }
            }

        }
        return ss
    }

    private fun onRestoreInstanceChildState(ss: ChildSavedState) {
        for (i in 0 until childCount) {
            val id = getChildAt(i).id
            if (id != 0) {
                ss.childrenStates?.get(id)?.let { childrenState ->
                    @Suppress("UNCHECKED_CAST")
                    getChildAt(i).restoreHierarchyState(childrenState as SparseArray<Parcelable>)
                }
            }
        }
    }

    abstract class ChildSavedState : BaseSavedState {
        var childrenStates: SparseArray<Any>? = null

        constructor(superState: Parcelable?) : super(superState)

        constructor(parcel: Parcel, classLoader: ClassLoader) : super(parcel) {
            childrenStates = parcel.readSparseArray(classLoader)
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            childrenStates?.let { childrenStates ->
                out.writeSparseArray(childrenStates)
            }
        }
    }
}