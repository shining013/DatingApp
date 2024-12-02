package com.autoever.jamanchu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.jamanchu.R
import com.autoever.jamanchu.api.RetrofitInstance
import com.autoever.jamanchu.models.Line
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LineFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LineAdapter
    private val lines = mutableListOf<Line>()
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_line, container, false)

            // 리사이클러뷰
            recyclerView = view.findViewById(R.id.recyclerView)

            adapter = LineAdapter(lines)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            fetchLines()
            return view
        }

        fun fetchLines() {
            // 비동기 실행
            lifecycleScope.launch {
                try {
                    val response = RetrofitInstance.api.getLines()
                    if (response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            lines.clear()
                            lines.addAll(response.body()!!)
                            adapter.notifyDataSetChanged()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
}

class LineAdapter(
    private val lines: List<Line>
) : RecyclerView.Adapter<LineAdapter.LineViewHolder>() {
    class LineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LineViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_line, parent, false)
        return LineViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lines.size
    }

    override fun onBindViewHolder(holder: LineViewHolder, position: Int) {
        val line = lines[position]
        holder.textView.text = line.line
    }
}

