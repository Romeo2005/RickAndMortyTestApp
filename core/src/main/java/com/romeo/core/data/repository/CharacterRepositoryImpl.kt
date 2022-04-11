package com.romeo.core.data.repository

import com.romeo.core.data.datasource.remote.CharacterRemoteDatasource
import com.romeo.core.data.local.dao.CharacterDAO
import com.romeo.core.domain.entity.Character
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single

class CharacterRepositoryImpl(
    private val remoteDatasource: CharacterRemoteDatasource,
    private val localDatasource: CharacterDAO
) : CharacterRepository {
    private var characters: List<Character>? = null
    private var favorites: MutableList<Character>? = null

    override suspend fun getAll(
        page: Int,
        pageSize: Int,
        update: Boolean
    ): Flow<List<Character>> {
        return if (update) {
            try {
                val values = remoteDatasource.getAllCharacters(page, pageSize).single()

                flowOfLocals().collect()

                characters?.forEach { old ->
                    val new = values.find { it.id == old.id }
                    new?.apply {
                        isFavorite = old.isFavorite
                    }
                }

                flowOf(values)
            } catch (t: Throwable) {
                flowOfLocals()
            }
        } else flowOfLocals()
    }

    private suspend fun flowOfLocals() = flowOf(
        characters ?: localDatasource.getAll().apply { characters = this }
    )

    private suspend fun flowOfFavorites() = flowOf(
        favorites ?: localDatasource.getFavorites().apply { favorites = toMutableList() }
    )

    override suspend fun getFavorites(): Flow<List<Character>> {
        return flowOfFavorites()
    }

    override suspend fun getOne(id: Int): Flow<Character?> {
        return flowOf(localDatasource.get(id))
    }

    override suspend fun addToFavorites(char: Character) {
        char.isFavorite = true
        localDatasource.put(char)
        characters?.find { it.id == char.id }?.isFavorite = true
        favorites?.add(char)
    }

    override suspend fun removeFromFavorites(char: Character) {
        char.isFavorite = false
        localDatasource.put(char)
        characters?.find { it.id == char.id }?.isFavorite = false
        favorites?.removeIf { it.id == char.id }
    }
}