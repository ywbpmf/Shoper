package com.gane.shoper.ui.dialog

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.gane.shoper.R
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration

/**
 * 条目
 */
class ClauseSheetDialog(val context: Context, val listener: ClauseSheetListener, val data: List<String>, val choose: String = "", val max: Int = 5) {

    interface ClauseSheetListener {
        fun onClauseSheet(index: Int, text: String)
    }

    private val recyclerView: RecyclerView = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context)
        adapter = ClauseAdapter()
        layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                max * context.resources.getDimensionPixelSize(R.dimen.clause_height))
        addItemDecoration(HorizontalDividerItemDecoration.Builder(context)
                .colorResId(R.color.line_color).sizeResId(R.dimen.line_height).build())
    }

    private val bottomDialog: BottomSheetDialog = BottomSheetDialog(context).apply {
        setContentView(recyclerView)
    }

    fun show() {
        if (bottomDialog.isShowing) return
        bottomDialog.show()
    }

    fun dismiss() {
        bottomDialog.dismiss()
    }

    inner class ClauseAdapter : RecyclerView.Adapter<ClauseVH>() {
        override fun onBindViewHolder(holder: ClauseVH, position: Int) {
            holder.bindTo(data[position])
            holder.onClick(position)
        }

        override fun getItemCount() = data.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ClauseVH(parent)
    }

    inner class ClauseVH(parent: ViewGroup) : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.adapter_clause, parent, false)) {

        private val text = itemView.findViewById<TextView>(R.id.text)
        private val dot = itemView.findViewById<View>(R.id.dot)

        fun bindTo(text: String) {
            this.text.text = text
            dot.visibility = if (choose == text) View.VISIBLE else View.INVISIBLE
        }

        fun onClick(position: Int) {
            itemView.setOnClickListener {
                dismiss()
                listener.onClauseSheet(position, data[position])
            }
        }

    }


}