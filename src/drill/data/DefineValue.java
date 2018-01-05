package drill.data;

/**
 * Created by Chan-Ju on 2015-09-12.
 */
public class DefineValue {

    public static final int IT_IS_NOTHING = 0x0000FFFF;

    //단계별 구분 문자열
    public static final String ACTDIV_URLACC = "urlacc" ;
    public static final String ACTDIV_INSTALL = "install" ;
    public static final String ACTDIV_EXEC = "exec" ;

    //그룹사 구분 문자열
    public static final String GROUPTYPE_SFG = "SFG"; //신한지주
    public static final String GROUPTYPE_SHB = "SHB"; //신한은행
    public static final String GROUPTYPE_SHC = "SHC"; //신한카드
    public static final String GROUPTYPE_SHI = "SHI"; //신한금융투자
    public static final String GROUPTYPE_SHL = "SHL"; //신한생명보험
    public static final String GROUPTYPE_BNP = "BNP"; //신한BNP파리바자산운용
    public static final String GROUPTYPE_CAP = "CAP"; //신한캐피탈
    public static final String GROUPTYPE_JJB = "JJB"; //제주은행
    public static final String GROUPTYPE_SAV = "SAV"; //신한저축은행
    public static final String GROUPTYPE_SDS = "SDS"; //신한데이타시스템
    public static final String GROUPTYPE_TAS = "TAS"; //신한아이타스
    public static final String GROUPTYPE_SCI = "SCI"; //신한신용정보
    public static final String GROUPTYPE_SSF = "SSF"; // (신한 스칼라쉽 파운데이션)신한장학재단
    public static final String GROUPTYPE_ETC = "ETC"; //나머지(A3,통합관제실SDS인원)

    public class SHINHAN_APT_STATE {
        public static final String S_SMS_SEND = "SMS SEND";
        public static final String S_NO_RESPONSE = "NO RESPONSE";
        public static final String S_WEB_ACCESS = "URL ACCESS";
        public static final String S_APP_INSTALL = "APP EXECUTE";
        public static final String S_APP_EXECUTE =    "ACQUIRE PRIV";
        public static final String S_ACQ_PERMISSION = "ACQUIRE PRIV"; // PERMISSION"; privileges

    }

    public class SHINHAN_GROUP_CODE
    {
        public static final int SFG = 1;
        public static final int SHB = 2;
        public static final int SHC = 3;
        public static final int SHI = 4;
        public static final int SHL = 5;
        public static final int BNP = 6;
        public static final int CAP = 7;
        public static final int JJB = 8;
        public static final int SAV = 9;
        public static final int SDS = 10;
        public static final int TAS = 11;
        public static final int SCI = 12;
        public static final int SSF = 13;
        public static final int ETC = 14;
    }

    public class  SHINHAN_GROUP_NAME
    {
        public static final String SFG = ("SHINHAN GROUP");//"신한지주"; //1;
        public static final String SHB = ("SHINHAN BANK");//"신한은행"; //2;
        public static final String SHC = ("SHINHAN CARD");//"신한카드"; //3;
        public static final String SHI = ("SHINHAN INVEST");;//"신한금융투자"; //4;
        public static final String SHL = ("SHINHAN LIFE");;//"신한생명보험"; //5;
        public static final String BNP = ("SHINHAN BNPP");;//"신한BNP파리바자산운용"; //6;
        public static final String CAP = ("SHINHAN CAPITAL");;//"신한캐피탈"; //7;
        public static final String JJB = ("SHINHAN JEJU BANK");;//"제주은행"; //8;
        public static final String SAV = ("SHINHAN SAVINGS");;//"신한저축은행"; //9;
        public static final String SDS = ("SHINHAN DATA SYSTEM");;//"신한데이타시스템"; //10;
        public static final String TAS = ("SHINHAN AITAS");;//"신한아이타스"; //11;
        public static final String SCI = ("SHINHAN CI");;//"신한신용정보"; //12;
        public static final String SSF = ("SHINHAN SF");;//"신한장학재단"; //13;
        public static final String ETC = ("SHINHAN SEURITY LAB");;//"통합보안관제센터인원"; //14;
    }
}