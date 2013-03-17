package ch.unil.ci.wifi.locator;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LocatorViewImpl extends JFrame implements LocatorView {

  private static final long serialVersionUID = 1L;

  private final JPanel macPanel = new JPanel(new FlowLayout());
  private final JLabel macLabel = new JLabel("MAC ");
  private final JTextField macTextField = new JTextField(10);

  private final JLabel channelLabel = new JLabel();
  private final JLabel currentSignalLabel = new JLabel();
  private final JLabel bestSignalLabel = new JLabel();

  private LocatorPresenter presenter;

  public LocatorViewImpl() {
    setSize(300, 250);
    setDefaultCloseOperation(LocatorViewImpl.EXIT_ON_CLOSE);

    setLayout(new GridBagLayout());

    macTextField.setText("00:00:00:00:00:00");
    macTextField.addKeyListener(new KeyAdapter() {

      @Override
      public void keyTyped(KeyEvent e) {
        presenter.onSourceChanged(macTextField.getText());
      }

    });

    GridBagConstraints c = new GridBagConstraints();

    c.gridx = 0;
    c.gridy = 0;
    this.add(new JLabel(new ImageIcon(getClass().getResource("/unil.png"))), c);

    macPanel.add(macLabel);
    macPanel.add(macTextField);
    c.gridx = 0;
    c.gridy = 1;
    this.add(macPanel, c);

    c.gridx = 0;
    c.gridy = 2;
    this.add(channelLabel, c);

    c.gridx = 0;
    c.gridy = 3;
    this.add(currentSignalLabel, c);

    c.gridx = 0;
    c.gridy = 4;
    this.add(bestSignalLabel, c);
  }

  public void setPresenter(LocatorPresenter presenter) {
    this.presenter = presenter;
  }

  public void setChannel(final String text) {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        channelLabel.setText("Channel: " + text);
      }

    });
  }

  public void setCurrentSignal(final String text) {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        currentSignalLabel.setText("Current signal: " + text);
      }

    });
  }

  public void setBestSignal(final String text) {
    SwingUtilities.invokeLater(new Runnable() {

      public void run() {
        bestSignalLabel.setText("Best signal: " + text);
      }

    });
  }

}
