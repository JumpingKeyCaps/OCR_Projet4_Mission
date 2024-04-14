package com.aura.unitTest.mainTest.uiTest

import com.aura.main.data.repository.LoginRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.login.LoginLCE
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
    private val loginViewModel = LoginViewModel(mockLoginRepository)

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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)
        // Verify that the LCE state is updated with LoginContent(true, true)
        val expectedState = LoginLCE.LoginContent(fieldIsOK = true, granted = true)
        assertEquals(expectedState, loginViewModel.lceState.first())
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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(TestCoroutineDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with the expected error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is LoginLCE.LoginError)
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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is LoginLCE.LoginError)

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
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is LoginLCE.LoginError)

    }

    /**
     * Test login method - others exception
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `login - throws NetworkException others`() = runTest {
        val identifier = "username"
        val password = "password"
        val exception = NetworkException.UnknownNetworkException()

        // Mock the loginRepository to throw an exception
        coEvery { mockLoginRepository.login(LoginRequest(identifier, password)) } throws exception
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        loginViewModel.login(identifier, password)

        // Verify that the LCE state is updated with LoginError with a generic network error message
        val actualState = loginViewModel.lceState.first()
        assertTrue(actualState is LoginLCE.LoginError)

    }


}