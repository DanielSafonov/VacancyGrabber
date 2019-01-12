//Класс для хранения данных о вакансии
public class Vacancy {
    //Поля класса
    //Обработанные данные
    private String channel; //К какому каналу относится вакансия
    private String request; //Поисковой запрос, по которому найдена вакансия
    private String name; //Название
    private String link; //Ссылка на вакансию
    private String salary; //Размер заработной платы
    private String employer; //Работодатель
    private String description; //Описание вакансии

    //Методы класса
    //Конструктор
    public Vacancy(String channel, String request, String name, String link, String salary, String employer, String description) {
        this.channel = channel;
        this.request = request;
        this.name = name;
        this.link = link;
        this.salary = salary;
        this.employer = employer;
        this.description = description;
    }

    //Геттеры
    public String getChannel() {
        return channel;
    }

    public String getRequest() {
        return request;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getSalary() {
        return salary;
    }

    public String getEmployer() {
        return employer;
    }

    public String getDescription() {
        return description;
    }
}
