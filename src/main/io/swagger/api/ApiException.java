package swagger.api;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-10-01T12:07:30.310865165Z[GMT]")
public class ApiException extends Exception
{
    private int code;
    public ApiException (int code, String msg)
    {
        super(msg);
        this.code = code;
    }
}
