package com.aura.unitTest.mainTest.dataTest.serviceTest

import com.aura.main.data.service.AuraApiService
import com.aura.main.data.service.network.AuraNetworkServiceImpl
import com.aura.main.data.service.network.NetworkException
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

/**
 * Test class for AraApiService.
 */
class AuraApiServiceTest  {

    // Mock the AuraNetworkService dependency
    private val mockNetworkService = mockk<AuraNetworkServiceImpl>()

    // Create an instance of AuraApiService with the mock
    private val apiService = AuraApiService(mockNetworkService)


    /**
     * Test login method with a successful response.
     */
    @Test
    fun `login - successful response`() {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        val expectedResponse = LoginResponse(true)

        // Mock the network service to return a successful response
        coEvery { mockNetworkService.login(loginRequest) } returns expectedResponse

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Call the login method on the api service
            val actualResponse = apiService.login(loginRequest)

            // Verify that the returned response matches the expected one
            assertEquals(expectedResponse, actualResponse)
        }
    }

    /**
     * Test login method with a failed response.
     */
    @Test
    fun `login - fail response`() {
        val loginRequest = LoginRequest("1234", "p@sswOrd")
        val expectedResponse = LoginResponse(false)

        // Mock the network service to return a failed response
        coEvery { mockNetworkService.login(loginRequest) } returns expectedResponse

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Call the login method on the api service
            val actualResponse = apiService.login(loginRequest)

            // Verify that the returned response matches the expected one
            assertEquals(expectedResponse, actualResponse)
        }
    }

    /**
     * Test login method with a throw exception response (server code).
     */
    @Test
    fun `login - throws NetworkException on server error`() {
        val loginRequest = LoginRequest("1234", "password")
        val statusCode = 500

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.login(loginRequest) } throws NetworkException.ServerErrorException(statusCode)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.login(loginRequest)
            } catch (e: Exception) {
                //check the error code
                if (e is NetworkException.ServerErrorException && e.errorCode == statusCode) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test login method with a throw exception response (no internet).
     */
    @Test
    fun `login - throws NetworkException on no internet`() {
        val loginRequest = LoginRequest("1234", "password")

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.login(loginRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.login(loginRequest)
            } catch (e: Exception) {
                //check the error code
                if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test login method with a throw exception response (server down).
     */
    @Test
    fun `login - throws NetworkException on server down`() {
        val loginRequest = LoginRequest("1234", "password")
        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.login(loginRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)
        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.login(loginRequest)
            } catch (e: Exception) {
                //check the error code
                if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test login method with a throw exception response (others).
     */
    @Test
    fun `login - throws UnknownNetworkException`() {
        val loginRequest = LoginRequest("1234", "password")
        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.login(loginRequest) } throws NetworkException.UnknownNetworkException()
        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.login(loginRequest)
            } catch (e: Exception) {
                //check the error code
                if (e is NetworkException.UnknownNetworkException) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }




    /**
     * Test transfer method with a successful result.
     */
    @Test
    fun `transfer - successful transfer`() {
        val transferRequest = TransferRequest("1234", "5678", 10.0)
        val expectedResponse = TransferResponse(true)

        // Mock the network service to return a successful response
        coEvery { mockNetworkService.transfer(transferRequest) } returns expectedResponse

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Call the transfer method on the api service
            val actualResponse = apiService.transfer(transferRequest)

            // Verify that the returned response matches the expected one
            assertEquals(expectedResponse, actualResponse)
        }
    }

    /**
     * Test transfer method with a failed result.
     */
    @Test
    fun `transfer - fail transfer`() {
        val transferRequest = TransferRequest("1234", "5678", 10.0)
        val expectedResponse = TransferResponse(false)

        // Mock the network service to return a fail result
        coEvery { mockNetworkService.transfer(transferRequest) } returns expectedResponse

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Call the transfer method on the api service
            val actualResponse = apiService.transfer(transferRequest)

            // Verify that the returned response matches the expected one
            assertEquals(expectedResponse, actualResponse)
        }
    }

    /**
     * Test transfer method with a throw exception response (Server code error).
     */
    @Test
    fun `transfer - throws NetworkException on server error`() {
        val transferRequest = TransferRequest("1234", "5678", 10.0)
        val statusCode = 500

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.transfer(transferRequest) } throws NetworkException.ServerErrorException(statusCode)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.transfer(transferRequest)
            } catch (e: Exception) {
                if (e is NetworkException.ServerErrorException && e.errorCode == statusCode) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test transfer method with a throw exception response (No internet).
     */
    @Test
    fun `transfer - throws NetworkConnectionException on no internet`() {
        val transferRequest = TransferRequest("1234", "5678", 10.0)

        // Mock the network service to throw NetworkConnectionException
        coEvery { mockNetworkService.transfer(transferRequest) } throws NetworkException.NetworkConnectionException(false,isConnectFail = true)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.transfer(transferRequest)
            } catch (e: Exception) {
                if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test transfer method with a throw exception response (Server down).
     */
    @Test
    fun `transfer - throws NetworkConnectionException on server down`() {
        val transferRequest = TransferRequest("1234", "4567", 10.0)

        // Mock the network service to throw NetworkConnectionException
        coEvery { mockNetworkService.transfer(transferRequest) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.transfer(transferRequest)
            } catch (e: Exception) {
                if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test transfer method with a throw exception response (others).
     */
    @Test
    fun `transfer - throws UnknownNetworkException`() {
        val transferRequest = TransferRequest("1234", "4567", 10.0)

        // Mock the network service to throw NetworkConnectionException
        coEvery { mockNetworkService.transfer(transferRequest) } throws NetworkException.UnknownNetworkException()

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.transfer(transferRequest)
            } catch (e: Exception) {
                if (e is NetworkException.UnknownNetworkException) {
                    exceptionThrown = true
                }
            }
            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }



    /**
     * Test getUserAccount method with a successful result.
     */
    @Test
    fun `getUserAccounts - successful response`() {
        val userId = "123"
        val expectedUserAccounts = listOf(
            UserAccount("1", false,200.0),
            UserAccount("2", true,1000.0)
        )

        // Mock the network service to return a successful response
        coEvery { mockNetworkService.getUserAccounts(userId) } returns expectedUserAccounts

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Call the getUserAccounts method on the api service
            val actualUserAccounts = apiService.getUserAccounts(userId)

            // Verify that the returned user accounts match the expected ones
            assertEquals(expectedUserAccounts, actualUserAccounts)
        }
    }

    /**
     * Test getUserAccount method with a throw exception response (server error code).
     */
    @Test
    fun `getUserAccounts - throws NetworkException on server error`() {
        val userId = "123"
        val statusCode = 500

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.getUserAccounts(userId) } throws NetworkException.ServerErrorException(statusCode)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.getUserAccounts(userId)
            } catch (e: Exception) {
                if (e is NetworkException.ServerErrorException && e.errorCode == statusCode) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test getUserAccount method with a throw exception response (no internet).
     */
    @Test
    fun `getUserAccounts - throws NetworkException on no internet`() {
        val userId = "123"

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.getUserAccounts(userId) } throws NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.getUserAccounts(userId)
            } catch (e: Exception) {
                if (e is NetworkException.NetworkConnectionException && e.isConnectFail) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test getUserAccount method with a throw exception response (server down).
     */
    @Test
    fun `getUserAccounts - throws NetworkException on server down`() {
        val userId = "123"

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.getUserAccounts(userId) } throws NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.getUserAccounts(userId)
            } catch (e: Exception) {
                if (e is NetworkException.NetworkConnectionException && e.isSocketTimeout) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }

    /**
     * Test getUserAccount method with a throw exception response (others).
     */
    @Test
    fun `getUserAccounts - throws UnknownNetworkException`() {
        val userId = "123"

        // Mock the network service to throw an exception on error
        coEvery { mockNetworkService.getUserAccounts(userId) } throws NetworkException.UnknownNetworkException()

        // Wrap the test logic in runTest for coroutine scope
        runTest {
            // Custom function to verify the exception is thrown
            var exceptionThrown = false
            try {
                apiService.getUserAccounts(userId)
            } catch (e: Exception) {
                if (e is NetworkException.UnknownNetworkException) {
                    exceptionThrown = true
                }
            }

            // Assert that the exception was thrown
            assertTrue(exceptionThrown)
        }
    }


}