package com.example.minipro3

import android.os.Bundle
import android.util.Log
//import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minipro3.Adapters.OffersAdapter
import com.example.minipro3.Models.ModelOffers
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_message.*

class MessageFragment : Fragment() {

    private val fStore = FirebaseFirestore.getInstance()
    lateinit var recylerView: RecyclerView
    lateinit var offersAdapter: OffersAdapter
    private val mArrayList : ArrayList<ModelOffers> = ArrayList()
    private val adminID = "KKpL3gQiy8Ps7x5CdobOn5uxTGl1"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_message, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recylerView = view.findViewById(R.id.offers_recycler_view)
        recylerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(context)
        recylerView.layoutManager = layoutManager
        offersAdapter = OffersAdapter(requireActivity().applicationContext, mArrayList)
        recylerView.adapter = offersAdapter

        val ref = fStore.collection("MagicMealAdmin").document(adminID).collection("FoodOffers")

        ref.get().addOnSuccessListener {
            if (it.isEmpty){
                no_offers_added_yet.visibility = View.VISIBLE
            }
            else{
                ref.addSnapshotListener { snap, e ->
                    if (snap!=null){
                        for (i in snap.documentChanges){
                            if (i.type == DocumentChange.Type.ADDED){
                                if (i.document.exists()){
                                    try {
                                        val types = ModelOffers(
                                            i.document.getString("foodcategory")!!,
                                            i.document.getString("foodname")!!,
                                            i.document.getString("foodoffer")!!,
                                            i.document.getString("foodprice")!!
                                        )
                                        mArrayList.add(types)
                                    }catch (e: Exception){
                                        e.printStackTrace()
                                        Log.d("e", e.message.toString().trim())
                                    }
                                }
                            }
                        }
                        offersAdapter.update(mArrayList)
                    }
                }
            }
        }
    }
}
