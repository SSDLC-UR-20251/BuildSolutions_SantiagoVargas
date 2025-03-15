import sys
import random
import string

# TODO: Implementar la función para generar códigos de recuperación
def generar_codigo():
    """
    Debe generar un código de 6 caracteres alfanuméricos sin caracteres ambiguos.
    Caracteres ambiguos a evitar: '0', 'O', '1', 'l'
    """
     # Definir el conjunto de caracteres permitidos (excluyendo caracteres ambiguos)
    caracteres_permitidos = string.ascii_uppercase + string.ascii_lowercase + string.digits
    caracteres_ambiguos = ['0', 'O', '1', 'l']
    
    # Filtrar caracteres ambiguos
    caracteres_filtrados = [c for c in caracteres_permitidos if c not in caracteres_ambiguos]
    
    # Generar el código de 6 caracteres aleatorios
    codigo = ''.join(random.choices(caracteres_filtrados, k=6))
    
    return codigo

if __name__ == "__main__":
    if len(sys.argv) != 2 or not sys.argv[1].isdigit():
        print("Uso: python main.py <cantidad_de_codigos>")
        sys.exit(1)

    cantidad = int(sys.argv[1])
    print("Generando códigos de recuperación...")

    for i in range(cantidad):
        print(f"Código {i+1}: {generar_codigo()}")
