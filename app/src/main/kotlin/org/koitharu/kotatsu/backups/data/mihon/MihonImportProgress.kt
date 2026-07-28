package org.koitharu.kotatsu.backups.data.mihon

sealed class MihonImportState {
	data object Idle : MihonImportState()
	data class Loading(val message: String) : MihonImportState()
	data class Progress(val current: Int, val total: Int, val title: String) : MihonImportState()
	data class Completed(val result: MihonImportResult) : MihonImportState()
	data class Error(val error: Throwable) : MihonImportState()
}

data class MihonImportResult(
	val importedMangaCount: Int,
	val importedCategoriesCount: Int,
	val importedHistoryCount: Int,
	val unmappedSources: List<String>,
)
