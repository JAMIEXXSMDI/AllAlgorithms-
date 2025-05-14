import math
import numpy as np
import matplotlib.pyplot as plt
from tabulate import tabulate

def f(x):
    return x  # Пример линейной функции

def integrate_simpson(func, a, b, N):
    if N % 2 == 0:
        N += 1  # Метод Симпсона требует нечётное число точек

    x = np.linspace(a, b, N)
    h = (b - a) / (N - 1)

    integral = func(x[0]) + func(x[-1])

    for i in range(1, N - 1):
        if i % 2 == 1:
            integral += 4 * func(x[i])
        else:
            integral += 2 * func(x[i])

    integral *= h / 3
    return integral

def fourier_coefficients(func, L, N_terms, num_points=1000):
    a0 = (1 / (2 * L)) * integrate_simpson(func, -L, L, num_points)
    an = []
    bn = []
    for n in range(1, N_terms + 1):
        integrand_cos = lambda x: func(x) * np.cos(n * np.pi * x / L)
        an.append((1 / L) * integrate_simpson(integrand_cos, -L, L, num_points))

        integrand_sin = lambda x: func(x) * np.sin(n * np.pi * x / L)
        bn.append((1 / L) * integrate_simpson(integrand_sin, -L, L, num_points))

    return a0, an, bn

def fourier_series_approximation(a0, an, bn, L, N_terms, x):
    approximation = a0
    for n in range(1, N_terms + 1):
        approximation += an[n - 1] * np.cos(n * np.pi * x / L)
        approximation += bn[n - 1] * np.sin(n * np.pi * x / L)
    return approximation

if __name__ == "__main__":
    a, b = -np.pi, np.pi
    L = (b - a) / 2
    N_terms = 100 # Теперь можно ставить любое значение ≥1
    num_points = 10000

    a0, an, bn = fourier_coefficients(f, L, N_terms, num_points)

    # Исправлено: выводим только существующие коэффициенты
    print(f"Первые {N_terms} коэффициентов Фурье:")
    table_data = [["n", "a_n", "b_n"]]
    table_data.append([0, f"{a0:.6f}", "-"])
    for n in range(1, N_terms + 1):
        table_data.append([n, f"{an[n-1]:.6f}", f"{bn[n-1]:.6f}"])
    
    print(tabulate(table_data, headers="firstrow", tablefmt="grid"))

    # Графики
    x_vals = np.linspace(a, b, num_points)
    y_vals = [f(x) for x in x_vals]
    y_fourier = fourier_series_approximation(a0, an, bn, L, N_terms, x_vals)

    plt.figure(figsize=(10, 6))
    plt.plot(x_vals, y_vals, label="Исходная функция", color="blue")
    plt.plot(x_vals, y_fourier, label=f"Ряд Фурье (N={N_terms})", color="red", linestyle="--")
    plt.title("Разложение в ряд Фурье для f(x) = x")
    plt.xlabel("x")
    plt.ylabel("f(x)")
    plt.legend()
    plt.grid()
    plt.show()