package com.aura.unitTest.mainTest.uiTest

import androidx.lifecycle.SavedStateHandle
import com.aura.main.data.repository.TransferRepository
import com.aura.main.data.service.network.NetworkException
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.home.HomeContent
import com.aura.main.model.transfer.TransferContent
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import com.aura.main.ui.transfer.TransferViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Test


/**
 * Test of the TransferViewModel
 */
class TransferViewModelTest {


    private val mockTransferRepository = mockk<TransferRepository>()
    private val mockSavedStateHandle = mockk<SavedStateHandle>()



    /**
     * Test transfer method - Success
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `transfer - successful transfer`() = runTest {
        val identifier = "1234"
        val target = "5678"
        val amount = 200.0
        val mockResponse = TransferResponse(true)

        // Mock the loginRepository to return a successful response
        coEvery { mockTransferRepository.transfer(TransferRequest(identifier, target,amount)) } returns mockResponse
        coEvery { mockSavedStateHandle.get<String>(AppConstants.KEY_USER_ID) } returns identifier
        val transferViewModel = TransferViewModel(mockTransferRepository,mockSavedStateHandle)
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        transferViewModel.transfer(target,"$amount")
        // Verify that the LCE state is updated with LoginContent(true, true)

        val expectedContent = ScreenState.Content(TransferContent(fieldIsOK = true,result = true))
        val actualContent = transferViewModel.lceState.first() as ScreenState.Content // Cast to Content
        TestCase.assertEquals(expectedContent.data.result, actualContent.data.result)



    }


    /**
     * Test transfer method - fail
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `transfer - failed transfer`() = runTest {
        val identifier = "1234"
        val target = "5678"
        val amount = 200.0
        val mockResponse = TransferResponse(false)

        // Mock the loginRepository to return a successful response
        coEvery { mockTransferRepository.transfer(TransferRequest(identifier, target,amount)) } returns mockResponse
        coEvery { mockSavedStateHandle.get<String>(AppConstants.KEY_USER_ID) } returns identifier
        val transferViewModel = TransferViewModel(mockTransferRepository,mockSavedStateHandle)
        // set the dispatcher of the coroutine
        Dispatchers.setMain(UnconfinedTestDispatcher())
        // Call the login method on the ViewModel
        transferViewModel.transfer(target,"$amount")
        // Verify that the LCE state is updated with LoginError with the expected error message
        val actualState = transferViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)
    }


    /**
     * Test transfer method - no internet exception (no internet)
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `transfer - throws NetworkException no internet`() = runTest {
        val identifier = "1234"
        val target = "5678"
        val amount = 200.0
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = false,isConnectFail = true)

        coEvery {mockTransferRepository.transfer(TransferRequest(identifier, target,amount)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.KEY_USER_ID) } returns identifier
        val transferViewModel = TransferViewModel(mockTransferRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        transferViewModel.transfer(target,"$amount")

        val actualState = transferViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }


    /**
     * Test transfer method - server exception (server down)
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `transfer - throws NetworkException server down`() = runTest {
        val identifier = "1234"
        val target = "5678"
        val amount = 200.0
        val exception = NetworkException.NetworkConnectionException(isSocketTimeout = true,isConnectFail = false)

        coEvery {mockTransferRepository.transfer(TransferRequest(identifier, target,amount)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.KEY_USER_ID) } returns identifier
        val transferViewModel = TransferViewModel(mockTransferRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        transferViewModel.transfer(target,"$amount")

        val actualState = transferViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }

    /**
     * Test transfer method - UnknownNetworkException (others)
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `transfer - throws NetworkException others`() = runTest {
        val identifier = "1234"
        val target = "5678"
        val amount = 200.0
        val exception = NetworkException.UnknownNetworkException

        coEvery {mockTransferRepository.transfer(TransferRequest(identifier, target,amount)) } throws exception
        coEvery { mockSavedStateHandle.get<String>(AppConstants.KEY_USER_ID) } returns identifier
        val transferViewModel = TransferViewModel(mockTransferRepository,mockSavedStateHandle)
        Dispatchers.setMain(UnconfinedTestDispatcher())
        transferViewModel.transfer(target,"$amount")

        val actualState = transferViewModel.lceState.first()
        TestCase.assertTrue(actualState is ScreenState.Error)

    }

}