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
// Calcクラス
// 各種計算
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////

import java.util.Random;
import java.util.Arrays;

public class Calc extends Field{

    Calc(){
        super();
        setArea();
        setProportionalArea();
        setVoter();
        setParty();
        investigateArea();
        setCandidate();
    }

    void initVote(){
        /*投票数の初期化*/
        for (int i=0; i<DFN.NO_CANDIDATE;i++) {
            candidate[i].resetElection();
        }
        for (int i=0; i<DFN.NO_VOTER;i++) {
            voter[i].resetVote();
        }
    }

    /**
     * 投票
     */
    void progressVote(){
        /*投票の初期化*/
        initVote();
        /*関心の伝播*/
        Voter[] tmpVoter = new Voter[DFN.NO_VOTER];
        for (int i=0; i<DFN.NO_VOTER;i++) {
            tmpVoter[i] = new Voter(0,0,0,0,0);
            tmpVoter[i].copy(voter[i]);
            tmpVoter[i] = propagation(i);
        }
        for (int i=0; i<DFN.NO_VOTER;i++) {
            voter[i].copy(tmpVoter[i]);
        }
        /*年代別の関心度調査*/
        for (int i=0;i<party.length;i++ ) {
            investigationInterestAge(i);
        }
        /*エリア別の関心度調査*/
        investigateArea();
        /*候補者の方針調整*/
        for (int i=0; i<DFN.NO_CANDIDATE;i++ ) {
            if(candidate[i].areaID != DFN.PROPORTIONAL_AREA_ID){
                candidate[i].setInterest(area[candidate[i].areaID].interest, party[candidate[i].partyID].policyParam);
            }else {
                candidate[i].setInterest(proportionalArea.interest, party[candidate[i].partyID].policyParam);
            }
        }
        /*小選挙区投票*/
        for (int i=0; i<DFN.NO_VOTER;i++) {
            if(area[voter[i].areaID].flagMerge == true)continue;
            voter[i].candidateID = voteCandidate(i);
            candidate[voter[i].candidateID].votes++;
        }
        /*比例区投票*/
        for (int i=0; i<DFN.NO_VOTER;i++) {
            voter[i].proportionalCandidateID = voteProportionlaCandidate(i);
            if(voter[i].proportionalCandidateID < 0)continue;
            if(candidate[voter[i].proportionalCandidateID].areaID == DFN.PROPORTIONAL_AREA_ID){
                candidate[voter[i].proportionalCandidateID].votes++;
            }
        }
    }

    /**
     * 当選処理
     */
    void progressElection(){
        Random rnd = new Random(System.currentTimeMillis());
        /*小選挙区当選*/
        for (int i=0; i<area.length; i++) {
            if(area[i].flagMerge == true)continue;
            int candidateID = searchElection(i);
            candidate[candidateID].electionFlag = true;
        }
        /*比例区当選*/
        for (int i=0; i<proportionalSeats; i++) {
            int candidateID = searchProportionalElection();
                candidate[candidateID].electionFlag = true;
        }
        /*獲得議席の集計*/
        for (int i=0;i<party.length;i++ ) {
            party[i].seats = countSeats(i);
        }
    }

    /**
     * 投票後の処理
     */
    void progressAfter(){
        Random rnd = new Random(System.currentTimeMillis());
        /*不満の計算*/
        discontentYoung=0.0;
        discontentMiddle=0.0;
        discontentUpper=0.0;
        discontentAll=0.0;
        for (int i=0; i<DFN.NO_VOTER;i++) {
            voter[i].discontent = calcDiscontent(i);
            discontentAll += voter[i].discontent*voter[i].discontent;
            switch(voter[i].age){
                case DFN.AGE_YOUNG:
                    discontentYoung += voter[i].discontent*voter[i].discontent;
                    break;
                case DFN.AGE_MIDDLE:
                    discontentMiddle += voter[i].discontent*voter[i].discontent;
                    break;
                case DFN.AGE_UPPER:
                    discontentUpper += voter[i].discontent*voter[i].discontent;
                    break;
            }
        }
        discontentYoung /= youngList.size();
        discontentMiddle /= middleList.size();
        discontentUpper /= upperList.size();
        discontentAll /= DFN.NO_VOTER;
        discontentYoung = Math.sqrt(discontentYoung);
        discontentMiddle = Math.sqrt(discontentMiddle);
        discontentUpper = Math.sqrt(discontentUpper);
        discontentAll = Math.sqrt(discontentAll);
        /*投票者の地区内の移動*/
        /*
        for (int i=0; i<DFN.NO_VOTER;i++) {
            if(area[voter[i].areaID].size == 0)continue;
            int index = rnd.nextInt(area[voter[i].areaID].size);
            voter[i].x = area[voter[i].areaID].x[index];
            voter[i].y = area[voter[i].areaID].y[index];
        }*/
        /*候補者の地区内の移動*/
        /*
        for (int i=0; i<DFN.NO_CANDIDATE;i++) {
            int index = rnd.nextInt(area[candidate[i].areaID].size);
            candidate[i].x = area[candidate[i].areaID].x[index];
            candidate[i].y = area[candidate[i].areaID].y[index];
        }*/
    }

    double convDefault(double x, double sigma, double mu){
        return (1.0/Math.sqrt(2.0*Math.PI*Math.pow(sigma,2.0)))*Math.exp(-1.0*(Math.pow(x-mu,2)/(2*Math.pow(sigma,2))));
    }

    /**
     * 関心の伝播
     * @param _voterID [description]
     */
    Voter propagation(int _voterID) {
        Voter tmpVoter = new Voter(0,0,0,0,0);
        tmpVoter.copy(voter[_voterID]);
        double maxD = Math.sqrt(DFN.HEIGHT*DFN.HEIGHT + DFN.WIDTH*DFN.WIDTH);
        for (int i=0; i<DFN.NO_VOTER;i++) {
            if(i == _voterID)continue;
            double d = Math.sqrt((voter[_voterID].y-voter[i].y)*(voter[_voterID].y-voter[i].y)+(voter[_voterID].x-voter[i].x)*(voter[_voterID].x-voter[i].x));
            double p = convDefault(d, 10.0, 0.0)*10;
            if(Math.random() < p){
                for (int j=0; j<(voter[_voterID].interest).length; j++ ) {
                    double c1 = Math.random();
                    double u;
                    double t;
                    if(voter[i].age == voter[_voterID].age){
                        u = 0.5*Math.random();
                        t = 0.1*Math.random()+0.9;
                    }else {
                        u = 0.1*Math.random();
                        t = 0.4*Math.random()+0.6;
                    }
                    double r = Math.random();
                    if(r < u){
                        tmpVoter.interest[j] = (tmpVoter.interest[j] + c1*Math.abs(tmpVoter.individualValue[j]-voter[i].interest[j]))/(1+c1);
                    }else if(r > t){
                        tmpVoter.interest[j] = (tmpVoter.interest[j] + c1*(1-Math.abs(tmpVoter.individualValue[j]-voter[i].interest[j])))/(1+c1);
                    }else {
                        tmpVoter.interest[j] = (tmpVoter.individualValue[j] + c1*(Math.abs(tmpVoter.individualValue[j]-tmpVoter.interest[j])))/(1+c1);
                    }
                }
            }
        }
        return tmpVoter;
    }

    /**
     * 不満の計算
     * @param  _voterID [description]
     * @return          [description]
     */
    double calcDiscontent(int _voterID) {
        double max = Integer.MIN_VALUE;
        double min = Integer.MAX_VALUE;
        double electionValue = 0.0;
        double score=0.0;
        for (int i=0; i<area[voter[_voterID].areaID].noCandidate;i++ ) {
            score = evaluateCandidate(_voterID, area[voter[_voterID].areaID].candidateID[i]);
            if(candidate[area[voter[_voterID].areaID].candidateID[i]].electionFlag == true){
                electionValue = score;
            }
            if(max < score){
                max = score;
            }
            if(min > score){
                min = score;
            }
        }
        double singleScore = (electionValue-min)/(max-min);
        electionValue = 0;
        max = Integer.MIN_VALUE;
        min = Integer.MAX_VALUE;
        for (int i=0; i<proportionalArea.noCandidate;i++ ) {
            score = evaluateCandidate(_voterID, proportionalArea.candidateID[i]);
            if(candidate[proportionalArea.candidateID[i]].electionFlag == true){
                electionValue += score;
            }
            if(max < score){
                max = score;
            }
            if(min > score){
                min = score;
            }
        }
        if(proportionalSeats > 0) electionValue /= proportionalSeats;
        double proportionalScore = (electionValue-min)/(max-min);
        return ((DFN.NO_SEATS - proportionalSeats)*singleScore + (proportionalSeats)*proportionalScore)/DFN.NO_SEATS;
    }

    /**
     * 小選挙区開票
     * @param  _areaID [description]
     * @return         [description]
     */
    int searchElection(int _areaID){
        int candidateID=-1;
        int max = Integer.MIN_VALUE;
        for (int i=0;i<area[_areaID].noCandidate;i++ ) {
            if(max < candidate[area[_areaID].candidateID[i]].votes ){
                max = candidate[area[_areaID].candidateID[i]].votes;
                candidateID=area[_areaID].candidateID[i];
            }
        }
        return candidateID;
    }

    /**
     * 比例区開票
     * @param  _areaID [description]
     * @return         [description]
     */
    int searchProportionalElection(){
        int candidateID=-1;
        int max = Integer.MIN_VALUE;
        for (int i=0;i<proportionalArea.noCandidate;i++ ) {
            if(candidate[proportionalArea.candidateID[i]].electionFlag == true)continue;
            if(max < candidate[proportionalArea.candidateID[i]].votes ){
                max = candidate[proportionalArea.candidateID[i]].votes;
                candidateID = proportionalArea.candidateID[i];
            }
        }
        return candidateID;
    }

    /**
     * 小選挙区投票
     * @param  _voterID 投票者のID
     * @return          投票先の候補者ID
     */
    int voteCandidate(int _voterID){
        int candidateID=-1;
        double min = Integer.MAX_VALUE;
        for (int i=0; i<area[voter[_voterID].areaID].noCandidate;i++ ) {
            double score = evaluateCandidate(_voterID, area[voter[_voterID].areaID].candidateID[i]);
            if(min > score){
                min = score;
                candidateID = area[voter[_voterID].areaID].candidateID[i];
            }
        }
        return candidateID;
    }

    /**
     * 比例区投票
     * @param  _voterID [description]
     * @return          [description]
     */
    int voteProportionlaCandidate(int _voterID){
        int candidateID=-1;
        double min = Integer.MAX_VALUE;
        for (int i=0; i<proportionalArea.noCandidate;i++ ) {
            double score = evaluateCandidate(_voterID, proportionalArea.candidateID[i]);
            if(min > score){
                min = score;
                candidateID = proportionalArea.candidateID[i];
            }
        }
        return candidateID;
    }

    /**
     * 候補者の評価
     * @param  _voterID     [description]
     * @param  _candidateID [description]
     * @return              [description]
     */
    double evaluateCandidate(int _voterID, int _candidateID){
        double score = 0.0;
        for (int i=0;i<DFN.NO_PARAM;i++ ) {
            score += (voter[_voterID].interest[i] - candidate[_candidateID].interest[i])*(voter[_voterID].interest[i] - candidate[_candidateID].interest[i]);
        }
        return score;
    }

    /**
     * 獲得議席数の計算
     * @param  _partyID [description]
     * @return          [description]
     */
    int countSeats(int _partyID) {
        int count = 0;
        for (int i=0; i<DFN.NO_CANDIDATE;i++) {
            if(candidate[i].partyID == _partyID && candidate[i].electionFlag == true) {
                count++;
            }
        }
        return count;
    }

}