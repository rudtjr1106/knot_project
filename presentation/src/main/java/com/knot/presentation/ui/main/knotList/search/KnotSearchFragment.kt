package com.knot.presentation.ui.main.knotList.search

import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotSearchBinding
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
            override fun onClickKnot(id: String) {
                //goToKnotDetail(id)
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

    override fun onStart() {
        super.onStart()
    }
}