import functions.*;

public class Main {

    public static void main(String[] args) {
        print("=== Тестирование ArrayTabulatedFunction ===\n");
        testTabulatedFunction(true);
        
        print("\n=== Тестирование LinkedListTabulatedFunction ===\n");
        testTabulatedFunction(false);
    }
    
    private static void testTabulatedFunction(boolean useArray) {
        TabulatedFunction func;
        
        // Тест 1: Некорректные параметры конструктора
        print("--- Тест 1: Некорректные параметры конструктора");
        
        try {
            if (useArray) {
                func = new ArrayTabulatedFunction(5.0, 1.0, 5);
            } else {
                func = new LinkedListTabulatedFunction(5.0, 1.0, 5);
            }
            print("ОШИБКА: Исключение не выброшено");
        } catch (IllegalArgumentException e) {
            print("OK: " + e.getMessage());
        }
        
        try {
            if (useArray) {
                func = new ArrayTabulatedFunction(0.0, 5.0, 1);
            } else {
                func = new LinkedListTabulatedFunction(0.0, 5.0, 1);
            }
            print("ОШИБКА: Исключение не выброшено");
        } catch (IllegalArgumentException e) {
            print("OK: " + e.getMessage());
        }
        
        // cоздаем корректную функцию
        double[] values = {0, 1, 4, 9, 16};
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        print("\nФункция создана: x=[0,4], y=" + arrayToString(values));
        printAllPoints(func);
        
        // Тест 2: Выход за границы индекса
        print("\n--- Тест 2: Выход за границы индекса");
        
        try {
            func.getPoint(-1);
            print("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("OK getPoint(-1): " + e.getMessage());
        }
        
        try {
            func.getPoint(10);
            print("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("OK getPoint(10): " + e.getMessage());
        }
        
        // Тест 3: Некорректное изменение X
        print("\n--- Тест 3: Некорректное изменение X");
        
        try {
            func.setPointX(2, 0.5);
            print("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("ОШИБКА: Неверный тип исключения");
        } catch (InappropriateFunctionPointException e) {
            print("OK setPointX(2, 0.5): " + e.getMessage());
        }
        
        try {
            func.setPointX(2, 3.5);
            print("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("ОШИБКА: Неверный тип исключения");
        } catch (InappropriateFunctionPointException e) {
            print("OK setPointX(2, 3.5): " + e.getMessage());
        }
        
        // Тест 4: Добавление точки с существующим X
        print("\n--- Тест 4: Добавление точки с существующим X");
        
        try {
            func.addPoint(new FunctionPoint(2.0, 100));
            print("ОШИБКА: Исключение не выброшено");
        } catch (InappropriateFunctionPointException e) {
            print("OK addPoint(2.0, 100): " + e.getMessage());
        }
        
        // Тест 5: Удаление при минимальном количестве точек
        print("\n--- Тест 5: Удаление при минимальном количестве точек");
        
        try {
            while (func.getPointsCount() > 2) {
                func.deletePoint(0);
            }
            print("Осталось точек: " + func.getPointsCount());
            
            func.deletePoint(0);
            print("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("ОШИБКА: Неверный тип исключения");
        } catch (IllegalStateException e) {
            print("OK deletePoint(0): " + e.getMessage());
        }
        
        // Тест 6: Нормальная работа
        print("\n--- Тест 6: Нормальная работа");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        print("Границы: [" + func.getLeftDomainBorder() + ", " + func.getRightDomainBorder() + "]");
        print("f(1.5) = " + func.getFunctionValue(1.5));
        print("f(2.0) = " + func.getFunctionValue(2.0));
        
        try {
            func.addPoint(new FunctionPoint(1.5, 2.25));
            print("Добавлена точка (1.5, 2.25)");
            print("Количество точек: " + func.getPointsCount());
            print("f(1.5) = " + func.getFunctionValue(1.5));
        } catch (InappropriateFunctionPointException e) {
            print("ОШИБКА Т6: " + e.getMessage());
        }
        
        // Тест 7: Демонстрация изменений точек
        print("\n--- Тест 7: Демонстрация изменений точек");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        print("Исходная функция:");
        printAllPoints(func);
        
        // setPointY
        try {
            print("\nsetPointY(2, 100):");
            print("ДО: точка[2] = " + pointToString(func.getPoint(2)));
            func.setPointY(2, 100);
            print("ПОСЛЕ: точка[2] = " + pointToString(func.getPoint(2)));
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("ОШИБКА: " + e.getMessage());
        }
        
        // setPointX
        try {
            print("\nsetPointX(2, 2.5):");
            print("ДО: точка[2] = " + pointToString(func.getPoint(2)));
            func.setPointX(2, 2.5);
            print("ПОСЛЕ: точка[2] = " + pointToString(func.getPoint(2)));
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            print("ОШИБКА: " + e.getMessage());
        }
        
        // setPoint
        try {
            print("\nsetPoint(1, (1.5, 50)):");
            print("ДО: точка[1] = " + pointToString(func.getPoint(1)));
            func.setPoint(1, new FunctionPoint(1.5, 50));
            print("ПОСЛЕ: точка[1] = " + pointToString(func.getPoint(1)));
        } catch (FunctionPointIndexOutOfBoundsException | InappropriateFunctionPointException e) {
            print("ОШИБКА: " + e.getMessage());
        }
        
        print("\nФункция после изменений:");
        printAllPoints(func);
        
        // Тест 8: Демонстрация добавления точек
        print("\n--- Тест 8: Демонстрация добавления точек");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, new double[]{0, 4, 16});
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, new double[]{0, 4, 16});
        }
        
        print("Исходная функция (3 точки):");
        printAllPoints(func);
        
        try {
            print("\naddPoint(1.0, 1.0) - в середину:");
            func.addPoint(new FunctionPoint(1.0, 1.0));
            printAllPoints(func);
            
            print("\naddPoint(-1.0, 1.0) - в начало:");
            func.addPoint(new FunctionPoint(-1.0, 1.0));
            printAllPoints(func);
            
            print("\naddPoint(5.0, 25.0) - в конец:");
            func.addPoint(new FunctionPoint(5.0, 25.0));
            printAllPoints(func);
        } catch (InappropriateFunctionPointException e) {
            print("ОШИБКА: " + e.getMessage());
        }
        
        // Тест 9: Демонстрация удаления точек
        print("\n--- Тест 9: Демонстрация удаления точек");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 5.0, new double[]{0, 1, 4, 9, 16, 25});
        } else {
            func = new LinkedListTabulatedFunction(0.0, 5.0, new double[]{0, 1, 4, 9, 16, 25});
        }
        
        print("Исходная функция (6 точек):");
        printAllPoints(func);
        
        try {
            print("\ndeletePoint(2) - удаление из середины:");
            print("Удаляемая точка: " + pointToString(func.getPoint(2)));
            func.deletePoint(2);
            printAllPoints(func);
            
            print("\ndeletePoint(0) - удаление первой:");
            print("Удаляемая точка: " + pointToString(func.getPoint(0)));
            func.deletePoint(0);
            printAllPoints(func);
            
            int lastIdx = func.getPointsCount() - 1;
            print("\ndeletePoint(" + lastIdx + ") - удаление последней:");
            print("Удаляемая точка: " + pointToString(func.getPoint(lastIdx)));
            func.deletePoint(lastIdx);
            printAllPoints(func);
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("ОШИБКА: " + e.getMessage());
        }
        
        // Тест 10: Вычисление значений функции
        print("\n--- Тест 10: Вычисление значений функции");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        print("Функция f(x) = x^2 на [0, 4]:");
        printAllPoints(func);
        
        print("\nВ узловых точках:");
        for (int i = 0; i <= 4; i++) {
            print("f(" + i + ") = " + func.getFunctionValue(i));
        }  
        
        print("\nМежду узлами (интерполяция):");
        for (int i = 1; i <= 3; i++) {
            print("f(" + (i-0.5) + ") = " + func.getFunctionValue(i-0.5));

        }
        
        print("\nВне области определения:");
        print("f(-1) = " + func.getFunctionValue(-1));
        print("f(5) = " + func.getFunctionValue(5));
    }
    
    // вывод всех точек TabulatedFunction (для до-после)
    private static void printAllPoints(TabulatedFunction func) {
        print("Точки (" + func.getPointsCount() + "):");
        try {
            for (int i = 0; i < func.getPointsCount(); i++) {
                FunctionPoint p = func.getPoint(i);
                print("  [" + i + "] x=" + p.getX() + ", y=" + p.getY());
            }
        } catch (FunctionPointIndexOutOfBoundsException e) {
            print("  Ошибка при получении точки");
        }
    }
    
    // вывод точки p
    private static String pointToString(FunctionPoint p) {
        return "(" + p.getX() + ", " + p.getY() + ")";
    }
    
    // вывод массива через запятую [0,1,...,n-1,n]
    private static String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // вывод поудобнее
    static void print(Object o) {    
        System.out.println(o);
    }

}
