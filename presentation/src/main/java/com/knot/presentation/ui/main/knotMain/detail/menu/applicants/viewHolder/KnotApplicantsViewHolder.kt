package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.ApplicantUserVo
import com.knot.presentation.databinding.RecyclerItemApplicantsListBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.applicants.adapter.KnotApplicantsAdapter

class KnotApplicantsViewHolder(
    private val binding: RecyclerItemApplicantsListBinding,
    private val listener : KnotApplicantsAdapter.KnotApplicantDelegate
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var uid: String

    init {
        binding.apply {
            constraintLayoutUser.setOnClickListener {
                listener.onClickCard(uid)
            }
        }
    }

    fun bind(item : ApplicantUserVo) {
        uid = item.user.uid
        binding.apply {
            textViewName.text = item.user.name
            textViewMajor.text = item.user.major
            textViewIntroduce.text = item.moreInfo
        }
    }
}