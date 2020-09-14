package com.sostrovsky.travelup.ui.app_init

import androidx.lifecycle.*
import com.sostrovsky.travelup.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Author: Sergey Ostrovsky
 * Date: 24.08.20
 * Email: sergey.ostrovsky.it.dev@gmail.com
 */
class AppInitViewModel: ViewModel() {

    /**
     * The job for all coroutines started by this ViewModel.
     */
    private val viewModelJob = SupervisorJob()

    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     */
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _appInitComplete = MutableLiveData<Boolean>()
    val appInitComplete: LiveData<Boolean>
        get() = _appInitComplete

    init {
        viewModelScope.launch {
            _appInitComplete.value = Repository.init()
        }
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
