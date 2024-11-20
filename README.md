# Лабораторная работа №3: Реализация алгоритмов компьютерной графики

## Описание проекта

Цель данной лабораторной работы — изучение и реализация алгоритмов компьютерной графики для построения линий, окружностей, а также для сглаживания линий. Реализованы следующие алгоритмы:

- **Step-by-step (пошаговый алгоритм)**  
- **DDA (Digital Differential Analyzer)**  
- **Брезенхем (линия)**  
- **Брезенхем (окружность)**  
- **Wu (антиалиасинг линий)**  

Программа предоставляет пользовательский интерфейс с использованием Java Swing, что позволяет вводить параметры и наблюдать результат работы алгоритмов в графической форме.

---

## Описание алгоритмов

### 1. Step-by-step (Пошаговый алгоритм)
Простой алгоритм, который вычисляет промежуточные точки линии.  
- **Принцип работы**:
  - Разница координат по x и y используется для вычисления количества шагов.
  - На каждом шаге координаты увеличиваются пропорционально наклону линии.
- **Преимущества**: простота реализации.  
- **Недостатки**: низкая производительность из-за работы с дробными числами и ошибок округления.  

---

### 2. DDA (Digital Differential Analyzer)
Усовершенствованный пошаговый алгоритм, который автоматически вычисляет промежуточные точки.  
- **Принцип работы**:
  - Рассчитывается наклон линии (dx/dy).
  - Координаты обновляются через инкремент.  
- **Время выполнения**: `O(n)`, где `n` — длина линии в пикселях.  
- **Преимущества**: равномерное распределение пикселей.  
- **Недостатки**: использование дробных чисел.  

---

### 3. Алгоритм Брезенхема для линии
Целочисленный алгоритм для рисования линий, который учитывает ошибку смещения.  
- **Принцип работы**:
  - Вычисляется ошибка смещения от идеальной линии.
  - Корректируется путём добавления или вычитания `dx` и `dy`.  
- **Время выполнения**: `O(n)`.  
- **Преимущества**: высокая скорость, целочисленные вычисления.  
- **Недостатки**: подходит только для прямых линий.  

---

### 4. Алгоритм Брезенхема для окружности
Целочисленный алгоритм для рисования окружностей с использованием симметрии.  
- **Принцип работы**:
  - Рисуются пиксели для одной восьмой окружности, затем отображаются зеркально.
  - Вычисляется ошибка по аналогии с алгоритмом для линий.  
- **Время выполнения**: `O(radius)`.  
- **Преимущества**: высокая скорость, благодаря учёту симметрии.  
- **Недостатки**: применим только для окружностей.  

---

### 5. Алгоритм Wu (антиалиасинг линии)
Предназначен для сглаживания линий путём изменения интенсивности пикселей.  
- **Принцип работы**:
  - Рассчитывается наклон линии.
  - Для пикселей вдоль линии назначаются частичные значения интенсивности.  
- **Время выполнения**: `O(n)`.  
- **Преимущества**: высокая визуальная точность, линии выглядят сглаженными.  
- **Недостатки**: сложная реализация и необходимость работы с полупрозрачными пикселями.  

---

## Сравнение времени выполнения алгоритмов

| Алгоритм               | Сложность       | Особенности                              |
|------------------------|----------------|------------------------------------------|
| Step-by-step           | `O(n)`         | Низкая производительность, дробные числа.|
| DDA                   | `O(n)`         | Улучшение Step-by-step.                  |
| Bresenham (линия)      | `O(n)`         | Высокая скорость, целочисленные вычисления.|
| Bresenham (окружность) | `O(radius)`    | Эффективен для окружностей.              |
| Wu                     | `O(n)`         | Эффект сглаживания линий.                |

---

## Выводы

1. **Алгоритм Брезенхема для линии** — наиболее эффективный для рисования прямых линий благодаря целочисленной арифметике.  
2. **Алгоритм Брезенхема для окружности** — оптимальный выбор для рисования окружностей из-за использования симметрии.  
3. **Алгоритм Wu** — идеален для случаев, где требуется антиалиасинг, обеспечивая сглаживание линий.  
4. **Step-by-step** и **DDA** менее эффективны, но полезны для изучения базовых принципов построения графики.