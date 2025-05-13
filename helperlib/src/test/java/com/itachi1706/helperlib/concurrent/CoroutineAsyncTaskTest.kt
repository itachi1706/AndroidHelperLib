package com.itachi1706.helperlib.concurrent

import com.itachi1706.helperlib.concurrent.Constants.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.spy
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Ignore
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
        task.bgJob?.join()
        assertEquals(Status.FINISHED, task.status)
        assertEquals("Result", task.result)
    }

    @Test
    fun executeTaskTwiceThrowsException() {
        task.execute("param1")
        assertFailsWith<IllegalStateException> {
            task.execute("param2")
        }
    }

    @Test
    fun cancelTaskBeforeExecution() = runBlocking {
        task.cancel(true)
        assertEquals(Status.PENDING, task.status)
        assertEquals(false, task.isCancelled)
    }

    @Test
    fun cancelTaskDuringExecution() = runBlocking {
        task.execute("param1")
        task.preJob?.join()
        task.cancel(true)
        assertEquals(Status.FINISHED, task.status)
        assertEquals(true, task.isCancelled)
    }

    @Test
    fun publishProgressUpdates() = runBlocking {
        task.execute("param1")
        task.publishProgress(50)
        assertEquals(50, task.progress)
    }

    @Test
    fun executeOnExecutorSuccessfully() = runTest {
        task.executeOnExecutor("param1")
        task.preJob?.join()
        task.bgJob?.join()
        assertEquals(Status.FINISHED, task.status)
        assertEquals("Result", task.result)
    }

    @Test
    fun executeOnExecutorTwiceThrowsException() = runTest {
        task.executeOnExecutor("param1")
        assertFailsWith<IllegalStateException> {
            task.executeOnExecutor("param2")
        }
    }

    @Test
    fun cancelTaskBeforeExecutionOnExecutor() = runTest {
        task.cancel(true)
        assertEquals(Status.PENDING, task.status)
        assertEquals(false, task.isCancelled)
    }

    @Test
    fun cancelTaskDuringExecutionOnExecutor() = runTest {
        task.executeOnExecutor("param1")
        task.preJob?.join()
        task.cancel(true)
        assertEquals(Status.FINISHED, task.status)
        assertEquals(true, task.isCancelled)
    }

    @Test
    @Ignore("TODO. Broken right now")
    fun publishProgressUpdatesOnExecutor() = runTest {
        task.executeOnExecutor("param1")
        task.publishProgress(50)
        assertEquals(50, task.progress)
    }

    private class TestCoroutineAsyncTask : CoroutineAsyncTask<String, Int, String>("TestTask") {
        var result: String? = null
        var progress: Int? = null

        override fun doInBackground(vararg params: String?): String {
            return "Result"
        }

        override fun onPostExecute(result: String?) {
            this.result = result
        }

        override fun onProgressUpdate(vararg params: Int?) {
            this.progress = params[0]
        }
    }
}