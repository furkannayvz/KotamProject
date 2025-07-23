package net.i2i.kotam.handler;

import net.i2i.kotam.model.CustomerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerEventHandler implements EventHandler<CustomerEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CustomerEventHandler.class);

    @Override
    public void handle(CustomerEvent event) {
        logger.info("CUSTOMER EVENT - MSISDN: {}, Name: {} {}, E-mail: {}, Password: {}, National ID: {}, Date: {}",
                event.getMSISDN(),
                event.getNAME(),
                event.getSURNAME(),
                event.getEMAIL(),
                event.getPASSWORD(),
                event.getNATIONAL_ID(),
                event.getSDATE());
    }
}
