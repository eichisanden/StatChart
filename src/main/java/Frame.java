import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

public class Frame {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("vmstat");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Maximum Window
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle rect = env.getMaximumWindowBounds();
        frame.setBounds(rect);

        // Menu Bar
        JMenuBar menubar = new JMenuBar();
        frame.setJMenuBar(menubar);

        // Menu
        JMenu menu1 = new JMenu("File");
        menubar.add(menu1);

        // MenuItem
        JMenuItem menuFileOpen = new JMenuItem("Open");
        JMenuItem menuQuit = new JMenuItem("Quit");
        menu1.add(menuFileOpen);
        menu1.addSeparator();
        menu1.add(menuQuit);

        // FileOpen
        menuFileOpen.setMnemonic(KeyEvent.VK_CONTROL | KeyEvent.VK_O);
        menuFileOpen.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();

            int selected = fileChooser.showOpenDialog(frame);
            if (selected == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();
                System.out.println(file.getName());

                List<XYChart> charts = null;
                try {
                    charts = VmStat.getCharts(file);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                frame.getContentPane().removeAll();
                int numRows = (int) (Math.sqrt(charts.size()) + .5);
                int numColumns = (int) ((double) charts.size() / numRows + 1);
                frame.getContentPane().setLayout(new GridLayout(numRows, numColumns));

                for (XYChart chart : charts) {
                    if (chart != null) {
                        XChartPanel chartPanel = new XChartPanel(chart);
                        frame.add(chartPanel);
                    } else {
                        JPanel chartPanel = new JPanel();
                        frame.getContentPane().add(chartPanel);
                    }
                }

                // Display the window.
                frame.setVisible(true);

            }else if (selected == JFileChooser.CANCEL_OPTION){
                System.out.println("キャンセルされました");
            }else if (selected == JFileChooser.ERROR_OPTION){
                System.out.println("エラー又は取消しがありました");
            }

        });

        // Quit
        menuQuit.setMnemonic(KeyEvent.VK_CONTROL | KeyEvent.VK_Q);
        menuQuit.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }
}
