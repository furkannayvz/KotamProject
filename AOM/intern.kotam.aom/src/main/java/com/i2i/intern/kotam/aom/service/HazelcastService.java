package com.i2i.intern.kotam.aom.service;

import org.i2i.kotam.HazelcastMWOperation;
import org.springframework.stereotype.Service;

@Service
public class HazelcastService {


     // * Hazelcast'e MSISDN gönderir.
     // *
     // * msisdn Gönderilecek müşteri MSISDN
     // * value  Değer (örneğin: "active", "verified" vb.)
     // * return işlem sonucu mesajı

    public String sendMsisdnToHazelcast(String msisdn, String value) {
        return HazelcastMWOperation.put(msisdn, value);
    }
}

// -> insert balance kisminda voltdbye yazarsa hazelcast e de gönder bunu oracla da kaydedecez-
// -> önce oracle paketid ve msisdn gönder - deger true ise voltdbye de yazacaz -
// -> volt true ise hazelcaste yazacaz

