public class Tire
{
    float tiretemp = 150.0f;
    float vehiclevelocity = 0.0f;
    float tiretraction = 1.0f;
    float tirewear = 0.0f;
    boolean tirepoped = false;
    boolean wheelslip = false;
    float rotationspeed = 0.0f;
    float tiretempupdate(){
        //TODO: add actual formula
        //used for calculating tire temperature
        float temperature = this.tiretemp;
        System.out.println("rotspeed "+rotationspeed);
        System.out.println( "velocity "+vehiclevelocity);
        System.out.println("traction "+tiretraction);
        if(wheelslip){
            temperature = tiretemp + (rotationspeed - vehiclevelocity) * tiretraction;
        }
        return temperature;
    }

    private boolean Tirepoped(){
        tirepoped = tiretemp >= 150.0f || tirewear > 90.0f && rotationspeed >= vehiclevelocity * 1.2f || tirewear > 95.0f;
        return tirepoped;
    }

    void main(){
        wheelslip = true;//TODO: do change! only for testing
        rotationspeed += 1f; //TODO: change
        tiretemp = tiretempupdate();
        tirepoped = Tirepoped();
        if (tirepoped){
            System.out.println("Tire has popped!");
        }
        System.out.println("temp " + tiretemp);
    }

    public Tire()
    {

    }

}
