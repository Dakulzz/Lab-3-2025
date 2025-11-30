import functions.*;

public class Main {
    
    public static void main(String[] args) {
        System.out.println("=== Тестирование ArrayTabulatedFunction ===\n");
        testTabulatedFunction(true);
        
        System.out.println("\n=== Тестирование LinkedListTabulatedFunction ===\n");
        testTabulatedFunction(false);
    }
    
    private static void testTabulatedFunction(boolean useArray) {
        TabulatedFunction func;
        
        // Тест 1: Некорректные параметры конструктора
        System.out.println("--- Тест 1: Некорректные параметры конструктора");
        
        try {
            if (useArray) {
                func = new ArrayTabulatedFunction(5.0, 1.0, 5);
            } else {
                func = new LinkedListTabulatedFunction(5.0, 1.0, 5);
            }
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: " + e.getMessage());
        }
        
        try {
            if (useArray) {
                func = new ArrayTabulatedFunction(0.0, 5.0, 1);
            } else {
                func = new LinkedListTabulatedFunction(0.0, 5.0, 1);
            }
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("OK: " + e.getMessage());
        }
        
        // Создаем корректную функцию
        double[] values = {0, 1, 4, 9, 16};
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        System.out.println("\nФункция создана: x=[0,4], y=" + arrayToString(values));
        
        // Тест 2: Выход за границы индекса
        System.out.println("\n--- Тест 2: Выход за границы индекса");
        
        try {
            func.getPoint(-1);
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("OK getPoint(-1): " + e.getMessage());
        }
        
        try {
            func.getPoint(10);
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("OK getPoint(10): " + e.getMessage());
        }
        
        // Тест 3: Некорректное изменение X
        System.out.println("\n--- Тест 3: Некорректное изменение X");
        
        try {
            func.setPointX(2, 0.5); // Пытаемся установить X меньше предыдущего
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("ОШИБКА: Неверный тип исключения");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("OK setPointX(2, 0.5): " + e.getMessage());
        }
        
        try {
            func.setPointX(2, 3.5); // Пытаемся установить X больше следующего
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("ОШИБКА: Неверный тип исключения");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("OK setPointX(2, 3.5): " + e.getMessage());
        }
        
        // Тест 4: Добавление точки с существующим X
        System.out.println("\n--- Тест 4: Добавление точки с существующим X");
        
        try {
            func.addPoint(new FunctionPoint(2.0, 100)); // X=2 уже есть
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (InappropriateFunctionPointException e) {
            System.out.println("OK addPoint(2.0, 100): " + e.getMessage());
        }
        
        // Тест 5: Удаление при минимальном количестве точек
        System.out.println("\n--- Тест 5: Удаление при минимальном количестве точек");
        
        // Удаляем точки до минимума
        try {
            while (func.getPointsCount() > 2) {
                func.deletePoint(0);
            }
            System.out.println("Осталось точек: " + func.getPointsCount());
            
            func.deletePoint(0); // Пытаемся удалить при 2 точках
            System.out.println("ОШИБКА: Исключение не выброшено");
        } catch (FunctionPointIndexOutOfBoundsException e) {
            System.out.println("ОШИБКА: Неверный тип исключения");
        } catch (IllegalStateException e) {
            System.out.println("OK deletePoint(0): " + e.getMessage());
        }
        
        // Тест 6: Нормальная работа
        System.out.println("\n--- Тест 6: Нормальная работа");
        
        if (useArray) {
            func = new ArrayTabulatedFunction(0.0, 4.0, values);
        } else {
            func = new LinkedListTabulatedFunction(0.0, 4.0, values);
        }
        
        System.out.println("Границы: [" + func.getLeftDomainBorder() + ", " + func.getRightDomainBorder() + "]");
        System.out.println("f(1.5) = " + func.getFunctionValue(1.5));
        System.out.println("f(2.0) = " + func.getFunctionValue(2.0));
        
        try {
            func.addPoint(new FunctionPoint(1.5, 2.25));
            System.out.println("Добавлена точка (1.5, 2.25)");
            System.out.println("Количество точек: " + func.getPointsCount());
            System.out.println("f(1.5) = " + func.getFunctionValue(1.5));
        } catch (InappropriateFunctionPointException e) {
            System.out.println("ОШИБКА: " + e.getMessage());
        }
    }
    
    private static String arrayToString(double[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}