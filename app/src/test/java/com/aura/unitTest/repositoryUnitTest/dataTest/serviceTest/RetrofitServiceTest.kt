package com.aura.unitTest.repositoryUnitTest.dataTest.serviceTest

import com.aura.main.data.service.network.interfaces.RetrofitService
import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk


import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit

/**
 * Test of RetrofitService interface
 */
class RetrofitServiceTest {

    @MockK
    lateinit var retrofit: Retrofit

    private lateinit var retrofitService: RetrofitService

    @Before
    fun setUp() {
        retrofitService = mockk<RetrofitService>()
        retrofit = mockk<Retrofit>()
        // Mock retrofit behavior using MockK
        coEvery { retrofit.create(RetrofitService::class.java) } returns retrofitService
    }


    /**
     * Test of the retrofit login request.
     */
    @Test
    fun `login - calls Retrofit endpoint with login request`() = runTest {
        val loginRequest = LoginRequest("1234", "password")
        val mockResponse = Response.success(LoginResponse(true)) // Mock response object

        // Mock the login method to return the mock response
        coEvery { retrofitService.login(loginRequest) } returns mockResponse

        // Call the login method with the login request
        val actualResponse = retrofitService.login(loginRequest)

        // Verify that the actual response is not null
        assertNotNull(actualResponse)

        // Verify that the actual response body is equal to the mock response body
        assertEquals(mockResponse.body(), actualResponse.body())
    }

    /**
     * Test of the retrofit transfer request.
     */
    @Test
    fun `transfer - calls Retrofit endpoint with transfer request`() = runTest {
        val transferRequest = TransferRequest("1234", "5678", 10.0)
        val mockResponse = Response.success(TransferResponse(true)) // Mock response object

        // Mock the transfer method to return the mock response
        coEvery { retrofitService.transfer(transferRequest) } returns mockResponse

        // Call the transfer method with the transfer request
        val actualResponse = retrofitService.transfer(transferRequest)

        // Verify that the actual response is not null
        assertNotNull(actualResponse)

        // Verify that the actual response body is equal to the mock response body
        assertEquals(mockResponse.body(), actualResponse.body())
    }

    /**
     * Test of the retrofit getUserAccounts request.
     */
    @Test
    fun `getUserAccounts - calls Retrofit endpoint with user ID`() = runTest {
        val userId = "12345"
        val mockResponse = Response.success(listOf(UserAccount("1",false,100.0), UserAccount("2",true,2000.0))) // Mock response object

        // Mock the getUserAccounts method to return the mock response
        coEvery { retrofitService.getUserAccounts(userId) } returns mockResponse

        // Call the getUserAccounts method with the user ID
        val actualResponse = retrofitService.getUserAccounts(userId)

        // Verify that the actual response is not null
        assertNotNull(actualResponse)

        // Verify that the actual response size is equal to the mock response size
        assertEquals(mockResponse.body()?.size, actualResponse.body()?.size ?: 0)

    }


}