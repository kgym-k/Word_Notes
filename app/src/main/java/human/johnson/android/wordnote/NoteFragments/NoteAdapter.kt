package human.johnson.android.wordnote.NoteFragments

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import human.johnson.android.wordnote.R
import human.johnson.android.wordnote.model.Note
import human.johnson.android.wordnote.viewmodel.NoteViewModel
import kotlinx.android.synthetic.main.note_row.view.*

class NoteAdapter(val mNoteViewModel: NoteViewModel): RecyclerView.Adapter<NoteAdapter.MyViewHolder>() {

    private var noteList = emptyList<Note>()
    private lateinit var context: Context

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        context = parent.context

        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.note_row,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.itemView.front_word_txt.text = currentItem.word
        holder.itemView.back_word_txt.text = currentItem.word
        holder.itemView.front_meaning_txt.text = currentItem.meaning
        holder.itemView.back_meaning_txt.text = currentItem.meaning
        holder.itemView.front_memo_txt.text = currentItem.memo
        holder.itemView.back_memo_txt.text = currentItem.memo

        // set color
        when (currentItem.color) {
            1 ->  {
                holder.itemView.front_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect1))
                holder.itemView.back_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect1))
            }
            2 ->  {
                holder.itemView.front_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect2))
                holder.itemView.back_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect2))
            }
            3 ->  {
                holder.itemView.front_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect3))
                holder.itemView.back_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect3))
            }
            4 ->  {
                holder.itemView.front_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect4))
                holder.itemView.back_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect4))
            }
            5 ->  {
                holder.itemView.front_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect5))
                holder.itemView.back_line.setBackgroundColor(ContextCompat.getColor(context, R.color.colorSelect5))
            }
        }

        fun updateItem(is_front: Boolean, is_stared: Boolean, is_checked: Boolean) {
            val word = currentItem.word
            val meaning = currentItem.meaning
            val memo = currentItem.memo
            val front = is_front
            val star = is_stared
            val color = currentItem.color

            val updatedNote = Note(currentItem.id, currentItem.shelfId, word, meaning, memo, front, star, is_checked, color)
            mNoteViewModel.updateNote(updatedNote)
        }

        // flip the card
        if (currentItem.front) {
            holder.itemView.back_card.visibility = View.INVISIBLE
            holder.itemView.front_card.visibility = View.VISIBLE
        }
        else {
            holder.itemView.front_card.visibility = View.INVISIBLE
            holder.itemView.back_card.visibility = View.VISIBLE
        }

        fun View.appear() {
            val front_to_back: Animation = AnimationUtils.loadAnimation(context, R.anim.front_to_back)
            this.animation = front_to_back
            this.visibility = View.VISIBLE
        }

        fun View.disappear() {
            val back_to_front: Animation = AnimationUtils.loadAnimation(context, R.anim.back_to_front)
            this.animation = back_to_front
            this.visibility = View.INVISIBLE
        }

        holder.itemView.note_rowLayout.setOnClickListener {
            if (holder.itemView.front_card.isVisible) {
                holder.itemView.front_card.disappear()
                holder.itemView.back_card.appear()
                updateItem(false, currentItem.star, currentItem.checked)
            }
            else {
                holder.itemView.back_card.disappear()
                holder.itemView.front_card.appear()
                updateItem(true, currentItem.star, currentItem.checked)
            }
        }

        // edit
        holder.itemView.front_note_edit.setOnClickListener{
            val action = NoteCollectionFragmentDirections.actionNoteCollectionFragmentToNoteUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
        holder.itemView.back_note_edit.setOnClickListener{
            val action = NoteCollectionFragmentDirections.actionNoteCollectionFragmentToNoteUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }

        // star
        if (currentItem.star) {
            holder.itemView.front_star_emp.visibility = View.INVISIBLE
            holder.itemView.back_star_emp.visibility = View.INVISIBLE
            holder.itemView.front_star_full.visibility = View.VISIBLE
            holder.itemView.back_star_full.visibility = View.VISIBLE
        }
        else {
            holder.itemView.front_star_full.visibility = View.INVISIBLE
            holder.itemView.back_star_full.visibility = View.INVISIBLE
            holder.itemView.front_star_emp.visibility = View.VISIBLE
            holder.itemView.back_star_emp.visibility = View.VISIBLE
        }

        holder.itemView.front_star_emp.setOnClickListener {
            holder.itemView.front_star_emp.visibility = View.INVISIBLE
            holder.itemView.back_star_emp.visibility = View.INVISIBLE
            holder.itemView.front_star_full.visibility = View.VISIBLE
            holder.itemView.back_star_full.visibility = View.VISIBLE
            updateItem(currentItem.front, true, currentItem.checked)
        }
        holder.itemView.front_star_full.setOnClickListener {
            holder.itemView.front_star_full.visibility = View.INVISIBLE
            holder.itemView.back_star_full.visibility = View.INVISIBLE
            holder.itemView.front_star_emp.visibility = View.VISIBLE
            holder.itemView.back_star_emp.visibility = View.VISIBLE
            updateItem(currentItem.front, false, currentItem.checked)
        }
        holder.itemView.back_star_emp.setOnClickListener {
            holder.itemView.front_star_emp.visibility = View.INVISIBLE
            holder.itemView.back_star_emp.visibility = View.INVISIBLE
            holder.itemView.front_star_full.visibility = View.VISIBLE
            holder.itemView.back_star_full.visibility = View.VISIBLE
            updateItem(currentItem.front, true, currentItem.checked)
        }
        holder.itemView.back_star_full.setOnClickListener {
            holder.itemView.front_star_full.visibility = View.INVISIBLE
            holder.itemView.back_star_full.visibility = View.INVISIBLE
            holder.itemView.front_star_emp.visibility = View.VISIBLE
            holder.itemView.back_star_emp.visibility = View.VISIBLE
            updateItem(currentItem.front, false, currentItem.checked)
        }

        // check
        if (currentItem.checked) {
            holder.itemView.front_check.visibility = View.VISIBLE
            holder.itemView.back_check.visibility = View.VISIBLE
        }
        else {
            holder.itemView.front_check.visibility = View.INVISIBLE
            holder.itemView.back_check.visibility = View.INVISIBLE
        }
    }

    fun setData(note: List<Note>) {
        this.noteList = note
        notifyDataSetChanged()
    }
}
