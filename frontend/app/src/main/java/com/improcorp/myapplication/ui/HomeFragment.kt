package com.improcorp.myapplication.ui

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.improcorp.myapplication.R
import com.improcorp.myapplication.adapter.CardsAdapter
import com.improcorp.myapplication.adapter.EditionsAdapter
import com.improcorp.myapplication.adapter.MarginItemDecoration
import com.improcorp.myapplication.databinding.FragmentHomeBinding
import com.improcorp.myapplication.model.Card
import com.improcorp.myapplication.model.Edition
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.viewmodel.HomeViewModel

class HomeFragment : Fragment(), EditionsAdapter.EditionsAdapterListener, CardsAdapter.CardsAdapterListener, FragmentDialogListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    private var mIsSortOrderAscending = false

    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var editionsAdapter: EditionsAdapter

    private var mSelectedEditionPosition: Int = -1

    private lateinit var mContext: FragmentActivity
    private lateinit var repo: MyRepository

    private lateinit var mMenu: Menu

    companion object{
        private const val DESC = "DESC"
        private const val ASC = "ASC"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        repo = MyRepository(mContext)

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

        gridLayoutManager = GridLayoutManager(mContext, 1, GridLayoutManager.VERTICAL, false)

        binding.fragmentHomeRecyclerview.layoutManager = gridLayoutManager
        binding.fragmentHomeRecyclerview.setHasFixedSize(true)
        binding.fragmentHomeRecyclerview.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.grid_margin)))

        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupObservable()

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main, menu)
        mMenu = menu
    }

    private fun setupObservable(){
        homeViewModel.loadEditions(repo, object: HomeViewModel.EditionsCallback{
            override fun onError(message: String) {
                Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
            }
        })

        homeViewModel.getEditions().observe(viewLifecycleOwner, Observer {
            editionsAdapter = EditionsAdapter(mContext, it, this, this)
            binding.fragmentHomeRecyclerview.adapter = editionsAdapter
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sortOrder = if(mIsSortOrderAscending) ASC else DESC
        when (item.itemId) {
            R.id.search -> {
                val searchView = item.actionView as SearchView
                searchView.queryHint = getString(R.string.fragment_edition_search_hit)

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(p0: String?): Boolean = false

                    override fun onQueryTextChange(p0: String?): Boolean {
                        editionsAdapter.filter.filter(p0)
                        return false
                    }
                })
                return true
            }
            R.id.group_sort_date -> {
                item.isChecked = true
                editionsAdapter.sortByDate(sortOrder)
            }
            R.id.group_sort_name -> {
                item.isChecked = true
                editionsAdapter.sortByName(sortOrder)
            }
            R.id.group_sort_completion -> {
                item.isChecked = true
                editionsAdapter.sortByCompletion(sortOrder)
            }
            R.id.group_order_asc -> {
                item.isChecked = true
                mIsSortOrderAscending = true
                applySortingOnCheckedItem()
            }
            R.id.group_order_desc -> {
                item.isChecked = true
                mIsSortOrderAscending = false
                applySortingOnCheckedItem()
            }
        }
        return false
    }

    private fun applySortingOnCheckedItem(){
        when {
            mMenu.findItem(R.id.group_sort_date).isChecked -> {
                onOptionsItemSelected(mMenu.findItem(R.id.group_sort_date))
            }
            mMenu.findItem(R.id.group_sort_name).isChecked -> {
                onOptionsItemSelected(mMenu.findItem(R.id.group_sort_name))
            }
            mMenu.findItem(R.id.group_sort_completion).isChecked -> {
                onOptionsItemSelected(mMenu.findItem(R.id.group_sort_completion))
            }
        }
    }

    override fun onEditionSelected(position: Int) {
        val isCardsLoaded: Boolean = editionsAdapter.mEditionList[position].mIsCardsLoaded

        val ithChildViewHolder: EditionsAdapter.ItemHolder = binding.fragmentHomeRecyclerview.findViewHolderForAdapterPosition(position) as EditionsAdapter.ItemHolder
        val childRecyclerViewAdapter: CardsAdapter = ithChildViewHolder.recyclerView.adapter as CardsAdapter

        if(isCardsLoaded){
            ithChildViewHolder.expandableLayout.visibility = if(ithChildViewHolder.expandableLayout.visibility == View.GONE) View.VISIBLE else View.GONE
        } else{
            homeViewModel.loadCardsInEdition(repo, editionsAdapter.mEditionList[position].mId, object: HomeViewModel.CardsInEditionCallback{
                override fun onSuccess(message: String, cardList: ArrayList<Card>) {
                    childRecyclerViewAdapter.updateCardsListFromEdition(cardList)
                    editionsAdapter.mEditionList[position].mIsCardsLoaded = true

                    ithChildViewHolder.expandableLayout.visibility = View.VISIBLE
                }

                override fun onError(message: String) {
                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onCardSelected(editionPosition: Int, cardPosition: Int) {
        mSelectedEditionPosition = editionPosition

        CardDetailsDialogFragment(
            editionsAdapter.mEditionList[mSelectedEditionPosition],
            cardPosition,
            this
        ).show(this.parentFragmentManager, "Details")
    }

    override fun handleDialogClose(edition: Edition) {
        val ithChildViewHolder: EditionsAdapter.ItemHolder = binding.fragmentHomeRecyclerview.findViewHolderForAdapterPosition(mSelectedEditionPosition) as EditionsAdapter.ItemHolder

        editionsAdapter.mEditionList[mSelectedEditionPosition] = edition
        (ithChildViewHolder.recyclerView.adapter as CardsAdapter).mCardList = edition.mCardList

        editionsAdapter.notifyDataSetChanged()
    }
}