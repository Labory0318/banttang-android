package com.hproject.core.presentation.binding_adapter

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.LayoutRes
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

object SpinnerBindingAdapter {
    @JvmStatic
    @BindingAdapter(
        value = [
            "entries",
            "layoutRes",
            "dropDownLayoutRes"
        ],
        requireAll = false
    )
    fun Spinner.setEntries(
        entries: List<Any>,
        @LayoutRes layoutRes: Int? = null,
        @LayoutRes dropDownLayoutRes: Int? = null
    ) {
        val layout = layoutRes ?: android.R.layout.simple_spinner_item
        val dropDownLayout = dropDownLayoutRes ?: android.R.layout.simple_spinner_dropdown_item

        adapter = ArrayAdapter(context, layout, entries).also {
            it.setDropDownViewResource(dropDownLayout)
        }
    }

    @JvmStatic
    @BindingAdapter(
        value = [
            "minNumber",
            "maxNumber",
            "layoutRes",
            "dropDownLayoutRes"
        ],
        requireAll = false
    )
    fun Spinner.setNumberEntries(
        minNumber: Int = 0,
        maxNumber: Int,
        @LayoutRes layoutRes: Int? = null,
        @LayoutRes dropDownLayoutRes: Int? = null
    ) = setEntries(
        entries = List((maxNumber - minNumber + 1)) { it + minNumber },
        layoutRes = layoutRes,
        dropDownLayoutRes = dropDownLayoutRes
    )

    @JvmStatic
    @BindingAdapter("selectedValue")
    @Suppress("UNCHECKED_CAST")
    fun <T> Spinner.setSelectedValue(
        selectedValue: T?
    ) {
        (adapter as? ArrayAdapter<T>)?.let { spinnerAdapter ->
            val selectedPosition = spinnerAdapter.getPosition(selectedValue)
            setSelection(selectedPosition)
            tag = selectedPosition
        }
    }

    @JvmStatic
    @InverseBindingAdapter(
        attribute = "selectedValue",
        event = "selectedValueChanged"
    )
    @Suppress("UNCHECKED_CAST")
    fun <T> Spinner.getSelectedValue(): T? {
        return selectedItem as? T
    }

    @JvmStatic
    @BindingAdapter("selectedValueChanged")
    fun Spinner.setInverseBindingListener(inverseBindingListener: InverseBindingListener?) {
        inverseBindingListener?.run {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    if (tag != position) {
                        inverseBindingListener.onChange()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
    }
}