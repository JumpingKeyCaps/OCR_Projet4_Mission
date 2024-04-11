package com.aura.UnitTest.repositoryUnitTest

import com.aura.main.data.repository.TransferRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

/**
 * Tests of the transfer repository.
 */
class TransferRepositoryTest {
    // Mock the AuraApiService interface
    private val mockApiService = mockk<AuraApiService>()
    private val transferRepository = TransferRepository(mockApiService)

    /**
     * Test transfer method with a successful response from the Api service.
     */
    @Test
    fun `transfer - successful transfer`() = runTest {
        val transferRequest = TransferRequest("1234", "5678", 1000.0)
        val expectedResponse = Response.success(TransferResponse(true))

        // Mock the behavior of auraApiService.transfer
        coEvery { mockApiService.transfer(transferRequest) } returns expectedResponse

        // Call the transfer method on the TransferRepository
        val actualResponse = transferRepository.transfer(transferRequest)

        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse.body(), actualResponse)
    }

    /**
     * Test transfer method with a failed response from the Api service.
     */
    @Test
    fun `transfer - failed transfer`() = runTest {
        val transferRequest = TransferRequest("1234", "0000", 1000.0)
        val expectedResponse = Response.success(TransferResponse(false))

        // Mock the behavior of auraApiService.transfer
        coEvery { mockApiService.transfer(transferRequest) } returns expectedResponse

        // Call the transfer method on the TransferRepository
        val actualResponse = transferRepository.transfer(transferRequest)

        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse.body(), actualResponse)
    }

    /**
     * Test transfer method with an empty response body.
     */
    @Test
    fun `transfer - empty response body`() = runTest {
        val transferRequest = TransferRequest("5678", "1234",500.0)
        val emptyResponse = Response.success<TransferResponse>(null)

        // Mock the behavior of auraApiService.transfer to return an empty body
        coEvery { mockApiService.transfer(transferRequest) } returns emptyResponse

        // Call the transfer method on the TransferRepository
        val actualResponse = transferRepository.transfer(transferRequest)

        // Assert that the actual response has 'result' set to false (default)
        assertEquals(false, actualResponse.result)
    }
}