package io.github.drumber.kitsune.preference

import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.drumber.kitsune.constants.Defaults
import io.github.drumber.kitsune.data.model.SearchParams
import io.github.drumber.kitsune.data.model.category.CategoryPrefWrapper
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object KitsunePref : KotprefModel(), KoinComponent {

    override val commitAllPropertiesByDefault = true

    var titles by enumValuePref(TitlesPref.Canoncial)


    private var searchParamsJson by stringPref(Defaults.DEFAULT_SEARCH_PARAMS.toJsonString())

    var searchParams: SearchParams
        set(value) {
            searchParamsJson = value.toJsonString()
        }
        get() = searchParamsJson.fromJsonString()


    private var searchQueriesJson by stringPref("[]")

    val searchQueries = SearchQueryData(searchQueriesJson.fromJsonString()) {
        searchQueriesJson = it.toJsonString()
    }


    private var searchCategoriesJson by stringPref("[]")

    var searchCategories: List<CategoryPrefWrapper>
        set(value) {
            searchCategoriesJson = value.toJsonString()
        }
        get() = searchCategoriesJson.fromJsonString()


    private inline fun Any.toJsonString(): String {
        val objectMapper: ObjectMapper = get()
        return objectMapper.writeValueAsString(this)
    }

    private inline fun <reified T> String.fromJsonString(): T {
        val objectMapper: ObjectMapper = get()
        return objectMapper.readValue(this)
    }

}

enum class TitlesPref {
    Canoncial, Romanized, English
}
