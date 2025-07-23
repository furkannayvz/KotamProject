package net.i2i.kotam.handler;

import net.i2i.kotam.model.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


    public class NotificationMessageHandler implements EventHandler<NotificationMessage> {
        private static final Logger logger = LoggerFactory.getLogger(NotificationMessageHandler.class);

        @Override
        public void handle(NotificationMessage event) {
            logger.info("NOTIFICATION - User: {} {}, msisdn: {}, Package Name: {}, Local Date Time : {}, Voice Data {} {}, MB Data: {}, SMS Count : {},  Used Amount : {}, Total Amount: {}, Type: {}  ",
                    event.getName(),
                    event.getLastname(),
                    event.getMsisdn(),
                    event.getPackageName(),
                    event.getLocalDateTime(),
                    event.getVoiceMinutes(),
                    event.getVoiceSeconds(),
                    event.getDataMb(),
                    event.getSmsCount(),
                    event.getUsedAmount(),
                    event.getTotalAmount(),
                    event.getType());
        }
    }



