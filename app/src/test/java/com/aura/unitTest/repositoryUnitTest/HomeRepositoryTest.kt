package com.aura.unitTest.repositoryUnitTest

import com.aura.main.data.repository.HomeRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.model.home.UserAccount
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import junit.framework.Assert.fail
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

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
        val expectedAccounts = listOf(UserAccount("1",true,2000.0), UserAccount("2",false,345.1))
        val successfulResponse = Response.success(expectedAccounts)

        // Mock the behavior of auraApiService.getUserAccounts
        coEvery { mockApiService.getUserAccounts(userId) } returns successfulResponse

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
     * Test getUserAccounts method with an empty response body.
     */
    @Test
    fun `getUserAccounts - empty response body`() = runTest {
        val userId = "user123"
        val emptyResponse = Response.success<List<UserAccount>>(null)

        // Mock the behavior of auraApiService.getUserAccounts to return an empty body
        coEvery { mockApiService.getUserAccounts(userId) } returns emptyResponse

        // Call the getUserAccounts method on the HomeRepository
        val actualAccounts = homeRepository.getUserAccounts(userId)

        // Assert that the actual list is empty
        assertTrue(actualAccounts.isEmpty())
    }

}