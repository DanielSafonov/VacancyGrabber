/*

oo     oo     Учебный проект "VacanciesGrabber"
o       o
o   o   o     Daniil Safonov
o       o     daniil.safonov@gmail.com
oo     oo     2018

Maven: JSOUP, GSON

*/

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;

public class Main {

    final static private Scanner in = new Scanner(System.in); //Объект для ввода с консоли

    public static void main(String[] args) {
        //Приветсвенное сообщение
        System.out.println("********************************************");
        System.out.println("VacanciesGrabber - автоматическая агрегация ");
        System.out.println("и выдача в удобочитаемом формате вакансий   ");
        System.out.println("для начинающего айтишника                   ");
        System.out.println("********************************************");

        //Проверка наличия входных параметров
        //Входной параметр "-refresh" - обновить данные по запросам из файла и сгенерировать ответ
        //Входной параметр "-new" - создать новый набор параметров и сгенерировать ответ
        //Входного параметра нет или он не соответсвует существующим - вывести справку
        /* REFRESH */

        /* NEW */
        List<List<VacancyGiver>> dataChannels = null; //Список запросов к каналам данных

        dataChannels = selectChannels(); //Выбор используемых для сбора данных каналов пользователем

        dataChannels = generateRequest(dataChannels); //Создание запросов для каждого из выбранных каналов пользователем
        printCreatedRequests(dataChannels); //Вывести список каналов и запросов

        getData(dataChannels); //Получить данные по каждой из вакансий

        generateExportPage(dataChannels); //Сгенерировать html страницу с данными из списка
    }

    //Выбор используемых для сбора данных каналов
    public static List<List<VacancyGiver>> selectChannels() {
        List<List<VacancyGiver>> dataChannels = new ArrayList<List<VacancyGiver>>();

        System.out.print("\n");
        System.out.println("Выберите набор каналов данных, в которых будет производится поиск вакансий:");
        System.out.println("1 - Добавить все");
        System.out.println("2 - HeadHunter");
        System.out.println("Ввод флагов в формате 0 1 2 0, где 0 - канал не будет создан, n - количество запросов к каналу ");
        System.out.print("Ввод > ");

        int input = 0; //Введенное пользователем число
        boolean somethingSelected = false; //Выбран ли хотя бы один канал
        for (int i = 0; i < 2; i++) {
            if (in.hasNextInt()) {
                //В потоке ввода есть/появилось целое число
                input = in.nextInt(); //Записываем число в массив
                //Необходимо создать канал
                if (input > 0) {

                    somethingSelected = true; //Хотя бы один канал выбран
                    System.out.print("\n"); //Перевод каретки

                    //Порядковый номер флага
                    switch (i) {
                        case 0:
                            //Добавить все
                            System.out.println("Добавить все каналы..");
                            dataChannels.add(new ArrayList<VacancyGiver>()); //Добавить канал HH
                            //dataChannels.add(new ArrayList<VacancyGiver>()); //Добавить канал ???

                            //Создание нужного числа запросов к каналу
                            for (int j = 0; j < input; j++) {
                                dataChannels.get(dataChannels.size() - 1).add(new DataChannelHH()); //Добавляем пустой запрос
                                System.out.println("HeadHunter" + (j + 1));
                                System.out.println("???" + (j + 1));
                            }
                            return dataChannels; //Вовзрат созданного списка списков
                        case 1:
                            //Добавить HH
                            dataChannels.add(new ArrayList<VacancyGiver>()); //Добавить HH

                            //Создание нужного числа запросов к каналу
                            for (int j = 0; j < input; j++) {
                                dataChannels.get(dataChannels.size() - 1).add(new DataChannelHH()); //Добавляем пустой запрос
                                System.out.println("HeadHunter" + (j + 1));
                            }
                            System.out.println("Добавлен HeadHunter");
                            break;
                    }
                }
            }
        }

        //Не выбран ни один канал
        if (somethingSelected == false) {
            System.out.print("\n");
            System.out.println("Не выбран ни один канал для сбора вакансий!");
            System.out.println("Завершение программы..");
            System.exit(0);
        }

        return dataChannels; //Вовзрат созданного списка списков
    }

    //Ввод полльзователем запроса к каналу данных
    public static List<List<VacancyGiver>> generateRequest(List<List<VacancyGiver>> dataChannels) {
        String input; //Введенная пользователем строка
        String inputElements[]; //Разбитая на элементы строка

        //Цикл для обхода списка каналов
        for (int i = 0; i < dataChannels.size(); i++) {

            //Проверка списка запросов на принадлежность к одному из каналов
            if (dataChannels.get(i).get(0) instanceof DataChannelHH) {
                //Канал данных Head Hunter
                System.out.print("\n");
                System.out.println("Канал данных Head Hunter - запросов: " + dataChannels.get(i).size());

                //Цикл для обхода списка запросов внутри канала
                for (int j = 0; j < dataChannels.get(i).size(); j++) {
                    System.out.print("\n");
                    System.out.println("Сформируйте запрос в формате: java 0 0 0 0 0");
                    System.out.print("\n");
                    System.out.println("1. Ключевое слово");
                    System.out.println("2. Опыт работы: 0 - без опыта, 1 - от 1 до 3-х лет");
                    System.out.println("3. Тип занятости: 0 - стажировка, 1 - частичная занятость");
                    System.out.println("4. График работы: 0 - гибкий, 1 - удаленная работа, 2 - полный рабочий день");
                    System.out.println("5. Начало карьеры: 0 - не важно, 1 - да");
                    System.out.println("6. Зарплата указана: 0 - не важно, 1 - да");
                    System.out.print("Запрос > ");

                    if (j == 0) {
                        in.nextLine(); //Костыль
                    }
                    input = in.nextLine(); //Считать строку

                    //Разделить введенную строку по пробелам на массив подстрок
                    inputElements = input.split("\\s+");

                    //Проверка формата введенной строки
                    if (inputElements.length == 6 && Integer.class.isInstance(Integer.parseInt(inputElements[1])) && Integer.class.isInstance(Integer.parseInt(inputElements[2])) && Integer.class.isInstance(Integer.parseInt(inputElements[3])) && Integer.class.isInstance(Integer.parseInt(inputElements[4])) && Integer.class.isInstance(Integer.parseInt(inputElements[5]))) {
                        //Приведение числовых выражений к строковым
                        if (inputElements[4].equals("1")) {
                            inputElements[4] = "true";
                        } else {
                            inputElements[4] = "false";
                        }
                        if (inputElements[5].equals("1")) {
                            inputElements[5] = "true";
                        } else {
                            inputElements[5] = "false";
                        }

                        //Передача значений в метод генерации данных запроса
                        dataChannels.get(i).get(j).newRequest(inputElements[0], Integer.parseInt(inputElements[1]), Integer.parseInt(inputElements[2]), Integer.parseInt(inputElements[3]), Boolean.parseBoolean(inputElements[4]), Boolean.parseBoolean(inputElements[5]));
                        if (dataChannels.get(i).get(j).isRequestExist()) {
                            System.out.println("OK");
                        } else {
                            System.out.print("\n");
                            System.out.println("Запрос не удалось сформировать!");
                            System.out.println("Завершение программы..");
                            System.exit(0);
                        }
                    } else {
                        System.out.print("\n");
                        System.out.println("Формат переданного запроса не соответсвует шаблону!");
                        System.out.println("Завершение программы..");
                        System.exit(0);
                    }
                }
            } else if (false) {
                //Канал данных ???
            }
        }
        return dataChannels;
    }

    //Вывести список каналов и запросов
    public static void printCreatedRequests(List<List<VacancyGiver>> dataChannels) {
        System.out.print("\n");
        System.out.print("Созданные запросы:");

        //Цикл для обхода списка списков каналов
        for (int i = 0; i < dataChannels.size(); i++) {
            //Проверка списка запросов на принадлежность к одному из каналов
            if (dataChannels.get(i).get(0) instanceof DataChannelHH) {
                //Канал данных Head Hunter
                System.out.print("\n");
                System.out.println("Канал данных Head Hunter - запросов: " + dataChannels.get(i).size());
            } else if (false) {
                //Канал данных ???
            }

            //Цикл для обхода списка запросов внутри канала
            for (int j = 0; j < dataChannels.get(i).size(); j++) {
                System.out.println(dataChannels.get(i).get(j).getRequest());
            }
        }
    }

    //Получить собранные и обработанные даннные
    public static void getData(List<List<VacancyGiver>> dataChannels) {
        //Цикл для обхода списка списков каналов
        for (int i = 0; i < dataChannels.size(); i++) {
            //Цикл для обхода списка запросов внутри канала
            for (int j = 0; j < dataChannels.get(i).size(); j++) {
                //Получить список вакансий для данного запроса
                dataChannels.get(i).get(j).getData();
            }
        }
    }

    //Сгенерировать html страницу с данными из списка
    public static void generateExportPage(List<List<VacancyGiver>> vacancies) {
        System.out.println("Генерация html-страницы..");
        Document page = loadExportPageTemplate(); // Загрузить шаблон HTML-страницы
        insertDataToPage(page, vacancies); // Произвести вставку данных в поля HTML-шаблона
    }

    // Загрузить шаблон HTML-страницы
    public static Document loadExportPageTemplate() {
        Document page = null;

        File pageFile = new File("./src/main/resources/VacancyGrabberTemplate.html"); // Пробуем открыть файл

        if (pageFile.isFile()) {
            // Файл существует
            if (pageFile.length() > 0) {
                // Файл не пуст
                try {
                    page = Jsoup.parse(pageFile, "utf-8");
                    System.out.println("Файл шаблона успешно открыт!\n");
                    return page;
                } catch (IOException e) {
                    System.out.println("При чтении файла шаблона возникла ошибка!");
                    e.printStackTrace();
                    System.out.println("Завершение программы..");
                    System.exit(0); //Завершение программы
                }
            } else {
                // Файл пуст
                System.out.println("Файл шаблона пуст!");
                System.out.println("Завершение программы..");
                System.exit(0); //Завершение программы
            }
        } else {
            // Такого файла не существует
            System.out.println("Такого файла не существует!");
            System.out.println("Завершение программы..");
            System.exit(0); //Завершение программы
        }
        return null;
    }

    // Произвести вставку данных в поля HTML-шаблона
    public static void insertDataToPage(Document page, List<List<VacancyGiver>> vacanciesAndChannels) {
        String request = "", channel = ""; //Текущие запрос и канал
        String baseSelector = null; //Базовый селектор для текущего блока
        int vacanciesCount = 0, headersCount = 0; //Счетчики вакансий и блоков вакансий (для #id)
        List <Vacancy> vacancyList = new ArrayList<Vacancy>(); //Список найденных вакансий

        //Элементы страницы
        Element blockVacancyTemplate = page.selectFirst(".block#0"); //Шаблон блока вакансии

        Element vacanciesBlockTemplate = page.selectFirst(".vacanciesBlock"); //Шаблон блока вакансий
        vacanciesBlockTemplate.select(".blocks").empty(); //Очистка списка вакансий

        Element insertBuff = page.clone(); //Буфер для вставки
        insertBuff.empty(); //Очистка буфера

        //Проверка получения элементов шаблона программой
        if (vacanciesBlockTemplate == null || blockVacancyTemplate == null) {
            //Файл шаблона поврежден!
            System.out.println("Файл шаблона поврежден!");
            System.out.println("Завершение программы..");
            System.exit(0);
        }

        //Удаление блоков шаблона
        page.selectFirst(".main-wrapper").empty(); //Удаляет дочерние элементы для .main-wrapper

        //Цикл для обхода списка списков каналов
        for (int i = 0; i < vacanciesAndChannels.size(); i++) {
            //Цикл для обхода списка запросов внутри канала
            for (int j = 0; j < vacanciesAndChannels.get(i).size(); j++) {
                channel = vacanciesAndChannels.get(i).get(j).getChannel(); //Получить канал данных
                request = vacanciesAndChannels.get(i).get(j).getRequest(); //Получить запрос к каналу данных
                vacancyList = vacanciesAndChannels.get(i).get(j).getData(); //Получить список вакансий

                //Генерация блока вакансий (шапки)
                headersCount++; //Увеличение счетчика блоков вакансий
                //Вставка в буфер шаблона шапки с нужным id
                if(insertBuff.toString().length() == 0){
                    //Вставка в корень .main-wrapper
                    insertBuff.html(vacanciesBlockTemplate.attr("id", Integer.toString(headersCount)).toString());
                } else{
                    //Вставка после последнего блока вакансий
                    insertBuff.select(".vacanciesBlock").last().after(vacanciesBlockTemplate.attr("id", Integer.toString(headersCount)).toString());
                }

                //Вставка данных в шаблон
                //Вставка канала и запроса в блок шапки
                insertBuff.select(".vacanciesBlock#" + Integer.toString(headersCount) + " .section" + " .channel").html(channel + ": ");
                insertBuff.select(".vacanciesBlock#" + Integer.toString(headersCount) + " .section" + " .request").html(request);

                //Вставка количества вакансий в блок шапки
                insertBuff.select(".vacanciesBlock#" + Integer.toString(headersCount) + " .section" + " .vacancies_count").html("Найдено вакансий: " + Integer.toString(vacancyList.size()));

                //Генерация блоков списка вакансий
                //Обход списка вакансий
                for(int k = 0; k < vacancyList.size(); k++){
                    vacanciesCount++; //Увеличение счетчика вакансий
                    //Вставка в буфер шаблона вакансии с нужным id
                    insertBuff.select(".vacanciesBlock#" + Integer.toString(headersCount) + " .blocks").append(blockVacancyTemplate.attr("id", Integer.toString(vacanciesCount)).toString());
                    //Вставка данных в шаблон
                    //Название
                    insertBuff.select(".block#" + Integer.toString(vacanciesCount) + " .header").html(vacancyList.get(k).getName());
                    //Компания
                    insertBuff.select(".block#" + Integer.toString(vacanciesCount) + " .employer").html(vacancyList.get(k).getEmployer());
                    //Зарплата
                    insertBuff.select(".block#" + Integer.toString(vacanciesCount) + " .salary").html(vacancyList.get(k).getSalary());
                    //Описание
                    insertBuff.select(".block#" + Integer.toString(vacanciesCount) + " .description-text").html(vacancyList.get(k).getDescription());
                    //Ссылка
                    insertBuff.select(".block#" + Integer.toString(vacanciesCount) + " .link").attr("href", vacancyList.get(k).getLink());
                }

            }
        }

        //Вставка буфера
        page.select(".main-wrapper").html(insertBuff.toString());

        //Сохранение сгенерированной страницы в файл
        savePageToFile(page);
    }

    // Сохранить страницу в файл
    public static void savePageToFile(Document page) {
        FileWriter fileWriter = null; // Поток вывода
        try {
            File pageFile = new File("./src/main/resources/vacanciesExport.html");
            // Существует ли файл
            if (!pageFile.exists()) {
                pageFile.createNewFile();// Создаем новый файл
            }

            fileWriter = new FileWriter(pageFile); // Поток вывода

            // Запись кода страницы в файл
            fileWriter.write(page.toString());
            fileWriter.flush();
            System.out.println("Страница успешно сохранена!\n");
        } catch (IOException e) {
            System.out.println("При сохранении страницы возникла ошибка!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("При сохранении страницы возникла ошибка!");
                e.printStackTrace();
            }
        }
    }


}

//TODO: исправить фильтры поиска вакансий, поправить выравнимание ссылки на вакансию
//TODO: УБРАТЬ ФЛАГ "ГИБКИЙ ГРАФИК"
//TODO: общая шапка для статистики по найденным вакансиям + дата и время обновления

//TODO: "найдено вакансий" - жирным
//TODO: ОБРАБОТКА ОТСУТСВИЯ ВАКАНСИЙ ПО ЗАПРОСУ
//TODO: ОБРАБОТКА СИТУАЦИЙ ОТСТУСТВИЯ ДАННЫХ (НЕТ СОЕДИНЕНИЯ ИЛИ ЕЩЕ ЧТО)
//TODO: ПЕРЕПИСАТЬ МЕТОД getData (один метод тупо отдает список, а другой - получает и обрабатывает данные)
//TODO: поправить порядок формирования запроса для HH, чтобы было как на сайте
//TODO: поправить вывод в консоль
//TODO: файл конфига, в котором указан путь для экспортируемого файла и файла с предыдущим набором запрсов и тд
//TODO: выводить количество найденных вакансий по каждому из запросов и общее количество вакансий в консоль
//TODO: Вывести абсолютный адрес сгенерированной страницы
//TODO: НЕТ ССЫЛКИ
//TODO: Показывать адрес компании на карте
//TODO: Указывать станцию метро
//TODO: Указывать дату публикации
//TODO: Переписать на java.nio.File
//TODO: Добавить на страницу блок со ссылками на Яндекс, MailRU и прочие стажировки