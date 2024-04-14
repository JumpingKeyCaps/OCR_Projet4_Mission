package com.aura.unitTest.mainTest.uiTest

import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.home.HomeLCE
import com.aura.main.model.home.UserAccount
import com.aura.main.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
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
    private val homeViewModel = HomeViewModel(mockHomeRepository)


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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        homeViewModel.getUserAccount(identifier)
        // Verify that the LCE state is updated with LoginContent(true, true)
        val expectedState = HomeLCE.HomeContent(2000.0)
        TestCase.assertEquals(expectedState, homeViewModel.lceState.first())
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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        homeViewModel.getUserAccount(identifier)
        // Verify that the LCE state is updated with LoginError with the expected error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is HomeLCE.HomeError)
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
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount(identifier)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is HomeLCE.HomeError)

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
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount(identifier)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is HomeLCE.HomeError)

    }

    /**
     * Test getUserAccount method - others exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getUserAccount - throws NetworkException others`() = runTest {
        val identifier = "1234"
        val exception = NetworkException.UnknownNetworkException()

        coEvery { mockHomeRepository.getUserAccounts(identifier) } throws exception
        Dispatchers.setMain(UnconfinedTestDispatcher())
        homeViewModel.getUserAccount(identifier)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = homeViewModel.lceState.first()
        TestCase.assertTrue(actualState is HomeLCE.HomeError)

    }



}