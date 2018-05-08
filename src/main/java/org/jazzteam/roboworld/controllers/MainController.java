package org.jazzteam.roboworld.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.Setting;
import org.jazzteam.roboworld.command.Command;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.task.TaskHolder;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedException;
import org.jazzteam.roboworld.model.facroty.CommandFactory;
import org.jazzteam.roboworld.model.facroty.OperatorFactory;
import org.jazzteam.roboworld.model.facroty.TrackerFactory;
import org.jazzteam.roboworld.output.OutputInformation;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * This controller is designed to provide information about robots and tasks, as well as executing commands
 * sent by the user.
 */
@WebServlet(urlPatterns = Constants.MAIN_URL,
    initParams = {
            /*
              This parameter is responsible for the operator controlling the robots.
              Available value: recreator.
             */
        @WebInitParam(name = Constants.INIT_PARAM_NAME_OPERATOR, value = Setting.INIT_PARAM_VALUE_OPERATOR),
            // This parameter specifies additional parameters for the operator.
            /*
              The parameter used indicates whether to enable or disable the ability to recreate robots in the event
               of their death. Available value: true.
             */
        @WebInitParam(name = Constants.INIT_PARAM_NAME_OPERATOR_ADDITION_PARAM, value = Setting.INIT_PARAM_VALUE_OPERATOR_ADDITION_PARAM),
            /*
              This parameter is responsible for the selection of the tracker used by the operator to control robots.
              Available value: performance.
             */
        @WebInitParam(name = Constants.INIT_PARAM_NAME_TRACKER, value = Setting.INIT_PARAM_VALUE_TRACKER),
            // This parameter specifies additional parameters for the tracker.
            /*
              This parameter is responsible for the period with which the tracker will monitor the performance of robots.
              Specified in milliseconds. Installed value: 5000.
             */
        @WebInitParam(name = Constants.INIT_PARAM_NAME_TRACKER_ADDITION_PARAM, value = Setting.INIT_PARAM_VALUE_TRACKER_PERIOD),
    })
public class MainController extends HttpServlet {
    /** the operator who will carry out the management of robots */
    private static Operator operator;

    /**
     * This method initializes the output method, operator, and tracker, as well as their additional parameters.
     */
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
            OutputInformation.installOutput(Setting.output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to pass information about robots and tasks to the client side.
     *
     * @param request request came from the user
     * @param response response to the user containing the requested data
     * @throws IOException if an input or output error is detected
     */
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
            sendError(response, e);
        }
    }

    /**
     * The method executes the passed command from the user.
     *
     * @param request request came from the user
     * @param response response to the user containing the requested data
     * @throws IOException if an input or output error is detected
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            String commandName = request.getParameter(Constants.PARAM_NAME_COMMAND);
            Command command = CommandFactory.getCommandFromFactory(commandName);
            command.execute(request, operator);
        } catch (UnsupportedException | RuntimeException e) {
            sendError(response, e);
        }
    }

    /**
     * Sends an error message to the client using the code 500.
     *
     * @param response response used to send a message
     * @param e exception occurred during the work
     * @throws IOException if an input or output error is detected
     */
    private static void sendError(HttpServletResponse response, Exception e) throws IOException{
        response.setStatus(Constants.ERROR_STATUS_CODE);
        try(PrintWriter writer = response.getWriter()){
            writer.write(e.getMessage());
        }
    }

    /**
     * Clears the reference to the used operator.
     */
    @Override
    public void destroy(){
        operator = null;
    }

}
