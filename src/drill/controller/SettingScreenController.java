package drill.controller;

import drill.ControlledScreen;
import drill.ParserObject;
import drill.ScreensController;
import drill.ScreensFramework;
import drill.data.StatisticsDataObject;
import drill.utils.DBObject;
import drill.utils.MyLog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Chan-Ju on 2015-09-06.
 */
public class SettingScreenController implements Initializable, ControlledScreen {

    /////////////////

    @FXML private TextField idSFGField;
    @FXML private TextField idSHBField;
    @FXML private TextField idSHCField;
    @FXML private TextField idSHIField;
    @FXML private TextField idSHLField;
    @FXML private TextField idBNPField;
    @FXML private TextField idCAPField;
    @FXML private TextField idJJBField;
    @FXML private TextField idSAVField;
    @FXML private TextField idSDSField;
    @FXML private TextField idTASField;
    @FXML private TextField idSCIField;
    @FXML private TextField idETCField;

    private ParserObject mParserObject;
    private StatisticsDataObject mStatisticsDataObject;

    ScreensController myController;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mParserObject = ParserObject.getInstance();
        mStatisticsDataObject = StatisticsDataObject.getInstance();

    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        MyLog.i("");
        return DBObject.getInstance().getConnection();
    }

    private void showHowManyParticipants()
    {
        int lSFG_pp=0;
        int lSHB_pp=0;
        int lSHC_pp=0;
        int lSHI_pp=0;
        int lSHL_pp=0;
        int lBNP_pp=0;
        int lCAP_pp=0;
        int lJJB_pp=0;
        int lSAV_pp=0;
        int lSDS_pp=0;
        int lTAS_pp=0;
        int lSCI_pp=0;
        int lETC_pp=0;

        mParserObject.calcul_group_info();


        lSFG_pp= mStatisticsDataObject.get_SFG_TOTAL_MAN();
        lSHB_pp= mStatisticsDataObject.get_SHB_TOTAL_MAN();
        lSHC_pp= mStatisticsDataObject.get_SHC_TOTAL_MAN();
        lSHI_pp= mStatisticsDataObject.get_SHI_TOTAL_MAN();
        lSHL_pp= mStatisticsDataObject.get_SHL_TOTAL_MAN();
        lBNP_pp= mStatisticsDataObject.get_BNP_TOTAL_MAN();
        lCAP_pp= mStatisticsDataObject.get_CAP_TOTAL_MAN();
        lJJB_pp= mStatisticsDataObject.get_JJB_TOTAL_MAN();
        lSAV_pp= mStatisticsDataObject.get_SAV_TOTAL_MAN();
        lSDS_pp= mStatisticsDataObject.get_SDS_TOTAL_MAN();
        lTAS_pp= mStatisticsDataObject.get_TAS_TOTAL_MAN();
        lSCI_pp= mStatisticsDataObject.get_SCI_TOTAL_MAN();
        lETC_pp= mStatisticsDataObject.get_ETC_TOTAL_MAN();


        System.out.println(String.format("mStatisticsDataObject.get_ETC_TOTAL_MAN() %d",lETC_pp));
        //내용지우기
        idSFGField.setText(String.format("%d",lSFG_pp));
        idSHBField.setText(String.format("%d",lSHB_pp));
        idSHCField.setText(String.format("%d",lSHC_pp));
        idSHIField.setText(String.format("%d",lSHI_pp));
        idSHLField.setText(String.format("%d",lSHL_pp));
        idBNPField.setText(String.format("%d",lBNP_pp));
        idCAPField.setText(String.format("%d",lCAP_pp));
        idJJBField.setText(String.format("%d",lJJB_pp));
        idSAVField.setText(String.format("%d",lSAV_pp));
        idSDSField.setText(String.format("%d",lSDS_pp));
        idTASField.setText(String.format("%d",lTAS_pp));
        idSCIField.setText(String.format("%d",lSCI_pp));
        idETCField.setText(String.format("%d",lETC_pp));


    }





    private int setGroupInfoTableContent()
    {
        String lSEQString;
        String lGROUP_CODEString;
        String lTRY_PERSONSString;

        int lpersons_SFG = 0;
        int lpersons_SHB = 0;
        int lpersons_SHC = 0;
        int lpersons_SHI = 0;
        int lpersons_SHL = 0;
        int lpersons_BNP = 0;
        int lpersons_CAP = 0;
        int lpersons_JJB = 0;
        int lpersons_SAV = 0;
        int lpersons_SDS = 0;
        int lpersons_TAS = 0;
        int lpersons_SCI = 0;
        int lpersons_SSF = 0;
        int lpersons_ETC = 0;

        lpersons_SFG = Integer.parseInt(idSFGField.getText());
        lpersons_SHB = Integer.parseInt(idSHBField.getText());
        lpersons_SHC = Integer.parseInt(idSHCField.getText());
        lpersons_SHI = Integer.parseInt(idSHIField.getText());
        lpersons_SHL = Integer.parseInt(idSHLField.getText());
        lpersons_BNP = Integer.parseInt(idBNPField.getText());
        lpersons_CAP = Integer.parseInt(idCAPField.getText());
        lpersons_JJB = Integer.parseInt(idJJBField.getText());
        lpersons_SAV = Integer.parseInt(idSAVField.getText());
        lpersons_SDS = Integer.parseInt(idSDSField.getText());
        lpersons_TAS = Integer.parseInt(idTASField.getText());
        lpersons_SCI = Integer.parseInt(idSCIField.getText());
        lpersons_ETC = Integer.parseInt(idETCField.getText());

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


            String sql_group_sfg = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SFG'";
            String sql_group_shb = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SHB'";
            String sql_group_shc = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SHC'";
            String sql_group_shi = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SHI'";
            String sql_group_shl = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SHL'";
            String sql_group_bnp = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'BNP'";
            String sql_group_cap = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'CAP'";
            String sql_group_jjb = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'JJB'";
            String sql_group_sav = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SAV'";
            String sql_group_sds = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SDS'";
            String sql_group_tas = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'TAS'";
            String sql_group_sci = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SCI'";
            String sql_group_ssf = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'SSF'";
            String sql_group_etc = "update group_info set  TRY_PERSONS = ? where GROUP_CODE = 'ETC'";
            String sql_commit =    "commit"; // sql 쿼리

            // prepareStatement에서 해당 sql을 미리 컴파일한다.
            // 쿼리를 실행하고 결과를 ResultSet 객체에 담는다.
            pstmt = conn.prepareStatement(sql_group_sfg);
            pstmt.setString(1, String.valueOf(lpersons_SFG));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_shb);
            pstmt.setString(1, String.valueOf(lpersons_SHB));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_shc);
            pstmt.setString(1, String.valueOf(lpersons_SHC));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_shi);
            pstmt.setString(1, String.valueOf(lpersons_SHI));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_shl);
            pstmt.setString(1, String.valueOf(lpersons_SHL));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_bnp);
            pstmt.setString(1, String.valueOf(lpersons_BNP));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_cap);
            pstmt.setString(1, String.valueOf(lpersons_CAP));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_jjb);
            pstmt.setString(1, String.valueOf(lpersons_JJB));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_sav);
            pstmt.setString(1, String.valueOf(lpersons_SAV));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_sds);
            pstmt.setString(1, String.valueOf(lpersons_SDS));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_tas);
            pstmt.setString(1, String.valueOf(lpersons_TAS));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_sci);
            pstmt.setString(1, String.valueOf(lpersons_SCI));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_ssf);
            pstmt.setString(1, String.valueOf(lpersons_SSF));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_group_etc);
            pstmt.setString(1, String.valueOf(lpersons_ETC));
            pstmt.executeQuery();

            pstmt = conn.prepareStatement(sql_commit);
            pstmt.executeQuery();


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

    ////////////////

    @FXML
    private void showButtonAction(ActionEvent event)
    {
        System.out.println("[DEBUG][SettingScreenController] showButtonAction");
        showHowManyParticipants();

    }

    @FXML
    private void applyButtonAction(ActionEvent event)
    {
        System.out.println("[DEBUG][SettingScreenController] applyButtonAction");
        setGroupInfoTableContent();

    }

    @FXML
    private void exitButtonAction(ActionEvent event)
    {
        System.out.println("[DEBUG][SettingScreenController] exitButtonAction");
        myController.setScreen(ScreensFramework.screen13ID_all_group);
    }


}