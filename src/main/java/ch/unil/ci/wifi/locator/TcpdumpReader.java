package ch.unil.ci.wifi.locator;

import com.google.common.io.Closeables;

import java.io.BufferedReader;
import java.io.IOException;

public class TcpdumpReader {

  private final BufferedWifiPackets buffer;

  public TcpdumpReader(BufferedWifiPackets buffer) {
    this.buffer = buffer;
  }

  public void read(BufferedReader reader) {
    try {
      for (String line = reader.readLine(); line != null; line = reader.readLine()) {
        try {
          WifiPacket packet = TcpdumpParser.parse(line);
          buffer.append(packet);
        } catch (IllegalArgumentException e) {
          // skip invalid line
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      Closeables.closeQuietly(reader);
    }
  }

}
