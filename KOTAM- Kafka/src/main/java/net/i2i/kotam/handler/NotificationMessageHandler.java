package net.i2i.kotam.handler;

import net.i2i.kotam.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


    public class NotificationMessageHandler implements EventHandler<NotificationMessage> {
        private static final Logger logger = LoggerFactory.getLogger(NotificationMessageHandler.class);

        @Override
        public void handle(NotificationMessage event) {
            logger.info("NOTIFICATION - User: {} {}, msisdn: {}, Package Name: {}, Threshold : {}, Amount: {}, Timestamp: {}, Remaining: {}, Email : {},  Start Date : {}, End Date: {}, Type: {}  ",
                    event.getName(),
                    event.getLastname(),
                    event.getMsisdn(),
                    event.getPackageName(),
                    event.getThreshold(),
                    event.getAmount(),
                    event.getTimestamp(),
                    event.getRemaining(),
                    event.getEmail(),
                    event.getStartDate(),
                    event.getEndDate(),
                    event.getType());
        }
    }



