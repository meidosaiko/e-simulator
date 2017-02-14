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
// LineChartPlotDrawerクラス
// 棒グラフウィンドウの制御
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

public class LineChartPlotDrawer extends JFrame{

    JPanel p,chp;
    Container cp;

    Dimension size = new Dimension(540, 300);

    ArrayList<Double> discontentYoung = new ArrayList<Double>();
    ArrayList<Double> discontentMiddle = new ArrayList<Double>();
    ArrayList<Double> discontentUpper = new ArrayList<Double>();

    LineChartPlotDrawer(double _discontentYoung, double _discontentMiddle,double _discontentUpper) {
        addDiscontent(_discontentYoung,_discontentMiddle,_discontentUpper);
        //パネル
        p = new JPanel();
        cp = getContentPane();
        chp = lineChartPlot();
        chp.setPreferredSize(size);

        //追加
        p.add(chp);
        cp.add(p, BorderLayout.SOUTH);

        //ウィンドウの設定
        setTitle("LineChart");
        setBounds(1070, 710, size.width, size.height);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addComponentListener(new ComponentAdapterJFrame());
    }

    void init(double _discontentYoung, double _discontentMiddle,double _discontentUpper){
        discontentYoung = new ArrayList<Double>();
        discontentMiddle = new ArrayList<Double>();
        discontentUpper = new ArrayList<Double>();
        addDiscontent(_discontentYoung,_discontentMiddle,_discontentUpper);
    }

    void addDiscontent(double _discontentYoung, double _discontentMiddle,double _discontentUpper){
        discontentYoung.add(_discontentYoung);
        discontentMiddle.add(_discontentMiddle);
        discontentUpper.add(_discontentUpper);
    }

    void update() {
        p.removeAll();
        chp = lineChartPlot();
        chp.setPreferredSize(size);
        p.add(chp);
        p.revalidate();
    }

    JPanel lineChartPlot() {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        for (int i=0;i<discontentYoung.size();i++ ) {
            ds.addValue(discontentYoung.get(i), "若者", String.valueOf(i));
        }
        for (int i=0;i<discontentMiddle.size();i++ ) {
            ds.addValue(discontentMiddle.get(i), "中高年", String.valueOf(i));
        }
        for (int i=0;i<discontentUpper.size();i++ ) {
            ds.addValue(discontentUpper.get(i), "老年", String.valueOf(i));
        }
        JFreeChart fc = ChartFactory.createLineChart("不満度の推移","STEP","不満度",ds,PlotOrientation.VERTICAL, true, false, false);
        ChartPanel cp = new ChartPanel(fc);
        return cp;
    }

    class LineChartActionListenerCbox implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            update();
        }
    }

    class ComponentAdapterJFrame extends ComponentAdapter{
        public void componentResized(ComponentEvent e) {
            size = getContentPane().getSize();
            update();
        }
    }

}