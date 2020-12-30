package org.viper75.image_search_kotlin.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import org.viper75.image_search_kotlin.databinding.UnsplashPhotoLoadStateFooterBinding

class UnsplashPhotoLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<UnsplashPhotoLoadStateAdapter.UnsplashPhotoLoadStateItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): UnsplashPhotoLoadStateItemViewHolder {
        val binding = UnsplashPhotoLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return UnsplashPhotoLoadStateItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: UnsplashPhotoLoadStateItemViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    inner class UnsplashPhotoLoadStateItemViewHolder(private val binding: UnsplashPhotoLoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {
            binding.apply {
                loadingProgressIndicator.isVisible = loadState is LoadState.Loading
                errorTextView.isVisible = loadState !is LoadState.Loading
                retryButton.isVisible = loadState !is LoadState.Loading
            }
        }
    }
}