package com.aura.unitTest.viewModelUnitTest

import com.aura.main.data.repository.TransferRepository
import com.aura.main.model.transfer.TransferRequest
import com.aura.main.model.transfer.TransferResponse
import com.aura.main.ui.transfer.TransferState
import com.aura.main.ui.transfer.TransferViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * Tests of the Transfer ViewModel.
 */
class TransferViewModelTest {
    // Mock the TransferRepository interface
    private val mockTransferRepository = mockk<TransferRepository>()

    // Create an instance of the ViewModel with the mocked repository
    private val viewModel = TransferViewModel(mockTransferRepository)

    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }
    /**
     * Test verifierTransferChamps with empty fields.
     */
    @Test
    fun `verifierTransferChamps - empty fields`() = runTest {
        viewModel.verifierTransferChamps("", "")
        assertEquals(TransferState.IDLE, viewModel.etat.first())
    }

    /**
     * Test verifierTransferChamps with valid fields but invalid amount format.
     */
    @Test
    fun `verifierTransferChamps - invalid amount format`() = runTest {
        viewModel.verifierTransferChamps("1234", "invalidAmount")
        assertEquals(TransferState.IDLE, viewModel.etat.first())
    }

    /**
     * Test verifierTransferChamps with valid fields.
     */
    @Test
    fun `verifierTransferChamps - valid fields`() = runTest {
        viewModel.verifierTransferChamps("1234", "123.45")
        assertEquals(TransferState.FIELDS_OK, viewModel.etat.first())
    }

    /**
     * Test transfer method with a successful transfer response from the repository.
     */
    @Test
    fun `transfer - successful transfer`() = runTest {
        val senderId = "1234"
        val receiverId = "5678"
        val amount = 100.0
        val expectedResponse = TransferResponse(true)

        // Mock the behavior of transferRepository.transfer
        coEvery { mockTransferRepository.transfer(TransferRequest(senderId, receiverId, amount)) } returns expectedResponse

        // Call the transfer method on the ViewModel
        viewModel.transfer(senderId, receiverId, amount.toString())

        // Collect the latest values from StateFlows
        val actualStates = viewModel.etat.first()

        // Assert that states is SUCCESS
        assertEquals(TransferState.SUCCESS, actualStates)
    }

    /**
     * Test transfer method with a failed transfer response from the repository.
     */
    @Test
    fun `transfer - failed transfer`() = runTest {
        val senderId = "1234"
        val receiverId = "1234"
        val amount = 100.0
        val expectedResponse = TransferResponse(false)

        // Mock the behavior of transferRepository.transfer
        coEvery { mockTransferRepository.transfer(TransferRequest(senderId, receiverId, amount)) } returns expectedResponse

        // Call the transfer method on the ViewModel
        viewModel.transfer(senderId, receiverId, amount.toString())

        // Collect the latest values from StateFlows
        val actualStates = viewModel.etat.first()

        // Assert that states go through LOADING and then FAIL
        assertEquals(TransferState.FAIL, actualStates)
    }

    /**
     * Test transfer method with a repository exception.
     */
    @Test
    fun `transfer - repository exception`() = runTest {
        val senderId = "1234"
        val receiverId = "5678"
        val amount = 100.0

        // Mock the behavior of transferRepository.transfer to throw an exception
        coEvery { mockTransferRepository.transfer(TransferRequest(senderId, receiverId, amount)) } throws Exception("Transfer failed")

        // Call the transfer method on the ViewModel
        viewModel.transfer(senderId, receiverId, amount.toString())

        // Collect the latest value from the state Flow
        val actualState = viewModel.etat.first()

        // Assert that the state is ERROR
        assertEquals(TransferState.ERROR, actualState)
    }
}