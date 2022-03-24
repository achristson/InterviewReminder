package com.achristson.interviewreminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.achristson.interviewreminder.databinding.InterviewItemBinding
import com.achristson.interviewreminder.models.Interview

class InterviewAdapter(private val onClickListener: OnClickListener)
    : ListAdapter<Interview, InterviewAdapter.InterviewViewHolder>(DiffCallback) {
    class InterviewViewHolder(
        private var binding : InterviewItemBinding
        ) : RecyclerView.ViewHolder(binding.root) {
            fun bind(interview : Interview){
                binding.interview = interview
                binding.executePendingBindings()
            }
        }

    companion object DiffCallback : DiffUtil.ItemCallback<Interview>(){
        override fun areItemsTheSame(oldItem: Interview, newItem: Interview): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Interview, newItem: Interview): Boolean {
            return oldItem.interviewId == newItem.interviewId
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InterviewViewHolder {
        return InterviewViewHolder(
            InterviewItemBinding
                .inflate(LayoutInflater
                    .from(parent.context)))
    }

    override fun onBindViewHolder(holder: InterviewViewHolder, position: Int) {
        val interview = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(interview)
        }
        holder.bind(interview)
    }

    class OnClickListener(val clickListener: (interview:Interview) -> Unit){
        fun onClick(interview: Interview) = clickListener(interview)
    }
}

