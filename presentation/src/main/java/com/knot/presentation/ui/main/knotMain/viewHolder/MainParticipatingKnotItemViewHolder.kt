package com.knot.presentation.ui.main.knotMain.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.R
import com.knot.presentation.databinding.RecyclerItemMainParticipatingKnotBinding
import com.knot.presentation.ui.main.knotMain.adapter.MainParticipatingKnotAdapter

class MainParticipatingKnotItemViewHolder(
    private val binding: RecyclerItemMainParticipatingKnotBinding,
    private val listener: MainParticipatingKnotAdapter.MainParticipatingKnotDelegate
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.apply {

        }
    }

    fun bind(item : String, position : String) {
        binding.apply {
            textViewKnotNumber.text = binding.root.context.getString(R.string.main_participating_knot_number, position)
            textViewKnotTitle.text = item
        }

    }
}