package ch.unil.ci.wifi.locator;

import org.junit.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TcpdumpReaderTest {

  @Test
  public void test() throws Exception {
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("src/test/resources/tcpdump_output.txt")));
    BufferedWifiPackets buffer = mock(BufferedWifiPackets.class);

    new TcpdumpReader(buffer).read(reader);

    verify(buffer, times(2)).append(Mockito.any(WifiPacket.class));
  }

}
