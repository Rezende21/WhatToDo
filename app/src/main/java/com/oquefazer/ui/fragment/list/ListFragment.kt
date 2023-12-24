package com.oquefazer.ui.fragment.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.oquefazer.ui.fragment.viewmodel.WhatsViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.oquefazer.R
import com.oquefazer.adapter.TaskListAdapter
import com.oquefazer.data.local.WhatsToDo
import com.oquefazer.databinding.FragmentListBinding
import com.oquefazer.notification.SetNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListFragment : Fragment(), MenuProvider {

    private lateinit var mAdView : AdView
    private val adapter by lazy { TaskListAdapter {
        updateTaskOtherWay(it)
    } }
    private val binding by lazy { FragmentListBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<WhatsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setipRecycleView()
        setObserver()
        insertListeners()
        getThegoogleMob()
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MobileAds.initialize(requireContext()){}
    }

    private fun getThegoogleMob() {
        mAdView = binding.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }

    private fun updateTaskOtherWay(it: WhatsToDo) {
        val bundle = Bundle()
        bundle.putSerializable("id", it)
        findNavController().navigate(R.id.action_listFragment_to_editTaskFragment, bundle)
    }

    private fun insertListeners() {
        binding.btFloat.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addTaskFragment)
        }

        adapter.listenerDelete = { task ->
            viewModel.deleteTask(task)
            Toast.makeText(requireContext(), "Tarefa deletada", Toast.LENGTH_LONG).show()
        }
    }

    private fun setObserver() {
        viewModel.showAllTasks().observe(viewLifecycleOwner) { task ->
            adapter.submitList(task)
        }
    }

    private fun setipRecycleView() {
        binding.recycleView.adapter = adapter
    }

//    fun showDeleteMenu(show : Boolean) {
//
//    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.popupmenu, menu)
        //menu.setGroupVisible(0, false)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.action_delete -> {
                Toast.makeText(requireContext(), "that's it for now", Toast.LENGTH_LONG).show()
                true
            }
            else -> { true}
        }
    }
}


/*  private fun showPopup(item: WhatsToDo) {
      val ivMore = binding.moreItem
      val popupMenu = PopupMenu(ivMore.context, ivMore)
      popupMenu.menuInflater.inflate(R.menu.popupmenu, popupMenu.menu)
      popupMenu.setOnMenuItemClickListener {
          when (it.itemId) {
              R.id.action_delete -> listenerDelete(item)
              R.id.action_notification -> showNotification(item)
          }
          return@setOnMenuItemClickListener true
      }
      popupMenu.show()
  } */