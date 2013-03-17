package ch.unil.ci.wifi.locator;

import com.google.common.base.Throwables;

import javax.swing.SwingUtilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocatorLauncher {

  public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        final BufferedWifiPackets bufferedPackets = new BufferedWifiPackets();

        final LocatorViewImpl view = new LocatorViewImpl();
        final LocatorPresenter presenter = new LocatorPresenter(view);

        new Thread(new Runnable() {

          public void run() {
            try {
              // tcpdump -e -I -i [interface] -s 256
              ProcessBuilder builder = new ProcessBuilder("tcpdump", "-e", "-I", "-i", "en0", "-s", "256");
              Process process = builder.start();
              BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
              TcpdumpReader tcpdumpReader = new TcpdumpReader(bufferedPackets);
              tcpdumpReader.read(reader);
            } catch (IOException e) {
              Throwables.propagate(e);
            }
          }

        }).start();

        new Thread(new Runnable() {

          public void run() {
            while (true) {
              presenter.process(bufferedPackets.read());

              try {
                Thread.currentThread().sleep(100);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }

        }).start();

        view.setVisible(true);
      }
    });
  }

}
