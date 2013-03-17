package ch.unil.ci.wifi.locator;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.Iterator;

public class LocatorPresenter {

  private static final int WINDOW = 3000;

  private final LocatorView view;
  private final Collection<WifiPacket> previousPackets = Lists.newArrayList();
  private String source;
  private int bestSignal;

  public LocatorPresenter(LocatorView view) {
    this.view = view;
    this.view.setPresenter(this);
    this.view.setChannel("N/A MHz");
    reset("");
  }

  public void onSourceChanged(String source) {
    reset(source);
  }

  private void reset(String source) {
    this.source = source;
    this.bestSignal = Integer.MIN_VALUE;
    view.setCurrentSignal("N/A dB");
    view.setBestSignal("N/A dB");
  }

  public void process(Collection<WifiPacket> newPackets) {
    previousPackets.addAll(newPackets);
    dropOldPackets();

    int channel = getChannel();
    if (channel != Integer.MIN_VALUE) {
      view.setChannel(intToString(channel) + " MHz");
    }

    int currentSignal = getCurrentSignal();
    view.setCurrentSignal(intToString(currentSignal) + " dB");

    this.bestSignal = Math.max(this.bestSignal, currentSignal);
    view.setBestSignal(intToString(bestSignal) + " dB");
  }

  public String intToString(int value) {
    return value == Integer.MIN_VALUE ? "N/A" : "" + value;
  }

  private void dropOldPackets() {
    Long currentTimestamp = System.currentTimeMillis();

    Iterator<WifiPacket> it = previousPackets.iterator();
    while (it.hasNext()) {
      WifiPacket packet = it.next();
      if (packet.getTimestamp() < currentTimestamp - WINDOW) {
        it.remove();
      }
    }
  }

  private int getCurrentSignal() {
    int result = Integer.MIN_VALUE;

    for (WifiPacket packet : previousPackets) {
      if (packet.getSource().equals(source)) {
        result = Math.max(result, packet.getSignalLevel());
      }
    }

    return result;
  }

  private int getChannel() {
    Iterator<WifiPacket> it = previousPackets.iterator();
    return it.hasNext() ? it.next().getChannel() : Integer.MIN_VALUE;
  }

}
