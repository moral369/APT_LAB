package drill;

import drill.Login.LoginDialogController;
import drill.data.DefineValue;
import drill.data.SharedDataObject;
import drill.utils.MyLog;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

/*
*2015. 08. 30
* Shinhan 금융그룹 APT 훈련 관련 UI 작성
* 메인함수 있는 부분
*
*
*/


public class ScreensFramework extends Application {

    private Stage primaryStage;

    //member variable
    SharedDataObject mSharedDataObject;

    //screen

    //1.group detail
    public static String screen0ID_group_detail = "group_detail_screen";
    public static String screen0File = "/drill/fxml/DetailPieCharts.fxml";


    //여기까지 아직 화면 구현안함.

    //전체 그룹 한번에 보여주는 화면
    public static String screen13ID_all_group = "all_group_window";
    public static String screen13File = "/drill/fxml/AllGroupPie.fxml";

    //테이블보여주는 화면
    public static String screen14ID_apttable = "apt_table_screen";
    public static String screen14File = "/drill/fxml/ScreenAPTtable.fxml";

    //훈련정보 셋팅
    public static String screen15ID_aptsetting = "apt_setting_screen";
    public static String screen15File = "/drill/fxml/SettingScreen.fxml";


    private ParserObject mParserObject;
    //main function
    public static void main(String[] args) {
        MyLog.i("");


        launch(args);
    }


    private void init(Stage primaryStage) throws Exception {

        MyLog.i("");

        mParserObject = ParserObject.getInstance();
        mSharedDataObject = SharedDataObject .getInstance();

        //for MultiScreen , sub screen
        ScreensController mainContainer = new ScreensController();

        //그룹사별 디테일 화면
        mainContainer.loadScreen(ScreensFramework.screen0ID_group_detail, ScreensFramework.screen0File);//1.group detail



        //전체 그룹사 화면
        mainContainer.loadScreen(ScreensFramework.screen13ID_all_group, ScreensFramework.screen13File);

        //apt database table show
        mainContainer.loadScreen(ScreensFramework.screen14ID_apttable, ScreensFramework.screen14File);

        //setting view
        mainContainer.loadScreen(ScreensFramework.screen15ID_aptsetting, ScreensFramework.screen15File);



        mainContainer.setScreen(ScreensFramework.screen13ID_all_group);

        Group root = new Group();
        //for menubar
        VBox root_menu_box = new VBox();

        MenuBar menuBar = new MenuBar();
        //---Menu View
        Menu menuView = new Menu("View");

        MenuItem intro = new MenuItem("Intro");
        intro.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mainContainer.setScreen(ScreensFramework.screen13ID_all_group);
            }
        });



        MenuItem clear = new MenuItem("Clear");
        //clear.setAccelerator(KeyCombination.keyCombination("Ctrl+X"));
        clear.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            }
        });

        MenuItem dbview = new MenuItem("DBView");
        dbview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mainContainer.setScreen(ScreensFramework.screen14ID_apttable);

            }
        });

        MenuItem exit = new MenuItem("Exit");
        exit.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                System.exit(0);
            }
        });

        //=================================================

        MenuItem sfgview = new MenuItem("1.SFGView");
        sfgview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {

                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SFG);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem shbview = new MenuItem("2.SHBView");
        shbview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHB);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem shcview = new MenuItem("3.SHCView");
        shcview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHC);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem shiview = new MenuItem("4.SHIView");
        shiview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHI);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem shlview = new MenuItem("5.SHLView");
        shlview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SHL);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem bnpview = new MenuItem("6.BNPView");
        bnpview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.BNP);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem capview = new MenuItem("7.CAPView");
        capview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.CAP);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem jjbview = new MenuItem("8.JJBView");
        jjbview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.JJB);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem savview = new MenuItem("9.SAVView");
        savview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SAV);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem sdsview = new MenuItem("10.SDSView");
        sdsview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SDS);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem tasview = new MenuItem("11.TASView");
        tasview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.TAS);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });
        MenuItem sciview = new MenuItem("12.SCIView");
        sciview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.SCI);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });

        MenuItem etcview = new MenuItem("13.ETCView");
        etcview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mSharedDataObject.setSelectGroupView(DefineValue.SHINHAN_GROUP_CODE.ETC);
                mainContainer.setScreen(ScreensFramework.screen0ID_group_detail);

            }
        });

        menuView.getItems().addAll(intro, dbview,exit, new SeparatorMenuItem(),
        sfgview, shbview, shcview, shiview, shlview, bnpview,
        capview, jjbview, savview, sdsview, tasview, sciview,etcview);

        //=================================================
        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        MenuItem settingview = new MenuItem("Setting");
        settingview.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                mainContainer.setScreen(ScreensFramework.screen15ID_aptsetting);

            }
        });

        menuEdit.getItems().addAll(settingview);


        // --- Menu View
        //Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(menuView, menuEdit);
        //((VBox) root_scene.getRoot()).getChildren().addAll(menuBar);

        BorderPane root_layout = new BorderPane();
        //vbox.getChildren().addAll(menuBar,mainContainer);
        root_layout.setTop(menuBar);
        root_layout.setCenter(mainContainer);
        //root.getChildren().addAll(root_menu_box,mainContainer);
        //root.getChildren().addAll(vbox);

        //display screen size를 구하기 위한 부분
        javafx.geometry.Rectangle2D r = Screen.getPrimary().getVisualBounds();
        MyLog.i("" + r.getWidth() + "  " + r.getHeight());

        Scene scene = new Scene(root_layout, r.getWidth(), r.getHeight());
        primaryStage.setScene(scene);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Shinhan Group APT DashBoard");

        init(this.primaryStage);



        boolean is_correct_user = this.showLoginDialog();
        if (is_correct_user)
        {
            primaryStage.show();
        }
        else
        {
            System.exit(0);
        }
    }


    public boolean showLoginDialog() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ScreensFramework.class.getResource("/drill/Login/Login.fxml"));
            GridPane page = (GridPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Shinhan Group APT");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page, 300, 275);
            dialogStage.setScene(scene);


            // Set the person into the controller.
            LoginDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            //controller.setPerson(person);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isCorrectUser();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}


//=폐기처분============================================
//아래 화면은 아직 구현안함.
//1.SFG
//public static String screen1ID_sfg_detail = "sfg_detail_screen";
//public static String screen1File = "/drill/fxml/DetailPieCharts.fxml";

//2.SHB
//public static String screen2ID_shb_detail = "shb_detail_screen";
//public static String screen2File = "/drill/fxml/DetailPieCharts.fxml";

//3.SHC
//public static String screen3ID_shc_detail = "shc_detail_screen";
//public static String screen3File = "/drill/fxml/DetailPieCharts.fxml";

//4.SHI
//public static String screen4ID_shi_detail = "shi_detail_screen";
//public static String screen4File = "/drill/fxml/DetailPieCharts.fxml";

//5.SHL
//public static String screen5ID_shl_detail = "shl_detail_screen";
//public static String screen5File = "/drill/fxml/DetailPieCharts.fxml";

//6.BNP
//public static String screen6ID_bnp_detail = "bnp_detail_screen";
//public static String screen6File = "/drill/fxml/DetailPieCharts.fxml";

//7.CAP
//public static String screen7ID_cap_detail = "cap_detail_screen";
//public static String screen7File = "/drill/fxml/DetailPieCharts.fxml";

//8.JJB
//public static String screen8ID_jjb_detail = "jjb_detail_screen";
//public static String screen8File = "/drill/fxml/DetailPieCharts.fxml";

//9.SAV
//public static String screen9ID_sav_detail = "sav_detail_screen";
//public static String screen9File = "/drill/fxml/DetailPieCharts.fxml";

//10.SDS
//public static String screen10ID_sds_detail = "sds_detail_screen";
//public static String screen10File = "/drill/fxml/DetailPieCharts.fxml";

//11.TAS
//public static String screen11ID_tas_detail = "tas_detail_screen";
//public static String screen11File = "/drill/fxml/DetailPieCharts.fxml";

//12.SCI
//public static String screen12ID_sci_detail = "sci_detail_screen";
//public static String screen12File = "/drill/fxml/DetailPieCharts.fxml";

//16.ETC
//public static String screen16ID_etc_detail = "etc_detail_screen";
//public static String screen16File = "/drill/fxml/DetailPieCharts.fxml";



//mainContainer.loadScreen(ScreensFramework.screen0ID_sfg_detail, ScreensFramework.screen1File);//1.SFG
//mainContainer.loadScreen(ScreensFramework.screen2ID_shb_detail, ScreensFramework.screen2File);//2.SHB
//mainContainer.loadScreen(ScreensFramework.screen3ID_shc_detail, ScreensFramework.screen3File);//3.SHC
//mainContainer.loadScreen(ScreensFramework.screen4ID_shi_detail, ScreensFramework.screen4File);//4.SHI
//mainContainer.loadScreen(ScreensFramework.screen5ID_shl_detail, ScreensFramework.screen5File);//5.SHL
//mainContainer.loadScreen(ScreensFramework.screen6ID_bnp_detail, ScreensFramework.screen6File);//6.BNP
//mainContainer.loadScreen(ScreensFramework.screen7ID_cap_detail, ScreensFramework.screen7File);//7.CAP
//mainContainer.loadScreen(ScreensFramework.screen8ID_jjb_detail, ScreensFramework.screen8File);//8.JJB
//mainContainer.loadScreen(ScreensFramework.screen9ID_sav_detail, ScreensFramework.screen9File);//9.SAV
//mainContainer.loadScreen(ScreensFramework.screen10ID_sds_detail, ScreensFramework.screen10File);//10.SDS
//mainContainer.loadScreen(ScreensFramework.screen11ID_tas_detail, ScreensFramework.screen11File);//11.TAS
//mainContainer.loadScreen(ScreensFramework.screen12ID_sci_detail, ScreensFramework.screen12File);//12.SCI

//SmishingTabController controller;

//Parent root = FXMLLoader.load(getClass().getResource("/drill/fxml/smishingTest.fxml"));
//FXMLLoader loader = new FXMLLoader(getClass().getResource("/drill/fxml/smishingTest.fxml"));
//Parent root = loader.load();
//controller = loader.getController();
//controller.setStage(primaryStage);

//public static String screen3ID = "screen3";
//public static String screen3File = "Screen3.fxml";

//public static String screen1ID = "main";
//public static String screen1File = "/drill/fxml/Screen1.fxml";
//public static String screen2ID = "screen2";
//public static String screen2File = "/drill/fxml/Screen2.fxml";
//public static String screen3ID = "screen3";
//public static String screen3File = "/drill/fxml/Screen3.fxml";

//mainContainer.loadScreen(ScreensFramework.screen1ID, ScreensFramework.screen1File);
//mainContainer.loadScreen(ScreensFramework.screen2ID, ScreensFramework.screen2File);
//mainContainer.loadScreen(ScreensFramework.screen3ID, ScreensFramework.screen3File);

