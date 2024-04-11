package com.knot.presentation.ui.main.knotList.search

import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotSearchBinding
import com.knot.presentation.ui.main.knotList.KnotListFragmentDirections
import com.knot.presentation.ui.main.knotMain.participatingKnot.adapter.KnotListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotSearchFragment : BaseFragment<FragmentKnotSearchBinding, KnotSearchPageState, KnotSearchViewModel>(
    FragmentKnotSearchBinding::inflate
) {

    override val viewModel: KnotSearchViewModel by viewModels()

    private val knotListAdapter : KnotListAdapter by lazy {
        KnotListAdapter(object : KnotListAdapter.KnotListDelegate{
            override fun goToDetail(id: String) {
                goToKnotDetail(id)
            }

            override fun goToApplication(id: String) {
                goToApplicationKnotDetail(id)
            }
        })
    }

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewKnotList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = knotListAdapter
            }
            bindEditTextKeyboard()
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.knotList.collect{
                    knotListAdapter.submitList(it)
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as KnotSearchEvent)
                }
            }
        }
    }

    private fun bindEditTextKeyboard(){
        binding.apply {
            editTextSearch.apply {
                setOnEditorActionListener { _, keyCode, keyEvent ->
                    if(keyCode == EditorInfo.IME_ACTION_SEARCH) {
                        viewModel.getKnotList()
                        true
                    }else false
                }
            }
        }
    }

    private fun inspectEvent(event: KnotSearchEvent){
        when(event){
            KnotSearchEvent.GoToBackEvent -> findNavController().popBackStack()
        }
    }

    private fun goToKnotDetail(knotId : String){
        val action = KnotSearchFragmentDirections.actionKnotSearchToKnotDetail(knotId)
        findNavController().navigate(action)
    }

    private fun goToApplicationKnotDetail(knotId : String){
        val action = KnotSearchFragmentDirections.actionKnotSearchToKnotApplicationDetail(knotId)
        findNavController().navigate(action)
    }

    override fun onStart() {
        super.onStart()
    }
}