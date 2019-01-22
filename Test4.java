//В течение дня в банк заходят люди, у каждого человека есть время захода в банк и
//время выхода. Всего за день у банка было N посетителей. Банк работает с 8:00 до 20:00. 
//Человек посещает банк только один раз за день. Написать программу, которая определяет 
//периоды времени, когда в банке было максимальное количество посетителей. 
//Входные данные о посетителях программа должна получать из файла, формат файла - произвольный, 
//файл высылается вместе с решением.

//От автора: я имел смелость предположить, что тут имеются ввиду те же периоды 
//времени, что и в предыдущем задании. Надеюсь, не ошибся. :) 
package test4;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Александр Машьянов, mashyanov1987@gmail.com, +7(981)784-79-67
 */
public class Test4 {
   
    public static void main(String[] args) {
        /*Коллекция, которая будет содержать всех клиентов банка, зашедших сегодня*/
        ArrayList<BankClient> clients= new ArrayList<>();
        
        // От автора: В задании не предусмотрен вывод всех клиентов банка в 
        // консоль, поэтому я просто оставлю это здесь:
        //
//        clients.add(new BankClient(8, 0, 8, 31));
//        clients.add(new BankClient(8, 10, 9, 1));
//        clients.add(new BankClient(9, 11, 9, 43));
//        clients.add(new BankClient(9, 18, 9, 59));
//        clients.add(new BankClient(9, 29, 10, 35));
//        clients.add(new BankClient(10, 2, 10, 21));
//        clients.add(new BankClient(11, 10, 11, 45));
//        clients.add(new BankClient(11, 27, 12, 0));
//        clients.add(new BankClient(12, 13, 13, 31));
//        clients.add(new BankClient(12, 46, 13, 15));
//        clients.add(new BankClient(13, 12, 13, 17));
//        clients.add(new BankClient(13, 30, 13, 56));
//        clients.add(new BankClient(13, 35, 14, 31));
//        clients.add(new BankClient(14, 0, 14, 31));
//        clients.add(new BankClient(15, 0, 15, 42));
//        clients.add(new BankClient(15, 10, 16, 1));
//        clients.add(new BankClient(16, 32, 16, 41));
//        clients.add(new BankClient(17, 47, 18, 31));
//        clients.add(new BankClient(19, 10, 19, 59));
//        try {
//            File f =new File("test.dat");
//            if(!f.exists()) f.createNewFile();
//            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
//                oos.writeObject(clients);
//            }
//        } catch (FileNotFoundException ex) {
//            Logger.getLogger(Test4.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex) {
//            Logger.getLogger(Test4.class.getName()).log(Level.SEVERE, null, ex);
//        }


        //СЧИТЫВАЕМ ДАННЫЕ О КЛИЕНТАХ ИЗ ФАЙЛА ПРОИЗВОЛЬНОГО ФОРМАТА
        try {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("test.dat")))) {
                clients = (ArrayList<BankClient>) ois.readObject();
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(new JPanel(), "FileNotFoundException "+ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println(ex.getMessage());
            return;
        } catch (IOException | ClassNotFoundException ex) {
            JOptionPane.showMessageDialog(new JPanel(), "IOException or ClassNotFoundException" + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
            System.err.println(ex.getMessage());
            return;
        }
        
        System.out.println(getHardestTime(clients));
    }
    /**Метод находи самый(-ые) загруженный(-ые) периоды за день
     * @param clients коллекция, содержащая всех клиентов, зашедших в банк
     * @return String, содержащую отчет о загруженности за день */
    static String getHardestTime(ArrayList<BankClient> clients){
        /* Счетчик клиентов в период времени. Каждый элемент массива - период,
         * а значение элемента - количество клиентов в этот временной период*/
        int [] intervalCounter = new int[24];
        /*Эту переменную можно было не объявлять, но мне так удобнее и нагляднее
        * писать формулу, да и вам виднее, что и где используется.
        * Содержит номер периода, когда клиент вошел в банк**/
        int beginTimeIntervalNumber;
        /*Эту переменную так же можно было не объявлять, но мне по прежнему так 
        * удобнее и нагляднее писать формулу, а вам все так же проще разбирать 
        * мою писанину.
        * Содержит количество временных периодов, проведенных клиентом в банке**/
        int spentTimeIntervals;
        /*Счетчик максимального количества клиентов в период времени*/
        int max = 0;
        /*Счетчик временных периодов с максимальным количество клиентов*/
        int repeatCounter = 0;
        //СЧИТАЕМ КОЛИЧЕСТВО КЛИЕНТОВ В КАЖДЫЙ ПОЛУЧАСОВОЙ ИНТЕРВАЛ
        for (BankClient c : clients) {
            beginTimeIntervalNumber = (c.getBeginTime() - 480) / 30; //Вычитая 480, превращаем 8 часов(начало дня) в ноль, т.е в номер первого временного интервала.
            spentTimeIntervals = c.getSpentTime() / 30 + 1; //Рассчитвыаем, сколько периодов времени клиент находился в банке
            for (int i = beginTimeIntervalNumber; i < beginTimeIntervalNumber+spentTimeIntervals; i++) 
                intervalCounter[i]++;
        }
        
        //ИЩЕМ САМЫЙ НАГРУЖЕННЫЙ ПЕРИОД
        for (int i = 0; i < 24; i++)             
            if(intervalCounter[i]>max) 
                max=intervalCounter[i];
                    
        //ПРОВЕРИМ, ОН ОДИН ТАКОЙ НАГРУЖЕННЫЙ, ИЛИ ЕСТЬ ЕЩЕ. ИСХОДЯ ИЗ ЭТОГО ВЫДАЕМ РЕЗУЛЬТАТ
        StringBuilder stb = new StringBuilder("Cамый загруженный интервал выдался");
        /* сравниваем счетчик клиентов каждого периода с максимальным значением. 
         * Если совпадает, то добавляем соответсвующий кусочек строки в ответ.
         * Заодно проверяем, один у нас самый загруженый период или несколько*/
        for (int i =0; i < 24; i++) 
            if(intervalCounter[i]==max) {
                repeatCounter++;
                stb.append(" c ");
                stb.append(i+7 - 1*i%2); //по номеру интервала вычисляем время в часах
                stb.append(':');
                stb.append(i%2==0 ? "00 до " + String.valueOf(i+7 - 1*i%2) +":30," //а теперь опять же исходя из номера интервала, добавляем минуты
                        : "30 до "+ String.valueOf(i+8 - 1*i%2)+":00," );          //и время окончания интервала. 
            } 
        /**Вот эта строчка и будет содержать наш результат*/
        String result = stb.toString();
        //Заменяем последнюю автосгенерированную запятую точкой.
        result = result.substring(0, result.length()-1);
        result+=".";
        //Приводим к правилам русского языка, если интервалов не один, а несколько.
        if(repeatCounter>1)
            result = result.replaceFirst("Cамый загруженный интервал выдался", "Самые загруженные интервалы выдались");
       
    return result;    
    }}
    
    class BankClient implements Serializable{
        /*Время, когда клиент пришел в банк, начиная с 00:00,  в минутах*/
        private int beginTime;
        /*Время, которое клиент провел в банке, в минутах*/
        private int spentTime;
           
        /**Конструктор клиента. Вводи часы прибытия, минуты прибытия, часы убытия, минуты убытия*/
        public BankClient(int hourArrival,  int minuteArrival, int hourDepature,int minuteDepature) {
            try {
                /*Пересчитываем введенные даты в минуты*/
                this.beginTime = hourArrival*60 + minuteArrival;
                this.spentTime = ((hourDepature*60+minuteDepature)
                        - this.beginTime);
                /*Проверяем верность ввода данных на ошибки*/
                if(hourArrival < 8 || (hourArrival>=20 && minuteArrival > 0) ||
                        minuteArrival >59 || minuteArrival < 0 || hourDepature < 8 ||
                        (hourDepature>=20 && minuteDepature > 0) ||
                        minuteDepature >59 || minuteDepature < 0 ||this.spentTime < 0)
                    throw new IOException("Время заданно неверно");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(new JPanel(), "FileNotFoundException:\n" + ex.getMessage(), "Exception", JOptionPane.ERROR_MESSAGE);
                System.err.println("IOException:" + ex.getMessage());
                System.exit(1);
            }
        }
        /**Метод возвращает время, когда клиент пришел в банк, начиная с 00:00, 
         * в минутах*/
        public final int getSpentTime() {    return spentTime;        }
        /**Метод возвращает время, проведённое клиентом в банке, в минутах*/
        public final int getBeginTime() {    return beginTime;        }
    }
