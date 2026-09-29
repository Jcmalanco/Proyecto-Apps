package com.attor.app.ui.seeall

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.attor.app.data.WorkFormat
import com.attor.app.data.WorkRepository
import com.attor.app.databinding.ActivitySeeAllBinding
import com.attor.app.ui.common.BaseActivity
import com.attor.app.ui.workdetails.WorkDetailsActivity
import com.attor.app.util.GridSpacingItemDecoration
import com.attor.app.util.WorkAdapter
import com.attor.app.util.dpToPx
import kotlinx.coroutines.launch

class SeeAllActivity : BaseActivity() {
    private lateinit var binding: ActivitySeeAllBinding
    private lateinit var repository: WorkRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeeAllBinding.inflate(layoutInflater)
        setContentView(binding.root)

        repository = WorkRepository(this)
        val formatName = intent.getStringExtra(EXTRA_FORMAT) ?: WorkFormat.NOVEL.name
        val format = try { WorkFormat.valueOf(formatName) } catch (e: Exception) { WorkFormat.NOVEL }

        title = format.displayName

        val adapter = WorkAdapter { work -> WorkDetailsActivity.start(this, work.id) }
        binding.rvSeeAll.layoutManager = GridLayoutManager(this, 2)
        binding.rvSeeAll.addItemDecoration(GridSpacingItemDecoration(dpToPx(12), spanCount = 2))
        binding.rvSeeAll.adapter = adapter

        lifecycleScope.launch {
            val all = repository.getAll()
            adapter.submitList(all.filter { it.format == format })
        }
    }

    companion object {
        private const val EXTRA_FORMAT = "extra_format"
        fun start(context: Context, format: WorkFormat) {
            val intent = Intent(context, SeeAllActivity::class.java).apply {
                putExtra(EXTRA_FORMAT, format.name)
            }
            context.startActivity(intent)
        }
    }
}
