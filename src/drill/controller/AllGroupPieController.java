package drill.controller;

import drill.ControlledScreen;
import drill.ScreensController;
import drill.ScreensFramework;
import drill.data.AccessData;
import drill.data.DefineValue;
import drill.data.SharedDataObject;
import drill.data.StatisticsDataObject;
import drill.utils.DBObject;
import drill.utils.MyLog;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
import java.util.ResourceBundle;
/**
 * Created by cross on 2015-06-23.
 */
//public class SmishingTabController extends Applet implements Initializable , ControlledScreen {
public class AllGroupPieController implements Initializable , ControlledScreen {

    ScreensController myController;

    StatisticsDataObject mStatisticsDataObject;
    SharedDataObject mSharedDataObject;

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private AnchorPane ransomMain;

    //Main View - Pie Chart Metrix
    @FXML
    private PieChart main_pie_metrix00;
    @FXML
    private PieChart main_pie_metrix01;
    @FXML
    private PieChart main_pie_metrix02;
    @FXML
    private PieChart main_pie_metrix03;
    @FXML
    private PieChart main_pie_metrix10;
    @FXML
    private PieChart main_pie_metrix11;
    @FXML
    private PieChart main_pie_metrix12;
    @FXML
    private PieChart main_pie_metrix13;
    @FXML
    private PieChart main_pie_metrix20;
    @FXML
    private PieChart main_pie_metrix21;
    @FXML
    private PieChart main_pie_metrix22;
    @FXML
    private PieChart main_pie_metrix23;

    //그룹사 파이차트 밑에 리스트뷰
    @FXML
    private ListView<String> listviewtemp;


    private int temp_count_int = 0;

    private int DB_TABLE_FLAG_BANK = 1;


    //_NO_RESPONSE
    //_WEB_ACCESS
    //_APP_DOWN
    //_APP_EXECUTE
    //PieChart.Data pie_data_xxx_NO_RESPONSE;
    //PieChart.Data pie_data_xxx_WEB_ACCESS;
    //PieChart.Data pie_data_xxx_APP_DOWN;
    //PieChart.Data pie_data_xxx_APP_EXECUTE;


    //SFG
    private PieChart.Data pie_data_SFG_SMS_SEND;
    private PieChart.Data pie_data_SFG_WEB_ACCESS;
    private PieChart.Data pie_data_SFG_APP_DOWN;
    private PieChart.Data pie_data_SFG_APP_EXECUTE;
    //SHB
    private PieChart.Data pie_data_SHB_SMS_SEND;
    private PieChart.Data pie_data_SHB_WEB_ACCESS;
    private PieChart.Data pie_data_SHB_APP_DOWN;
    private PieChart.Data pie_data_SHB_APP_EXECUTE;
    //SHC
    private PieChart.Data pie_data_SHC_SMS_SEND;
    private PieChart.Data pie_data_SHC_WEB_ACCESS;
    private PieChart.Data pie_data_SHC_APP_DOWN;
    private PieChart.Data pie_data_SHC_APP_EXECUTE;
    //SHI
    private PieChart.Data pie_data_SHI_SMS_SEND;
    private PieChart.Data pie_data_SHI_WEB_ACCESS;
    private PieChart.Data pie_data_SHI_APP_DOWN;
    private PieChart.Data pie_data_SHI_APP_EXECUTE;
    //SHL
    private PieChart.Data pie_data_SHL_SMS_SEND;
    private PieChart.Data pie_data_SHL_WEB_ACCESS;
    private PieChart.Data pie_data_SHL_APP_DOWN;
    private PieChart.Data pie_data_SHL_APP_EXECUTE;
    //BNP
    private PieChart.Data pie_data_BNP_SMS_SEND;
    private PieChart.Data pie_data_BNP_WEB_ACCESS;
    private PieChart.Data pie_data_BNP_APP_DOWN;
    private PieChart.Data pie_data_BNP_APP_EXECUTE;
    //CAP
    private PieChart.Data pie_data_CAP_SMS_SEND;
    private PieChart.Data pie_data_CAP_WEB_ACCESS;
    private PieChart.Data pie_data_CAP_APP_DOWN;
    private PieChart.Data pie_data_CAP_APP_EXECUTE;
    //JJB
    private PieChart.Data pie_data_JJB_SMS_SEND;
    private PieChart.Data pie_data_JJB_WEB_ACCESS;
    private PieChart.Data pie_data_JJB_APP_DOWN;
    private PieChart.Data pie_data_JJB_APP_EXECUTE;
    //SAV
    private PieChart.Data pie_data_SAV_SMS_SEND;
    private PieChart.Data pie_data_SAV_WEB_ACCESS;
    private PieChart.Data pie_data_SAV_APP_DOWN;
    private PieChart.Data pie_data_SAV_APP_EXECUTE;
    //SDS
    private PieChart.Data pie_data_SDS_SMS_SEND;
    private PieChart.Data pie_data_SDS_WEB_ACCESS;
    private PieChart.Data pie_data_SDS_APP_DOWN;
    private PieChart.Data pie_data_SDS_APP_EXECUTE;
    //TAS
    private PieChart.Data pie_data_TAS_SMS_SEND;
    private PieChart.Data pie_data_TAS_WEB_ACCESS;
    private PieChart.Data pie_data_TAS_APP_DOWN;
    private PieChart.Data pie_data_TAS_APP_EXECUTE;

    //SCI
    private PieChart.Data pie_data_SCI_SMS_SEND;
    private PieChart.Data pie_data_SCI_WEB_ACCESS;
    private PieChart.Data pie_data_SCI_APP_DOWN;
    private PieChart.Data pie_data_SCI_APP_EXECUTE;

    ObservableList<PieChart.Data> pieChartDatas_SFG;
    ObservableList<PieChart.Data> pieChartDatas_SHB;
    ObservableList<PieChart.Data> pieChartDatas_SHC;
    ObservableList<PieChart.Data> pieChartDatas_SHI;
    ObservableList<PieChart.Data> pieChartDatas_SHL;
    ObservableList<PieChart.Data> pieChartDatas_BNP;
    ObservableList<PieChart.Data> pieChartDatas_CAP;
    ObservableList<PieChart.Data> pieChartDatas_JJB;
    ObservableList<PieChart.Data> pieChartDatas_SAV;
    ObservableList<PieChart.Data> pieChartDatas_SDS;
    ObservableList<PieChart.Data> pieChartDatas_TAS;
    ObservableList<PieChart.Data> pieChartDatas_SCI;

    //Stage stage;
    //Scene scene;
    //FXMLLoader loader;
    //Parent root;
    //public void setStage(Stage stage){ this.stage = stage; }


    private final String LAYOUT_FXML_RANSOM_MAIN = "all_group_display.fxml";

    @FXML
    private void OnMouseClicked_00(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SFG);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_01(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHB);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_02(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHC);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_03(MouseEvent event){

        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHI);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_10(MouseEvent event){

        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHL);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_11(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.BNP);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_12(MouseEvent event){

        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.CAP);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_13(MouseEvent event){

        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.JJB);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_20(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SAV);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_21(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SDS);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_22(MouseEvent event){

        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.TAS);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }
    @FXML
    private void OnMouseClicked_23(MouseEvent event){
        mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SCI);
        myController.setScreen(ScreensFramework.screen0ID_group_detail);
    }

    @FXML
    private void apt_table_ButtonAction(ActionEvent event){ myController.setScreen(ScreensFramework.screen14ID_apttable); }

    private ObservableList<String> getPhoneNumList(int flags) throws SQLException, ClassNotFoundException {
        Connection con = getConnection();
        ObservableList<String> phoneNum = FXCollections.observableArrayList();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select PH from info");
        while (rs.next()) {
        phoneNum.add(rs.getString("PH"));
        }
        return phoneNum;
    }

    private void pie_chart_init()
    {
        //그룹사 서열
        //1.SFG
        //2.SHB
        //3.SHC
        //4.SHI
        //5.SHL
        //6.BNP
        //7.CAP
        //8.JJB
        //9.SAV
        //1.0SDS
        //11.TAS
        //12.SCI
        main_pie_metrix00.setTitle("SHINHAN GROUP");
        main_pie_metrix01.setTitle("SHINHAN BANK");
        main_pie_metrix02.setTitle("SHINHAN CARD");
        main_pie_metrix03.setTitle("SHINHAN INVEST");
        main_pie_metrix10.setTitle("SHINHAN LIFE");
        main_pie_metrix11.setTitle("SHINHAN BNPP");
        main_pie_metrix12.setTitle("SHINHAN CAPITAL");
        main_pie_metrix13.setTitle("SHINHAN JEJU BANK");
        main_pie_metrix20.setTitle("SHINHAN SAVINGS");
        main_pie_metrix21.setTitle("SHINHAN DATA SYSTEM");
        main_pie_metrix22.setTitle("SHINHAN AITAS");
        main_pie_metrix23.setTitle("SHINHAN CI");

        //범례는 왼쪽에
        main_pie_metrix00.setLegendSide(Side.LEFT);
        main_pie_metrix01.setLegendSide(Side.LEFT);
        main_pie_metrix02.setLegendSide(Side.LEFT);
        main_pie_metrix03.setLegendSide(Side.LEFT);
        main_pie_metrix10.setLegendSide(Side.LEFT);
        main_pie_metrix11.setLegendSide(Side.LEFT);
        main_pie_metrix12.setLegendSide(Side.LEFT);
        main_pie_metrix13.setLegendSide(Side.LEFT);
        main_pie_metrix20.setLegendSide(Side.LEFT);
        main_pie_metrix21.setLegendSide(Side.LEFT);
        main_pie_metrix22.setLegendSide(Side.LEFT);
        main_pie_metrix23.setLegendSide(Side.LEFT);


        //원에 달린 라벨 안보기, 이유는 원이 너무 작다.
        //main_pie_metrix00.setLabelsVisible(true);
        //main_pie_metrix00.setLabelLineLength(-30);

        main_pie_metrix01.setLabelsVisible(true);
        main_pie_metrix02.setLabelsVisible(true);
        main_pie_metrix03.setLabelsVisible(true);
        main_pie_metrix10.setLabelsVisible(true);
        main_pie_metrix11.setLabelsVisible(true);
        main_pie_metrix12.setLabelsVisible(true);
        main_pie_metrix13.setLabelsVisible(true);
        main_pie_metrix20.setLabelsVisible(true);
        main_pie_metrix21.setLabelsVisible(true);
        main_pie_metrix22.setLabelsVisible(true);
        main_pie_metrix23.setLabelsVisible(true);

        //SFG
        pie_data_SFG_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0 );
        pie_data_SFG_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0 );
        pie_data_SFG_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0 );
        pie_data_SFG_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0 );

        //SHB
        pie_data_SHB_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0 );
        pie_data_SHB_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SHB_APP_DOWN  = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SHB_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SHC
        pie_data_SHC_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SHC_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SHC_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SHC_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SHI
        pie_data_SHI_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SHI_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SHI_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SHI_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SHL
        pie_data_SHL_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SHL_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SHL_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SHL_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //BNP
        pie_data_BNP_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_BNP_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_BNP_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_BNP_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //CAP
        pie_data_CAP_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_CAP_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_CAP_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_CAP_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //JJB
        pie_data_JJB_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_JJB_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_JJB_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_JJB_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SAV
        pie_data_SAV_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SAV_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SAV_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SAV_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SDS
        pie_data_SDS_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SDS_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SDS_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SDS_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //TAS
        pie_data_TAS_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_TAS_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_TAS_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_TAS_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

        //SCI
        //일단 차트 샘플로 데이터 하드코딩함.
        pie_data_SCI_SMS_SEND = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0);
        pie_data_SCI_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0);
        pie_data_SCI_APP_DOWN = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0);
        pie_data_SCI_APP_EXECUTE = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0);

    }
    private void pie_name_setting()
    {
        //pie_data_NO_RESPONSE.setName(String.format("%d", 0));
        //pie_data_WEB_ACCESS.setName(String.format("%d", a_urlacc_man));
        //pie_data_APP_INSTALL.setName(String.format("%d", a_app_install));
        //pie_data_APP_EXECUTE.setName(String.format("%d", a_exec_man));

        //SFG
        pie_data_SFG_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SFG_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SFG_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SFG_APP_EXECUTE.setName(String.format("%d", 0));

        //SHB
        pie_data_SHB_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SHB_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SHB_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SHB_APP_EXECUTE.setName(String.format("%d", 0));

        //SHC
        pie_data_SHC_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SHC_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SHC_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SHC_APP_EXECUTE.setName(String.format("%d", 0));

        //SHI
        pie_data_SHI_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SHI_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SHI_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SHI_APP_EXECUTE.setName(String.format("%d", 0));

        //SHL
        pie_data_SHL_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SHL_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SHL_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SHL_APP_EXECUTE.setName(String.format("%d", 0));

        //BNP
        pie_data_BNP_SMS_SEND.setName(String.format("%d", 0));
        pie_data_BNP_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_BNP_APP_DOWN.setName(String.format("%d", 0));
        pie_data_BNP_APP_EXECUTE.setName(String.format("%d", 0));

        //CAP
        pie_data_CAP_SMS_SEND.setName(String.format("%d", 0));
        pie_data_CAP_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_CAP_APP_DOWN.setName(String.format("%d", 0));
        pie_data_CAP_APP_EXECUTE.setName(String.format("%d", 0));

        //JJB
        pie_data_JJB_SMS_SEND.setName(String.format("%d", 0));
        pie_data_JJB_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_JJB_APP_DOWN.setName(String.format("%d", 0));
        pie_data_JJB_APP_EXECUTE.setName(String.format("%d", 0));

        //SAV
        pie_data_SAV_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SAV_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SAV_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SAV_APP_EXECUTE.setName(String.format("%d", 0));

        //SDS
        pie_data_SDS_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SDS_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SDS_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SDS_APP_EXECUTE.setName(String.format("%d", 0));

        //TAS
        pie_data_TAS_SMS_SEND.setName(String.format("%d", 0));
        pie_data_TAS_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_TAS_APP_DOWN.setName(String.format("%d", 0));
        pie_data_TAS_APP_EXECUTE.setName(String.format("%d", 0));

        //SCI
        //일단 차트 샘플로 데이터 하드코딩함.
        pie_data_SCI_SMS_SEND.setName(String.format("%d", 0));
        pie_data_SCI_WEB_ACCESS.setName(String.format("%d", 0));
        pie_data_SCI_APP_DOWN.setName(String.format("%d", 0));
        pie_data_SCI_APP_EXECUTE.setName(String.format("%d", 0));


        //SFG
        pie_data_SFG_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SFG_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SFG_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SFG_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SHB
        pie_data_SHB_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SHB_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SHB_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SHB_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SHC
        pie_data_SHC_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SHC_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SHC_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SHC_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SHI
        pie_data_SHI_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SHI_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SHI_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SHI_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SHL
        pie_data_SHL_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SHL_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SHL_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SHL_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //BNP
        pie_data_BNP_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_BNP_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_BNP_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_BNP_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //CAP
        pie_data_CAP_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_CAP_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_CAP_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_CAP_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //JJB
        pie_data_JJB_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_JJB_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_JJB_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_JJB_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SAV
        pie_data_SAV_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SAV_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SAV_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SAV_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SDS
        pie_data_SDS_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SDS_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SDS_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SDS_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //TAS
        pie_data_TAS_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_TAS_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_TAS_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_TAS_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

        //SCI
        //일단 차트 샘플로 데이터 하드코딩함.
        pie_data_SCI_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_SCI_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_SCI_APP_DOWN.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_SCI_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);

    }
    private void pie_chart_data_setting()
    {

        //System.out.println("pie_chart_data_setting()");
        //퍼센트로 넣을지 그냥 명수로 넣을지는 고민 및 결정 해야됨.
        //mStatisticsDataObject
        //그룹사별 데이터 Getter
        //SFG = 1
        int lSFG_TOTAL_MAN_ = mStatisticsDataObject.get_SFG_TOTAL_MAN();
        int lSFG_NO_RESPONSE_ = mStatisticsDataObject.get_SFG_NO_RESPONSE();
        int lSFG_WEB_ACCESS_ = mStatisticsDataObject.get_SFG_WEB_ACCESS();
        int lSFG_APP_DOWN_ = mStatisticsDataObject.get_SFG_APP_INSTALL();
        int lSFG_APP_EXECUTE_ = mStatisticsDataObject.get_SFG_APP_EXECUTE();

        //SHB = 2
        int lSHB_TOTAL_MAN_ = mStatisticsDataObject.get_SHB_TOTAL_MAN();
        int lSHB_NO_RESPONSE_ = mStatisticsDataObject.get_SHB_NO_RESPONSE();
        int lSHB_WEB_ACCESS_ = mStatisticsDataObject.get_SHB_WEB_ACCESS();
        int lSHB_APP_DOWN_ = mStatisticsDataObject.get_SHB_APP_INSTALL();
        int lSHB_APP_EXECUTE_ = mStatisticsDataObject.get_SHB_APP_EXECUTE();

        //SHC = 3
        int lSHC_TOTAL_MAN_ = mStatisticsDataObject.get_SHC_TOTAL_MAN();
        int lSHC_NO_RESPONSE_ = mStatisticsDataObject.get_SHC_NO_RESPONSE();
        int lSHC_WEB_ACCESS_ = mStatisticsDataObject.get_SHC_WEB_ACCESS();
        int lSHC_APP_DOWN_ = mStatisticsDataObject.get_SHC_APP_INSTALL();
        int lSHC_APP_EXECUTE_ = mStatisticsDataObject.get_SHC_APP_EXECUTE();

        //SHI = 4
        int lSHI_TOTAL_MAN_ = mStatisticsDataObject.get_SHI_TOTAL_MAN();
        int lSHI_NO_RESPONSE_ = mStatisticsDataObject.get_SHI_NO_RESPONSE();
        int lSHI_WEB_ACCESS_ = mStatisticsDataObject.get_SHI_WEB_ACCESS();
        int lSHI_APP_DOWN_ = mStatisticsDataObject.get_SHI_APP_INSTALL();
        int lSHI_APP_EXECUTE_ = mStatisticsDataObject.get_SHI_APP_EXECUTE();

        //SHL = 5
        int lSHL_TOTAL_MAN_ = mStatisticsDataObject.get_SHL_TOTAL_MAN();
        int lSHL_NO_RESPONSE_ = mStatisticsDataObject.get_SHL_NO_RESPONSE();
        int lSHL_WEB_ACCESS_ = mStatisticsDataObject.get_SHL_WEB_ACCESS();
        int lSHL_APP_DOWN_ = mStatisticsDataObject.get_SHL_APP_INSTALL();
        int lSHL_APP_EXECUTE_ = mStatisticsDataObject.get_SHL_APP_EXECUTE();

        //BNP = 6
        int lBNP_TOTAL_MAN_ = mStatisticsDataObject.get_BNP_TOTAL_MAN();
        int lBNP_NO_RESPONSE_ = mStatisticsDataObject.get_BNP_NO_RESPONSE();
        int lBNP_WEB_ACCESS_ = mStatisticsDataObject.get_BNP_WEB_ACCESS();
        int lBNP_APP_DOWN_ = mStatisticsDataObject.get_BNP_APP_INSTALL();
        int lBNP_APP_EXECUTE_ = mStatisticsDataObject.get_BNP_APP_EXECUTE();

        //CAP = 7
        int lCAP_TOTAL_MAN_ = mStatisticsDataObject.get_CAP_TOTAL_MAN();
        int lCAP_NO_RESPONSE_ = mStatisticsDataObject.get_CAP_NO_RESPONSE();
        int lCAP_WEB_ACCESS_ = mStatisticsDataObject.get_CAP_WEB_ACCESS();
        int lCAP_APP_DOWN_ = mStatisticsDataObject.get_CAP_APP_INSTALL();
        int lCAP_APP_EXECUTE_ = mStatisticsDataObject.get_CAP_APP_EXECUTE();

        //JJB = 8
        int lJJB_TOTAL_MAN_ = mStatisticsDataObject.get_JJB_TOTAL_MAN();
        int lJJB_NO_RESPONSE_ = mStatisticsDataObject.get_JJB_NO_RESPONSE();
        int lJJB_WEB_ACCESS_ = mStatisticsDataObject.get_JJB_WEB_ACCESS();
        int lJJB_APP_DOWN_ = mStatisticsDataObject.get_JJB_APP_INSTALL();
        int lJJB_APP_EXECUTE_ = mStatisticsDataObject.get_JJB_APP_EXECUTE();

        //SAV = 9
        int lSAV_TOTAL_MAN_ = mStatisticsDataObject.get_SAV_TOTAL_MAN();
        int lSAV_NO_RESPONSE_ = mStatisticsDataObject.get_SAV_NO_RESPONSE();
        int lSAV_WEB_ACCESS_ = mStatisticsDataObject.get_SAV_WEB_ACCESS();
        int lSAV_APP_DOWN_ = mStatisticsDataObject.get_SAV_APP_INSTALL();
        int lSAV_APP_EXECUTE_ = mStatisticsDataObject.get_SAV_APP_EXECUTE();

        //SDS = 10
        int lSDS_TOTAL_MAN_ = mStatisticsDataObject.get_SDS_TOTAL_MAN();
        int lSDS_NO_RESPONSE_ = mStatisticsDataObject.get_SDS_NO_RESPONSE();
        int lSDS_WEB_ACCESS_ = mStatisticsDataObject.get_SDS_WEB_ACCESS();
        int lSDS_APP_DOWN_ = mStatisticsDataObject.get_SDS_APP_INSTALL();
        int lSDS_APP_EXECUTE_ = mStatisticsDataObject.get_SDS_APP_EXECUTE();

        //TAS = 11
        int lTAS_TOTAL_MAN_ = mStatisticsDataObject.get_TAS_TOTAL_MAN();
        int lTAS_NO_RESPONSE_ = mStatisticsDataObject.get_TAS_NO_RESPONSE();
        int lTAS_WEB_ACCESS_ = mStatisticsDataObject.get_TAS_WEB_ACCESS();
        int lTAS_APP_DOWN_ = mStatisticsDataObject.get_TAS_APP_INSTALL();
        int lTAS_APP_EXECUTE_ = mStatisticsDataObject.get_TAS_APP_EXECUTE();

        //SCI = 12
        int lSCI_TOTAL_MAN_ = mStatisticsDataObject.get_SCI_TOTAL_MAN();
        int lSCI_NO_RESPONSE_ = mStatisticsDataObject.get_SCI_NO_RESPONSE();
        int lSCI_WEB_ACCESS_ = mStatisticsDataObject.get_SCI_WEB_ACCESS();
        int lSCI_APP_DOWN_ = mStatisticsDataObject.get_SCI_APP_INSTALL();
        int lSCI_APP_EXECUTE_ = mStatisticsDataObject.get_SCI_APP_EXECUTE();



        //SFG
        pie_data_SFG_SMS_SEND.setPieValue(lSFG_TOTAL_MAN_ );
        pie_data_SFG_WEB_ACCESS.setPieValue(lSFG_WEB_ACCESS_ );
        pie_data_SFG_APP_DOWN.setPieValue(lSFG_APP_DOWN_ );
        pie_data_SFG_APP_EXECUTE.setPieValue(lSFG_APP_EXECUTE_ );



        //SHB
        pie_data_SHB_SMS_SEND.setPieValue(lSHB_TOTAL_MAN_ );
        pie_data_SHB_WEB_ACCESS.setPieValue(lSHB_WEB_ACCESS_);
        pie_data_SHB_APP_DOWN.setPieValue(lSHB_APP_DOWN_);
        pie_data_SHB_APP_EXECUTE.setPieValue(lSHB_APP_EXECUTE_);

        //SHC
        pie_data_SHC_SMS_SEND.setPieValue( lSHC_TOTAL_MAN_);
        pie_data_SHC_WEB_ACCESS.setPieValue(lSHC_WEB_ACCESS_);
        pie_data_SHC_APP_DOWN.setPieValue(lSHC_APP_DOWN_);
        pie_data_SHC_APP_EXECUTE.setPieValue(lSHC_APP_EXECUTE_);

        //SHI
        pie_data_SHI_SMS_SEND.setPieValue(lSHI_TOTAL_MAN_);
        pie_data_SHI_WEB_ACCESS.setPieValue(lSHI_WEB_ACCESS_);
        pie_data_SHI_APP_DOWN.setPieValue(lSHI_APP_DOWN_);
        pie_data_SHI_APP_EXECUTE.setPieValue(lSHI_APP_EXECUTE_);

        //SHL
        pie_data_SHL_SMS_SEND.setPieValue(lSHL_TOTAL_MAN_);
        pie_data_SHL_WEB_ACCESS.setPieValue(lSHL_WEB_ACCESS_);
        pie_data_SHL_APP_DOWN.setPieValue(lSHL_APP_DOWN_);
        pie_data_SHL_APP_EXECUTE.setPieValue(lSHL_APP_EXECUTE_);

        //BNP
        pie_data_BNP_SMS_SEND.setPieValue(lBNP_TOTAL_MAN_);
        pie_data_BNP_WEB_ACCESS.setPieValue(lBNP_WEB_ACCESS_);
        pie_data_BNP_APP_DOWN.setPieValue(lBNP_APP_DOWN_);
        pie_data_BNP_APP_EXECUTE.setPieValue(lBNP_APP_EXECUTE_);

        //CAP
        pie_data_CAP_SMS_SEND.setPieValue(lCAP_TOTAL_MAN_);
        pie_data_CAP_WEB_ACCESS.setPieValue(lCAP_WEB_ACCESS_);
        pie_data_CAP_APP_DOWN.setPieValue(lCAP_APP_DOWN_);
        pie_data_CAP_APP_EXECUTE.setPieValue(lCAP_APP_EXECUTE_);

        //JJB
        pie_data_JJB_SMS_SEND.setPieValue(lJJB_TOTAL_MAN_);
        pie_data_JJB_WEB_ACCESS.setPieValue(lJJB_WEB_ACCESS_);
        pie_data_JJB_APP_DOWN.setPieValue(lJJB_APP_DOWN_);
        pie_data_JJB_APP_EXECUTE.setPieValue(lJJB_APP_EXECUTE_);

        //for debug
        temp_count_int++;
        //SAV
        pie_data_SAV_SMS_SEND.setPieValue(lSAV_TOTAL_MAN_);
        pie_data_SAV_WEB_ACCESS.setPieValue(lSAV_WEB_ACCESS_);
        pie_data_SAV_APP_DOWN.setPieValue(lSAV_APP_DOWN_);
        pie_data_SAV_APP_EXECUTE.setPieValue(lSAV_APP_EXECUTE_);

        //SDS
        pie_data_SDS_SMS_SEND.setPieValue(lSDS_TOTAL_MAN_);
        pie_data_SDS_WEB_ACCESS.setPieValue(lSDS_WEB_ACCESS_);
        pie_data_SDS_APP_DOWN.setPieValue(lSDS_APP_DOWN_);
        pie_data_SDS_APP_EXECUTE.setPieValue(lSDS_APP_EXECUTE_);

        //TAS
        pie_data_TAS_SMS_SEND.setPieValue(lTAS_TOTAL_MAN_);
        pie_data_TAS_WEB_ACCESS.setPieValue(lTAS_WEB_ACCESS_);
        pie_data_TAS_APP_DOWN.setPieValue(lTAS_APP_DOWN_);
        pie_data_TAS_APP_EXECUTE.setPieValue(lTAS_APP_EXECUTE_);

        //SCI
        //일단 차트 샘플로 데이터 하드코딩함.
        //pie_data_SCI_NO_RESPONSE = new PieChart.Data(S_NO_RESPONSE, 10);
        //pie_data_SCI_WEB_ACCESS = new PieChart.Data(S_WEB_ACCESS, 5);
        //pie_data_SCI_APP_DOWN = new PieChart.Data(S_APP_DOWN, 3);
        //pie_data_SCI_APP_EXECUTE = new PieChart.Data(S_APP_EXECUTE, 2);
        pie_data_SCI_SMS_SEND.setPieValue(lSCI_TOTAL_MAN_);
        pie_data_SCI_WEB_ACCESS.setPieValue(lSCI_WEB_ACCESS_);
        pie_data_SCI_APP_DOWN.setPieValue(lSCI_APP_DOWN_);
        pie_data_SCI_APP_EXECUTE.setPieValue(lSCI_APP_EXECUTE_);


        //pieChartDatas_SFG = FXCollections.observableArrayList(pie_data_SFG_NO_RESPONSE, pie_data_SFG_WEB_ACCESS,pie_data_SFG_APP_DOWN,pie_data_SFG_APP_EXECUTE);
        pieChartDatas_SFG = FXCollections.observableArrayList(pie_data_SFG_SMS_SEND, pie_data_SFG_WEB_ACCESS, pie_data_SFG_APP_DOWN, pie_data_SFG_APP_EXECUTE);
        pieChartDatas_SHB = FXCollections.observableArrayList(pie_data_SHB_SMS_SEND, pie_data_SHB_WEB_ACCESS, pie_data_SHB_APP_DOWN, pie_data_SHB_APP_EXECUTE);
        pieChartDatas_SHC = FXCollections.observableArrayList(pie_data_SHC_SMS_SEND, pie_data_SHC_WEB_ACCESS, pie_data_SHC_APP_DOWN, pie_data_SHC_APP_EXECUTE);
        pieChartDatas_SHI = FXCollections.observableArrayList(pie_data_SHI_SMS_SEND, pie_data_SHI_WEB_ACCESS, pie_data_SHI_APP_DOWN, pie_data_SHI_APP_EXECUTE);
        pieChartDatas_SHL = FXCollections.observableArrayList(pie_data_SHL_SMS_SEND, pie_data_SHL_WEB_ACCESS, pie_data_SHL_APP_DOWN, pie_data_SHL_APP_EXECUTE);
        pieChartDatas_BNP = FXCollections.observableArrayList(pie_data_BNP_SMS_SEND, pie_data_BNP_WEB_ACCESS, pie_data_BNP_APP_DOWN, pie_data_BNP_APP_EXECUTE);
        pieChartDatas_CAP = FXCollections.observableArrayList(pie_data_CAP_SMS_SEND, pie_data_CAP_WEB_ACCESS, pie_data_CAP_APP_DOWN, pie_data_CAP_APP_EXECUTE);
        pieChartDatas_JJB = FXCollections.observableArrayList(pie_data_JJB_SMS_SEND, pie_data_JJB_WEB_ACCESS, pie_data_JJB_APP_DOWN, pie_data_JJB_APP_EXECUTE);
        pieChartDatas_SAV = FXCollections.observableArrayList(pie_data_SAV_SMS_SEND, pie_data_SAV_WEB_ACCESS, pie_data_SAV_APP_DOWN, pie_data_SAV_APP_EXECUTE);
        pieChartDatas_SDS = FXCollections.observableArrayList(pie_data_SDS_SMS_SEND, pie_data_SDS_WEB_ACCESS, pie_data_SDS_APP_DOWN, pie_data_SDS_APP_EXECUTE);
        pieChartDatas_TAS = FXCollections.observableArrayList(pie_data_TAS_SMS_SEND, pie_data_TAS_WEB_ACCESS, pie_data_TAS_APP_DOWN, pie_data_TAS_APP_EXECUTE);
        pieChartDatas_SCI = FXCollections.observableArrayList(pie_data_SCI_SMS_SEND, pie_data_SCI_WEB_ACCESS, pie_data_SCI_APP_DOWN, pie_data_SCI_APP_EXECUTE);



        //data1.setPieValue(getUrlClickCount(DB_TABLE_FLAG_BANK));
        //data2.setPieValue(getAppInstallCount(DB_TABLE_FLAG_BANK));
        //data3.setPieValue(getAppExecuteCount(DB_TABLE_FLAG_BANK));
        //pieCharts00 = FXCollections.observableArrayList(data1, data2, data3);

    }

    /**
     * Initializes the controller class.
     */
    //@Override public void initialize(URL url, ResourceBundle rb) {}


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        mStatisticsDataObject = StatisticsDataObject.getInstance();
        mSharedDataObject = SharedDataObject.getInstance();


        pie_chart_init();
        pie_chart_data_setting();

//        data1 = new PieChart.Data("URL access", getUrlClickCount(DB_TABLE_FLAG_BANK));
  //      data2 = new PieChart.Data("APP install", getAppInstallCount(DB_TABLE_FLAG_BANK));
    //    data3 = new PieChart.Data("APP execute", getAppExecuteCount(DB_TABLE_FLAG_BANK));
      //  pieCharts00 = FXCollections.observableArrayList(data1, data2, data3);
       // main_pie_metrix00.setData(pieCharts00);







        //setDrilldownData(main_pie_metrix00, data1, "a");
        //setDrilldownData(main_pie_metrix00, data2, "b");
        //setDrilldownData(main_pie_metrix00, data3, "c");
/*
        ObservableList<PieChart.Data> pieCharts2 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));

        ObservableList<PieChart.Data> pieCharts3 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));

        ObservableList<PieChart.Data> pieCharts4 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));

        ObservableList<PieChart.Data> pieCharts5 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));

        ObservableList<PieChart.Data> pieCharts6 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts7 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts8 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts9 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts10 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts11 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        ObservableList<PieChart.Data> pieCharts12 = FXCollections.observableArrayList(
                new PieChart.Data("SMS received", 1),
                new PieChart.Data("SMS Click", 1),
                new PieChart.Data("Ransom Infected", 1));
        MyLog.i("flag!!!!!!!!!!!!!!!!");
*/
        /*
        영어
        main_pie_metrix00.setTitle("SHINHAN BANK");
        main_pie_metrix01.setTitle("SHINHAN CARD");
        main_pie_metrix02.setTitle("SHINHAN LIFE");
        main_pie_metrix03.setTitle("SHINHAN GROUP");
        main_pie_metrix10.setTitle("SHINHAN SAVINGS");
        main_pie_metrix11.setTitle("SHINHAN BNPP");
        main_pie_metrix12.setTitle("SHINHAN AITAS");
        main_pie_metrix13.setTitle("SHINHAN CAPITAL");
        main_pie_metrix20.setTitle("SHINHAN DATA SYSTEM");
        main_pie_metrix21.setTitle("SHINHAN JEJU BANK");
        main_pie_metrix22.setTitle("SHINHAN INVEST");
        main_pie_metrix23.setTitle("SHINHAN CI");
        */




        //그룹사 서열 대로
        main_pie_metrix00.setData(pieChartDatas_SFG); //1.SFG
        main_pie_metrix01.setData(pieChartDatas_SHB); //2.SHB
        main_pie_metrix02.setData(pieChartDatas_SHC);  //3.SHC
        main_pie_metrix03.setData(pieChartDatas_SHI);  //4.SHI
        main_pie_metrix10.setData(pieChartDatas_SHL); //5.SHL
        main_pie_metrix11.setData(pieChartDatas_BNP); //6.BNP
        main_pie_metrix12.setData(pieChartDatas_CAP); //7.CAP
        main_pie_metrix13.setData(pieChartDatas_JJB); //8.JJB
        main_pie_metrix20.setData(pieChartDatas_SAV); //9.SAV
        main_pie_metrix21.setData(pieChartDatas_SDS);  //10.SDS
        main_pie_metrix22.setData(pieChartDatas_TAS); //11.TAS
        main_pie_metrix23.setData(pieChartDatas_SCI);  //12.SCI




        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                //int i = 0;
                while (true) {
                    //final int finalI = i;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //data1.setPieValue(getUrlClickCount(DB_TABLE_FLAG_BANK));
                            //data2.setPieValue(getAppInstallCount(DB_TABLE_FLAG_BANK));
                            //data3.setPieValue(getAppExecuteCount(DB_TABLE_FLAG_BANK));
                            //pieCharts00 = FXCollections.observableArrayList(data1, data2, data3);


                            update_group_ui_display();
                            //System.out.println("=======thread run ===========!!! \n");
                        }
                    });
                    //i++;
                    Thread.sleep(3000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();
        /*executor = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                return thread;
            }
        });
        addToQueue = new AddToQueue();
        executor.execute(addToQueue);*/
        //-- Prepare Timeline

        //ObservableList<String> names = FXCollections.observableArrayList();
        //names.add("[1][2015.08.25 14:23:51][10.232.3.5][SHB][Android] WebPage Access");
        //names.add("[2][2015.08.25 14:30:42][10.232.3.5][SHB][Android] App Download Try");
        //names.add("[3][2015.08.25 14:23:51][192.168.0.1][JJB][IPhone] WebPage Access");
        //names.add("[4][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Executin");
        //names.add("[5][2015.08.25 14:23:51][10.232.3.5][SHB][Android] Enter the Password fails");
        //names.add("[7][2015.08.25 14:23:51][192.168.0.1][SCI][PC-Win7] WebPage Access");
        //names.add("[8][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Termination");
        //listviewtemp.setItems(names);


    //}else{ MyLog.i(""); }


    }

    private void print_value_pie_data()
    {
        //원에 숫자쓰기
        //지금은 그냥하고 숫자 원이 크기가 줄지 않도록 다른 코드로 바꿔야함.
        for (Node node : main_pie_metrix00.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SFG) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix01.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SHB) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix02.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SHC) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix03.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SHI) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix10.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SHL) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix11.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_BNP) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix12.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_CAP) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix13.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_JJB) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix20.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SAV) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix21.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SDS) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix22.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_TAS) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

        for (Node node : main_pie_metrix23.lookupAll(".text.chart-pie-label")) if (node instanceof Text)
            for (PieChart.Data data : pieChartDatas_SCI) if (data.getName().equals(((Text) node).getText()))
                ((Text) node).setText(String.format("%,.0f", data.getPieValue()));

    }
    private void update_group_ui_display() {

        //System.out.println("======update_group_ui_display()===========!!! \n");

        ObservableList<String> group_fising_log;

        //names = FXCollections.observableArrayList();
        group_fising_log = listviewtemp.getItems();

       //if (mStatisticsDataObject.isExistAccessDataInQueue())
        while (mStatisticsDataObject.isExistAccessDataInQueue4all())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4all();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log.add(0,lAccessData.getM_access_log());


        }

        //너무 많으면 좀 그럴수 있으니 100개 까지만 넣는다.
        //아직 안함.

        //FXCollections.sort(names);
        listviewtemp.setItems(group_fising_log);


        //ObservableList<String> names = FXCollections.observableArrayList();
        //names.add("[1][2015.08.25 14:23:51][10.232.3.5][SHB][Android] WebPage Access");
        //names.add("[2][2015.08.25 14:30:42][10.232.3.5][SHB][Android] App Download Try");
        //names.add("[3][2015.08.25 14:23:51][192.168.0.1][JJB][IPhone] WebPage Access");
        //names.add("[4][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Executin");
        //names.add("[5][2015.08.25 14:23:51][10.232.3.5][SHB][Android] Enter the Password fails");
        //names.add("[7][2015.08.25 14:23:51][192.168.0.1][SCI][PC-Win7] WebPage Access");
        //names.add("[8][2015.08.25 14:23:51][10.232.3.5][SHB][Android] App Termination");
        //listviewtemp.setItems(names);

        //차트를 그린다.
        //data1.setPieValue(getUrlClickCount(DB_TABLE_FLAG_BANK));
        //data2.setPieValue(getAppInstallCount(DB_TABLE_FLAG_BANK));
        //data3.setPieValue(getAppExecuteCount(DB_TABLE_FLAG_BANK));
        //pieCharts00 = FXCollections.observableArrayList(data1, data2, data3);
        pie_name_setting();
        pie_chart_data_setting();
        print_value_pie_data();
    }


    private int getUrlClickCount(int flag) {
        //System.out.println("=======getUrlClickCount ===========!!! \n");
        ResultSet a = null;
        int count = 0;
        switch (flag) {

            case 1:
            //    System.out.println("=======getUrlClickCount ===case 1: ========!!! \n");
                try (Connection con = getConnection()) {
                    //테이블이 존재하지 않으면 생성 낄낄
                    if (!DBObject.getInstance().urlClickSchemaExists(con)) {
                        MyLog.i("");
                        DBObject.getInstance().createUrlClickSchema(con);
                    }
                    Statement st = null;
                    st = con.createStatement();
                    ResultSet rs = st.executeQuery("select count(*) from urlclick");
                    rs.next();
                    count = rs.getInt(1);


                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
             //   System.out.println("=======getUrlClickCount ===case 2: ========!!! \n");
                break;
            default:
             //   System.out.println("=======getUrlClickCount ===default ========!!! \n");
                break;
        }
        return count;
    }

    private int getAppExecuteCount(int flag) {
  //      System.out.println("=======getAppExecuteCount ===========!!!");
        ResultSet a = null;
        int count = 0;
        try (Connection con = getConnection()) {
            //테이블이 존재하지 않으면 생성 낄낄
            if (!DBObject.getInstance().appExecuteSchemaExists(con)) {
                MyLog.i("");
                DBObject.getInstance().createAppExecuteSchema(con);
            }

            Statement st = null;
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select count(*) from info");
            rs.next();
//            MyLog.i("getAppExecuteCount = " + rs.getInt(1));
            count = rs.getInt(1);
//            System.out.println("=======getAppExecuteCount ========count : "+count);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    private int getAppInstallCount(int flag) {
    //    System.out.println("=======getAppInstallCount ===========!!! \n");
        ResultSet a = null;
        int count = 0;
        try (Connection con = getConnection()) {
            //테이블이 존재하지 않으면 생성 낄낄
            if (!DBObject.getInstance().appInstallSchemaExists(con)) {
                MyLog.i("");
                DBObject.getInstance().createAppInstallSchema(con);
            }

            Statement st = null;
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select count(*) from install");
            rs.next();
//            MyLog.i("getAppInstallCount = " + rs.getInt(1));
            count = rs.getInt(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    //hard coding
    //implement not yet!!
    private double getNetworkOwner(){return 2;}
    private double getCountry(){return 3; }
    private double getLocationInfo(){return 1;}
    private double getEmailAddr(){return 3;}
    private double getPhoneNum(){return 6;}
    private double getAccessorCount(){return 10;}
    private double getDownloadCount() {return 20;}
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        MyLog.i("");
        return DBObject.getInstance().getConnection();
    }


    /**
     * 이름 오름차순
     * @author falbb
     *
     */
    static class NameAscCompare implements Comparator<AccessData> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(AccessData arg0, AccessData arg1) {
            // TODO Auto-generated method stub
            return arg0.getM_group_name().compareTo(arg1.getM_group_name());
        }

    }

    /**
     * 이름 내림차순
     * @author falbb
     *
     */
    static class NameDescCompare implements Comparator<AccessData> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(AccessData arg0, AccessData arg1) {
            // TODO Auto-generated method stub
            return arg1.getM_group_name().compareTo(arg0.getM_group_name());
        }

    }

    /**
     * No 오름차순
     * @author falbb
     *
     */
    static class NoAscCompare implements Comparator<AccessData> {

        /**
         * 오름차순(ASC)
         */
        @Override
        public int compare(AccessData arg0, AccessData arg1) {
            // TODO Auto-generated method stub
            return arg0.getM_seq() < arg1.getM_seq() ? -1 : arg0.getM_seq() > arg1.getM_seq() ? 1:0;
        }

    }

    /**
     * No 내림차순
     * @author falbb
     *
     */
    static class NoDescCompare implements Comparator<AccessData> {

        /**
         * 내림차순(DESC)
         */
        @Override
        public int compare(AccessData arg0, AccessData arg1) {
            // TODO Auto-generated method stub
            return arg0.getM_seq() > arg1.getM_seq() ? -1 : arg0.getM_seq() < arg1.getM_seq() ? 1:0;
        }

    }
}


//=폐기처리=========================================================================
//String[] str = location.getPath().split("/");
//String temp = str[str.length - 1];
//if(temp.equals(LAYOUT_FXML_RANSOM_MAIN)){

/*
    private void setDrilldownData(final PieChart pie, PieChart.Data data, final String labelPrefix)
    {
        data.getNode().setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event) {
                try {

                    loader = new FXMLLoader(getClass().getResource("../fxml/DetailPieCharts.fxml"));
                    stage = (Stage) pie.getScene().getWindow();
                    Group gr = new Group();
                    root = loader.load();
                    gr.getChildren().addAll(pie, root);
                    scene = new Scene(gr, stage.getWidth(), stage.getHeight());
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException e) {
                    MyLog.i("e = " + e.getMessage());
                    e.printStackTrace();
                }

                pie.setData(FXCollections.observableArrayList(
                        new PieChart.Data(labelPrefix + "-1", 7),
                        new PieChart.Data(labelPrefix + "-2", 2),
                        new PieChart.Data(labelPrefix + "-3", 5),
                        new PieChart.Data(labelPrefix + "-4", 3),
                        new PieChart.Data(labelPrefix + "-5", 2)));
            }
        });
    }
    */

/*private class AddToQueue implements Runnable {
        public void run() {
            try {
                MyLog.i("run");
                // add a item of random data to queue
                ObservableList<PieChart.Data> pieCharts1 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS URL Click", getUrlClickCount()),
                        new PieChart.Data("SMS Click", 0),
                        new PieChart.Data("Ransom Infected", 0));
                ObservableList<PieChart.Data> pieCharts2 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 0),
                        new PieChart.Data("SMS Click", 0),
                        new PieChart.Data("Ransom Infected", 0));
                ObservableList<PieChart.Data> pieCharts3 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 0),
                        new PieChart.Data("SMS Click", 0),
                        new PieChart.Data("Ransom Infected", 0));
                ObservableList<PieChart.Data> pieCharts4 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 7),
                        new PieChart.Data("SMS Click", 5),
                        new PieChart.Data("Ransom Infected", 5));
                ObservableList<PieChart.Data> pieCharts5 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 3),
                        new PieChart.Data("SMS Click", 9),
                        new PieChart.Data("Ransom Infected", 9));
                ObservableList<PieChart.Data> pieCharts6 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 7),
                        new PieChart.Data("SMS Click", 4),
                        new PieChart.Data("Ransom Infected", 4));
                ObservableList<PieChart.Data> pieCharts7 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 5),
                        new PieChart.Data("SMS Click", 3),
                        new PieChart.Data("Ransom Infected", 2));
                ObservableList<PieChart.Data> pieCharts8 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 2),
                        new PieChart.Data("SMS Click", 3),
                        new PieChart.Data("Ransom Infected", 3));
                ObservableList<PieChart.Data> pieCharts9 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 7),
                        new PieChart.Data("SMS Click", 4),
                        new PieChart.Data("Ransom Infected", 4));
                ObservableList<PieChart.Data> pieCharts10 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 7),
                        new PieChart.Data("SMS Click", 3),
                        new PieChart.Data("Ransom Infected", 8));
                ObservableList<PieChart.Data> pieCharts11 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 2),
                        new PieChart.Data("SMS Click", 6),
                        new PieChart.Data("Ransom Infected", 9));
                ObservableList<PieChart.Data> pieCharts12 = FXCollections.observableArrayList(
                        new PieChart.Data("SMS received", 1),
                        new PieChart.Data("SMS Click", 2),
                        new PieChart.Data("Ransom Infected", 3));

                p1.setData(pieCharts1);

                Thread.sleep(500);
                executor.execute(this);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }*/