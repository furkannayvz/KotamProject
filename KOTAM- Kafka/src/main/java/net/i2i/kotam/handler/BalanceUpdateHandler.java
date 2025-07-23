package net.i2i.kotam.handler;

import net.i2i.kotam.model.BalanceUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BalanceUpdateHandler implements EventHandler<BalanceUpdate> {

    private static final Logger logger = LoggerFactory.getLogger(BalanceUpdateHandler.class);

    @Override
    public void handle(BalanceUpdate event) {
        logger.info("BALANCE UPDATE - Balance ID: {}, MSISDN: {}, Package ID: {}, Left Minutes: {}, Left SMS: {}, Left Data: {}, Date: {}",
                event.getBALANCE_ID(),
                event.getMSISDN(),
                event.getPACKAGE_ID(),
                event.getBAL_LEFT_MINUTES(),
                event.getBAL_LEFT_SMS(),
                event.getBAL_LEFT_DATA(),
                event.getSDATE());
    }
}

