package com.improcorp.myapplication.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.improcorp.myapplication.R
import com.improcorp.myapplication.model.Edition
import java.util.*
import kotlin.collections.ArrayList

class EditionsAdapter(
    private var mContext: Context,
    var mEditionList: ArrayList<Edition>,
    private var mEditionListener: EditionsAdapterListener,
    private var mCardListener: CardsAdapter.CardsAdapterListener
    ) :RecyclerView.Adapter<EditionsAdapter.ItemHolder>(), Filterable {

    companion object{
        const val DESC = "DESC"
        const val ASC = "ASC"
    }

    private val mEditionReferenceList: ArrayList<Edition> = ArrayList(mEditionList)

    class ItemHolder(
            itemView: View,
            onEditionListener: EditionsAdapterListener
    ): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        var label: TextView = itemView.findViewById(R.id.cd_edition_label_txt)
        var code: TextView = itemView.findViewById(R.id.cd_edition_code_date_txt)
        var name: TextView = itemView.findViewById(R.id.cd_edition_name_txt)
        var numberOfCards: TextView = itemView.findViewById(R.id.cd_edition_cards_txt)
        var completion: TextView = itemView.findViewById(R.id.cd_edition_progress_txt)
        var progressBar: ProgressBar = itemView.findViewById(R.id.cd_edition_progress_bar)
        var searchview: SearchView = itemView.findViewById(R.id.cd_edition_search_view)
        var recyclerView: RecyclerView = itemView.findViewById(R.id.cd_edition_recyclerview)

        var expandableLayout: ConstraintLayout = itemView.findViewById(R.id.cd_edition_expandable_layout)

        private var headerLayout: ConstraintLayout = itemView.findViewById(R.id.cd_edition_header_layout)

        private var onEditionListener: EditionsAdapterListener

        init {
            this.headerLayout.setOnClickListener(this)
            this.onEditionListener = onEditionListener
        }

        override fun onClick(v: View?) {
            when(v){
                headerLayout -> { this.onEditionListener.onEditionSelected(bindingAdapterPosition) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val itemHolder = LayoutInflater.from(mContext).inflate(R.layout.cardview_edition, parent, false)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        itemHolder.layoutParams = params

        return ItemHolder(itemHolder, mEditionListener)
    }

    override fun getItemCount(): Int = mEditionList.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getItemViewType(position: Int): Int = position

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val edition: Edition = mEditionList[position]

        edition.calculateCompletion()

        holder.label.text = edition.mCategory
        holder.code.text = StringBuilder().append(edition.mCode).append(" - ").append(edition.mReleaseDate)
        holder.name.text = edition.mName
        holder.numberOfCards.text = StringBuilder().append(edition.mUserNumberOfCards).append(" / ").append(edition.mTotalNumberOfCards)
        holder.completion.text = StringBuilder().append(edition.mCompletion).append("%")
        holder.progressBar.progress = edition.mCompletion.toInt()

        holder.recyclerView.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        val cardsAdapter = CardsAdapter(holder.recyclerView.context, position, edition.mCardList, mCardListener)

        holder.recyclerView.adapter = cardsAdapter

        holder.searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean = false

            override fun onQueryTextChange(p0: String?): Boolean {
                cardsAdapter.filter.filter(p0)
                return false
            }
        })
    }

    fun sortByName(order: String) {
        val sorted: Unit? = when (order){
            ASC -> mEditionList.sortBy { it.mName }
            DESC -> mEditionList.sortByDescending { it.mName }
            else -> null
        }

        if (sorted != null) notifyDataSetChanged()
    }

    fun sortByDate(order: String) {
        val sorted: Unit? = when (order){
            ASC -> mEditionList.sortBy { it.mReleaseDate }
            DESC -> mEditionList.sortByDescending { it.mReleaseDate }
            else -> null
        }

        if (sorted != null) notifyDataSetChanged()
    }

    fun sortByCompletion(order: String) {
        val sorted: Unit? = when (order){
            ASC -> mEditionList.sortBy { it.mCompletion }
            DESC -> mEditionList.sortByDescending { it.mCompletion }
            else -> null
        }

        if (sorted != null) notifyDataSetChanged()
    }

    interface EditionsAdapterListener {
        fun onEditionSelected(position: Int)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val filterResults = FilterResults()
                val charString = charSequence.toString()
                mEditionList = if (charString.isEmpty()) {
                    mEditionReferenceList
                } else {
                    val filteredList = ArrayList<Edition>()
                    for (row in mEditionReferenceList) {
                        if (row.mName.toLowerCase(Locale.ROOT)
                                        .contains(charString.toLowerCase(Locale.ROOT)) || row.mCode.toLowerCase(Locale.ROOT)
                                        .contains(charString.toLowerCase(Locale.ROOT))
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                filterResults.values = mEditionList
                filterResults.count = mEditionList.size
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                mEditionList = filterResults.values as ArrayList<Edition>

                notifyDataSetChanged()
            }
        }
    }
}