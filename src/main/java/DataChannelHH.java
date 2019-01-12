import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

public class DataChannelHH extends DataChannel {
    //Поля класса
    //VacanciesRequest request      - запрос к каналу данных
    //String rawData                - строка сырых данных, полученных по запросу
    //List <Vacancy> vacancies  - список данных о вакансиях
    private final static int TIMEOUT = 10000; //Константа времени ожидания соединения

    //Методы класса
    //.getURL(); - возврат base url канала

    //Метод для добавления данных о запросе
    @Override
    public void newRequest(String keyword, int experience, int employment, int schedule, boolean salaryExist, boolean intership){
        //Создать объект запроса к каналу данных
        request = new VacanciesRequest(keyword, experience, employment, schedule, salaryExist, intership);
    }

    //Метод для проверки создан ли запрос
    @Override
    public boolean isRequestExist() {
        if (request.equals(null)) {
            return false;
        }
        return true;
    }

    //Метод для возврата сформированного запроса
    @Override
    public String getRequest() {
        String pieceRequest = "/vacancies?area=1"; //Строка запроса с жесткой установкой поиска вакансий по Москве

        //Формирование строки запроса
        pieceRequest += "&text=" + request.getKeyword(); //Поисковой запрос

        //Опыт работы
        if(request.getExperience() == 0){
            pieceRequest += "&experience=noExperience";
        } else if(request.getExperience() == 1){
            pieceRequest += "&experience=between1And3";
        }

        //Тип занятости
        if(request.getEmployment() == 0){
            pieceRequest += "&employment=probation";
        } else if(request.getEmployment() == 1){
            pieceRequest += "&employment=part";
        } else if(request.getEmployment() == 2){
            pieceRequest += "&employment=full";
        }

        //Тип графика
        if(request.getSchedule() == 0){
            pieceRequest += "&schedule=flexible";
        } else if(request.getSchedule() == 1){
            pieceRequest += "&schedule=remote";
        } else if(request.getSchedule() == 2){
            pieceRequest += "&schedule=fullDay";
        }

        //Начало карьеры
        if(request.isIntership() == true){
            pieceRequest += "&specialization=15";
        }

        //Указана ли зарплата
        if(request.isSalaryExist() == true){
            pieceRequest += "&only_with_salary=true";
        }

        return pieceRequest;
    }

    //Метод для получения сырых данных
    @Override
    public void takeRawData() {
        String baseURL = this.getURL(); //Базовый адрес канала данных
        String request = this.getRequest(); //Запрос к каналу данных

        if (baseURL.length() > 0 && request.length() > 0) {
            //Базовый адрес и запрос успешно получены

            Connection.Response response = null; // Ответ сервера

            try {
                // Запрос к API
                response = Jsoup.connect(baseURL + request)
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; rv:40.0) Gecko/20100101 Firefox/40.0")
                        .ignoreContentType(true) // Игнорируем, что это не html/xml
                        .referrer("http://www.google.com").followRedirects(true).timeout(TIMEOUT).execute();

                if (response.statusCode() == 200) {
                    System.out.println("Запрос успешно выполнен! Код 200.\n");

                    //Заносим данные в поля объекта
                    rawData = response.body();
                } else {
                    // TODO: Какие еще коды при успешном запросе API может выдавать и нужна ли эта
                    // обработка? Как вообще корректно обрабатывать и выводить коды при неудачном
                    // соединении?
                    System.out.println("При выполнении запроса возникла ошибка: " + response.statusCode() + "\n");
                }
            } catch (IOException e) {
                // Возникла ошибка
                System.out.println("При соединении возникла ошибка!\n");
                e.printStackTrace();
            }
        }
    }

    //Метод для возврата сырых данных
    @Override
    public String getRawData() {
        return rawData;
    }

    //Метод для обработки сырых данных и заполнения полей списка вакансий
    @Override
    public void produceData(String currentRequest) {
        JsonObject jsonObjectData = new Gson().fromJson(rawData, JsonObject.class); // Объект JSON с сырыми данными
        JsonArray jsonArrayData = jsonObjectData.getAsJsonArray("items"); // Массив элементов из сырых данных

        JsonObject jsonSalary = null; // Объект JSON для хранения зарплаты
        JsonObject jsonEmployer = null; // Объект JSON для хранения работодателя
        JsonObject jsonDescription = null; // Объект JSON для хранения краткого описания вакансии
        String salary = null; // Строка с зарплатой
        String description = null; // Строка с кратким описанием вакансии

        for (int i = 0; i < jsonArrayData.size(); i++) {
            jsonObjectData = (JsonObject) jsonArrayData.get(i); // Записываем элемент массива в json объект

            // Зарплата
            if (!jsonObjectData.get("salary").isJsonNull()) {
                jsonSalary = jsonObjectData.get("salary").getAsJsonObject(); // Объект с описанием зарплаты
                // Формирование строки зарплаты
                if (!jsonSalary.get("from").isJsonNull()) {
                    salary = "от " + jsonSalary.get("from").getAsString();
                }
                if (!jsonSalary.get("to").isJsonNull()) {
                    salary = salary + " до " + jsonSalary.get("to").getAsString();
                }
                if (!jsonSalary.get("currency").isJsonNull()) {
                    salary = salary + " " + jsonSalary.get("currency").getAsString();
                }
                if (!jsonSalary.get("gross").isJsonNull()) {
                    if (jsonSalary.get("gross").getAsString() == "true") {
                        salary = salary + " до вычета НДФЛ";
                    } else {
                        salary = salary + " на руки";
                    }
                }
            } else {
                salary = "Не указана";
            }

            // Работодатель
            if (!jsonObjectData.get("employer").isJsonNull()) {
                jsonEmployer = jsonObjectData.get("employer").getAsJsonObject(); // Объект с описанием работодателя
            }

            // Описание
            if (!jsonObjectData.get("snippet").isJsonNull()) {
                jsonDescription = jsonObjectData.get("snippet").getAsJsonObject(); // Объект с кратким описанием вакансии
                if (!jsonDescription.get("responsibility").isJsonNull()) {
                    description = jsonDescription.get("responsibility").getAsString();
                }
                if (!jsonDescription.get("requirement").isJsonNull()) {
                    description = description + "<br/>" +jsonDescription.get("requirement").getAsString();
                }
            }

            // Запись полученных данных в элемент массива
            vacancies.add(new Vacancy("HeadHunter", currentRequest, jsonObjectData.get("name").getAsString(), "https://hh.ru/vacancy/"+ jsonObjectData.get("id").getAsString(), salary, jsonEmployer.get("name").getAsString(), description));
        }
    }

    //Метод для получения, обработки и возврата набора данных
    @Override
    public List <Vacancy> getData() {
        String currentRequest = this.getRequest();
        vacancies = new ArrayList<Vacancy>(); //Создать список вакансий
        takeRawData(); //Получить сырые данные
        produceData(currentRequest); //Обработать данные
        return vacancies;
    }
}
