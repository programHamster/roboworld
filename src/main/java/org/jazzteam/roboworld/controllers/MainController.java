package org.jazzteam.roboworld.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.command.Command;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.model.facroty.CommandFactory;
import org.jazzteam.roboworld.model.facroty.OperatorFactory;
import org.jazzteam.roboworld.model.facroty.TrackerFactory;
import org.jazzteam.roboworld.output.OutputWriter;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(urlPatterns = Constants.MAIN_URL,
    initParams = {
        @WebInitParam(name = Constants.INIT_PARAM_NAME_OUTPUT, value = Constants.INIT_PARAM_VALUE_WEB_SOCKET_OUTPUT),
        @WebInitParam(name = Constants.INIT_PARAM_NAME_OPERATOR, value = Constants.INIT_PARAM_VALUE_OPERATOR_RECREATOR),
            // if you want to pass more parameters, specify them separated by commas in the next parameter
        @WebInitParam(name = Constants.INIT_PARAM_NAME_OPERATOR_ADDITION_PARAM, value = Constants.INIT_PARAM_VALUE_OPERATOR_ADDITION_PARAM),
        @WebInitParam(name = Constants.INIT_PARAM_NAME_TRACKER, value = Constants.INIT_PARAM_VALUE_TRACKER_PERFORMANCE),
        @WebInitParam(name = Constants.INIT_PARAM_NAME_TRACKER_ADDITION_PARAM, value = Constants.INIT_PARAM_VALUE_TRACKER_DEFAULT_PERIOD),
    })
public class MainController extends HttpServlet {
    private static Operator operator;

    @Override
    public void init(ServletConfig config){
        try {
            super.init(config);
            String operatorName = config.getInitParameter(Constants.INIT_PARAM_NAME_OPERATOR);
            String operatorAdditionParams = config.getInitParameter(Constants.INIT_PARAM_NAME_OPERATOR_ADDITION_PARAM);
            operator = OperatorFactory.getOperatorFromFactory(operatorName, operatorAdditionParams);
            String trackerName = config.getInitParameter(Constants.INIT_PARAM_NAME_TRACKER);
            String trackerAdditionParams = config.getInitParameter(Constants.INIT_PARAM_NAME_TRACKER_ADDITION_PARAM);
            operator.addTracker(TrackerFactory.getTrackerFromFactory(trackerName, trackerAdditionParams, operator));
            String outputName = config.getInitParameter(Constants.INIT_PARAM_NAME_OUTPUT);
            OutputWriter.installOutput(outputName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            response.setHeader(Constants.HEADER_KEY_JSON, Constants.HEADER_VALUE_JSON);
            ObjectMapper mapper = new ObjectMapper();
            String need = request.getParameter(Constants.PARAM_NAME_INIT);
            Map<?, ?> data;
            switch(need){
                case Constants.PARAM_VALUE_TASKS_INIT:
                    data = TaskHolder.getInstance().getAllTasks();
                    break;
                case Constants.PARAM_VALUE_ROBOTS_INIT:
                    data = operator.getRobots();
                    break;
                default:
                    throw new IllegalArgumentException("unknown query \"" + need + "\"");
            }
            mapper.writeValue(response.getWriter(), data);
        } catch (RuntimeException | IOException e) {
            e.printStackTrace();
            sendError(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String commandName = request.getParameter(Constants.PARAM_NAME_COMMAND);
            Command command = CommandFactory.getCommandFromFactory(commandName);
            command.execute(request, operator);
        } catch (UnsupportedException | RuntimeException e) {
            e.printStackTrace();
            sendError(response, e);
        }
    }

    private static void sendError(HttpServletResponse response, Exception e) throws IOException{
        response.setStatus(Constants.ERROR_STATUS_CODE);
        try(PrintWriter writer = response.getWriter()){
            writer.write(e.getMessage());
        }
    }

    @Override
    public void destroy(){
        operator = null;
    }

}
