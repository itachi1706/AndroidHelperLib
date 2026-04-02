package com.itachi1706.helperlib.concurrent

import com.itachi1706.helperlib.concurrent.Constants.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class CoroutineAsyncTaskTest {

    private lateinit var task: TestCoroutineAsyncTask
    private val testDispatcher = Dispatchers.Unconfined

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        task = spy(TestCoroutineAsyncTask())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun executeTaskSuccessfully() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.bgJob?.await()
        delay(100)
        assertEquals(Status.FINISHED, task.status)
        assertEquals("Result", task.result)
    }

    @Test
    fun executeTaskTwiceThrowsException() {
        runBlocking {
            task.execute("param1")
            delay(50)
            assertFailsWith<IllegalStateException> {
                task.execute("param2")
            }
        }
    }

    @Test
    fun cancelTaskBeforeExecution() {
        task.cancel(true)
        assertEquals(Status.PENDING, task.status)
        assertEquals(false, task.isCancelled)
    }

    @Test
    fun cancelTaskDuringExecution() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.cancel(true)
        delay(100)
        assertEquals(Status.FINISHED, task.status)
        assertEquals(true, task.isCancelled)
    }

    @Test
    fun publishProgressUpdates() = runBlocking {
        task.execute("param1")
        task.publishProgress(50)
        delay(100)
        assertEquals(50, task.progress)
    }

    @Test
    fun executeOnExecutorSuccessfully() = runBlocking {
        task.executeOnExecutor("param1")
        task.preJob?.join()
        task.bgJob?.await()
        delay(100)
        assertEquals(Status.FINISHED, task.status)
        assertEquals("Result", task.result)
    }

    @Test
    fun executeOnExecutorTwiceThrowsException() {
        runBlocking {
            task.executeOnExecutor("param1")
            delay(50)
            assertFailsWith<IllegalStateException> {
                task.executeOnExecutor("param2")
            }
        }
    }

    @Test
    fun cancelTaskBeforeExecutionOnExecutor() {
        task.cancel(true)
        assertEquals(Status.PENDING, task.status)
        assertEquals(false, task.isCancelled)
    }

    @Test
    fun cancelTaskDuringExecutionOnExecutor() = runBlocking {
        task.executeOnExecutor("param1")
        task.preJob?.join()
        task.cancel(true)
        delay(100)
        assertEquals(Status.FINISHED, task.status)
        assertEquals(true, task.isCancelled)
    }

    @Test
    fun publishProgressUpdatesOnExecutor() = runBlocking {
        task.executeOnExecutor("param1")
        task.publishProgress(50)
        delay(100)
        assertEquals(50, task.progress)
    }

    @Test
    fun cancelWithMayInterruptFalseCancelsCompletedTask() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.bgJob?.await()

        task.cancel(false)
        delay(100)

        assertEquals(true, task.isCancelled)
        assertEquals(Status.FINISHED, task.status)
    }

    @Test
    fun publishProgressAfterCancellationDoesNotInvokeProgressCallback() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.cancel(true)

        task.publishProgress(99)
        delay(100)

        assertEquals(null, task.progress)
        assertEquals(0, task.progressUpdateCount)
    }

    @Test
    fun cancelAfterBackgroundCompletionInvokesOnCancelledWithResult() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.bgJob?.await()

        task.cancel(true)
        delay(100)

        assertEquals(1, task.cancelledInvocationCount)
        assertEquals("Result", task.cancelledResult)
    }

    private class TestCoroutineAsyncTask : CoroutineAsyncTask<String, Int, String>("TestTask") {
        var result: String? = null
        var progress: Int? = null
        var progressUpdateCount: Int = 0
        var cancelledResult: String? = null
        var cancelledInvocationCount: Int = 0

        override fun doInBackground(vararg params: String?): String {
            return "Result"
        }

        override fun onPostExecute(result: String?) {
            this.result = result
        }

        override fun onProgressUpdate(vararg params: Int?) {
            this.progress = params[0]
            this.progressUpdateCount++
        }

        override fun onCancelled(result: String?) {
            this.cancelledResult = result
            this.cancelledInvocationCount++
        }
    }

}