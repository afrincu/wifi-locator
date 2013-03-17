package ch.unil.ci.wifi.locator;

import ch.unil.ci.wifi.locator.WifiPacket;
import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class WifiPacketTest {

  @Test
  public void getTimestamp() {
    assertThat(new WifiPacket(0, 0, 0, "").getTimestamp()).isEqualTo(0);
    assertThat(new WifiPacket(42, 0, 0, "").getTimestamp()).isEqualTo(42);
  }

  @Test
  public void getChannel() {
    assertThat(new WifiPacket(0, 0, 0, "").getChannel()).isEqualTo(0);
    assertThat(new WifiPacket(0, 42, 0, "").getChannel()).isEqualTo(42);
  }

  @Test
  public void getSignalLevel() {
    assertThat(new WifiPacket(0, 0, 0, "").getSignalLevel()).isEqualTo(0);
    assertThat(new WifiPacket(0, 0, 42, "").getSignalLevel()).isEqualTo(42);
  }

  @Test
  public void getSource() {
    assertThat(new WifiPacket(0, 0, 0, "").getSource()).isEqualTo("");
    assertThat(new WifiPacket(0, 0, 0, "foo").getSource()).isEqualTo("foo");
  }

}
