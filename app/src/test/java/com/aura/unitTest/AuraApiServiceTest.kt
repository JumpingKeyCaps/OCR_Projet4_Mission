package com.aura.unitTest

import com.aura.main.data.service.ApiService
import com.aura.main.data.service.AuraApiService
import com.aura.main.model.home.UserAccount
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit


/**
 * Unit tests of the Aura Api service
 */
class AuraApiServiceTest {

    // Mock Retrofit with empty responses by default
    val mockRetrofit = mockk<Retrofit>() {
        coEvery { create(ApiService::class.java) } returns mockk<ApiService>() {
            //for login
            coEvery { login(LoginRequest("1111", "password")) } returns Response.success(LoginResponse(true))
            coEvery { login(LoginRequest("4444", "password")) } returns Response.success(LoginResponse(false))
            //for transfer
            coEvery { transfer(TransferRequest("1111", "2222", 10.0)) } returns Response.success(TransferResponse(true))
            coEvery { transfer(TransferRequest("3333", "4444", 10.0)) } returns Response.success(TransferResponse(false))
            //for getUserAccount
            coEvery { getUserAccounts("9898") } returns Response.success(listOf(UserAccount("1", true,2000.0)))
            coEvery { getUserAccounts("2323") } returns Response.success(emptyList<UserAccount>())
        }
    }


    /**
     * Test of login success
     */
    @Test
    fun `login - successful response`() = runTest {
        val loginRequest = LoginRequest("1111", "password")
        val expectedResponse = Response.success(LoginResponse(true))
        // AuraApiService now receives the mocked Retrofit
        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.login(loginRequest)
        // Assert that the returned response is successful (based on mock behavior)
        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }

    /**
     * Test of login fail
     */
    @Test
    fun `login - failed response`() = runTest {
        val loginRequest = LoginRequest("4444", "password")
        val expectedResponse = Response.success(LoginResponse(false))
        // AuraApiService now receives the mocked Retrofit
        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.login(loginRequest)
        // Assert that the returned response is successful (based on mock behavior)
        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }



    /**
     * Test of transfer success
     */
    @Test
    fun `transfer - successful response`() = runTest {
        val transferRequest = TransferRequest("1111", "2222", 10.0)
        val expectedResponse = Response.success(TransferResponse(true))

        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.transfer(transferRequest)

        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }

    /**
     * Test of transfer fail
     */
    @Test
    fun `transfer - failed response`() = runTest {
        val transferRequest = TransferRequest("3333", "4444", 10.0)
        val expectedResponse = Response.success(TransferResponse(false))

        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.transfer(transferRequest)

        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }



    /**
     * Test of getUserAccounts success
     */
    @Test
    fun `getUserAccounts - successful response`() = runTest {
        val userId = "9898"
        val expectedList = listOf(UserAccount("1", true,2000.0))
        val expectedResponse = Response.success(expectedList)

        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.getUserAccounts(userId)

        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }
    /**
     * Test of getUserAccounts fail
     */
    @Test
    fun `getUserAccounts - failed response`() = runTest {
        val userId = "2323"
        val expectedList = emptyList<UserAccount>()
        val expectedResponse = Response.success(expectedList)

        val auraApiService = AuraApiService(mockRetrofit)
        val actualResponse = auraApiService.getUserAccounts(userId)

        assertTrue(actualResponse.isSuccessful)
        assertEquals(expectedResponse.body(), actualResponse.body()!!)
    }


}