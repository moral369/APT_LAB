package drill.cache;

/**
 * Created by cross on 2015-06-07.
 */
public class ResultSource {

    private String rawData;
    private String url;
    private int resultCode;
    private String resultSource;

    public ResultSource(String url, int resultCode, String resultSource, String rawData) {
        this.url = url;
        this.resultCode = resultCode;
        this.resultSource = resultSource;
        this.rawData = rawData;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getUrl() {
        return url;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultSource() {
        return resultSource;
    }

}
