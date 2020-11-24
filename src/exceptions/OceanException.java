package exceptions;

public class OceanException extends Throwable {
    public OceanException() {
        super();
    }

    public OceanException(String msg) {
        super(msg);
    }

    public OceanException(String msg, Throwable t) {
        super(msg, t);
    }
}
