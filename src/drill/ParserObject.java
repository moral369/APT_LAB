package drill;

import drill.data.AccessData;
import drill.data.DefineValue;
import drill.data.StatisticsDataObject;
import drill.utils.DBObject;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Chan-Ju on 2015-09-06.
 */
public class ParserObject {



    static int m_now_seq=0;
    static String m_now_datetime= "1999-01-01 01:01:01";

    private static ParserObject uniqueInstance = null;

    private StatisticsDataObject mStatisticsDataObject;

    private ParserObject()
    {
        init();
    }

    public static ParserObject getInstance(){
        if (uniqueInstance == null)
            uniqueInstance = new ParserObject();
        return uniqueInstance;
    }

    private void init()
    {
        mStatisticsDataObject = StatisticsDataObject.getInstance();

        //getGroupInfoTableContent();
        getGroupsOfParticipants();
        calcul_data(); //�ϴ� �ӽ�

        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                //int i = 0;
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            //getAPTTableContent(m_now_seq);

                            //�ǽð� �α� ����
                            getAPTTableQuery(m_now_datetime);

                            //�׷�纰 ī��Ʈ(����, �ٿ�, ��ġ, ����)
                            getGroupsOfFishingPeople();
                            // System.out.println("[DEBUG][ParserObject] thread run =\n");
                        }
                    });

                    Thread.sleep(3000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
    }


    private void now_curser_save(int a_seq)
    {
        m_now_seq= (m_now_seq<a_seq ? a_seq:m_now_seq);
        System.out.print(String.format("ScreenAPTtable : now_curser_save : %d / ", a_seq));
    }

    private void now_curser_datetime_save(String a_datetime)
    {
        System.out.print(String.format("datetime_save : %s \n", a_datetime));

        int compare = a_datetime.compareTo(m_now_datetime);
        System.out.print(String.format("datetime compare : %d   \n", compare));

        //���ؼ� ū���� �����Ѵ�.
        if(compare > 0)
        {
            System.out.print(String.format("datetime compare : a_datetime > m_now_datetime = %s > %s   \n",  a_datetime, m_now_datetime));
            m_now_datetime = a_datetime;

        }
        else if(compare < 0)
        {
            System.out.print(String.format("datetime compare : a_datetime < m_now_datetime = %s < %s   \n", a_datetime, m_now_datetime));
        }
        else
        {
            System.out.print(String.format("datetime compare : a_datetime = m_now_datetime = %s = %s   \n", a_datetime, m_now_datetime));
        }


         //��Ʈ���� datetime���� �ٲ۴�.
        //DateTime dt = new DateTime(start.getTime());

        //���Ѵ�.

        //�� ����� ���� �����Ѵ�.
        //m_now_datetime = (m_now_seq<a_seq ? a_seq:m_now_seq);

    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        //MyLog.i("");
        return DBObject.getInstance().getConnection();
    }


    ////
    public void calcul_group_info()
    {
        getGroupInfoTableContent();
    }

    //�׷캰 �����ο�
    //Groups of participants
    private int getGroupsOfParticipants()
    {
        String lcustomer_codeString;
        String lcountString;


        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            String sql_query_sentence = "select customer_code , count(*) from VICTIMS_LIST group by customer_code"; // sql ����
            pstmt = conn.prepareStatement(sql_query_sentence);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            //pstmt.setString(1, String.valueOf(seq));
            ResultSet rs = pstmt.executeQuery();              // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.

            while(rs.next()){  // ����� �� �྿ ���ư��鼭 �����´�.

                lcustomer_codeString = rs.getString("customer_code");
                lcountString= rs.getString("count(*)");



                parsing_GroupsOfParticipants(lcustomer_codeString, lcountString);


            }


            //count = rs.getInt(1);

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }// end function - getGroupsOfParticipants()

    /*2015. 10. 12 ���� �ڵ�
    //�׷캰 ���� �ο� ī��Ʈ
    private boolean getGroupsOfFishingPeople()
    {

        String sql_query_sentence = "select (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'urlacc')\n" +
                "        where customer_code = 'SHC') as urlacc\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'down')\n" +
                "        where customer_code = 'SHC') as down\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'inst')\n" +
                "        where customer_code = 'SHC') as inst\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'exec')\n" +
                "        where customer_code = 'SHC') as exec\n" +
                "        from dual"; // sql ����


        String sql_query_sentence_pre = "select (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'urlacc')\n" +
                "        where customer_code =  ? ) as urlacc\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'down')\n" +
                "        where customer_code =  ? ) as down\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'inst')\n" +
                "        where customer_code = ? ) as inst\n" +
                "        , (select count (*)\n" +
                "        from (select distinct a.actdiv\n" +
                "                , a.user_seq\n" +
                "                , b.customer_code\n" +
                "                from tbl_apt a join victims_list b on a.user_seq = B.SEQUENCE_NO\n" +
                "                where a.actdiv = 'exec')\n" +
                "        where customer_code = ? ) as exec\n" +
                "        from dual"; // sql ����


        String lnumber_of_urlacc;
        String lnumber_of_down;
        String lnumber_of_inst;
        String lnumber_of_exec;

        int temp1;
        int temp2;
        int temp3;
        int temp4;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try (Connection conn = getConnection())
        {

            //SFG = 1
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SFG );
            pstmt.setString(2,DefineValue.GROUPTYPE_SFG );
            pstmt.setString(3,DefineValue.GROUPTYPE_SFG );
            pstmt.setString(4,DefineValue.GROUPTYPE_SFG );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SFG urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SFG_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SFG_APP_DOWN(temp2);
            mStatisticsDataObject.set_SFG_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SFG_APP_EXECUTE(temp4);

            //SHB = 2
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SHB );
            pstmt.setString(2,DefineValue.GROUPTYPE_SHB );
            pstmt.setString(3,DefineValue.GROUPTYPE_SHB );
            pstmt.setString(4,DefineValue.GROUPTYPE_SHB );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SHB urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SHB_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SHB_APP_DOWN(temp2);
            mStatisticsDataObject.set_SHB_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SHB_APP_EXECUTE(temp4);

            //SHC = 3
            //SHC
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SHC );
            pstmt.setString(2,DefineValue.GROUPTYPE_SHC );
            pstmt.setString(3,DefineValue.GROUPTYPE_SHC );
            pstmt.setString(4,DefineValue.GROUPTYPE_SHC );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SHC urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SHC_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SHC_APP_DOWN(temp2);
            mStatisticsDataObject.set_SHC_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SHC_APP_EXECUTE(temp4);

            //SHI
            //SHI = 4
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SHI );
            pstmt.setString(2,DefineValue.GROUPTYPE_SHI );
            pstmt.setString(3,DefineValue.GROUPTYPE_SHI );
            pstmt.setString(4,DefineValue.GROUPTYPE_SHI );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SHI urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SHI_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SHI_APP_DOWN(temp2);
            mStatisticsDataObject.set_SHI_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SHI_APP_EXECUTE(temp4);

            //SHL
            //SHL = 5
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SHL );
            pstmt.setString(2,DefineValue.GROUPTYPE_SHL );
            pstmt.setString(3,DefineValue.GROUPTYPE_SHL );
            pstmt.setString(4,DefineValue.GROUPTYPE_SHL );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SHL urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SHL_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SHL_APP_DOWN(temp2);
            mStatisticsDataObject.set_SHL_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SHL_APP_EXECUTE(temp4);

            //BNP
            //BNP = 6
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_BNP );
            pstmt.setString(2,DefineValue.GROUPTYPE_BNP );
            pstmt.setString(3,DefineValue.GROUPTYPE_BNP );
            pstmt.setString(4,DefineValue.GROUPTYPE_BNP );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] BNP urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_BNP_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_BNP_APP_DOWN(temp2);
            mStatisticsDataObject.set_BNP_APP_INSTALL(temp3);
            mStatisticsDataObject.set_BNP_APP_EXECUTE(temp4);

            //CAP
            //CAP = 7
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_CAP );
            pstmt.setString(2,DefineValue.GROUPTYPE_CAP );
            pstmt.setString(3,DefineValue.GROUPTYPE_CAP );
            pstmt.setString(4,DefineValue.GROUPTYPE_CAP );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] CAP urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_CAP_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_CAP_APP_DOWN(temp2);
            mStatisticsDataObject.set_CAP_APP_INSTALL(temp3);
            mStatisticsDataObject.set_CAP_APP_EXECUTE(temp4);

            //JJB
            //JJB = 8
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_JJB );
            pstmt.setString(2,DefineValue.GROUPTYPE_JJB );
            pstmt.setString(3,DefineValue.GROUPTYPE_JJB );
            pstmt.setString(4,DefineValue.GROUPTYPE_JJB );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] JJB urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_JJB_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_JJB_APP_DOWN(temp2);
            mStatisticsDataObject.set_JJB_APP_INSTALL(temp3);
            mStatisticsDataObject.set_JJB_APP_EXECUTE(temp4);

            //SAV
            //SAV = 9
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SAV );
            pstmt.setString(2,DefineValue.GROUPTYPE_SAV );
            pstmt.setString(3,DefineValue.GROUPTYPE_SAV );
            pstmt.setString(4,DefineValue.GROUPTYPE_SAV );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SAV urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SAV_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SAV_APP_DOWN(temp2);
            mStatisticsDataObject.set_SAV_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SAV_APP_EXECUTE(temp4);

            //SDS
            //SDS = 10
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SDS );
            pstmt.setString(2,DefineValue.GROUPTYPE_SDS );
            pstmt.setString(3,DefineValue.GROUPTYPE_SDS );
            pstmt.setString(4,DefineValue.GROUPTYPE_SDS );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SDS urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SDS_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SDS_APP_DOWN(temp2);
            mStatisticsDataObject.set_SDS_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SDS_APP_EXECUTE(temp4);

            //TAS
            //TAS = 11

            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_TAS );
            pstmt.setString(2,DefineValue.GROUPTYPE_TAS );
            pstmt.setString(3,DefineValue.GROUPTYPE_TAS );
            pstmt.setString(4,DefineValue.GROUPTYPE_TAS );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] TAS urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_TAS_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_TAS_APP_DOWN(temp2);
            mStatisticsDataObject.set_TAS_APP_INSTALL(temp3);
            mStatisticsDataObject.set_TAS_APP_EXECUTE(temp4);

            //SCI
            //SCI = 12
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            pstmt.setString(1,DefineValue.GROUPTYPE_SCI );
            pstmt.setString(2,DefineValue.GROUPTYPE_SCI );
            pstmt.setString(3,DefineValue.GROUPTYPE_SCI );
            pstmt.setString(4,DefineValue.GROUPTYPE_SCI );

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            rs.next();

            lnumber_of_urlacc = rs.getString("urlacc");
            lnumber_of_down = rs.getString("down");
            lnumber_of_inst = rs.getString("inst");
            lnumber_of_exec = rs.getString("exec");

            temp1 = Integer.parseInt(lnumber_of_urlacc);
            temp2 = Integer.parseInt(lnumber_of_down);
            temp3 = Integer.parseInt(lnumber_of_inst);
            temp4 = Integer.parseInt(lnumber_of_exec);

            System.out.print( String.format("[DEBUG] SCI urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

            mStatisticsDataObject.set_SCI_WEB_ACCESS(temp1);
            mStatisticsDataObject.set_SCI_APP_DOWN(temp2);
            mStatisticsDataObject.set_SCI_APP_INSTALL(temp3);
            mStatisticsDataObject.set_SCI_APP_EXECUTE(temp4);

            //SSF = 13
            //ETC = 14

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return true;
    } //�׷�纰 ���� ī��Ʈ
    */

    //�׷캰 ���� �ο� ī��Ʈ
    private boolean getGroupsOfFishingPeople()
    {




        //String sql_query_sentence_pre = "select CDCODE, URLACC, DOWN, INST, EXEC from V_CUST_CNT"; // sql ����
        String sql_query_sentence_pre = "select * from V_CUST_CNT"; // sql ����


        String lnumber_of_urlacc; //����
        String lnumber_of_down;   //
        String lnumber_of_inst;   //����
        String lnumber_of_exec;   //���� ȹ��
        String lgroup_cdcode;

        int temp1;
        int temp2;
        int temp3;
        int temp4;

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try (Connection conn = getConnection())
        {

            //SFG = 1
            pstmt = conn.prepareStatement(sql_query_sentence_pre);     // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.

            rs = pstmt.executeQuery(); // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.
            //rs.next();

            while(rs.next())
            {                                                        // ����� �� �྿ ���ư��鼭 �����´�.

                //cdcode, urlacc, down, inst, exec
                lgroup_cdcode  = rs.getString("cdcode");
                lnumber_of_urlacc = rs.getString("urlacc");
                lnumber_of_down = rs.getString("down");
                lnumber_of_inst = rs.getString("inst");
                lnumber_of_exec = rs.getString("exec");


                temp1 = Integer.parseInt(lnumber_of_urlacc);
                temp2 = Integer.parseInt(lnumber_of_down);
                temp3 = Integer.parseInt(lnumber_of_inst);
                temp4 = Integer.parseInt(lnumber_of_exec);

                if(lgroup_cdcode.equals("SFG")){
                    System.out.print( String.format("[DEBUG] SFG urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SFG_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SFG_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SFG_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SFG_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SHB")){
                    System.out.print( String.format("[DEBUG] SHB urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SHB_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SHB_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SHB_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SHB_APP_EXECUTE(temp4);

                }
                else if(lgroup_cdcode.equals("SHC")){
                    System.out.print( String.format("[DEBUG] SHC urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SHC_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SHC_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SHC_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SHC_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SHI")){
                    System.out.print( String.format("[DEBUG] SHI urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SHI_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SHI_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SHI_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SHI_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SHL")){
                    System.out.print( String.format("[DEBUG] SHL urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SHL_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SHL_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SHL_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SHL_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("BNP")){
                    System.out.print( String.format("[DEBUG] BNP urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_BNP_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_BNP_APP_DOWN(temp2);
                    mStatisticsDataObject.set_BNP_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_BNP_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("CAP")){
                    System.out.print( String.format("[DEBUG] CAP urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_CAP_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_CAP_APP_DOWN(temp2);
                    mStatisticsDataObject.set_CAP_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_CAP_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("JJB")){
                    System.out.print( String.format("[DEBUG] JJB urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_JJB_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_JJB_APP_DOWN(temp2);
                    mStatisticsDataObject.set_JJB_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_JJB_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SAV")){
                    System.out.print( String.format("[DEBUG] SAV urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SAV_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SAV_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SAV_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SAV_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SDS")){
                    System.out.print( String.format("[DEBUG] SDS urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SDS_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SDS_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SDS_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SDS_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("TAS")) {
                    System.out.print( String.format("[DEBUG] TAS urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_TAS_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_TAS_APP_DOWN(temp2);
                    mStatisticsDataObject.set_TAS_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_TAS_APP_EXECUTE(temp4);
                }
                else if(lgroup_cdcode.equals("SCI")){
                    System.out.print( String.format("[DEBUG] SCI urlacc - %d , down - %d, inst - %d, exec - %d \n", temp1, temp2, temp3, temp4));

                    mStatisticsDataObject.set_SCI_WEB_ACCESS(temp1);
                    mStatisticsDataObject.set_SCI_APP_DOWN(temp2);
                    mStatisticsDataObject.set_SCI_APP_INSTALL(temp3);
                    mStatisticsDataObject.set_SCI_APP_EXECUTE(temp4);
                }
            }//while(rs.next())

            //SSF = 13
            //ETC = 14

            calcul_data();

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return true;
    } //�׷�纰 ���� ī��Ʈ

    private void parsing_GroupsOfParticipants( String acustomer_codeString,  String acountString)
    {

        int li_try_persons = Integer.parseInt(acountString);

        if(acustomer_codeString.equals("SFG")){
            mStatisticsDataObject.set_SFG_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SHB")){
            mStatisticsDataObject.set_SHB_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SHC")){
            mStatisticsDataObject.set_SHC_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SHI")){
            mStatisticsDataObject.set_SHI_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SHL")){
            mStatisticsDataObject.set_SHL_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("BNP")){
            mStatisticsDataObject.set_BNP_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("CAP")){
            mStatisticsDataObject.set_CAP_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("JJB")){
            mStatisticsDataObject.set_JJB_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SAV")){
            mStatisticsDataObject.set_SAV_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SDS")){
            mStatisticsDataObject.set_SDS_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("TAS")) {
            mStatisticsDataObject.set_TAS_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("SCI")){
            mStatisticsDataObject.set_SCI_TOTAL_MAN(li_try_persons);
        }
        else if(acustomer_codeString.equals("ETC")){
            mStatisticsDataObject.set_ETC_TOTAL_MAN(li_try_persons);
        }
    }//end function - parsing_GroupsOfParticipants


    private int getGroupInfoTableContent()
    {
        String lSEQString;
        String lGROUP_CODEString;
        String lTRY_PERSONSString;


        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            String sql = "select * from group_info";                        // sql ����
            pstmt = conn.prepareStatement(sql);                          // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            //pstmt.setString(1, String.valueOf(seq));
            ResultSet rs = pstmt.executeQuery();                                        // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.


            while(rs.next()) {                                                        // ����� �� �྿ ���ư��鼭 �����´�.
//GROUP_INFO (SEQ , GROUP_CODE, TRY_PERSONS)

                lSEQString = rs.getString("SEQ");
                lGROUP_CODEString = rs.getString("GROUP_CODE");
                lTRY_PERSONSString= rs.getString("TRY_PERSONS");



                parsing_group_info_data(lSEQString, lGROUP_CODEString, lTRY_PERSONSString);


            }


            //count = rs.getInt(1);

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }// end function - getGroupInfoTableContent()

    /////
/*
    select * from (
            select seq
            , actdate
            , customer_code
            , person_name
            , phone_number
            , (select codename
            from tbl_syscode
                    where ap.actdiv = cdcode) as actstr
    from victims_list vl join TBL_APT ap on vl.sequence_no = ap.user_seq
    union all
    select seq, actdate, customer_code, person_name, phone_number, actstr
    from victims_list vl join tbl_aptdetail ad on vl.phone_number = ad.phnum) where actdate > to_date('2015-09-02 02:00:00','yyyy-mm-dd hh24:mi:ss') order by actdate;
*/
    ///////////////
    private int getAPTTableQuery(String a_datetime)
    {
        String lSEQString;
        String lACTDATEString;
        String lCUSTOMER_CODEString;
        String lPERSON_NAMEtring;
        String lPHNUMString;
        String lACTSTRString;


        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            // String sql = "select * from TBL_APT where SEQ > ? order by seq desc";                        // sql ����
            //String sql = "select * from TBL_APT where SEQ > ? order by seq ";                        // sql ����

            /* 2015 10 12 �� ����

            String sql_prepare = "select * from (\n" +
                    "            select seq\n" +
                    "            , actdate\n" +
                    "            , customer_code\n" +
                    "            , person_name\n" +
                    "            , phone_number\n" +
                    "            , (select codename\n" +
                    "            from tbl_syscode\n" +
                    "                    where ap.actdiv = cdcode) as actstr\n" +
                    "    from victims_list vl join TBL_APT ap on vl.sequence_no = ap.user_seq\n" +
                    "    union all\n" +
                    "    select seq, actdate, customer_code, person_name, phone_number, actstr\n" +
                    "    from victims_list vl join tbl_aptdetail ad on vl.phone_number = ad.phnum) " +
                    "    where actdate > to_date(?,'yyyy-mm-dd hh24:mi:ss') order by actdate\n";
*/
            //2015. 10. 13 ����
            String sql_prepare = "select * from V_REAL_LOG where actdate > to_date(?,'yyyy-mm-dd hh24:mi:ss') order by actdate "; //--�ǽð� �α�
            //String sql = "select customer_code , count (*) from VICTIMS_LIST group by customer_code" ;

            pstmt = conn.prepareStatement(sql_prepare);                          // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            pstmt.setString(1, m_now_datetime);

            ResultSet rs = pstmt.executeQuery();                                        // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.



            while(rs.next()) {                                                        // ����� �� �྿ ���ư��鼭 �����´�.
//TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT)

                lSEQString = rs.getString("seq");
                lACTDATEString = rs.getString("actdate");
                lCUSTOMER_CODEString = rs.getString("customer_code");
                lPERSON_NAMEtring = rs.getString("person_name");
                lPHNUMString = rs.getString("phone_number");
                lACTSTRString = rs.getString("actstr");

                parsing_apt_table_log
                (
                      lSEQString,
                      lACTDATEString,
                      lCUSTOMER_CODEString,
                      lPERSON_NAMEtring,
                      lPHNUMString,
                      lACTSTRString
                );

                now_curser_datetime_save(lACTDATEString);
            }


            //count = rs.getInt(1);

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    private void parsing_apt_table_log (
                    String aSEQString,
                    String aACTDATEString,
                    String aCUSTOMER_CODEString,
                    String aPERSON_NAMEtring,
                    String aPHNUMString,
                    String aACTSTRString
                    )
    {

       // --------------
        int l_seq;
        String l_group_name;
        String l_access_log;
        AccessData aAD4all = new AccessData();
        AccessData aAD4each = new AccessData();

        l_seq = Integer.parseInt(aSEQString);
        l_group_name = aCUSTOMER_CODEString;

        //ObservableList<String> names = FXCollections.observableArrayList();
        //names.add("[1][2015.08.25 14:23:51][10.232.3.5][SHB][Android] WebPage Access");
        //names.add("[2][2015.08.25 14:30:42][10.232.3.5][SHB][Android] App Download Try");
        //names.add("[3][2015.08.25 14:23:51][192.168.0.1][JJB][IPhone] WebPage Access");
        //names.add("[4][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Executin");
        //names.add("[5][2015.08.25 14:23:51][10.232.3.5][SHB][Android] Enter the Password fails");
        //names.add("[7][2015.08.25 14:23:51][192.168.0.1][SCI][PC-Win7] WebPage Access");
        //names.add("[8][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Termination");
        //listviewtemp.setItems(names);


        //����, ��¥, ����IP, �׷��, ���Ÿ��, �޽���(�̰� ���� ��Ʈ�� �� �ٸ缭)
        l_access_log = '['+ aSEQString +"]["+aACTDATEString+"]["+aCUSTOMER_CODEString +"]["+aPERSON_NAMEtring +"]["+aPHNUMString+"]["+aACTSTRString +']';

        //ACTDIV �� �����ؼ� ��Ʈ�� �������� �ٹδ�.

        //l_access_log = l_access_log+ "WebPage Access";
        //l_access_log = l_access_log+ aUSERAGENTString;

        //���׷�� ȭ���� ���ؼ�
        aAD4all.setM_seq(l_seq);
        aAD4all.setM_group_name(l_group_name);
        aAD4all.setM_access_log(l_access_log);

        //�׷�纰 ȭ���� ���ؼ�
        aAD4each.setM_seq(l_seq);
        aAD4each.setM_group_name(l_group_name);
        aAD4each.setM_access_log(l_access_log);

        //System.out.println("parsing_apt_data mStatisticsDataObject.setAccessData(aAD);");

        calcul_data(); //�ϴ� �ӽ�

        //�׷�� ����
        //group_distribute(aSEQString, aACTDATEString,  aIPADDRString, aPHTYPEString, aPHNUMString, aACTDIVString, aETCString, aGROUPTYPEString, aUSERAGENTString);

        mStatisticsDataObject.setAccessData4all(aAD4all);

        //////////////////

        //�׷�� ���� ���ڿ�
        if(l_group_name.equals(DefineValue.GROUPTYPE_SFG)){ mStatisticsDataObject.setAccessData4sfg(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SHB)){ mStatisticsDataObject.setAccessData4shb(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SHC)){ mStatisticsDataObject.setAccessData4shc(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SHI)){ mStatisticsDataObject.setAccessData4shi(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SHL)){ mStatisticsDataObject.setAccessData4shl(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_BNP)){ mStatisticsDataObject.setAccessData4bnp(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_CAP)){ mStatisticsDataObject.setAccessData4cap(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_JJB)){ mStatisticsDataObject.setAccessData4jjb(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SAV)){ mStatisticsDataObject.setAccessData4sav(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SDS)){ mStatisticsDataObject.setAccessData4sds(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_TAS)){ mStatisticsDataObject.setAccessData4tas(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_SCI)){ mStatisticsDataObject.setAccessData4sci(aAD4each); }
        else if(l_group_name.equals(DefineValue.GROUPTYPE_ETC)){ mStatisticsDataObject.setAccessData4etc(aAD4each); }
        //----------------

    }
    ////////////////

    private int getAPTTableContent(int seq)
    {
        String lSEQString;
        String lACTDATEString;
        String lIPADDRString;
        String lPHTYPEString;
        String lPHNUMString;
        String lACTDIVString;
        String lETCString;
        String lGROUPTYPEString;
        String lUSERAGENTString;

        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

           // String sql = "select * from TBL_APT where SEQ > ? order by seq desc";                        // sql ����
           String sql = "select * from TBL_APT where SEQ > ? order by seq ";                        // sql ����

            //String sql = "select customer_code , count (*) from VICTIMS_LIST group by customer_code" ;

            pstmt = conn.prepareStatement(sql);                          // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            pstmt.setString(1, String.valueOf(seq));

            ResultSet rs = pstmt.executeQuery();                                        // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.



            while(rs.next()) {                                                        // ����� �� �྿ ���ư��鼭 �����´�.
//TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT)

                lSEQString = rs.getString("SEQ");
                lACTDATEString = rs.getString("ACTDATE");
                lIPADDRString= rs.getString("IPADDR");
                lPHTYPEString= rs.getString("PHTYPE");
                lPHNUMString= rs.getString("PHNUM");
                lACTDIVString= rs.getString("ACTDIV");
                lETCString= rs.getString("ETC");
                lGROUPTYPEString= rs.getString("GROUPTYPE");
                lUSERAGENTString= rs.getString("USERAGENT");


                parsing_apt_data(
                        lSEQString,
                        lACTDATEString,
                        lIPADDRString,
                        lPHTYPEString,
                        lPHNUMString,
                        lACTDIVString,
                        lETCString,
                        lGROUPTYPEString,
                        lUSERAGENTString
                );

                now_curser_save(Integer.parseInt(lSEQString));
                now_curser_datetime_save(lACTDATEString);
            }


            //count = rs.getInt(1);

            if(conn!=null)
            {
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    //
    private void parsing_group_info_data( String aSEQString, String aGROUP_CODEString,  String aTRY_PERSONSString)
    {

        int li_try_persons = Integer.parseInt(aTRY_PERSONSString);

        if(aGROUP_CODEString.equals("SFG")){
            mStatisticsDataObject.set_SFG_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SHB")){
            mStatisticsDataObject.set_SHB_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SHC")){
            mStatisticsDataObject.set_SHC_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SHI")){
            mStatisticsDataObject.set_SHI_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SHL")){
            mStatisticsDataObject.set_SHL_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("BNP")){
            mStatisticsDataObject.set_BNP_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("CAP")){
            mStatisticsDataObject.set_CAP_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("JJB")){
            mStatisticsDataObject.set_JJB_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SAV")){
            mStatisticsDataObject.set_SAV_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SDS")){
            mStatisticsDataObject.set_SDS_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("TAS")) {
            mStatisticsDataObject.set_TAS_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("SCI")){
            mStatisticsDataObject.set_SCI_TOTAL_MAN(li_try_persons);
        }
        else if(aGROUP_CODEString.equals("ETC")){
            mStatisticsDataObject.set_ETC_TOTAL_MAN(li_try_persons);
        }
    }


    //���� �𸣰ڴ�, ������ ���� �׳� �ϴ� �Ǳ⸸ �ض� ,
    private void parsing_apt_data( String aSEQString, String aACTDATEString,  String aIPADDRString,
                                   String aPHTYPEString, String aPHNUMString, String aACTDIVString,
                                   String aETCString, String aGROUPTYPEString, String aUSERAGENTString

    )
    {

        int l_seq;
        String l_group_name;
        String l_access_log;
        AccessData aAD = new AccessData();

        l_seq = Integer.parseInt(aSEQString);
        l_group_name = aGROUPTYPEString;

        //ObservableList<String> names = FXCollections.observableArrayList();
        //names.add("[1][2015.08.25 14:23:51][10.232.3.5][SHB][Android] WebPage Access");
        //names.add("[2][2015.08.25 14:30:42][10.232.3.5][SHB][Android] App Download Try");
        //names.add("[3][2015.08.25 14:23:51][192.168.0.1][JJB][IPhone] WebPage Access");
        //names.add("[4][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Executin");
        //names.add("[5][2015.08.25 14:23:51][10.232.3.5][SHB][Android] Enter the Password fails");
        //names.add("[7][2015.08.25 14:23:51][192.168.0.1][SCI][PC-Win7] WebPage Access");
        //names.add("[8][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Termination");
        //listviewtemp.setItems(names);

        //����, ��¥, ����IP, �׷��, ���Ÿ��, �޽���(�̰� ���� ��Ʈ�� �� �ٸ缭)
        l_access_log = '['+ aSEQString +"]["+aACTDATEString+"]["+aIPADDRString +"]["+aGROUPTYPEString +"]["+aPHNUMString+"]["+aPHTYPEString +']';

        //ACTDIV �� �����ؼ� ��Ʈ�� �������� �ٹδ�.

        //l_access_log = l_access_log+ "WebPage Access";
        l_access_log = l_access_log+ aUSERAGENTString;

        aAD.setM_seq(l_seq);
        aAD.setM_group_name(l_group_name);
        aAD.setM_access_log(l_access_log);

        //System.out.println("parsing_apt_data mStatisticsDataObject.setAccessData(aAD);");

        calcul_data(); //�ϴ� �ӽ�

        //�׷�� ����
        //group_distribute(aSEQString, aACTDATEString,  aIPADDRString, aPHTYPEString, aPHNUMString, aACTDIVString, aETCString, aGROUPTYPEString, aUSERAGENTString);

        mStatisticsDataObject.setAccessData4all(aAD);
    }

    //�׷� �з�
    private void group_distribute(String aSEQString, String aACTDATEString,  String aIPADDRString,
                                  String aPHTYPEString, String aPHNUMString, String aACTDIVString,
                                  String aETCString, String aGROUPTYPEString, String aUSERAGENTString
    )
    {
        //1. �ߺ����� ���� �ϴ� �׳� �Ѵ�.
        //���ֿ� �ߺ����� ����.

        //�������� �׷�纰�� ī��Ʈ �ϴ� ������ �ָ� �װ��� �����ϸ� �ɵ�
      //  System.out.println("[DEBUG] group_distribute ");
      //  System.out.println("ACTDIV is " +aACTDIVString);

      //  System.out.println("GROUP TYPE String is " +aGROUPTYPEString);

        if(aGROUPTYPEString == null)
        {
        //    System.out.println("GROUP TYPE String is NULL");

            return;
        }

        if(aGROUPTYPEString.equals("SFG"))
        {
         //   System.out.println("[DEBUG] group_distribute SFG");
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
           //     System.out.println("[DEBUG] group_distribute SFG ACTDIV_URLACC");
                mStatisticsDataObject.sub_SFG_NO_RESPONSE(1);
                mStatisticsDataObject.add_SFG_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
            //    System.out.println("[DEBUG] group_distribute SFG ACTDIV_INSTALL");
                mStatisticsDataObject.add_SFG_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
             //   System.out.println("[DEBUG] group_distribute SFG ACTDIV_EXEC");
                mStatisticsDataObject.add_SFG_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SHB"))
        {
            //SHB = 2
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SHB_NO_RESPONSE(1);
                mStatisticsDataObject.add_SHB_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SHB_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SHB_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SHC"))
        {

            //SHC = 3
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SHC_NO_RESPONSE(1);
                mStatisticsDataObject.add_SHC_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SHC_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SHC_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SHI"))
        {

            //SHI = 4
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SHI_NO_RESPONSE(1);
                mStatisticsDataObject.add_SHI_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SHI_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SHI_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SHL"))
        {

            //SHL = 5
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SHL_NO_RESPONSE(1);
                mStatisticsDataObject.add_SHL_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SHL_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SHL_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("BNP"))
        {

            //BNP = 6
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_BNP_NO_RESPONSE(1);
                mStatisticsDataObject.add_BNP_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_BNP_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_BNP_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("CAP"))
        {
            //CAP = 7

            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {

                mStatisticsDataObject.sub_CAP_NO_RESPONSE(1);
                mStatisticsDataObject.add_CAP_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_CAP_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_CAP_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("JJB"))
        {

            //JJB = 8
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_JJB_NO_RESPONSE(1);
                mStatisticsDataObject.add_JJB_WEB_ACCESS(1);
            }
            else  if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_JJB_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_JJB_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SAV"))
        {

            //SAV = 9
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SAV_NO_RESPONSE(1);
                mStatisticsDataObject.add_SAV_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SAV_APP_DOWN(1);
            }
            else  if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SAV_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SDS"))
        {

            //SDS = 10
            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SDS_NO_RESPONSE(1);
                mStatisticsDataObject.add_SDS_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SDS_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SDS_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("TAS"))
        {

            //TAS = 11

            if(aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_TAS_NO_RESPONSE(1);
                mStatisticsDataObject.add_TAS_WEB_ACCESS(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_TAS_APP_DOWN(1);
            }
            else if(aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_TAS_APP_EXECUTE(1);
            }
        }
        else if(aGROUPTYPEString.equals("SCI"))
        {

            //SCI = 12
            if (aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_SCI_NO_RESPONSE(1);
                mStatisticsDataObject.add_SCI_WEB_ACCESS(1);
            }
            else if (aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_SCI_APP_DOWN(1);
            }
            else if (aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_SCI_APP_EXECUTE(1);
            }
        }
        else
        {
            System.out.println("[DEBUG] group_distribute  ETC");
            //if(aGROUPTYPEString.equals("ETC"))
            //ETC = 14
            if (aACTDIVString.equals(DefineValue.ACTDIV_URLACC))
            {
                mStatisticsDataObject.sub_ETC_NO_RESPONSE(1);
                mStatisticsDataObject.add_ETC_WEB_ACCESS(1);
            }
            else if (aACTDIVString.equals(DefineValue.ACTDIV_INSTALL))
            {
                mStatisticsDataObject.add_ETC_APP_DOWN(1);
            }
            else if (aACTDIVString.equals(DefineValue.ACTDIV_EXEC))
            {
                mStatisticsDataObject.add_ETC_APP_EXECUTE(1);
            }
        }
    }

    private void calcul_data()
    {
        //�ϴ� �ӽ�
        System.out.print("private void calcul_data()");

        int lisfg_tm = mStatisticsDataObject.get_SFG_TOTAL_MAN();
        int lishb_tm = mStatisticsDataObject.get_SHB_TOTAL_MAN();
        int lishc_tm = mStatisticsDataObject.get_SHC_TOTAL_MAN();
        int lishi_tm = mStatisticsDataObject.get_SHI_TOTAL_MAN();
        int lishl_tm = mStatisticsDataObject.get_SHL_TOTAL_MAN();
        int libnp_tm = mStatisticsDataObject.get_BNP_TOTAL_MAN();
        int licap_tm = mStatisticsDataObject.get_CAP_TOTAL_MAN();
        int lijjb_tm = mStatisticsDataObject.get_JJB_TOTAL_MAN();
        int lisav_tm = mStatisticsDataObject.get_SAV_TOTAL_MAN();
        int lisds_tm = mStatisticsDataObject.get_SDS_TOTAL_MAN();
        int litas_tm = mStatisticsDataObject.get_TAS_TOTAL_MAN();
        int lisci_tm = mStatisticsDataObject.get_SCI_TOTAL_MAN();

        int lisfg_urlacc = mStatisticsDataObject.get_SFG_WEB_ACCESS();
        int lishb_urlacc = mStatisticsDataObject.get_SHB_WEB_ACCESS();
        int lishc_urlacc = mStatisticsDataObject.get_SHC_WEB_ACCESS();
        int lishi_urlacc = mStatisticsDataObject.get_SHI_WEB_ACCESS();
        int lishl_urlacc = mStatisticsDataObject.get_SHL_WEB_ACCESS();
        int libnp_urlacc = mStatisticsDataObject.get_BNP_WEB_ACCESS();
        int licap_urlacc = mStatisticsDataObject.get_CAP_WEB_ACCESS();
        int lijjb_urlacc = mStatisticsDataObject.get_JJB_WEB_ACCESS();
        int lisav_urlacc = mStatisticsDataObject.get_SAV_WEB_ACCESS();
        int lisds_urlacc = mStatisticsDataObject.get_SDS_WEB_ACCESS();
        int litas_urlacc = mStatisticsDataObject.get_TAS_WEB_ACCESS();
        int lisci_urlacc = mStatisticsDataObject.get_SCI_WEB_ACCESS();


        int lisfg = lisfg_tm - lisfg_urlacc;
        int lishb = lishb_tm - lishb_urlacc;
        int lishc = lishc_tm - lishc_urlacc;
        int lishl = lishi_tm - lishi_urlacc;
        int lishi = lishl_tm - lishl_urlacc;
        int libnp = libnp_tm - libnp_urlacc;
        int licap = licap_tm - licap_urlacc;
        int lijjb = lijjb_tm - lijjb_urlacc;
        int lisav = lisav_tm - lisav_urlacc;
        int lisds = lisds_tm - lisds_urlacc;
        int litas = litas_tm - litas_urlacc;
        int lisci = lisci_tm - lisci_urlacc;



        mStatisticsDataObject.set_SFG_NO_RESPONSE(lisfg);
        mStatisticsDataObject.set_SHB_NO_RESPONSE(lishb);
        mStatisticsDataObject.set_SHC_NO_RESPONSE(lishc);
        mStatisticsDataObject.set_SHI_NO_RESPONSE(lishl);
        mStatisticsDataObject.set_SHL_NO_RESPONSE(lishi);
        mStatisticsDataObject.set_BNP_NO_RESPONSE(libnp);
        mStatisticsDataObject.set_CAP_NO_RESPONSE(licap);
        mStatisticsDataObject.set_JJB_NO_RESPONSE(lijjb);
        mStatisticsDataObject.set_SAV_NO_RESPONSE(lisav);
        mStatisticsDataObject.set_SDS_NO_RESPONSE(lisds);
        mStatisticsDataObject.set_TAS_NO_RESPONSE(litas);
        mStatisticsDataObject.set_SCI_NO_RESPONSE(lisci);

    }
}
