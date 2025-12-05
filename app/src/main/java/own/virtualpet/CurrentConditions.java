package own.virtualpet;

/**
 * Holds the current weather information gotten from Google
 * 
 * @author Jonathan
 * 
 */
public class CurrentConditions
{
    private String condition;
    private String main;
    private int tempF;


    public CurrentConditions()
    {

    }

    public CurrentConditions(String condition, String main, int tempF)
    {
	super();
	this.condition = condition;
    this.main = main;
	this.tempF = tempF;
    }

    public String getCondition()
    {
	return condition;
    }

    public String getMain()
    {
        return main;
    }

    public int getTempF()
    {
	return tempF;
    }



    public void setCondition(String condition)
    {
	this.condition = condition;
    }

    public void setMain(String main)
    {
        this.main = main;
    }

    public void setTempF(int tempF)
    {
	this.tempF = tempF;
    }




    @Override
    public String toString()
    {
	return "CurrentConditions [condition=" + condition + ", main=" + main + ", tempF=" + tempF + "]";
    }

}
