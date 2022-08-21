package com.improcorp.myapplication.tool

import android.content.Context
import android.util.Log
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.improcorp.myapplication.model.User
import com.improcorp.myapplication.model.UserCollection
import org.json.JSONArray
import org.json.JSONObject
import java.lang.StringBuilder

class MyRequest(val context: Context, private var queue: RequestQueue) {

    private val server = "http://localhost:5006/"

    fun register(confirmationPassword: String, callback: RegisterCallback){
        val url : String = StringBuilder().append(server).append("registration.php").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    callback.onSuccess("Inscription validée !")
                } else{
                    val messages = json.getJSONObject("message")

                    if(messages.has("pseudo")){
                        errors["pseudo"] = messages.getString("pseudo")
                    }

                    if(messages.has("email_address")){
                        errors["email_address"] = messages.getString("email_address")
                    }

                    if(messages.has("password")){
                        errors["password"] = messages.getString("password")
                    }

                    if(messages.has("last_name")){
                        errors["last_name"] = messages.getString("last_name")
                    }

                    if(messages.has("first_name")){
                        errors["first_name"] = messages.getString("first_name")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
            error -> Log.d("App", error.toString())

            if(error is NetworkError){
                callback.onError("Impossible de se connecter.")
            } else if(error is VolleyError){
                callback.onError("Une erreur s'est produite lors de l'inspection.")
            }
        }){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["pseudo"] = User.mPseudo
                hashMap["password1"] = User.mPassword
                hashMap["email_address"] = User.mEmailAddress
                hashMap["first_name"] = User.mFirstName
                hashMap["last_name"] = User.mLastName
                hashMap["registration_date"] = User.mRegistrationDate
                hashMap["password2"] = confirmationPassword

                return hashMap
            }
        }

        queue.add(request)
    }

    fun login(callback: LoginCallback){
        val url : String = StringBuilder().append(server).append("login").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                    val errors = HashMap<String, String>()

                    val json = JSONObject(response)
                    val isError = json.getBoolean("error")

                    val messages = json.getJSONObject("message")

                    if(!isError){
                        User.mId = messages.getInt("u_id")
                        User.mPseudo = messages.getString("u_pseudo")
                        User.mLastName = messages.getString("u_last_name")
                        User.mFirstName = messages.getString("u_first_name")
                        User.mEmailAddress = messages.getString("u_email_address")
                        User.mPassword = messages.getString("u_password")
                        User.mRegistrationDate = messages.getString("u_registration_date")

                        callback.onSuccess("Bienvenue ${User.mFirstName} !")
                    } else {

                        if (messages.has("post")) {
                            errors["post"] = messages.getString("post")
                        }

                        if (messages.has("empty")) {
                            errors["empty"] = messages.getString("empty")
                        }

                        if (messages.has("pseudo")) {
                            errors["pseudo"] = messages.getString("pseudo")
                        }

                        if (messages.has("password")) {
                            errors["password"] = messages.getString("password")
                        }

                        callback.inputErrors(errors)
                    }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())

                    if(error is NetworkError){
                        callback.onError("Impossible de se connecter.")
                    } else if(error is VolleyError){
                        callback.onError("Une erreur s'est produite lors de la communication avec le serveur.")
                    }
            }
        ){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["pseudo"] = User.mPseudo
                hashMap["password"] = User.mPassword

                return hashMap
            }
        }

        queue.add(request)
    }

    /* 
    fun login(callback: LoginCallback){
        val url : String = StringBuilder().append(server).append("login.php").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                    val errors = HashMap<String, String>()

                    val json = JSONObject(response)
                    val isError = json.getBoolean("error")

                    val messages = json.getJSONObject("message")

                    if(!isError){
                        User.mId = messages.getInt("u_id")
                        User.mPseudo = messages.getString("u_pseudo")
                        User.mLastName = messages.getString("u_last_name")
                        User.mFirstName = messages.getString("u_first_name")
                        User.mEmailAddress = messages.getString("u_email_address")
                        User.mPassword = messages.getString("u_password")
                        User.mRegistrationDate = messages.getString("u_registration_date")

                        callback.onSuccess("Bienvenue ${User.mFirstName} !")
                    } else {

                        if (messages.has("post")) {
                            errors["post"] = messages.getString("post")
                        }

                        if (messages.has("empty")) {
                            errors["empty"] = messages.getString("empty")
                        }

                        if (messages.has("pseudo")) {
                            errors["pseudo"] = messages.getString("pseudo")
                        }

                        if (messages.has("password")) {
                            errors["password"] = messages.getString("password")
                        }

                        callback.inputErrors(errors)
                    }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())

                    if(error is NetworkError){
                        callback.onError("Impossible de se connecter.")
                    } else if(error is VolleyError){
                        callback.onError("Une erreur s'est produite lors de la communication avec le serveur.")
                    }
            }
        ){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["pseudo"] = User.mPseudo
                hashMap["password"] = User.mPassword

                return hashMap
            }
        }

        queue.add(request)
    }
    */

    fun getInitializationList(callback: InitializationCallback){
        val url : String = StringBuilder().append(server).append("init.php").toString()

        val request = object: StringRequest(Method.GET, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    val messages = json.getJSONArray("message")

                    callback.onSuccess(messages)
                } else {
                    val messages = json.getJSONObject("message")

                    if(messages.has("database")){
                        errors["database"] = messages.getString("database")
                    }

                    if(messages.has("sql")){
                        errors["sql"] = messages.getString("sql")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())
                if(error is NoConnectionError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de la lecture des listes d'initialisation.")
                }
            }
        ){}

        queue.add(request)
    }

    fun getEditions(callback: EditionsCallback){
        val url : String = StringBuilder().append(server).append("editions.php").toString()

        val request = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    val messages = json.getJSONArray("message")

                    callback.onSuccess(messages)
                } else {
                    val messages = json.getJSONObject("message")

                    if(messages.has("database")){
                        errors["database"] = messages.getString("database")
                    }

                    if(messages.has("sql")){
                        errors["sql"] = messages.getString("sql")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                error -> Log.d("App", error.toString())
                if(error is NoConnectionError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de la lecture des éditions.")
                }
            }
        ){
            override fun getParams(): MutableMap<String, String> {
                val hashMap = HashMap<String, String>()
                hashMap["user_id"] = User.mId.toString()

                return hashMap
            }
        }

        queue.add(request)
    }

    fun getCardsInEdition(editionId: Int, callback: CardsInEditionCallback){
        val url : String = StringBuilder().append(server).append("cards_in_edition.php").toString()

        val request = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    val messages = json.getJSONArray("message")

                    callback.onSuccess(messages)
                } else{
                    val messages = json.getJSONObject("message")

                    if(messages.has("post")){
                        errors["post"] = messages.getString("post")
                    }

                    if (messages.has("empty")) {
                        errors["empty"] = messages.getString("empty")
                    }

                    if(messages.has("type")){
                        errors["type"] = messages.getString("type")
                    }

                    if(messages.has("database")){
                        errors["database"] = messages.getString("database")
                    }

                    if(messages.has("sql")){
                        errors["sql"] = messages.getString("sql")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                error -> Log.d("App", error.toString())
                if(error is NoConnectionError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de la lecture des éditions.")
                }
            }){
                override fun getParams(): MutableMap<String, String> {
                    val hashMap = HashMap<String, String>()
                    hashMap["user_id"] = User.mId.toString()
                    hashMap["edition_id"] = editionId.toString()

                    return hashMap
                }
        }

        queue.add(request)
    }

    fun addCardUser(userCollection: UserCollection, callback: AddCardUserCallback){
        val url : String = StringBuilder().append(server).append("add_card_user.php").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    callback.onSuccess("Carte ajoutée à la collection !")
                } else{
                    val messages = json.getJSONObject("message")

                    if(messages.has("user_id")){
                        errors["user_id"] = messages.getString("user_id")
                    }

                    if(messages.has("card_id")){
                        errors["card_id"] = messages.getString("card_id")
                    }

                    if(messages.has("quantity")){
                        errors["quantity"] = messages.getString("quantity")
                    }

                    if(messages.has("date_insert")){
                        errors["date_insert"] = messages.getString("date_insert")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())

                if(error is NetworkError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de l'ajout de la carte dans la collection.")
                }
            }){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["user_id"] = userCollection.userId.toString()
                hashMap["card_id"] = userCollection.cardId.toString()
                hashMap["quantity"] = userCollection.quantity.toString()
                hashMap["date_insert"] = userCollection.dateInsert

                return hashMap
            }
        }

        queue.add(request)
    }

    fun updateCardUser(userCollection: UserCollection, callback: UpdateCardUserCallback){
        val url : String = StringBuilder().append(server).append("update_card_user.php").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    callback.onSuccess("Mise à jour de nombre d'exemplaire !")
                } else{
                    val messages = json.getJSONObject("message")

                    if(messages.has("user_id")){
                        errors["user_id"] = messages.getString("user_id")
                    }

                    if(messages.has("card_id")){
                        errors["card_id"] = messages.getString("card_id")
                    }

                    if(messages.has("quantity")){
                        errors["quantity"] = messages.getString("quantity")
                    }

                    if(messages.has("date_update")){
                        errors["date_update"] = messages.getString("date_update")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())

                if(error is NetworkError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de la mise à jour de la carte dans la collection.")
                }
            }){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["user_id"] = userCollection.userId.toString()
                hashMap["card_id"] = userCollection.cardId.toString()
                hashMap["quantity"] = userCollection.quantity.toString()
                hashMap["date_update"] = userCollection.dateUpdate

                return hashMap
            }
        }

        queue.add(request)
    }

    fun deleteCardUser(userCollection: UserCollection, callback: DeleteCardUserCallback){
        val url : String = StringBuilder().append(server).append("delete_card_user.php").toString()

        val request: StringRequest = object: StringRequest(Method.POST, url,
            Response.Listener<String> {
                    response -> Log.d("App", response)

                val errors = HashMap<String, String>()

                val json = JSONObject(response)
                val isError = json.getBoolean("error")

                if(!isError){
                    callback.onSuccess("Supression de la carte de la collection !")
                } else{
                    val messages = json.getJSONObject("message")

                    if(messages.has("user_id")){
                        errors["user_id"] = messages.getString("user_id")
                    }

                    if(messages.has("card_id")){
                        errors["card_id"] = messages.getString("card_id")
                    }

                    callback.inputErrors(errors)
                }

            }, Response.ErrorListener {
                    error -> Log.d("App", error.toString())

                if(error is NetworkError){
                    callback.onError("Impossible de se connecter.")
                } else if(error is VolleyError){
                    callback.onError("Une erreur s'est produite lors de la supression de la carte dans la collection.")
                }
            }){
            override fun getParams(): MutableMap<String, String?> {
                val hashMap = HashMap<String, String?>()
                hashMap["user_id"] = userCollection.userId.toString()
                hashMap["card_id"] = userCollection.cardId.toString()

                return hashMap
            }
        }

        queue.add(request)
    }

    interface RegisterCallback{
        fun onSuccess(message: String)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface LoginCallback{
        fun onSuccess(message: String)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface InitializationCallback{
        fun onSuccess(initArray: JSONArray)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface EditionsCallback{
        fun onSuccess(editionsArray: JSONArray)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface CardsInEditionCallback{
        fun onSuccess(cardsInEditionArray: JSONArray)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface AddCardUserCallback{
        fun onSuccess(message: String)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface UpdateCardUserCallback{
        fun onSuccess(message: String)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }

    interface DeleteCardUserCallback{
        fun onSuccess(message: String)
        fun onError(message: String)
        fun inputErrors(errorHashMap: HashMap<String, String>)
    }
}