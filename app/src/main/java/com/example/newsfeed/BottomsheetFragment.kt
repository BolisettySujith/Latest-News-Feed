package com.example.newsfeed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.example.newsfeed.databinding.BottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomsheetFragment() : BottomSheetDialogFragment() {

    private lateinit var binding : BottomSheetFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val news = arguments?.getParcelable<News>("news")

        binding = DataBindingUtil.inflate(inflater,R.layout.bottom_sheet_fragment, container, false)
        binding.news = news
        if (news != null) {
            Glide.with(this).load(news.imageUrl).into(binding.image)
        }
        binding.newArticleUrlButton.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(news!!.url))
            startActivity(browserIntent)
        }

        return binding.root
    }

}
