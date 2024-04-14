package com.aura.unitTest.repositoryUnitTest.dataTest.repositoryTest

import com.aura.main.data.repository.TransferRepository
import com.aura.main.data.service.AuraApiService
import com.aura.main.data.service.network.NetworkException
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

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
        val expectedResponse = TransferResponse(true)

        // Mock the behavior of auraApiService.transfer
        coEvery { mockApiService.transfer(transferRequest) } returns expectedResponse

        // Call the transfer method on the TransferRepository
        val actualResponse = transferRepository.transfer(transferRequest)

        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse, actualResponse)
    }

    /**
     * Test transfer method with a failed response from the Api service.
     */
    @Test
    fun `transfer - failed transfer`() = runTest {
        val transferRequest = TransferRequest("1234", "0000", 1000.0)
        val expectedResponse = TransferResponse(false)

        // Mock the behavior of auraApiService.transfer
        coEvery { mockApiService.transfer(transferRequest) } returns expectedResponse

        // Call the transfer method on the TransferRepository
        val actualResponse = transferRepository.transfer(transferRequest)

        // Assert that the actual response is equal to the expected response
        assertEquals(expectedResponse, actualResponse)
    }


    /**
     * Test transfer method behavior throws an exception on network error (no internet).
     */
    @Test
    fun `transfer - throws exception on no internet error`() = runTest {
        val transferRequest = TransferRequest("1234", "5678",300.0)
        coEvery { mockApiService.transfer(transferRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)
        var exceptionThrown = false
        try {
            transferRepository.transfer(transferRequest)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test transfer method behavior throws an exception on network error (server down).
     */
    @Test
    fun `transfer - throws exception on server down error`() = runTest {
        val transferRequest = TransferRequest("1234", "5678",300.0)
        coEvery { mockApiService.transfer(transferRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
        var exceptionThrown = false
        try {
            transferRepository.transfer(transferRequest)
        } catch (e: Exception) {
            if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }

    /**
     * Test transfer method behavior throws an exception on network error (others).
     */
    @Test
    fun `transfer - throws exception on others error`() = runTest {
        val transferRequest = TransferRequest("1234", "5678",300.0)
        coEvery { mockApiService.transfer(transferRequest) } throws NetworkException.UnknownNetworkException()
        var exceptionThrown = false
        try {
            transferRepository.transfer(transferRequest)
        } catch (e: Exception) {
            if (e is NetworkException.UnknownNetworkException) {
                exceptionThrown = true
            }
        }
        Assert.assertTrue(exceptionThrown)
    }


}