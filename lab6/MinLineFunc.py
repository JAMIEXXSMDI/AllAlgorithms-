import numpy as np
import matplotlib.pyplot as plt

def himmelblau(x, y):
    """Функция Химмельблау (тестовая функция для оптимизации)."""
    return (x**2 + y - 11)**2 + (x + y**2 - 7)**2

def grad_himmelblau(x, y):
    """Градиент функции Химмельблау."""
    df_dx = 4 * x * (x**2 + y - 11) + 2 * (x + y**2 - 7)
    df_dy = 2 * (x**2 + y - 11) + 4 * y * (x + y**2 - 7)
    return np.array([df_dx, df_dy])

def gradient_descent(x0, y0, alpha=0.001, epsilon=1e-6, max_iter=10000):
    """Градиентный спуск с фиксированным шагом."""
    history = []
    x, y = x0, y0

    for i in range(max_iter):
        grad = grad_himmelblau(x, y)
        grad_norm = np.linalg.norm(grad)

        if grad_norm < epsilon or himmelblau(x, y) < 1e-6:
            break

        x -= alpha * grad[0]
        y -= alpha * grad[1]

        history.append([x, y, himmelblau(x, y)])

        if i % 100 == 0:
            print(f"Iteration {i}: x={x:.6f}, y={y:.6f}, f(x,y)={himmelblau(x, y):.6f}")

    return x, y, np.array(history)

def plot_optimization(history):
    """Визуализация процесса оптимизации."""
    plt.figure(figsize=(12, 5))

    # Контурный график
    plt.subplot(121)
    x = np.linspace(-5, 5, 100)
    y = np.linspace(-5, 5, 100)
    X, Y = np.meshgrid(x, y)
    Z = himmelblau(X, Y)
    plt.contour(X, Y, Z, levels=50, cmap='viridis')
    plt.plot(history[:, 0], history[:, 1], 'r.-')
    # Глобальные минимумы функции Химмельблау
    minima = np.array([
        [3.0, 2.0],
        [-2.805118, 3.131312],
        [-3.779310, -3.283186],
        [3.584428, -1.848126]
    ])
    plt.plot(minima[:, 0], minima[:, 1], 'g*', markersize=10)
    plt.title('Trajectory of Gradient Descent')
    plt.xlabel('x')
    plt.ylabel('y')

    # График сходимости
    plt.subplot(122)
    plt.plot(np.log10(history[:, 2]))
    plt.title('Function Value Convergence (log scale)')
    plt.xlabel('Iteration')
    plt.ylabel('log10(f(x,y))')

    plt.tight_layout()
    plt.show()

# Параметры оптимизации
x0, y0 = 0.0, 0.0  # Начальная точка
alpha = 0.001      # Шаг обучения
epsilon = 1e-6      # Критерий остановки

# Запуск оптимизации
x_opt, y_opt, history = gradient_descent(x0, y0, alpha, epsilon)

# Вывод результатов
print("\n=== Optimization Results ===")
print(f"Found minimum: ({x_opt:.8f}, {y_opt:.8f})")
print(f"Function value: {himmelblau(x_opt, y_opt):.12f}")
print(f"Global minima (reference):")
print("(3.0, 2.0), (-2.805118, 3.131312), (-3.779310, -3.283186), (3.584428, -1.848126)")
print(f"Iterations needed: {len(history)}")

# Визуализация
plot_optimization(history)
