package ch.unil.ci.wifi.locator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.Collection;

public class BufferedWifiPackets {

  private final Collection<WifiPacket> buffer = Lists.newArrayList();

  public synchronized void append(WifiPacket packet) {
    buffer.add(packet);
  }

  public synchronized Collection<WifiPacket> read() {
    Collection<WifiPacket> result = ImmutableList.copyOf(buffer);
    buffer.clear();
    return result;
  }

}
