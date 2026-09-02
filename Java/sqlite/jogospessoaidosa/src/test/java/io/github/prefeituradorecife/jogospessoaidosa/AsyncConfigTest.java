package io.github.prefeituradorecife.jogospessoaidosa;

import io.github.prefeituradorecife.jogospessoaidosa.config.AsyncConfig;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncConfigTest {

    @Test
    void deveCriarExecutorVirtualThread() throws Exception {
        AsyncConfig asyncConfig = new AsyncConfig();
        Executor executor = asyncConfig.virtualTaskExecutor();

        assertNotNull(executor);

        AtomicBoolean executado = new AtomicBoolean(false);
        executor.execute(() -> executado.set(true));

        Thread.sleep(100);
        assertTrue(executado.get());
    }
}
