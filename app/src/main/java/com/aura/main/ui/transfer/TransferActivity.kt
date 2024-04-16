package com.aura.main.ui.transfer

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.aura.R
import com.aura.databinding.ActivityTransferBinding
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.transfer.TransferContent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

/**
 * The transfer activity.
 */
@AndroidEntryPoint
class TransferActivity : AppCompatActivity() {



  /**
   * The binding for the transfer layout.
   */
  private lateinit var binding: ActivityTransferBinding

  /**
   * The transfer ViewModel to use with this activity.
   */
  private val transferViewModel: TransferViewModel by viewModels()

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




  /**
   * Life cycle method called at the creation of the activity.
   *
   * @param savedInstanceState the activity state bundle.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityTransferBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //Update the ViewModel userId with the retrieved one from intent extra
    transferViewModel.updateUserId(intent.getStringExtra(AppConstants.Keys.KEY_USER_ID)?:getString(R.string.empty_Extra))

    //Setup all listeners
    setupViewListeners()

    //Collect current Login State from the Viewmodel
    transferUiUpdater(transferViewModel)

  }


  /**
   * Method to setup all the used views listener in the activity.
   */
  private fun setupViewListeners(){
    //Set the button login listener.
    binding.transfer.setOnClickListener { transferViewModel.transfer(binding.recipient.text.toString(),binding.amount.text.toString()) }

    //add text watcher to our views
    binding.recipient.addTextChangedListener(textWatcher)
    binding.amount.addTextChangedListener(textWatcher)
  }


  /**
   * Method to collect the current Transfer state and update all views in consequences.
   * @param transferViewModel the ViewModel to use to collect the state flow
   */
  private fun transferUiUpdater(transferViewModel: TransferViewModel){
    lifecycle.coroutineScope.launch {
      transferViewModel.lceState.collect { state ->
        when (state) {

          is ScreenState.Loading -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_SHORT).show()
            binding.loading.visibility = View.VISIBLE
            binding.transfer.isEnabled = false
            binding.amount.clearFocus()
            binding.recipient.clearFocus()
          }

          is ScreenState.Content<TransferContent> -> {
            binding.loading.visibility = View.GONE
            //FIELDS OK !
            if(state.data.fieldIsOK){
              if(state.data.result){
                //SUCCESS Transfer !
                binding.transfer.isEnabled = false
                setResult(Activity.RESULT_OK)
                finish()
              }else{
                //ready to login (unlock button)
                binding.transfer.isEnabled = true
              }
            }else{
              //FIELDS NOT OK  (lock button login)
              binding.transfer.isEnabled = false
            }
          }

          is ScreenState.Error -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_LONG).show()
            binding.loading.visibility = View.GONE
            binding.transfer.isEnabled = true
          }
        }
      }
    }

  }

  /**
   * Method to update the state of the transfer button
   * this method call the viewmodel methode to verify if all condition to the transfer is ok
   * to update the current screen state (and unlock the button)
   */
  private fun updateTransferButtonState() = transferViewModel.fieldsCheck(binding.recipient.text.toString(), binding.amount.text.toString())

}
