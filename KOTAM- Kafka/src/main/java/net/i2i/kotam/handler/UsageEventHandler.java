package net.i2i.kotam.handler;

import net.i2i.kotam.model.UsageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsageEventHandler implements EventHandler<UsageEvent> {

    private static final Logger logger = LoggerFactory.getLogger(UsageEventHandler.class);

    @Override
    public void handle(UsageEvent event) {
        logger.info("USAGE EVENT - Personal Usage ID: {}, Giver ID: {}, Receiver ID: {}, Usage Type: {}, Usage Duration: {}, Usage Data: {}",
                event.getPERSONAL_USAGE_ID(),
                event.getGIVER_ID(),
                event.getRECEIVER_ID(),
                event.getUSAGE_TYPE(),
                event.getUSAGE_DURATION(),
                event.getUSAGE_DATE());
    }
}
