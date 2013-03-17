package ch.unil.ci.wifi.locator;

public interface LocatorView {

  void setPresenter(LocatorPresenter presenter);

  void setChannel(String text);

  void setCurrentSignal(String text);

  void setBestSignal(String text);

}
