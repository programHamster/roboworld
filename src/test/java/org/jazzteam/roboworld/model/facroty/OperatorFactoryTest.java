package org.jazzteam.roboworld.model.facroty;

import org.jazzteam.roboworld.Constants;
import org.jazzteam.roboworld.exception.unsupported.UnsupportedOperatorException;
import org.jazzteam.roboworld.model.bean.operator.Operator;
import org.jazzteam.roboworld.model.bean.operator.RecreaterOperator;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class OperatorFactoryTest {

    @DataPoints
    public static Object[][] operatorNames = new Object[][]{
            {Constants.INIT_PARAM_VALUE_OPERATOR_RECREATOR, RecreaterOperator.class}
    };

    @Theory
    public void getCommandFromFactory(Object... operators) throws UnsupportedOperatorException {
        // act
        Operator receivedOperator = OperatorFactory.getOperatorFromFactory((String)operators[0], null);
        // assert
        assertEquals(receivedOperator.getClass(), operators[1]);
    }

    @Test(expected = UnsupportedOperatorException.class)
    public void getCommandFromFactory_wrongParameter() throws UnsupportedOperatorException {
        // assert
        OperatorFactory.getOperatorFromFactory(org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER,
                        org.jazzteam.roboworld.model.facroty.Constants.WRONG_PARAMETER);
    }

    @Test(expected = NullPointerException.class)
    public void getCommandFromFactory_null() throws UnsupportedOperatorException {
        // assert
        OperatorFactory.getOperatorFromFactory(null, null);
    }
}