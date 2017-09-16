import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class VertxUnitThrownExceptionClassRule {

    @ClassRule
    public static RunTestOnContext runTestOnContext = new RunTestOnContext();

    // Receives expected exception.
    @Test(timeout = 1000, expected = RuntimeException.class)
    public void okayToThrow(TestContext testContext) throws Exception {
        Async async = testContext.async();
        if (true) {
            throw new RuntimeException("This is fine and fails.");
        }
        async.complete();
    }

    // Fails with timeout.
    @Test(timeout = 1000, expected = NullPointerException.class)
    public void willThrowButNotFailOrComplete(TestContext testContext) throws Exception {
        Async async = testContext.async();
        runTestOnContext.vertx().setTimer(100, l -> {
            String s = null;
            s.length();
            async.complete();
        });
    }

    // Fails with timeout.
    @Test(timeout = 1000, expected = RuntimeException.class)
    public void willThrowButNotFailOrComplete2(TestContext testContext) throws Exception {
        Async async = testContext.async();
        Thread testThread = Thread.currentThread();
        runTestOnContext.vertx().setTimer(100, l -> {
            testContext.assertEquals(testThread, Thread.currentThread());
            if (true) {         // gotta avoid those compilation errors
                throw new RuntimeException("expecting runtime exception to stop tests");
            }
            async.complete();
        });
    }

    // Fails with timeout.
    @Test(timeout = 1000, expected = RuntimeException.class)
    public void shouldFailOnContext(TestContext testContext) throws Exception {
        Async async = testContext.async();
        Thread testThread = Thread.currentThread();
        runTestOnContext.vertx().runOnContext(handler -> {
            testContext.assertEquals(testThread, Thread.currentThread());
            if (true) {
                throw new RuntimeException("expecting runtime exception to stop tests");
            }
            async.complete();
        });
    }

}
