package com.aura.unitTest.repositoryUnitTest

import com.aura.main.data.repository.LoginRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

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
        val expectedResponse = Response.success(LoginResponse(true))
        // Mock the behavior of auraApiService.login
        coEvery { mockApiService.login(loginRequest) } returns expectedResponse
        // Call the login method on the LoginRepository
        val actualResponse = loginRepository.login(loginRequest)
        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse.body(), actualResponse)
    }

    /**
     * Test login method behavior with a failed response from the Api service.
     */
    @Test
    fun `login - failed login`() = runTest {
        val loginRequest = LoginRequest("1234", "wrong_password")
        val expectedFailedResponse = Response.success(LoginResponse(false))
        // Mock the behavior of auraApiService.login to return a failed response
        coEvery { mockApiService.login(loginRequest) } returns  expectedFailedResponse
        // Call the login method on the LoginRepository
        val actualResponse = loginRepository.login(loginRequest)
        // Assert that the actual response is equal to the expected response
        assertEquals(expectedFailedResponse.body(), actualResponse)
    }

    /**
     * Test login method behavior with an empty response from the Api service.
     */
    @Test
    fun `login - empty response body`() = runTest {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        val emptyResponse = Response.success<LoginResponse>(null)
        // Mock the behavior of auraApiService.login to return an empty body
        coEvery { mockApiService.login(loginRequest) } returns emptyResponse
        // Call the login method on the LoginRepository
        val actualResponse = loginRepository.login(loginRequest)
        // Assert that the actual response has 'granted' set to false (default)
        assertEquals(false, actualResponse.granted)
    }

}