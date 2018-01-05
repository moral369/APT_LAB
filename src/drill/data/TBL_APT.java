package drill.data;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Chan-Ju on 2015-08-24.
 */
public class TBL_APT {

    //TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT)

    private final SimpleStringProperty mSEQ = new SimpleStringProperty("");
    private final SimpleStringProperty mACTDATE = new SimpleStringProperty("");
    private final SimpleStringProperty mIPADDR = new SimpleStringProperty("");

    private final SimpleStringProperty mPHTYPE = new SimpleStringProperty("");
    private final SimpleStringProperty mPHNUM = new SimpleStringProperty("");
    private final SimpleStringProperty mACTDIV = new SimpleStringProperty("");

    private final SimpleStringProperty mETC = new SimpleStringProperty("");
    private final SimpleStringProperty mGROUPTYPE = new SimpleStringProperty("");
    private final SimpleStringProperty mUSERAGENT = new SimpleStringProperty("");



    public TBL_APT() {
        this("", "", "","", "", "","", "", "");
    }

    public TBL_APT(String aSEQ , String aACTDATE, String aIPADDR,
                   String  aPHTYPE, String  aPHNUM, String aACTDIV,
                   String aETC, String aGROUPTYPE, String aUSERAGENT)
    {
        setSEQ( aSEQ);
        setACTDATE( aACTDATE);
        setIPADDR( aIPADDR);
        setPHTYPE( aPHTYPE);
        setPHNUM( aPHNUM);
        setACTDIV( aACTDIV);
        setETC( aETC);
        setGROUPTYPE( aGROUPTYPE);
        setUSERAGENT( aUSERAGENT) ;
    }

    //TBL_APT(SEQ , ACTDATE, IPADDR, PHTYPE, PHNUM, ACTDIV,ETC, GROUPTYPE, USERAGENT)

    public String getSEQ() {
        return mSEQ.get();
    }
    public String getACTDATE() {
        return mACTDATE.get();
    }
    public String getIPADDR() {
        return mIPADDR.get();
    }
    public String getPHTYPE() {
        return mPHTYPE.get();
    }
    public String getPHNUM() {
        return mPHNUM.get();
    }
    public String getACTDIV() {
        return mACTDIV.get();
    }
    public String getETC() {
        return mETC.get();
    }
    public String getGROUPTYPE() {
        return mGROUPTYPE.get();
    }
    public String getUSERAGENT() {
        return mUSERAGENT.get();
    }


    public void setSEQ(String aSEQ) {
        mSEQ.set(aSEQ);
    }
    public void setACTDATE(String aACTDATE) {
        mACTDATE.set(aACTDATE);
    }
    public void setIPADDR(String aIPADDR) {
        mIPADDR.set(aIPADDR);
    }
    public void setPHTYPE(String aPHTYPE) {
        mPHTYPE.set(aPHTYPE);
    }
    public void setPHNUM(String aPHNUM) {
        mPHNUM.set(aPHNUM);
    }
    public void setACTDIV(String aACTDIV) {
        mACTDIV.set(aACTDIV);
    }
    public void setETC(String aETC) {
        mETC.set(aETC);
    }
    public void setGROUPTYPE(String aGROUPTYPE) {
        mGROUPTYPE.set(aGROUPTYPE);
    }
    public void setUSERAGENT(String aUSERAGENT) {
        mUSERAGENT.set(aUSERAGENT);
    }

}




