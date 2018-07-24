package com.abnormallydriven.workmanagerintro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.abnormallydriven.workmanagerintro.common.di.ActivityScope
import com.abnormallydriven.workmanagerintro.common.di.ViewModelFactory
import com.abnormallydriven.workmanagerintro.common.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.android.AndroidInjection
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainActivityViewModel::class.java)


        setupSimpleUploadWorkerViews()
        setupChainWorkerViews()

    }

    private fun setupSimpleUploadWorkerViews() {
        viewModel.liveFileCount.observe(this, Observer { fileCount ->

            if (!viewModel.isUploading()) {
                file_upload_progress_text_view.text = getString(R.string.files_waiting_to_upload_message)
                return@Observer
            }

            if (fileCount == viewModel.totalFileCount) {
                file_upload_progress_text_view.text = getString(R.string.upload_complete_message)
            } else {
                file_upload_progress_text_view.text = "Uploading file ${fileCount + 1} of ${viewModel.totalFileCount}..."
            }
            upload_progress_bar.progress = ((fileCount / viewModel.totalFileCount.toFloat()) * 100).toInt()
        })


        start_simple_upload_worker.setOnClickListener {
            viewModel.startSimpleUpload()
        }
    }


    private fun setupChainWorkerViews() {
        viewModel.analysisInProgress.observe(this, Observer { status ->
            chain_work_progress_bar.progress = status

            if(status == 25){
                chain_upload_progress_text_view.text = "Running pre calculation work"
            } else if(status == 50){
                chain_upload_progress_text_view.text = "Pre calculation completed"
            } else if(status == 75){
                chain_upload_progress_text_view.text = "Running complicated calculation"
            } else if(status ==100){
                chain_upload_progress_text_view.text = "Analysis completed"
            } else {
                chain_upload_progress_text_view.text = "No analysis has been run"
            }
        })

        start_worker_chain_button.setOnClickListener{
            viewModel.startComplicatedAnalysis()
        }

    }
}


@Module
abstract class MainActivityInjectorModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityViewModelModule::class])
    abstract fun contributeActivityInjector(): MainActivity
}

@Module
abstract class MainActivityViewModelModule{
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindCharlieActivityViewModel(charlieActivityViewModel: MainActivityViewModel) : ViewModel
}