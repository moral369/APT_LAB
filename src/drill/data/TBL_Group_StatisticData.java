package drill.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Created by Chan-Ju on 2015-09-15.
 */
public class TBL_Group_StatisticData {

    //TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)

    private final SimpleStringProperty mSEQUENCE_NO = new SimpleStringProperty("");
    private final SimpleStringProperty mPERSON_NAME = new SimpleStringProperty("");
    private final SimpleStringProperty mPHONE_NUMBER = new SimpleStringProperty("");

    private final SimpleStringProperty mACC_COUNT = new SimpleStringProperty("");







    private final SimpleStringProperty mDOWN_COUNT = new SimpleStringProperty("");
    private final SimpleStringProperty mINSTALL_COUNT = new SimpleStringProperty("");
    private final SimpleStringProperty mEXEC_COUNT = new SimpleStringProperty("");

    public String getPermit_COUNT() {
        return mPermit_COUNT.get();
    }

    public SimpleStringProperty Permit_COUNTProperty() {
        return mPermit_COUNT;
    }

    public void setPermit_COUNT(String mPermit_COUNT) {
        this.mPermit_COUNT.set(mPermit_COUNT);
    }

    private final SimpleStringProperty mPermit_COUNT = new SimpleStringProperty("");

    private final SimpleStringProperty mBLANK_CONTENT = new SimpleStringProperty("");


    public void setSEQUENCE_NO(String mSEQUENCE_NO) { this.mSEQUENCE_NO.set(mSEQUENCE_NO); }
    public void setPERSON_NAME(String mPERSON_NAME) { this.mPERSON_NAME.set(mPERSON_NAME); }
    public void setPHONE_NUMBER(String mPHONE_NUMBER) { this.mPHONE_NUMBER.set(mPHONE_NUMBER); }
    public void setACC_COUNT(String aACC_COUNT) { this.mACC_COUNT.set(aACC_COUNT); }
    public void setEXEC_COUNT(String aEXEC_COUNT) { this.mEXEC_COUNT.set(aEXEC_COUNT); }
    public void setINSTALL_COUNT(String aINSTALL_COUNT) { this.mINSTALL_COUNT.set(aINSTALL_COUNT); }
    public void setBLANK_CONTENT(String aBLANK_CONTENT) { this.mBLANK_CONTENT.set(aBLANK_CONTENT); }
    public void setDOWN_COUNT(String mDOWN_COUNT) {  this.mDOWN_COUNT.set(mDOWN_COUNT); }

    public String getDOWN_COUNT() {        return mDOWN_COUNT.get();    }
    public String getACC_COUNT() { return mACC_COUNT.get(); }
    public String getINSTALL_COUNT() { return mINSTALL_COUNT.get(); }
    public String getEXEC_COUNT() { return mEXEC_COUNT.get(); }
    public String getBLANK_CONTENT() { return mBLANK_CONTENT.get();  }
    public String getSEQUENCE_NO() { return mSEQUENCE_NO.get(); }
    public String getPERSON_NAME() { return mPERSON_NAME.get(); }
    public String getPHONE_NUMBER() { return mPHONE_NUMBER.get(); }


    public StringProperty ACCCOUNTProperty() { return mACC_COUNT; }
    public StringProperty INSTALL_COUNTProperty() { return mINSTALL_COUNT; }
    public StringProperty EXEC_COUNPropertyT() { return mEXEC_COUNT; }
    public StringProperty BLANK_CONTENTProperty() { return mBLANK_CONTENT;  }
    public StringProperty SEQUENCE_NOProperty() { return mSEQUENCE_NO; }
    public StringProperty PERSON_NAMEProperty() { return mPERSON_NAME; }
    public StringProperty PHONE_NUMBERProperty() { return mPHONE_NUMBER; }
    public SimpleStringProperty DOWN_COUNTProperty() { return mDOWN_COUNT; }


    //TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)
    public TBL_Group_StatisticData()
    {
        this("", "", "","", "", "","");
    }


    public TBL_Group_StatisticData(
            String aSEQUENCE_NO ,  String aPERSON_NAME, String aPHONE_NUMBER,
            String aACC_COUNT, String aINSTALL_COUNT, String aEXEC_COUNT, String aDOWN_COUNT)
    {

        setSEQUENCE_NO(aSEQUENCE_NO);
        setPERSON_NAME(aPERSON_NAME);
        setPHONE_NUMBER(aPHONE_NUMBER);
        setACC_COUNT(aACC_COUNT);
        setINSTALL_COUNT(aINSTALL_COUNT);
        setEXEC_COUNT(aEXEC_COUNT);
        setDOWN_COUNT(aDOWN_COUNT);
        setBLANK_CONTENT("");
    }

    public TBL_Group_StatisticData(
            String aSEQUENCE_NO ,  String aPERSON_NAME, String aPHONE_NUMBER,
            String aACC_COUNT, String aINSTALL_COUNT, String aEXEC_COUNT,
            String aDOWN_COUNT, String aBLANK)
    {

        setSEQUENCE_NO(aSEQUENCE_NO);
        setPERSON_NAME(aPERSON_NAME);
        setPHONE_NUMBER(aPHONE_NUMBER);
        setACC_COUNT(aACC_COUNT);
        setINSTALL_COUNT(aINSTALL_COUNT);
        setEXEC_COUNT(aEXEC_COUNT);
        setDOWN_COUNT(aDOWN_COUNT);
        setBLANK_CONTENT(aBLANK);
    }

}


