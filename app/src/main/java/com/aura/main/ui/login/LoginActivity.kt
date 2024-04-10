package com.aura.main.ui.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.main.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * The login activity for the app.
 */
@AndroidEntryPoint
class LoginActivity : AppCompatActivity(){

  /**
   * The view binder for the login layout.
   */
  private lateinit var binding: ActivityLoginBinding
  /**
   * The login ViewModel to use with this activity.
   */
  private val loginViewModel: LoginViewModel by viewModels() // Access ViewModel instance


  /**
   * The TextWatcher used to watch the data enter by the user
   * and update the general state of the screen.
   */
  private val textWatcher = object : TextWatcher {
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      updateLoginButtonState() // call to update the button state
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
    override fun afterTextChanged(s: Editable) { }
  }


  /**
   * Life Cycle method : onCreate()
   * Called when the activity is create.
   * @param savedInstanceState the bundle of the saved activity state.
   */
  override fun onCreate(savedInstanceState: Bundle?){
    super.onCreate(savedInstanceState)

    //get the viewbinder and set the content with his root layout
    binding = ActivityLoginBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //Setup all listeners
    setupViewListeners()

    //Collect current Login State from the Viewmodel
    loginUiUpdater(loginViewModel)

  }

  /**
   * Method to setup all the used views listener in the activity.
   */
  private fun setupViewListeners(){
    //Set the button login listener.
    binding.login.setOnClickListener { loginViewModel.seConnecter(binding.identifier.text.toString(),binding.password.text.toString()) }

    //add text watcher to our views
    binding.identifier.addTextChangedListener(textWatcher)
    binding.password.addTextChangedListener(textWatcher)
  }

  /**
   * Method to collect the current login state and update all views in consequences.
   * @param loginViewModel the ViewModel to use to collect the state flow
   */
  private fun loginUiUpdater(loginViewModel: LoginViewModel){

    //on collect notre StateFlow d'etat d'ecran actuel depuis le viewmodel
    //on lance une coroutine

    loginViewModel.etat.onEach {connexionState ->

      // Mettre à jour l'interface utilisateur en fonction de l'état de connexion
      when (connexionState) {

        ConnexionState.INITIAL -> {
          withContext(Dispatchers.Main) {
            binding.login.isEnabled = false
          }
        }

        ConnexionState.CHAMPS_REMPLIS -> {
          withContext(Dispatchers.Main) {
            binding.login.isEnabled = true
          }
        }

        ConnexionState.ERREUR_CONNEXION -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,loginViewModel.getLoginInfoMessageToShow(ConnexionState.ERREUR_CONNEXION) , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.login.isEnabled = true
          }
        }

        ConnexionState.CONNEXION_EN_COURS -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,loginViewModel.getLoginInfoMessageToShow(ConnexionState.CONNEXION_EN_COURS) , Snackbar.LENGTH_SHORT).show()
            binding.loading.visibility = View.VISIBLE
            binding.login.isEnabled = false
            binding.identifier.clearFocus()
            binding.password.clearFocus()
          }
        }

        ConnexionState.CONNEXION_ECHEC -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,loginViewModel.getLoginInfoMessageToShow(ConnexionState.CONNEXION_ECHEC) , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.login.isEnabled = true
          }
        }


        ConnexionState.CONNEXION_REUSSIE -> {
          withContext(Dispatchers.Main) {
            binding.loading.visibility = View.GONE
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            //add user ID to the intent extra
            intent.putExtra("userId", binding.identifier.text.toString())
            startActivity(intent)
            finish()
          }
        }
      }
    }.launchIn(lifecycleScope)

  }


  /**
   * Method to update the state of the login button.
   *  - this method call the viewmodel methode to verify if all condition to login is ok
   * to update the current screen state (and unlock the button)
   */
  private fun updateLoginButtonState() = loginViewModel.verifierChamps(binding.identifier.text.toString(), binding.password.text.toString())

}
