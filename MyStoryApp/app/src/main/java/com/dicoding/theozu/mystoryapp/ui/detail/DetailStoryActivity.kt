package com.dicoding.theozu.mystoryapp.ui.detail

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.dicoding.theozu.mystoryapp.R
import com.dicoding.theozu.mystoryapp.api.response.ListStoryItem
import com.dicoding.theozu.mystoryapp.databinding.ActivityDetailStoryBinding

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = TITLE

        val story = intent.getParcelableExtra<ListStoryItem>(EXTRA_STORY)

        binding.apply {
            Glide.with(this@DetailStoryActivity)
                .load(story?.photoUrl)
                .placeholder(R.drawable.ic_landscape_grey_24)
                .into(imgPhotoDetail)
            tvNameDetail.text = story?.name
            tvDescription.text = story?.description
        }

        playAnimation()
    }

    private fun playAnimation() {
        val description = ObjectAnimator.ofFloat(binding.tvDescription, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(description)
            startDelay = 250
            start()
        }
    }

    companion object {
        private const val TITLE = "Detail Story"
        const val EXTRA_STORY = "extra_story"
    }
}