package com.aura.unitTest.mainTest.dataTest.repositoryTest

import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.home.UserAccount
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail

import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

/**
 * Tests of the home repository.
 */
class HomeRepositoryTest {
    // Mock the AuraApiService interface
    private val mockApiService = mockk<AuraApiService>()
    private val homeRepository = HomeRepository(mockApiService)

    /**
     * Test getUserAccounts method with a successful response from the Api service.
     */
    @Test
    fun `getUserAccounts - successful retrieval`() = runTest {
        val userId = "1234"
        val expectedAccounts =
            listOf(UserAccount("1", true, 2000.0), UserAccount("2", false, 345.1))

        // Mock the behavior of auraApiService.getUserAccounts
        coEvery { mockApiService.getUserAccounts(userId) } returns expectedAccounts

        // Call the getUserAccounts method on the HomeRepository
        val actualAccounts = homeRepository.getUserAccounts(userId)

        // Assert that the actual list is equal to the expected list
        assertEquals(expectedAccounts, actualAccounts)
    }

    /**
     * Test getUserAccounts method with a failed response from the Api service.
     */
    @Test
    fun `getUserAccounts - failed response`() = runTest {
        val userId = "user999"
        val expectedStatusCode = 404

        // Mock the behavior of auraApiService.getUserAccounts to return a failed response
        coEvery { mockApiService.getUserAccounts(userId) } throws Exception("Error: $expectedStatusCode")

        try {
            // Call the getUserAccounts method on the HomeRepository (expect an exception)
            homeRepository.getUserAccounts(userId)
            fail("Expected an exception to be thrown")
        } catch (e: Exception) {
            // Assert that the exception message contains the expected status code
            assertTrue(e.message?.contains("Error: $expectedStatusCode") ?: false)
        }
    }


    /**
     * Test getUserAccounts method behavior throws an exception on network error (no internet).
     */
    @Test
    fun `getUserAccounts - throws exception on no internet error`() = runTest {
        val userID = "1234"
        coEvery { mockApiService.getUserAccounts(userID) } throws NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)
        var exceptionThrown = false
        try {
            homeRepository.getUserAccounts(userID)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test getUserAccounts method behavior throws an exception on network error (server down).
     */
    @Test
    fun `getUserAccounts - throws exception on server down error`() = runTest {
        val userID = "1234"
        coEvery { mockApiService.getUserAccounts(userID) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
        var exceptionThrown = false
        try {
            homeRepository.getUserAccounts(userID)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test getUserAccounts method behavior throws an exception on network error (others).
     */
    @Test
    fun `getUserAccounts - throws exception on others error`() = runTest {
        val userID = "1234"
        coEvery { mockApiService.getUserAccounts(userID) } throws NetworkException.UnknownNetworkException()
        var exceptionThrown = false
        try {
            homeRepository.getUserAccounts(userID)
        } catch (e: Exception) {
            if (e is NetworkException.UnknownNetworkException) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }


}