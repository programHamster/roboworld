package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedOperatorException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;

public abstract class OperatorFactory {
    public static Operator getOperatorFromFactory(String operatorName, String additionalParam)
            throws UnsupportedOperatorException{
        if(operatorName == null){
            throw new NullPointerException(org.jazzteam.roboworld.exception.Constants.OPERATOR_NAME_IS_NULL);
        }
        switch(operatorName){
            case Constants.INIT_PARAM_VALUE_OPERATOR_RECREATOR:
                return getRecreaterOperator(additionalParam);
            default:
                throw new UnsupportedOperatorException();
        }
    }

    private static RecreaterOperator getRecreaterOperator(String additionalParam){
        // because it default value
        boolean recreatedOfRobot = true;
        if(additionalParam != null){
            recreatedOfRobot = Boolean.valueOf(additionalParam);
        }
        return new RecreaterOperator(recreatedOfRobot);
    }

}
