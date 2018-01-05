package drill.controller;

import drill.ControlledScreen;
import drill.ScreensController;
import drill.ScreensFramework;
import drill.data.*;
import drill.utils.DBObject;
import javafx.animation.TranslateTransitionBuilder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by Chan-Ju on 2015-08-23.
 */
public class DetailPieChartsController implements Initializable, ControlledScreen {

    private final int NOT_CONTAINS = 0xFFFFFFFF;
    ScreensController myController;

    SharedDataObject mSharedDataObject;
    StatisticsDataObject mStatisticsDataObject;

    ///�׷�纰 �α�����
    private ObservableList<String> group_fising_log_SFG;
    private ObservableList<String> group_fising_log_SHB;
    private ObservableList<String> group_fising_log_SHC;
    private ObservableList<String> group_fising_log_SHI;
    private ObservableList<String> group_fising_log_SHL;
    private ObservableList<String> group_fising_log_BNP;
    private ObservableList<String> group_fising_log_CAP;
    private ObservableList<String> group_fising_log_JJB;
    private ObservableList<String> group_fising_log_SAV;
    private ObservableList<String> group_fising_log_SDS;
    private ObservableList<String> group_fising_log_TAS;
    private ObservableList<String> group_fising_log_SCI;
    private ObservableList<String> group_fising_log_SSF;
    private ObservableList<String> group_fising_log_ETC;


    ///
    private int mNowViewGroup;

    @FXML
    private ListView<String> listview_group_detail_log;

    @FXML private Text title_group_name;

    @FXML private Button btn_back;

    @FXML private PieChart detail_pie;

    @FXML private TableView<TBL_Group_StatisticData> fx_tbl_group_victims;
    @FXML private TableView<TBL_Victims_Detail> fx_tbl_victims_detail;


    ///�׷�纰 ������ victims ���
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SFG = FXCollections.observableArrayList();//��������
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SHB = FXCollections.observableArrayList();//��������
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SHC = FXCollections.observableArrayList();//����ī��
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SHI = FXCollections.observableArrayList();//���ѱ�������
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SHL = FXCollections.observableArrayList();//���ѻ�����
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_BNP = FXCollections.observableArrayList();//����BNP�ĸ����ڻ���
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_CAP = FXCollections.observableArrayList();//����ĳ��Ż
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_JJB = FXCollections.observableArrayList();//��������
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SAV = FXCollections.observableArrayList();//������������
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SDS = FXCollections.observableArrayList();//���ѵ���Ÿ�ý���
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_TAS = FXCollections.observableArrayList();//���Ѿ���Ÿ��
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SCI = FXCollections.observableArrayList();//���ѽſ�����
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_SSF = FXCollections.observableArrayList();//���ѽſ�����
    private ObservableList<TBL_Group_StatisticData> mGroupVictimsData_ETC = FXCollections.observableArrayList();//������(A3,���հ�����SDS�ο�)

    //null
    private ObservableList<TBL_Victims_Detail> mNullData = FXCollections.observableArrayList();//�ƹ��͵� ���� ����Ʈ

    /////////////////
    @FXML private TextField fx_total_man; //�Ʒ� ���� �� �ο�
    //@FXML private TextField fx_urlacc_count;  //url Ŭ�� �Ѱ� , �ߺ����� ����
    //@FXML private TextField fx_exec_count; //���α׷� ���� �Ѱ�, �ߺ����� ����
    @FXML private TextField fx_safety_man; //������ �ο�
    @FXML private TextField fx_urlacc_man; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
    @FXML private TextField fx_exec_man;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
    @FXML private TextField fx_permit_man; //������ ���� ȹ�� ��
    //@FXML private TextField fx_many_times_urlacc_man; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
    //@FXML private TextField fx_many_times_exec_man;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)


    @FXML private TableColumn<TBL_Group_StatisticData, String> UserCodeColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> NameColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> PhoneNoColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> AccCountColumn;
    //@FXML private TableColumn<TBL_Group_StatisticData, String> InstallCountColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> ExecCountColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> PermitCountColumn;
    @FXML private TableColumn<TBL_Group_StatisticData, String> BlankColumn;


    @FXML private TableColumn<TBL_Victims_Detail, String> ActionDateColumn;
    @FXML private TableColumn<TBL_Victims_Detail, String> ActionStringColumn;

    //////////////////
    // ���� ��Ʈ ����Ÿ
    private PieChart.Data pie_data_SMS_SEND;//   = 2 ; // ������ �ο�
    private PieChart.Data pie_data_WEB_ACCESS;//    = 3 ; // ���ھ��� URI Ŭ�� �ο�
    private PieChart.Data pie_data_APP_INSTALL;//      = 4 ; // ���� �ٿ�ε�/Install �ο�
    private PieChart.Data pie_data_APP_EXECUTE;//   = 5 ; // ���� ���� �ο�
    private PieChart.Data pie_data_URL_ACC_COUNT;// = 6; //url Ŭ�� �Ѱ� , �ߺ����� ����
    private PieChart.Data pie_data_EXEC_COUNT;// = 7; //���α׷� ���� �Ѱ�, �ߺ����� ����
    private PieChart.Data pie_data_MANY_TIMES_URL_ACC_MAN;// = 8; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
    private PieChart.Data pie_data_MANY_TIMES_EXEC_MAN;// = 9;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)


    ObservableList<PieChart.Data> pieChartDatas_detail;

    ////////////////
    private String labelPrefix = "a";
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        mNowViewGroup = DefineValue.IT_IS_NOTHING;

        mSharedDataObject = SharedDataObject.getInstance();
        mStatisticsDataObject = StatisticsDataObject.getInstance();

        //fx_tbl_group_victims
        UserCodeColumn.setCellValueFactory(cellData -> cellData.getValue().SEQUENCE_NOProperty());
        NameColumn.setCellValueFactory(cellData -> cellData.getValue().PERSON_NAMEProperty());
        PhoneNoColumn.setCellValueFactory(cellData -> cellData.getValue().PHONE_NUMBERProperty());
        AccCountColumn.setCellValueFactory(cellData -> cellData.getValue().ACCCOUNTProperty());
        //InstallCountColumn.setCellValueFactory(cellData -> cellData.getValue().INSTALL_COUNTProperty());
        ExecCountColumn.setCellValueFactory(cellData -> cellData.getValue().INSTALL_COUNTProperty()); //inst �� ����

        PermitCountColumn.setCellValueFactory(cellData -> cellData.getValue().EXEC_COUNPropertyT()); //exec �� ������ ȹ��
        BlankColumn.setCellValueFactory(cellData -> cellData.getValue().BLANK_CONTENTProperty());

        //DownCountColumn.setCellValueFactory(cellData -> cellData.getValue().DOWN_COUNTProperty());

        //fx_tbl_victims_detail
        ActionDateColumn.setCellValueFactory(cellData -> cellData.getValue().mDateTimeProperty());
        ActionStringColumn.setCellValueFactory(cellData -> cellData.getValue().mVictimActionsProperty());


        fx_tbl_group_victims.getSelectionModel().selectedItemProperty().addListener(
              (observable, oldValue, newValue) -> showPersonDetails(newValue));


        //names = FXCollections.observableArrayList();
        ///�׷�纰 �α����� ����Ʈ �ʱ�ȭ
        group_fising_log_SFG = FXCollections.observableArrayList();
        group_fising_log_SHB = FXCollections.observableArrayList();
        group_fising_log_SHC = FXCollections.observableArrayList();
        group_fising_log_SHI = FXCollections.observableArrayList();
        group_fising_log_SHL = FXCollections.observableArrayList();
        group_fising_log_BNP = FXCollections.observableArrayList();
        group_fising_log_CAP = FXCollections.observableArrayList();
        group_fising_log_JJB = FXCollections.observableArrayList();
        group_fising_log_SAV = FXCollections.observableArrayList();
        group_fising_log_SDS = FXCollections.observableArrayList();
        group_fising_log_TAS = FXCollections.observableArrayList();
        group_fising_log_SCI = FXCollections.observableArrayList();
        group_fising_log_SSF = FXCollections.observableArrayList();
        group_fising_log_ETC = FXCollections.observableArrayList();


/*
        detail_pie.setData(FXCollections.observableArrayList(
                new PieChart.Data(labelPrefix + "-1", 7),
                new PieChart.Data(labelPrefix + "-2", 2),
                new PieChart.Data(labelPrefix + "-3", 5),
                new PieChart.Data(labelPrefix + "-4", 3),
                new PieChart.Data(labelPrefix + "-5", 2)));

*/
        pie_chart_init();

        //thread start
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true) {
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run() {
                            //System.out.println("[DEBUG][DetailPieChartsController] Thread Run!!! \n");
                            check_view_group_change();
                            display_group_detail();
                            getAPTGroupDetailTableContent(0);

                        }
                    });
                    Thread.sleep(1000);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void print_value_pie_data()
    {
        //���� ���ھ���
        //������ �׳��ϰ� ���� ���� ũ�Ⱑ ���� �ʵ��� �ٸ� �ڵ�� �ٲ����.
        for (Node node : detail_pie.lookupAll(".text.chart-pie-label")) {
            if (node instanceof Text) {
                for (PieChart.Data data : pieChartDatas_detail) {
                    if (data.getName().equals(((Text) node).getText())) {
                        ((Text) node).setText(String.format("%,.0f", data.getPieValue()));
                    }
                }
            }
        }
    }

    //���� ����Ʈ�ϱ� --- ����
    private int getVictimsActionDisplay(String aKey_SEQUENCE_NO)

    {
        //fx_tbl_victims_detail ���⿡ ����Ʈ �ϱ�

        //TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)

        //tbl_APTDETAIL(SEQ , ACTDATE, ADDRIP, PHNUM, ACTSTR)

        //System.out.println("getVictimsActionDisplay aKey_PhoneNumber = "+ aKey_PhoneNumber);
        System.out.println("getVictimsActionDisplay aKey_SEQUENCE_NO = "+ aKey_SEQUENCE_NO);

        String lSEQ;
        String lACTDATE;
        String lADDRIP;
        String lPHNUM;
        String lACTSTR;

        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        int temp_index =0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            //String sql = "select * from tbl_APTDETAIL where PHNUM = ? ";                        // sql ����
            //2015. 10. 12
            /*
            String sql_prepare = "select ap.actdate, (select codename \n" +
                    "          from tbl_syscode \n" +
                    "         where ap.actdiv = cdcode) as actstr \n" +
                    "   from tbl_apt ap\n" +
                    "  where user_seq = ? \n" +
                    "  union all\n" +
                    " select b.actdate, actstr \n" +
                    "   from tbl_aptdetail b\n" +
                    "  where phnum = (select phone_number\n" +
                    "                   from victims_list\n" +
                    "                  where sequence_no = ? ) ";                        // sql ����
            //String sql = "select * from tbl_APTDETAIL where PHNUM =\'000-0000-0001\' ";                        // sql ����
*/

            String sql_prepare = "select * from v_act_list where user_seq = ? ";


            pstmt = conn.prepareStatement(sql_prepare);                          // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            pstmt.setString(1, aKey_SEQUENCE_NO );
            //pstmt.setString(2, aKey_SEQUENCE_NO );

            ResultSet rs = pstmt.executeQuery();                                        // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.

            ObservableList<TBL_Victims_Detail> data  = FXCollections.observableArrayList();
            //data.clear();

            while(rs.next())
            {                                                        // ����� �� �྿ ���ư��鼭 �����´�.
//TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)

                //lSEQ = rs.getString("SEQ");
                lACTDATE = rs.getString("actdate");
                //lADDRIP= rs.getString("ADDRIP");
                //lPHNUM= rs.getString("PHNUM");
                lACTSTR= rs.getString("actdiv");



//print for debuging
                /*
                System.out.println("DetailPieChartsController-getVictimsActionDisplay"
                        +lSEQ+ "  "
                        +lACTDATE+ "  "
                        +lADDRIP+ "  "
                        +lPHNUM+ "  "
                        +lACTSTR);
                */

                    //������ Add
                    data.add(new TBL_Victims_Detail(lACTDATE,lACTSTR ));

            }

            fx_tbl_victims_detail.setItems(data);

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

    /////���� ����Ʈ �ϱ� ��===========================
    /**
          *
     * @param a_person the person or null
     */
    private void showPersonDetails(TBL_Group_StatisticData a_person) {

      //  System.out.println("[DetailPieChartsController] private void showPersonDetails(TBL_Group_StatisticData a_person) {");
        if (a_person != null) {

          //  System.out.println("[DetailPieChartsController] private void showPersonDetails(exist)");

            System.out.println(a_person.getSEQUENCE_NO());
            System.out.println(a_person.getPERSON_NAME());
            System.out.println(a_person.getPHONE_NUMBER());
            System.out.println(a_person.getACC_COUNT());
            System.out.println(a_person.getINSTALL_COUNT());
            System.out.println(a_person.getEXEC_COUNT());
            //getVictimsActionDisplay(a_person.getPHONE_NUMBER());
            getVictimsActionDisplay(a_person.getSEQUENCE_NO());
            // Fill the labels with info from the person object.
            //firstNameLabel.setText(person.getFirstName());
            //lastNameLabel.setText(person.getLastName());
            //streetLabel.setText(person.getStreet());
            //postalCodeLabel.setText(Integer.toString(person.getPostalCode()));
            //cityLabel.setText(person.getCity());
            //birthdayLabel.setText(DateUtil.format(person.getBirthday()));
        } else {
            System.out.println("[DetailPieChartsController] private void showPersonDetails( a_person == null) {");
            // Person is null, remove all the text.
            //firstNameLabel.setText("");
            //lastNameLabel.setText("");
            //streetLabel.setText("");
            //postalCodeLabel.setText("");
            //cityLabel.setText("");
            //birthdayLabel.setText("");
        }
    }

    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }

    @FXML
    private void handleBackButtonAction (ActionEvent event){

        mNowViewGroup = DefineValue.IT_IS_NOTHING;
        myController.setScreen(ScreensFramework.screen13ID_all_group);
    }

    //pie chart init
    private void pie_chart_init()
    {
        //1. ��Ʈ Ÿ��Ʋ
        //detail_pie.setTitle("");

        //2. ���� ��ġ
        //detail_pie.setLegendSide(Side.LEFT);


        detail_pie.setStartAngle(180);  //�������� ���� ����
        //detail_pie.setLabelLineLength(10);
        //detail_pie.setLegendSide(Side.BOTTOM); //���� ��ġ

        detail_pie.setLegendVisible(true); // ���� ���� /�Ⱥ���
        //detail_pie.setClockwise(false);   //���� ȸ�� ����


        //3. ���� �޸� �� ����/�Ⱥ��� & �� ��ġ
        detail_pie.setLabelsVisible(true);
        detail_pie.setLabelLineLength(10);

        //chart data
        pie_data_SMS_SEND  = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND, 0 ); // ������ �ο�
        pie_data_WEB_ACCESS = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS, 0 );  // ���ھ��� URI Ŭ�� �ο�
        pie_data_APP_INSTALL = new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL, 0 );    // ���� �ٿ�ε�&��ġ �ο�
        pie_data_APP_EXECUTE= new PieChart.Data(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE, 0 ); // ���� ���� �ο�

        //pie_data_URL_ACC_COUNT; //Ŭ�� �Ѱ� , �ߺ����� ����
        //pie_data_EXEC_COUNT;    //���α׷� ���� �Ѱ�, �ߺ����� ����
        //pie_data_MANY_TIMES_URL_ACC_MAN; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        //pie_data_MANY_TIMES_EXEC_MAN;    //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)

        pieChartDatas_detail = FXCollections.observableArrayList(pie_data_SMS_SEND, pie_data_WEB_ACCESS,
                pie_data_APP_INSTALL, pie_data_APP_EXECUTE);
        detail_pie.setData(pieChartDatas_detail);

        for( PieChart.Data d : pieChartDatas_detail )
        {
            d.getNode().setOnMouseEntered(new MouseHoverAnimation(d,detail_pie));
            d.getNode().setOnMouseExited(new MouseExitAnimation());
        }
    }


    ///show group content display
    private  void showScreenDetailSFG()
    {
        //title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SFG);

        int l_total_man = 0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count = 0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count = 0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man = 0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man = 0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man = 0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man = 0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SFG_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SFG_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SFG_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SFG_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SFG_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SFG_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SFG_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SFG_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SFG_MANY_TIMES_EXEC_MAN();

        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man, l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4sfg())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4sfg();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SFG.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSHB()
    {
        //title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHB);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SHB_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SHB_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SHB_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SHB_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SHB_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SHB_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SHB_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SHB_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SHB_MANY_TIMES_EXEC_MAN();

        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4shb())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4shb();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SHB.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSHC()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHC);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SHC_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SHC_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SHC_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SHC_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SHC_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SHC_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SHC_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SHC_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SHC_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4shc())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4shc();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SHC.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSHI()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHI);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SHI_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SHI_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SHI_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SHI_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SHI_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SHI_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SHI_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SHI_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SHI_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4shi())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4shi();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SHI.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSHL()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHL);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SHL_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SHL_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SHL_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SHL_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SHL_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SHL_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SHL_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SHL_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SHL_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4shl())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4shl();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SHL.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailBNP()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.BNP);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_BNP_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_BNP_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_BNP_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_BNP_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_BNP_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_BNP_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_BNP_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_BNP_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_BNP_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4bnp())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4bnp();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_BNP.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailCAP()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.CAP);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_CAP_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_CAP_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_CAP_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_CAP_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_CAP_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_CAP_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_CAP_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_CAP_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_CAP_MANY_TIMES_EXEC_MAN();

        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4cap())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4cap();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_CAP.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailJJB()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.JJB);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_JJB_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_JJB_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_JJB_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_JJB_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_JJB_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_JJB_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_JJB_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_JJB_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_JJB_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4jjb())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4jjb();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_JJB.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSAV()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SAV);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SAV_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SAV_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SAV_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SAV_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SAV_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SAV_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SAV_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SAV_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SAV_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4sav())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4sav();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SAV.add(0,lAccessData.getM_access_log());
        }
    }

    private  void showScreenDetailSDS()
    {
        //title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SDS);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SDS_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SDS_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SDS_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SDS_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SDS_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SDS_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SDS_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SDS_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SDS_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);


        while (mStatisticsDataObject.isExistAccessDataInQueue4sds())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4sds();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SDS.add(0,lAccessData.getM_access_log());
        }
    }
    private  void showScreenDetailTAS()
    {
        //  title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.TAS);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_TAS_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_TAS_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_TAS_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_TAS_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_TAS_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_TAS_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_TAS_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_TAS_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_TAS_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4tas())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4tas();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_TAS.add(0,lAccessData.getM_access_log());
        }

    }
    private  void showScreenDetailSCI()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SCI);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SCI_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SCI_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SCI_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SCI_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SCI_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SCI_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SCI_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SCI_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SCI_MANY_TIMES_EXEC_MAN();

        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4sci())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4sci();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SCI.add(0,lAccessData.getM_access_log());
        }
    }
    private  void showScreenDetailSSF()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SSF);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_SSF_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_SSF_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_SSF_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_SSF_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_SSF_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_SSF_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_SSF_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_SSF_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_SSF_MANY_TIMES_EXEC_MAN();

        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4ssf())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4ssf();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_SSF.add(0,lAccessData.getM_access_log());
        }
    }
    private void showScreenDetailETC()
    {
        // title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.ETC);
        int l_total_man =0; //�Ʒ� ���� �� �ο�
        int l_urlacc_count =0;  //url Ŭ�� �Ѱ� , �ߺ����� ����
        int l_exec_count =0; //���α׷� ���� �Ѱ�, �ߺ����� ����
        int l_urlacc_man =0; //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_exec_man =0;   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_urlacc_man =0; //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_many_times_exec_man =0;  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        int l_safety_man = 0; //������ �ο�
        int l_app_install =0;

        l_total_man = mStatisticsDataObject.get_ETC_TOTAL_MAN();
        l_safety_man =mStatisticsDataObject.get_ETC_NO_RESPONSE();
        l_urlacc_man = mStatisticsDataObject.get_ETC_WEB_ACCESS();
        l_app_install = mStatisticsDataObject.get_ETC_APP_INSTALL();
        l_exec_man = mStatisticsDataObject.get_ETC_APP_EXECUTE();
        l_urlacc_count =  mStatisticsDataObject.get_ETC_URL_ACC_COUNT();
        l_exec_count = mStatisticsDataObject.get_ETC_EXEC_COUNT();
        l_many_times_urlacc_man = mStatisticsDataObject.get_ETC_MANY_TIMES_URL_ACC_MAN();
        l_many_times_exec_man = mStatisticsDataObject.get_ETC_MANY_TIMES_EXEC_MAN();


        cout_blank_field(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man, l_safety_man, l_app_install);

        draw_pie_chart(l_total_man, l_urlacc_count, l_exec_count, l_urlacc_man,
                l_exec_man, l_many_times_urlacc_man, l_many_times_exec_man ,l_safety_man,l_app_install);

        while (mStatisticsDataObject.isExistAccessDataInQueue4etc())
        {

            AccessData lAccessData = mStatisticsDataObject.getAccessData4etc();
            //System.out.println(String.format("access data seq = %d", lAccessData.getM_seq()));
            //System.out.println("group name" + lAccessData.getM_group_name());
            //System.out.println("access log" + lAccessData.getM_access_log());

            group_fising_log_ETC.add(0,lAccessData.getM_access_log());
        }
    }



    private void draw_pie_chart(
            int a_total_man, //�Ʒ� ���� �� �ο�
            int a_urlacc_count,  //url Ŭ�� �Ѱ� , �ߺ����� ����
            int a_exec_count, //���α׷� ���� �Ѱ�, �ߺ����� ����
            int a_urlacc_man, //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_exec_man,   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_many_times_urlacc_man, //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_many_times_exec_man,  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_safety_man, //������ �ο�
            int a_app_install
    )
    {
        //pie name setting

        pie_data_SMS_SEND.setName(String.format("%d", a_total_man));
        pie_data_WEB_ACCESS.setName(String.format("%d", a_urlacc_man));
        pie_data_APP_INSTALL.setName(String.format("%d", a_app_install));
        pie_data_APP_EXECUTE.setName(String.format("%d", a_exec_man));

        pie_data_SMS_SEND.setName(DefineValue.SHINHAN_APT_STATE.S_SMS_SEND);
        pie_data_WEB_ACCESS.setName(DefineValue.SHINHAN_APT_STATE.S_WEB_ACCESS);
        pie_data_APP_INSTALL.setName(DefineValue.SHINHAN_APT_STATE.S_APP_INSTALL);
        pie_data_APP_EXECUTE.setName(DefineValue.SHINHAN_APT_STATE.S_APP_EXECUTE);


        //chart data setting
        pie_data_SMS_SEND.setPieValue(a_total_man);
        pie_data_WEB_ACCESS.setPieValue(a_urlacc_man);
        pie_data_APP_INSTALL.setPieValue(a_app_install);
        pie_data_APP_EXECUTE.setPieValue(a_exec_man);

        pieChartDatas_detail = FXCollections.observableArrayList(pie_data_SMS_SEND, pie_data_WEB_ACCESS,
                pie_data_APP_INSTALL, pie_data_APP_EXECUTE);

        print_value_pie_data();

        //System.out.println(String.format("[Debug][draw_pie_chart] s = %d, u = %d, i = %d, e = %d"  ,a_safety_man,a_urlacc_man,a_app_install,a_exec_man));
    }

    private void cout_blank_field(
            int a_total_man, //�Ʒ� ���� �� �ο�
            int a_urlacc_count,  //url Ŭ�� �Ѱ� , �ߺ����� ����
            int a_exec_count, //���α׷� ���� �Ѱ�, �ߺ����� ����
            int a_urlacc_man, //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_exec_man,   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_many_times_urlacc_man, //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_many_times_exec_man,  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
            int a_safety_man, //������ �ο�
            int a_inst_man

    )
    {
        fx_total_man.setText(String.format("%d",a_total_man)); //�Ʒ� ���� �� �ο�
        //fx_urlacc_count.setText(String.format("%d",a_urlacc_count));  //url Ŭ�� �Ѱ� , �ߺ����� ����
        //fx_exec_count.setText(String.format("%d", a_exec_count)); //���α׷� ���� �Ѱ�, �ߺ����� ����
        fx_urlacc_man.setText(String.format("%d", a_urlacc_man)); //url ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        fx_exec_man.setText(String.format("%d", a_inst_man));   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        fx_permit_man.setText(String.format("%d", a_exec_man));   //���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        //fx_many_times_urlacc_man.setText(String.format("%d", a_many_times_urlacc_man)); //url ���� 2���̻� �ο� �� (�ߺ����� �� ī��Ʈ)
        //fx_many_times_exec_man.setText(String.format("%d",a_many_times_exec_man));  // ���� ���� �ο� �� (�ߺ����� �� ī��Ʈ)
        fx_safety_man.setText(String.format("%d", a_safety_man));  //������ �ο�
    }

    private void display_group_detail()
    {
        //System.out.println("[DEBUG][DetailPieChartsController] display_group_detail() \n");
        switch (mNowViewGroup)
        {
            case DefineValue.SHINHAN_GROUP_CODE.SFG: showScreenDetailSFG(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SHB: showScreenDetailSHB(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SHC: showScreenDetailSHC(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SHI: showScreenDetailSHI(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SHL: showScreenDetailSHL(); break;
            case DefineValue.SHINHAN_GROUP_CODE.BNP: showScreenDetailBNP(); break;
            case DefineValue.SHINHAN_GROUP_CODE.CAP: showScreenDetailCAP(); break;
            case DefineValue.SHINHAN_GROUP_CODE.JJB: showScreenDetailJJB(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SAV: showScreenDetailSAV(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SDS: showScreenDetailSDS(); break;
            case DefineValue.SHINHAN_GROUP_CODE.TAS: showScreenDetailTAS(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SCI: showScreenDetailSCI(); break;
            case DefineValue.SHINHAN_GROUP_CODE.SSF: showScreenDetailSSF(); break;
            case DefineValue.SHINHAN_GROUP_CODE.ETC: showScreenDetailETC(); break;
            default: break;
        }
    }

    private void check_view_group_change()
    {

        //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() \n");
        int lselect_group = mSharedDataObject.getSelectGroupView();

        if(lselect_group != mNowViewGroup)
        {
            mNowViewGroup = lselect_group;
            fx_tbl_victims_detail.setItems(mNullData);
          //  System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() lselect_group != mNowViewGroup \n");
            switch (mNowViewGroup) {
                case DefineValue.SHINHAN_GROUP_CODE.SFG:
                    initScreenDetailSFG();
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SFG:\n");
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHB:
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SHB\n");
                    initScreenDetailSHB();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHC:
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SHC\n");
                    initScreenDetailSHC();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHI:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SHI\n");
                    initScreenDetailSHI();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHL:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SHL \n");
                    initScreenDetailSHL();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.BNP:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() BNP\n");
                    initScreenDetailBNP();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.CAP:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() CAP \n");
                    initScreenDetailCAP();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.JJB:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() JJB\n");
                    initScreenDetailJJB();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SAV:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SAV\n");
                    initScreenDetailSAV();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SDS:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SDS\n");
                    initScreenDetailSDS();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.TAS:
                    //System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() TAS\n");
                    initScreenDetailTAS();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SCI:
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SCI\n");
                    initScreenDetailSCI();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SSF:
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() SSF\n");
                    initScreenDetailSSF();
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.ETC:
                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change() ETC\n");
                    initScreenDetailETC();
                    break;
                default:

                   // System.out.println("[DEBUG][DetailPieChartsController] check_view_group_change()  default:\n");
                    break;
            }
        }

    }

    ////////////////////////
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        //MyLog.i("");
        return DBObject.getInstance().getConnection();
    }
    private int getAPTGroupDetailTableContent(int view_group)
    {
        //TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)
        String lSEQUENCE_NO;
        String lPERSON_NAME;
        String lPHONE_NUMBER;
        String lACCCOUNT;
        String linstallcount;
        String lexeccount;
        String ldowncount;
        String lcustomer_code;
        String lpermitcount;

        String lNowViewGroupString;

        switch (mNowViewGroup)
        {

                case DefineValue.SHINHAN_GROUP_CODE.SFG:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SFG; // = "SFG"; //��������
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHB:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SHB; // = "SHB"; //��������
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHC:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SHC; // = "SHC"; //����ī��
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHI:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SHI; // = "SHI"; //���ѱ�������

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SHL:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SHL; // = "SHL"; //���ѻ�����

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.BNP:
                    lNowViewGroupString = DefineValue.GROUPTYPE_BNP; // = "BNP"; //����BNP�ĸ����ڻ���
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.CAP:
                    lNowViewGroupString = DefineValue.GROUPTYPE_CAP; // = "CAP"; //����ĳ��Ż

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.JJB:
                    lNowViewGroupString = DefineValue.GROUPTYPE_JJB; // = "JJB"; //��������

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SAV:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SAV; // = "SAV"; //������������

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SDS:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SDS; // = "SDS"; //���ѵ���Ÿ�ý���

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.TAS:
                    lNowViewGroupString = DefineValue.GROUPTYPE_TAS; // = "TAS"; //���Ѿ���Ÿ��
                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SCI:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SCI; // = "SCI"; //���ѽſ�����

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.SSF:
                    lNowViewGroupString = DefineValue.GROUPTYPE_SSF; // = "SSF"; // (���� ��Į�� �Ŀ�̼�)�����������

                    break;
                case DefineValue.SHINHAN_GROUP_CODE.ETC:
                    lNowViewGroupString = DefineValue.GROUPTYPE_ETC; // = "ETC"; //������(A3,���հ�����SDS�ο�)
                    break;
                default:
                    return 0;



        }


        ResultSet a = null;
        PreparedStatement pstmt = null;
        int count = 0;
        int temp_index =0;
        try (Connection conn = getConnection())
        {
            //���̺��� �������� ������ ���� ����

            //Statement st = null;
            //st = conn.createStatement();
            //ResultSet rs = st.executeQuery("select * from TBL_APT where SEQ >=4 ");
            //rs.next();

            String sql = "select sequence_no, person_name, phone_number , " +
                    "(select count(*) from tbl_apt where user_seq = vic.sequence_no and actdiv = 'urlacc') as acccount , " +
                    "(select count(*) from tbl_apt where user_seq = vic.sequence_no and actdiv = 'down') as installcount , " +
                    "(select count(*) from tbl_apt where user_seq = vic.sequence_no and actdiv = 'exec') as execcount  " +
                    "from victims_list vic ";                        // sql ����

            String sql_query_sentence_old = "select sequence_no\n" +
                    "     , customer_code\n" +
                    "     , person_name\n" +
                    "     , phone_number\n" +
                    "     , (select count (*) from tbl_apt where sequence_no = user_seq and actdiv = 'urlacc') as urlacc\n" +
                    "     , (select count (*) from tbl_apt where sequence_no = user_seq and actdiv = 'down') as down\n" +
                    "     , (select count (*) from tbl_apt where sequence_no = user_seq and actdiv = 'inst') as inst\n" +
                    "     , (select count (*) from tbl_apt where sequence_no = user_seq and actdiv = 'exec') as execcount\n" +
                    " from victims_list\n" +
                    "where customer_code = ? ";


            String sql_query_sentence = "select * from V_USER_CNT where CUSTOMER_CODE = ? ";// --���� ȭ�鿡�� ������ ī��Ʈ

            pstmt = conn.prepareStatement(sql_query_sentence);                          // prepareStatement���� �ش� sql�� �̸� �������Ѵ�.
            //pstmt.setString(1, String.valueOf(seq));
            pstmt.setString(1,lNowViewGroupString );

            ResultSet rs = pstmt.executeQuery();                                        // ������ �����ϰ� ����� ResultSet ��ü�� ��´�.


            switch(mNowViewGroup)
            {
                case  DefineValue.SHINHAN_GROUP_CODE.SFG:
                    break;
            }
            ObservableList<TBL_Group_StatisticData> data  = fx_tbl_group_victims.getItems();
            //data.clear();

            while(rs.next())
            {                                                        // ����� �� �྿ ���ư��鼭 �����´�.
//TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)

                lSEQUENCE_NO = rs.getString("sequence_no");
                lPERSON_NAME = rs.getString("person_name");
                lPHONE_NUMBER= rs.getString("phone_number");


               lACCCOUNT= rs.getString("urlacc");
               ldowncount= rs.getString("down");
               linstallcount= rs.getString("inst");
               lexeccount = rs.getString("execcount");

               //lcustomer_code = rs.getString("customer_code");
                lcustomer_code = rs.getString("phtype");


/*
//print for debuging
                System.out.println("DetailPieChartsController-getAPTGroupDetailTableContent"
                        +lSEQUENCE_NO+ "  "
                        +lPERSON_NAME+ "  "
                        +lPHONE_NUMBER+ "  "
                        +lACCCOUNT+ "  "
                        +linstallcount+ "  "
                        +lexeccount);
*/
                //������ Edit
                temp_index = NOT_CONTAINS;
                temp_index = findUserCode(lSEQUENCE_NO);
                if( temp_index != NOT_CONTAINS)
                {
                    //System.out.println("Edit TableVidw Item");
                   // verify_person_display(temp_index); //for debug
                    edit_victim_data(temp_index,
                            lSEQUENCE_NO,
                            lPERSON_NAME,
                            lPHONE_NUMBER,
                            lACCCOUNT,
                            linstallcount,
                            lexeccount,
                            ldowncount,
                            lcustomer_code
                            ); //for process, real use code

                }
                else
                {
                 //   System.out.println("Add TableVidw Item");
                    //������ Add
                    data.add(new TBL_Group_StatisticData(
                            lSEQUENCE_NO,
                            lPERSON_NAME,
                            lPHONE_NUMBER,
                            lACCCOUNT,
                            linstallcount,
                            lexeccount,
                            ldowncount,
                            lcustomer_code

                    ));
                }
            }

            //fx_tbl_group_victims.setItems(data);

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

    //////////////////////
    //find

    private void edit_victim_data(int index,
                                  String aSEQUENCE_NO,
                                  String aPERSON_NAME,
                                  String aPHONE_NUMBER,
                                  String aACCCOUNT,
                                  String ainstallcount,
                                  String aexeccount,
                                  String adowncount,
                                  String agroup_code)
    {

        if(index == NOT_CONTAINS) {
            System.out.println("Not Contains item");
            return;
        }
        ObservableList<TBL_Group_StatisticData> personData = fx_tbl_group_victims.getItems();
        TBL_Group_StatisticData lperson = personData.get(index);

        //lperson.setSEQUENCE_NO();  //�� 3���� ���� �ٲ�� �ȵ�.
        //lperson.setPERSON_NAME();
        //lperson.setPHONE_NUMBER();
        lperson.setACC_COUNT(aACCCOUNT);
        lperson.setINSTALL_COUNT(ainstallcount);
        lperson.setEXEC_COUNT(aexeccount);
        lperson.setDOWN_COUNT(adowncount);
        lperson.setBLANK_CONTENT(agroup_code);
    }

    private void verify_person_display(int index)
    {
        if(index == NOT_CONTAINS) {
            System.out.println("Not Contains item");
            return;
        }
        ObservableList<TBL_Group_StatisticData> personData = fx_tbl_group_victims.getItems();
        TBL_Group_StatisticData lperson = personData.get(index);

        System.out.println(lperson.getSEQUENCE_NO());
        System.out.println(lperson.getPERSON_NAME());
        System.out.println(lperson.getPHONE_NUMBER());
        System.out.println(lperson.getACC_COUNT());
        System.out.println(lperson.getINSTALL_COUNT());
        System.out.println(lperson.getEXEC_COUNT());
    }

    private int findUserCode(String USER_CODE)
    {

        ObservableList<TBL_Group_StatisticData> personData = fx_tbl_group_victims.getItems();
        for (TBL_Group_StatisticData person : personData)
        {
            if (person.getSEQUENCE_NO().equals(USER_CODE))
            {
                return personData.indexOf(person);
            }
        }
        return NOT_CONTAINS;
    }


    /////////////////////////

//view init
    private  void  initScreenDetailSFG()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SFG;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SFG);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SFG);
        listview_group_detail_log.setItems(group_fising_log_SFG);
    }

    private void initScreenDetailSHB()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SHB ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHB);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SHB);
        listview_group_detail_log.setItems(group_fising_log_SHB);
    }

    private void initScreenDetailSHC()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SHC ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHC);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SHC);
        listview_group_detail_log.setItems(group_fising_log_SHC);
    }

    private void initScreenDetailSHI()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SHI ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHI);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SHI);
        listview_group_detail_log.setItems(group_fising_log_SHI);
    }

    private void initScreenDetailSHL()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SHL ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SHL);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SHL);
        listview_group_detail_log.setItems(group_fising_log_SHL);
    }

    private void initScreenDetailBNP()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.BNP ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.BNP);
        fx_tbl_group_victims.setItems(mGroupVictimsData_BNP);
        listview_group_detail_log.setItems(group_fising_log_BNP);
    }

    private void initScreenDetailCAP()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.CAP ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.CAP);
        fx_tbl_group_victims.setItems(mGroupVictimsData_CAP);
        listview_group_detail_log.setItems(group_fising_log_CAP);
    }

    private void initScreenDetailJJB()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.JJB ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.JJB);
        fx_tbl_group_victims.setItems(mGroupVictimsData_JJB);
        listview_group_detail_log.setItems(group_fising_log_JJB);
    }

    private void initScreenDetailSAV()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SAV ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SAV);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SAV);
        listview_group_detail_log.setItems(group_fising_log_SAV);
    }

    private void initScreenDetailSDS()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SDS ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SDS);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SDS);
        listview_group_detail_log.setItems(group_fising_log_SDS);
    }
    private void initScreenDetailTAS()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.TAS ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.TAS);
        fx_tbl_group_victims.setItems(mGroupVictimsData_TAS);
        listview_group_detail_log.setItems(group_fising_log_TAS);
    }
    private void initScreenDetailSCI()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SCI ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SCI);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SCI);
        listview_group_detail_log.setItems(group_fising_log_SCI);
    }
    private void initScreenDetailSSF()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.SSF ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.SSF);
        fx_tbl_group_victims.setItems(mGroupVictimsData_SSF);
        listview_group_detail_log.setItems(group_fising_log_SSF);
    }
    private void initScreenDetailETC()
    {
        mNowViewGroup = DefineValue.SHINHAN_GROUP_CODE.ETC ;
        title_group_name.setText(DefineValue.SHINHAN_GROUP_NAME.ETC);
        fx_tbl_group_victims.setItems(mGroupVictimsData_ETC);
        listview_group_detail_log.setItems(group_fising_log_ETC);
    }

    ///////////���ϸ��̼�

    static class MouseHoverAnimation implements EventHandler<MouseEvent> {
        static final Duration ANIMATION_DURATION = new Duration(500);
        static final double ANIMATION_DISTANCE = 0.15;
        private double cos;
        private double sin;
        private PieChart chart;

        public MouseHoverAnimation(PieChart.Data d, PieChart chart) {
            this.chart = chart;
            double start = 0;
            double angle = calcAngle(d);
            for( PieChart.Data tmp : chart.getData() ) {
                if( tmp == d ) {
                    break;
                }
                start += calcAngle(tmp);
            }

            cos = Math.cos(Math.toRadians(0 - start - angle / 2));
            sin = Math.sin(Math.toRadians(0 - start - angle / 2));
        }

        @Override
        public void handle(MouseEvent arg0) {
            Node n = (Node) arg0.getSource();

            double minX = Double.MAX_VALUE;
            double maxX = Double.MAX_VALUE * -1;

            for( PieChart.Data d : chart.getData() ) {
                minX = Math.min(minX, d.getNode().getBoundsInParent().getMinX());
                maxX = Math.max(maxX, d.getNode().getBoundsInParent().getMaxX());
            }

            System.out.print("MouseHoverAnimation(PieChart.Data d, PieChart chart) {");
            double radius = maxX - minX;
            TranslateTransitionBuilder.create().toX((radius *  ANIMATION_DISTANCE) * cos).toY((radius *  ANIMATION_DISTANCE) * sin).duration(ANIMATION_DURATION).node(n).build().play();
        }

        private static double calcAngle(PieChart.Data d) {
            double total = 0;
            for( PieChart.Data tmp : d.getChart().getData() ) {
                total += tmp.getPieValue();
            }

            return 360 * (d.getPieValue() / total);
        }
    }

    static class MouseExitAnimation implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            TranslateTransitionBuilder.create().toX(0).toY(0).duration(new Duration(500)).node((Node) event.getSource()).build().play();
        }
    }
}


/*
    @FXML
    private void handleButtonAction(ActionEvent event) throws IOException {
        MyLog.i("");
        if(event.getSource()==btn_back){
            //get reference to the button's stage
            stage=(Stage) btn_back.getScene().getWindow();
            //load up OTHER FXML document
            root = FXMLLoader.load(getClass().getResource("../fxml/all_group_display.fxml"));

        } else{
        }
        //create a new scene with root and set the stage
        scene = new Scene(root, stage.getWidth(), stage.getHeight());
        stage.setScene(scene);
        stage.show();
    }
    */