import java.util.List;

//Класс-обертка для работы с запросами внутри каналов данных
public abstract class DataChannel implements VacancyGiver {
    //Поля класса
    protected VacanciesRequest request = null; //Запрос к каналу данных
    protected static String rawData = null; //Строка сырых данных, полученных по запросу
    protected List <Vacancy> vacancies = null; //Список данных о вакансиях

    //Методы класса

    //Метод для добавления данных о запросе
    public abstract void newRequest(String keyword, int experience, int employment, int schedule, boolean salaryExist, boolean intership);

    //Метод для проверки создан ли запрос
    public abstract boolean isRequestExist();

    //Метод для возврата сформированного запроса
    public abstract String getRequest();

    //Метод для возврата сырых данных
    public abstract String getRawData();

    //Метод для возврата адреса соединения с каналом данных
    public String getURL(){
        if (this instanceof DataChannelHH) {
            return "https://api.hh.ru";
        } else if(false){
            //Канал ???
        }
        return "";
    }

    //Метод для возврата канала
    public String getChannel(){
        if (this instanceof DataChannelHH) {
            return "HeadHunter";
        } else if(false){
            //Канал ???
        }
        return "";
    }

    //Метод для подключения к серверу и получения сырых данных
    public abstract void takeRawData();

    //Метод для обработки сырых данных о вакансиях
    public abstract void produceData(String currentRequest);

    //Метод для возврата подготовленных данных о вакансиях
    public abstract List <Vacancy> getData();
}
