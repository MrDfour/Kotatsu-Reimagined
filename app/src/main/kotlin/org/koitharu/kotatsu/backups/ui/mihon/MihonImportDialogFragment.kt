package org.koitharu.kotatsu.backups.ui.mihon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import org.koitharu.kotatsu.R
import org.koitharu.kotatsu.backups.data.mihon.MihonImportState
import org.koitharu.kotatsu.core.ui.AlertDialogFragment
import org.koitharu.kotatsu.core.util.ext.getDisplayMessage
import org.koitharu.kotatsu.core.util.ext.observe
import org.koitharu.kotatsu.databinding.DialogProgressBinding

@AndroidEntryPoint
class MihonImportDialogFragment : AlertDialogFragment<DialogProgressBinding>() {

	private val viewModel: MihonImportViewModel by viewModels()

	override fun onCreateViewBinding(
		inflater: LayoutInflater,
		container: ViewGroup?,
	): DialogProgressBinding = DialogProgressBinding.inflate(inflater, container, false)

	override fun onBuildDialog(builder: MaterialAlertDialogBuilder): MaterialAlertDialogBuilder {
		return super.onBuildDialog(builder)
			.setTitle(R.string.import_mihon_backup)
			.setPositiveButton(R.string.close, null)
			.setCancelable(false)
	}

	override fun onViewBindingCreated(binding: DialogProgressBinding, savedInstanceState: Bundle?) {
		super.onViewBindingCreated(binding, savedInstanceState)
		viewModel.importState.observe(viewLifecycleOwner, this::renderState)
	}

	private fun renderState(state: MihonImportState) {
		val binding = requireViewBinding()
		when (state) {
			is MihonImportState.Idle -> {
				binding.progressBar.isIndeterminate = true
				binding.textViewTitle.setText(R.string.importing_mihon_backup)
				binding.textViewSubtitle.setText(R.string.loading_)
			}

			is MihonImportState.Loading -> {
				binding.progressBar.isIndeterminate = true
				binding.textViewTitle.setText(R.string.importing_mihon_backup)
				binding.textViewSubtitle.text = state.message
			}

			is MihonImportState.Progress -> {
				binding.progressBar.isIndeterminate = false
				binding.progressBar.max = state.total
				binding.progressBar.progress = state.current
				binding.textViewTitle.setText(R.string.importing_mihon_backup)
				binding.textViewSubtitle.text = getString(
					R.string.fraction_pattern, state.current, state.total
				) + ": " + state.title
			}

			is MihonImportState.Completed -> {
				isCancelable = true
				binding.progressBar.isIndeterminate = false
				binding.progressBar.progress = binding.progressBar.max
				binding.textViewTitle.setText(R.string.mihon_import_complete)

				val res = state.result
				var summary = getString(
					R.string.mihon_import_summary,
					res.importedMangaCount,
					res.importedCategoriesCount,
				)
				if (res.unmappedSources.isNotEmpty()) {
					summary += "\n\n" + getString(R.string.mihon_unmapped_sources) + "\n" +
							res.unmappedSources.joinToString(", ")
				}
				binding.textViewSubtitle.text = summary
			}

			is MihonImportState.Error -> {
				isCancelable = true
				binding.progressBar.isGone = true
				binding.textViewTitle.setText(R.string.error)
				binding.textViewSubtitle.text = state.error.getDisplayMessage(resources)
			}
		}
	}
}
