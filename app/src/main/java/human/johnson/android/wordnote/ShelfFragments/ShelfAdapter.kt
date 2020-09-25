package human.johnson.android.wordnote.ShelfFragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import human.johnson.android.wordnote.MyApplication.Companion.star_flag
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Shelf
import human.johnson.android.wordnote.viewmodel.ShelfViewModel
import kotlinx.android.synthetic.main.shelf_row.view.*

class ShelfAdapter(): RecyclerView.Adapter<ShelfAdapter.MyViewHolder>() {

    private var shelfList = emptyList<Shelf>()
    private var noteNum = 0
    private lateinit var context: Context

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShelfAdapter.MyViewHolder {
        context = parent.context

        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.shelf_row,
                parent,
                false))
    }

    override fun getItemCount(): Int {
        return shelfList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = shelfList[position]
        holder.itemView.subject_txt.text = currentItem.subject
        holder.itemView.note_num.text = currentItem.noteNum.toString()

        holder.itemView.shelf_rowLayout.setOnClickListener {
            star_flag = false
            val action = ShelfFragmentDirections.actionShelfFragmentToNoteCollectionFragment(currentItem.id)
            holder.itemView.findNavController().navigate(action)
        }

        holder.itemView.shelf_edit.setOnClickListener {
            val action = ShelfFragmentDirections.actionShelfFragmentToShelfUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        // set color
        when (currentItem.color) {
            1 ->  {
                holder.itemView.line_shelf.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect1))
            }
            2 ->  {
                holder.itemView.line_shelf.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect2))
            }
            3 ->  {
                holder.itemView.line_shelf.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect3))
            }
            4 ->  {
                holder.itemView.line_shelf.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect4))
            }
            5 ->  {
                holder.itemView.line_shelf.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect5))
            }
        }
    }

    fun setData(shelf: List<Shelf>) {
        this.shelfList = shelf
        notifyDataSetChanged()
    }
}