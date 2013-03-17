package ch.unil.ci.wifi.locator;

import ch.unil.ci.wifi.locator.TcpdumpParser;
import ch.unil.ci.wifi.locator.WifiPacket;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.fest.assertions.Assertions.assertThat;

public class TcpdumpParserTest {

  @Rule
  public final ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_fail_to_parse_when_unexpected_line_format() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Unexpected line format: foo");
    TcpdumpParser.parse("foo");
  }

  @Test
  public void should_have_current_timestamp() {
    long timestamp = System.currentTimeMillis();

    assertThat(TcpdumpParser.parse("5540 MHz -40dB signal SA:00:00:00:00:00:00").getTimestamp())
        .isGreaterThanOrEqualTo(timestamp)
        .isLessThan(timestamp + 1000);
  }

  @Test
  public void should_extract_channel() {
    assertThat(TcpdumpParser.parse("5540 MHz -40dB signal SA:00:00:00:00:00:00").getChannel()).isEqualTo(5540);
    assertThat(TcpdumpParser.parse("2425 MHz -40dB signal SA:00:00:00:00:00:00").getChannel()).isEqualTo(2425);
  }

  @Test
  public void should_extract_signalLevel() {
    assertThat(TcpdumpParser.parse("5540 MHz -40dB signal SA:00:00:00:00:00:00").getSignalLevel()).isEqualTo(-40);
    assertThat(TcpdumpParser.parse("5540 MHz -68dB signal SA:00:00:00:00:00:00").getSignalLevel()).isEqualTo(-68);
  }

  @Test
  public void should_extract_source() {
    assertThat(TcpdumpParser.parse("5540 MHz -40dB signal SA:00:00:00:00:00:00").getSource()).isEqualTo("00:00:00:00:00:00");
    assertThat(TcpdumpParser.parse("5540 MHz -40dB signal SA:01:23:45:67:89:aB").getSource()).isEqualTo("01:23:45:67:89:aB");
  }

  @Test
  public void reallife() {
    WifiPacket packet = TcpdumpParser
        .parse("12:22:48.244700 1489560287us tsft short preamble wep 24.0 Mb/s 5540 MHz 11a -37dB signal -87dB noise antenna 0 DA:Broadcast BSSID:c0:8a:de:dd:d0:8c (oui Unknown) SA:b8:8d:12:03:4f:a6 (oui Unknown) Data IV:398f Pad 20 KeyID 2");
    assertThat(packet.getChannel()).isEqualTo(5540);
    assertThat(packet.getSignalLevel()).isEqualTo(-37);
    assertThat(packet.getSource()).isEqualTo("b8:8d:12:03:4f:a6");
  }

}
