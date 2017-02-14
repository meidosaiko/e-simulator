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
// SplashWindowクラス
// メインウィンドウの制御
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.BorderLayout;

public class SplashWindow extends JFrame
{

    JPanel p;

    void killLogo(){
        setVisible(false);
    }

    void changeLogo(){
        setVisible(false);
        p.removeAll();

        ImageIcon icon = new ImageIcon("img/elogo.png");
        JLabel label = new JLabel(icon);
        p.add(label);
        p.revalidate();
        setBounds(100, 100,320, 263);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    SplashWindow()
    {
        p = new JPanel();

        ImageIcon icon = new ImageIcon("img/clogo.png");
        JLabel label = new JLabel(icon);
        p.add(label);

        Container contentPane = getContentPane();
        contentPane.add(p, BorderLayout.CENTER);

        setTitle("");
        setBounds(100, 100,822, 480);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setVisible(true);
    }
}