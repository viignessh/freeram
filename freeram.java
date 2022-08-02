import etc. *;

import java.io. *;
import java.awt. *;
import java.awt.event. *;
import javax.swing. *;
import com.formdev.flatlaf.intellijthemes.FlatCarbonIJTheme;

public class freeram {
    public static boolean changed = false;
    public static boolean pause = false;
    public static String timestring;
    public static void main(String[] args)throws IOException {
        File fi = new File("C:\\FreeRam\\config\\");
        if (!fi.exists()) {
            fi.mkdirs();
        }

        fi = new File("C:\\FreeRam\\config\\version.ini");
        if (fi.exists() == false) {
            fi.createNewFile();
            FileWriter fw = new FileWriter("C:\\FreeRam\\config\\version.ini");
            fw.write("2.3");
            fw.close();
            new startup();
        } 

        fi = new File("C:\\FreeRam\\config\\freeram.ini");
        if (fi.exists() == false) {
            fi.createNewFile();
            FileWriter fw = new FileWriter("C:\\FreeRam\\config\\freeram.ini");
            fw.write("60");
            fw.close();
        }

        BufferedReader br = new BufferedReader(
            new FileReader("C:\\Freeram\\config\\freeram.ini")
        );
        timestring = br.readLine();
        br.close();

        //  LAF
        try {
            UIManager.setLookAndFeel(new FlatCarbonIJTheme());
        } catch (Exception ex) {
            System
                .err
                .println("Failed to initialize LaF");
        }

        //  Frame
        JFrame f = new JFrame("Free Ram");
        f.setResizable(false);
        f.setLocationByPlatform(true);
        f.setSize(315, 220);
        f.setLayout(null);
        f.setAlwaysOnTop(true);
        ImageIcon img1 = new ImageIcon("C:\\Freeram\\src\\ram.png");
        f.setIconImage(img1.getImage());
        f.setLocationRelativeTo(null);
        f.setVisible(true);

        //  Label
        JLabel l1 = new JLabel("Change Timings: ");
        //  Debug: System.out.println("L1= " + l1.getPreferredSize());
        l1.setFont(new Font("Roboto", Font.PLAIN, 20));
        l1.setBounds(20, 10, 152, 26);

        JLabel l2 = new JLabel("Manual Clean:");
        l2.setFont(new Font("Roboto", Font.PLAIN, 20));
        l2.setBounds(20, 115, 125, 26);

        JLabel l3 = new JLabel("⚠️ Press the close button to minimize to tray");
        l3.setFont(new Font("Roboto", Font.PLAIN, 12));
        l3.setBounds(20, 145, 295, 26);

        //  TextField
        JTextField t1 = new JTextField(timestring);
        t1.setBounds(20, 45, 260, 35);
        t1.setFont(new Font("Roboto", Font.PLAIN, 17));
        t1.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                String timestr = t1.getText();
                try {
                    if (!timestring.equals(timestr)) {
                        FileWriter fw2 = new FileWriter("C:\\FreeRam\\config\\freeram.ini");
                        fw2.append(timestr);
                        timestring = timestr;
                        fw2.close();
                        changed = true;
                        System
                            .out
                            .println("User input changed to " + timestr);
                    }
                } catch (IOException f) {
                    System
                        .out
                        .println("An error occurred.");
                    f.printStackTrace();
                }
            }
        });

        //  CheckBox
        JCheckBox c1 = new JCheckBox("Pause auto clean", pause);
        // System.out.println("c1= " + c1.getPreferredSize());
        c1.setBounds(20, 82, 112, 30);
        c1.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                pause = c1.isSelected();
            }
        });

        //  Button
        JButton b1 = new JButton("Click Here");
        b1.putClientProperty("JButton.buttonType", "roundRect");
        b1.setBounds(150, 115, 100, 30);
        b1.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    Runtime
                        .getRuntime()
                        .exec("cmd /c C:\\FreeRam\\bin\\rammap.exe -ew", null);
                } catch (IOException a) {
                    a.printStackTrace();
                }
            }
        });

        f.addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) {
                f.setVisible(false);
                JOptionPane.showMessageDialog(f, "Freeram has been minimized to tray!    ");
            }
        });

        //  Add components
        f.add(l1);
        f.add(l2);
        f.add(l3);
        f.add(t1);
        f.add(c1);
        f.add(b1);
        f.repaint();

        //  System Tray
        if (!SystemTray.isSupported()) {
            System
                .out
                .println("System tray is not supported!");
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        //  Tray icon
        Image image = Toolkit
            .getDefaultToolkit()
            .getImage("C:\\Freeram\\src\\ram.png");
        TrayIcon trayIcon = new TrayIcon(image, "FreeRam");
        JPopupMenu popup = new JPopupMenu();
        trayIcon.setImageAutoSize(true);
        try {
            systemTray.add(trayIcon);
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }

        //  Menu items
        JMenuItem show = new JMenuItem("Show");
        show.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                f.setVisible(true);
                f.repaint();
            }
        });
        popup.add(show);

        JMenuItem clean = new JMenuItem("Clean");
        clean.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                try {
                    Runtime
                        .getRuntime()
                        .exec("cmd /c C:\\FreeRam\\bin\\rammap.exe -ew", null);
                } catch (IOException c) {
                    c.printStackTrace();
                }
            }
        });
        popup.add(clean);

        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        popup.add(close);

        //  Tray events
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (e.getClickCount() >= 2) {
                        f.setVisible(true);
                        f.repaint();
                    }
                }
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    popup.setLocation(e.getX() - 80, e.getY() - 50);
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                Timer t = new Timer(2500, new ActionListener() {
                    @Override public void actionPerformed(ActionEvent e) {
                        popup.setVisible(false);
                    }
                });
                t.setRepeats(false);
                t.start();
            }
        });

        //  RAMMAP exec
        Runtime
            .getRuntime()
            .exec("cmd /c C:\\FreeRam\\bin\\rammap.exe -ew", null);
        try {
            for (;;) { //infinite
                br = new BufferedReader(new FileReader("C:\\Freeram\\config\\freeram.ini"));
                String timestr = br.readLine();
                br.close();

                int time = Integer.valueOf(timestr);
                time = time * 60000;
                int divtime = time / 1000;
                int flag = 0;

                while (changed == false) {
                    new wait(1000);
                    flag++;
                    if (changed == true) {
                        break;
                    }
                    if (flag == divtime) {
                        break;
                    }

                    while (pause == true) {
                        System
                            .out
                            .print("");
                        if (pause == false) {
                            break;
                        }
                    }
                }

                Runtime
                    .getRuntime()
                    .exec("cmd /c C:\\FreeRam\\bin\\rammap.exe -ew", null);
                changed = false;
            }
        } catch (IOException c) {
            c.printStackTrace();
        }
    }
}
