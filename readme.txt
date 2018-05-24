
Task description.
In the game world there are robots. You can add your own robots by implementing an interface or base class. Robots get tasks to run from the queue, and do these tasks. Tasks are sent by broadcasting or specifically to one robot, there is a REST API interface. Sometimes the task can mean-kill yourself, then the robot ceases to exist. There is a tracker activity robots, if it notices that someone is missing (killed himself) , or that robots do not have time to perform tasks-it creates a new robot type. All activities of the game world automatically, dynamically put out on the Web UI in the form of a log of actions. Also, the user has the opportunity to enter the task in the queue on the same page to a specific robot or all robots.

Description of the implementation of the game world.
The game world describes IT company in which robots of four types work: back-end developers, front-end developers, HR and just General robots capable to carry out only those tasks which any robot can carry out.
For these robots, there are tasks that correspond to their destination. Among the general tasks implemented the “task die”, which tells the robot that it is necessary to cease to exist.
After the task of self-destruction of the robot can not be restored again, it is only possible to create a new robot of the same type and with the same name.
To control robots, there is an operator who can create them, assign tasks to a particular robot or broadcast that them need to perform a task to a certain type of robots. Then the first liberated robot able to perform it will begin to perform it immediately.
For an operator, there is only one implementation: the recreator operator. This operator is able to determine whether the robot is alive and in the event that the robot is dead recreate in his image a new robot with the same type and same name. The ability of the operator to recreate robots can be disabled in the parameter, then it will remove the robot. If you try to assign a task to a dead robot, it will be a message that it is dead, and when broadcasting to report what robots have been removed.
For tracking robots provided interface tracker with the one implementation is the “monitor performance tracker”. This tracker for each task broadcast to robots to perform, counts a certain time and checks the number of tasks for this type of robots and the number of robots. If the number of tasks exceeds the number of robots, it creates a new robot necessary type to improve their performance and after the same period of time will make the check again. If the robots are smaller than the tasks, then the tracker will turn off their control until the operator again broadcasts a new task for that type of robots. You can change the period for checks in the tracker parameter.
When you create a task, it initially falls into the task holder, from where it can be assigned without recreating it after its execution.

Setting the world before starting.
In the class “by.roboworld.spring.RoboworldConfig” contains methods which determine the parameters of the application and can be changed or extended if necessary.

Output option
Here are the settings for output information about everything that happens in the game world.
Pass the desired implementation of "by.roboworld.output.Output" interface to the method OutputInformation.installOutput().
If the value of the “system” output will be displayed in the console.
If the value “webSocket” is set, the output will be made to the browser using the websocket technology.

Operator option
Here are the settings of the operator. You can select a specific implementation and specify it in a constant INIT_PARAM_VALUE_OPERATOR. Only the recreator is available and can be extended if necessary.
Below is an additional parameter passed to the constructor when creating an operator object. This parameter indicates the mode of recreating robots.

Tracker option
Here are the settings of the tracker used by the operator. Only the performance tracker specified in the constant is implemented INIT_PARAM_VALUE_TRACKER, which can be extended if necessary. In the constant INIT_PARAM_VALUE_TRACKER_PERIOD specify the period with which will be execute to monitor the performance of robots.