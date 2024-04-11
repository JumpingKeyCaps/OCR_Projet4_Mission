package com.aura.unitTest.viewModelUnitTest

import com.aura.main.data.repository.HomeRepository
import com.aura.main.model.home.UserAccount
import com.aura.main.ui.home.HomeState
import com.aura.main.ui.home.HomeViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * Tests of the Home ViewModel.
 */
class HomeViewModelTest {
    // Mock the HomeRepository interface
    private val mockHomeRepository = mockk<HomeRepository>()

    // Create an instance of the ViewModel with the mocked repository
    private val viewModel = HomeViewModel(mockHomeRepository)

    /**
     * Setup
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        // Set the Main dispatcher for testing
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    /**
     * Test getUserAccounts method with a successful response from the repository.
     */
    @Test
    fun `getUserAccounts - successful response`() = runTest {
        val userId = "1234"
        val expectedUserAccount = UserAccount("1", true, 2000.0)
        val expectedAccountList = listOf(expectedUserAccount)

        // Mock the behavior of homeRepository.getUserAccounts
        coEvery { mockHomeRepository.getUserAccounts(userId) } returns expectedAccountList

        // Call the getUserAccounts method on the ViewModel
        viewModel.getUserAccounts(userId)

        // Collect the latest values from StateFlows
        val actualState = viewModel.etat.first()
        val actualUserAccount = viewModel.userAccount.first()

        // Assert that the state is SUCCESS and userAccount matches the expected one
        assertEquals(HomeState.SUCCESS, actualState)
        assertEquals(expectedUserAccount, actualUserAccount)
    }

    /**
     * Test getUserAccounts method with an empty user account list from the repository.
     */
    @Test
    fun `getUserAccounts - empty user account list`() = runTest {
        val userId = "1234"
        val emptyUserList = emptyList<UserAccount>()

        // Mock the behavior of homeRepository.getUserAccounts
        coEvery { mockHomeRepository.getUserAccounts(userId) } returns emptyUserList

        // Call the getUserAccounts method on the ViewModel
        viewModel.getUserAccounts(userId)

        // Collect the latest values from StateFlows
        val actualState = viewModel.etat.first()
        val actualUserAccount = viewModel.userAccount.first()

        // Assert that the state is ERROR and userAccount is null
        assertEquals(HomeState.ERROR, actualState)
        assertNull(actualUserAccount)
    }

    /**
     * Test getUserAccounts method with a repository exception.
     */
    @Test
    fun `getUserAccounts - repository exception`() = runTest {
        val userId = "1234"
        val expectedErrorMessage = "Error fetching user accounts"

        // Mock the behavior of homeRepository.getUserAccounts to throw an exception
        coEvery { mockHomeRepository.getUserAccounts(userId) } throws Exception(expectedErrorMessage)

        // Call the getUserAccounts method on the ViewModel
        viewModel.getUserAccounts(userId)

        // Collect the latest value from the state Flow
        val actualState = viewModel.etat.first()

        // Assert that the state is ERROR
        assertEquals(HomeState.ERROR, actualState)

    }


}