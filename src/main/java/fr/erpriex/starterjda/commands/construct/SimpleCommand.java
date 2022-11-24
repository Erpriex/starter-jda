package fr.erpriex.starterjda.commands.construct;

import java.lang.reflect.Method;

public class SimpleCommand {

    private final String name, description;
    private Command.ExecutorType executorType;
    private final Object object;
    private final Method method;

    public SimpleCommand(String name, String description, Command.ExecutorType executorType, Object object, Method method) {
        super();
        this.name = name;
        this.description = description;
        this.executorType = executorType;
        this.object = object;
        this.method = method;
    }

    public Command.ExecutorType getExecutorType() {
        return executorType;
    }

    public void setExecutorType(Command.ExecutorType executorType) {
        this.executorType = executorType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Object getObject() {
        return object;
    }

    public Method getMethod() {
        return method;
    }

}
