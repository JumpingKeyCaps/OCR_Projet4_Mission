package com.aura.unitTest.mainTest.uiTest

import androidx.lifecycle.SavedStateHandle
import com.aura.main.data.repository.LoginRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.login.LoginContent
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test

/**
 * Test of the LoginViewModel
 */
class LoginViewModelTest {

    private val mockLoginRepository = mockk<LoginRepository>()
    private val mockSavedStateHandle = mockk<SavedStateHandle>()


    /**
     * Test login method - Success
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - successful login`() = runTest {
        val identifier = "username"
        val password = "password"
        val mockResponse = LoginResponse(true)

        // Mock the loginRepository to return a successful response
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } returns mockResponse
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val loginViewModel = LoginViewModel(mockLoginRepository)
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)
        // Verify that the LCE state is updated with LoginContent(true, true)


        val expectedContent = ScreenState.Content(LoginContent(fieldIsOK = true, granted = true))
        val actualContent = loginViewModel.lceState.first() as ScreenState.Content // Cast to Content
        assertEquals(expectedContent.data.granted, actualContent.data.granted)


    }


    /**
     * Test login method - fail
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - failed login`() = runTest {
        val identifier = "1234"
        val password = "wrong_password"

        // Mock the loginRepository to return a failed response
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } returns LoginResponse(false)
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val loginViewModel = LoginViewModel(mockLoginRepository)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(TestCoroutineDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with the expected error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is ScreenState.Error)
    }


    /**
     * Test login method - no internet exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - throws NetworkException no internet`() = runTest {
        val identifier = "username"
        val password = "password"
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)

        // Mock the loginRepository to throw an exception
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val loginViewModel = LoginViewModel(mockLoginRepository)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is ScreenState.Error)

    }


    /**
     * Test login method - server down exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - throws NetworkException server down`() = runTest {
        val identifier = "username"
        val password = "password"
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)

        // Mock the loginRepository to throw an exception
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val loginViewModel = LoginViewModel(mockLoginRepository)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is ScreenState.Error)

    }

    /**
     * Test login method - others exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - throws NetworkException others`() = runTest {
        val identifier = "username"
        val password = "password"
        val exception = NetworkException.UnknownNetworkException

        // Mock the loginRepository to throw an exception
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.Keys.KEY_USER_ID) } returns identifier
        val loginViewModel = LoginViewModel(mockLoginRepository)

        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is ScreenState.Error)

    }


}