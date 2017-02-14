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
// SpiderWebPlotDrawerクラス
// JFreeChartのレーダーチャートを拡張する
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


public class SpiderWebPlotDrawer extends JFrame {

    Dimension size = new Dimension(540, 400);

    JPanel p,chp,pnCbox,pnCkBox,pnCtrl,pnInfo,pnInfoVoter,pnInfoCandidate;
    JLabel lbInfoVoterID,lbInfoCandidateID,lbInfoVoterAreaID,lbInfoCandidateAreaID,lbInfoVoterCandidateID,lbInfoCandidateElectionFlag,lbInfoVoterproportionalCandidateID,lbInfoCandidateVotes,lbInfoCandidatePartyName,lbInfoVoterValue,lbInfoVoterAge,lbInfoCandidatePerVotes;
    Container cp;
    JComboBox<String> cbVoter,cbCandidate;
    int[] cbVoterID,cbCandidateID;
    String[] voterName, candidateName;
    DefaultComboBoxModel<String> voterList, candidateList;
    JCheckBox ckVoter,ckCandidate,ckParty,ckArea;

    Calc calc = new Calc();

    SpiderWebPlotDrawer(Calc _calc) {
        setData(_calc);
        setCandidateName();
        setVoterName(0);

        //パネル
        p = new JPanel();
        pnCkBox = new JPanel();
        pnCkBox.setLayout(new GridLayout(1,4));
        pnCbox = new JPanel();
        pnCbox.setLayout(new GridLayout(1,2));
        pnCtrl = new JPanel();
        pnCtrl.setLayout(new GridLayout(2,1));
        pnInfo = new JPanel();
        pnInfo.setLayout(new GridLayout(1,2));
        pnInfoVoter = new JPanel();
        pnInfoVoter.setLayout(new GridLayout(6,1));
        pnInfoCandidate = new JPanel();
        pnInfoCandidate.setLayout(new GridLayout(6,1));

        //コンボボックス
        voterList = new DefaultComboBoxModel<String>(voterName);
        cbVoter = new JComboBox<String>(voterList);
        candidateList = new DefaultComboBoxModel<String>(candidateName);
        cbCandidate = new JComboBox<String>(candidateList);

        //チェックボックス
        ckVoter = new JCheckBox("有権者");
        ckVoter.setSelected(true);
        ckCandidate = new JCheckBox("候補者");
        ckCandidate.setSelected(true);
        ckParty = new JCheckBox("政党方針");
        ckArea = new JCheckBox("選挙区平均");

        //ラベル
        lbInfoVoterID = new JLabel("メッセージ");
        lbInfoVoterID.setForeground(Color.black);
        lbInfoVoterID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterID.setPreferredSize(new Dimension(225,26));
        lbInfoCandidateID = new JLabel("メッセージ");
        lbInfoCandidateID.setForeground(Color.black);
        lbInfoCandidateID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidateID.setPreferredSize(new Dimension(225,26));
        lbInfoVoterAge = new JLabel("メッセージ");
        lbInfoVoterAge.setForeground(Color.black);
        lbInfoVoterAge.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterAge.setPreferredSize(new Dimension(225,26));
        lbInfoVoterAreaID = new JLabel("メッセージ");
        lbInfoVoterAreaID.setForeground(Color.black);
        lbInfoVoterAreaID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterAreaID.setPreferredSize(new Dimension(225,26));
        lbInfoCandidateAreaID = new JLabel("メッセージ");
        lbInfoCandidateAreaID.setForeground(Color.black);
        lbInfoCandidateAreaID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidateAreaID.setPreferredSize(new Dimension(225,26));
        lbInfoVoterCandidateID = new JLabel("メッセージ");
        lbInfoVoterCandidateID.setForeground(Color.black);
        lbInfoVoterCandidateID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterCandidateID.setPreferredSize(new Dimension(225,26));
        lbInfoCandidateElectionFlag = new JLabel("メッセージ");
        lbInfoCandidateElectionFlag.setForeground(Color.black);
        lbInfoCandidateElectionFlag.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidateElectionFlag.setPreferredSize(new Dimension(225,26));
        lbInfoVoterproportionalCandidateID = new JLabel("メッセージ");
        lbInfoVoterproportionalCandidateID.setForeground(Color.black);
        lbInfoVoterproportionalCandidateID.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterproportionalCandidateID.setPreferredSize(new Dimension(225,26));
        lbInfoCandidateVotes = new JLabel("メッセージ");
        lbInfoCandidateVotes.setForeground(Color.black);
        lbInfoCandidateVotes.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidateVotes.setPreferredSize(new Dimension(225,26));
        lbInfoCandidatePartyName = new JLabel("メッセージ");
        lbInfoCandidatePartyName.setForeground(Color.black);
        lbInfoCandidatePartyName.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidatePartyName.setPreferredSize(new Dimension(225,26));
        lbInfoVoterValue = new JLabel("メッセージ");
        lbInfoVoterValue.setForeground(Color.black);
        lbInfoVoterValue.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoVoterValue.setPreferredSize(new Dimension(225,26));
        lbInfoCandidatePerVotes = new JLabel("メッセージ");
        lbInfoCandidatePerVotes.setForeground(Color.black);
        lbInfoCandidatePerVotes.setFont(new Font("Serif", Font.BOLD, 18));
        lbInfoCandidatePerVotes.setPreferredSize(new Dimension(225,26));

        //追加
        cp = getContentPane();
        update();
        p.add(chp);
        pnCkBox.add(ckCandidate);
        pnCkBox.add(ckParty);
        pnCkBox.add(ckArea);
        pnCkBox.add(ckVoter);
        pnCbox.add(cbCandidate);
        pnCbox.add(cbVoter);
        pnCtrl.add(pnCbox);
        pnCtrl.add(pnCkBox);
        pnInfoCandidate.add(lbInfoCandidateID);
        pnInfoCandidate.add(lbInfoCandidatePartyName);
        pnInfoCandidate.add(lbInfoCandidateAreaID);
        pnInfoCandidate.add(lbInfoCandidateElectionFlag);
        pnInfoCandidate.add(lbInfoCandidateVotes);
        pnInfoCandidate.add(lbInfoCandidatePerVotes);
        pnInfoVoter.add(lbInfoVoterID);
        pnInfoVoter.add(lbInfoVoterAge);
        pnInfoVoter.add(lbInfoVoterAreaID);
        pnInfoVoter.add(lbInfoVoterCandidateID);
        pnInfoVoter.add(lbInfoVoterproportionalCandidateID);
        pnInfoVoter.add(lbInfoVoterValue);
        pnInfo.add(pnInfoCandidate);
        pnInfo.add(pnInfoVoter);
        cp.add(pnCtrl,BorderLayout.NORTH);
        cp.add(p, BorderLayout.CENTER);
        cp.add(pnInfo, BorderLayout.SOUTH);

        //リスナ
        cbVoter.addActionListener(new SpiderWebActionListenerCbox());
        cbCandidate.addActionListener(new SpiderWebActionListenerCbox());
        ckVoter.addActionListener(new SpiderWebActionListenerCbox());
        ckCandidate.addActionListener(new SpiderWebActionListenerCbox());
        ckParty.addActionListener(new SpiderWebActionListenerCbox());
        ckArea.addActionListener(new SpiderWebActionListenerCbox());

        //ウィンドウの設定
        setTitle("SpiderWeb");
        setBounds(1070, 50, size.width, size.height + 260);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        addComponentListener(new ComponentAdapterJFrame());
    }

    void init(Calc _calc){
        setData(_calc);
        setCandidateName();
        setVoterName(0);
    }

    void setData(Calc _calc) {
        calc = new Calc();
        calc = _calc;
    }

    void setCandidateName(){
        candidateName = new String[calc.candidate.length];
        cbCandidateID = new int[calc.candidate.length];
        for (int i=0;i<candidateName.length;i++) {
            String areaName;
            if(calc.candidate[i].areaID == DFN.PROPORTIONAL_AREA_ID){
                areaName = "比例";
            }else {
                areaName = "" + calc.candidate[i].areaID;
            }
            if(calc.candidate[i].areaID == DFN.PROPORTIONAL_AREA_ID){
                candidateName[i] = "候補者" + calc.candidate[i].id + "(政党:"+ DFN.PARTY_NAME[calc.candidate[i].partyID] + "地区：比例)";
            }else{
                candidateName[i] = "候補者" + calc.candidate[i].id + "(政党:"+ DFN.PARTY_NAME[calc.candidate[i].partyID] + "地区：" + calc.candidate[i].areaID + ")";
            }
            cbCandidateID[i] = calc.candidate[i].id;
        }
    }

    void setVoterName(int _areaID){
        if(_areaID == DFN.PROPORTIONAL_AREA_ID){
            voterName = new String[calc.proportionalArea.getNoVoter()];
            cbVoterID = new int[calc.proportionalArea.getNoVoter()];
            for (int i=0;i<calc.proportionalArea.getNoVoter();i++) {
                voterName[i] = "有権者" + calc.proportionalArea.voterList.get(i) + "(年齢:" + calc.voter[calc.proportionalArea.voterList.get(i)].age + ")";
                cbVoterID[i] = calc.proportionalArea.voterList.get(i);
            }
        }else{
            voterName = new String[calc.area[_areaID].getNoVoter()];
            cbVoterID = new int[calc.area[_areaID].getNoVoter()];
            for (int i=0;i<calc.area[_areaID].getNoVoter();i++) {
                voterName[i] = "有権者" + calc.area[_areaID].voterList.get(i) + "(年齢:" + calc.voter[calc.area[_areaID].voterList.get(i)].age + ")";
                cbVoterID[i] = calc.area[_areaID].voterList.get(i);
            }
        }
    }

    void updateCboxV(){
        int _candidateID = cbCandidateID[cbCandidate.getSelectedIndex()];
        pnCbox.removeAll();
        setVoterName(calc.candidate[_candidateID].areaID);
        voterList = new DefaultComboBoxModel<String>(voterName);
        cbVoter = new JComboBox<String>(voterList);
        cbVoter.addActionListener(new SpiderWebActionListenerCbox());
        pnCbox.add(cbCandidate);
        pnCbox.add(cbVoter);
        pnCbox.revalidate();
    }

    void updateCbox() {
        pnCbox.removeAll();
        setCandidateName();
        candidateList = new DefaultComboBoxModel<String>(candidateName);
        cbCandidate = new JComboBox<String>(candidateList);
        cbCandidate.addActionListener(new SpiderWebActionListenerCbox());
        setVoterName(calc.candidate[cbCandidateID[cbCandidate.getSelectedIndex()]].areaID);
        voterList = new DefaultComboBoxModel<String>(voterName);
        cbVoter = new JComboBox<String>(voterList);
        cbVoter.addActionListener(new SpiderWebActionListenerCbox());
        pnCbox.setLayout(new GridLayout(1,2));
        pnCbox.add(cbCandidate);
        pnCbox.add(cbVoter);
        pnCbox.revalidate();
    }

    void update() {
        int _voterID  = cbVoterID[cbVoter.getSelectedIndex()];
        int _candidateID = cbCandidateID[cbCandidate.getSelectedIndex()];
        p.removeAll();
        chp = spiderWebPlot(_voterID, _candidateID);
        chp.setPreferredSize(size);
        p.add(chp);
        p.revalidate();
        String str = "　　　有権者ID：" + calc.voter[_voterID].id;
        lbInfoVoterID.setText(str);
        str = "　　　候補者ID：" + calc.candidate[_candidateID].id;
        lbInfoCandidateID.setText(str);
        str = "　　　年齢：" + calc.voter[_voterID].age;
        lbInfoVoterAge.setText(str);
        str = "　　　政党：" + calc.party[calc.candidate[_candidateID].partyID].name + "党";
        lbInfoCandidatePartyName.setText(str);
        str = "　　　選挙区：" + calc.voter[_voterID].areaID;
        lbInfoVoterAreaID.setText(str);
        str = "　　　選挙区：" + calc.candidate[_candidateID].areaID;
        if(calc.candidate[_candidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
            str = "　　　選挙区：比例";
        }
        lbInfoCandidateAreaID.setText(str);
        str = "　　　小選挙区投票先：候補者" + calc.voter[_voterID].candidateID;
        if(calc.voter[_voterID].candidateID < 0){
            str = "　　　小選挙区投票先：なし";
        }
        lbInfoVoterCandidateID.setText(str);
        str = "　　　当選：" + calc.candidate[_candidateID].electionFlag;
        lbInfoCandidateElectionFlag.setText(str);
        str = "　　　比例区投票先：候補者" + calc.voter[_voterID].proportionalCandidateID;
        if(calc.voter[_voterID].proportionalCandidateID < 0){
            str = "　　　比例区投票先：なし";
        }
        lbInfoVoterproportionalCandidateID.setText(str);
        if(calc.candidate[_candidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
            str = "　　　得票数：" + calc.candidate[_candidateID].votes + "／" + calc.proportionalArea.getNoVoter();
        }else {
            str = "　　　得票数：" + calc.candidate[_candidateID].votes + "／" + calc.area[calc.candidate[_candidateID].areaID].getNoVoter();
        }
        lbInfoCandidateVotes.setText(str);
        if(calc.voter[_voterID].areaID == calc.candidate[_candidateID].areaID || calc.candidate[_candidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
            lbInfoVoterValue.setForeground(Color.BLACK);
            str = "　　　候補者"+_candidateID+"の評価：" + calc.evaluateCandidate(_voterID, _candidateID);
        } else {
            lbInfoVoterValue.setForeground(Color.RED);
            str = "　　　地域が違います！";
        }
        lbInfoVoterValue.setText(str);
        if(calc.candidate[_candidateID].areaID != DFN.PROPORTIONAL_AREA_ID){
            str = "　　　得票率：" + (double)calc.candidate[_candidateID].votes / calc.area[calc.candidate[_candidateID].areaID].getNoVoter();
        }else {
            str = "　　　得票率：" + (double)calc.candidate[_candidateID].votes / calc.proportionalArea.getNoVoter();
        }
        lbInfoCandidatePerVotes.setText(str);
    }

    JPanel spiderWebPlot(int _voterID, int _candidateID) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        if (ckCandidate.isSelected()){
            String cName = "候補者"+ _candidateID;
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(calc.candidate[_candidateID].interest[i], cName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }else{
            String cName = "候補者"+ _candidateID;
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(0.0, cName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }
        if (ckVoter.isSelected()){
            String vName = "有権者" + _voterID;
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(calc.voter[_voterID].interest[i], vName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }else{
            String vName = "有権者" + _voterID;
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(0.0, vName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }
        if (ckParty.isSelected()){
            String agName = calc.party[calc.candidate[_candidateID].partyID].name + "党方針";
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(calc.party[calc.candidate[_candidateID].partyID].policyParam[i], agName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }else{
            String agName = calc.party[calc.candidate[_candidateID].partyID].name + "党方針";
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(0.0, agName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }
        if (ckArea.isSelected()){
            String arName = "" + calc.candidate[_candidateID].areaID + "区平均";
            if(calc.candidate[_candidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
                arName = "比例区平均";
            }
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                if(calc.candidate[_candidateID].areaID != DFN.PROPORTIONAL_AREA_ID){
                    ds.addValue(calc.area[calc.candidate[_candidateID].areaID].interest[i], arName, DFN.QUESTIONNAIRE_RES_NAME[i]);
                }else {
                    ds.addValue(calc.proportionalArea.interest[i], arName, DFN.QUESTIONNAIRE_RES_NAME[i]);
                }
            }
        }else{
            String arName = "" + calc.candidate[_candidateID].areaID + "区平均";
            if(calc.candidate[_candidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
                arName = "比例区平均";
            }
            for (int i=0;i<DFN.QUESTIONNAIRE_RES_NAME.length;i++ ) {
                ds.addValue(0.0, arName, DFN.QUESTIONNAIRE_RES_NAME[i]);
            }
        }
        //SpiderWebPlot sp = new SpiderWebPlot(ds);
        GaugeSpiderWebPlot sp = new GaugeSpiderWebPlot(ds);
        sp.setMaxValue(1.0);
        JFreeChart fc = new JFreeChart("関心度", TextTitle.DEFAULT_FONT, sp, true);
        Plot plot = fc.getPlot();
        plot.setBackgroundPaint(new Color(169, 169, 169));
        ChartPanel cp = new ChartPanel(fc);
        return cp;
    }

    class SpiderWebActionListenerCbox implements ActionListener{
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource()==cbCandidate){
                updateCboxV();
            }
            update();
        }

    }

    class ComponentAdapterJFrame extends ComponentAdapter{
        public void componentResized(ComponentEvent e) {
            size = getContentPane().getSize();
            if(size.height-260 >= 0){
                size.setSize(size.width, size.height-260);
            }
            update();
        }
    }

}