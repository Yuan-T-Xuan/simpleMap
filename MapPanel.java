import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/*
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
 */

public class MapPanel extends JPanel {
        private int _moveX, _moveY;
        private int _initX, _initY;
        private int _currX, _currY;
        //drag control
        private MainWindow mainWindow = null;
        //also drag control
        
        private HashMap<String, Node> Nodes;
        private HashMap<String, String> NodeNames;	//ID to name
        private HashMap<String, Way> Ways;
        private Vector<Vector<Node>> Locations;
        
        private double leftX, rightX;
        private double upY, downY;
        //
        private int L, R;
        private Vector<Integer> U = new Vector<>();
        private Vector<Integer> D = new Vector<>();
        
        private HashSet<String> currNodes = new HashSet<>();
        private HashMap<String, String> currNames = new HashMap<>();    //映射关系是从ID到名称
        private HashSet<String> currWays = new HashSet<>();
        //
        private Map<String, Vector<String>> nodeLinks = new HashMap<>();
        private Vector<String> pathNodes = new Vector<>();
        private HashMap<String, String> prevNode = new HashMap<>();
        //
        
        private Set<String> points2draw = new HashSet<>();
        
        public void showPath(String location1, String location2) {
                //先确定两个location对应的ID
                String IDOne, IDTwo;
                IDOne = getIdFromName(location1);
                IDTwo = getIdFromName(location2);
                
                //System.out.println("Prev L1.lat: " + Nodes.get(IDOne).lat + " lon: " + Nodes.get(IDOne).lon);
                //System.out.println("Prev L2.lat: " + Nodes.get(IDTwo).lat + " lon: " + Nodes.get(IDTwo).lon);
                pathNodes.clear();
                if(IDOne == null || IDTwo == null) {
                        return;
                }
                /*
                 如果IDOne/Two不被连在Way里，则选一个邻近的Way里的点为IDOne/Two。
                 */
                if(nodeLinks.get(IDOne) == null) {
                        double Left, Right, Up, Down;
                        Left = Nodes.get(IDOne).lon - 0.001;
                        Right = Nodes.get(IDOne).lon + 0.001;
                        Up = Nodes.get(IDOne).lat - 0.001;
                        Down = Nodes.get(IDOne).lat + 0.001;
                        
                        int LL, RR;
                        Vector<Integer> UU = new Vector<>();
                        Vector<Integer> DD = new Vector<>();
                        int left, right, middle, answer = -1;
                        
                        left = 0;
                        right = Locations.size() - 1;
                        answer = (left + right) / 2;
                        while(left < right) {
                                middle = (left + right) / 2;
                                if(Locations.elementAt(middle).elementAt(0).lon >= Left) {
                                        answer = middle;
                                        right = middle - 1;
                                }
                                else {
                                        left = middle + 1;
                                }
                        }
                        LL = answer;
                        
                        left = 0;
                        right = Locations.size() - 1;
                        answer = (left + right) / 2;
                        while(left < right) {
                                middle = (left + right) / 2;
                                if(Locations.elementAt(middle).elementAt(0).lon >= Right) {
                                        answer = middle;
                                        right = middle - 1;
                                }
                                else {
                                        left = middle + 1;
                                }
                        }
                        RR = answer;
                        
                        for(int i = LL; i <= RR; i++) {
                                left = 0;
                                right = Locations.elementAt(i).size() - 1;
                                answer = (left + right) / 2;
                                while(left < right) {
                                        middle = (left + right) / 2;
                                        if(Locations.elementAt(i).elementAt(middle).lat >= Up) {
                                                answer = middle;
                                                right = middle - 1;
                                        }
                                        else {
                                                left = middle + 1;
                                        }
                                }
                                UU.add(answer);
                                
                                left = 0;
                                right = Locations.elementAt(i).size() - 1;
                                answer = (left + right) / 2;
                                while(left < right) {
                                        middle = (left + right) / 2;
                                        if(Locations.elementAt(i).elementAt(middle).lat >= Down) {
                                                answer = middle;
                                                right = middle - 1;
                                        }
                                        else {
                                                left = middle + 1;
                                        }
                                }
                                DD.add(answer);
                        }
                        
                        String tempID;
                        boolean flag = false;
                        for(int i = LL; i <= RR; i++) {
                                for(int j = UU.elementAt(i-LL); j <= DD.elementAt(i-LL); j++) {
                                        tempID = Locations.elementAt(i).elementAt(j).ID;
                                        if(nodeLinks.get(tempID) != null && Nodes.get(tempID).lat - Nodes.get(IDOne).lat <= 0.001
                                           && Nodes.get(tempID).lat - Nodes.get(IDOne).lat >= -0.001) {
                                                IDOne = tempID;
                                                flag = true;
                                                break;
                                        }
                                }
                                if(flag) {
                                        //System.out.println("- L1 Changed -");
                                        break;
                                }
                        }
                }
                //System.out.println("Curr L1.lat: " + Nodes.get(IDOne).lat + " lon: " + Nodes.get(IDOne).lon);
                if(nodeLinks.get(IDTwo) == null) {
                        double Left, Right, Up, Down;
                        Left = Nodes.get(IDTwo).lon - 0.001;
                        Right = Nodes.get(IDTwo).lon + 0.001;
                        Up = Nodes.get(IDTwo).lat - 0.001;
                        Down = Nodes.get(IDTwo).lat + 0.001;
                        
                        int LL, RR;
                        Vector<Integer> UU = new Vector<>();
                        Vector<Integer> DD = new Vector<>();
                        int left, right, middle, answer = -1;
                        
                        left = 0;
                        right = Locations.size() - 1;
                        answer = (left + right) / 2;
                        while(left < right) {
                                middle = (left + right) / 2;
                                if(Locations.elementAt(middle).elementAt(0).lon >= Left) {
                                        answer = middle;
                                        right = middle - 1;
                                }
                                else {
                                        left = middle + 1;
                                }
                        }
                        LL = answer;
                        
                        left = 0;
                        right = Locations.size() - 1;
                        answer = (left + right) / 2;
                        while(left < right) {
                                middle = (left + right) / 2;
                                if(Locations.elementAt(middle).elementAt(0).lon >= Right) {
                                        answer = middle;
                                        right = middle - 1;
                                }
                                else {
                                        left = middle + 1;
                                }
                        }
                        RR = answer;
                        
                        for(int i = LL; i <= RR; i++) {
                                left = 0;
                                right = Locations.elementAt(i).size() - 1;
                                answer = (left + right) / 2;
                                while(left < right) {
                                        middle = (left + right) / 2;
                                        if(Locations.elementAt(i).elementAt(middle).lat >= Up) {
                                                answer = middle;
                                                right = middle - 1;
                                        }
                                        else {
                                                left = middle + 1;
                                        }
                                }
                                UU.add(answer);
                                
                                left = 0;
                                right = Locations.elementAt(i).size() - 1;
                                answer = (left + right) / 2;
                                while(left < right) {
                                        middle = (left + right) / 2;
                                        if(Locations.elementAt(i).elementAt(middle).lat >= Down) {
                                                answer = middle;
                                                right = middle - 1;
                                        }
                                        else {
                                                left = middle + 1;
                                        }
                                }
                                DD.add(answer);
                        }
                        
                        String tempID;
                        boolean flag = false;
                        for(int i = LL; i <= RR; i++) {
                                for(int j = UU.elementAt(i-LL); j <= DD.elementAt(i-LL); j++) {
                                        tempID = Locations.elementAt(i).elementAt(j).ID;
                                        if(nodeLinks.get(tempID) != null && Nodes.get(tempID).lat - Nodes.get(IDTwo).lat <= 0.001
                                           && Nodes.get(tempID).lat - Nodes.get(IDTwo).lat >= -0.001) {
                                                IDTwo = tempID;
                                                flag = true;
                                                break;
                                        }
                                }
                                if(flag) {
                                        //System.out.println("- L2 Changed -");
                                        break;
                                }
                        }
                }
                //System.out.println("Curr L2.lat: " + Nodes.get(IDTwo).lat + " lon: " + Nodes.get(IDTwo).lon);
                
                /*
                 设一个队列，
                 又设一个记录各节点路径距离的Distance。
                 先将IDOne的Distance值设为0，IDOne压入队列，开始循环。
                 
                 循环的每一次：
                 取出队列首的一个节点，
                 遍历每一个与该节点相邻的节点，
                 若是新连上的路径或更短路径则存入Distance，
                 并更新prevNode。
                 更新prevNode时同时将节点压入队列。
                 */
                prevNode.clear();
                Vector<String> queue = new Vector<>();
                Map<String, Double> Distance = new HashMap<>();
                Distance.put(IDOne, 0.0);
                queue.add(IDOne);
                String tempString;
                double tempDistance;
                while(!queue.isEmpty() && prevNode.get(IDTwo) == null) {
                        tempString = queue.elementAt(0);
                        queue.remove(0);
                        for(int i = 0; i < nodeLinks.get(tempString).size(); i++) {
                                tempDistance = Distance.get(tempString) +
                                Math.sqrt((Nodes.get(tempString).lat - Nodes.get(nodeLinks.get(tempString).elementAt(i)).lat) *
                                          (Nodes.get(tempString).lat - Nodes.get(nodeLinks.get(tempString).elementAt(i)).lat) +
                                          (Nodes.get(tempString).lon - Nodes.get(nodeLinks.get(tempString).elementAt(i)).lon) *
                                          (Nodes.get(tempString).lon - Nodes.get(nodeLinks.get(tempString).elementAt(i)).lon));	//算平方会出问题！
                                if(Distance.get(nodeLinks.get(tempString).elementAt(i)) == null || tempDistance < Distance.get(nodeLinks.get(tempString).elementAt(i))) {
                                        Distance.put(nodeLinks.get(tempString).elementAt(i), tempDistance);
                                        prevNode.put(nodeLinks.get(tempString).elementAt(i), tempString);
                                        queue.add(nodeLinks.get(tempString).elementAt(i));
                                }
                        }
                }
                //private Map<String, Vector<String>> nodeLinks = new HashMap<>();
                //private Vector<String> pathNodes = new Vector<>();
                //private HashMap<String, String> prevNode = new HashMap<>();
                String currID = IDTwo;
                while(prevNode.get(currID) != null) {
                        pathNodes.add(currID);
                        currID = prevNode.get(currID);
                }
                pathNodes.add(currID);
                /*
                 循环结束条件：
                 prevNode中以IDTwo为key的return值不为null。
                 或某一个时间阈值内没有找到，提示找不到。
                 */
        }
        
        private String getIdFromName(String Name) {
                Iterator<String> iter;
                String tmpID;
                iter = NodeNames.keySet().iterator();
                while(iter.hasNext()) {
                        tmpID = iter.next();
                        if(NodeNames.get(tmpID).contains(Name)) {
                                return tmpID;
                        }
                }
                
                iter = Ways.keySet().iterator();
                while(iter.hasNext()) {
                        tmpID = iter.next();
                        if(Ways.get(tmpID).tags.get("name") != null && Ways.get(tmpID).tags.get("name").contains(Name)) {
                                return Ways.get(tmpID).nodes.elementAt(1);
                        }
                }
                return null;
        }
        
        public Vector<Double> showTarget(String targetName) {
                points2draw.clear();
                if(targetName.compareTo("") == 0) {
                        repaint();
                        return null;
                }
                Iterator<String> iter;
                
                String tmpID;
                iter = NodeNames.keySet().iterator();
                while(iter.hasNext()) {
                        tmpID = iter.next();
                        if(NodeNames.get(tmpID).contains(targetName)) {
                                points2draw.add(tmpID);
                                setBorder(Nodes.get(tmpID).lon-0.02, Nodes.get(tmpID).lon+0.02, Nodes.get(tmpID).lat-0.01, Nodes.get(tmpID).lat+0.01);
                                repaint();
                                Vector<Double> result = new Vector<>();
                                result.add(Nodes.get(tmpID).lon-0.02);
                                result.add(Nodes.get(tmpID).lon+0.02);
                                result.add(Nodes.get(tmpID).lat-0.01);
                                result.add(Nodes.get(tmpID).lat+0.01);
                                return result;
                        }
                }
                
                iter = Ways.keySet().iterator();
                while(iter.hasNext()) {
                        tmpID = iter.next();
                        if(Ways.get(tmpID).tags.get("name") != null && Ways.get(tmpID).tags.get("name").contains(targetName)) {
                                points2draw.add(Ways.get(tmpID).nodes.elementAt(1));
                                setBorder(Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lon-0.02,
                                          Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lon+0.02,
                                          Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lat-0.01,
                                          Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lat+0.01);
                                repaint();
                                Vector<Double> result = new Vector<>();
                                result.add(Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lon-0.02);
                                result.add(Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lon+0.02);
                                result.add(Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lat-0.01);
                                result.add(Nodes.get(Ways.get(tmpID).nodes.elementAt(1)).lat+0.01);
                                return result;
                        }
                }
                return null;
        }
        
        public void setBorder(double left, double right, double up, double down) {
                leftX = left;
                rightX = right;
                upY = up;
                downY = down;
                
                int ll = 0, rr = Locations.size() - 1, mm;
                while(true) {
                        mm = (ll + rr) / 2;
                        if(Locations.elementAt(mm).elementAt(0).lon < left) {
                                if(mm + 1 == Locations.size()) {
                                        break;
                                }
                                else if(Locations.elementAt(mm + 1).elementAt(0).lon > left) {
                                        mm = mm + 1;
                                        break;
                                }
                                ll = mm + 1;
                        }
                        else if(Locations.elementAt(mm).elementAt(0).lon > left) {
                                if(mm == 0) {
                                        break;
                                }
                                else if(Locations.elementAt(mm - 1).elementAt(0).lon < left) {
                                        break;
                                }
                                rr = mm - 1;
                        }
                        else {
                                break;
                        }
                }
                L = mm;
                
                ll = 0; rr = Locations.size() - 1;
                while(true) {
                        mm = (ll + rr) / 2;
                        if(Locations.elementAt(mm).elementAt(0).lon < right) {
                                if(mm + 1 == Locations.size()) {
                                        break;
                                }
                                else if(Locations.elementAt(mm + 1).elementAt(0).lon > right) {
                                        mm = mm + 1;
                                        break;
                                }
                                ll = mm + 1;
                        }
                        else if(Locations.elementAt(mm).elementAt(0).lon > right) {
                                if(mm == 0) {
                                        break;
                                }
                                else if(Locations.elementAt(mm - 1).elementAt(0).lon < right) {
                                        break;
                                }
                                rr = mm - 1;
                        }
                        else {
                                break;
                        }
                }
                R = mm;
                
                U.clear(); D.clear();
                for(int i = L; i <= R; i++) {
                        ll = 0; rr = Locations.elementAt(i).size() - 1;
                        
                        if(ll == rr) {
                                U.add(0);
                                D.add(0);
                                continue;
                        }
                        
                        while(true) {
                                mm = (ll + rr) / 2;
                                if(Locations.elementAt(i).elementAt(mm).lat < up) {
                                        if(mm + 1 == Locations.elementAt(i).size()) {
                                                break;
                                        }
                                        else if(Locations.elementAt(i).elementAt(mm + 1).lat > up) {
                                                mm = mm + 1;
                                                break;
                                        }
                                        ll = mm + 1;
                                }
                                else if(Locations.elementAt(i).elementAt(mm).lat > up) {
                                        if(mm == 0) {
                                                break;
                                        }
                                        else if(Locations.elementAt(i).elementAt(mm - 1).lat < up) {
                                                break;
                                        }
                                        rr = mm - 1;
                                }
                                else {
                                        break;
                                }
                        }
                        U.add(mm);
                        
                        ll = 0; rr = Locations.elementAt(i).size() - 1;
                        while(true) {
                                mm = (ll + rr) / 2;
                                if(Locations.elementAt(i).elementAt(mm).lat < down) {
                                        if(mm + 1 == Locations.elementAt(i).size()) {
                                                break;
                                        }
                                        else if(Locations.elementAt(i).elementAt(mm + 1).lat > down) {
                                                mm = mm + 1;
                                                break;
                                        }
                                        ll = mm + 1;
                                }
                                else if(Locations.elementAt(i).elementAt(mm).lat > down) {
                                        if(mm == 0) {
                                                break;
                                        }
                                        else if(Locations.elementAt(i).elementAt(mm - 1).lat < down) {
                                                break;
                                        }
                                        rr = mm - 1;
                                }
                                else {
                                        break;
                                }
                        }
                        D.add(mm);
                }
                
                //System.out.println("L  :" + L);
                //System.out.println("R  :" + R);
                //System.out.println("Max:" + Locations.size());
                
                //下面将范围内节点存入currNodes和currNames
                currNodes.clear();
                currNames.clear();
                for(int ii = L; ii <= R; ii++) {
                        for(int jj = U.elementAt(ii - L); jj <= D.elementAt(ii - L); jj++) {
                                currNodes.add(Locations.elementAt(ii).elementAt(jj).ID);
                                if(Locations.elementAt(ii).elementAt(jj).tags.get("name") != null) {
                                        currNames.put(Locations.elementAt(ii).elementAt(jj).ID,
                                                      Locations.elementAt(ii).elementAt(jj).tags.get("name"));
                                }
                        }
                }
                //下面将范围内的道路存入currWays
                currWays.clear();
                Iterator<String> iter = null;
                Way tmpWay;
                iter = Ways.keySet().iterator();
                while(iter.hasNext()) {
                        tmpWay = Ways.get(iter.next());
                        for(int ii = 0; ii < tmpWay.nodes.size(); ii++) {
                                if(currNodes.contains(tmpWay.nodes.elementAt(ii))) {
                                        currWays.add(tmpWay.ID);
                                        break;
                                }
                        }
                }
        }
        
        public MapPanel() {
                super(new BorderLayout());
                
                _initX = _initY = 0;
                _currX = _currY = 0;
                _moveX = _moveY = 0;
                addMouseMotionListener(new MouseAdapter() {
                        public void mouseDragged(MouseEvent e) {
                                _moveX = e.getX();
                                _moveY = e.getY();
                                repaint();
                        }
                });
                addMouseListener(new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                                _initX = _moveX = e.getX();
                                _initY = _moveY = e.getY();
                                repaint();
                        }
                        
                        public void mouseReleased(MouseEvent e) {
                                double zoomY = getHeight() / (downY - upY);
                                double zoomX = getWidth() / (rightX - leftX);
                                _currX = _currX + (_moveX - _initX);
                                _currY = _currY + (_moveY - _initY);
                                _initX = _initY = 0;
                                _moveX = _moveY = 0;
                                mainWindow.dragMap(-1.0 * (double)_currX / zoomX,
                                                   -1.0 * (double)_currY / zoomY);
                                //System.out.println((double)_currX / zoomX);
                                //System.out.println((double)_currY / zoomY);
                                _currX = 0;
                                _currY = 0;
                                //System.out.println("Released.");
                        }
                });
        }
        
        private void Insert(Node anode) {
                int curr1 = 0, curr2 = Locations.size() - 1, middle;
                
                if(curr2 == -1) {
                        Vector<Node> tmp = new Vector<>();
                        tmp.add(anode);
                        Locations.add(tmp);
                        return;
                }
                
                while(true) {
                        middle = (curr2 + curr1) / 2;
                        //System.out.println(middle);
                        if(Locations.elementAt(middle).elementAt(0).lon < anode.lon) {
                                if(middle + 1 == Locations.size()) {
                                        Vector<Node> tmp = new Vector<>();
                                        tmp.add(anode);
                                        Locations.add(tmp);
                                        return;
                                }
                                else if(Locations.elementAt(middle + 1).elementAt(0).lon > anode.lon) {
                                        Vector<Node> tmp = new Vector<>();
                                        tmp.add(anode);
                                        Locations.add(middle + 1, tmp);
                                        return;
                                }
                                curr1 = middle + 1;
                        }
                        else if(Locations.elementAt(middle).elementAt(0).lon > anode.lon) {
                                if(middle == 0) {
                                        Vector<Node> tmp = new Vector<>();
                                        tmp.add(anode);
                                        Locations.add(0, tmp);
                                        return;
                                }
                                else if(Locations.elementAt(middle - 1).elementAt(0).lon < anode.lon) {
                                        Vector<Node> tmp = new Vector<>();
                                        tmp.add(anode);
                                        Locations.add(middle, tmp);
                                        return;
                                }
                                curr2 = middle - 1;
                        }
                        else {
                                break;
                        }
                }
                
                Vector<Node> sameLon = Locations.elementAt(middle);
                curr1 = 0; curr2 = sameLon.size() - 1;
                while(true) {
                        middle = (curr2 + curr1) / 2;
                        if(sameLon.elementAt(middle).lat < anode.lat) {
                                if(middle + 1 == sameLon.size()) {
                                        sameLon.add(anode);
                                        return;
                                }
                                else if(sameLon.elementAt(middle + 1).lat > anode.lat) {
                                        sameLon.add(middle + 1, anode);
                                        return;
                                }
                                curr1 = middle + 1;
                        }
                        else if(sameLon.elementAt(middle).lat > anode.lat) {
                                if(middle == 0) {
                                        sameLon.add(0, anode);
                                        return;
                                }
                                else if(sameLon.elementAt(middle - 1).lat < anode.lat) {
                                        sameLon.add(middle, anode);
                                        return;
                                }
                                curr2 = middle - 1;
                        }
                        else {
                                sameLon.add(middle, anode);
                                return;
                        }
                }
        }
        
        public void setData(HashMap<String, Node> Nodes, HashMap<String, String> NodeNames,
                            HashMap<String, Way> Ways, MainWindow MW) {
                //System.out.println("Setting data, please wait.");
                
                this.Nodes = Nodes;
                this.NodeNames = NodeNames;
                this.Ways = Ways;
                this.mainWindow = MW;
                Locations = new Vector<>();
                
                Iterator<String> iter = null;
                iter = Nodes.keySet().iterator();
                while(iter.hasNext()) {
                        Insert(Nodes.get(iter.next()));
                }
                
                /*
                 基本工作描述：
                 从Nodes读入每一个点，插入Location向量向量中，
                 向量向量中的每一个元素向量按lat升序排列
                 元素向量中的每一个元素lat相同，lon升序排列。
                 插入时直接进行插入排序看来是较好的方案。
                 */
                
                /*
                 为了正常地查找最短路
                 */
                iter = Ways.keySet().iterator();
                Way tmpWay = null;
                Vector<String> tmpVector = null;
                while(iter.hasNext()) {
                        tmpWay = Ways.get(iter.next());
                        //忽略水路和铁路，以及封闭区域
                        if(tmpWay.tags.get("waterway") != null ||
                           tmpWay.tags.get("railway") != null ||
                           tmpWay.nodes.elementAt(0).equals(tmpWay.nodes.elementAt(tmpWay.nodes.size()-1))) {
                                continue;
                        }
                        for(int i = 0; i < tmpWay.nodes.size()-1; i++) {
                                if(this.nodeLinks.get(tmpWay.nodes.elementAt(i)) == null) {
                                        tmpVector = new Vector<>();
                                        tmpVector.add(tmpWay.nodes.elementAt(i+1));
                                        nodeLinks.put(tmpWay.nodes.elementAt(i), tmpVector);
                                }
                                else {
                                        this.nodeLinks.get(tmpWay.nodes.elementAt(i)).add(tmpWay.nodes.elementAt(i+1));
                                }
                                if(this.nodeLinks.get(tmpWay.nodes.elementAt(i+1)) == null) {
                                        tmpVector = new Vector<>();
                                        tmpVector.add(tmpWay.nodes.elementAt(i));
                                        nodeLinks.put(tmpWay.nodes.elementAt(i+1), tmpVector);
                                }
                                else {
                                        this.nodeLinks.get(tmpWay.nodes.elementAt(i+1)).add(tmpWay.nodes.elementAt(i));
                                }
                        }
                }
        }
        
        private void drawLineWithWidth(Graphics g, int x1, int y1, int x2, int y2, int width) {
                if(width <= 1) {
                        g.drawLine(x1, y1, x2, y2);
                        return;
                }
                int deltaX = x2 - x1, deltaY = y2 - y1;
                double degree = Math.atan((double)deltaX / deltaY);
                int px1 = x1 + (int)(width * Math.cos(degree) / 2);
                int px2 = x2 + (int)(width * Math.cos(degree) / 2);
                int px3 = x2 + (int)(width * Math.sin(degree) / 2);
                int px4 = x2 - (int)(width * Math.cos(degree) / 2);
                int px5 = x1 - (int)(width * Math.cos(degree) / 2);
                int px6 = x1 - (int)(width * Math.sin(degree) / 2);
                int[] px = { px1, px2, px3, px4, px5, px6 };
                
                int py1 = y1 - (int)(width * Math.sin(degree) / 2);
                int py2 = y2 - (int)(width * Math.sin(degree) / 2);
                int py3 = y2 + (int)(width * Math.cos(degree) / 2);
                int py4 = y2 + (int)(width * Math.sin(degree) / 2);
                int py5 = y1 + (int)(width * Math.sin(degree) / 2);
                int py6 = y1 - (int)(width * Math.cos(degree) / 2);
                int[] py = { py1, py2, py3, py4, py5, py6 };
                
                g.fillPolygon(px, py, 6);
                return;
        }
        
        protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                String tmpString = null;
                double zoomY = getHeight() / (downY - upY);
                double zoomX = getWidth() / (rightX - leftX);
                Iterator<String> iter = null;
                //画每一个（在范围内）有名称的点
                iter = currNames.keySet().iterator();
                g.setColor(new Color(220, 0, 148));
                //while(iter.hasNext()) {
                        //tmpString = iter.next();
                        //g.fillOval((int)((Nodes.get(tmpString).lon - leftX) * zoomX),
                        //(int)((Nodes.get(tmpString).lat - upY) * zoomY), 4, 4);
                //}
                g.setColor(Color.black);
                //接下去画每一条（在范围中的）路
                g.setColor(new Color(180, 208, 208));
                int tmpWidth;
                iter = currWays.iterator();
                Way tmpWay = null;
                while(iter.hasNext()) {
                        tmpWay = Ways.get(iter.next());
                        tmpWidth = 3;
                        //if it is a waterway
                        if(tmpWay.tags.get("waterway") != null) {
                                g.setColor(new Color(180, 208, 208));
                                for(int ii = 0; ii < tmpWay.nodes.size() - 1; ii++) {
                                        if(Nodes.get(tmpWay.nodes.elementAt(ii)) != null && Nodes.get(tmpWay.nodes.elementAt(ii+1)) != null) {
                                                drawLineWithWidth(g, (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lat - upY) * zoomY) + _currY + (_moveY - _initY),
                                                                  tmpWidth);
                                        }
                                }
                                g.setColor(new Color(215, 215, 215));
                                continue;
                        }
                        //情况：如果这是一个封闭轮廓
                        if(tmpWay.nodes.elementAt(0).compareTo(tmpWay.nodes.elementAt(tmpWay.nodes.size()-1)) == 0) {
                                int[] Xarray = new int[tmpWay.nodes.size()-1];
                                int[] Yarray = new int[tmpWay.nodes.size()-1];
                                for(int i = 0; i < tmpWay.nodes.size()-1; i++) {
                                        Xarray[i] = (int)((Nodes.get(tmpWay.nodes.elementAt(i)).lon - leftX) * zoomX + _currX + (_moveX - _initX));
                                        Yarray[i] = (int)((Nodes.get(tmpWay.nodes.elementAt(i)).lat - upY) * zoomY + _currY + (_moveY - _initY));
                                }
                                g.setColor(new Color(240, 240, 215));
                                g.fillPolygon(Xarray, Yarray, tmpWay.nodes.size()-1);
                                g.setColor(new Color(215, 215, 215));
                                g.drawPolygon(Xarray, Yarray, tmpWay.nodes.size()-1);
                                continue;
                        }
                        //情况：如果这是高架路
                        if(tmpWay.tags.get("highway") != null && (tmpWay.tags.get("highway").compareTo("primary") == 0 || tmpWay.tags.get("highway").compareTo("secondary") == 0)) {
                                g.setColor(new Color(221, 158, 158));
                                for(int ii = 0; ii < tmpWay.nodes.size() - 1; ii++) {
                                        if(Nodes.get(tmpWay.nodes.elementAt(ii)) != null && Nodes.get(tmpWay.nodes.elementAt(ii+1)) != null) {
                                                drawLineWithWidth(g, (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                                  (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                                  tmpWidth);
                                        }
                                }
                                g.setColor(new Color(215, 215, 215));
                                continue;
                        }
                        //以下是几个需要忽略的情况
                        if(tmpWay.tags.get("railway") != null) {
                                continue;
                        }
                        if(tmpWay.tags.get("highway") != null && (tmpWay.tags.get("highway").compareTo("service") == 0)) {
                                continue;
                        }
                        //else
                        tmpWidth = 3;
                        for(int ii = 0; ii < tmpWay.nodes.size() - 1; ii++) {
                                if(Nodes.get(tmpWay.nodes.elementAt(ii)) != null && Nodes.get(tmpWay.nodes.elementAt(ii+1)) != null) {
                                        drawLineWithWidth(g, (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                          (int)((Nodes.get(tmpWay.nodes.elementAt(ii)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                          (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                          (int)((Nodes.get(tmpWay.nodes.elementAt(ii+1)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                          tmpWidth);
                                }
                        }
                }
                
                //paint the path
                //System.out.println("pathNodes.size: " + pathNodes.size());
                if(pathNodes != null && pathNodes.size() != 0) {
                        g.setColor(new Color(129,102,187));
                        for(int ii = 0; ii < pathNodes.size() - 1; ii++) {
                                if(Nodes.get(pathNodes.elementAt(ii)) != null && Nodes.get(pathNodes.elementAt(ii+1)) != null) {
                                        drawLineWithWidth(g, (int)((Nodes.get(pathNodes.elementAt(ii)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                          (int)((Nodes.get(pathNodes.elementAt(ii)).lat - upY) * zoomY + _currY + (_moveY - _initY)),
                                                          (int)((Nodes.get(pathNodes.elementAt(ii+1)).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                                                          (int)((Nodes.get(pathNodes.elementAt(ii+1)).lat - upY) * zoomY + _currY + (_moveY - _initY)), 5);
                                }
                        }
                        g.setColor(new Color(215, 215, 215));
                }
                
                Iterator<String> pointIter;
                pointIter = points2draw.iterator();
                if(!pointIter.hasNext()) {
                        return;
                }
                tmpString = pointIter.next();
                g.setColor(Color.white);
                g.fillOval((int)((Nodes.get(tmpString).lon - leftX) * zoomX + _currX + (_moveX - _initX) - 2),
                           (int)((Nodes.get(tmpString).lat - upY) * zoomY + _currY + (_moveY - _initY) - 2), 12, 12);
                g.setColor(new Color(220, 0, 148));
                g.fillOval((int)((Nodes.get(tmpString).lon - leftX) * zoomX + _currX + (_moveX - _initX)),
                           (int)((Nodes.get(tmpString).lat - upY) * zoomY + _currY + (_moveY - _initY)), 8, 8);
        }
        
        //only for test...
        public static void main(String[] args) {
                MapPanel panel = new MapPanel();
                JFrame frame = new JFrame();
                frame.setLayout(new GridLayout(1, 1));
                frame.add(panel);
                frame.setSize(600, 600);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
        }
}

