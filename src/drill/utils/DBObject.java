package drill.utils;

/**
 * Created by Chan-Ju on 2015-08-23.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Chan-Ju on 2015-08-23.
 */
public class DBObject {

    private Connection m_db_conn = null;
   // private final String jdbc_url = "jdbc:oracle:thin:@a3security.iptime.org:1521:orcl";
   // private final String db_id = "c##moral"; //데이터베이스 계정
   // private final String db_pwd = "dufwjd"; //데이터베이스 비밀 번호

   // private final String DB_USER_OLD    = "c##moral"; // DB USER명
   // private final String DB_PASSWORD_OLD = "dufwjd"; // 패스워드
   // private final String DB_URL_OLD = "jdbc:oracle:thin:@a3security.iptime.org:1521:orcl";

    //2015. 10. 11
   // private final String DB_USER    = "tocsoft"; // DB USER명
   // private final String DB_PASSWORD = "qwer12#$"; // 패스워드
   // private final String DB_URL = "jdbc:oracle:thin:@shinhanapt.ddns.net:1521:orcl";

    //2015. 10. 13
    private final String DB_USER    = "tocapt"; // DB USER명
    private final String DB_PASSWORD = "qwer12#$"; // 패스워드
    private final String DB_URL = "jdbc:oracle:thin:@shinapt.ddns.net:1521:orcl";


    // private으로 Sinleton클래스의 유일한 인스턴스를 저장하기 위한 정적 변수를 선언
    private volatile static DBObject uniqueInstance;



    // 생성자를 private로 선언했기 때문에 Singleton에서만 클래스를 만들 수 있다.
    private DBObject() throws ClassNotFoundException, SQLException {
        init();
    }

    // 클래스의 인스턴스를 만들어서 리턴해 준다.
    public static DBObject getInstance() throws ClassNotFoundException, SQLException {
        if (uniqueInstance == null) {
            // 이렇게 하면 처음에만 동기화 된다
            synchronized (DBObject.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new DBObject();
                }
            }
        }
        return uniqueInstance;
    }


    public Connection getConnection()
    {
        //System.out.println("[debug] oracle driver handle = "+ m_db_conn);

        try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                m_db_conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                //System.out.println("database driver connection success^^");  //성공적으로 로딩되었음
            } catch (ClassNotFoundException e) {
                System.out.println("[ERROR] oracle driver connection fail - ClassNotFoundException"); //해당 드라이버를 찾을 수 없습니다.
            } catch (SQLException se) {
                System.out.println("[ERROR] oracle driver connection fail - SQLException");
        }

        return m_db_conn;
    }

    private  void init() throws ClassNotFoundException, SQLException {

        connect();
    }

    private void connect() throws ClassNotFoundException, SQLException {
        try {
            Class.forName( "oracle.jdbc.driver.OracleDriver");
            m_db_conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
           // System.out.println( "database driver connection success^^" );  //성공적으로 로딩되었음
        } catch( ClassNotFoundException e ) {
            System.out.println( "[ERROR] oracle driver connection fail - ClassNotFoundException" ); //해당 드라이버를 찾을 수 없습니다.
        } catch( SQLException se) {
            System.out.println( "[ERROR] oracle driver connection fail - SQLException" );
        }
    }

    public double getInfecteeCount() {
        try (Connection con = getConnection()) {
            //테이블이 존재하지 않으면 생성 낄낄
            if (!infecteeSchemaExists(con)) {
                MyLog.i("");
                createInfecteeSchema(con);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 6;
    }


    public boolean infecteeSchemaExists(Connection con) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeQuery("select count(*) from infectee");
            MyLog.i("asdfasdfasdfasdfasdf"+st.executeQuery("select count(*) from infectee"));
        } catch (SQLException e) {
            e.printStackTrace();
            MyLog.i("return false");
            return false;
        }
        MyLog.i("return true");
        return true;
    }

    public void createInfecteeSchema(Connection con) throws SQLException {
        Statement st = null;
        try {
            MyLog.i("createSchema");
            st = con.createStatement();
//            String table = "create table infectee(counting integer(10) NOT NULL)";
//            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean urlClickSchemaExists(Connection con) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeQuery("select count(*) from urlclick");
//            MyLog.i(""+st.executeQuery("select count(*) from urlclick"));
        } catch (SQLException e) {
            e.printStackTrace();
            MyLog.i("return false");
            return false;
        }
        return true;
    }

    public void createUrlClickSchema(Connection con) throws SQLException {
        Statement st = null;
        try {
            MyLog.i("createSchema");
            st = con.createStatement();
//            String table = "create table bank(counting integer(10) NOT NULL)";
//            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean appExecuteSchemaExists(Connection con) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeQuery("select count(*) from info");
//            MyLog.i(""+st.executeQuery("select count(*) from info"));
        } catch (SQLException e) {
            e.printStackTrace();
            MyLog.i("return false");
            return false;
        }
        return true;
    }

    public void createAppExecuteSchema(Connection con) throws SQLException {
        Statement st = null;
        try {
            MyLog.i("createSchema");
            st = con.createStatement();
            String table = "create table info(counting integer(10) NOT NULL)";
            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean appInstallSchemaExists(Connection con) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeQuery("select count(*) from install");
//            MyLog.i(""+st.executeQuery("select count(*) from install"));
        } catch (SQLException e) {
            e.printStackTrace();
            MyLog.i("return false");
            return false;
        }
        return true;
    }

    public void createAppInstallSchema(Connection con) throws SQLException {
        Statement st = null;
        try {
            MyLog.i("createSchema");
            st = con.createStatement();
            String table = "create table install(counting integer(10) NOT NULL)";
            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
