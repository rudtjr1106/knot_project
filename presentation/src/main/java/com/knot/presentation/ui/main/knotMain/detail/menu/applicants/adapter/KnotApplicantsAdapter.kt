package com.knot.presentation.ui.main.knotMain.detail.menu.applicants.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.knot.domain.vo.ApplicantUserVo
import com.knot.presentation.databinding.RecyclerItemApplicantsListBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.applicants.viewHolder.KnotApplicantsViewHolder

class KnotApplicantsAdapter(
    private val listener : KnotApplicantDelegate
) : ListAdapter<ApplicantUserVo, RecyclerView.ViewHolder>(KnotApplicantDiffCallBack()) {

    interface KnotApplicantDelegate{
        fun onClickCard(uid: String)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is KnotApplicantsViewHolder -> holder.bind(currentList[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RecyclerItemApplicantsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return KnotApplicantsViewHolder(binding, listener)
    }
}

class KnotApplicantDiffCallBack : DiffUtil.ItemCallback<ApplicantUserVo>() {
    override fun areItemsTheSame(oldItem: ApplicantUserVo, newItem: ApplicantUserVo): Boolean = oldItem.user.id == newItem.user.id
    override fun areContentsTheSame(oldItem: ApplicantUserVo, newItem: ApplicantUserVo): Boolean = oldItem == newItem
}