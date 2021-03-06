package com.romeo.character.presentation

import com.romeo.core.data.repository.CharacterRepository
import com.romeo.core.domain.entity.Character
import com.romeo.core.presentation.BaseViewModel
import kotlinx.coroutines.flow.collect
import java.io.File

class CharacterViewModel(
    private val repository: CharacterRepository,
    private val _item: Character?
) : BaseViewModel<CharacterViewState>() {

    private var isFav = _item?.isFavorite

    private fun withItem(action: suspend (Character) -> Unit) {
        _item?.let {
            runAsync {
                action(it)
            }
        } ?: runAsync {
            navigateBack()
        }
    }

    private fun withCharState(action: (CharacterViewState.CharacterState) -> Unit) {
        withItem {
            action(CharacterViewState.CharacterState(it))
        }
    }

    override fun onViewInit() {
        withCharState { emitSuccess(it) }
    }

    fun onChangeFavoriteStatusPressed() {
        isFav?.let {
            if (it) {
                emitSuccess(CharacterViewState.ToAddToFav)
                withItem { char ->
                    repository.removeFromFavorites(char)
                }

            } else{
                emitSuccess(CharacterViewState.ToRemoveFromFav)
                withItem { char ->
                    repository.addToFavorites(char)
                }
            }

            isFav = !it
        }
    }

    fun onImageSelected(bytes: ByteArray) {
        withItem { oldItem ->
            repository.changeImage(oldItem.id, bytes).collect { newItem ->
                emitSuccess(CharacterViewState.CharacterState(newItem))
            }
        }
    }

    fun onBackPressed() {
        navigateBack()
    }
}