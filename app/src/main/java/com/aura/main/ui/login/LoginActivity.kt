package com.aura.main.ui.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.RequiresExtension
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.aura.databinding.ActivityLoginBinding
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.login.LoginContent
import com.aura.main.ui.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
      updateLoginButtonState() // update the button state
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
    override fun afterTextChanged(s: Editable) { }
  }


  /**
   * Life Cycle method : onCreate()
   * Called when the activity is create.
   * @param savedInstanceState the bundle of the saved activity state.
   */
  @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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
  @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
  private fun setupViewListeners(){
    //Set the button login listener.
    binding.login.setOnClickListener { loginViewModel.login(binding.identifier.text.toString(),binding.password.text.toString()) }

    //add text watcher to our views
    binding.identifier.addTextChangedListener(textWatcher)
    binding.password.addTextChangedListener(textWatcher)
  }

  /**
   * Method to collect the current login state and update all views in consequences.
   * @param loginViewModel the ViewModel to use to collect the state flow
   */
  private fun loginUiUpdater(loginViewModel: LoginViewModel){
    //the new way
    lifecycle.coroutineScope.launch {
      loginViewModel.lceState.collect { state ->
        when (state) {

          is ScreenState.Loading -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_SHORT).show()
            binding.loading.visibility = View.VISIBLE
            binding.login.isEnabled = false
            binding.identifier.clearFocus()
            binding.password.clearFocus()
          }

          is ScreenState.Content<LoginContent> -> {
            binding.loading.visibility = View.GONE
            //FIELDS OK !
            if(state.data.fieldIsOK){
              if(state.data.granted){
                //SUCCESS LOGIN !
                binding.login.isEnabled = false
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                //add user ID to the intent extra
                intent.putExtra(AppConstants.KEY_USER_ID, binding.identifier.text.toString())
                startActivity(intent)
                finish()
              }else{
                //ready to login (unlock button)
                binding.login.isEnabled = true
              }
            }else{
              //FIELDS NOT OK  (lock button login)
              binding.login.isEnabled = false
            }
          }


          is ScreenState.Error -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.login.isEnabled = true
          }

        }

      }
    }

  }

  /**
   * Method to update the state of the login button.
   *  - this method call the viewmodel methode to verify if all condition to login is ok
   * to update the current screen state (and unlock the button)
   */
  private fun updateLoginButtonState() = loginViewModel.fieldsCheck(binding.identifier.text.toString(), binding.password.text.toString())




}
