package customthreadpool.task;

import org.junit.Test;
import static org.junit.Assert.*;

public class CallableTaskTest {

    @Test
    public void testGetResult() throws Exception {
        String result = "result";
        CallableTask<String> callableTask = new CallableTask<>(() -> result, 1000);
        callableTask.setResult(callableTask.getCallable().call());
        assertEquals(result, callableTask.getResult());
    }

    @Test
    public void testSetResult() throws Exception {
        String result = "result";
        CallableTask<String> callableTask = new CallableTask<>(() -> result, 1000);
        callableTask.setResult(callableTask.getCallable().call());
        Thread.sleep(1000);
        String anotherResult = "Another result";
        callableTask.setResult(anotherResult);
        assertEquals(anotherResult, callableTask.getResult());

    }

}