package functions;

public class LinkedListTabulatedFunction implements TabulatedFunction {
    
    private static final double EPSILON = 1e-9;
    
    // Внутренний класс элемента списка
    private static class FunctionNode {
        FunctionPoint data;
        FunctionNode prev;
        FunctionNode next;
        
        FunctionNode() {
            this.data = null;
            this.prev = this;
            this.next = this;
        }
        
        FunctionNode(FunctionPoint data) {
            this.data = data;
        }
    }
    
    private FunctionNode head;      // Голова списка (не хранит данные)
    private int pointsCount;        // Количество значащих элементов
    
    // Инициализация пустого списка
    private void initList() {
        head = new FunctionNode();
        head.prev = head;
        head.next = head;
        pointsCount = 0;
    }
    
    // Получение элемента по индексу с оптимизацией
 private FunctionNode getNodeByIndex(int index) {
        FunctionNode current;
        int currentIndex;
        
        // Выбираем направление обхода
        if (index <= pointsCount / 2) {
            current = head.next;
            currentIndex = 0;
            while (currentIndex != index) {
                current = current.next;
                currentIndex++;
            }
        } else {
            current = head.prev;
            currentIndex = pointsCount - 1;
            while (currentIndex != index) {
                current = current.prev;
                currentIndex--;
            }
        }
        
        return current;
    }
    
    // Добавление элемента в конец списка
    private FunctionNode addNodeToTail() {
        FunctionNode newNode = new FunctionNode();
        newNode.prev = head.prev;
        newNode.next = head;
        head.prev.next = newNode;
        head.prev = newNode;
        pointsCount++;
        return newNode;
    }
    
    // Добавление элемента в указанную позицию
    private FunctionNode addNodeByIndex(int index) {
        if (index == pointsCount) {
            return addNodeToTail();
        }
        
        FunctionNode nextNode = getNodeByIndex(index);
        FunctionNode newNode = new FunctionNode();
        
        newNode.prev = nextNode.prev;
        newNode.next = nextNode;
        nextNode.prev.next = newNode;
        nextNode.prev = newNode;
        
        pointsCount++;
        return newNode;
    }
    
    // Удаление элемента по индексу
    private FunctionNode deleteNodeByIndex(int index) {
        FunctionNode nodeToDelete = getNodeByIndex(index);
        
        nodeToDelete.prev.next = nodeToDelete.next;
        nodeToDelete.next.prev = nodeToDelete.prev;
        
        pointsCount--;
        
        return nodeToDelete;
    }
    
    // Конструктор с количеством точек
    public LinkedListTabulatedFunction(double leftX, double rightX, int count) {
        if (count < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        initList();
        
        double step = (rightX - leftX) / (count - 1);
        for (int i = 0; i < count; i++) {
            FunctionNode node = addNodeToTail();
            node.data = new FunctionPoint(leftX + i * step, 0);
        }
    }
    
    // Конструктор с массивом значений
    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) {
        if (values == null || values.length < 2) {
            throw new IllegalArgumentException("Массив значений должен содержать не менее 2 элементов");
        }
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }
        
        initList();
        
        double step = (rightX - leftX) / (values.length - 1);
        for (int i = 0; i < values.length; i++) {
            FunctionNode node = addNodeToTail();
            node.data = new FunctionPoint(leftX + i * step, values[i]);
        }
    }
    
    @Override public double getLeftDomainBorder() {return head.next.data.getX();}
    
    @Override public double getRightDomainBorder() {return head.prev.data.getX();}
    
    @Override public double getFunctionValue(double x) {
        if (x < getLeftDomainBorder() || x > getRightDomainBorder()) {
            return Double.NaN;
        }
        
        // Оптимизированный поиск - проходим по списку напрямую
        FunctionNode current = head.next;
        
        // Ищем точку или интервал
        while (current.next != head) {
            if (Math.abs(x - current.data.getX()) < EPSILON) {
                return current.data.getY();
            }
            if (x < current.next.data.getX()) {
                break;
            }
            current = current.next;
        }
        
        // Проверка последней точки
        if (Math.abs(x - current.data.getX()) < EPSILON) {
            return current.data.getY();
        }
        
        // Линейная интерполяция
        double x1 = current.data.getX(), y1 = current.data.getY();
        double x2 = current.next.data.getX(), y2 = current.next.data.getY();
        
        return y1 + (y2 - y1) * (x - x1) / (x2 - x1);
    }
    
    @Override
    public int getPointsCount() {
        return pointsCount;
    }
    
    @Override
    public FunctionPoint getPoint(int index) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(index);
        return new FunctionPoint(getNodeByIndex(index).data);
    }
    
    @Override
    public void setPoint(int index, FunctionPoint point) 
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndex(index);
        
        FunctionNode node = getNodeByIndex(index);
        double newX = point.getX();
        
        // Проверка границ
        if (node.prev != head && newX <= node.prev.data.getX()) {
            throw new InappropriateFunctionPointException("x должен быть больше x предыдущей точки");
        }
        if (node.next != head && newX >= node.next.data.getX()) {
            throw new InappropriateFunctionPointException("x должен быть меньше x следующей точки");
        }
        
        node.data = new FunctionPoint(point);
    }
    
    @Override
    public double getPointX(int index) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(index);
        return getNodeByIndex(index).data.getX();
    }
    
    @Override
    public double getPointY(int index) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(index);
        return getNodeByIndex(index).data.getY();
    }
    
    @Override
    public void setPointX(int index, double x) 
            throws FunctionPointIndexOutOfBoundsException, InappropriateFunctionPointException {
        checkIndex(index);
        
        FunctionNode node = getNodeByIndex(index);
        
        if (node.prev != head && x <= node.prev.data.getX()) {
            throw new InappropriateFunctionPointException("x должен быть больше x предыдущей точки");
        }
        if (node.next != head && x >= node.next.data.getX()) {
            throw new InappropriateFunctionPointException("x должен быть меньше x следующей точки");
        }
        
        node.data.setX(x);
    }
    
    @Override
    public void setPointY(int index, double y) throws FunctionPointIndexOutOfBoundsException {
        checkIndex(index);
        getNodeByIndex(index).data.setY(y);
    }
    
    @Override
    public void deletePoint(int index) throws FunctionPointIndexOutOfBoundsException, IllegalStateException {
        if (pointsCount <= 2) {
            throw new IllegalStateException("Нельзя удалить точку: минимальное количество точек - 2");
        }
        checkIndex(index);
        deleteNodeByIndex(index);
    }
    
    @Override public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException {
        double newX = point.getX();
        
        // Оптимизированный поиск позиции - проходим по списку напрямую
        FunctionNode current = head.next;
        int pos = 0;
        
        while (current != head && current.data.getX() < newX) {
            if (Math.abs(current.data.getX() - newX) < EPSILON) {
                throw new InappropriateFunctionPointException("Точка с таким x уже существует");
            }
            current = current.next;
            pos++;
        }
        
        // Проверка текущего элемента
        if (current != head && Math.abs(current.data.getX() - newX) < EPSILON) {
            throw new InappropriateFunctionPointException("Точка с таким x уже существует");
        }
        
        // Вставка нового узла
        FunctionNode newNode = new FunctionNode(new FunctionPoint(point));
        newNode.prev = current.prev;
        newNode.next = current;
        current.prev.next = newNode;
        current.prev = newNode;
        
        pointsCount++;
    }
    
    private void checkIndex(int index) throws FunctionPointIndexOutOfBoundsException {
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException(
                "Индекс " + index + " вне диапазона [0, " + (pointsCount - 1) + "]");
        }
    }
}