package com.example.dessertclicker.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.example.dessertclicker.R
import com.example.dessertclicker.data.Datasource
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DessertViewModel: ViewModel() {
    private val desserts = Datasource.dessertList

    private var _uiState = MutableStateFlow(DessertUiState(currentDessertIndex = 0, currentDessertPrice = desserts[0].price, currentDessertImageId = desserts[0].imageId))
    val uiState: StateFlow<DessertUiState> = _uiState.asStateFlow()

    private fun currentDessertPrice(): Int  = desserts[_uiState.value.currentDessertIndex].price
    fun currentDessertImageId(): Int = desserts[_uiState.value.currentDessertIndex].imageId

    fun shareSoldDessertsInformation(intentContext: Context) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                intentContext.getString(R.string.share_text, _uiState.value.dessertsSold, _uiState.value.revenue)
            )
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)

        try {
            ContextCompat.startActivity(intentContext, shareIntent, null)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                intentContext,
                intentContext.getString(R.string.sharing_not_available),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun onDessertClicked() {
        _uiState.update { currentState ->
            val incrementedDessertsSold = currentState.dessertsSold.inc()
            val dessertToShow = determineDessertToShow(desserts, incrementedDessertsSold)
            currentState.copy(
                revenue = currentState.revenue + currentDessertPrice(),
                dessertsSold = incrementedDessertsSold,
                currentDessertImageId = dessertToShow.imageId,
                currentDessertPrice = dessertToShow.price
            )
        }
    }

    private fun determineDessertToShow(
        desserts: List<Dessert>,
        dessertsSold: Int
    ): Dessert {
        var dessertToShow = desserts.first()
        for (dessert in desserts) {
            if (dessertsSold >= dessert.startProductionAmount) {
                dessertToShow = dessert
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertToShow
    }
}