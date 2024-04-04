package com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.domain.vo.TeamUserVo
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotEditRoleAndRuleBinding
import com.knot.presentation.databinding.FragmentKnotListBinding
import com.knot.presentation.ui.main.knotMain.detail.menu.editRoleAndRule.adapter.KnotRoleAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditRoleAndRuleFragment : BaseFragment<FragmentKnotEditRoleAndRuleBinding, EditRoleAndRulePageState, EditRoleAndRuleViewModel>(
    FragmentKnotEditRoleAndRuleBinding::inflate
) {

    private val editRoleAndRuleFragmentArgs : EditRoleAndRuleFragmentArgs by navArgs()

    override val viewModel: EditRoleAndRuleViewModel by viewModels()

    private val knotRoleAdapter : KnotRoleAdapter by lazy {
        KnotRoleAdapter(object : KnotRoleAdapter.KnotRoleDelegate{
            override fun onChangedRole(teamUserVo: TeamUserVo) {
                viewModel.updateTeamRole(teamUserVo)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewRole.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = knotRoleAdapter
            }

            viewModel.getKnotDetail(editRoleAndRuleFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.teamRoleList.collect{
                    knotRoleAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as EditRoleAndRuleEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: EditRoleAndRuleEvent){
        when(event){
            EditRoleAndRuleEvent.GoToBack -> findNavController().popBackStack()
            EditRoleAndRuleEvent.OnClickEditRule -> focusEditText()
        }
    }

    private fun focusEditText(){
        binding.apply {
            val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            editTextRule.isEnabled = true
            editTextRule.isFocusableInTouchMode = true
            editTextRule.requestFocus()
            inputMethodManager.showSoftInput(editTextRule, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onStart() {
        super.onStart()
    }
}