package com.gane.smartcheck

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.gane.smartcheck.bean.ProductBean
import com.gane.smartcheck.db.RoomDb
import kotlinx.android.synthetic.main.fm_his.*
import kotlinx.android.synthetic.main.fm_his2.*
import kotlinx.android.synthetic.main.ui_history.*

/**
 * 历史提交
 */
class HistoryActivity : AppCompatActivity() {


    var items = LinkedHashMap<String, ArrayList<ProductBean>>()
    var alls = LinkedHashMap<String, ProductBean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_history)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val checkNo = intent.getStringExtra("id")

        val list1 = RoomDb.getInstance(applicationContext).productDao().getProductList(checkNo)
        val list2 = RoomDb.getInstance(applicationContext).productDao().getProductList(checkNo)

        if (null == list1 || list1.isEmpty()) {
            return
        }
        ll_content.visibility = View.VISIBLE
        tv_data.visibility = View.GONE

//        var items = HashMap<String, ArrayList<ProductBean>>()
//        var alls = HashMap<String, ProductBean>()

        for (i in 0 until list1.size) {
            val it = list1[i]
            val keyNo = it.factcheckno


            if (items.containsKey(keyNo)) {
                items[keyNo]!!.add(it)
            } else {
                items.put(keyNo, arrayListOf(it))
            }

//            val keyBarcode = it.barcode!!
//            if (alls.containsKey(keyBarcode)) {
//                val oldCount = alls[keyBarcode]!!.count
//                val newCount = it.count
//                alls[keyBarcode]!!.count = oldCount + newCount
//            } else {
//                alls.put(keyBarcode, it)
//            }

        }

        for (i in 0 until list2.size) {
            val it = list2[i]
//            val keyNo = it.factcheckno
//
//
//            if (items.containsKey(keyNo)) {
//                items[keyNo]!!.add(it)
//            } else {
//                items.put(keyNo, arrayListOf(it))
//            }

            val keyBarcode = it.barcode!!
            if (alls.containsKey(keyBarcode)) {
//                val oldCount = alls[keyBarcode]!!.count
//                val newCount = it.count
                val pro = alls[keyBarcode]
                pro!!.count += it.count
                alls.put(keyBarcode, pro!!)
            } else {
                alls.put(keyBarcode, it)
            }

        }

        vp_his.adapter = VPAdapter(supportFragmentManager)
        tab_layout.setupWithViewPager(vp_his)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

     inner class VPAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        private val fragments = arrayOf(
                His1Fragment(items), His2Fragment(alls)
        )


        override fun getItem(position: Int) = fragments[position]

        override fun getCount() = fragments.size

        override fun getItemPosition(`object`: Any?): Int {
            return PagerAdapter.POSITION_NONE
        }

         override fun getPageTitle(position: Int): CharSequence {
             return arrayOf("批次", "全部")[position]
         }


    }


}


@SuppressLint("ValidFragment")
class His1Fragment(val data: LinkedHashMap<String, ArrayList<ProductBean>>) : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_his, container, false)
    }


    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_list.layoutManager = LinearLayoutManager(context)
        rv_list.adapter = His1Adapter(data)
    }


}

@SuppressLint("ValidFragment")
class His2Fragment(val data: HashMap<String, ProductBean>) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fm_his2, null)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        text1.text = "共提交 ${data.size} 批次"
//        layout.removeAllViews()
//        for ((_, v) in data) {
//            text2.text = v.opdate
//            val itView = LayoutInflater.from(context).inflate(R.layout.adapter_his_item, null)
//            val text1 = itView.findViewById<TextView>(R.id.text1)
//            val text2 = itView.findViewById<TextView>(R.id.text2)
//            text1.text = v.goodsname
//            text2.text = v.count.toString()
//            layout.addView(itView)
//        }
    }

}

class His1Adapter(val data: LinkedHashMap<String, ArrayList<ProductBean>>): RecyclerView.Adapter<VH>() {

    val keyList = ArrayList<String>()

    init {
        for ((k, _) in data) {
            keyList.add(k)
        }
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val it = data[keyList[position]]!!
        holder.text1.text = it[0]!!.factcheckno
        holder.text2.text = it[0]!!.opdate
        holder.layout.removeAllViews()
        for (i in 0 until it.size) {
            val itView = LayoutInflater.from(holder.itemView.context).inflate(R.layout.adapter_his_item, null)
            val text1 = itView.findViewById<TextView>(R.id.text1)
            val text2 = itView.findViewById<TextView>(R.id.text2)
            text1.text = it[i].goodsname
            text2.text = it[i].count.toString()
            holder.layout.addView(itView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(parent.context).inflate(R.layout.adapter_his, parent, false))
    }

    override fun getItemCount() = data.size


}


class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val text1 = itemView.findViewById<TextView>(R.id.text1)
    val text2 = itemView.findViewById<TextView>(R.id.text2)
    val layout = itemView.findViewById<LinearLayout>(R.id.layout)
}