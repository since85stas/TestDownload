package stas.batura.testdownload

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.lifecycle.ViewModelProvider
import com.tonyodev.fetch2.*
import com.tonyodev.fetch2core.DownloadBlock
import com.tonyodev.fetch2core.Extras
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import okhttp3.OkHttpClient
import stas.batura.testdownload.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private lateinit var downloader: Fetch

    private val downlUrl = "https://d2btva0juw41cj.cloudfront.net/android-tv-init-data/Beauty+Salons_Explainer.mp4"

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

        downloader = initDownloader()
    }

    override fun onStart() {
        super.onStart()
        viewModel.textValue.observe(this) {text ->
            binding.textField.text = text
        }

        cache()
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
            viewModel.addText("download is quaede: $waitingOnNetwork")
        }

        override fun onCancelled(download: Download) {
            viewModel.addText("download is canceled: ")
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
            viewModel.addText("Downloading added: ")
        }

        override fun onPaused(download: Download) {
            viewModel.addText("Downloading paused: ")
        }

        override fun onError(download: Download, error: Error, throwable: Throwable?) {
            viewModel.addText("Downloading error:  $throwable")
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

        }

        override fun onStarted(
            download: Download,
            downloadBlocks: List<DownloadBlock>,
            totalBlocks: Int
        ) {
            viewModel.addText("download started ")
        }

        override fun onWaitingNetwork(download: Download) {
            viewModel.addText("download wait: onWaitingNetwork")
        }
    }

    private fun cache() {
        val path = applicationContext.cacheDir.absolutePath
        val cachePath = path + "/donwnl.mp4"
        val request = Request(downlUrl, cachePath)
        request.tag = "TAG"
        request.priority = Priority.HIGH
        request.networkType = NetworkType.ALL

        downloader.enqueue(
            request, {}, {}
        )
    }
}

