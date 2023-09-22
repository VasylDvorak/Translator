package com.translator.view.main_fragment

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.test.runner.AndroidJUnit4
import com.nhaarman.mockito_kotlin.atLeastOnce
import com.translator.TestCoroutineRule
import com.translator.model.data.AppState
import com.translator.model.data.DataModel
import com.translator.model.data.Meanings
import com.translator.model.repository.Repository
import com.translator.model.repository.RepositoryLocal
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1], manifest = Config.NONE)
@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var testCoroutineRule = TestCoroutineRule()

    private lateinit var mainViewModel: MainViewModel


    private lateinit var interactor: MainInteractor

    @Mock
    private lateinit var repositoryRemote: Repository<List<DataModel>>

    @Mock
    private lateinit var repositoryLocal: RepositoryLocal<List<DataModel>>


    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = MainInteractor(repositoryRemote, repositoryLocal)
        mainViewModel = MainViewModel(interactor).apply {

            coroutineScope = CoroutineScope(
                Dispatchers.Main
                        + SupervisorJob()
                        + CoroutineExceptionHandler { _, throwable ->
                    mainViewModel.handleError(throwable)
                })
        }
    }

    @Test
    fun coroutines_MainRequest_TestReturnValueIsEquals() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = mainViewModel.subscribe()

            try {
                liveData.observeForever(observer)

                val answer = listOf(
                    DataModel(text = "first", listOf(Meanings(imageUrl = "firstImage"))),
                    DataModel(text = "first", listOf(Meanings(imageUrl = "secondImage")))
                )

                `when`(interactor.repositoryRemote.getData(SEARCH_QUERY)).thenReturn(answer)

                mainViewModel.getData(SEARCH_QUERY, true)

                Assert.assertNotNull(liveData)

                when (liveData.value) {
                    is AppState.Success -> {
                        val value = liveData.value as AppState.Success
                        value.data?.let { Assert.assertEquals(it, answer) }
                    }

                    else -> {
                        Assert.assertTrue(false)
                    }
                }

            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    @Test
    fun coroutines_MainRequest_TestReturnValueIsErrorEmptyRequestString() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = mainViewModel.subscribe()

            try {
                liveData.observeForever(observer)
                mainViewModel.getData("", true)

                Assert.assertNotNull(liveData)

                when (liveData.value) {
                    is AppState.Error -> {
                        val value = liveData.value as AppState.Error
                        Assert.assertEquals(value.error.message, ERROR_TEXT)
                    }

                    else -> {
                        Assert.assertTrue(false)
                    }
                }

            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    @Test
    fun coroutines_MainRequest_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<AppState> {}
            val liveData = mainViewModel.subscribe()

            `when`(interactor.repositoryRemote.getData(SEARCH_QUERY)).thenReturn(null)

            try {
                liveData.observeForever(observer)
                mainViewModel.getData(SEARCH_QUERY, true)

                Assert.assertNotNull(liveData)

                when (liveData.value) {
                    is AppState.Error -> {
                        val value = liveData.value as AppState.Error
                        Assert.assertEquals(value.error.message, EXCEPTION_TEXT)
                    }

                    else -> {
                        Assert.assertTrue(false)
                    }
                }

            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    @Test
    fun coroutines_FromHistory_TestReturnValueIsEquals() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<DataModel> {}
            val liveData = mainViewModel.subscribeFindWord()

            try {
                liveData.observeForever(observer)

                val answer = DataModel(text = "first", listOf(Meanings(imageUrl = "firstImage")))

                `when`(interactor.repositoryLocal.findWordInDB(SEARCH_QUERY)).thenReturn(answer)

                mainViewModel.findWordInHistory(SEARCH_QUERY)

                Assert.assertNotNull(liveData)

                liveData.value?.let { Assert.assertEquals(it, answer) }

            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    @Test
    fun coroutines_FromHistory_TestReturnValueIsErrorEmptyRequestString() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<DataModel> {}
            val liveData = mainViewModel.subscribeFindWord()

            try {
                liveData.observeForever(observer)

                val answer = DataModel()

                mainViewModel.findWordInHistory(SEARCH_QUERY)

                Assert.assertNotNull(liveData)

                liveData.value?.let { Assert.assertEquals(it, answer) }

            } finally {
                liveData.removeObserver(observer)
            }

        }
    }

    @Test
    fun coroutines_FromHistory_TestException() {
        testCoroutineRule.runBlockingTest {
            val observer = Observer<DataModel> {}
            val liveData = mainViewModel.subscribeFindWord()

            try {
                liveData.observeForever(observer)

                val answer = DataModel()
                `when`(interactor.repositoryLocal.findWordInDB(SEARCH_QUERY)).thenReturn(null)

                mainViewModel.findWordInHistory(SEARCH_QUERY)

                liveData.value?.let { Assert.assertEquals(it, answer) }

            } finally {
                liveData.removeObserver(observer)
            }

        }

    }

    @Test
    fun coroutines_putFavorite() {

        mainViewModel.interactor = mock(MainInteractor::class.java)
        val sendToFavorite = DataModel(text = "first", listOf(Meanings(imageUrl = "firstImage")))

        testCoroutineRule.runBlockingTest {
            mainViewModel.putInFavorite(sendToFavorite)
            verify(mainViewModel.interactor, atLeastOnce()).putFavorite(sendToFavorite)
        }

    }

    @Test
    fun test_onCleared(){
            val observerAppState: Observer<AppState?>? = Observer {}
            val liveDataAppState: LiveData<AppState>? = mainViewModel.subscribe()
            val observerDataModel: Observer<DataModel?>? = Observer {}
            val liveDataDataModel: LiveData<DataModel>? = mainViewModel.subscribeFindWord()

            try {
                observerAppState?.let { liveDataAppState?.observeForever(it) }
                observerDataModel?.let { liveDataDataModel?.observeForever(it) }

                mainViewModel.onCleared()

                when (liveDataAppState?.value) {
                    is AppState.Success -> {
                        val value = liveDataAppState.value as AppState.Success
                        Assert.assertNull(value.data)
                    }
                    else -> {
                        Assert.assertTrue(false)
                    }
                }
                Assert.assertNull(liveDataDataModel?.value)

            } finally {
                observerAppState?.let { liveDataAppState?.removeObserver(it) }
                observerDataModel?.let { liveDataDataModel?.removeObserver(it) }
            }

    }

    @Test
    fun test_handleError(){
        val observerAppState: Observer<AppState?>? = Observer {}
        val liveDataAppState: LiveData<AppState>? = mainViewModel.subscribe()

        try {
            observerAppState?.let { liveDataAppState?.observeForever(it) }
            val answer=Throwable(EXCEPTION_TEXT)
            mainViewModel.handleError(answer)

            when (liveDataAppState?.value) {
                is AppState.Error -> {
                    val value = liveDataAppState.value as AppState.Error
                    Assert.assertEquals(value.error.message, answer.message)
                }
                else -> {
                    Assert.assertTrue(false)
                }
            }
        } finally {
            observerAppState?.let { liveDataAppState?.removeObserver(it) }
        }
    }

    companion object {
        private const val SEARCH_QUERY = "some query"
        private const val ERROR_TEXT = "Пустая строка"
        private const val EXCEPTION_TEXT = "Ошибка"
    }
}
