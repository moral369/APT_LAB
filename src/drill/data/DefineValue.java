package drill.data;

/**
 * Created by Chan-Ju on 2015-09-12.
 */
public class DefineValue {

    public static final int IT_IS_NOTHING = 0x0000FFFF;

    //�ܰ躰 ���� ���ڿ�
    public static final String ACTDIV_URLACC = "urlacc" ;
    public static final String ACTDIV_INSTALL = "install" ;
    public static final String ACTDIV_EXEC = "exec" ;

    //�׷�� ���� ���ڿ�
    public static final String GROUPTYPE_SFG = "SFG"; //��������
    public static final String GROUPTYPE_SHB = "SHB"; //��������
    public static final String GROUPTYPE_SHC = "SHC"; //����ī��
    public static final String GROUPTYPE_SHI = "SHI"; //���ѱ�������
    public static final String GROUPTYPE_SHL = "SHL"; //���ѻ�����
    public static final String GROUPTYPE_BNP = "BNP"; //����BNP�ĸ����ڻ���
    public static final String GROUPTYPE_CAP = "CAP"; //����ĳ��Ż
    public static final String GROUPTYPE_JJB = "JJB"; //��������
    public static final String GROUPTYPE_SAV = "SAV"; //������������
    public static final String GROUPTYPE_SDS = "SDS"; //���ѵ���Ÿ�ý���
    public static final String GROUPTYPE_TAS = "TAS"; //���Ѿ���Ÿ��
    public static final String GROUPTYPE_SCI = "SCI"; //���ѽſ�����
    public static final String GROUPTYPE_SSF = "SSF"; // (���� ��Į�� �Ŀ�̼�)�����������
    public static final String GROUPTYPE_ETC = "ETC"; //������(A3,���հ�����SDS�ο�)

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
        public static final String SFG = ("SHINHAN GROUP");//"��������"; //1;
        public static final String SHB = ("SHINHAN BANK");//"��������"; //2;
        public static final String SHC = ("SHINHAN CARD");//"����ī��"; //3;
        public static final String SHI = ("SHINHAN INVEST");;//"���ѱ�������"; //4;
        public static final String SHL = ("SHINHAN LIFE");;//"���ѻ�����"; //5;
        public static final String BNP = ("SHINHAN BNPP");;//"����BNP�ĸ����ڻ���"; //6;
        public static final String CAP = ("SHINHAN CAPITAL");;//"����ĳ��Ż"; //7;
        public static final String JJB = ("SHINHAN JEJU BANK");;//"��������"; //8;
        public static final String SAV = ("SHINHAN SAVINGS");;//"������������"; //9;
        public static final String SDS = ("SHINHAN DATA SYSTEM");;//"���ѵ���Ÿ�ý���"; //10;
        public static final String TAS = ("SHINHAN AITAS");;//"���Ѿ���Ÿ��"; //11;
        public static final String SCI = ("SHINHAN CI");;//"���ѽſ�����"; //12;
        public static final String SSF = ("SHINHAN SF");;//"�����������"; //13;
        public static final String ETC = ("SHINHAN SEURITY LAB");;//"���պ��Ȱ��������ο�"; //14;
    }
}