package ch.unil.ci.wifi.locator;

import com.google.common.collect.ImmutableList;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LocatorPresenterTest {

  @Test
  public void should_set_presenter_on_view() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    verify(view).setPresenter(presenter);
  }

  @Test
  public void should_initialize_to_NA() {
    LocatorView view = mock(LocatorView.class);
    new LocatorPresenter(view);
    verify(view).setChannel("N/A MHz");
    verify(view).setCurrentSignal("N/A dB");
    verify(view).setBestSignal("N/A dB");
  }

  @Test
  public void should_update_channel() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    presenter.process(ImmutableList.of(new WifiPacket(System.currentTimeMillis(), 2445, 0, "")));
    verify(view).setChannel("2445 MHz");
  }

  @Test
  public void should_not_update_channel_when_no_packet() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    presenter.process(Collections.EMPTY_LIST);
    verify(view).setChannel("N/A MHz");
  }

  @Test
  public void should_compute_best_on_all_packets() throws Exception {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);

    presenter.onSourceChanged("00:00:00:00:00:00");

    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -60, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -62, "00:00:00:00:00:00")));

    Thread.currentThread().sleep(5000);

    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -42, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -62, "00:00:00:00:00:00")));
    presenter.process(ImmutableList.of(new WifiPacket(System.currentTimeMillis(), 2445, -45, "00:00:00:00:00:00")));

    verify(view).setBestSignal("-60 dB");
    verify(view, times(2)).setBestSignal("-42 dB");
  }

  @Test
  public void should_compute_best_only_on_matching_source() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    presenter.onSourceChanged("00:00:00:00:00:00");
    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -60, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -28, "ff:ff:ff:ff:ff:ff")));
    verify(view).setBestSignal("-60 dB");
  }

  @Test
  public void should_compute_current() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);

    presenter.onSourceChanged("00:00:00:00:00:00");

    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -60, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -62, "00:00:00:00:00:00")));
    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -42, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -62, "00:00:00:00:00:00")));
    presenter.process(ImmutableList.of(new WifiPacket(System.currentTimeMillis(), 2445, -45, "00:00:00:00:00:00")));

    verify(view).setCurrentSignal("-60 dB");
    verify(view, times(2)).setCurrentSignal("-42 dB");
  }

  @Test
  public void should_compute_current_only_on_matching_source() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);

    presenter.onSourceChanged("00:00:00:00:00:00");

    presenter.process(ImmutableList.of(
        new WifiPacket(System.currentTimeMillis(), 2445, -60, "00:00:00:00:00:00"),
        new WifiPacket(System.currentTimeMillis(), 2445, -28, "ff:ff:ff:ff:ff:ff")));

    verify(view).setCurrentSignal("-60 dB");
  }

  @Test
  public void should_exclude_old_packets_from_current() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    presenter.onSourceChanged("00:00:00:00:00:00");
    presenter.process(ImmutableList.of(new WifiPacket(System.currentTimeMillis() - 5000, 2445, -60, "00:00:00:00:00:00")));
    verify(view, never()).setCurrentSignal("-60 dB");
  }

  @Test
  public void should_reset_on_source_change() {
    LocatorView view = mock(LocatorView.class);
    LocatorPresenter presenter = new LocatorPresenter(view);
    presenter.onSourceChanged("00:00:00:00:00:00");
    verify(view).setChannel("N/A MHz");
    verify(view, times(2)).setCurrentSignal("N/A dB");
    verify(view, times(2)).setBestSignal("N/A dB");
  }

}
