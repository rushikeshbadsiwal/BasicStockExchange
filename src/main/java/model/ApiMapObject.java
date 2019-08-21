package model;

import controller.Controller;

import java.lang.reflect.Method;

public class ApiMapObject {
    private Controller controller;
    private Method method;

    public ApiMapObject(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Controller getService() {
        return controller;
    }

    public void setService(Controller controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
