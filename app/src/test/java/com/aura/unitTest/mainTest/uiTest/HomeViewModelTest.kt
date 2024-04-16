package com.aura.unitTest.mainTest.uiTest

import androidx.lifecycle.SavedStateHandle
import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.home.HomeContent
import com.aura.main.model.home.UserAccount
import com.aura.main.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

/**
 * Test of the HomeViewModel
 */
class HomeViewModelTest {

    private val mockHomeRepository = mockk<HomeRepository>()
    private val mockSavedStateHandle = mockk<SavedStateHandle>()


    /**
     * Test getUserAccount method - Success
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - successful retrieval`() = runTest {
        val identifier = "1234"
        val expectedAccounts =
            listOf(UserAccount("1", true, 2000.0), UserAccount("2", false, 345.1))

        // Mock the loginRepository to return a successful response
        coEvery { mockHomeRepository.getUserAccounts(identifier) } returns expectedAccounts
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val homeViewModel = HomeViewModel(mockHomeRepository,mockSavedStateHandle)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        homeViewModel.getUserAccount()
        // Verify that the LCE state is updated with LoginContent(true, true)
        val expectedContent = ScreenState.Content(HomeContent(2000.0))
        val actualContent = homeViewModel.lceState.first() as ScreenState.Content // Cast to Content
        assertEquals(expectedContent.data.userBalance, actualContent.data.userBalance)
    }


    /**
     * Test getUserAccount method - fail
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - failed retrieval`() = runTest {
        val identifier = "1234"

        // Mock the loginRepository to return a successful response
        coEvery { mockHomeRepository.getUserAccounts(identifier) } returns emptyList()
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val homeViewModel = HomeViewModel(mockHomeRepository,mockSavedStateHandle)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        homeViewModel.getUserAccount()
        // Verify that the LCE state is updated with LoginError with the expected error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)
    }



    /**
     * Test getUserAccount method - no internet exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - throws NetworkException no internet`() = runTest {
        val identifier = "1234"
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)

        coEvery { mockHomeRepository.getUserAccounts(identifier) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val homeViewModel = HomeViewModel(mockHomeRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount()

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }

    /**
     * Test getUserAccount method - server down exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - throws NetworkException server down`() = runTest {
        val identifier = "1234"
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
        coEvery { mockHomeRepository.getUserAccounts(identifier) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val homeViewModel = HomeViewModel(mockHomeRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount()

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }

    /**
     * Test getUserAccount method - others exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - throws NetworkException others`() = runTest {
        val identifier = "1234"
        val exception = NetworkException.UnknownNetworkException
        coEvery { mockHomeRepository.getUserAccounts(identifier) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val homeViewModel = HomeViewModel(mockHomeRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount()

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }



}