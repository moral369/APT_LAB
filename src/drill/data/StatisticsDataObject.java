package drill.data;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Chan-Ju on 2015-09-06.
 */

//
public class StatisticsDataObject
{
    private final int ROW_SFG = 1 ;
    private final int ROW_SHB = 2 ;
    private final int ROW_SHC = 3 ;
    private final int ROW_SHI = 4 ;
    private final int ROW_SHL = 5 ;
    private final int ROW_BNP = 6 ;
    private final int ROW_CAP = 7 ;
    private final int ROW_JJB = 8 ;
    private final int ROW_SAV = 9 ;
    private final int ROW_SDS = 10 ;
    private final int ROW_TAS = 11 ;
    private final int ROW_SCI = 12 ;
    private final int ROW_SSF = 13 ;
    private final int ROW_ETC = 14 ;

    private final int COL_TOTAL_MAN     = 1 ; // 전체 참여 인원
    private final int COL_NO_RESPONSE   = 2 ; // 무반응 인원
    private final int COL_WEB_ACCESS    = 3 ; // 문자안의 URI 클릭 인원
    private final int COL_APP_DOWN      = 4 ; // 어플 다운로드 인원
    private final int COL_APP_INSTALL   = 5 ; // 어플 설치 인원
    private final int COL_APP_EXECUTE   = 6 ; // 어플 실행 인원



    private final int COL_URL_ACC_COUNT = 7; //url 클릭 총계 , 중복제거 없이
    private final int COL_EXEC_COUNT = 8; //프로그램 실행 총계, 중복제거 없이
    private final int COL_MANY_TIMES_URL_ACC_MAN = 9; //url 접근 2번이상 인원 수 (중복제거 한 카운트)
    private final int COL_MANY_TIMES_EXEC_MAN = 10;  // 어플 실행 인원 수 (중복제거 한 카운트)


    private final int MAX_ROW_NO = 16;
    private final int MAX_COL_NO = 16;


    //자료구조
    private int[][] m_statiscs_table;  //= new int[MAX_ROW_NO][MAX_COL_NO];

    //전체 그룹 한번에 볼때 사용하는 큐
    private Queue<AccessData> mAccessDataQueue4all;

    //그룹사 별로 보여주기 위한 큐
    private Queue<AccessData> mAccessDataQueue4SFG;
    private Queue<AccessData> mAccessDataQueue4SHB;
    private Queue<AccessData> mAccessDataQueue4SHC;
    private Queue<AccessData> mAccessDataQueue4SHI;
    private Queue<AccessData> mAccessDataQueue4SHL;
    private Queue<AccessData> mAccessDataQueue4BNP;
    private Queue<AccessData> mAccessDataQueue4CAP;
    private Queue<AccessData> mAccessDataQueue4JJB;
    private Queue<AccessData> mAccessDataQueue4SAV;
    private Queue<AccessData> mAccessDataQueue4SDS;
    private Queue<AccessData> mAccessDataQueue4TAS;
    private Queue<AccessData> mAccessDataQueue4SCI;
    private Queue<AccessData> mAccessDataQueue4SSF;
    private Queue<AccessData> mAccessDataQueue4ETC;

    //mutex를 사용하기 위한 변수
    private static Object lock4all = new Object();

    //그룹사별 큐 뮤텍스 처리
    private static Object lock4sfg = new Object(); //SFG
    private static Object lock4shb = new Object(); //SHB
    private static Object lock4shc = new Object(); //SHC
    private static Object lock4shi = new Object(); //SHI
    private static Object lock4shl = new Object(); //SHL
    private static Object lock4bnp = new Object(); //BNP
    private static Object lock4cap = new Object(); //CAP
    private static Object lock4jjb = new Object(); //JJB
    private static Object lock4sav = new Object(); //SAV
    private static Object lock4sds = new Object(); //SDS
    private static Object lock4tas = new Object(); //TAS
    private static Object lock4sci = new Object(); //SCI
    private static Object lock4ssf = new Object(); //SSF
    private static Object lock4etc = new Object(); //ETC


    private void init()
    {
        //배열 초기화
        for(int i=0 ; i< MAX_ROW_NO;++i)
            for(int j=0; j < MAX_COL_NO; ++j)
                m_statiscs_table[i][j] = 0;
    }

    private static StatisticsDataObject uniqueInstance = new StatisticsDataObject();

    private StatisticsDataObject()
    {
        m_statiscs_table = new int[MAX_ROW_NO][MAX_COL_NO];

        //전체 그룹 한번에 볼때 사용하는 큐
        mAccessDataQueue4all = new LinkedList<AccessData>();


        //그룹사 별로 보여주기 위한 큐
        mAccessDataQueue4SFG = new LinkedList<AccessData>();
        mAccessDataQueue4SHB = new LinkedList<AccessData>();
        mAccessDataQueue4SHC = new LinkedList<AccessData>();
        mAccessDataQueue4SHI = new LinkedList<AccessData>();
        mAccessDataQueue4SHL = new LinkedList<AccessData>();
        mAccessDataQueue4BNP = new LinkedList<AccessData>();
        mAccessDataQueue4CAP = new LinkedList<AccessData>();
        mAccessDataQueue4JJB = new LinkedList<AccessData>();
        mAccessDataQueue4SAV = new LinkedList<AccessData>();
        mAccessDataQueue4SDS = new LinkedList<AccessData>();
        mAccessDataQueue4TAS = new LinkedList<AccessData>();
        mAccessDataQueue4SCI = new LinkedList<AccessData>();
        mAccessDataQueue4SSF = new LinkedList<AccessData>();
        mAccessDataQueue4ETC = new LinkedList<AccessData>();


        init();
    }

    public static StatisticsDataObject getInstance(){
        if (uniqueInstance == null)
            uniqueInstance = new StatisticsDataObject();
        return uniqueInstance;
    }

    //메인화면에서 접근로그 뿌리기
    public AccessData getAccessData4all()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4all) {

            lAccessData = mAccessDataQueue4all.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4all(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4all) {
            lresult = mAccessDataQueue4all.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4all()
    {
        if(null != mAccessDataQueue4all.peek())
            return true;

        return false;
    }

    //------------------------------------
    //그룹사별 로그 뿌리기

    //SFG
    public AccessData getAccessData4sfg()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4sfg) {

            lAccessData = mAccessDataQueue4SFG.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4sfg(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4sfg) {
            lresult = mAccessDataQueue4SFG.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4sfg()
    {
        if(null != mAccessDataQueue4SFG.peek())
            return true;

        return false;
    }

    //SHB
    public AccessData getAccessData4shb()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4shb) {

            lAccessData = mAccessDataQueue4SHB.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4shb(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4shb) {
            lresult = mAccessDataQueue4SHB.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4shb()
    {
        if(null != mAccessDataQueue4SHB.peek())
            return true;

        return false;
    }

    //SHC
    public AccessData getAccessData4shc()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4shc) {

            lAccessData = mAccessDataQueue4SHC.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4shc(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4shc) {
            lresult = mAccessDataQueue4SHC.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4shc()
    {
        if(null != mAccessDataQueue4SHC.peek())
            return true;

        return false;
    }

    //SHI
    public AccessData getAccessData4shi()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4shi) {

            lAccessData = mAccessDataQueue4SHI.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4shi(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4shi) {
            lresult = mAccessDataQueue4SHI.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4shi()
    {
        if(null != mAccessDataQueue4SHI.peek())
            return true;

        return false;
    }

    //SHL
    public AccessData getAccessData4shl()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4shl) {

            lAccessData = mAccessDataQueue4SHL.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4shl(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4shl) {
            lresult = mAccessDataQueue4SHL.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4shl()
    {
        if(null != mAccessDataQueue4SHL.peek())
            return true;

        return false;
    }

    //BNP
    public AccessData getAccessData4bnp()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4bnp) {

            lAccessData = mAccessDataQueue4BNP.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4bnp(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4bnp) {
            lresult = mAccessDataQueue4BNP.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4bnp()
    {
        if(null != mAccessDataQueue4BNP.peek())
            return true;

        return false;
    }

    //CAP
    public AccessData getAccessData4cap()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4cap) {

            lAccessData = mAccessDataQueue4CAP.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4cap(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4cap) {
            lresult = mAccessDataQueue4CAP.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4cap()
    {
        if(null != mAccessDataQueue4CAP.peek())
            return true;

        return false;
    }

    //JJB
    public AccessData getAccessData4jjb()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4jjb) {

            lAccessData = mAccessDataQueue4JJB.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4jjb(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4jjb) {
            lresult = mAccessDataQueue4JJB.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4jjb()
    {
        if(null != mAccessDataQueue4JJB.peek())
            return true;

        return false;
    }

    //SAV
    public AccessData getAccessData4sav()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4sav) {

            lAccessData = mAccessDataQueue4SAV.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4sav(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4sav) {
            lresult = mAccessDataQueue4SAV.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4sav()
    {
        if(null != mAccessDataQueue4SAV.peek())
            return true;

        return false;
    }

    //SDS
    public AccessData getAccessData4sds()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4sds) {

            lAccessData = mAccessDataQueue4SDS.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4sds(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4sds) {
            lresult = mAccessDataQueue4SDS.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4sds()
    {
        if(null != mAccessDataQueue4SDS.peek())
            return true;

        return false;
    }

    //TAS
    public AccessData getAccessData4tas()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4tas) {

            lAccessData = mAccessDataQueue4TAS.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4tas(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4tas) {
            lresult = mAccessDataQueue4TAS.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4tas()
    {
        if(null != mAccessDataQueue4TAS.peek())
            return true;

        return false;
    }

    //SCI
    public AccessData getAccessData4sci()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4sci) {

            lAccessData = mAccessDataQueue4SCI.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4sci(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4sci) {
            lresult = mAccessDataQueue4SCI.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4sci()
    {
        if(null != mAccessDataQueue4SCI.peek())
            return true;

        return false;
    }

    //SSF
    public AccessData getAccessData4ssf()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4ssf) {

            lAccessData = mAccessDataQueue4SSF.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4ssf(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4ssf) {
            lresult = mAccessDataQueue4SSF.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4ssf()
    {
        if(null != mAccessDataQueue4SSF.peek())
            return true;

        return false;
    }

    //ETC
    public AccessData getAccessData4etc()
    {
        AccessData lAccessData;
        //세마포어나, 뮤텍스 넣어야함.
        synchronized(this.lock4etc) {

            lAccessData = mAccessDataQueue4ETC.poll();
        }
        return lAccessData;
    }
    public boolean setAccessData4etc(AccessData aAccessData)
    {
        //세마포어나, 뮤텍스 넣어야함.
        boolean lresult;
        synchronized(this.lock4etc) {
            lresult = mAccessDataQueue4ETC.offer(aAccessData);
        }
        return lresult;
    }
    public boolean isExistAccessDataInQueue4etc()
    {
        if(null != mAccessDataQueue4ETC.peek())
            return true;

        return false;
    }

    //++++++++++++++++++++++++++++++++++++
    //그룹사별 데이터 Getter


    public int get_SFG_APP_INSTALL(){ return m_statiscs_table[ROW_SFG][COL_APP_INSTALL]; }
    public int get_SHB_APP_INSTALL(){ return m_statiscs_table[ROW_SHB][COL_APP_INSTALL]; }
    public int get_SHC_APP_INSTALL(){ return m_statiscs_table[ROW_SHC][COL_APP_INSTALL]; }
    public int get_SHI_APP_INSTALL(){ return m_statiscs_table[ROW_SHI][COL_APP_INSTALL]; }
    public int get_SHL_APP_INSTALL(){ return m_statiscs_table[ROW_SHL][COL_APP_INSTALL]; }
    public int get_BNP_APP_INSTALL(){ return m_statiscs_table[ROW_BNP][COL_APP_INSTALL]; }
    public int get_CAP_APP_INSTALL(){ return m_statiscs_table[ROW_CAP][COL_APP_INSTALL]; }
    public int get_JJB_APP_INSTALL(){ return m_statiscs_table[ROW_JJB][COL_APP_INSTALL]; }
    public int get_SAV_APP_INSTALL(){ return m_statiscs_table[ROW_SAV][COL_APP_INSTALL]; }
    public int get_SDS_APP_INSTALL(){ return m_statiscs_table[ROW_SDS][COL_APP_INSTALL]; }
    public int get_TAS_APP_INSTALL(){ return m_statiscs_table[ROW_TAS][COL_APP_INSTALL]; }
    public int get_SCI_APP_INSTALL(){ return m_statiscs_table[ROW_SCI][COL_APP_INSTALL]; }
    public int get_SSF_APP_INSTALL(){ return m_statiscs_table[ROW_SSF][COL_APP_INSTALL]; }
    public int get_ETC_APP_INSTALL(){ return m_statiscs_table[ROW_ETC][COL_APP_INSTALL]; }

    //SFG = 1
    public int get_SFG_TOTAL_MAN(){ return m_statiscs_table[ROW_SFG][COL_TOTAL_MAN]; }
    public int get_SFG_NO_RESPONSE(){ return m_statiscs_table[ROW_SFG][COL_NO_RESPONSE]; }
    public int get_SFG_WEB_ACCESS(){ return m_statiscs_table[ROW_SFG][COL_WEB_ACCESS]; }
    public int get_SFG_APP_DOWN(){ return m_statiscs_table[ROW_SFG][COL_APP_DOWN]; }



    public int get_SFG_APP_EXECUTE(){ return m_statiscs_table[ROW_SFG][COL_APP_EXECUTE]; }

    public int get_SFG_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SFG][COL_URL_ACC_COUNT];}
    public int get_SFG_EXEC_COUNT(){ return m_statiscs_table[ROW_SFG][COL_EXEC_COUNT];}
    public int get_SFG_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SFG][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SFG_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SFG][COL_MANY_TIMES_EXEC_MAN];}


    //SHB = 2
    public int get_SHB_TOTAL_MAN(){ return m_statiscs_table[ROW_SHB][COL_TOTAL_MAN]; }
    public int get_SHB_NO_RESPONSE(){ return m_statiscs_table[ROW_SHB][COL_NO_RESPONSE]; }
    public int get_SHB_WEB_ACCESS(){ return m_statiscs_table[ROW_SHB][COL_WEB_ACCESS]; }
    public int get_SHB_APP_DOWN(){ return m_statiscs_table[ROW_SHB][COL_APP_DOWN]; }
    public int get_SHB_APP_EXECUTE(){ return m_statiscs_table[ROW_SHB][COL_APP_EXECUTE]; }

    public int get_SHB_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SHB ][COL_URL_ACC_COUNT];}
    public int get_SHB_EXEC_COUNT(){ return m_statiscs_table[ROW_SHB ][COL_EXEC_COUNT];}
    public int get_SHB_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SHB ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SHB_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SHB ][COL_MANY_TIMES_EXEC_MAN];}



    //SHC = 3
    public int get_SHC_TOTAL_MAN(){ return m_statiscs_table[ROW_SHC][COL_TOTAL_MAN]; }
    public int get_SHC_NO_RESPONSE(){ return m_statiscs_table[ROW_SHC][COL_NO_RESPONSE]; }
    public int get_SHC_WEB_ACCESS(){ return m_statiscs_table[ROW_SHC][COL_WEB_ACCESS]; }
    public int get_SHC_APP_DOWN(){ return m_statiscs_table[ROW_SHC][COL_APP_DOWN]; }
    public int get_SHC_APP_EXECUTE(){ return m_statiscs_table[ROW_SHC][COL_APP_EXECUTE]; }

    public int get_SHC_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SHC ][COL_URL_ACC_COUNT];}
    public int get_SHC_EXEC_COUNT(){ return m_statiscs_table[ROW_SHC ][COL_EXEC_COUNT];}
    public int get_SHC_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SHC ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SHC_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SHC ][COL_MANY_TIMES_EXEC_MAN];}



    //SHI = 4
    public int get_SHI_TOTAL_MAN(){ return m_statiscs_table[ROW_SHI][COL_TOTAL_MAN]; }
    public int get_SHI_NO_RESPONSE(){ return m_statiscs_table[ROW_SHI][COL_NO_RESPONSE]; }
    public int get_SHI_WEB_ACCESS(){ return m_statiscs_table[ROW_SHI][COL_WEB_ACCESS]; }
    public int get_SHI_APP_DOWN(){ return m_statiscs_table[ROW_SHI][COL_APP_DOWN]; }
    public int get_SHI_APP_EXECUTE(){ return m_statiscs_table[ROW_SHI][COL_APP_EXECUTE]; }

    public int get_SHI_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SHI ][COL_URL_ACC_COUNT];}
    public int get_SHI_EXEC_COUNT(){ return m_statiscs_table[ROW_SHI ][COL_EXEC_COUNT];}
    public int get_SHI_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SHI ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SHI_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SHI ][COL_MANY_TIMES_EXEC_MAN];}



    //SHL = 5
    public int get_SHL_TOTAL_MAN(){ return m_statiscs_table[ROW_SHL][COL_TOTAL_MAN]; }
    public int get_SHL_NO_RESPONSE(){ return m_statiscs_table[ROW_SHL][COL_NO_RESPONSE]; }
    public int get_SHL_WEB_ACCESS(){ return m_statiscs_table[ROW_SHL][COL_WEB_ACCESS]; }
    public int get_SHL_APP_DOWN(){ return m_statiscs_table[ROW_SHL][COL_APP_DOWN]; }
    public int get_SHL_APP_EXECUTE(){ return m_statiscs_table[ROW_SHL][COL_APP_EXECUTE]; }

    public int get_SHL_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SHL ][COL_URL_ACC_COUNT];}
    public int get_SHL_EXEC_COUNT(){ return m_statiscs_table[ROW_SHL ][COL_EXEC_COUNT];}
    public int get_SHL_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ ROW_SHL][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SHL_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SHL ][COL_MANY_TIMES_EXEC_MAN];}

    //BNP = 6
    public int get_BNP_TOTAL_MAN(){ return m_statiscs_table[ROW_BNP][COL_TOTAL_MAN]; }
    public int get_BNP_NO_RESPONSE(){ return m_statiscs_table[ROW_BNP][COL_NO_RESPONSE]; }
    public int get_BNP_WEB_ACCESS(){ return m_statiscs_table[ROW_BNP][COL_WEB_ACCESS]; }
    public int get_BNP_APP_DOWN(){ return m_statiscs_table[ROW_BNP][COL_APP_DOWN]; }
    public int get_BNP_APP_EXECUTE(){ return m_statiscs_table[ROW_BNP][COL_APP_EXECUTE]; }

    public int get_BNP_URL_ACC_COUNT(){ return m_statiscs_table[ROW_BNP ][COL_URL_ACC_COUNT];}
    public int get_BNP_EXEC_COUNT(){ return m_statiscs_table[ROW_BNP ][COL_EXEC_COUNT];}
    public int get_BNP_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_BNP ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_BNP_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_BNP ][COL_MANY_TIMES_EXEC_MAN];}

    //CAP = 7
    public int get_CAP_TOTAL_MAN(){ return m_statiscs_table[ROW_CAP][COL_TOTAL_MAN]; }
    public int get_CAP_NO_RESPONSE(){ return m_statiscs_table[ROW_CAP][COL_NO_RESPONSE]; }
    public int get_CAP_WEB_ACCESS(){ return m_statiscs_table[ROW_CAP][COL_WEB_ACCESS]; }
    public int get_CAP_APP_DOWN(){ return m_statiscs_table[ROW_CAP][COL_APP_DOWN]; }
    public int get_CAP_APP_EXECUTE(){ return m_statiscs_table[ROW_CAP][COL_APP_EXECUTE]; }

    public int get_CAP_URL_ACC_COUNT(){ return m_statiscs_table[ROW_CAP ][COL_URL_ACC_COUNT];}
    public int get_CAP_EXEC_COUNT(){ return m_statiscs_table[ROW_CAP ][COL_EXEC_COUNT];}
    public int get_CAP_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ ROW_CAP][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_CAP_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_CAP ][COL_MANY_TIMES_EXEC_MAN];}

    //JJB = 8
    public int get_JJB_TOTAL_MAN(){ return m_statiscs_table[ROW_JJB][COL_TOTAL_MAN]; }
    public int get_JJB_NO_RESPONSE(){ return m_statiscs_table[ROW_JJB][COL_NO_RESPONSE]; }
    public int get_JJB_WEB_ACCESS(){ return m_statiscs_table[ROW_JJB][COL_WEB_ACCESS]; }
    public int get_JJB_APP_DOWN(){ return m_statiscs_table[ROW_JJB][COL_APP_DOWN]; }
    public int get_JJB_APP_EXECUTE(){ return m_statiscs_table[ROW_JJB][COL_APP_EXECUTE]; }

    public int get_JJB_URL_ACC_COUNT(){ return m_statiscs_table[ROW_JJB ][COL_URL_ACC_COUNT];}
    public int get_JJB_EXEC_COUNT(){ return m_statiscs_table[ROW_JJB ][COL_EXEC_COUNT];}
    public int get_JJB_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_JJB ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_JJB_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_JJB ][COL_MANY_TIMES_EXEC_MAN];}

    //SAV = 9
    public int get_SAV_TOTAL_MAN(){ return m_statiscs_table[ROW_SAV][COL_TOTAL_MAN]; }
    public int get_SAV_NO_RESPONSE(){ return m_statiscs_table[ROW_SAV][COL_NO_RESPONSE]; }
    public int get_SAV_WEB_ACCESS(){ return m_statiscs_table[ROW_SAV][COL_WEB_ACCESS]; }
    public int get_SAV_APP_DOWN(){ return m_statiscs_table[ROW_SAV][COL_APP_DOWN]; }
    public int get_SAV_APP_EXECUTE(){ return m_statiscs_table[ROW_SAV][COL_APP_EXECUTE]; }

    public int get_SAV_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SAV ][COL_URL_ACC_COUNT];}
    public int get_SAV_EXEC_COUNT(){ return m_statiscs_table[ROW_SAV ][COL_EXEC_COUNT];}
    public int get_SAV_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ ROW_SAV][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SAV_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SAV ][COL_MANY_TIMES_EXEC_MAN];}

    //SDS = 10
    public int get_SDS_TOTAL_MAN(){ return m_statiscs_table[ROW_SDS][COL_TOTAL_MAN]; }
    public int get_SDS_NO_RESPONSE(){ return m_statiscs_table[ROW_SDS][COL_NO_RESPONSE]; }
    public int get_SDS_WEB_ACCESS(){ return m_statiscs_table[ROW_SDS][COL_WEB_ACCESS]; }
    public int get_SDS_APP_DOWN(){ return m_statiscs_table[ROW_SDS][COL_APP_DOWN]; }
    public int get_SDS_APP_EXECUTE(){ return m_statiscs_table[ROW_SDS][COL_APP_EXECUTE]; }

    public int get_SDS_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SDS ][COL_URL_ACC_COUNT];}
    public int get_SDS_EXEC_COUNT(){ return m_statiscs_table[ ROW_SDS][COL_EXEC_COUNT];}
    public int get_SDS_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SDS ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SDS_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SDS ][COL_MANY_TIMES_EXEC_MAN];}

    //TAS = 11
    public int get_TAS_TOTAL_MAN(){ return m_statiscs_table[ROW_TAS][COL_TOTAL_MAN]; }
    public int get_TAS_NO_RESPONSE(){ return m_statiscs_table[ROW_TAS][COL_NO_RESPONSE]; }
    public int get_TAS_WEB_ACCESS(){ return m_statiscs_table[ROW_TAS][COL_WEB_ACCESS]; }
    public int get_TAS_APP_DOWN(){ return m_statiscs_table[ROW_TAS][COL_APP_DOWN]; }
    public int get_TAS_APP_EXECUTE(){ return m_statiscs_table[ROW_TAS][COL_APP_EXECUTE]; }


    public int get_TAS_URL_ACC_COUNT(){ return m_statiscs_table[ ROW_TAS][COL_URL_ACC_COUNT];}
    public int get_TAS_EXEC_COUNT(){ return m_statiscs_table[ROW_TAS ][COL_EXEC_COUNT];}
    public int get_TAS_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_TAS ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_TAS_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_TAS ][COL_MANY_TIMES_EXEC_MAN];}

    //SCI = 12
    public int get_SCI_TOTAL_MAN(){ return m_statiscs_table[ROW_SCI][COL_TOTAL_MAN]; }
    public int get_SCI_NO_RESPONSE(){ return m_statiscs_table[ROW_SCI][COL_NO_RESPONSE]; }
    public int get_SCI_WEB_ACCESS(){ return m_statiscs_table[ROW_SCI][COL_WEB_ACCESS]; }
    public int get_SCI_APP_DOWN(){ return m_statiscs_table[ROW_SCI][COL_APP_DOWN]; }
    public int get_SCI_APP_EXECUTE(){ return m_statiscs_table[ROW_SCI][COL_APP_EXECUTE]; }

    public int get_SCI_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SCI ][COL_URL_ACC_COUNT];}
    public int get_SCI_EXEC_COUNT(){ return m_statiscs_table[ROW_SCI ][COL_EXEC_COUNT];}
    public int get_SCI_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SCI ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SCI_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SCI ][COL_MANY_TIMES_EXEC_MAN];}


    //SSF = 13
    public int get_SSF_TOTAL_MAN(){ return m_statiscs_table[ROW_SSF][COL_TOTAL_MAN]; }
    public int get_SSF_NO_RESPONSE(){ return m_statiscs_table[ROW_SSF][COL_NO_RESPONSE]; }
    public int get_SSF_WEB_ACCESS(){ return m_statiscs_table[ROW_SSF][COL_WEB_ACCESS]; }
    public int get_SSF_APP_DOWN(){ return m_statiscs_table[ROW_SSF][COL_APP_DOWN]; }
    public int get_SSF_APP_EXECUTE(){ return m_statiscs_table[ROW_SSF][COL_APP_EXECUTE]; }

    public int get_SSF_URL_ACC_COUNT(){ return m_statiscs_table[ROW_SSF ][COL_URL_ACC_COUNT];}
    public int get_SSF_EXEC_COUNT(){ return m_statiscs_table[ROW_SSF ][COL_EXEC_COUNT];}
    public int get_SSF_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_SSF ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_SSF_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_SSF ][COL_MANY_TIMES_EXEC_MAN];}

    //ETC = 14
    public int get_ETC_TOTAL_MAN(){ return m_statiscs_table[ROW_ETC][COL_TOTAL_MAN]; }
    public int get_ETC_NO_RESPONSE(){ return m_statiscs_table[ROW_ETC][COL_NO_RESPONSE]; }
    public int get_ETC_WEB_ACCESS(){ return m_statiscs_table[ROW_ETC][COL_WEB_ACCESS]; }
    public int get_ETC_APP_DOWN(){ return m_statiscs_table[ROW_ETC][COL_APP_DOWN]; }
    public int get_ETC_APP_EXECUTE(){ return m_statiscs_table[ROW_ETC][COL_APP_EXECUTE]; }

    public int get_ETC_URL_ACC_COUNT(){ return m_statiscs_table[ ROW_ETC][COL_URL_ACC_COUNT];}
    public int get_ETC_EXEC_COUNT(){ return m_statiscs_table[ROW_ETC ][COL_EXEC_COUNT];}
    public int get_ETC_MANY_TIMES_URL_ACC_MAN(){ return m_statiscs_table[ROW_ETC ][COL_MANY_TIMES_URL_ACC_MAN];}
    public int get_ETC_MANY_TIMES_EXEC_MAN(){ return m_statiscs_table[ROW_ETC ][COL_MANY_TIMES_EXEC_MAN];}

    //그룹사별 데이터 Setter

    public void set_SFG_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_INSTALL] = a_count; }
    public void set_SHB_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_INSTALL] = a_count; }
    public void set_SHC_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_INSTALL] = a_count; }
    public void set_SHI_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_INSTALL] = a_count; }
    public void set_SHL_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_INSTALL] = a_count; }
    public void set_BNP_APP_INSTALL(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_INSTALL] = a_count; }
    public void set_CAP_APP_INSTALL(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_INSTALL] = a_count; }
    public void set_JJB_APP_INSTALL(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_INSTALL] = a_count; }
    public void set_SAV_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_INSTALL] = a_count; }
    public void set_SDS_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_INSTALL] = a_count; }
    public void set_TAS_APP_INSTALL(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_INSTALL] = a_count; }
    public void set_SCI_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_INSTALL] = a_count; }
    public void set_SSF_APP_INSTALL(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_INSTALL] = a_count; }
    public void set_ETC_APP_INSTALL(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_INSTALL] = a_count; }



    //SFG = 1
    public void set_SFG_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_TOTAL_MAN] = a_count; }
    public void set_SFG_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SFG][COL_NO_RESPONSE] = a_count; }
    public void set_SFG_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SFG][COL_WEB_ACCESS] = a_count; }
    public void set_SFG_APP_DOWN(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_DOWN] = a_count; }
    public void set_SFG_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_EXECUTE] = a_count; }

    public void set_SFG_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_URL_ACC_COUNT] = a_count; }
    public void set_SFG_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_EXEC_COUNT] = a_count; }
    public void set_SFG_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SFG_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SHB = 2
    public void set_SHB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_TOTAL_MAN] = a_count; }
    public void set_SHB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHB][COL_NO_RESPONSE] = a_count; }
    public void set_SHB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHB][COL_WEB_ACCESS] = a_count; }
    public void set_SHB_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_DOWN] = a_count; }
    public void set_SHB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_EXECUTE] = a_count; }

    public void set_SHB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_URL_ACC_COUNT] = a_count; }
    public void set_SHB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_EXEC_COUNT] = a_count; }
    public void set_SHB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SHB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SHC = 3
    public void set_SHC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_TOTAL_MAN] = a_count; }
    public void set_SHC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHC][COL_NO_RESPONSE] = a_count; }
    public void set_SHC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHC][COL_WEB_ACCESS] = a_count; }
    public void set_SHC_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_DOWN] = a_count; }
    public void set_SHC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_EXECUTE] = a_count; }

    public void set_SHC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_URL_ACC_COUNT] = a_count; }
    public void set_SHC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_EXEC_COUNT] = a_count; }
    public void set_SHC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SHC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_EXEC_MAN] = a_count; }

    //SHI = 4
    public void set_SHI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_TOTAL_MAN] = a_count; }
    public void set_SHI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHI][COL_NO_RESPONSE] = a_count; }
    public void set_SHI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHI][COL_WEB_ACCESS] = a_count; }
    public void set_SHI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_DOWN] = a_count; }
    public void set_SHI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_EXECUTE] = a_count; }

    public void set_SHI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_URL_ACC_COUNT] = a_count; }
    public void set_SHI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_EXEC_COUNT] = a_count; }
    public void set_SHI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SHI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SHL = 5
    public void set_SHL_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_TOTAL_MAN] = a_count; }
    public void set_SHL_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHL][COL_NO_RESPONSE] = a_count; }
    public void set_SHL_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHL][COL_WEB_ACCESS] = a_count; }
    public void set_SHL_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_DOWN] = a_count; }
    public void set_SHL_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_EXECUTE] = a_count; }

    public void set_SHL_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_URL_ACC_COUNT] = a_count; }
    public void set_SHL_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_EXEC_COUNT] = a_count; }
    public void set_SHL_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SHL_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //BNP = 6
    public void set_BNP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_TOTAL_MAN] = a_count; }
    public void set_BNP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_BNP][COL_NO_RESPONSE] = a_count; }
    public void set_BNP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_BNP][COL_WEB_ACCESS] = a_count; }
    public void set_BNP_APP_DOWN(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_DOWN] = a_count; }
    public void set_BNP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_EXECUTE] = a_count; }

    public void set_BNP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_URL_ACC_COUNT] = a_count; }
    public void set_BNP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_EXEC_COUNT] = a_count; }
    public void set_BNP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_BNP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //CAP = 7
    public void set_CAP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_TOTAL_MAN] = a_count; }
    public void set_CAP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_CAP][COL_NO_RESPONSE] = a_count; }
    public void set_CAP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_CAP][COL_WEB_ACCESS] = a_count; }
    public void set_CAP_APP_DOWN(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_DOWN] = a_count; }
    public void set_CAP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_EXECUTE] = a_count; }

    public void set_CAP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_URL_ACC_COUNT] = a_count; }
    public void set_CAP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_EXEC_COUNT] = a_count; }
    public void set_CAP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_CAP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //JJB = 8
    public void set_JJB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_TOTAL_MAN] = a_count; }
    public void set_JJB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_JJB][COL_NO_RESPONSE] = a_count; }
    public void set_JJB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_JJB][COL_WEB_ACCESS] = a_count; }
    public void set_JJB_APP_DOWN(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_DOWN] = a_count; }
    public void set_JJB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_EXECUTE] = a_count; }

    public void set_JJB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_URL_ACC_COUNT] = a_count; }
    public void set_JJB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_EXEC_COUNT] = a_count; }
    public void set_JJB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_JJB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SAV = 9
    public void set_SAV_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_TOTAL_MAN] = a_count; }
    public void set_SAV_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SAV][COL_NO_RESPONSE] = a_count; }
    public void set_SAV_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SAV][COL_WEB_ACCESS] = a_count; }
    public void set_SAV_APP_DOWN(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_DOWN] = a_count; }
    public void set_SAV_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_EXECUTE] = a_count; }

    public void set_SAV_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_URL_ACC_COUNT] = a_count; }
    public void set_SAV_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_EXEC_COUNT] = a_count; }
    public void set_SAV_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SAV_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SDS = 10
    public void set_SDS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_TOTAL_MAN] = a_count; }
    public void set_SDS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SDS][COL_NO_RESPONSE] = a_count; }
    public void set_SDS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SDS][COL_WEB_ACCESS] = a_count; }
    public void set_SDS_APP_DOWN(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_DOWN] = a_count; }
    public void set_SDS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_EXECUTE] = a_count; }

    public void set_SDS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_URL_ACC_COUNT] = a_count; }
    public void set_SDS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_EXEC_COUNT] = a_count; }
    public void set_SDS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SDS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //TAS = 11
    public void set_TAS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_TOTAL_MAN] = a_count; }
    public void set_TAS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_TAS][COL_NO_RESPONSE] = a_count; }
    public void set_TAS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_TAS][COL_WEB_ACCESS] = a_count; }
    public void set_TAS_APP_DOWN(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_DOWN] = a_count; }
    public void set_TAS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_EXECUTE] = a_count; }

    public void set_TAS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_URL_ACC_COUNT] = a_count; }
    public void set_TAS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_EXEC_COUNT] = a_count; }
    public void set_TAS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_TAS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_EXEC_MAN] = a_count; }

    //SCI = 12
    public void set_SCI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_TOTAL_MAN] = a_count; }
    public void set_SCI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SCI][COL_NO_RESPONSE] = a_count; }
    public void set_SCI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SCI][COL_WEB_ACCESS] = a_count; }
    public void set_SCI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_DOWN] = a_count; }
    public void set_SCI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_EXECUTE] = a_count; }

    public void set_SCI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_URL_ACC_COUNT] = a_count; }
    public void set_SCI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_EXEC_COUNT] = a_count; }
    public void set_SCI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SCI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //SSF = 13
    public void set_SSF_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_TOTAL_MAN] = a_count; }
    public void set_SSF_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SSF][COL_NO_RESPONSE] = a_count; }
    public void set_SSF_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SSF][COL_WEB_ACCESS] = a_count; }
    public void set_SSF_APP_DOWN(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_DOWN] = a_count; }
    public void set_SSF_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_EXECUTE] = a_count; }

    public void set_SSF_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_URL_ACC_COUNT] = a_count; }
    public void set_SSF_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_EXEC_COUNT] = a_count; }
    public void set_SSF_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_SSF_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //ETC = 14
    public void set_ETC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_TOTAL_MAN] = a_count; }
    public void set_ETC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_ETC][COL_NO_RESPONSE] = a_count; }
    public void set_ETC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_ETC][COL_WEB_ACCESS] = a_count; }
    public void set_ETC_APP_DOWN(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_DOWN] = a_count; }
    public void set_ETC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_EXECUTE] = a_count; }

    public void set_ETC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_URL_ACC_COUNT] = a_count; }
    public void set_ETC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_EXEC_COUNT] = a_count; }
    public void set_ETC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_URL_ACC_MAN] = a_count; }
    public void set_ETC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_EXEC_MAN] = a_count; }


    //add & sub
    //SFG = 1
    public void add_SFG_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_TOTAL_MAN] += a_count; }
    public void add_SFG_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SFG][COL_NO_RESPONSE] += a_count; }
    public void add_SFG_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SFG][COL_WEB_ACCESS] += a_count; }
    public void add_SFG_APP_DOWN(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_DOWN] += a_count; }
    public void add_SFG_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_EXECUTE] += a_count; }

    public void add_SFG_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_URL_ACC_COUNT] += a_count; }
    public void add_SFG_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_EXEC_COUNT] += a_count; }
    public void add_SFG_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SFG_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_EXEC_MAN] += a_count; }



    //SHB = 2
    public void add_SHB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_TOTAL_MAN] += a_count; }
    public void add_SHB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHB][COL_NO_RESPONSE] += a_count; }
    public void add_SHB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHB][COL_WEB_ACCESS] += a_count; }
    public void add_SHB_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_DOWN] += a_count; }
    public void add_SHB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_EXECUTE] += a_count; }

    public void add_SHB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_URL_ACC_COUNT] += a_count; }
    public void add_SHB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_EXEC_COUNT] += a_count; }
    public void add_SHB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SHB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_EXEC_MAN] += a_count; }

    //SHC = 3
    public void add_SHC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_TOTAL_MAN] += a_count; }
    public void add_SHC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHC][COL_NO_RESPONSE] += a_count; }
    public void add_SHC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHC][COL_WEB_ACCESS] += a_count; }
    public void add_SHC_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_DOWN] += a_count; }
    public void add_SHC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_EXECUTE] += a_count; }

    public void add_SHC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_URL_ACC_COUNT] += a_count; }
    public void add_SHC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_EXEC_COUNT] += a_count; }
    public void add_SHC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SHC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //SHI = 4
    public void add_SHI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_TOTAL_MAN] += a_count; }
    public void add_SHI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHI][COL_NO_RESPONSE] += a_count; }
    public void add_SHI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHI][COL_WEB_ACCESS] += a_count; }
    public void add_SHI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_DOWN] += a_count; }
    public void add_SHI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_EXECUTE] += a_count; }

    public void add_SHI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_URL_ACC_COUNT] += a_count; }
    public void add_SHI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_EXEC_COUNT] += a_count; }
    public void add_SHI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SHI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //SHL = 5
    public void add_SHL_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_TOTAL_MAN] += a_count; }
    public void add_SHL_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHL][COL_NO_RESPONSE] += a_count; }
    public void add_SHL_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHL][COL_WEB_ACCESS] += a_count; }
    public void add_SHL_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_DOWN] += a_count; }
    public void add_SHL_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_EXECUTE] += a_count; }


    public void add_SHL_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_URL_ACC_COUNT] += a_count; }
    public void add_SHL_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_EXEC_COUNT] += a_count; }
    public void add_SHL_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SHL_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_EXEC_MAN] += a_count; }

    //BNP = 6
    public void add_BNP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_TOTAL_MAN] += a_count; }
    public void add_BNP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_BNP][COL_NO_RESPONSE] += a_count; }
    public void add_BNP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_BNP][COL_WEB_ACCESS] += a_count; }
    public void add_BNP_APP_DOWN(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_DOWN] += a_count; }
    public void add_BNP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_EXECUTE] += a_count; }

    public void add_BNP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_URL_ACC_COUNT] += a_count; }
    public void add_BNP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_EXEC_COUNT] += a_count; }
    public void add_BNP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_BNP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //CAP = 7
    public void add_CAP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_TOTAL_MAN] += a_count; }
    public void add_CAP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_CAP][COL_NO_RESPONSE] += a_count; }
    public void add_CAP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_CAP][COL_WEB_ACCESS] += a_count; }
    public void add_CAP_APP_DOWN(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_DOWN] += a_count; }
    public void add_CAP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_EXECUTE] += a_count; }

    public void add_CAP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_URL_ACC_COUNT] += a_count; }
    public void add_CAP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_EXEC_COUNT] += a_count; }
    public void add_CAP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_CAP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //JJB = 8
    public void add_JJB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_TOTAL_MAN] += a_count; }
    public void add_JJB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_JJB][COL_NO_RESPONSE] += a_count; }
    public void add_JJB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_JJB][COL_WEB_ACCESS] += a_count; }
    public void add_JJB_APP_DOWN(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_DOWN] += a_count; }
    public void add_JJB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_EXECUTE] += a_count; }

    public void add_JJB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_URL_ACC_COUNT] += a_count; }
    public void add_JJB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_EXEC_COUNT] += a_count; }
    public void add_JJB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_JJB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //SAV = 9
    public void add_SAV_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_TOTAL_MAN] += a_count; }
    public void add_SAV_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SAV][COL_NO_RESPONSE] += a_count; }
    public void add_SAV_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SAV][COL_WEB_ACCESS] += a_count; }
    public void add_SAV_APP_DOWN(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_DOWN] += a_count; }
    public void add_SAV_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_EXECUTE] += a_count; }


    public void add_SAV_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_URL_ACC_COUNT] += a_count; }
    public void add_SAV_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_EXEC_COUNT] += a_count; }
    public void add_SAV_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SAV_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_EXEC_MAN] += a_count; }

    //SDS = 10
    public void add_SDS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_TOTAL_MAN] += a_count; }
    public void add_SDS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SDS][COL_NO_RESPONSE] += a_count; }
    public void add_SDS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SDS][COL_WEB_ACCESS] += a_count; }
    public void add_SDS_APP_DOWN(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_DOWN] += a_count; }
    public void add_SDS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_EXECUTE] += a_count; }

    public void add_SDS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_URL_ACC_COUNT] += a_count; }
    public void add_SDS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_EXEC_COUNT] += a_count; }
    public void add_SDS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SDS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //TAS = 11
    public void add_TAS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_TOTAL_MAN] += a_count; }
    public void add_TAS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_TAS][COL_NO_RESPONSE] += a_count; }
    public void add_TAS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_TAS][COL_WEB_ACCESS] += a_count; }
    public void add_TAS_APP_DOWN(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_DOWN] += a_count; }
    public void add_TAS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_EXECUTE] += a_count; }

    public void add_TAS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_URL_ACC_COUNT] += a_count; }
    public void add_TAS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_EXEC_COUNT] += a_count; }
    public void add_TAS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_TAS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //SCI = 12
    public void add_SCI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_TOTAL_MAN] += a_count; }
    public void add_SCI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SCI][COL_NO_RESPONSE] += a_count; }
    public void add_SCI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SCI][COL_WEB_ACCESS] += a_count; }
    public void add_SCI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_DOWN] += a_count; }
    public void add_SCI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_EXECUTE] += a_count; }


    public void add_SCI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_URL_ACC_COUNT] += a_count; }
    public void add_SCI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_EXEC_COUNT] += a_count; }
    public void add_SCI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SCI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_EXEC_MAN] += a_count; }

    //SSF = 13
    public void add_SSF_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_TOTAL_MAN] += a_count; }
    public void add_SSF_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SSF][COL_NO_RESPONSE] += a_count; }
    public void add_SSF_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SSF][COL_WEB_ACCESS] += a_count; }
    public void add_SSF_APP_DOWN(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_DOWN] += a_count; }
    public void add_SSF_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_EXECUTE] += a_count; }

    public void add_SSF_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_URL_ACC_COUNT] += a_count; }
    public void add_SSF_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_EXEC_COUNT] += a_count; }
    public void add_SSF_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_SSF_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //ETC = 14
    public void add_ETC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_TOTAL_MAN] += a_count; }
    public void add_ETC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_ETC][COL_NO_RESPONSE] += a_count; }
    public void add_ETC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_ETC][COL_WEB_ACCESS] += a_count; }
    public void add_ETC_APP_DOWN(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_DOWN] += a_count; }
    public void add_ETC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_EXECUTE] += a_count; }


    public void add_ETC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_URL_ACC_COUNT] += a_count; }
    public void add_ETC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_EXEC_COUNT] += a_count; }
    public void add_ETC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_URL_ACC_MAN] += a_count; }
    public void add_ETC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_EXEC_MAN] += a_count; }


    //SFG = 1
    public void sub_SFG_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_TOTAL_MAN] -= a_count; }
    public void sub_SFG_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SFG][COL_NO_RESPONSE] -= a_count; }
    public void sub_SFG_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SFG][COL_WEB_ACCESS] -= a_count; }
    public void sub_SFG_APP_DOWN(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_DOWN] -= a_count; }
    public void sub_SFG_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SFG][COL_APP_EXECUTE] -= a_count; }

    public void sub_SFG_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SFG_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SFG][COL_EXEC_COUNT] -= a_count; }
    public void sub_SFG_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SFG_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SFG][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SHB = 2
    public void sub_SHB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_TOTAL_MAN] -= a_count; }
    public void sub_SHB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHB][COL_NO_RESPONSE] -= a_count; }
    public void sub_SHB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHB][COL_WEB_ACCESS] -= a_count; }
    public void sub_SHB_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_DOWN] -= a_count; }
    public void sub_SHB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHB][COL_APP_EXECUTE] -= a_count; }

    public void sub_SHB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SHB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHB][COL_EXEC_COUNT] -= a_count; }
    public void sub_SHB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SHB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHB][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SHC = 3
    public void sub_SHC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_TOTAL_MAN] -= a_count; }
    public void sub_SHC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHC][COL_NO_RESPONSE] -= a_count; }
    public void sub_SHC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHC][COL_WEB_ACCESS] -= a_count; }
    public void sub_SHC_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_DOWN] -= a_count; }
    public void sub_SHC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHC][COL_APP_EXECUTE] -= a_count; }

    public void sub_SHC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SHC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHC][COL_EXEC_COUNT] -= a_count; }
    public void sub_SHC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SHC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHC][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SHI = 4
    public void sub_SHI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_TOTAL_MAN] -= a_count; }
    public void sub_SHI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHI][COL_NO_RESPONSE] -= a_count; }
    public void sub_SHI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHI][COL_WEB_ACCESS] -= a_count; }
    public void sub_SHI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_DOWN] -= a_count; }
    public void sub_SHI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHI][COL_APP_EXECUTE] -= a_count; }

    public void sub_SHI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SHI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHI][COL_EXEC_COUNT] -= a_count; }
    public void sub_SHI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SHI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHI][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SHL = 5
    public void sub_SHL_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_TOTAL_MAN] -= a_count; }
    public void sub_SHL_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SHL][COL_NO_RESPONSE] -= a_count; }
    public void sub_SHL_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SHL][COL_WEB_ACCESS] -= a_count; }
    public void sub_SHL_APP_DOWN(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_DOWN] -= a_count; }
    public void sub_SHL_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SHL][COL_APP_EXECUTE] -= a_count; }

    public void sub_SHL_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SHL_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SHL][COL_EXEC_COUNT] -= a_count; }
    public void sub_SHL_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SHL_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SHL][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //BNP = 6
    public void sub_BNP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_TOTAL_MAN] -= a_count; }
    public void sub_BNP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_BNP][COL_NO_RESPONSE] -= a_count; }
    public void sub_BNP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_BNP][COL_WEB_ACCESS] -= a_count; }
    public void sub_BNP_APP_DOWN(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_DOWN] -= a_count; }
    public void sub_BNP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_BNP][COL_APP_EXECUTE] -= a_count; }

    public void sub_BNP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_BNP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_BNP][COL_EXEC_COUNT] -= a_count; }
    public void sub_BNP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_BNP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_BNP][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //CAP = 7
    public void sub_CAP_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_TOTAL_MAN] -= a_count; }
    public void sub_CAP_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_CAP][COL_NO_RESPONSE] -= a_count; }
    public void sub_CAP_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_CAP][COL_WEB_ACCESS] -= a_count; }
    public void sub_CAP_APP_DOWN(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_DOWN] -= a_count; }
    public void sub_CAP_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_CAP][COL_APP_EXECUTE] -= a_count; }

    public void sub_CAP_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_CAP_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_CAP][COL_EXEC_COUNT] -= a_count; }
    public void sub_CAP_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_CAP_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_CAP][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //JJB = 8
    public void sub_JJB_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_TOTAL_MAN] -= a_count; }
    public void sub_JJB_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_JJB][COL_NO_RESPONSE] -= a_count; }
    public void sub_JJB_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_JJB][COL_WEB_ACCESS] -= a_count; }
    public void sub_JJB_APP_DOWN(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_DOWN] -= a_count; }
    public void sub_JJB_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_JJB][COL_APP_EXECUTE] -= a_count; }

    public void sub_JJB_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_JJB_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_JJB][COL_EXEC_COUNT] -= a_count; }
    public void sub_JJB_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_JJB_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_JJB][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SAV = 9
    public void sub_SAV_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_TOTAL_MAN] -= a_count; }
    public void sub_SAV_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SAV][COL_NO_RESPONSE] -= a_count; }
    public void sub_SAV_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SAV][COL_WEB_ACCESS] -= a_count; }
    public void sub_SAV_APP_DOWN(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_DOWN] -= a_count; }
    public void sub_SAV_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SAV][COL_APP_EXECUTE] -= a_count; }

    public void sub_SAV_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SAV_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SAV][COL_EXEC_COUNT] -= a_count; }
    public void sub_SAV_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SAV_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SAV][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SDS = 10
    public void sub_SDS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_TOTAL_MAN] -= a_count; }
    public void sub_SDS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SDS][COL_NO_RESPONSE] -= a_count; }
    public void sub_SDS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SDS][COL_WEB_ACCESS] -= a_count; }
    public void sub_SDS_APP_DOWN(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_DOWN] -= a_count; }
    public void sub_SDS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SDS][COL_APP_EXECUTE] -= a_count; }

    public void sub_SDS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SDS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SDS][COL_EXEC_COUNT] -= a_count; }
    public void sub_SDS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SDS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SDS][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //TAS = 11
    public void sub_TAS_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_TOTAL_MAN] -= a_count; }
    public void sub_TAS_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_TAS][COL_NO_RESPONSE] -= a_count; }
    public void sub_TAS_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_TAS][COL_WEB_ACCESS] -= a_count; }
    public void sub_TAS_APP_DOWN(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_DOWN] -= a_count; }
    public void sub_TAS_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_TAS][COL_APP_EXECUTE] -= a_count; }

    public void sub_TAS_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_TAS_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_TAS][COL_EXEC_COUNT] -= a_count; }
    public void sub_TAS_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_TAS_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_TAS][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SCI = 12
    public void sub_SCI_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_TOTAL_MAN] -= a_count; }
    public void sub_SCI_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SCI][COL_NO_RESPONSE] -= a_count; }
    public void sub_SCI_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SCI][COL_WEB_ACCESS] -= a_count; }
    public void sub_SCI_APP_DOWN(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_DOWN] -= a_count; }
    public void sub_SCI_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SCI][COL_APP_EXECUTE] -= a_count; }

    public void sub_SCI_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SCI_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SCI][COL_EXEC_COUNT] -= a_count; }
    public void sub_SCI_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SCI_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SCI][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //SSF = 13
    public void sub_SSF_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_TOTAL_MAN] -= a_count; }
    public void sub_SSF_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_SSF][COL_NO_RESPONSE] -= a_count; }
    public void sub_SSF_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_SSF][COL_WEB_ACCESS] -= a_count; }
    public void sub_SSF_APP_DOWN(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_DOWN] -= a_count; }
    public void sub_SSF_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_SSF][COL_APP_EXECUTE] -= a_count; }

    public void sub_SSF_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_SSF_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_SSF][COL_EXEC_COUNT] -= a_count; }
    public void sub_SSF_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_SSF_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_SSF][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


    //ETC = 14
    public void sub_ETC_TOTAL_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_TOTAL_MAN] -= a_count; }
    public void sub_ETC_NO_RESPONSE(int a_count){  m_statiscs_table[ROW_ETC][COL_NO_RESPONSE] -= a_count; }
    public void sub_ETC_WEB_ACCESS(int a_count){  m_statiscs_table[ROW_ETC][COL_WEB_ACCESS] -= a_count; }
    public void sub_ETC_APP_DOWN(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_DOWN] -= a_count; }
    public void sub_ETC_APP_EXECUTE(int a_count){  m_statiscs_table[ROW_ETC][COL_APP_EXECUTE] -= a_count; }

    public void sub_ETC_URL_ACC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_URL_ACC_COUNT] -= a_count; }
    public void sub_ETC_EXEC_COUNT(int a_count){  m_statiscs_table[ROW_ETC][COL_EXEC_COUNT] -= a_count; }
    public void sub_ETC_MANY_TIMES_URL_ACC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_URL_ACC_MAN] -= a_count; }
    public void sub_ETC_MANY_TIMES_EXEC_MAN(int a_count){  m_statiscs_table[ROW_ETC][COL_MANY_TIMES_EXEC_MAN] -= a_count; }


}
