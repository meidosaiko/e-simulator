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
// Voterクラス
// 投票者の制御
// 最終更新日 2017/02/08 0:04
//
///////////////////////////////////////////////////////////////////////////////////////////////////////
public class Voter extends Human{

    int candidateID;
    int proportionalCandidateID;
    double discontent;
    protected boolean[] questionnaireRes = new boolean[DFN.NO_PARAM];
    protected double[] individualValue = new double[DFN.NO_PARAM];

    Voter(int _id, int _x, int _y, int _areaID, int _age){
        super(_id,_x,_y, _areaID, _age);
    }

    /**
     * 投票情報の初期化
     */
    void resetVote() {
        candidateID = -1;
        proportionalCandidateID=-1;
        discontent = 0.0;
    }

    /**
     * 関心度合いの設定
     */
    void setInterest() {
        double tmp;
        for (int i=0;i<interest.length;i++) {
            if(questionnaireRes[i]==true){
                tmp = Math.random()*0.5+0.5;
                if(2.0*tmp-1.0 < 0){
                    tmp = -1.0*Math.pow(-1.0*(2.0*tmp-1.0),1.0/3.0);
                }else {
                    tmp = Math.pow((2.0*tmp-1.0),1.0/3.0);
                }
            } else {
                tmp = Math.random()*0.5;
                tmp = -1.0*Math.pow(-1.0*(2.0*tmp-1.0),1.0/3.0);
            }
            interest[i] = 0.5*(tmp+1.0);
            individualValue[i] = interest[i];
        }
    }

    /**
     * 個性値のコピー
     * @param _individualValue コピー元の個性値
     */
    void copyIndividualValue(double[] _individualValue){
        for (int i=0; i<individualValue.length; i++ ) {
            individualValue[i] = _individualValue[i];
        }
    }

    /**
     * Voterのコピー
     * @param _voter コピー元のVoter
     */
    void copy(Voter _voter) {
        id = _voter.id;
        x = _voter.x;
        y = _voter.y;
        areaID = _voter.areaID;
        age = _voter.age;
        copyInterest(_voter.interest);
        copyIndividualValue(_voter.individualValue);
        candidateID = _voter.candidateID;
        proportionalCandidateID = _voter.proportionalCandidateID;
        discontent = _voter.discontent;
        questionnaireRes = _voter.questionnaireRes;
    }
}