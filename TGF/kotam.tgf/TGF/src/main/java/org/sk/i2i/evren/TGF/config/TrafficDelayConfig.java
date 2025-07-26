package org.sk.i2i.evren.TGF.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "traffic.delay")
public class TrafficDelayConfig {

    private int data;
    private int sms;
    private int voice;

    // Getter & Setter
    public int getData() { return data; }
    public void setData(int data) { this.data = data; }

    public int getSms() { return sms; }
    public void setSms(int sms) { this.sms = sms; }

    public int getVoice() { return voice; }
    public void setVoice(int voice) { this.voice = voice; }
}
