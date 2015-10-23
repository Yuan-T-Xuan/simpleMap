import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.*;

class Node {
        public String ID;
        public double lat, lon;
        public HashMap<String, String> tags = new HashMap<>();
}

class Way {
        public String ID;
        public Vector<String> nodes = new Vector<>();
        public HashMap<String, String> tags = new HashMap<>();
}

public class MainWindow extends JFrame {
        private JTextField jtf = new JTextField();
        private JButton jbtBigger = new JButton("放大");
        private JButton jbtSmaller = new JButton("缩小");
        private JButton jbtSearch = new JButton("搜索地点");
        private JButton jbtUp = new JButton("上");
        private JButton jbtDown = new JButton("下");
        private JButton jbtLeft = new JButton("左");
        private JButton jbtRight = new JButton("右");
        private JButton jbtExtra = new JButton("其他功能");
        private MapPanel canvas = new MapPanel();
        private ExtraWindow extraWindow = new ExtraWindow();
        
        double minlat, minlon, maxlat, maxlon;
        double currMinLat, currMinLon, currMaxLat, currMaxLon;
        
        class ExtraWindow extends JFrame {
                private JButton jbtSearchPath = new JButton("查找最短路线");
                private JButton jbtClearPath = new JButton("清除查找结果");
                private JTextField jtfLocation1 = new JTextField();
                private JTextField jtfLocation2 = new JTextField();
                private ActionListener listener01, listener02;
                
                public ExtraWindow() {
                        setLayout(new GridLayout(6,1));
                        add(new JLabel("输入起点："));
                        add(jtfLocation1);
                        add(new JLabel("输入终点："));
                        add(jtfLocation2);
                        add(jbtSearchPath);
                        add(jbtClearPath);
                        pack();
                        listener01 = new Listener01();
                        listener02 = new Listener02();
                        jbtSearchPath.addActionListener(listener01);
                        jbtClearPath.addActionListener(listener02);
                }
                
                class Listener01 implements ActionListener {
                        public void actionPerformed(ActionEvent e) {
                                canvas.showPath(jtfLocation1.getText(), jtfLocation2.getText());
                                canvas.repaint();
                        }
                }
                
                class Listener02 implements ActionListener {
                        public void actionPerformed(ActionEvent e) {
                                canvas.showPath("怎#么可能有这种名#字", "怎#么可能有这种名#字");
                                canvas.repaint();
                        }
                }
        }
        
        public MainWindow() throws Exception {
                ProcessData();
                
                setTitle("My Open Map");
                setSize(1200, 650);
                setLayout(new BorderLayout());
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                setLocationRelativeTo(null);
                //setResizable(false);
                
                JPanel panel01 = new JPanel(new GridLayout(1,2));
                panel01.add(jbtBigger);
                panel01.add(jbtSmaller);
                JPanel panel02 = new JPanel(new BorderLayout());
                panel02.add(panel01, BorderLayout.WEST);
                panel02.add(jtf, BorderLayout.CENTER);
                JPanel panel03 = new JPanel(new GridLayout(1,2));
                panel03.add(jbtSearch);
                panel03.add(jbtExtra);
                panel02.add(panel03, BorderLayout.EAST);
                add(panel02, BorderLayout.NORTH);
                
                canvas.add(jbtUp, BorderLayout.NORTH);
                jbtUp.setPreferredSize(new Dimension(20, 20));
                canvas.add(jbtDown, BorderLayout.SOUTH);
                jbtDown.setPreferredSize(new Dimension(20, 20));
                canvas.add(jbtLeft, BorderLayout.WEST);
                jbtLeft.setPreferredSize(new Dimension(25, 25));
                canvas.add(jbtRight, BorderLayout.EAST);
                jbtRight.setPreferredSize(new Dimension(25, 25));
                canvas.setBackground(new Color(241, 238, 233));
                add(canvas, BorderLayout.CENTER);
                
                jbtUp.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double temp = 0.1 * (currMaxLat - currMinLat);
                                currMaxLat -= temp;
                                currMinLat -= temp;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtDown.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double temp = 0.1 * (currMaxLat - currMinLat);
                                currMaxLat += temp;
                                currMinLat += temp;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtLeft.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double temp = 0.1 * (currMaxLon - currMinLon);
                                currMaxLon -= temp;
                                currMinLon -= temp;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtRight.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double temp = 0.1 * (currMaxLon - currMinLon);
                                currMaxLon += temp;
                                currMinLon += temp;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtBigger.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double currMidLon = (currMaxLon + currMinLon) / 2;
                                double currMidLat = (currMaxLat + currMinLat) / 2;
                                currMaxLon = currMidLon + (currMaxLon - currMidLon) * 0.8;
                                currMinLon = currMidLon + (currMinLon - currMidLon) * 0.8;
                                currMaxLat = currMidLat + (currMaxLat - currMidLat) * 0.8;
                                currMinLat = currMidLat + (currMinLat - currMidLat) * 0.8;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtSmaller.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                double currMidLon = (currMaxLon + currMinLon) / 2;
                                double currMidLat = (currMaxLat + currMinLat) / 2;
                                currMaxLon = currMidLon + (currMaxLon - currMidLon) * 1.25;
                                currMinLon = currMidLon + (currMinLon - currMidLon) * 1.25;
                                currMaxLat = currMidLat + (currMaxLat - currMidLat) * 1.25;
                                currMinLat = currMidLat + (currMinLat - currMidLat) * 1.25;
                                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                                canvas.repaint();
                        }
                });
                jbtSearch.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                Vector<Double> tmpVec;
                                try {
                                        if(jtf.getText() != null && jtf.getText().compareTo("") != 0) {
                                                tmpVec = canvas.showTarget(jtf.getText());
                                                if(tmpVec != null) {
                                                        currMinLon = tmpVec.elementAt(0);
                                                        currMaxLon = tmpVec.elementAt(1);
                                                        currMinLat = tmpVec.elementAt(2);
                                                        currMaxLat = tmpVec.elementAt(3);
                                                }
                                        }
                                } catch(Exception exp) {
                                        //do nothing...
                                }
                        }
                });
                jbtExtra.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                                extraWindow.setVisible(true);
                        }
                });
        }
        
        public void dragMap(double XXX, double YYY) {
                currMaxLon = currMaxLon + XXX;
                currMinLon = currMinLon + XXX;
                currMaxLat = currMaxLat + YYY;
                currMinLat = currMinLat + YYY;
                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                canvas.repaint();
        }
        
        private void ProcessData() throws Exception {
                BufferedReader MapData = new BufferedReader(new FileReader("map.data"));
                minlat = Double.parseDouble(MapData.readLine());
                minlon = Double.parseDouble(MapData.readLine());
                maxlat = Double.parseDouble(MapData.readLine());
                maxlon = Double.parseDouble(MapData.readLine());
                //
                HashMap<String, Node> Nodes = new HashMap<>();
                HashMap<String, String> NodeNames = new HashMap<>();
                HashMap<String, Way> Ways = new HashMap<>();
                //
                String tmp = MapData.readLine(), tmp2;
                Node tmpNode;
                Way tmpWay;
                while(tmp != null) {
                        if(tmp.compareTo("node") == 0) {
                                tmpNode = new Node();
                                tmpNode.ID = MapData.readLine();
                                tmpNode.lat = Double.parseDouble(MapData.readLine());
                                tmpNode.lon = Double.parseDouble(MapData.readLine());
                                tmp = MapData.readLine();
                                while(tmp.compareTo("#") != 0) {
                                        tmp2 = MapData.readLine();
                                        tmpNode.tags.put(tmp, tmp2);
                                        tmp = MapData.readLine();
                                }
                                //read single node (done)
                                Nodes.put(tmpNode.ID, tmpNode);
                                if(tmpNode.tags.get("name") != null)
                                        NodeNames.put(tmpNode.ID, tmpNode.tags.get("name"));
                        }
                        else if(tmp.compareTo("way") == 0) {
                                tmpWay = new Way();
                                tmpWay.ID = MapData.readLine();
                                tmp = MapData.readLine();
                                while(tmp.compareTo("/") != 0) {
                                        tmpWay.nodes.add(tmp);
                                        tmp = MapData.readLine();
                                }
                                tmp = MapData.readLine();
                                while(tmp.compareTo("#") != 0) {
                                        tmp2 = MapData.readLine();
                                        tmpWay.tags.put(tmp, tmp2);
                                        tmp = MapData.readLine();
                                }
                                Ways.put(tmpWay.ID, tmpWay);
                        }
                        tmp = MapData.readLine();
                }
                canvas.setData(Nodes, NodeNames, Ways, this);
                
                currMinLat = (minlat + maxlat) / 2.0 - 0.01;
                currMaxLat = (minlat + maxlat) / 2.0 + 0.01;
                currMinLon = (minlon + maxlon) / 2.0 - 0.02;
                currMaxLon = (minlon + maxlon) / 2.0 + 0.02;
                canvas.setBorder(currMinLon, currMaxLon, currMinLat, currMaxLat);
                //canvas.showPath("复旦大学", "复旦大学张江校区");
                
                MapData.close();
        }
        
        public static void main(String[] args) throws Exception {
                MainWindow frame = new MainWindow();
                frame.setVisible(true);
        }
}

