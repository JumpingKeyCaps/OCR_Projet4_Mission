package com.aura.main.ui.transfer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.databinding.ActivityTransferBinding
import com.aura.main.ui.home.HomeActivity
import com.aura.main.ui.login.ConnexionState
import com.aura.main.ui.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

/**
 * The transfer activity for the app.
 */
@AndroidEntryPoint
class TransferActivity : AppCompatActivity()
{

  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding

  /**
   * The transfer ViewModel to use with this activity.
   */
  private val transferViewModel: TransferViewModel by viewModels() // Access ViewModel instance


  /**
   * The TextWatcher used to watch the data enter by the user
   * and update the general state of the screen.
   */
  private val textWatcher = object : TextWatcher {
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
      updateTransferButtonState()
    }
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) { }
    override fun afterTextChanged(s: Editable) { }
  }

  private lateinit var userId: String


  override fun onCreate(savedInstanceState: Bundle?)
  {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //on recup l'id user dans le extra de l'intent
    userId = intent.getStringExtra("userId")?:""

    //Setup all listeners
    setupViewListeners()

    //Collect current Login State from the Viewmodel
    TransferUiUpdater(transferViewModel)


  }

  /**
   * Method to setup all the used views listener in the activity.
   */
  fun setupViewListeners(){
    //Set the button login listener.
    binding.transfer.setOnClickListener { transferViewModel.transfer(userId,binding.recipient.text.toString(),binding.amount.text.toString()) }

    //add text watcher to our views
    binding.recipient.addTextChangedListener(textWatcher)
    binding.amount.addTextChangedListener(textWatcher)
  }


  /**
   * Method to collect the current Transfer state and update all views in consequences.
   * @param transferViewModel the ViewModel to use to collect the state flow
   */
  fun TransferUiUpdater(transferViewModel: TransferViewModel){

    transferViewModel.etat.onEach {transferState ->

      // Mettre à jour l'interface utilisateur en fonction de l'état de connexion
      when (transferState) {

        TransferState.IDLE -> {
          withContext(Dispatchers.Main) {
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = false
          }
        }


        TransferState.FIELDS_OK -> {
          withContext(Dispatchers.Main) {
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = true

          }

        }

        TransferState.LOADING -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,transferViewModel.getTransferInfoMessageToShow(TransferState.LOADING) , Snackbar.LENGTH_SHORT).show()
            binding.loading.visibility = View.VISIBLE
            binding.transfer.isEnabled = false
            binding.amount.clearFocus()
            binding.recipient.clearFocus()
          }
        }


        TransferState.ERROR -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,transferViewModel.getTransferInfoMessageToShow(TransferState.ERROR) , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = true

          }
        }

        TransferState.FAIL -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,transferViewModel.getTransferInfoMessageToShow(TransferState.FAIL) , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = true
          }
        }


        TransferState.SUCCESS -> {
          withContext(Dispatchers.Main) {
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = false
            setResult(Activity.RESULT_OK)
            finish()
          }
        }
      }
    }.launchIn(lifecycleScope)

  }

  /**
   * Method to update the state of the transfer button
   * this method call the viewmodel methode to verify if all condition to the transfer is ok
   * to update the current screen state (and unlock the button)
   */
  private fun updateTransferButtonState() = transferViewModel.verifierTransferChamps(binding.recipient.text.toString(), binding.amount.text.toString())


}
