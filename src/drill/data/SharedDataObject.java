package drill.data;

/**
 * Created by Chan-Ju on 2015-09-13.
 */
public class SharedDataObject
{

    private int mSelectGroupView;

    private static SharedDataObject uniqueInstance = new SharedDataObject();

    private SharedDataObject()
    {
        init();
    }

    public static SharedDataObject getInstance(){
        if (uniqueInstance == null)
            uniqueInstance = new SharedDataObject();
        return uniqueInstance;
    }

    private void init()
    {
        mSelectGroupView = DefineValue.IT_IS_NOTHING;
    }

    public int getSelectGroupView(){return mSelectGroupView;}

    public void setSelectGroupView(int group)
    {
        mSelectGroupView = group;
    }

}
