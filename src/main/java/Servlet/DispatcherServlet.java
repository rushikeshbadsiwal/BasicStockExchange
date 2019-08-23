package Servlet;

import model.ApiMapObject;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Singleton
public class DispatcherServlet extends HttpServlet {

    private final Map<String, ApiMapObject> processorMap;

    @Inject
    public DispatcherServlet(Map<String, ApiMapObject> processorMap) {
        this.processorMap = processorMap;
    }


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        final long startTime = System.currentTimeMillis();
        System.out.println("path is " + request.getPathInfo());
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().println(doGetRedirect(request));
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        response.getWriter().println("Dispatcher Servlet okay  Time elapsed: " + (System.currentTimeMillis() - startTime));
        response.getWriter().println(request.getPathInfo());
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            doPostRedirect(request);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        response.setContentType("text/plain");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("Dispatcher Servlet okay  Time elapsed: " + (System.currentTimeMillis() - startTime));
        response.getWriter().println(request.getPathInfo());
    }

//    private void doGetRedirect(HttpServletRequest request){
//        Reflections reflections = new Reflections("");
//        for (Class service : reflections.getSubTypesOf(Service.class)) {
//            if(request.getPathInfo().startsWith(((PrefixPath)service.getAnnotation(PrefixPath.class)).path())) {
//                System.out.println("this is the service "+service);
//                for (Method method : service.getMethods()) {
//                    Path pathAnnotation = (Path) method.getAnnotation(Path.class);
//                    if (pathAnnotation != null) {
//                        if (pathAnnotation.path().equals(request.getPathInfo())) {
//                            try {
//                                method.invoke(service.newInstance());
//                            } catch (IllegalAccessException e) {
//                                e.printStackTrace();
//                            } catch (InvocationTargetException e) {
//                                e.printStackTrace();
//                            } catch (InstantiationException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

    private Object doGetRedirect(HttpServletRequest request) throws IOException, InvocationTargetException, IllegalAccessException {
        ApiMapObject apiMapObject = processorMap.get(request.getPathInfo());
        return apiMapObject.getMethod().invoke(apiMapObject.getService(), request.getReader());
    }

    private void doPostRedirect(HttpServletRequest request) throws IOException, InvocationTargetException, IllegalAccessException {
        ApiMapObject apiMapObject = processorMap.get(request.getPathInfo());
        apiMapObject.getMethod().invoke(apiMapObject.getService(), request.getReader());
    }
}