package org.koitharu.kotatsu.backups.data.mihon

import org.koitharu.kotatsu.parsers.model.MangaParserSource
import java.net.URI

object MihonSourceMapper {

	private val knownSourceMap = mapOf(
		2499283573021260255L to "MANGADEX",
		7542456487843468504L to "MANGASEE",
		5842880790847953683L to "MANGALIFE",
		8612140417647249953L to "ASURASCANS",
		7962452296065584852L to "FLAMECOMICS",
		782352210803582498L to "MANGANATO",
		8814761000627500642L to "WEBTOON",
		2588146747530644347L to "READMANGA",
		5534888126135898863L to "MINTMANGA",
		7321528659102434313L to "BATOTO",
		8122971842055627725L to "MANGAPARK",
		6908006253457193699L to "NHENTAI",
		8973685934509424911L to "PURURIN",
		703350438186259461L to "EHENTAI",
	)

	fun resolveKotatsuSource(
		mihonSourceId: Long,
		mihonSourceName: String?,
		mangaUrl: String,
		thumbnailUrl: String?,
	): String {
		// 1. Check known source map
		knownSourceMap[mihonSourceId]?.let { sourceName ->
			MangaParserSource.entries.find { it.name == sourceName }?.let {
				return it.name
			}
		}

		// 2. Fallback to URL / domain matching against Kotatsu MangaParserSource entries
		val urlToMatch = mangaUrl.ifEmpty { thumbnailUrl.orEmpty() }
		val host = runCatching { URI(urlToMatch).host?.lowercase() }.getOrNull().orEmpty()

		if (host.isNotEmpty()) {
			for (parser in MangaParserSource.entries) {
				val parserName = parser.name.lowercase()
				val parserTitle = parser.title.lowercase().replace(" ", "")
				if (host.contains(parserName) || (parserTitle.length > 3 && host.contains(parserTitle))) {
					return parser.name
				}
			}
		}

		// Also check by name matching if mihonSourceName is available
		if (!mihonSourceName.isNullOrEmpty()) {
			val cleanName = mihonSourceName.replace(" ", "").lowercase()
			for (parser in MangaParserSource.entries) {
				val parserClean = parser.name.replace("_", "").lowercase()
				if (cleanName.contains(parserClean) || parserClean.contains(cleanName)) {
					return parser.name
				}
			}
		}

		// 3. Fallback to unmapped source name for in-app migration
		return mihonSourceName?.ifEmpty { null } ?: "MIHON_$mihonSourceId"
	}
}
