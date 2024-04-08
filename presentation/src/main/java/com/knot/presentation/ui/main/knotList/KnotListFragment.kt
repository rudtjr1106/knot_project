package com.knot.presentation.ui.main.knotList

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.flexbox.FlexboxLayoutManager
import com.knot.domain.vo.CategoryVo
import com.knot.domain.vo.SearchKnotRequest
import com.knot.presentation.PageState
import com.knot.presentation.R
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotListBinding
import com.knot.presentation.databinding.FragmentMainBinding
import com.knot.presentation.ui.main.createOrEditKnot.adapter.CategoryAdapter
import com.knot.presentation.ui.main.knotMain.MainViewModel
import com.knot.presentation.ui.main.knotMain.participatingKnot.adapter.KnotListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class KnotListFragment : BaseFragment<FragmentKnotListBinding, KnotListPageState, KnotListViewModel>(
    FragmentKnotListBinding::inflate
) {

    override val viewModel: KnotListViewModel by viewModels()

    private val categoryAdapter : CategoryAdapter by lazy {
        CategoryAdapter(object : CategoryAdapter.CategoryDelegate{
            override fun onClickCategory(categoryVo: CategoryVo) {
                viewModel.onClickCategory(categoryVo)
            }
        })
    }

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

            recyclerViewCategory.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = categoryAdapter
            }

            recyclerViewKnotList.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = knotListAdapter
            }
            initCategory()
            viewModel.getKnotList(SearchKnotRequest())
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.categoryList.collect {
                    categoryAdapter.submitList(it)
                }
            }
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

    private fun initCategory(){
        viewModel.updateEmptyCategoryList(resources.getStringArray(R.array.knot_category).toList())
    }

    override fun onStart() {
        super.onStart()
    }
}