package org.koitharu.kotatsu.backups.data.mihon

import kotlinx.serialization.Serializable
import kotlinx.serialization.protobuf.ProtoNumber

@Serializable
data class MihonBackup(
	@ProtoNumber(1) val backupManga: List<MihonBackupManga> = emptyList(),
	@ProtoNumber(2) val backupCategories: List<MihonBackupCategory> = emptyList(),
	@ProtoNumber(101) val backupSources: List<MihonBackupSource> = emptyList(),
)

@Serializable
data class MihonBackupManga(
	@ProtoNumber(1) val source: Long = 0,
	@ProtoNumber(2) val url: String = "",
	@ProtoNumber(3) val title: String = "",
	@ProtoNumber(4) val artist: String? = null,
	@ProtoNumber(5) val author: String? = null,
	@ProtoNumber(6) val description: String? = null,
	@ProtoNumber(7) val genre: List<String> = emptyList(),
	@ProtoNumber(8) val status: Int = 0,
	@ProtoNumber(9) val thumbnailUrl: String? = null,
	@ProtoNumber(13) val dateAdded: Long = 0,
	@ProtoNumber(16) val chapters: List<MihonBackupChapter> = emptyList(),
	@ProtoNumber(17) val categories: List<Long> = emptyList(),
	@ProtoNumber(100) val favorite: Boolean = true,
	@ProtoNumber(104) val history: List<MihonBackupHistory> = emptyList(),
)

@Serializable
data class MihonBackupCategory(
	@ProtoNumber(1) val name: String = "",
	@ProtoNumber(2) val order: Long = 0,
	@ProtoNumber(3) val id: Long = 0,
	@ProtoNumber(100) val flags: Long = 0,
)

@Serializable
data class MihonBackupSource(
	@ProtoNumber(1) val name: String = "",
	@ProtoNumber(2) val sourceId: Long = 0,
)

@Serializable
data class MihonBackupChapter(
	@ProtoNumber(1) val url: String = "",
	@ProtoNumber(2) val name: String = "",
	@ProtoNumber(3) val scanlator: String? = null,
	@ProtoNumber(4) val read: Boolean = false,
	@ProtoNumber(5) val bookmark: Boolean = false,
	@ProtoNumber(6) val lastPageRead: Long = 0,
	@ProtoNumber(7) val dateFetch: Long = 0,
	@ProtoNumber(8) val dateUpload: Long = 0,
	@ProtoNumber(9) val chapterNumber: Float = 0f,
	@ProtoNumber(10) val sourceOrder: Long = 0,
)

@Serializable
data class MihonBackupHistory(
	@ProtoNumber(1) val url: String = "",
	@ProtoNumber(2) val lastRead: Long = 0,
	@ProtoNumber(3) val readDuration: Long = 0,
)
