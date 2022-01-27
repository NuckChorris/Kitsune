package io.github.drumber.kitsune

import io.github.drumber.kitsune.data.service.category.CategoryService
import io.github.drumber.kitsune.di.serviceModule
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test
import kotlin.test.assertEquals

class CategoriesServiceTest : BaseTest() {

    override val koinModules = listOf(serviceModule)

    @Test
    fun fetchAllCategories() = runBlocking {
        val categoryService = getKoin().get<CategoryService>()
        val response = categoryService.allCategories()
        val categoryList = response.get()
        assertNotNull(categoryList)
        println("Received ${categoryList?.size} categories")
        println("First: ${categoryList?.first()}")
    }

    @Test
    fun fetchSingleCategory() = runBlocking {
        val categoryService = getKoin().get<CategoryService>()
        val response = categoryService.getCategory("10")
        val category = response.get()
        assertNotNull(category)
        assertEquals("Elf", category?.title)
        println("Received category: $category")
    }

}