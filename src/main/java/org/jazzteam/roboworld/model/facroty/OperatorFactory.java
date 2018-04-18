package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.model.bean.operator.MonitoringPerformanceOperator;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;
import org.jazzteam.roboworld.model.exception.unsupported.UnsupportedOperatorException;

public abstract class OperatorFactory {
    public static Operator getOperatorFromFactory(String operatorName, String additionalParams)
            throws UnsupportedOperatorException{
        switch(operatorName){
            case Constants.INIT_PARAM_VALUE_OPERATOR_PERFORMANCE:
                return getMonitoringPerformanceOperator(additionalParams);
            case Constants.INIT_PARAM_VALUE_OPERATOR_RECREATOR:
                return getRecreaterOperator(additionalParams);
            default:
                throw new UnsupportedOperatorException();
        }
    }

    private static MonitoringPerformanceOperator getMonitoringPerformanceOperator(String additionalParams)
            throws UnsupportedOperatorException{
        MonitoringPerformanceOperator operator;
        if(additionalParams != null){
            String[] params = split(additionalParams);
            long period = 0;
            // because true is default value
            boolean recreatedOfRobot = true;
            switch (params.length){
                case 2:
                    recreatedOfRobot = Boolean.valueOf(params[1]);
                case 1:
                    period = Long.valueOf(params[0]);
            }
            if(period > 0){
                operator = new MonitoringPerformanceOperator(recreatedOfRobot, period);
            } else {
                throw new UnsupportedOperatorException("The period parameter isn't positive");
            }
            return operator;
        } else {
            throw new UnsupportedOperatorException("MonitoringPerformanceOperator has parameters");
        }
    }

    private static RecreaterOperator getRecreaterOperator(String additionalParams){
        boolean recreatedOfRobot = true;
        if(additionalParams != null){
            recreatedOfRobot = Boolean.valueOf(additionalParams);
        }
        return new RecreaterOperator(recreatedOfRobot);
    }

    private static String[] split(String params){
        return params.split(",");
    }
}
