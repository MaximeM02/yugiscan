package com.improcorp.myapplication.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.improcorp.myapplication.adapter.CardsDetailsAdapter
import com.improcorp.myapplication.databinding.FragmentCardDetailsBinding
import com.improcorp.myapplication.model.Edition
import com.improcorp.myapplication.tool.MyRepository

class CardDetailsDialogFragment(
    private val mEdition: Edition,
    private val mCardPosition: Int,
    private val mFragmentDialogListener: FragmentDialogListener
) : DialogFragment() {

    private lateinit var binding: FragmentCardDetailsBinding

    private lateinit var gridLayoutManager: LinearLayoutManager
    private lateinit var cardsDetailsAdapter: CardsDetailsAdapter

    private lateinit var repo: MyRepository

    private lateinit var mContext: FragmentActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        repo = MyRepository(mContext)

        binding = FragmentCardDetailsBinding.inflate(inflater, container, false)

        cardsDetailsAdapter = CardsDetailsAdapter(mContext, mEdition, repo)
        gridLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        gridLayoutManager.scrollToPosition(mCardPosition)
        val snapHelper: SnapHelper = PagerSnapHelper()

        binding.fragmentCardDetailsRecyclerview.layoutManager = gridLayoutManager
        binding.fragmentCardDetailsRecyclerview.setHasFixedSize(true)

        binding.fragmentCardDetailsRecyclerview.adapter = cardsDetailsAdapter

        snapHelper.attachToRecyclerView(binding.fragmentCardDetailsRecyclerview)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context as FragmentActivity
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mFragmentDialogListener.handleDialogClose(mEdition)
    }
}