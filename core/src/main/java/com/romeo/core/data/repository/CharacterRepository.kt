package com.romeo.core.data.repository

import com.romeo.core.domain.entity.Character
import kotlinx.coroutines.flow.Flow
import java.io.File

interface CharacterRepository {
    suspend fun getAll(
        page: Int,
        pageSize: Int,
        update: Boolean = true
    ): Flow<List<Character>>

    suspend fun getFavorites(): Flow<List<Character>>
    suspend fun getOne(id: Int): Flow<Character?>
    suspend fun addToFavorites(char: Character)
    suspend fun removeFromFavorites(char: Character)
    suspend fun changeImage(id: Int, bytes: ByteArray): Flow<Character>
}