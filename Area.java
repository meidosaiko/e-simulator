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
// Areaクラス
// 1選挙区の管理
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.Arrays;
import java.util.ArrayList;

public class Area {

    public int id;
    ArrayList<Integer> x = new ArrayList<Integer>();
    ArrayList<Integer> y = new ArrayList<Integer>();

    protected int noCandidate;
    protected int[] candidateID;

    //地域の関心平均
    protected double[] interest = new double[DFN.NO_PARAM];
    //年代別リスト
    ArrayList<Integer> voterList = new ArrayList<Integer>();
    ArrayList<Integer> youngList = new ArrayList<Integer>();
    ArrayList<Integer> middleList = new ArrayList<Integer>();
    ArrayList<Integer> upperList = new ArrayList<Integer>();

    protected boolean flagMerge;

    Area(int _noCandidate){
        noCandidate = _noCandidate;
        candidateID = new int[noCandidate];
        Arrays.fill(interest, 0);
        Arrays.fill(candidateID, -1);
        flagMerge = false;
    }

    void initPoint(){
        x.clear();
        y.clear();
    }

    /**
     * 位置の設定
     * @param _id    [description]
     * @param _index [description]
     * @param _x     [description]
     * @param _y     [description]
     */
    void setPoint(int _id, int _x ,int _y){
        id = _id;
        x.add(_x);
        y.add(_y);
    }

    //投票者数を返す
    int getNoVoter(){
        return voterList.size();
    }

    //若者数を返す
    int getNoYoung(){
        return youngList.size();
    }

    //中年数を返す
    int getNoMiddle(){
        return middleList.size();
    }

    //老年数を返す
    int getNoUpper(){
        return upperList.size();
    }

    //エリアサイズを返す
    int getSize(){
        return x.size();
    }

    /**
     * 関心度の調査
     * @param _field [description]
     */
    void investigateInterest(Field _field) {
        Arrays.fill(interest, 0);
        for (int i=0;i<interest.length;i++ ) {
            for (int j=0;j<getSize();j++ ) {
                for(int k=0;k<_field.cell[y.get(j)][x.get(j)].noVoter;k++){
                    if(Math.random() < _field.voter[_field.cell[y.get(j)][x.get(j)].voterID[k]].interest[i]){
                        interest[i] += 1;
                    }
                }
            }
            interest[i] /= voterList.size();
        }
    }

    /**
     * 候補者リストの生成
     * @param _field [description]
     */
    void setCandidateList(Field _field) {
        Arrays.fill(candidateID, -1);
        int count=0;
        for (int i=0;i<getSize();i++ ) {
            for (int j=0;j<_field.cell[y.get(i)][x.get(i)].noCandidate;j++ ) {
                if(_field.candidate[_field.cell[y.get(i)][x.get(i)].candidateID[j]].areaID == id){
                    candidateID[count] = _field.cell[y.get(i)][x.get(i)].candidateID[j];
                    count++;
                }
            }
        }
    }

    /**
     * 年代別リストの生成
     * @param _areaID [description]
     */
    void setAgeList(Field _field) {
        voterList.clear();
        youngList.clear();
        middleList.clear();
        upperList.clear();
        for (int i=0;i<getSize();i++ ) {
            for (int j=0;j<_field.cell[y.get(i)][x.get(i)].noVoter;j++ ) {
                if(_field.voter[_field.cell[y.get(i)][x.get(i)].voterID[j]].age == DFN.AGE_YOUNG){
                    youngList.add(_field.cell[y.get(i)][x.get(i)].voterID[j]);
                }else if(_field.voter[_field.cell[y.get(i)][x.get(i)].voterID[j]].age == DFN.AGE_MIDDLE){
                    middleList.add(_field.cell[y.get(i)][x.get(i)].voterID[j]);
                }else {
                    upperList.add(_field.cell[y.get(i)][x.get(i)].voterID[j]);
                }
                voterList.add(_field.cell[y.get(i)][x.get(i)].voterID[j]);
            }
        }
    }

}