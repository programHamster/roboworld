<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="org.jazzteam.roboworld.Constants" %>
<!DOCTYPE html>
<html>
<head>
    <title>Roboworld</title>
    <script type="text/javascript" src="js/main.js"></script>
    <style type="text/css">
        p {
            margin: 5px 0;
        }
    </style>
</head>
<body onload="connect();" onunload="disconnect();">
<h2>Roboworld</h2>
<div class="container">
    <div class="functional">
        <hr/>
        Create a new robot :
        <form name="createRobot" id="formCreateRobot" method="post" action="main" >
            <br>
            <input type="hidden" name="<%=Constants.PARAM_NAME_COMMAND%>" value="<%=Constants.COMMAND_CREATE_ROBOT%>">
            Select the type of robot
            <select name="<%=Constants.PARAM_NAME_ROBOT_TYPE%>" required>
                <option selected value="<%=Constants.BACK_END_ROBOT_VALUE%>">back-end developer</option>
                <option value="<%=Constants.FRONT_END_ROBOT_VALUE%>">front-end developer</option>
                <option value="<%=Constants.HR_ROBOT_VALUE%>">HR</option>
                <option value="<%=Constants.GENERAL_ROBOT_VALUE%>">general</option>
            </select>
            Enter a name of robot <input type="text" name="<%=Constants.PARAM_NAME_ROBOT_NAME%>" placeholder="optional" />
            <button type="button" id="buttonCreateRobot" onclick="createRobotOrTask('robot')">create</button>
        </form>
        <hr/>
        Create a new task :
        <form name="createTask" id="formCreateTask" method="post" action="main" >
            <br>
            <input type="hidden" name="<%=Constants.PARAM_NAME_COMMAND%>" value="<%=Constants.COMMAND_CREATE_TASK%>">
            Select the implementation of task
            <select name="<%=Constants.PARAM_NAME_TASK_TYPE%>" required>
                <optgroup label="back-end tasks">
                    <option selected value="<%=Constants.BACK_END_TASK_VALUE%>">back-end task</option>
                </optgroup>
                <optgroup label="front-end tasks">
                    <option value="<%=Constants.FRONT_END_TASK_VALUE%>">front-end task</option>
                </optgroup>
                <optgroup label="HR tasks">
                    <option value="<%=Constants.HR_TASK_VALUE%>">HR task</option>
                </optgroup>
                <optgroup label="general tasks">
                    <option value="<%=Constants.DIE_TASK_VALUE%>">die</option>
                </optgroup>
            </select>
            Enter a name of task <input type="text" name="<%=Constants.PARAM_NAME_TASK_NAME%>" placeholder="optional" />
            <button type="button" id="buttonCreateTask" onclick="createRobotOrTask('task')">create</button>
        </form>
        <hr/>
        Broadcast the task :
        <form name="giveTask" id="formGiveTask" method="post" action="main" >
            <br>
            <input type="hidden" name="<%=Constants.PARAM_NAME_COMMAND%>" value="<%=Constants.COMMAND_GIVE_TASK%>">
            Select the task
            <select name="<%=Constants.PARAM_NAME_TASK_NAME%>" id="taskNames" required></select>
            <script>init("<%=Constants.PARAM_VALUE_TASKS_INIT%>")</script>
            assign to a particular robot(optional)
            <input type="checkbox" id="checkboxRobotNames" name="<%=Constants.PARAM_NAME_CHECKBOX%>"
                   value="<%=Constants.PARAM_VALUE_CHECKBOX%>" onchange="hideRobotName()">
            <select name="<%=Constants.PARAM_NAME_ROBOT_NAME%>" id="robotNames" hidden>
            </select>
            <script>init("<%=Constants.PARAM_VALUE_ROBOTS_INIT%>")</script>
            <button type="button" id="buttonGiveTask" onclick="challenge()">challenge</button>
        </form>
        <hr/>
    </div>
    <div class="messages" id="messagesBlock">
    </div>
</div>
</body>
</html>
