///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
////                                                                                               ////
////  KKKKKKKKK    KKKKKKKMMMMMMMM               MMMMMMMMYYYYYYY       YYYYYYY   SSSSSSSSSSSSSSS   ////
////  K:::::::K    K:::::KM:::::::M             M:::::::MY:::::Y       Y:::::Y SS:::::::::::::::S  ////
////  K:::::::K    K:::::KM::::::::M           M::::::::MY:::::Y       Y:::::YS:::::SSSSSS::::::S  ////
////  K:::::::K   K::::::KM:::::::::M         M:::::::::MY::::::Y     Y::::::YS:::::S     SSSSSSS  ////
////  KK::::::K  K:::::KKKM::::::::::M       M::::::::::MYYY:::::Y   Y:::::YYYS:::::S              ////
////    K:::::K K:::::K   M:::::::::::M     M:::::::::::M   Y:::::Y Y:::::Y   S:::::S              ////
////    K::::::K:::::K    M:::::::M::::M   M::::M:::::::M    Y:::::Y:::::Y     S::::SSSS           ////
////    K:::::::::::K     M::::::M M::::M M::::M M::::::M     Y:::::::::Y       SS::::::SSSSS      ////
////    K:::::::::::K     M::::::M  M::::M::::M  M::::::M      Y:::::::Y          SSS::::::::SS    ////
////    K::::::K:::::K    M::::::M   M:::::::M   M::::::M       Y:::::Y              SSSSSS::::S   ////
////    K:::::K K:::::K   M::::::M    M:::::M    M::::::M       Y:::::Y                   S:::::S  ////
////  KK::::::K  K:::::KKKM::::::M     MMMMM     M::::::M       Y:::::Y                   S:::::S  ////
////  K:::::::K   K::::::KM::::::M               M::::::M       Y:::::Y       SSSSSSS     S:::::S  ////
////  K:::::::K    K:::::KM::::::M               M::::::M    YYYY:::::YYYY    S::::::SSSSSS:::::S  ////
////  K:::::::K    K:::::KM::::::M               M::::::M    Y:::::::::::Y    S:::::::::::::::SS   ////
////  KKKKKKKKK    KKKKKKKMMMMMMMM               MMMMMMMM    YYYYYYYYYYYYY     SSSSSSSSSSSSSSS     ////
////                                                                                               ////
///////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////
//
// MainWindowクラス
// メインウィンドウとサブウィンドウに表示する情報を管理
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.title.*;
import org.jfree.data.category.*;
import java.util.Formatter;
import java.io.File;

public class MainWindow extends JFrame
{
    SpiderWebPlotDrawer spiderWeb;
    LineChartPlotDrawer lineChart;

    GuiField field;
    Calc calc;

    MainThread mainThread;

    Dimension windowSize;

    int runNo;
    protected int time = 0;
    double waitTime = DFN.WAIT_TIME_SLOW;
    int stepCounta = DFN.LIMIT_STEP;
    protected boolean startFlag = false;
    protected boolean limitModeFlag = false;

    JPanel pnSide, pnSideCtrlStart, pnSideCtrlWaitTime;
    MainPanel pnMain;
    JLabel lbTime;
    JLabel lbSeatsTitle, lbSeats;
    JLabel lbDiscontentTitle, lbDiscontentAll, lbDiscontentYoung, lbDiscontentMiddle, lbDiscontentUpper;
    JLabel lbSingleToProportional, lbSingleSeats, lbProportionalSeats;
    JButton btStart, btLimitStart, btMerger, btInit;
    JButton btWaitTimeFast, btWaitTimeSlow;

    MainWindow()
    {
        // フィールド生成
        field = new GuiField();
        calc = new Calc();
        copyToGUI();

        //サイドウィンドウ
        spiderWeb = new SpiderWebPlotDrawer(calc);
        lineChart = new LineChartPlotDrawer(calc.discontentYoung,calc.discontentMiddle,calc.discontentUpper);

        // コンポーネント
        // ラベル(タイマーとメッセージ)
        lbTime = new JLabel("メッセージ");
        lbTime.setForeground(Color.black);
        lbTime.setFont(new Font("Serif", Font.BOLD, 24));
        lbSeats = new JLabel("メッセージ");
        lbSeats.setForeground(Color.black);
        lbSeats.setFont(new Font("Serif", Font.BOLD, 16));
        lbSeatsTitle = new JLabel("メッセージ");
        lbSeatsTitle.setForeground(Color.black);
        lbSeatsTitle.setFont(new Font("Serif", Font.BOLD, 16));
        lbDiscontentTitle = new JLabel("メッセージ");
        lbDiscontentTitle.setForeground(Color.black);
        lbDiscontentTitle.setFont(new Font("Serif", Font.BOLD, 16));
        lbDiscontentAll = new JLabel("メッセージ");
        lbDiscontentAll.setForeground(Color.black);
        lbDiscontentAll.setFont(new Font("Serif", Font.BOLD, 16));
        lbDiscontentYoung = new JLabel("メッセージ");
        lbDiscontentYoung.setForeground(Color.black);
        lbDiscontentYoung.setFont(new Font("Serif", Font.BOLD, 16));
        lbDiscontentMiddle = new JLabel("メッセージ");
        lbDiscontentMiddle.setForeground(Color.black);
        lbDiscontentMiddle.setFont(new Font("Serif", Font.BOLD, 16));
        lbDiscontentUpper = new JLabel("メッセージ");
        lbDiscontentUpper.setForeground(Color.black);
        lbDiscontentUpper.setFont(new Font("Serif", Font.BOLD, 16));
        lbSingleToProportional = new JLabel("メッセージ");
        lbSingleToProportional.setForeground(Color.black);
        lbSingleToProportional.setFont(new Font("Serif", Font.BOLD, 16));
        lbSingleSeats = new JLabel("メッセージ");
        lbSingleSeats.setForeground(Color.black);
        lbSingleSeats.setFont(new Font("Serif", Font.BOLD, 16));
        lbProportionalSeats = new JLabel("メッセージ");
        lbProportionalSeats.setForeground(Color.black);
        lbProportionalSeats.setFont(new Font("Serif", Font.BOLD, 16));

        //ボタン
        btStart = new JButton("▶");
        String str = "|▶ (≦ " + DFN.LIMIT_STEP + ")";
        btLimitStart = new JButton(str);
        btMerger = new JButton("合併");
        btInit = new JButton("初期化");
        btWaitTimeSlow = new JButton("SLOW");
        btWaitTimeSlow.setFont(new Font("Serif", Font.BOLD, 14));
        btWaitTimeFast = new JButton("FAST");
        btWaitTimeFast.setFont(new Font("Serif", Font.PLAIN, 12));

        //　パネル(グラフィックス)
        pnMain = new MainPanel(); // グラフィックス描画用パネル

        // サイドパネル
        pnSide = new JPanel();
        pnSide.setLayout(new GridLayout(20,1));
        pnSideCtrlStart = new JPanel();
        pnSideCtrlStart.setLayout(new GridLayout(1,2));
        pnSideCtrlWaitTime = new JPanel();
        pnSideCtrlWaitTime.setLayout(new GridLayout(1,2));

        // コンテナへの追加
        pnSideCtrlWaitTime.add(btWaitTimeSlow);
        pnSideCtrlWaitTime.add(btWaitTimeFast);
        pnSideCtrlStart.add(btStart);
        pnSideCtrlStart.add(btLimitStart);
        pnSide.add(pnSideCtrlWaitTime);
        pnSide.add(pnSideCtrlStart);
        pnSide.add(btMerger);
        pnSide.add(btInit);
        pnSide.add(lbTime);
        pnSide.add(lbSeatsTitle);
        pnSide.add(lbSeats);
        pnSide.add(lbSingleToProportional);
        pnSide.add(lbSingleSeats);
        pnSide.add(lbProportionalSeats);
        pnSide.add(lbDiscontentTitle);
        pnSide.add(lbDiscontentAll);
        pnSide.add(lbDiscontentYoung);
        pnSide.add(lbDiscontentMiddle);
        pnSide.add(lbDiscontentUpper);
        add(pnMain, BorderLayout.CENTER);
        add(pnSide, BorderLayout.EAST);

        // リスナの登録
        btStart.addActionListener(new MainActionListenerBt());
        btLimitStart.addActionListener(new MainActionListenerBt());
        btMerger.addActionListener(new MainActionListenerBt());
        btInit.addActionListener(new MainActionListenerBt());
        btWaitTimeFast.addActionListener(new MainActionListenerBt());
        btWaitTimeSlow.addActionListener(new MainActionListenerBt());

        // メインウィンドウの設定
        setTitle("e-simlator");
        setBounds(50,50,1020,700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addComponentListener(new ComponentAdapterJFrame());

        //スレッドの開始
        mainThread = new MainThread();

    }

    public void init(){
        calc = new Calc();
        copyToGUI();
        runNo = 0;
        time = 0;
        stepCounta = DFN.LIMIT_STEP;
        startFlag = false;
        btStart.setText("▶");
        mainThread.setStop();
        limitModeFlag = false;
    }

    public void copyToGUI(){
        field.cell = calc.cell;
        field.voter = calc.voter;
        field.candidate = calc.candidate;
        field.party = calc.party;
        field.discontentAll = calc.discontentAll;
        field.discontentYoung = calc.discontentYoung;
        field.discontentMiddle = calc.discontentMiddle;
        field.discontentUpper = calc.discontentUpper;
        field.area = calc.area;
        field.proportionalArea = calc.proportionalArea;
        field.youngList  = calc.youngList;
        field.middleList = calc.middleList;
        field.upperList  = calc.upperList;
        field.singleSeats = calc.singleSeats;
        field.proportionalSeats = calc.proportionalSeats;
        field.areaMap = calc.areaMap;
    }

    public void copyFromGUI(){
        calc.cell = field.cell;
        calc.voter = field.voter;
        calc.candidate = field.candidate;
        calc.party = field.party;
        calc.discontentAll = field.discontentAll;
        calc.discontentYoung = field.discontentYoung;
        calc.discontentMiddle = field.discontentMiddle;
        calc.discontentUpper = field.discontentUpper;
        calc.area = field.area;
        calc.proportionalArea = field.proportionalArea;
        calc.youngList  = field.youngList;
        calc.middleList = field.middleList;
        calc.upperList  = field.upperList;
        calc.singleSeats = field.singleSeats;
        calc.proportionalSeats = field.proportionalSeats;
        calc.areaMap = field.areaMap;
    }

    class MainPanel extends JPanel
    {
        public void paintComponent(Graphics g)
        {
            Formatter fm = new Formatter();
            field.paintField(g);
            String str = "" + time + "STEP";
            lbTime.setText(str);
            lbSeatsTitle.setText("[獲得議席数]　　　　　");
            str = "　" + field.party[0].name + "：" + field.party[0].seats + "　" + field.party[1].name +"："+ field.party[1].seats + "　"+field.party[2].name +"："+ field.party[2].seats;
            lbSeats.setText(str);
            lbSingleToProportional.setText("[区別議席数]");
            str = " 　小選挙区：" + field.singleSeats;
            lbSingleSeats.setText(str);
            str = " 　比例区　：" + field.proportionalSeats;
            lbProportionalSeats.setText(str);
            lbDiscontentTitle.setText("[不満平均]");
            str = " 　全体　  ：" + fm.format("%5.4f", field.discontentAll) + "(" + DFN.NO_VOTER + "人)";
            fm = new Formatter();
            lbDiscontentAll.setText(str);
            str = " 　若年層  ：" + fm.format("%5.4f", field.discontentYoung) + "(" + field.youngList.size() + "人)";
            fm = new Formatter();
            lbDiscontentYoung.setText(str);
            str = " 　中高年層：" + fm.format("%5.4f", field.discontentMiddle) + "(" + field.middleList.size() + "人)";
            lbDiscontentMiddle.setText(str);
            fm = new Formatter();
            str = " 　老年層  ：" + fm.format("%5.4f", field.discontentUpper) + "(" + field.upperList.size() + "人)";
            lbDiscontentUpper.setText(str);
            fm.close();
        }
    }

    class MainActionListenerBt implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==btWaitTimeFast){
                waitTime = DFN.WAIT_TIME_FAST;
                btWaitTimeSlow.setFont(new Font("Serif", Font.PLAIN, 12));
                btWaitTimeFast.setFont(new Font("Serif", Font.BOLD, 14));
            }else if(e.getSource()==btWaitTimeSlow){
                waitTime = DFN.WAIT_TIME_SLOW;
                btWaitTimeSlow.setFont(new Font("Serif", Font.BOLD, 14));
                btWaitTimeFast.setFont(new Font("Serif", Font.PLAIN, 12));
            }else if (e.getSource()==btMerger){
                copyToGUI();
                field.mergerArea();
                copyFromGUI();
                spiderWeb.setData(calc);
                spiderWeb.updateCbox();
                spiderWeb.update();
                repaint();
                startFlag=false;
                btStart.setText("▶");
                mainThread.setStop();
            }else if(e.getSource()==btInit){
                init();
                spiderWeb.init(calc);
                spiderWeb.updateCbox();
                spiderWeb.update();
                lineChart.init(calc.discontentYoung,calc.discontentMiddle,calc.discontentUpper);
                lineChart.update();
                startFlag=false;
                btStart.setText("▶");
                mainThread.setStop();
                repaint();
            }else if (e.getSource()==btStart) {
                //System.out.println("ボタン= "+ btStart.getText());
                stepCounta = DFN.LIMIT_STEP;
                limitModeFlag = false;
                if(startFlag == true){
                    startFlag=false;
                    btStart.setText("▶");
                    mainThread.setStop();
                }else {
                    startFlag=true;
                    btStart.setText("||");
                    if(mainThread.end == true){
                        mainThread.end = false;
                        mainThread.start();
                    }else {
                        mainThread.setStop();
                    }
                }
            }else if(e.getSource()==btLimitStart){
                stepCounta = DFN.LIMIT_STEP;
                limitModeFlag = true;
                startFlag=true;
                btStart.setText("||");
                if(mainThread.end == true){
                    mainThread.end = false;
                    mainThread.start();
                }else{
                    mainThread.setStop();
                }
            }
        }
    }

    class ComponentAdapterJFrame extends ComponentAdapter{
        public void componentResized(ComponentEvent e) {
            windowSize = getContentPane().getSize();
            double w = (windowSize.width - 200)/(5 * DFN.AREA_SIZE + 1);
            if(w < 0) w = 0;
            double h = windowSize.height/(4 * DFN.AREA_SIZE + 1);
            if(w < h){
                field.size = (int)(w);
            }else {
                field.size = (int)(h);
            }
            repaint();
        }
    }

    class MainThread extends Thread {
        boolean end = true;

        public MainThread(){
            runNo = 0;
        }

        public void run() {
            while(end == false){
                switch(runNo){
                    case 0:
                        calc.progressVote();
                        runNo++;
                        break;
                    case 1:
                        calc.progressElection();
                        spiderWeb.setData(calc);
                        spiderWeb.update();
                        runNo++;
                        break;
                    case 2:
                        calc.progressAfter();
                        lineChart.addDiscontent(calc.discontentYoung,calc.discontentMiddle,calc.discontentUpper);
                        lineChart.update();
                        time++;
                        if(limitModeFlag == true)stepCounta--;
                        runNo=0;
                        break;
                    }
                try {
                    Thread.sleep((int)(waitTime*1000));
                    synchronized(this){
                        if (startFlag == false) wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                copyToGUI();
                requestFocusInWindow();
                repaint();
                 if (stepCounta == 0) {
                    startFlag = false;
                    btStart.setText("▶");
                    mainThread.setStop();
                }
            }
        }

        public void stopRun(){
            startFlag  = false;
        }

        public synchronized void setStop() {
            if (startFlag == true) {
                notify();
            }

        }
    }
}