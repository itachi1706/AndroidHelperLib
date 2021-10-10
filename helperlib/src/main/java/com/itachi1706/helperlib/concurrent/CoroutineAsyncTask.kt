@file:Suppress("unused")

package com.itachi1706.helperlib.concurrent

import com.itachi1706.helperlib.concurrent.Constants.Status
import com.itachi1706.helperlib.helpers.LogHelper
import kotlinx.coroutines.*
import java.util.concurrent.Executors

/**
 * Replacement of AsyncTask in Kotlin Coroutine format
 */
abstract class CoroutineAsyncTask<Params, Progress, Result>(val taskName: String) {

    val logTAG by lazy {
        CoroutineAsyncTask::class.java.simpleName
    }

    companion object {
        private var threadPoolExecutor: CoroutineDispatcher? = null
    }

    var status: Status = Status.PENDING
    var preJob: Job? = null
    var bgJob: Deferred<Result>? = null
    abstract fun doInBackground(vararg params: Params?) : Result
    open fun onProgressUpdate(vararg params: Progress?) {}
    open fun onPostExecute(result: Result?) {}
    open fun onPreExecute() {}
    open fun onCancelled(result: Result?) {}
    protected var isCancelled = false

    /**
     * Executes background task parallel with other background tasks in the queue using
     * the default thread pool
     */
    fun execute(vararg params: Params?) {
        execute(Dispatchers.Default, *params)
    }

    /**
     * Executes background tasks sequentially with other background tasks in the queue using
     * a single thread executor @Executors.newSingleThreadExecutor().
     */
    fun executeOnExecutor(vararg params: Params?) {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
        }
        execute(threadPoolExecutor!!, *params)
    }

    private fun execute(dispatcher: CoroutineDispatcher, vararg params: Params?) {
        if (status != Status.PENDING) {
            when (status) {
                Status.RUNNING -> throw IllegalStateException("Cannot execute task: the task is already running")
                Status.FINISHED -> throw IllegalStateException("Cannot execute task: the task has already been executed (task can only be executed once)")
                else -> {

                }
            }
        }

        status = Status.RUNNING

        // Needs access to main thread as it can setup UI
        GlobalScope.launch(Dispatchers.Main) {
            preJob = launch(Dispatchers.Main) {
                printLog("$taskName onPreExecute started")
                onPreExecute()
                printLog("$taskName onPreExecute completed")
                bgJob = async(dispatcher) {
                    printLog("$taskName doInBackground started")
                    doInBackground(*params)
                }
            }
            preJob!!.join()
            if (!isCancelled) {
                withContext(Dispatchers.Main) {
                    onPostExecute(bgJob!!.await())
                    printLog("$taskName doInBackground finished")
                    status = Status.FINISHED
                }
            }
        }
    }

    fun cancel(mayInterruptIfRunning: Boolean) {
        if (preJob == null || bgJob == null) {
            printLog("$taskName is already cancelled or not yet started")
            return
        }
        if (mayInterruptIfRunning || (!preJob!!.isActive && !bgJob!!.isActive)) {
            isCancelled = true
            status = Status.FINISHED
            if (bgJob!!.isCompleted) {
                GlobalScope.launch(Dispatchers.Main) {
                    onCancelled(bgJob!!.await())
                }
            }
            preJob?.cancel(CancellationException("PreExecute task cancelled"))
            bgJob?.cancel(CancellationException("doInBackground task cancelled"))
            printLog("$taskName is cancelled")
        }
    }

    fun publishProgress(vararg progress: Progress) {
        GlobalScope.launch(Dispatchers.Main) {
            if (!isCancelled) {
                onProgressUpdate(*progress)
            }
        }
    }

    private fun printLog(message: String) {
        LogHelper.d(logTAG, message)
    }
}