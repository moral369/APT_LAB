package drill.controller;

import drill.ControlledScreen;
import drill.ScreensController;
import drill.ScreensFramework;
import drill.data.TBL_APT;
import drill.utils.DBObject;
import drill.utils.MyLog;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

/**
 * Created by Chan-Ju on 2015-08-24.
 */
public class ScreenAPTtableController implements Initializable, ControlledScreen {

    ScreensController myController;

    static int m_now_seq=0;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        System.out.println(String.format("[Debug]================================================"));
        System.out.println(String.format("[Debug]    ScreenAPTtable : initialize : "));
        System.out.println(String.format("[Debug]================================================"));
        //getAPTTableContent(0);



    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void backButtonAction(ActionEvent event)
 {
     myController.setScreen(ScreensFramework.screen13ID_all_group);
 }
    //@FXML private void goToScreen3(ActionEvent event){ myController.setScreen(ScreensFramework.screen3ID);}


    //TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT) -->
    @FXML private TableView<TBL_APT> tableView;
    @FXML private TextField SEQField;
    @FXML private TextField ACTDATEField;
    @FXML private TextField IPADDRField;
    @FXML private TextField PHTYPEField;
    @FXML private TextField PHNUMField;
    @FXML private TextField ACTDIVField;
    @FXML private TextField ETCField;
    @FXML private TextField GROUPTYPEField;
    @FXML private TextField USERAGENTField;

    //TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT)

    @FXML
    protected void queryAPTTable(ActionEvent event) {
        getAPTTableContent( (m_now_seq+1) );
    }


    @FXML
    protected void addAPTTable(ActionEvent event) {
        ObservableList<TBL_APT> data = tableView.getItems();
        data.add(new TBL_APT(SEQField.getText(),
                ACTDATEField.getText(),
                IPADDRField.getText(),
                PHTYPEField.getText(),
                PHNUMField.getText(),
                ACTDIVField.getText(),
                ETCField.getText(),
                GROUPTYPEField.getText(),
                USERAGENTField.getText()
        ));

        //내용지우기
        SEQField.setText("");
        ACTDATEField.setText("");
        IPADDRField.setText("");
        PHTYPEField.setText("");
        PHNUMField.setText("");
        ACTDIVField.setText("");
        ETCField.setText("");
        GROUPTYPEField.setText("");
        USERAGENTField.setText("");
    }

    private void now_curser_save(int a_seq)
    {
        m_now_seq= (m_now_seq<a_seq ? a_seq:m_now_seq);
        System.out.print(String.format("ScreenAPTtable : now_curser_save : %d", a_seq));
    }
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        MyLog.i("");
        return DBObject.getInstance().getConnection();
    }

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

        TextField lSEQField;
        TextField lACTDATEField;
        TextField lIPADDRField;
        TextField lPHTYPEField;
        TextField lPHNUMField;
        TextField lACTDIVField;
        TextField lETCField;
        TextField lGROUPTYPEField;
        TextField lUSERAGENTField;

        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        try (Connection conn = getConnection())
        {
            //테이블이 존재하지 않으면 생성 낄낄

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            String sql = "select * from TBL_APT where SEQ >= ?";                        // sql 쿼리
            pstmt = conn.prepareStatement(sql);                          // prepareStatement에서 해당 sql을 미리 컴파일한다.
            pstmt.setString(1, String.valueOf(seq));

            ResultSet rs = pstmt.executeQuery();                                        // 쿼리를 실행하고 결과를 ResultSet 객체에 담는다.

            ObservableList<TBL_APT> data = tableView.getItems();

            while(rs.next())
            {                                                        // 결과를 한 행씩 돌아가면서 가져온다.
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



                    System.out.print(lPHNUMString);



                data.add(new TBL_APT(
                        lSEQString,
                        lACTDATEString,
                        lIPADDRString,
                        lPHTYPEString,
                        lPHNUMString,
                        lACTDIVString,
                        lETCString,
                        lGROUPTYPEString,
                        lUSERAGENTString
                ));

                now_curser_save(Integer.parseInt(lSEQString));
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

}