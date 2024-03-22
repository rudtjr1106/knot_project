package com.knot.presentation.ui.main.knotMain.viewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.MainLayoutVo
import com.knot.domain.vo.TodoVo
import com.knot.presentation.databinding.RecyclerLayoutMainTopBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainAdapter
import com.knot.presentation.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

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
        val dates = getWeekDates()
        binding.apply {
            includeLayoutWeek.textViewSundayDate.text = dates[SUNDAY]
            includeLayoutWeek.textViewMondayDate.text = dates[MONDAY]
            includeLayoutWeek.textViewTuesdayDate.text = dates[TUESDAY]
            includeLayoutWeek.textViewWednesdayDate.text = dates[WEDNESDAY]
            includeLayoutWeek.textViewThursdayDate.text = dates[THURSDAY]
            includeLayoutWeek.textViewFridayDate.text = dates[FRIDAY]
            includeLayoutWeek.textViewSaturdayDate.text = dates[SATURDAY]
        }
    }

    fun bind(item : List<TodoVo>) {

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

    private fun getWeekDates(): List<String> {
        val dates = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("dd")
        val calendar = Calendar.getInstance()

        // 오늘 날짜를 기준으로 설정
        calendar.time = Date()

        // 이번 주의 첫 번째 날(일요일)로 설정
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        // 일요일부터 토요일까지 날짜를 리스트에 추가
        for (i in 1..7) {
            dates.add(dateFormat.format(calendar.time))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return dates
    }
}