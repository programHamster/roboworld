
//                        for initialization
var EMPTY = '';
var PRE_ERROR_STATUS = "Error! Status ";
var POST_ERROR_STATUS = ". ";
var HEADER_KEY_AJAX = "X-Requested-With";
var HEADER_VALUE_AJAX = "XMLHttpRequest";
var HEADER_KEY_CACHE = "Cache-Control";
var HEADER_VALUE_CACHE = "no-cache";
var READY_STATE_CHANGE = "readystatechange";
var GET_METHOD = "GET";
var POST_METHOD = "POST";
var URL = "main";
var GIVE_TASK_FORM_ID = "formGiveTask";
var NEED_ROBOTS = "robots";
var NEED_TASKS = "tasks";
var QUERY_PARAMETER = "?need=";

var ID_SELECT_TASK_ELEMENT = "taskNames";
var ID_SELECT_ROBOT_ELEMENT = "robotNames";
var NAME_FOR_ROBOT_TYPE_KEY = "robotType";
var OPTGROUP_TAG = "optgroup";
var OPTGROUP_LABEL = "label";
var OPTGROUP_LABEL_NAMES = {
    "BACK_END_DEVELOPER":"back-end",
    "FRONT_END_DEVELOPER":"front-end",
    "HR":"HR",
    "GENERAL":"general"
};
var TASK = "task";
var ROBOT = "robot";
var SPACE = " ";
var ADDITIONAL_TASKS_LABEL = SPACE + NEED_TASKS;
var ADDITIONAL_ROBOTS_LABEL = SPACE + NEED_ROBOTS;
var OPTION_TAG = "option";
var VALUE = "value";
var jsonDataTasks;

function init(need) {
    var form = document.getElementById(GIVE_TASK_FORM_ID);
    var xhr = new XMLHttpRequest();
    xhr.open(GET_METHOD, URL + QUERY_PARAMETER + need, true);
    xhr.setRequestHeader(HEADER_KEY_AJAX, HEADER_VALUE_AJAX);
    xhr.setRequestHeader(HEADER_KEY_CACHE, HEADER_VALUE_CACHE);
    xhr.send();

    xhr.addEventListener(READY_STATE_CHANGE, function(){
        if(this.readyState !== 4) return;
        if(this.status === 200){
            hideErrorMessage(form);
            var data = JSON.parse(this.responseText);
            if(need === NEED_TASKS){
                jsonDataTasks = data;
            }
            showInSelect(need, data);
        } else {
            showErrorMessage(this, form);
        }
    });
}

function showInSelect(need, data) {
    var selectId;
    var additionalLabel;
    var robotsByTypes;
    switch(need){
        case NEED_TASKS:
            selectId = ID_SELECT_TASK_ELEMENT;
            additionalLabel = ADDITIONAL_TASKS_LABEL;
            break;
        case NEED_ROBOTS:
            selectId = ID_SELECT_ROBOT_ELEMENT;
            additionalLabel = ADDITIONAL_ROBOTS_LABEL;
            robotsByTypes = getRobotsByTypes(data);
            break;
    }
    var selectElement = document.getElementById(selectId);
    selectElement.innerHTML = EMPTY;
    for(var type in OPTGROUP_LABEL_NAMES){
        var optgroupElement = document.createElement(OPTGROUP_TAG);
        optgroupElement.setAttribute(OPTGROUP_LABEL, OPTGROUP_LABEL_NAMES[type] + additionalLabel);
        var list;
        switch(need){
            case NEED_TASKS:
                list = data[type];
                break;
            case NEED_ROBOTS:
                list = robotsByTypes[type];
                break;
        }
        for(var key in list){
            if(list.hasOwnProperty(key)){
                var optionElement = document.createElement(OPTION_TAG);
                optionElement.setAttribute(VALUE, key);
                optionElement.innerHTML = key;
                selectElement.appendChild(optgroupElement);
                optgroupElement.appendChild(optionElement);
            }
        }
    }
}

function getRobotsByTypes(data){
    var result = {};
    for(var type in OPTGROUP_LABEL_NAMES){
        result[type] = {};
    }
    for(var robotName in data){
        if(data.hasOwnProperty(robotName)){
            var robot = data[robotName];
            var robotType = robot.robotType;
            result[robotType][robot.name] = robot;
        }
    }
    return result;
}

var SPAN = "span";
var ATTRIBUTE_ID = "id";
var ATTRIBUTE_VALUE_ID = "initTaskError";
var ERROR_COLOR = "red";

function showErrorMessage(xhr, element){
    var message = PRE_ERROR_STATUS + xhr.status + POST_ERROR_STATUS
    + xhr.responseText !== EMPTY ? xhr.responseText : xhr.statusText;
    var spanElement = document.createElement(SPAN);
    spanElement.setAttribute(ATTRIBUTE_ID, ATTRIBUTE_VALUE_ID);
    spanElement.style.color = ERROR_COLOR;
    spanElement.innerText = message;
    var firstElement = element.firstChild;
    element.insertBefore(spanElement, firstElement);
}

function hideErrorMessage(element){
    var firstElement = element.firstElementChild;
    if(firstElement !== null && firstElement !== undefined){
        var id = firstElement[ATTRIBUTE_ID];
        if(id !== null && id !== undefined && id === ATTRIBUTE_VALUE_ID){
            firstElement.remove();
        }
    }
}

//                      for create robot or task

var CREATE_ROBOT_FORM_ID = "formCreateRobot";
var CREATE_TASK_FORM_ID = "formCreateTask";
var HEADER_KEY_CONTENT_TYPE = "Content-Type";
var HEADER_VALUE_CONTENT_TYPE = "application/x-www-form-urlencoded";
var INPUT_TYPE_TEXT = 'input[type="text"]';

function createRobotOrTask(name) {
    var form;
    switch(name){
        case TASK:
            form = document.forms[CREATE_TASK_FORM_ID];
            break;
        case ROBOT:
            form = document.forms[CREATE_ROBOT_FORM_ID];
            break;
    }
    var inputText = form.querySelectorAll(INPUT_TYPE_TEXT)[0];
    var body = parseForm(form);
    var xhr = sendPost(body);
    inputText.value = EMPTY;

    xhr.addEventListener(READY_STATE_CHANGE, function(){
        if(this.readyState !== 4) return;
        if(this.status === 200){
            hideErrorMessage(form);
        } else {
            showErrorMessage(this, form);
        }
    });

}

function parseForm(form) {
    result = parseElement(form, 'input');
    var select = parseElement(form, 'select');
    if(result.length > 0 && select.length > 0){
        result += "&" + select;
    }
    return result;
}

function parseElement(form, element){
    var result = "";
    var elements = form.querySelectorAll([element]);
    for(var elem of elements){
        if(elem.type === 'checkbox' && elem.checked === false){
            continue;
        }
        result = appendToQuery(result, elem.name, elem.value);
    }
    return result;
}

function appendToQuery(query, key, value){
    if(query === undefined || query === null){
        query = EMPTY;
    }
    if(query.length > 0){
        query += "&";
    }
    return query += key + "=" + encodeURIComponent(value);
}

//                      for challenge

var ROBOT_TYPES_NAME = {
    "BACK_END_DEVELOPER":"back",
    "FRONT_END_DEVELOPER":"front",
    "HR":"hr",
    "GENERAL":"general"
}

function challenge() {
    var form = document.getElementById(GIVE_TASK_FORM_ID);
    var body = parseForm(form);
    var robotType = findRobotType(form);
    var body = appendToQuery(body, NAME_FOR_ROBOT_TYPE_KEY, robotType);
    var xhr = sendPost(body);
    xhr.addEventListener(READY_STATE_CHANGE, function(){
        if(this.readyState !== 4) return;
        if(this.status === 200){
            hideErrorMessage(form);
        } else {
            showErrorMessage(this, form);
        }
    });
}

function findRobotType(form){
    var result = "";
    var select = form.querySelectorAll(['select[name="taskName"]'])[0];
    var taskName = select.value;
    for(var type in jsonDataTasks){
        var task = jsonDataTasks[type][taskName];
        if(task !== undefined && task !== null){
            result = ROBOT_TYPES_NAME[type];
        }
    }
    return result;
}

function sendPost(body) {
    var xhr = new XMLHttpRequest();
    xhr.open(POST_METHOD, URL, true);
    xhr.setRequestHeader(HEADER_KEY_AJAX, HEADER_VALUE_AJAX);
    xhr.setRequestHeader(HEADER_KEY_CONTENT_TYPE, HEADER_VALUE_CONTENT_TYPE);
    xhr.send(body);
    return xhr;
}

//                       show messages from server

var P_TAG = "p";
var HOST = window.location.host;
var PATH = window.location.pathname;
var WEB_CTX = PATH.substring(0, PATH.indexOf('/', 1));
var END_POINT_URL = "ws://" + HOST + WEB_CTX + "/chat";
var ID_MESSAGE_BLOCK = "messagesBlock";
var MESSAGES_BLOCK;
var KEY_DELIMITER = ':';

var chatClient = null;

function connect () {
    chatClient = new WebSocket(END_POINT_URL);
    MESSAGES_BLOCK = document.getElementById(ID_MESSAGE_BLOCK);
    chatClient.onmessage = function (event) {
        var message = extractKey(event.data);
        showMessage(message);
    };

    chatClient.onerror = function (event) {
        showMessage(event.message, true);
    }
}

function disconnect () {
    chatClient.close();
}

function extractKey(message){
    var indexColon = message.indexOf(KEY_DELIMITER);
    if(indexColon > 0){
        var key = message.substring(0, indexColon);
        var confirmation = false;
        switch(key){
            case ROBOT:
                init(NEED_ROBOTS);
                confirmation = true;
                break;
            case TASK:
                init(NEED_TASKS);
                confirmation = true;
                break;
        }
        if(confirmation){
            message = message.substring(indexColon + 1);
        }
    }
    return message;
}

function showMessage(message, isError) {
    var messageElement = document.createElement(P_TAG);
    if(isError){
        messageElement.style.color = ERROR_COLOR;
    }
    messageElement.innerText = message;
    MESSAGES_BLOCK.appendChild(messageElement);
}

//                       hide robot name

var CHECKBOX_ID = "checkboxRobotNames";

function hideRobotName(){
    var checkboxElement = document.getElementById(CHECKBOX_ID);
    var selectElement = checkboxElement.nextElementSibling;
    selectElement.hidden = !checkboxElement.checked;
}