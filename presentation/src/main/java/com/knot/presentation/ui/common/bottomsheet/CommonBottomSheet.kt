package com.knot.presentation.ui.common.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.knot.presentation.databinding.CommonBottomSheetBinding
import com.knot.presentation.util.serializable
import java.io.Serializable

class CommonBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val KEY_MENU_TYPE = "KEY_MENU_TYPE"
        private const val KEY_SELECTED_MENU_TYPE = "KEY_SELECTED_MENU_TYPE"

        fun newInstance(
            menuList: List<String>,
            selectedMenu: String? = null,
            menuClick: (String) -> Unit
        ) = CommonBottomSheet().apply {
            this.menuClick = menuClick
            arguments = Bundle().apply {
                putSerializable(KEY_MENU_TYPE, menuList as Serializable)
                putSerializable(KEY_SELECTED_MENU_TYPE, selectedMenu)
            }
        }
    }

    private var menuClick: ((String) -> Unit)? = null
    private val menuList: List<String> by lazy {
        arguments?.serializable(KEY_MENU_TYPE) ?: emptyList()
    }
    private val selectedMenu: String? by lazy {
        arguments?.serializable(KEY_SELECTED_MENU_TYPE)
    }

    private val binding: CommonBottomSheetBinding by lazy {
        CommonBottomSheetBinding.inflate(layoutInflater)
    }

    private val listAdapter: KnotBottomSheetAdapter by lazy {
        KnotBottomSheetAdapter(object : KnotBottomSheetAdapter.Delegate {
            override fun onClickMenu(title: String) {
                dismiss()
                menuClick?.invoke(title)
            }

            override val selectedDialogMenuItem: String? = selectedMenu
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.apply {
            isDraggable = false
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.apply {
            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = listAdapter
            }
        }

        listAdapter.submitList(menuList)
    }
}
