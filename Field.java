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
// Fieldクラス
// フィールド全体の制御
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.Random;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;

public class Field{

    //エリアの配置を表した2次元配列
    protected int[][] areaMap = new int[DFN.HEIGHT/DFN.AREA_SIZE][DFN.WIDTH/DFN.AREA_SIZE];
    //セル：フィールドの1マスの相当
    protected Cell[][] cell = new Cell[DFN.HEIGHT][DFN.WIDTH];
    //小選挙区エリア
    protected Area[] area = new Area[DFN.NO_AREA];
    //比例区エリア
    protected Area proportionalArea = new Area(DFN.NO_CANDIDATE);

    //投票者
    protected Voter[] voter = new Voter[DFN.NO_VOTER];
    //候補者
    protected Candidate[] candidate = new Candidate[DFN.NO_CANDIDATE];
    //政党
    protected Party[] party = new Party[DFN.NO_PARTY];

    //不満
    protected double discontentYoung=0.0;
    protected double discontentMiddle=0.0;
    protected double discontentUpper=0.0;
    protected double discontentAll = 0.0;

    //世代別リスト
    ArrayList<Integer> youngList = new ArrayList<Integer>();
    ArrayList<Integer> middleList = new ArrayList<Integer>();
    ArrayList<Integer> upperList = new ArrayList<Integer>();

    //議席数
    protected int singleSeats = DFN.NO_SEATS;
    protected int proportionalSeats = 0;

    /**
     * コンストラクタ：フィールドの初期化
     */
    Field(){
        int countX = 0;
        int countY = 0;
        for (int i=0; i<DFN.HEIGHT; i++) {
            countY = countX;
            for (int j=0; j<DFN.WIDTH; j++) {
                cell[i][j] = new Cell(countY);
                if( (j+1)%DFN.AREA_SIZE == 0){
                    countY++;
                }
            }
            if( (i+1)%DFN.AREA_SIZE == 0 ){
                countX = countY;
            }
        }
        for (int i=0;i<DFN.NO_AREA;i++ ) {
            area[i] = new Area(DFN.NO_AREA_CANDIDATE);
        }
        proportionalArea.noCandidate = 0;
    }

    /**
     * セルの設定
     */
    void setCell() {
        for (int i=0; i<DFN.HEIGHT; i++) {
            for (int j=0; j<DFN.WIDTH; j++) {
                cell[i][j].noVoter = 0;
                cell[i][j].noCandidate = 0;
            }
        }
        for (int i=0;i<voter.length;i++) {
            cell[voter[i].y][voter[i].x].voterID[cell[voter[i].y][voter[i].x].noVoter] = voter[i].id;
            cell[voter[i].y][voter[i].x].noVoter++;
        }

        for (int i=0;i<candidate.length;i++) {
            cell[candidate[i].y][candidate[i].x].candidateID[cell[candidate[i].y][candidate[i].x].noCandidate] = candidate[i].id;
            cell[candidate[i].y][candidate[i].x].noCandidate++;
        }
    }

    /**
     * 投票者の設定
     */
    void setVoter(){
        int countVoter=0;
        Random rnd = new Random(System.currentTimeMillis());
        //最低人数の設定(動作保証の為)
        for (int i=0;i<DFN.NO_AREA;i++) {
            for (int j=0;j<DFN.MIN_AREA_VOTER;j++) {
                int index = rnd.nextInt(area[i].getSize());
                if(cell[area[i].y.get(index)][area[i].x.get(index)].noVoter < DFN.MAX_CELL_VOTER){
                    int x = area[i].x.get(index);
                    int y = area[i].y.get(index);
                    voter[countVoter] = new Voter(countVoter, x, y, cell[y][x].areaID, 0);
                    cell[y][x].voterID[cell[y][x].noVoter] = voter[countVoter].id;
                    cell[y][x].noVoter++;
                    countVoter++;
                }else j--;
            }
        }
        //1回の配置人数の設定
        int oneSet = (voter.length - countVoter) / DFN.NO_AREA;
        //行分割による配置
        for (int i=0; i<DFN.HEIGHT/DFN.AREA_SIZE;i++) {
            for (int j=0; j<oneSet; j++) {
                int x = rnd.nextInt(DFN.WIDTH);
                int y = rnd.nextInt(DFN.HEIGHT - i*DFN.AREA_SIZE) + i*DFN.AREA_SIZE;
                if(cell[y][x].noVoter < DFN.MAX_CELL_VOTER){
                    voter[countVoter] = new Voter(countVoter, x, y, cell[y][x].areaID, 0);
                    cell[y][x].voterID[cell[y][x].noVoter] = voter[countVoter].id;
                    cell[y][x].noVoter++;
                    countVoter++;
                }else j--;
            }
        }
        //列分割による配置
        for (int i=0; i<DFN.WIDTH/DFN.AREA_SIZE;i++) {
            for (int j=0; j<oneSet; j++) {
                int x = rnd.nextInt(DFN.WIDTH - i*DFN.AREA_SIZE) + i * DFN.AREA_SIZE;
                int y = rnd.nextInt(DFN.HEIGHT);
                if(cell[y][x].noVoter < DFN.MAX_CELL_VOTER){
                    voter[countVoter] = new Voter(countVoter, x, y, cell[y][x].areaID, 0);
                    cell[y][x].voterID[cell[y][x].noVoter] = voter[countVoter].id;
                    cell[y][x].noVoter++;
                    countVoter++;
                }else j--;
            }
        }
        //余りの無いように配置
        while(countVoter < DFN.NO_VOTER){
            int x = rnd.nextInt(DFN.WIDTH);
            int y = rnd.nextInt(DFN.HEIGHT);
            if(cell[y][x].noVoter < DFN.MAX_CELL_VOTER){
                voter[countVoter] = new Voter(countVoter, x, y, cell[y][x].areaID, 0);
                cell[y][x].voterID[cell[y][x].noVoter] = voter[countVoter].id;
                cell[y][x].noVoter++;
                countVoter++;
            }
        }
        //年齢の設定
        youngList.clear();
        middleList.clear();
        upperList.clear();
        for (int i=0;i<DFN.NO_VOTER;i++) {
            double p = Math.random();
            if(p < DFN.PER_UPPER){
                voter[i].age = DFN.AGE_UPPER;
                upperList.add(voter[i].id);
            }else if(DFN.PER_UPPER <= p && p < DFN.PER_UPPER+DFN.PER_MIDDLE){
                voter[i].age = DFN.AGE_MIDDLE;
                middleList.add(voter[i].id);
            }else {
                voter[i].age = DFN.AGE_YOUNG;
                youngList.add(voter[i].id);
            }
        }
        //アンケート結果から個性を生成
        setQuestionnaireRes();
        for (int i=0;i<DFN.NO_VOTER;i++) {
            voter[i].setInterest();
        }
    }

    /**
     * アンケート結果から擬似的な回答集団を生成
     */
    void setQuestionnaireRes(){
        Random rnd = new Random(System.currentTimeMillis());
        for (int i=0;i<DFN.NO_PARAM;i++) {
            int noRes = (int)(youngList.size()*DFN.QUESTIONNAIRE_RES_Y[i]+0.5);
            for (int count=0; count<noRes;) {
                int index = rnd.nextInt(youngList.size());
                if(voter[youngList.get(index)].questionnaireRes[i] == false){
                    voter[youngList.get(index)].questionnaireRes[i] = true;
                    count++;
                }
            }
        }
        for (int i=0;i<DFN.NO_PARAM;i++) {
            int noRes = (int)(middleList.size()*DFN.QUESTIONNAIRE_RES_M[i]+0.5);
            for (int count = 0;count<noRes;) {
                int index = rnd.nextInt(middleList.size());
                if(voter[middleList.get(index)].questionnaireRes[i] == false){
                    voter[middleList.get(index)].questionnaireRes[i] = true;
                    count++;
                }
            }
        }
        for (int i=0;i<DFN.NO_PARAM;i++) {
            int noRes = (int)(upperList.size()*DFN.QUESTIONNAIRE_RES_U[i]+0.5);
            for (int count = 0;count<noRes;) {
                int index = rnd.nextInt(upperList.size());
                if(voter[upperList.get(index)].questionnaireRes[i] == false){
                    voter[upperList.get(index)].questionnaireRes[i] = true;
                    count++;
                }
            }
        }
    }

    /**
     * 政党の設定
     */
    void setParty(){
        //政党の生成
        for (int i=0; i<party.length;i++ ) {
            party[i] = new Party(i,DFN.PARTY_NAME[i],DFN.PARTY_AGE[i]);
            investigationInterestAge(i);
        }
    }

    /**
     * 年代別の関心度調査
     */
    void investigationInterestAge(int _partyID) {
        int noPeople;
        ArrayList<Integer> list = new ArrayList<Integer>();
        switch(party[_partyID].age){
            case DFN.AGE_YOUNG:
            noPeople = youngList.size();
            list = youngList;
            break;
            case DFN.AGE_MIDDLE:
            noPeople = middleList.size();
            list = middleList;
            break;
            default:
            noPeople = upperList.size();
            list = upperList;
            break;
        }
        for (int i=0;i<(party[_partyID].policyParam).length;i++) {
            party[_partyID].policyParam[i] = 0;
            for (int j=0;j<noPeople;j++) {
                double r = Math.random();
                if(r < voter[list.get(j)].interest[i]){
                    party[_partyID].policyParam[i] += 1;
                }
            }
            party[_partyID].policyParam[i] /= noPeople;
        }
    }

    /**
     * 候補者の配置
     */
    void setCandidate(){
        int count = 0;
        Random rnd = new Random(System.currentTimeMillis());
        for (int i=0; i < DFN.NO_AREA; i++ ) {
            for (int j=0; j<DFN.NO_AREA_CANDIDATE;j++ ) {
                int index = rnd.nextInt(area[i].getSize());
                if(cell[area[i].y.get(index)][area[i].x.get(index)].noCandidate < DFN.MAX_CELL_CANDIDATE){
                    candidate[count] = new Candidate(j, count, area[i].x.get(index), area[i].y.get(index), i, DFN.PARTY_AGE[j]);
                    candidate[count].setInterest(area[i].interest, party[candidate[count].partyID].policyParam);
                    cell[area[i].y.get(index)][area[i].x.get(index)].candidateID[cell[area[i].y.get(index)][area[i].x.get(index)].noCandidate] = count;
                    cell[area[i].y.get(index)][area[i].x.get(index)].noCandidate++;
                    area[i].candidateID[j] = count;
                    count++;
                }else {
                    j--;
                }
            }
        }
    }

    /**
     * 比例区の設定
     */
    void setProportionalArea() {
        proportionalArea.initPoint();
        for (int i=0; i<DFN.HEIGHT; i++) {
            for (int j=0; j<DFN.WIDTH; j++) {
                proportionalArea.setPoint(DFN.PROPORTIONAL_AREA_ID, j, i);
            }
        }
    }

    /**
     * 選挙区の設定
     */
    void setArea(){
        for (int i=0; i<area.length; i++) {
            area[i].initPoint();
        }
        for (int i=0; i<DFN.HEIGHT; i++) {
            for (int j=0; j<DFN.WIDTH; j++) {
                areaMap[(int)(i/DFN.AREA_SIZE)][(int)(j/DFN.AREA_SIZE)] = cell[i][j].areaID;
                area[cell[i][j].areaID].setPoint(cell[i][j].areaID, j, i);
            }
        }
    }

    /**
     * 調査
     */
    void investigateArea() {
        for (int i=0;i<area.length;i++ ) {
            area[i].setAgeList(this);
            area[i].investigateInterest(this);
        }
        proportionalArea.setAgeList(this);
        proportionalArea.investigateInterest(this);
    }


    /**
     *  エリアの合併
     */
    void mergerArea(){
        Random rnd = new Random(System.currentTimeMillis());
        int min1 = Integer.MAX_VALUE;
        int min2 = Integer.MAX_VALUE;
        int fromID = 0, toID = 0;
        //合併元の決定
        for (int i=0;i<area.length;i++ ) {
            if(area[i].flagMerge == true)continue;
            if(min1 > area[i].getNoVoter()){
                min1 = area[i].getNoVoter();
                fromID = i;
            }
        }
        //合併先の決定
        for (int i=0;i<areaMap.length;i++) {
            for (int j=0;j<areaMap[i].length;j++ ) {
                if(areaMap[i][j] == fromID){
                    for (int k=0;k<DFN.VEC_LENGTH4;k++) {
                        if(i + DFN.VEC_Y4[k] < 0 || j + DFN.VEC_X4[k] < 0)continue;
                        if(i + DFN.VEC_Y4[k] >= areaMap.length || j + DFN.VEC_X4[k] >= areaMap[i].length)continue;
                        int areaID = areaMap[i + DFN.VEC_Y4[k]][j+DFN.VEC_X4[k]];
                        if(areaID == fromID)continue;
                        if(min2 > area[areaID].getNoVoter() && area[areaID].getNoVoter() > min1){
                            min2 = area[areaID].getNoVoter();
                            toID = areaID;
                        }
                    }
                }
            }
        }

        if(fromID > 0 || toID > 0){
            for (int i=0; i<DFN.HEIGHT; i++) {
                for (int j=0; j<DFN.WIDTH; j++) {
                    if(cell[i][j].areaID == fromID){
                        cell[i][j].areaID = toID;
                    }
                }
            }
            for (int i=0; i<area[fromID].getNoYoung() ;i++) {
                voter[area[fromID].youngList.get(i)].areaID = toID;
            }
            for (int i=0; i<area[fromID].getNoMiddle() ;i++) {
                voter[area[fromID].middleList.get(i)].areaID = toID;
            }
            for (int i=0; i<area[fromID].getNoUpper() ;i++) {
                voter[area[fromID].upperList.get(i)].areaID = toID;
            }
            for (int i=0; i<area[fromID].noCandidate ;i++) {
                candidate[area[fromID].candidateID[i]].areaID = DFN.PROPORTIONAL_AREA_ID;
                candidate[area[fromID].candidateID[i]].resetElection();
                /*
                for (;;) {
                    int x = rnd.nextInt(DFN.WIDTH);
                    int y = rnd.nextInt(DFN.HEIGHT);
                    if(cell[y][x].noCandidate < DFN.MAX_CELL_CANDIDATE){
                        candidate[area[fromID].candidateID[i]].x = x;
                        candidate[area[fromID].candidateID[i]].y = y;
                        cell[y][x].noCandidate++;
                        break;
                    }
                }*/
                proportionalArea.candidateID[proportionalArea.noCandidate] = candidate[area[fromID].candidateID[i]].id;
                proportionalArea.noCandidate++;
            }
            area[fromID] = new Area(0);
            area[fromID].flagMerge = true;
            setCell();
            setArea();
            for (int i=0;i<area.length;i++ ){
                area[i].setAgeList(this);
                area[i].setCandidateList(this);
            }
            investigateArea();
            for (int i=0; i<DFN.NO_CANDIDATE;i++ ) {
                if(candidate[i].areaID == DFN.PROPORTIONAL_AREA_ID){
                    candidate[i].setInterest(proportionalArea.interest, party[candidate[i].partyID].policyParam);
                }
            }
            singleSeats--;
            proportionalSeats++;
        }
    }

}