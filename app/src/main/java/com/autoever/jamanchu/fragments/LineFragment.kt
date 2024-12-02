package com.autoever.jamanchu.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.autoever.jamanchu.R
import com.autoever.jamanchu.models.Line

class LineFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LineAdapter
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            val view = inflater.inflate(R.layout.fragment_line, container, false)

            // 리사이클러뷰
            recyclerView = view.findViewById(R.id.recyclerView)

            // 라인 리스트 더미 데이터
            val lines = listOf(
                Line(id = "1", user="Alice", line = "Hello, how are you?"),
                Line(id = "2", user = "Bob", line = "I'm doing great, thanks!"),
                Line(id = "3", user = "Charlie", line = "Let's catch up later.")
            )

            adapter = LineAdapter(lines)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            return view
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

