package com.aura.unitTest.viewModelUnitTest

import com.aura.main.data.repository.LoginRepository
import com.aura.main.model.login.LoginRequest
import com.aura.main.model.login.LoginResponse
import com.aura.main.ui.login.ConnexionState
import com.aura.main.ui.login.LoginViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test

/**
 * Tests of the Login ViewModel.
 */
class LoginViewModelTest {
    // Mock the LoginRepository interface
    private val mockLoginRepository = mockk<LoginRepository>()

    // Create an instance of the ViewModel with the mocked repository
    private val viewModel = LoginViewModel(mockLoginRepository)

    /**
     * Setup
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    /**
     * Test verifierChamps method with valid data.
     */
    @Test
    fun `verifierChamps - valid data`() = runTest {
        val identifier = "546546"
        val motDePasse = "password"

        viewModel.verifierChamps(identifier, motDePasse)

        val actualState = viewModel.etat.first()

        assertEquals(ConnexionState.CHAMPS_REMPLIS, actualState)
    }

    /**
     * Test verifierChamps method with empty data.
     */
    @Test
    fun `verifierChamps - empty data`() = runTest {
        val emptyData = ""

        viewModel.verifierChamps(emptyData, emptyData)

        val actualState = viewModel.etat.first()

        assertEquals(ConnexionState.INITIAL, actualState)
    }

    /**
     * Test seConnecter method with a successful login response from the repository.
     */
    @Test
    fun `seConnecter - successful login`() = runTest {
        val identifier = "1234"
        val motDePasse = "p@sswOrd"
        val expectedResponse = LoginResponse(true)

        // Mock the behavior of loginRepository.login
        coEvery { mockLoginRepository.login(LoginRequest(identifier, motDePasse)) } returns expectedResponse

        viewModel.seConnecter(identifier, motDePasse)

        // Collect the latest value from StateFlow
        val states = viewModel.etat.first()

        // Assert that the state is CONNEXION_REUSSIE
        assertEquals(ConnexionState.CONNEXION_REUSSIE, states)
    }

    /**
     * Test seConnecter method with a failed login response from the repository.
     */
    @Test
    fun `seConnecter - failed login`() = runTest {
        val identifier = "1234"
        val motDePasse = "password"
        val expectedResponse = LoginResponse(false)

        // Mock the behavior of loginRepository.login
        coEvery { mockLoginRepository.login(LoginRequest(identifier, motDePasse)) } returns expectedResponse

        viewModel.seConnecter(identifier, motDePasse)

        // Collect the latest values from StateFlows
        val states = viewModel.etat.first()

        // Assert that the state is CONNEXION_ECHEC
        assertEquals(ConnexionState.CONNEXION_ECHEC, states)
    }

    /**
     * Test seConnecter method with a repository exception.
     */
    @Test
    fun `seConnecter - repository exception`() = runTest {
        val identifier = "user123"
        val motDePasse = "password"
        val expectedErrorMessage = "Error during login"

        // Mock the behavior of loginRepository.login to throw an exception
        coEvery { mockLoginRepository.login(LoginRequest(identifier, motDePasse)) } throws Exception(expectedErrorMessage)

        viewModel.seConnecter(identifier, motDePasse)

        // Collect the latest value from the state Flow
        val actualState = viewModel.etat.first()

        // Assert that the state is ERREUR_CONNEXION
        assertEquals(ConnexionState.ERREUR_CONNEXION, actualState)
    }


}