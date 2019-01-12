import java.util.List;

//Интерфейс для работы с запросами к каналам данных
interface VacancyGiver {
    //Метод для добавления данных о запросе
    void newRequest(String keyword, int experience, int employment, int schedule, boolean salaryExist, boolean intership);

    //Метод для проверки создан ли запрос
    boolean isRequestExist();

    //Метод для возврата сформированного запроса
    String getRequest();

    //Метод для возврата канала получения вакансии
    String getChannel();

    //Метод для возврата подготовленных данных о вакансиях
    List <Vacancy> getData();
}
