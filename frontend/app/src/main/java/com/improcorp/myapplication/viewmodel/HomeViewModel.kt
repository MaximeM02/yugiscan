package com.improcorp.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.improcorp.myapplication.model.Card
import com.improcorp.myapplication.model.Edition
import com.improcorp.myapplication.tool.MyRepository
import com.improcorp.myapplication.tool.MyRequest
import org.json.JSONArray

class HomeViewModel : BaseViewModel() {

    private val _editions = MutableLiveData<ArrayList<Edition>>().apply { value = ArrayList() }
    private val _cardsInEdition = MutableLiveData<ArrayList<Card>>().apply { value = ArrayList() }

    fun getEditions(): LiveData<ArrayList<Edition>> = _editions

    fun loadEditions(repo: MyRepository, callback: EditionsCallback){
        repo.getEditions(object: MyRequest.EditionsCallback{
            override fun onSuccess(editionsArray: JSONArray) {
                val editionList: ArrayList<Edition> = ArrayList()
                for (i in 0 until editionsArray.length()) {
                    val item = editionsArray.getJSONObject(i)

                    val cardsList: ArrayList<Card> = ArrayList()

                    editionList.add(Edition(
                            mId = item.getInt("e_id"),
                            mCode = item.getString("e_code"),
                            mName = item.getString("e_name"),
                            mReleaseDate = item.getString("e_release_date"),
                            mTotalNumberOfCards = item.getInt("number_of_cards"),
                            mImageUrl = item.getString("e_img_url"),
                            mCategory = item.getString("ec_name"),
                            mUserNumberOfCards = item.getInt("uc_user_number_of_cards"),
                            mCompletion = -1.0,
                            mIsCardsLoaded = false,
                            mCardList = cardsList))
                }
                _editions.value = editionList
            }

            override fun onError(message: String) = callback.onError(message)

            override fun inputErrors(errorHashMap: HashMap<String, String>) {
                val builder = StringBuilder()

                for(item in errorHashMap){
                    builder.append(item.value).append("\n")
                }

                callback.onError(builder.toString().trim())
            }
        })
    }

    fun loadCardsInEdition(repo: MyRepository, editionId: Int, callback: CardsInEditionCallback){
        repo.getCardsInEdition(editionId, object: MyRequest.CardsInEditionCallback{
            override fun onSuccess(cardsInEditionArray: JSONArray) {
                val cardInEditionList: ArrayList<Card> = ArrayList()
                for (i in 0 until cardsInEditionArray.length()) {
                    val item = cardsInEditionArray.getJSONObject(i)

                    val dateInsert = if (item.has("uc_date_insert") && !item.isNull("uc_date_insert")) item.getString("uc_date_insert") else null
                    val dateUpdate = if (item.has("uc_date_update") && !item.isNull("uc_date_update")) item.getString("uc_date_update") else null

                    cardInEditionList.add(Card(
                            mId =  item.getInt("c_id"),
                            mEdition = item.getString("e_name"),
                            mCode =  item.getString("c_real_code"),
                            mName = item.getString("c_name"),
                            mRarity =  item.getString("c_rarity"),
                            mAttribute =  item.getString("c_attribute"),
                            mLevel =  item.getString("c_level"),
                            mAttack =  item.getString("c_attack"),
                            mDefense = item.getString("c_defense"),
                            mType = item.getString("c_type"),
                            mText = item.getString("c_text"),
                            mTemplateId = item.getInt("c_template_id"),
                            mLinkClassification = item.getString("c_link_classification"),
                            mImageUrl = item.getString("c_img_url"),
                            mQuantityInEdition = if(item.get("c_quantity_in_edition") == null) 0 else item.getInt("c_quantity_in_edition"),
                            mUserQuantity = item.getInt("uc_user_quantity"),
                            mInsertDate =  dateInsert,
                            mUpdateDate =  dateUpdate)
                    )
                }
                _cardsInEdition.value = cardInEditionList

                println(cardInEditionList[0].mInsertDate == null)

                callback.onSuccess("Chargement des cartes réussi !", cardInEditionList)
            }

            override fun onError(message: String) {
                _cardsInEdition.value=ArrayList()
                callback.onError(message)
            }

            override fun inputErrors(errorHashMap: HashMap<String, String>) {
                val builder = StringBuilder()

                for(item in errorHashMap){
                    builder.append(item.value).append("\n")
                }

                _cardsInEdition.value=ArrayList()
                callback.onError(builder.toString().trim())
            }
        })
    }

    interface EditionsCallback{
       fun onError(message: String)
    }

    interface CardsInEditionCallback{
        fun onSuccess(message: String, cardList: ArrayList<Card>)
        fun onError(message: String)
    }
}