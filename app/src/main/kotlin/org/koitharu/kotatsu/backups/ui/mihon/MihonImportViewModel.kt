package org.koitharu.kotatsu.backups.ui.mihon

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koitharu.kotatsu.backups.data.mihon.MihonImportRepository
import org.koitharu.kotatsu.backups.data.mihon.MihonImportState
import org.koitharu.kotatsu.core.nav.AppRouter
import org.koitharu.kotatsu.core.ui.BaseViewModel
import org.koitharu.kotatsu.core.util.ext.toUriOrNull
import java.io.FileNotFoundException
import javax.inject.Inject

@HiltViewModel
class MihonImportViewModel @Inject constructor(
	savedStateHandle: SavedStateHandle,
	private val repository: MihonImportRepository,
) : BaseViewModel() {

	val fileUri: Uri? = savedStateHandle.get<String>(AppRouter.KEY_FILE)?.toUriOrNull()

	private val _importState = MutableStateFlow<MihonImportState>(MihonImportState.Idle)
	val importState: StateFlow<MihonImportState> = _importState.asStateFlow()

	init {
		if (fileUri != null) {
			startImport(fileUri)
		} else {
			_importState.value = MihonImportState.Error(FileNotFoundException("No backup file selected"))
		}
	}

	fun startImport(uri: Uri) {
		launchLoadingJob(Dispatchers.IO) {
			try {
				repository.importBackup(uri, _importState)
			} catch (e: Throwable) {
				_importState.value = MihonImportState.Error(e)
			}
		}
	}
}
