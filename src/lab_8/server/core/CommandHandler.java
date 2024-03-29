package lab_8.server.core;

import lab_8.message.Account;
import lab_8.message.Message;
import lab_8.server.DatabaseSQL;
import lab_8.world.creation.Dancer;
import lab_8.world.state.FeelState;
import lab_8.world.state.PositionState;
import lab_8.world.state.ThinkState;

import static lab_8.server.Database.*;
import static lab_8.server.DatabaseSQL.getFromDB;
import static lab_8.server.DatabaseSQL.getInfoSQL;

/**
 * Класс, который содержит методы для выполнения команд, приходящих с клиента на сервер, и создания ответа клиенту.
 */
class CommandHandler {
    /**
     * Метод, реализующий вызов команды в зависимости от текста сообщения, пришедшего в объекте Message.
     * @param message Объект Message, содержащий название команды, которую необходимо выполнить.
     * @return Объект Message полученный после выполнения команды.
     */
    static Message command(Message message){
        Account user = accounts.get(message.login);
        if (message.time > user.lastAccessTime) {
            user.lastAccessTime = message.time;
            accounts.put(message.login, user);
        }
        else{
            Message response = new Message();
            response.text = "Hello from the Mesozoic";
            response.login = message.login;
            response.time = message.time;
            return response;
        }
        if (message.text.length() > 3 && message.text.substring(0,4).equals("help"))
            return getHelpMessage();
        if (message.text.length() > 9 && message.text.substring(0,10).equals("disconnect"))
            return disconnect(message);
        if (message.text.length() > 3 && message.text.substring(0,4).equals("show"))
            return show(message);
        if (message.text.length() > 9 && message.text.substring(0,10).equals("add_if_max"))
            return add_if_max(message);
        if (message.text.length() > 9 && message.text.substring(0,10).equals("add_if_min"))
            return add_if_min(message);
        if (message.text.length() > 2 && message.text.substring(0,3).equals("add"))
            return add(message);
        if (message.text.length() > 5 && message.text.substring(0,6).equals("remove"))
            return remove(message);
        if (message.text.length() > 3 && message.text.substring(0,4).equals("info"))
            return info();
        if (message.text.length() > 8 && message.text.substring(0,9).equals("new table"))
            return newTable();
        return new Message();
    }

    private static Message newTable()
    {
        Message response = new Message();
        if(DatabaseSQL.newTableDefault())
        {
            response.text = "success";
        }
        else { response.text = "error"; }
        return response;
    }

    /**
     * Метод, реализующий отключение клиента от сервера.
     * @param message Объект Message, содержащий логин клиента, отключаемого от сервера.
     * @return Объект Message с текстовым сообщением о о завершении отключения.
     */
    private static Message disconnect(Message message){
        Account user = accounts.get(message.login);
        user.lastAccessTime = 0;
        user.secretKey = null;
        user.random = null;
        accounts.put(message.login, user);
        Message response = new Message();
        response.text = "disconnect";
        response.login = message.login;
        response.time = message.time;
        return response;
    }

    /**
     * Метод возвращающий объект Message с текстовым списком команд сервера и их описанием.
     * @return Объект Message с текстовым списком команд сервера и их описанием.
     */
    private static Message getHelpMessage(){
        StringBuffer stringBuffer = new StringBuffer()
                .append("help\n")
                .append("--- Commands ---\n")
                .append("help - Вывести в стандартный поток вывода помощь по командам\n")
                .append("disconnect - выполнить корректное отключение от сервера и уничтожить сессионный AES256 ключ\n")
                .append("import - загрузить элементы коллекции из файла по пути переменной окружения COLLECTION_PATH в коллекцию на сервере\n")
                .append("add {...} - Добавить новый элемент в коллекцию\n")
                .append("add_if_min {...} - Добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n")
                .append("add_if_max {...} - Добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции\n")
                .append("remove {...} - Удалить элемент из коллекции по его значению\n")
                .append("show - Вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n")
                .append("info - Вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, дата последнего изменения, количество элементов)\n")
                .append("\n\n--- JSON params ---\n")
                .append("--- [ЗНАЧЕНИЕ] : [описание] ---\n")
                .append("-------------------\n")
                .append("=== name ===\n")
                .append("====== String : Строка с именем\n")
                .append("============\n")
                .append("=== danceQuality ===\n")
                .append("====== int : Начальное количество \"dance points\"\n")
                .append("====================\n");
        stringBuffer.append("=== dynamics ===\n");
        for (PositionState iter : PositionState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("================\n");
        stringBuffer.append("=== feel ===\n");
        for (FeelState iter : FeelState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("============\n");
        stringBuffer.append("=== think ===\n");
        for (ThinkState iter : ThinkState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("=============\n");
        stringBuffer.append("=== position ===\n");
        for (PositionState iter : PositionState.values())
            stringBuffer.append(String.format("====== %s : %s\n", iter.name(), iter.toString()));
        stringBuffer.append("=============\n");
        Message response = new Message();
        response.text = stringBuffer.toString();
        return response;
    }

    /**
     * Метод, создающий объект типа Message, в котором находится коллекция всех объектов, находящихся на сервере.
     * Реализация с помощью Stream API.
     * @return объект типа Message с коллекцией элементов.
     */
    private static Message show(Message request){
        Message response = new Message();
        response.text = "show";
        response.values.addAll(DatabaseSQL.getFromDB());
        return response;
    }

    /**
     * Метод, добавляющий коллекцию объектов, пришедший в объекте типа Message, в коллекцию на сервере.
     * Реализация с помощью Stream API.
     * @param request Объект Message, который содержит коллекцию элементов, которые необходимо добавить в коллекцию на сервере.
     * @return Объект Message, содержащий текст об успешном добавлении элементов в коллекцию.
     */
    private static Message add(Message request){
        Message response = new Message();
        try {
            request.values.parallelStream().map(o -> (Dancer) o).forEach(dancer -> DatabaseSQL.insertToDB(dancer, request.login));
        } catch (Exception e){e.printStackTrace();}
        response.text = "add success";
        return response;
    }

    /**
     * Метод, выполняющий добавление каждого элемента из коллекции объекта Messsage, если этот элемент больше или равен максимальному,
     * имеющемуся в коллекции на сервере.
     * @param request Объект Message, который содержит коллекцию элементов.
     * @return Объект Message, содержащий текст об успешности добавления элементов в коллекцию.
     */
    private static Message add_if_max(Message request){
        Message response = new Message();
        if (getFromDB().isEmpty()){
            response.text = "add_if_max failed";
            return response;
        }
        Dancer dancerMax = DatabaseSQL.getFromDB().stream().max((dancer, t1) -> (dancer.getDanceQuality() - t1.getDanceQuality())).get();
        try {
            request.values.parallelStream().map(o -> (Dancer)o).filter(o -> (o.getDanceQuality() >= dancerMax.getDanceQuality())).forEach(dancer -> DatabaseSQL.insertToDB(dancer, request.login));
        } catch (Exception e) {e.printStackTrace();}
        response.text = "add_if_max success";


        return response;
    }
    /**
     * Метод, выполняющий добавление каждого элемента из коллекции объекта Message, если этот элемент меньше или равен максимальному,
     * имеющемуся в коллекции на сервере.
     * @param request Объект Message, который содержит коллекци элементов.
     * @return Объект Message, содержащий текст об успешности добавления элементов в коллекцию.
     */
    private static Message add_if_min(Message request){
        Message response = new Message();
        if (DatabaseSQL.getFromDB().isEmpty()){
            response.text = "add_if_min failed";
            return response;
        }
        Dancer dancerMin = DatabaseSQL.getFromDB().stream().min((dancer, t1) -> (dancer.getDanceQuality() - t1.getDanceQuality())).get();
        try {
            request.values.parallelStream().map(o -> (Dancer)o).filter(o -> (o.getDanceQuality() <= dancerMin.getDanceQuality())).forEach(dancer -> DatabaseSQL.insertToDB(dancer, request.login));
        } catch (Exception e) {e.printStackTrace();}
        response.text = "add_if_min success";
        return response;
    }
    /**
     * Метод, выполняющий удаление из коллекции на сервере каждого элемента,
     * который соответствует одному из элементов коллекции объекта Messsage.
     * @param request Объект Message, который содержит коллекцию элементов.
     * @return Объект Message, содержащий текст об успешном удалении элементов из коллекции.
     */
    private static Message remove(Message request){
        Message response = new Message();
        request.values.parallelStream().map(o -> (Dancer)o).forEach(o -> DatabaseSQL.removeFromDB(o,request.login));
        response.text = "remove success";
        return response;
    }



    /**
     * Метод, возвращающий объект Message, содержащий информацию о коллекции, хранящейся на сервере.
     * @return Объект Message, содержащий информацию о коллекции, хранящейся на сервере.
     */
    private static Message info(){
        Message response = new Message();
        response.text = "info\n" + getInfoSQL();
        return response;
    }
}
