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
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.main.ui.login.LoginActivity
import com.aura.main.ui.transfer.TransferActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

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
      if (result.resultCode == Activity.RESULT_OK) updateUserAccount(userId)
    }


  /**
   * The Home ViewModel to use with this activity.
   */
  private val homeViewModel: HomeViewModel by viewModels() // Access ViewModel instance

  private lateinit var userId: String

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)
    //on recup l'id user dans le extra de l'intent
    userId = intent.getStringExtra("userId")?:""

    //listenr sur le bouton try again
    binding.tryAgainButton.setOnClickListener { updateUserAccount(userId)}

    //listerner sur le bouton transfer
    binding.transfer.setOnClickListener {
      val intent = Intent(this@HomeActivity, TransferActivity::class.java)
      intent.putExtra("userId", userId)
      startTransferActivityForResult.launch(
        intent
      )}

    //on collect l'user account et on update l'Ui
    userAccountUpdater()
    //on collect notre Etat pour homescreen
    homeUiUpdater()

    //to get the useraccount in activity start.
    updateUserAccount(userId)


  }

  private fun updateUserAccount(iduser: String){
    homeViewModel.getUserAccounts(iduser)
  }



  private fun userAccountUpdater(){
    homeViewModel.userAccount.onEach { userAccount ->
      // Mise à jour de l'UI basée sur userAccount (si non null)
      if (userAccount != null) {
        // ... (par exemple, afficher le nom d'utilisateur, la photo de profil, etc.)


        binding.balance.text = "${userAccount.balance}"


      } else {
        // Gérer le cas où aucun compte principal n'est trouvé
        // ... (par exemple, afficher un message)
      }
    }.launchIn(lifecycleScope)
  }
  private fun homeUiUpdater(){
    homeViewModel.etat.onEach { etat ->
      // Mise à jour de l'UI basée sur l'etat du screen home
      when(etat){
        HomeState.IDLE -> {
          withContext(Dispatchers.Main) {
            binding.title.visibility = View.VISIBLE
            binding.balance.visibility = View.VISIBLE
            binding.loadingHome.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
            binding.tryAgainButton.isEnabled = false
            binding.transfer.isEnabled = false
          }
        }
        HomeState.LOADING -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,homeViewModel.getHomeInfoMessageToShow(HomeState.LOADING) , Snackbar.LENGTH_SHORT).show()
            binding.title.visibility = View.GONE
            binding.balance.visibility = View.GONE
            binding.loadingHome.visibility = View.VISIBLE
            binding.tryAgainButton.visibility = View.GONE
            binding.tryAgainButton.isEnabled = false
            binding.transfer.isEnabled = false
          }

        }
        HomeState.SUCCESS -> {
          withContext(Dispatchers.Main) {
            binding.title.visibility = View.VISIBLE
            binding.balance.visibility = View.VISIBLE
            binding.loadingHome.visibility = View.GONE
            binding.tryAgainButton.visibility = View.GONE
            binding.tryAgainButton.isEnabled = false
            binding.transfer.isEnabled = true
          }

        }
        HomeState.ERROR -> {
          withContext(Dispatchers.Main) {
            Snackbar.make(binding.root,homeViewModel.getHomeInfoMessageToShow(HomeState.ERROR) , Snackbar.LENGTH_LONG).show()
            binding.title.visibility = View.GONE
            binding.balance.visibility = View.GONE
            binding.loadingHome.visibility = View.GONE
            binding.tryAgainButton.visibility = View.VISIBLE
            binding.tryAgainButton.isEnabled = true
            binding.transfer.isEnabled = false
          }

        }
      }
    }.launchIn(lifecycleScope)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean
  {
    menuInflater.inflate(R.menu.home_menu, menu)
    return true
  }

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
