package stas.batura.testdownload

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Error
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchListener
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import okhttp3.OkHttpClient
import stas.batura.testdownload.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var _downloader: Fetch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.textValue.observe(this) {text ->
            binding.textField.text = text
        }
    }

    /**
     * инициализируем загрузчик для медиа
     */
    private fun initDownloader(): Fetch {
        val okHttpClient: OkHttpClient = OkHttpClient.Builder().build()

        val fetchConfiguration: FetchConfiguration = FetchConfiguration.Builder(context = this)
//            .setDownloadConcurrentLimit(10)
            .setProgressReportingInterval(1000)
            .setHttpDownloader(OkHttpDownloader(okHttpClient))
            .enableHashCheck(true)
            .setAutoRetryMaxAttempts(50)
            .enableRetryOnNetworkGain(enabled = true)
            .build()

        val dm = Fetch.Impl.getInstance(fetchConfiguration)
        dm.addListener(downloadHandler())
        return dm
    }

    /**
     * хэндлер для обработки загрузок кэшируемых медиа
     */
    private fun downloadHandler(): FetchListener = object : FetchListener {

        override fun onQueued(download: Download, waitingOnNetwork: Boolean) {
            viewModel.addText("download is quaede: $download $waitingOnNetwork")
        }

        override fun onCancelled(download: Download) {
            viewModel.addText("download is canceled: $download")
        }

        override fun onCompleted(download: Download) {
            viewModel.addText("download completed, ${download.file}")
        }

        override fun onDeleted(download: Download) {
            viewModel.addText("download is deleted")
        }

        override fun onDownloadBlockUpdated(
            download: Download,
            downloadBlock: DownloadBlock,
            totalBlocks: Int
        ) {
//            TODO("Not yet implemented")
        }

        override fun onAdded(download: Download) {
            viewModel.addText("Downloading added: $download")
        }

        override fun onPaused(download: Download) {
            viewModel.addText("Downloading paused: $download")
        }

        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            viewModel.addText("Downloading error: $download, $throwable")
        }

        override fun onRemoved(download: Download) {
            viewModel.addText("dowload is removed")
        }

        override fun onProgress(
            download: Download,
            etaInMilliSeconds: Long,
            downloadedBytesPerSecond: Long
        ) {
            viewModel.addText("download progress: ${download.progress} perSec: $downloadedBytesPerSecond")
        }

        override fun onResumed(download: Download) {
//            TODO("Not yet implemented")
        }

        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
            viewModel.addText("download started $download")
        }

        override fun onWaitingNetwork(download: Download) {
            viewModel.addText("download wait: onWaitingNetwork")
        }
    }
}

