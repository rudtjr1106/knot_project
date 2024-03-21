package com.knot.presentation.ui.main.knotMain.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.databinding.RecyclerLayoutMainTopBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.util.DateTimeFormatter

class MainTopViewHolder(
    private val binding: RecyclerLayoutMainTopBinding,
    private val listener: MainAdapter.MainDelegate
) : RecyclerView.ViewHolder(binding.root) {

    companion object{
        const val SUNDAY = 0
        const val MONDAY = 1
        const val TUESDAY = 2
        const val WEDNESDAY = 3
        const val THURSDAY = 4
        const val FRIDAY = 5
        const val SATURDAY = 6
    }

    init {
        binding.apply {

        }
    }

    fun bind(item : List<String>) {
        binding.apply {
            includeLayoutWeek.textViewSundayDate.text = item[SUNDAY]
            includeLayoutWeek.textViewMondayDate.text = item[MONDAY]
            includeLayoutWeek.textViewTuesdayDate.text = item[TUESDAY]
            includeLayoutWeek.textViewWednesdayDate.text = item[WEDNESDAY]
            includeLayoutWeek.textViewThursdayDate.text = item[THURSDAY]
            includeLayoutWeek.textViewFridayDate.text = item[FRIDAY]
            includeLayoutWeek.textViewSaturdayDate.text = item[SATURDAY]
        }
        showToday()
    }

    private fun showToday(){
        binding.apply {
            when(DateTimeFormatter.getTodayDayOfWeek()){
                SUNDAY -> includeLayoutWeek.imageViewSundayCircle.visibility = View.VISIBLE
                MONDAY -> includeLayoutWeek.imageViewMondayCircle.visibility = View.VISIBLE
                TUESDAY -> includeLayoutWeek.imageViewTuesdayCircle.visibility = View.VISIBLE
                WEDNESDAY -> includeLayoutWeek.imageViewWednesdayCircle.visibility = View.VISIBLE
                THURSDAY -> includeLayoutWeek.imageViewThursdayCircle.visibility = View.VISIBLE
                FRIDAY -> includeLayoutWeek.imageViewFridayCircle.visibility = View.VISIBLE
                SATURDAY -> includeLayoutWeek.imageViewSaturdayCircle.visibility = View.VISIBLE
            }
        }
    }
}