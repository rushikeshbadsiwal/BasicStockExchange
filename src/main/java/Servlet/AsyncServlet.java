package Servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


public class AsyncServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request, final HttpServletResponse response) {

        final long startTime = System.currentTimeMillis();
        final AsyncContext asyncContext = request.startAsync(request, response);

        new Thread() {
            @Override
            public void run() {
                try {
                    ServletResponse response = asyncContext.getResponse();
                    response.setContentType("text/plain");
                    PrintWriter out = response.getWriter();
                    Thread.sleep(0);
                    out.println("Work completed. Time elapsed - " + (System.currentTimeMillis() - startTime));
                    asyncContext.complete();
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }.start();
    }
}