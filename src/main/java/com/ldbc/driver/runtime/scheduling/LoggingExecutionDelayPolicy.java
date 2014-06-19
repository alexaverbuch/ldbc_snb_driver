package com.ldbc.driver.runtime.scheduling;

import com.ldbc.driver.Operation;
import com.ldbc.driver.runtime.ConcurrentErrorReporter;
import com.ldbc.driver.temporal.Duration;
import org.apache.log4j.Logger;

public class LoggingExecutionDelayPolicy implements ExecutionDelayPolicy {
    private static Logger logger = Logger.getLogger(LoggingExecutionDelayPolicy.class);

    private final Duration toleratedDelay;

    public LoggingExecutionDelayPolicy(Duration toleratedDelay) {
        this.toleratedDelay = toleratedDelay;
    }

    @Override
    public boolean handleUnassignedScheduledStartTime(Operation<?> operation) {
        String errMsg = String.format("%s\nOperation has no Scheduled Start Time\n%s",
                ConcurrentErrorReporter.whoAmI(this),
                operation.toString());
        logger.error(errMsg);
        return true;
    }

    @Override
    public Duration toleratedDelay() {
        return toleratedDelay;
    }

    @Override
    public boolean handleExcessiveDelay(Operation<?> operation) {
        String errMsg = String.format("%s\nTolerated scheduled start time delay [%s] exceeded on operation:\n\t%s",
                ConcurrentErrorReporter.whoAmI(this), toleratedDelay, operation);
        logger.error(errMsg);
        return true;
    }
}
