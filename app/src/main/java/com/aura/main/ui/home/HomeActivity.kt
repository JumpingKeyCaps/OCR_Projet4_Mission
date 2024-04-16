package com.aura.main.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.coroutineScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.main.di.AppConstants
import com.aura.main.model.ScreenState
import com.aura.main.model.home.HomeContent
import com.aura.main.ui.login.LoginActivity
import com.aura.main.ui.transfer.TransferActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.math.RoundingMode

/**
 * The home activity for the app.
 */
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {


  /**
   * The binding for the home layout.
   */
  private lateinit var binding: ActivityHomeBinding

  /**
   * A callback for the result of starting the TransferActivity.
   */
  private val startTransferActivityForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
      if (result.resultCode == Activity.RESULT_OK) getUserAccount()
    }

  /**
   * The Home ViewModel to use with this activity.
   */
  private val homeViewModel: HomeViewModel by viewModels()




  /**
   * Life cycle method called on the creation of the activity.
   *
   * @param savedInstanceState the activity state bundle.
   */
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)

    //Update the ViewModel userId with the retrieved one from intent extra
    homeViewModel.updateUserId(intent.getStringExtra(AppConstants.Keys.KEY_USER_ID)?:getString(R.string.empty_Extra))

    //Setup the listeners
    setupViewsListener()

    //on collect notre Etat pour le homescreen
    homeUiUpdater()

    //call to get the get useraccount
    getUserAccount()
  }


  /**
   * Method to setup all the views listener.
   */
  private fun setupViewsListener(){
    //listener sur le bouton try again
    binding.tryAgainButton.setOnClickListener { getUserAccount()}

    //listerner sur le bouton transfer
    binding.transfer.setOnClickListener {
      val intent = Intent(this@HomeActivity, TransferActivity::class.java)
      intent.putExtra(AppConstants.Keys.KEY_USER_ID, homeViewModel.userId)
      startTransferActivityForResult.launch(intent)}
  }

  /**
   * Method to get the user account.
   *
   */
  private fun getUserAccount(){
    homeViewModel.getUserAccount()
  }

  /**
   * Method to update the User Account UI.
   */
  private fun homeUiUpdater(){
    lifecycle.coroutineScope.launch {
      homeViewModel.lceState.collect{ state ->
        when (state) {

          is ScreenState.Loading -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_SHORT).show()
            binding.loadingHome.visibility = View.VISIBLE
            binding.title.visibility = View.GONE
            binding.balance.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
            binding.tryAgainButton.isEnabled = false
            binding.transfer.isEnabled = false
          }

          is ScreenState.Content<HomeContent> -> {
            binding.balance.text = "${state.data.userBalance.toBigDecimal().setScale(2, RoundingMode.UP).toDouble()}"
            binding.title.visibility = View.VISIBLE
            binding.balance.visibility = View.VISIBLE
            binding.loadingHome.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
            binding.tryAgainButton.isEnabled = false
            binding.transfer.isEnabled = true
          }

          is ScreenState.Error -> {
            Snackbar.make(binding.root,state.message , Snackbar.LENGTH_LONG).show()
            binding.title.visibility = View.GONE
            binding.balance.visibility = View.GONE
            binding.loadingHome.visibility = View.GONE
            binding.tryAgainButton.visibility = View.VISIBLE
            binding.tryAgainButton.isEnabled = true
            binding.transfer.isEnabled = false
          }

        }
      }
    }
  }





  /**
   * Method to the create the Option Menu.
   *
   * @param menu the menu container where to inflate the menu layout.
   * @return a boolean if the inflation is a success.
   */
  override fun onCreateOptionsMenu(menu: Menu?): Boolean
  {
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

  /**
   * Method to handling the click on Menu option items
   *
   * @param item the menu item selected.
   * @return a boolean to show if the select state.
   */
  override fun onOptionsItemSelected(item: MenuItem): Boolean
  {
    return when (item.itemId)
    {
      R.id.disconnect ->{
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

}
