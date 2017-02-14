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
// GuiFieldクラス
// GUI表示を制御
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class GuiField extends Field
{
    int Mode;
    int size = DFN.SIZE;

    GuiField(){
        super();
    }

    /**
     * [paintField description]
     * @param g     [description]
     * @param panel [description]
     */
    void paintField(Graphics g){
        for (int i=1; i<=DFN.HEIGHT; i++) {
            for (int j=1; j<=DFN.WIDTH; j++) {
                int x = j*size;
                int y = i*size;
                g.setFont(new Font("TimesRoman",Font.PLAIN,10));
                if(i==1)g.drawString(String.valueOf(j-1), x+5, y-5);
                if(j==1)g.drawString(String.valueOf(i-1), x-15, y+size-7);
            }
        }
        for (int i=0; i<DFN.HEIGHT; i++) {
            for (int j=0; j<DFN.WIDTH; j++) {
                int m = cell[i][j].areaID;
                int x = j*size+size;
                int y = i*size+size;

                g.setColor(new Color(DFN.COLOR_R[m], DFN.COLOR_G[m], DFN.COLOR_B[m],100));
                g.fillRect(x, y, size, size);

                g.setColor(Color.BLACK);
                g.drawRect(x, y, size, size);
                g.setFont(new Font("TimesRoman",Font.PLAIN,9));
                //g.drawString(String.valueOf(cell[i][j].areaID), x+(int)(size*0.5), y+(int)(size*0.5));
            }
        }

        for (int i=0; i<voter.length; i++) {
            if(voter[i].age == DFN.AGE_YOUNG){
                g.setColor(new Color(0, 255, 255));
            }else if(voter[i].age == DFN.AGE_MIDDLE){
                g.setColor(new Color(30, 190, 190));
            }else {
                g.setColor(new Color(50, 140, 140));
            }
            g.fillOval(voter[i].x*size+size, voter[i].y*size+size, size, size);
            g.setColor(new Color(0, 0, 0,70));
            g.drawOval(voter[i].x*size+size, voter[i].y*size+size, size, size);
            g.setColor(Color.BLACK);
            //g.drawString(String.valueOf(voter[i].id), voter[i].x*size+size+(int)(size*0.2), voter[i].y*size+size+(int)(size*0.7));
        }

        for (int i=0; i<candidate.length; i++) {
            if(candidate[i].electionFlag == true){
                if (candidate[i].areaID == DFN.PROPORTIONAL_AREA_ID) {
                    g.setColor(new Color(196,0,204));
                }else {
                    g.setColor(Color.RED);
                }
            }else{
                if(candidate[i].areaID == DFN.PROPORTIONAL_AREA_ID){
                    g.setColor(Color.YELLOW);
                }else {
                    g.setColor(Color.GREEN);
                }
            }
            g.fillOval(candidate[i].x*size+size, candidate[i].y*size+size, size, size);
            g.setFont(new Font("TimesRoman",Font.BOLD,9));
            g.setColor(Color.BLACK);
            g.drawString(DFN.PARTY_NAME[candidate[i].partyID], candidate[i].x*size+size+(int)(size*0.3), candidate[i].y*size+size+(int)(size*0.7));
            g.setColor(new Color(0, 0, 0,70));
            g.drawOval(candidate[i].x*size+size, candidate[i].y*size+size, size, size);
        }


    }
}