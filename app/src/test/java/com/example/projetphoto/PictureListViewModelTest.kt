package com.example.projetphoto

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.projetphoto.db.AppDatabase
import com.example.projetphoto.db.pictures.Pictures
import com.example.projetphoto.pictureList.PictureListViewModel
import com.example.projetphoto.pictureList.PictureListViewModelState
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Assert
import org.junit.Rule
import org.junit.Test


class PictureListViewModelTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `load picture yields state Failure`() {

//         val db: AppDatabase = mock()
//        whenever(db.picturesDao().getAll())
//            .thenReturn(mutableListOf<Pictures>())
        //Throw NullException
        val mock = mock<AppDatabase> {
            on { picturesDao().getAll() } doReturn mutableListOf<Pictures>()
        }

        val model = PictureListViewModel()
        val observer = model.getState().testObserver()

        model.loadPictures(mock)
        Assert.assertEquals(
            listOf(
                PictureListViewModelState.Loading,
                PictureListViewModelState.Failure("Aucune image dans la liste")
            ),
            observer.observedValues
        )
    }

    @Test
    fun `load picture yields state Success`() {
        val pictures = mutableListOf<Pictures>(Pictures("test", "12/03/2021 13:37:50", "null", 1))
        val db: AppDatabase = mock()
        whenever(db.picturesDao().getAll())
            .thenReturn(pictures)

        val model = PictureListViewModel()
        val observer = model.getState().testObserver()
        //Error context
//        model.insert("null", "test", "12/03/2021 13:37:50", applicationContext)
        model.loadPictures(db)
        Assert.assertEquals(
            listOf(
                PictureListViewModelState.Loading,
                PictureListViewModelState.Success(pictures)
            ),
            observer.observedValues
        )
    }

}