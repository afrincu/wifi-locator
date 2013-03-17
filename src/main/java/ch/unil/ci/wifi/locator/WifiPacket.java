package ch.unil.ci.wifi.locator;

public class WifiPacket {

  private final long timestamp;
  private final int channel;
  private final int signalLevel;
  private final String source;

  public WifiPacket(long timestamp, int channel, int signalLevel, String source) {
    this.timestamp = timestamp;
    this.channel = channel;
    this.signalLevel = signalLevel;
    this.source = source;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public int getChannel() {
    return channel;
  }

  public int getSignalLevel() {
    return signalLevel;
  }

  public String getSource() {
    return source;
  }

}
