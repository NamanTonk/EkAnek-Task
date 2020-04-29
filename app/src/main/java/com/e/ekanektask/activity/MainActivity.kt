package com.e.ekanektask.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.e.ekanektask.R
import com.e.ekanektask.Utils.EndlessRecyclerViewScrollListener
import com.e.ekanektask.Utils.RetroCall
import com.e.ekanektask.Utils.Prefrences
import com.e.ekanektask.adapter.RecyclerViewAdapter
import com.e.ekanektask.model.ImagesModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.search_view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class MainActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        var set = HashSet<String>()
        fun disableForFiveSecond(view: View) {
            this?.let {
                if (view.isEnabled) {
                    view.isEnabled = false
                    android.os.Handler().postDelayed({
                        view.isEnabled = true
                    }, 5000)
                }
            }

        }
    }

    var adapter: RecyclerViewAdapter? = null
    var list = ArrayList<ImagesModel.HitsBean>()
    var dialog: AlertDialog? = null
    var setPage = 1
    var arrayAdapter: ArrayAdapter<String>? = null
    var quiryChange = false
    var prefrences: Prefrences? = null
    var searchKey = "MakeUp"
    var manager: GridLayoutManager? = null
    var gridList = arrayOf("5 Grid", "4 Grid", "3 Grid", "2 Grid")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeView()
        apiCall()
    }

    /** APi Calling Method**/
    private fun apiCall() {
        Log.d("Naman", setPage.toString())

        RetroCall.CallAPi.getImages(searchKey, true, setPage).enqueue(object : Callback<ImagesModel> {
            override fun onFailure(call: Call<ImagesModel>, t: Throwable) {
                Log.d("Naman", t.message)
            }

            override fun onResponse(call: Call<ImagesModel>, response: Response<ImagesModel>) {
                if (response?.code() == 200) {
                    response?.let {
                        it.body()?.let { body ->
                            if (body.hits.size == 0) {
                                nodataFound()
                                return
                            }
                            if (quiryChange) {
                                list.clear()
                                quiryChange = false
                            }
                            list?.addAll(body.hits)
                            adapter?.notifyDataSetChanged()
                            loaderHide()

                        }
                    }

                }


            }

        })

    }

    /** No data Available When Data Not Show **/
    private fun nodataFound() {
        no_data_found?.text = "'$searchKey'\n NO Data Found at this Quirt "
        recyclerView?.visibility = View.GONE
        no_data_found?.visibility = View.VISIBLE
        loader_show?.visibility = View.GONE

    }

    /** Progress Bar Show**/
    private fun loaderShow() {
        loader_show.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        no_data_found.visibility = View.GONE
    }

    /** Progress Bar Hide**/
    private fun loaderHide() {
        loader_show.visibility = View.GONE
        no_data_found.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    /** Initialize View's **/

    private fun initializeView() {
        supportActionBar?.hide()
        gridDialogSetup()
        prefrences = Prefrences.getInstance(this@MainActivity)
        prefrences?.getData()?.let { set.addAll(it) }
        search_image?.setOnClickListener(this)
        search?.setOnClickListener(this)
        change_grid?.setOnClickListener(this)
        recyclerViewSetup(2, list)
        searchShow()

    }

    private fun searchShow() {
        arrayAdapter = ArrayAdapter<String>(
            this@MainActivity,
            android.R.layout.simple_dropdown_item_1line,
            ArrayList(set)
        )
        search_text?.setAdapter(arrayAdapter)

    }

    /** Grid Dialog Setup After Click**/
    private fun gridDialogSetup() {
        var dialog = AlertDialog.Builder(this@MainActivity)
        var checkItem = 3
        dialog?.setTitle("Please Select Title")
        dialog.setSingleChoiceItems(gridList, checkItem) { dialog, which ->
            when (which) {

                0 -> {
                    recyclerViewSetup(5, list)
                    dialog.dismiss()

                }
                1 -> {
                    recyclerViewSetup(4, list)
                    dialog.dismiss()
                }
                2 -> {
                    recyclerViewSetup(3, list)
                    dialog.dismiss()
                }
                3 -> {
                    recyclerViewSetup(2, list)
                    dialog.dismiss()
                }
            }
        }
        this?.dialog = dialog?.create()
    }

    /** List Setup**/
    private fun recyclerViewSetup(
        gridCound: Int,
        hits: ArrayList<ImagesModel.HitsBean>
    ) {
        manager = GridLayoutManager(this@MainActivity, gridCound)
        recyclerView.layoutManager = manager
        adapter = RecyclerViewAdapter(hits)
        recyclerView?.adapter = adapter
        recyclerView?.addOnScrollListener(object : EndlessRecyclerViewScrollListener(manager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                setPage++
                apiCall()
            }

        })


    }

    /**Activity OnClick**/
    override fun onClick(v: View?) {

        when (v) {
            search_image -> {
                searchView.visibility = View.VISIBLE
                toolbar.visibility = View.GONE
            }
            change_grid -> {
                dialog?.show()
            }
            search -> {
                var searchQuiry = search_text?.text?.toString()
                if (searchQuiry?.isNotEmpty() == true) {
                    searchKey = searchQuiry.trim()
                    apiCall()
                    quiryChange = true
                    disableForFiveSecond(
                        search
                    )
                    loaderShow()
                    set.add(searchQuiry)
                    Log.d("NamanSEarch", set.size.toString())
                    searchShow()
                    hideKeyboard()
                    search_text?.setText("")
                } else {
                    Toast.makeText(this@MainActivity, "Please Enter SomeText.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    /** Hide keyboard**/
    private fun hideKeyboard() {
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(main_layout.windowToken, 0)
    }

    override fun onPause() {
        super.onPause()
        prefrences?.saveData(set)

    }

    override fun onDestroy() {
        super.onDestroy()
        prefrences?.saveData(set)
    }

    override fun onBackPressed() {
        if (toolbar.visibility == View.GONE) {
            toolbar.visibility = View.VISIBLE
            searchView.visibility = View.GONE

        } else super.onBackPressed()
    }
}
