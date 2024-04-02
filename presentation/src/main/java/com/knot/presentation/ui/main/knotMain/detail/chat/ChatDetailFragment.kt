package com.knot.presentation.ui.main.knotMain.detail.chat

import android.graphics.Rect
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knot.presentation.PageState
import com.knot.presentation.base.BaseFragment
import com.knot.presentation.databinding.FragmentKnotChatDetailBinding
import com.knot.presentation.ui.main.knotMain.detail.chat.adapter.ChatAdapter
import com.knot.presentation.util.KnotLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatDetailFragment : BaseFragment<FragmentKnotChatDetailBinding, ChatDetailPageState, ChatDetailViewModel>(
    FragmentKnotChatDetailBinding::inflate
) {

    private val chatDetailFragmentArgs : ChatDetailFragmentArgs by navArgs()

    override val viewModel: ChatDetailViewModel by viewModels()

    private val chatAdapter : ChatAdapter by lazy { ChatAdapter() }

    private var isOpen = false // 키보드 올라왔는지 확인

    override fun initView() {
        binding.apply {
            vm = viewModel

            recyclerViewChat.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = chatAdapter
                itemAnimator = null

                addOnLayoutChangeListener(onLayoutChangeListener)
                viewTreeObserver.addOnScrollChangedListener {
                    if (isScrollable() && !isOpen) {
                        setStackFromEnd()
                        removeOnLayoutChangeListener(onLayoutChangeListener)
                    }
                }
            }
            viewModel.getDetail(chatDetailFragmentArgs.knotId)
        }
    }

    override fun initStates() {
        super.initStates()

        repeatOnStarted(viewLifecycleOwner) {
            launch {
                viewModel.uiState.knotDetail.collect{
                    updateTeamCount(it.teamList.size)
                }
            }
            launch {
                viewModel.uiState.chatList.collect{
                    chatAdapter.submitList(it)
                    if(chatAdapter.itemCount != 0){
                        binding.recyclerViewChat.smoothScrollToPosition(chatAdapter.itemCount)
                    }
                }
            }
            launch {
                viewModel.eventFlow.collect {
                    inspectEvent(it as ChatDetailEvent)
                }
            }
        }
    }

    private fun inspectEvent(event: ChatDetailEvent){
        when(event){
            ChatDetailEvent.ScrollDownEvent -> {

            }
        }
    }

    private val onLayoutChangeListener =
        View.OnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
            if (bottom < oldBottom) {
                binding.recyclerViewChat.scrollBy(0, oldBottom - bottom) // 스크롤 유지를 위해 추가
            }
        }

    fun RecyclerView.isScrollable(): Boolean {
        return canScrollVertically(1) || canScrollVertically(-1)
    }

    fun RecyclerView.setStackFromEnd() {
        (layoutManager as? LinearLayoutManager)?.stackFromEnd = true
    }

    private fun updateTeamCount(size : Int){
        binding.textViewParticipatePeople.text = size.toString()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        viewModel.outChatRoom(chatDetailFragmentArgs.knotId)
    }
}