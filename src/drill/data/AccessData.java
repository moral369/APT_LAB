package drill.data;

/**
 * Created by Chan-Ju on 2015-09-06.
 */
public class AccessData {
    private int m_seq;
    private String m_group_name;
    private String m_access_log;

    public void setM_seq(int m_seq) {
        this.m_seq = m_seq;
    }

    public void setM_group_name(String m_group_name) {
        this.m_group_name = m_group_name;
    }

    public void setM_access_log(String m_access_log) {
        this.m_access_log = m_access_log;
    }

    public int getM_seq() {
        return m_seq;
    }

    public String getM_group_name() {
        return m_group_name;
    }

    public String getM_access_log() {
        return m_access_log;
    }
}
