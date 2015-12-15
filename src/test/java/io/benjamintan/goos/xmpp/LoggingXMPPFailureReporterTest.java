package io.benjamintan.goos.xmpp;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.logging.Logger;

@PrepareForTest(Logger.class)
public class LoggingXMPPFailureReporterTest {

    @Test
    public void writesMessageTranslationFailureToLog() {

        PowerMockito.mock(Logger.class);
        // TODO: WIP
    }
}