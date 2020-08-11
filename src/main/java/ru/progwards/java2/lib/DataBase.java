package ru.progwards.java2.lib;

import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public enum DataBase {
    INSTANCE;

    public final static String DB_PATH = "c:/consult_db/";

    public Users users = new Users();
    public Consultations consultations = new Consultations();
    public Schedule schedule = new Schedule();
    public Settings settings = new Settings();

    // таблица пользователи
    public static class Users extends AbstractDbTable<String, Users.User> {
        public static class User {
            public final String login;
            public final String password;
            public final String name;
            public final boolean is_mentor;
            public final String image;

            public User(String login, String password, String name, boolean is_mentor, String image) {
                this.login = login;
                this.password = password;
                this.name = name;
                this.is_mentor = is_mentor;
                this.image = image;
            }

            public String getLogin() {
                return login;
            }

            public String getPassword() {
                return password;
            }

            public String getName() {
                return name;
            }

            public boolean isIs_mentor() {
                return is_mentor;
            }

            public String getImage() {
                return image;
            }

            @Override
            public String toString() { return login; }
        }

        private Users() {
            super(new TypeToken<ArrayList<User>>() {}.getType());
        }

        @Override
        public String getTableName() { return "users.json"; }

        @Override
        public String getKey(User elem) { return elem.login; }
    }

    // таблица консультации
    public static class Consultations extends AbstractDbTable<Consultations.Key, Consultations.Consultation> {
        public static class Consultation {
            public final String mentor;
            public final long start;
            public final long duration;
            public final String student;
            public final String comment;

            public Consultation(String mentor, long start, long duration, String student, String comment) {
                this.mentor = mentor;
                this.start = start;
                this.duration = duration;
                this.student = student;
                this.comment = comment;
            }

            public String getMentor() {
                return mentor;
            }

            public long getStart() {
                return start;
            }

            public long getDuration() {
                return duration;
            }

            public String getStudent() {
                return student;
            }

            public String getComment() {
                return comment;
            }
        }

        public static class Key {
            public final String mentor;
            public final long start;

            public Key(String mentor, long start) {
                this.mentor = mentor;
                this.start = start;
            }
        }

        private Consultations() {
            super(new TypeToken<ArrayList<Consultation>>() {}.getType());
        }

        @Override
        public String getTableName() { return "consultations.json"; }

        @Override
        public Key getKey(Consultation elem) {
            return new Key(elem.mentor, elem.start);
        }
    }

    // таблица расписание
    public static class Schedule extends AbstractDbTable<Schedule.Key, Schedule.Value> {
        public static class Value {
            public final String mentor;
            public final int day_of_week;
            public final long start;
            public final long duration;

            public Value(String mentor, int day_of_week, long start, long duration) {
                this.mentor = mentor;
                this.day_of_week = day_of_week;
                this.start = start;
                this.duration = duration;
            }

            public String getMentor() {
                return mentor;
            }

            public int getDay_of_week() {
                return day_of_week;
            }

            public long getStart() {
                return start;
            }

            public long getDuration() {
                return duration;
            }
        }

        public static class Key {
            public final String mentor;
            public final int day_of_week;
            public final long start;

            public Key(String mentor, int day_of_week, long start) {
                this.mentor = mentor;
                this.day_of_week = day_of_week;
                this.start = start;
            }
        }

        private Schedule() {
            super(new TypeToken<ArrayList<Value>>() {}.getType());
        }

        @Override
        public String getTableName() { return "schedule.json"; }

        @Override
        public Key getKey(Value elem) {
            return new Key(elem.mentor, elem.day_of_week, elem.start);
        }
    }

    // таблица настройки
    public static class Settings extends AbstractDbTable<String, Settings.Record> {
        public static class Record {
            public final String name;
            public final String value;

            public Record(String name, String value) {
                this.name = name;
                this.value = value;
            }

            public String getName() {
                return name;
            }

            public String getValue() {
                return value;
            }
        }

        private Settings() {
            super(new TypeToken<ArrayList<Record>>() {}.getType());
        }

        @Override
        public String getTableName() { return "settings.json"; }

        @Override
        public String getKey(Record elem) { return elem.name; }
    }


    public static void main(String[] args) throws IOException {
        INSTANCE.users.readAll();
        System.out.println(INSTANCE.users.getAll());

        if (!DataBase.INSTANCE.users.put(new Users.User("login", "hash", "name", false, "c:/!/!.jpg")))
            System.out.println("Пользователь уже существует...");;
        DataBase.INSTANCE.users.put(new Users.User("login2", "hash2", "name2", false, "c:/!/2!.jpg"));
        DataBase.INSTANCE.users.put(new Users.User("mazneff", "hash3", "Мазнев Валерий", true, "c:/!/m!.png"));

        List<Users.User> list2 = INSTANCE.users.select(e -> e.is_mentor);
        System.out.println(list2);

        System.out.println(INSTANCE.users.findKey("login2"));

        System.out.println(INSTANCE.users.exists("123"));

        System.out.println(INSTANCE.users.exists("mazneff"));

        System.out.println(INSTANCE.users.getAll());
    }
}
