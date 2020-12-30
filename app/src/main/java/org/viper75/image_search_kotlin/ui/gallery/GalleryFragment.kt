package org.viper75.image_search_kotlin.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.gallery_fragment_layout.*
import org.viper75.image_search_kotlin.R
import org.viper75.image_search_kotlin.databinding.GalleryFragmentLayoutBinding
import org.viper75.image_search_kotlin.models.UnsplashPhoto

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.gallery_fragment_layout), UnsplashPhotoAdapter.OnItemClickListener {

    private val viewModel by viewModels<GalleryViewModel>()

    private var _binding: GalleryFragmentLayoutBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GalleryFragmentLayoutBinding.bind(view)

        val adapter = UnsplashPhotoAdapter(this)

        _binding.apply {
            unsplash_photos_recycler_view.setHasFixedSize(true)
            unsplash_photos_recycler_view.itemAnimator= null
            unsplash_photos_recycler_view.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UnsplashPhotoLoadStateAdapter { adapter.retry() },
                footer = UnsplashPhotoLoadStateAdapter { adapter.retry() }
            )

            retry_button.setOnClickListener { adapter.retry() }
        }

        viewModel.photos.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        setHasOptionsMenu(true)

        adapter.addLoadStateListener { loadStates ->
            binding.apply {
                loadingProgressIndicator.isVisible = loadStates.source.refresh is LoadState.Loading
                unsplashPhotosRecyclerView.isVisible = loadStates.source.refresh is LoadState.NotLoading
                errorTextView.isVisible = loadStates.source.refresh is LoadState.Error
                retryButton.isVisible = loadStates.source.refresh is LoadState.Error

                //For empty view
                if (loadStates.source.refresh is LoadState.NotLoading &&
                        loadStates.append.endOfPaginationReached &&
                        adapter.itemCount < 1) {
                    unsplashPhotosRecyclerView.isVisible = false
                    resultsNotFoundTextView.isVisible = true
                } else {
                    resultsNotFoundTextView.isVisible = false
                }
            }
        }
    }

    override fun onItemClick(photo: UnsplashPhoto) {
        val option =  GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(photo)
        findNavController().navigate(option)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_gallery, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.unsplashPhotosRecyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(query)
                    searchView.clearFocus()
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}