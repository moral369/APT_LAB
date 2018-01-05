package drill.data;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Chan-Ju on 2015-09-15.
 */
public class TBL_Victims_Detail {
    //TBL_APT_DETAIL(SEQUENCE_NO , PERSON_NAME, PHONE_NUMBER, ACCCOUNT, ACCCOUNT_1, ACCCOUNT_2)

    private final SimpleStringProperty mSEQUENCE_NO = new SimpleStringProperty("");
    private final SimpleStringProperty mDateTime = new SimpleStringProperty("");
    private final SimpleStringProperty mVictimActions = new SimpleStringProperty("");

    public TBL_Victims_Detail() {
        this("", "", "");
    }

    public TBL_Victims_Detail(String aSEQUENCE_NO , String aDateTime, String aVictimActions )
    {
        setSEQUENCE_NO(aSEQUENCE_NO);
        setDateTime(aDateTime);
        setVictimActions(aVictimActions);
    }
    public TBL_Victims_Detail( String aDateTime, String aVictimActions )
    {
        setSEQUENCE_NO("0");
        setDateTime(aDateTime);
        setVictimActions(aVictimActions);
    }


    public void setSEQUENCE_NO(String mSEQUENCE_NO) {
        this.mSEQUENCE_NO.set(mSEQUENCE_NO);
    }

    public void setDateTime(String mDate) {
        this.mDateTime.set(mDate);
    }

    public void setVictimActions(String mVictimActions) {
        this.mVictimActions.set(mVictimActions);
    }

    public String getSEQUENCE_NO() {

        return mSEQUENCE_NO.get();
    }

    public SimpleStringProperty mSEQUENCE_NOProperty() {
        return mSEQUENCE_NO;
    }

    public String getDateTime() {
        return mDateTime.get();
    }

    public SimpleStringProperty mDateTimeProperty() {
        return mDateTime;
    }

    public String getVictimActions() {
        return mVictimActions.get();
    }

    public SimpleStringProperty mVictimActionsProperty() {
        return mVictimActions;
    }


}
