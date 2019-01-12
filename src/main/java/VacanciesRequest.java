//Класс для хранения запроса к каналу даных
public class VacanciesRequest {
    //Поля класса
    private String keyword; //Поисковой запрос
    private int experience = 0; //Опыт работы (0 - нет, 1 - от 1 до 3-х лет)
    private int employment = 0; //Занятость (0 - стажировка, 1 - частичная занятость, 2 - полная занятость)
    private int schedule = 0; //График (0 - гибкий, 1 - удаленная работа, 2 - полный рабочий день)
    private boolean salaryExist = false; //Показывать вакансии только с указанной зарплатой (1) или все (0)
    private boolean intership = false;//Показывать только вакансии для флага "Начало карьеры" или "Стажировка" (0 или 1)

    //Конструктор класса
    public VacanciesRequest(String keyword, int experience, int employment, int schedule, boolean intership, boolean salaryExist) {
        this.keyword = keyword;
        this.experience = experience;
        this.employment = employment;
        this.schedule = schedule;
        this.intership = intership;
        this.salaryExist = salaryExist;
    }

    //Методы возврата полей класса
    public String getKeyword() {
        return keyword;
    }

    public int getExperience() {
        return experience;
    }

    public int getEmployment() {
        return employment;
    }

    public int getSchedule() {
        return schedule;
    }

    public boolean isIntership() {
        return intership;
    }

    public boolean isSalaryExist() {
        return salaryExist;
    }
}
