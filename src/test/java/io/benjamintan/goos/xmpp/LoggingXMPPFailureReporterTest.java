package io.benjamintan.goos.xmpp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.logging.Logger;

import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Logger.class)
public class LoggingXMPPFailureReporterTest {

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Logger.class);
        Mockito.when(Logger.getLogger("logger")).thenReturn(mock(Logger.class));
    }

    @Test
    public void writesMessageTranslationFailureToLog() {
        final Logger logger = Logger.getLogger("logger") ;
        final LoggingXMPPFailureReporter reporter = new LoggingXMPPFailureReporter(logger);

        reporter.cannotTranslateMessage("auction id", "bad message", new Exception("bad"));

        verify(logger).severe("<auction id> "
                + "Could not translate message \"bad message\" "
                + "because \"java.lang.Exception: bad\"");
    }
}