package com.aura.main.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aura.R
import com.aura.databinding.ActivityHomeBinding
import com.aura.main.ui.login.LoginActivity
import com.aura.main.ui.login.LoginViewModel
import com.aura.main.ui.transfer.TransferActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
      //TODO
    }


  /**
   * The Home ViewModel to use with this activity.
   */
  private val homeViewModel: HomeViewModel by viewModels() // Access ViewModel instance



  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityHomeBinding.inflate(layoutInflater)
    setContentView(binding.root)




    binding.transfer.setOnClickListener {
      startTransferActivityForResult.launch(Intent(this@HomeActivity, TransferActivity::class.java))
    }

    //on collect l'user account et on update l'Ui
    userAccountUpdater()
    //on collect notre Etat pour homescreen
    homeUiUpdater()

    // Récupération de l'ID utilisateur depuis l'intent extra
    val intent = intent
    val userId = intent.getStringExtra("userId")?:""

     homeViewModel.getUserAccounts(userId)


  }

  fun userAccountUpdater(){
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
  fun homeUiUpdater(){
    homeViewModel.etat.onEach { etat ->
      // Mise à jour de l'UI basée sur l'etat du screen home
      when(etat){
        HomeState.IDLE -> {}
        HomeState.LOADING -> {}
        HomeState.SUCCESS -> {}
        HomeState.ERROR -> {}
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
      R.id.disconnect ->
      {
        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
        finish()
        true
      }
      else            -> super.onOptionsItemSelected(item)
    }
  }

}
