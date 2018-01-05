package drill.controller;

import drill.cache.ResultSource;
import drill.utils.MyLog;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.ContextMenuEvent;
import javafx.util.Callback;

import javax.xml.ws.handler.Handler;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;

/**
 * Created by cross on 2015-06-23.
 */
public class VulsimController implements Initializable {

    @FXML
    private ListView<String> listview1;
    @FXML
    private ListView<String> listview2;
    @FXML
    private TextArea ta_responseCode;
    @FXML
    private TextArea ta_rawData;
    @FXML
    private TextField input_parameter;
    @FXML
    private TextField input_url;
    @FXML
    private TextField input_channel;
    @FXML
    private ListView<ResultSource> listview3;

    String DB_USER    = "c##moral"; // DB USER명
    String DB_PASSWORD = "dufwjd"; // 패스워드
    String DB_URL = "jdbc:oracle:thin:@a3security.iptime.org:1521:orcl";

    @FXML
    public void handle_btn_load(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning");
        alert.setContentText("172");
        setListView2(listview1.getSelectionModel().getSelectedItem());
    }
    @FXML
    public void handle_btn_inputUrl(ActionEvent actionEvent) throws IOException {
        MyLog.i("");
        String url = input_url.getText().toString();
        String[] splitString = url.split(",");
        for(String temp : splitString) {
            MyLog.i("split = "+temp);
            insertURLAddressToDatabase(listview1.getSelectionModel().getSelectedItem(), temp);
        }
    }

    @FXML
    public void handle_btn_input_Channel(ActionEvent actionEvent) throws IOException {
        String channel = input_channel.getText().toString();
//        createTableToDatabase(channel);
    }

    private void createTableToDatabase(String channel) {
        Statement st = null;
        try (Connection con = getConnection()) {
            if (!schemaExists(con)) {
                MyLog.i("");
                createSchema(con);
                populateDatabase(con);
            }
            st = con.createStatement();

            String table = "create table "+channel+"(url varchar2(100) NOT NULL)";
            MyLog.i("table = "+table);
            st.executeUpdate(table);
            fetchSavedListFromDatabaseToListview();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void insertURLAddressToDatabase(String selectedItem, String url) {
        Statement st = null;
        try (Connection con = getConnection()) {
            MyLog.i("createSchema");
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }
            st = con.createStatement();

            String table = "insert into "+selectedItem+" values('"+url+"')";
            MyLog.i("insert 구문 = " + table);
            st.executeUpdate(table);
            setListView2(listview1.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteURLAddressFromDatabase(String selectedItem, String selectedUrl) {
        Statement st = null;
        try (Connection con = getConnection()) {
            MyLog.i("createSchema");
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }
            st = con.createStatement();
            String table = "delete from "+selectedItem+" where url = '"+selectedUrl+"'";
            st.executeUpdate(table);
            setListView2(listview1.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    ObservableList<String> currentlyKeepingURLList;
    private void setListView2(String selectedItem) {
        try (Connection con = getConnection()) {
            //테이블이 존재하지 않으면 생성 낄낄
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }

            currentlyKeepingURLList = queryUrlList(con, selectedItem);
            listview2.setItems(queryUrlList(con, selectedItem));

            listview2.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    deleteURLAddressFromDatabase(listview1.getSelectionModel().getSelectedItem(), listview2.getSelectionModel().getSelectedItem());
                }
            });

            listview2.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    ListCell<String> cell = new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item != null) {
                                setText(item);
                            }
                        }
                    };
                    return cell;
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Warning");
            Optional<ButtonType> result = alert.showAndWait();
            alert.setContentText("172"+result);
            e.printStackTrace();
        }
    }



    @FXML
    public void handle_btn_start_ButtonAction(ActionEvent actionEvent) throws IOException {
        listview3.getItems().clear();
        String parameter = input_parameter.getText().toString();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Warning");
        alert.setContentText("do not use this program for malicious reasons.");
        Optional<ButtonType> result = alert.showAndWait();
        AsyncHttpTask httpTask = new AsyncHttpTask();
        if(result.get() == ButtonType.OK) {

            for(int i = 0; i<currentlyKeepingURLList.size(); i++) {
                httpTask.Task(currentlyKeepingURLList.get(i) + parameter);

            }
        } else {
        }
    }

    private ObservableList<ResultSource> listData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MyLog.i("");
        fetchSavedListFromDatabaseToListview();
        listData = FXCollections.observableArrayList();
        listview3.setItems(listData);
        listview3.setCellFactory(new Callback<ListView<ResultSource>, ListCell<ResultSource>>() {
            @Override
            public ListCell<ResultSource> call(ListView<ResultSource> param) {
                ListCell<ResultSource> cell = new ListCell<ResultSource>() {
                    @Override
                    protected void updateItem(ResultSource item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            switch (item.getResultCode()) {
                                case SERVER_RESPONSE_CODE_0:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_0 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_100:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_100 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_101:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_101 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_200:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_200 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_201:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_201 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_202:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_201 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_203:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_203 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_204:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_204 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_205:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_205 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_206:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_206 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_300:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_300 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_301:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_301 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_302:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_302 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_303:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_303 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_304:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_304 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_305:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_305 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_306:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_306 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_307:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_307 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_400:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_400 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_401:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_401 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_402:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_402 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_403:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_403 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_404:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_404 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_405:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_405 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_406:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_406 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_407:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_407 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_408:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_408 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_409:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_409 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_410:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_410 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_411:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_411 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_412:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_412 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_413:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_413 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_414:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_414 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_415:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_415 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_416:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_416 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_417:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_417 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_500:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_500 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_501:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_501 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_502:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_502 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_503:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_503 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_504:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_504 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_505:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_505 + "  " + item.getUrl());
                                    break;
                                case SERVER_RESPONSE_CODE_1000:
                                    setText(item.getResultCode() + "  " + STATUS_CODE_DEFINITION_ERROR + "  " + item.getUrl());
                                    break;
                            }
                        }
                    }
                };
                return cell;
            }
        });

        listview3.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ResultSource>() {
            @Override
            public void changed(ObservableValue<? extends ResultSource> observable, ResultSource oldValue, ResultSource newValue) {
                ta_responseCode.clear();
                ta_responseCode.setWrapText(true);
                ta_responseCode.appendText(newValue.getResultSource());
                ta_rawData.clear();
                ta_rawData.setWrapText(true);
                ta_rawData.appendText(newValue.getRawData());
            }
        });

    }

    public void handleListViewAction(Event event) {
        MyLog.i("getSelected item = " + listview3.getSelectionModel().getSelectedItem());
    }

    public class AsyncHttpTask {
        String currentUrl;
        String u2rl, myResult;
        int handlernum = 1;
        int resultCode;
        String exceptionAsString;

        // Upload
        private FileInputStream mFileInputStream = null;
        private  URL connectUrl = null;

        public String Task(String url) {
            MyLog.i("");

            MyLog.i("url = "+url);
            try {
                currentUrl = url;
                connectUrl = new URL(url);
                // open connection
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                conn.setRequestMethod("GET");
                conn.setReadTimeout(3000);
                conn.setConnectTimeout(1000);
                conn.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");
                MyLog.i("1");

               Map<String,List<String>> hf = conn.getHeaderFields();
               for (String key: hf.keySet ())
               System.out.println (key+": "+conn.getHeaderField (key));

                /*for(int i = 0;; i++){
                    List<String> headerName = conn.getRequestProperties().get(i);
                    MyLog.i(""+conn.getRequestProperties().getOrDefault(headerName.get(i), headerName));
                    String headerValue = conn.getRequestProperty(headerName.get(i));
                    MyLog.i("headerName = "+headerName+" : "+headerValue);
                    if (headerName ==null && headerValue ==null){
                        MyLog.i("");
                        break;
                    }
                }

                for (String header : conn.getRequestProperties().keySet()) {
                    MyLog.i("");
                    if (header != null) {
                        MyLog.i("");
                        for (String value : conn.getRequestProperties().get(header)) {
                            MyLog.i("");
                            System.out.println(header + ":" + value);
                        }
                    }
                }
*/
                /*Map<String,List<String>> headers = conn.getHeaderFields();
                Iterator<String> it = headers.keySet().iterator();
                while(it.hasNext()) {
                    String key = it.next();
                    List<String> values = headers.get(key);
                    StringBuffer sb = new StringBuffer();
                    for(int i=0; i<values.size(); i++) {
                        sb.append(";" + values.get(i));
                    }
                    MyLog.i("test", key + "=" + sb.toString().substring(1));
                }*/
                // write data
               /* for(int i = 0;; i++){
                    String headerName = conn.getHeaderFieldKey(i);
                    String headerValue = conn.getHeaderField(i);
                    MyLog.i("headerName = "+headerName+" : "+headerValue);
                    if (headerName ==null && headerValue ==null){
                        MyLog.i("");
                        break;
                    }
                }
*/



/*
                OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");// EUC-KR");
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write("");
                writer.close();
                out.flush(); // finish upload...*/
                for(int i = 0;; i++){
                    String headerName = conn.getHeaderFieldKey(i);
                    String headerValue = conn.getHeaderField(i);
                    MyLog.i("headerName = "+headerName+" : "+headerValue);
                    if (headerName ==null && headerValue ==null){
                        MyLog.i("");
                        break;
                    }
                }
                // get response
                resultCode = conn.getResponseCode();
//                String a = String.valueOf(resultCode);
                String b;



                for (String header : conn.getHeaderFields().keySet()) {
                    if (header != null) {
                        for (String value : conn.getHeaderFields().get(header)) {
                            System.out.println(header + ":" + value);
                        }
                    }
                }



                boolean isError = resultCode >= 400;
                InputStream is = isError ? conn.getErrorStream() : conn.getInputStream();
                InputStreamReader tmp = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder sb = new StringBuilder();

                String str;
                while ((str = reader.readLine()) != null) { // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    sb.append(str); // View에 표시하기 위해 라인 구분자 추가
                }


                myResult = sb.toString(); // 전송결과를 전역 변수에 저장
                listData.add(new ResultSource(url, resultCode, myResult, "a \n b \t c"));

                MyLog.i("myResult = "+myResult);
//                out.close();

            } catch (Exception e) {
                StringWriter stringWriter = new StringWriter();
                e.printStackTrace(new PrintWriter(stringWriter));
                exceptionAsString = stringWriter.toString();
                listData.add(new ResultSource(url, 1000, exceptionAsString, "a \n b \n c"));
                MyLog.i("exceptionAsString = " + exceptionAsString);
                handlernum = -1;
                // TODO: handle exception
            }
            // mod.getString(R.string.error_des), mod.getString(R.string.yes));
            //Change to error code

            return null;
        }
    }

    private void fetchSavedListFromDatabaseToListview() {
        MyLog.i("fetchSavedList");
        try (Connection con = getConnection()) {
            //기본 HOME 테이블이 존재하지 않으면 생성 낄낄
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }
            listview1.setItems(querySavedList(con));
            listview1.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Warning");
                    alert.setContentText("don't use this program without accepting by authorizer");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        dropTableFromDatabase(listview1.getSelectionModel().getSelectedItem());
                    } else {
                    }
                }
            });
            listview1.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
                @Override
                public ListCell<String> call(ListView<String> param) {
                    ListCell<String> cell = new ListCell<String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item != null) {
                                setText(item);
                            }
                        }
                    };
                    return cell;
                }
            });
        } catch (SQLException | ClassNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText(""+e);
            alert.setContentText("172");
            e.printStackTrace();
        }
    }

    private void dropTableFromDatabase(String selectedItem) {
        Statement st = null;
        try (Connection con = getConnection()) {
            MyLog.i("createSchema");
            if (!schemaExists(con)) {
                createSchema(con);
                populateDatabase(con);
            }
            st = con.createStatement();
            String table = "drop table "+selectedItem;
            st.executeUpdate(table);
            fetchSavedListFromDatabaseToListview();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> querySavedList(Connection con) {
        ObservableList<String> names = FXCollections.observableArrayList();
        Statement st = null;
        try {
            st = con.createStatement();
            ResultSet rs = st.executeQuery("select * from tab where tname not like 'BIN$%'");
            while (rs.next()) {
                names.add(rs.getString("tname"));
                MyLog.i("rs.getString saved list = "+rs.getString("tname"));
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning Dialog");
            alert.setHeaderText("Warning"+e);
            alert.setContentText("172"+e);
            e.printStackTrace();
        }

        return names;
    }

    /*private ObservableList<String> querySavedList(Connection con) throws SQLException {
        ObservableList<String> names = FXCollections.observableArrayList();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from tab where tname not like 'BIN$%'");
        while (rs.next()) {
            names.add(rs.getString("tname"));
            MyLog.i("rs.getString saved list = "+rs.getString("tname"));
        }
        return names;
    }*/

    private ObservableList<String> queryUrlList(Connection con, String selectedItem) throws SQLException {
        ObservableList<String> names = FXCollections.observableArrayList();
        Statement st = con.createStatement();
        MyLog.i("selected Item = "+selectedItem);
        ResultSet rs = st.executeQuery("select * from "+selectedItem);
        while (rs.next()) {
            names.add(rs.getString("url"));
            MyLog.i("rs.getString url = "+rs.getString("url"));
        }
        return names;
    }

    private void populateDatabase(Connection con) throws SQLException {

    }

    private boolean schemaExists(Connection con) {
        Statement st = null;
        try {
            st = con.createStatement();
            st.executeQuery("select count(*) from home");

        } catch (SQLException e) {
            e.printStackTrace();
            MyLog.i("return false");
            return false;
        }
        MyLog.i("return true");
        return true;
    }

    private void createSchema(Connection con) throws SQLException {
        Statement st = null;
        try {
            MyLog.i("createSchema");
            st = con.createStatement();
            String table = "create table home(url varchar2(100) NOT NULL)";
            st.executeUpdate(table);
            table = "insert into home values('http://www.yahoo.co.kr/')";
            st.executeUpdate(table);
            table = "insert into home values('http://www.google.co.kr/')";
            st.executeUpdate(table);
            table = "insert into home values('http://mobile.twitter.com/')";
            st.executeUpdate(table);
            table = "insert into home values('http://www.amazon.com/')";
            st.executeUpdate(table);
            table = "insert into home values('http://www.naver.com/')";
            st.executeUpdate(table);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        MyLog.i("class for name = "+Class.forName("oracle.jdbc.driver.OracleDriver"));
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    private final int SERVER_RESPONSE_CODE_0 = 0;
    private final int SERVER_RESPONSE_CODE_100 = 100;
    private final int SERVER_RESPONSE_CODE_101 = 101;
    private final int SERVER_RESPONSE_CODE_200 = 200;
    private final int SERVER_RESPONSE_CODE_201 = 201;
    private final int SERVER_RESPONSE_CODE_202 = 202;
    private final int SERVER_RESPONSE_CODE_203 = 203;
    private final int SERVER_RESPONSE_CODE_204 = 204;
    private final int SERVER_RESPONSE_CODE_205 = 205;
    private final int SERVER_RESPONSE_CODE_206 = 206;
    private final int SERVER_RESPONSE_CODE_300 = 300;
    private final int SERVER_RESPONSE_CODE_301 = 301;
    private final int SERVER_RESPONSE_CODE_302 = 302;
    private final int SERVER_RESPONSE_CODE_303 = 303;
    private final int SERVER_RESPONSE_CODE_304 = 304;
    private final int SERVER_RESPONSE_CODE_305 = 305;
    private final int SERVER_RESPONSE_CODE_306 = 306;
    private final int SERVER_RESPONSE_CODE_307 = 307;
    private final int SERVER_RESPONSE_CODE_400 = 400;
    private final int SERVER_RESPONSE_CODE_401 = 401;
    private final int SERVER_RESPONSE_CODE_402 = 402;
    private final int SERVER_RESPONSE_CODE_403 = 403;
    private final int SERVER_RESPONSE_CODE_404 = 404;
    private final int SERVER_RESPONSE_CODE_405 = 405;
    private final int SERVER_RESPONSE_CODE_406 = 406;
    private final int SERVER_RESPONSE_CODE_407 = 407;
    private final int SERVER_RESPONSE_CODE_408 = 408;
    private final int SERVER_RESPONSE_CODE_409 = 409;
    private final int SERVER_RESPONSE_CODE_410 = 410;
    private final int SERVER_RESPONSE_CODE_411 = 411;
    private final int SERVER_RESPONSE_CODE_412 = 412;
    private final int SERVER_RESPONSE_CODE_413 = 413;
    private final int SERVER_RESPONSE_CODE_414 = 414;
    private final int SERVER_RESPONSE_CODE_415 = 415;
    private final int SERVER_RESPONSE_CODE_416 = 416;
    private final int SERVER_RESPONSE_CODE_417 = 417;
    private final int SERVER_RESPONSE_CODE_500 = 500;
    private final int SERVER_RESPONSE_CODE_501 = 501;
    private final int SERVER_RESPONSE_CODE_502 = 502;
    private final int SERVER_RESPONSE_CODE_503 = 503;
    private final int SERVER_RESPONSE_CODE_504 = 504;
    private final int SERVER_RESPONSE_CODE_505 = 505;
    private final int SERVER_RESPONSE_CODE_1000 = 1000;
    private final String STATUS_CODE_DEFINITION_100 = "Continue";
    private final String STATUS_CODE_DEFINITION_101 = "Switching Protocols";
    private final String STATUS_CODE_DEFINITION_200 = "Ok";
    private final String STATUS_CODE_DEFINITION_201 = "Created";
    private final String STATUS_CODE_DEFINITION_202 = "Accepted";
    private final String STATUS_CODE_DEFINITION_203 = "Non-AuthInfo";
    private final String STATUS_CODE_DEFINITION_204 = "NoContent";
    private final String STATUS_CODE_DEFINITION_205 = "Reset Content";
    private final String STATUS_CODE_DEFINITION_206 = "Partial Content";
    private final String STATUS_CODE_DEFINITION_300 = "Multiple Choices";
    private final String STATUS_CODE_DEFINITION_301 = "Moved Permanently";
    private final String STATUS_CODE_DEFINITION_302 = "302 Found";
    private final String STATUS_CODE_DEFINITION_303 = "See Other";
    private final String STATUS_CODE_DEFINITION_304 = "Not Modified";
    private final String STATUS_CODE_DEFINITION_305 = "Use Proxy";
    private final String STATUS_CODE_DEFINITION_306 = "Unused";
    private final String STATUS_CODE_DEFINITION_307 = "Temp Redirect";
    private final String STATUS_CODE_DEFINITION_400 = "Bad Request";
    private final String STATUS_CODE_DEFINITION_401 = "Unauthorized";
    private final String STATUS_CODE_DEFINITION_402 = "Payment Required";
    private final String STATUS_CODE_DEFINITION_403 = "Forbidden";
    private final String STATUS_CODE_DEFINITION_404 = "Not Found";
    private final String STATUS_CODE_DEFINITION_405 = "Method NotAllowed";
    private final String STATUS_CODE_DEFINITION_406 = "Not Acceptable";
    private final String STATUS_CODE_DEFINITION_407 = "Proxy AuthRequired";
    private final String STATUS_CODE_DEFINITION_408 = "Request Timeout";
    private final String STATUS_CODE_DEFINITION_409 = "Conflict";
    private final String STATUS_CODE_DEFINITION_410 = "Gone";
    private final String STATUS_CODE_DEFINITION_411 = "Length Required";
    private final String STATUS_CODE_DEFINITION_412 = "PreconditionFailed";
    private final String STATUS_CODE_DEFINITION_413 = "RequestEntityTooLarge";
    private final String STATUS_CODE_DEFINITION_414 = "Request-URITooLong";
    private final String STATUS_CODE_DEFINITION_415 = "Unsupported Media Type";
    private final String STATUS_CODE_DEFINITION_416 = "Requested Range Not Satisfiable";
    private final String STATUS_CODE_DEFINITION_417 = "Expectation Failed";
    private final String STATUS_CODE_DEFINITION_500 = "Internal Server Error";
    private final String STATUS_CODE_DEFINITION_501 = "Not Implemented";
    private final String STATUS_CODE_DEFINITION_502 = "Bad Gateway";
    private final String STATUS_CODE_DEFINITION_503 = "Service Unavailable";
    private final String STATUS_CODE_DEFINITION_504 = "Gateway Timeout";
    private final String STATUS_CODE_DEFINITION_505 = "HTTP Version Not Supported";
    private final String STATUS_CODE_DEFINITION_0 = "Timeout";
    private final String STATUS_CODE_DEFINITION_ERROR = "Error";

}
