package ch.unil.ci.wifi.locator;

import com.google.common.base.Preconditions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TcpdumpParser {

  private static final Pattern PATTERN = Pattern.compile("(?s).*?([0-9]++) MHz.*?([-0-9]++)dB signal.*?SA:([0-9a-fA-F:]{17}).*+");

  private TcpdumpParser() {
  }

  public static WifiPacket parse(String line) {
    Matcher matcher = PATTERN.matcher(line);
    Preconditions.checkArgument(matcher.matches(), "Unexpected line format: " + line);
    Preconditions.checkState(matcher.groupCount() == 3, "Unexpected group count: " + matcher.groupCount() + " on line: " + line);

    long timestamp = System.currentTimeMillis();
    int channel = Integer.parseInt(matcher.group(1));
    int signalLevel = Integer.parseInt(matcher.group(2));
    String source = matcher.group(3);

    return new WifiPacket(timestamp, channel, signalLevel, source);
  }

}
