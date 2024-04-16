package com.aura.unitTest.mainTest.dataTest.repositoryTest

import com.aura.main.data.repository.LoginRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

/**
 * Tests of the login repository.
 */
class LoginRepositoryTest {

    // Mock the AuraApiService interface
    private val mockApiService = mockk<AuraApiService>()
    private val loginRepository = LoginRepository(mockApiService)

    /**
     * Test login method behavior with a successful response from the Api service.
     */
    @Test
    fun `login - successful login`() = runTest {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        val expectedResponse = LoginResponse(true)
        // Mock the behavior of auraApiService.login
        coEvery { mockApiService.login(loginRequest) } returns expectedResponse
        // Call the login method on the LoginRepository
        val actualResponse = loginRepository.login(loginRequest)
        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse, actualResponse)
    }

    /**
     * Test login method behavior with a failed response from the Api service.
     */
    @Test
    fun `login - failed login`() = runTest {
        val loginRequest = LoginRequest("1234", "wrong_password")
        val expectedFailedResponse = LoginResponse(false)
        // Mock the behavior of auraApiService.login to return a failed response
        coEvery { mockApiService.login(loginRequest) } returns  expectedFailedResponse
        // Call the login method on the LoginRepository
        val actualResponse = loginRepository.login(loginRequest)
        // Assert that the actual response is equal to the expected response
        assertEquals(expectedFailedResponse, actualResponse)
    }

    /**
     * Test login method behavior throws an exception on network error (no internet).
     */
    @Test
    fun `login - throws exception on no internet error`() = runTest {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        coEvery { mockApiService.login(loginRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)
        var exceptionThrown = false
        try {
            loginRepository.login(loginRequest)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test login method behavior throws an exception on network error (server down).
     */
    @Test
    fun `login - throws exception on server down error`() = runTest {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        coEvery { mockApiService.login(loginRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
        var exceptionThrown = false
        try {
            loginRepository.login(loginRequest)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test login method behavior throws an exception on network error (others).
     */
    @Test
    fun `login - throws exception on others error`() = runTest {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        coEvery { mockApiService.login(loginRequest) } throws NetworkException.UnknownNetworkException
        var exceptionThrown = false
        try {
            loginRepository.login(loginRequest)
        } catch (e: Exception) {
            if (e is NetworkException.UnknownNetworkException) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }





}