package com.company;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        // Ввод данных
        System.out.print("Enter expression: ");
        Scanner inputValue = new Scanner(System.in);
        String inputString = inputValue.nextLine();
        // Проверка количества операторов в выражении
        int quantityOfOperators = 0;
        for(int i = 0; i < inputString.length(); i++)
        {
            if(inputString.charAt(i) == '/' || inputString.charAt(i) == '*' || inputString.charAt(i) == '+' || inputString.charAt(i) == '-')
            {
                quantityOfOperators++;
            }
            if(quantityOfOperators >= 2)
            {
                try
                {
                    throw new IOException();
                }
                catch (IOException e)
                {
                    System.out.println("In this case more than one operator");
                    System.exit(1);
                }
            }
        }

        Operand firstOperand = new Operand(inputString);                        // создаем первый операнд
        char ExpressionOperator = getOperator(inputString);                     // вычисляем тип оператора
        // выделение из строки второго оператора
        int indexOfOperator = inputString.indexOf(ExpressionOperator) + 1;
        String modifiedString = inputString.substring(indexOfOperator);

        Operand secondOperand = new Operand(modifiedString);                    // создаем второй оператор
        // проверка чтобы операнды были одного типа
        if(!firstOperand.getType().equals(secondOperand.getType()))
        {
            try
            {
                throw new IOException();
            }
            catch (IOException e)
            {
                System.out.println("Operands have different format");
                System.exit(1);
            }
        }
        // вычисления результата в арабских цифрах
        int result = 0;
        switch (ExpressionOperator)
        {
            case '+':
            {
                result = firstOperand.getDigitValue() + secondOperand.getDigitValue();
                break;
            }
            case '*':
            {
                result = firstOperand.getDigitValue() * secondOperand.getDigitValue();
                break;
            }
            case '/':
            {
                // проверка делимости на 0, хотя она тут необязательна, по условию цифры должны быть от 1 до 10
                if(secondOperand.getDigitValue() == 0)
                {
                    try
                    {
                        throw new IOException();
                    }
                    catch (IOException e)
                    {
                        System.out.println("Divide by zero");
                        System.exit(1);
                    }
                }
                result = firstOperand.getDigitValue() / secondOperand.getDigitValue();
                break;
            }
            case '-':
            {
                result = firstOperand.getDigitValue() - secondOperand.getDigitValue();
                break;
            }
            default: {
                try
                {
                    throw new IOException();
                }
                catch (IOException e)
                {
                    System.out.println("This string is not math operation");        // если в выражении не было операторов, завершаем работу.
                    System.exit(1);
                }
            }
        }

        // выбор формата отображения резульаты вычисления
        if(firstOperand.getType().equals("arabic"))
        {
            System.out.println(result);
        }
        else
        {
            System.out.println(Converter.convertAtoR(result));
        }
    }

    public static char getOperator(String str)  // метод для получения оператора из введенной строки
    {
        for(int i = 0; i < str.length(); i++)
        {
            if(str.charAt(i) == '/' || str.charAt(i) == '*' || str.charAt(i) == '+' || str.charAt(i) == '-')
            {
                return str.charAt(i);
            }
        }
        return 0;
    }
}

class Operand           // класс для работы с операндами
{

    Operand(String str) // конструктор
    {
        data = "";
        for(int i = 0; i < str.length(); i++)   // считываем информацию для получения данных об операдне
        {
            if(str.charAt(i) != '/' && str.charAt(i) != '*' && str.charAt(i) != '+' && str.charAt(i) != '-')
            {
                if(!Character.isSpaceChar(str.charAt(i)))
                    data += str.charAt(i);
            }
            else
            {
                break;
            }
        }
        try     // попытка преобразовать данные в число
        {
            digitValue = Integer.parseInt(data);
            type = "arabic";
        }
        catch (NumberFormatException e)
        {
            digitValue = Converter.convertRtoA(data);   // попытка преобразовать данные в арабское число
            type = "roman";
        }
        if(digitValue < 1 || digitValue > 10)           // проверка на допустимые значения
        {
            try
            {
                throw new IOException();
            }
            catch (IOException e)
            {
                System.out.println("One of entered values less than 1 or greater than 10");
                System.exit(1);
            }
        }
    }
    // геттеры для получения данных
    public int getDigitValue()
    {
        return digitValue;
    }

    public String getType()
    {
        return type;
    }
    // данные класса
    private String data;
    private int digitValue;
    private String type;
}

class Converter
{
    public static int convertRtoA(String str)       // статический метод для конвертации римских цифр в арабские
    {
        if(str.length() == 0)
        {
            try
            {
                throw new IOException();
            }
            catch (IOException e)
            {
                System.out.println("Empty data");
                System.exit(1);
            }
        }
        int result = 0;
        int index = 0;
        int tempValidator = 0;
        while(str.charAt(index) == 'X')
        {
            result += 10;
            index++;
            tempValidator++;
            if(index == str.length())
            {
                return result;
            }
            if(tempValidator > 3)
            {
                try
                {
                    throw new IOException();
                }
                catch (IOException e)
                {
                    System.out.println("Wrong format of data");
                    System.exit(1);
                }
            }
        }
        if(str.length() - index >= 2 && str.charAt(index) == 'I' && str.charAt(index+1) == 'X')
        {
            result += 9;
            index += 2;
            if(index == str.length())
            {
                return result;
            }
        }
        if(str.charAt(index) == 'V')
        {
            result += 5;
            index++;
            if(index == str.length())
            {
                return result;
            }
        }
        tempValidator = 0;
        while(str.charAt(index) == 'I')
        {
            result += 1;
            index++;
            tempValidator++;
            if(index == str.length())
            {
                return result;
            }
            if(tempValidator > 3)
            {
                try
                {
                    throw new IOException();
                }
                catch (IOException e)
                {
                    System.out.println("Wrong format of data");
                    System.exit(1);
                }
            }
        }
        if(index != str.length())
        {
            try
            {
                throw new IOException();
            }
            catch (IOException e)
            {
                System.out.println("Wrong format of data");
                System.exit(1);
            }
        }
        return result;
    }

    public static String convertAtoR(int data)      // статический метод для конвертации арабских цифр в римские
    {
        if (data < 1)
        {
            try
            {
                throw new IOException();
            }
            catch (IOException e)
            {
                System.out.println("Roman values can't be negative or zero");
                System.exit(1);
            }
        }
        String str = "";
        if (data >= 100)
        {
            str += "C";
            data -= 100;
        }
        if (data >= 90)
        {
            str += "XC";
            data -= 90;
        }
        if (data >= 50)
        {
            str += "L";
            data -= 50;
        }
        if (data >= 40)
        {
            str += "XL";
            data -= 40;
        }
        while (data >= 10)
        {
            str += "X";
            data -= 10;
        }
        if (data >= 9)
        {
            str += "IX";
            data -= 9;
        }
        if (data >= 5)
        {
            str += "V";
            data -= 5;
        }
        if (data >= 4)
        {
            str += "IV";
            data -= 4;
        }
        while (data >= 1)
        {
            str += "I";
            data -= 1;
        }
        return str;
    }
}
