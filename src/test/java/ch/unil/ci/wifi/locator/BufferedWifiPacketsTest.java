package ch.unil.ci.wifi.locator;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class BufferedWifiPacketsTest {

  @Test
  public void should_be_empty_at_beginning() {
    assertThat(new BufferedWifiPackets().read()).isEmpty();
  }

  @Test
  public void should_append_one_packet() {
    WifiPacket packet = mock(WifiPacket.class);

    BufferedWifiPackets buffer = new BufferedWifiPackets();
    buffer.append(packet);
    assertThat(buffer.read()).containsOnly(packet);
  }

  @Test
  public void should_append_several_packet2() {
    WifiPacket packet1 = mock(WifiPacket.class);
    WifiPacket packet2 = mock(WifiPacket.class);

    BufferedWifiPackets buffer = new BufferedWifiPackets();
    buffer.append(packet1);
    buffer.append(packet2);
    assertThat(buffer.read()).containsOnly(packet1, packet2);
  }

  @Test
  public void should_clear_buffer_after_read() {
    BufferedWifiPackets buffer = new BufferedWifiPackets();
    buffer.append(mock(WifiPacket.class));
    assertThat(buffer.read()).isNotEmpty();
    assertThat(buffer.read()).isEmpty();
  }

}
