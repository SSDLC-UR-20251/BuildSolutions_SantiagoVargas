const { leerArchivo, escribirArchivo, calcularSaldo, transferir } = require('../src/app');

beforeEach(() => {
    // Configurar datos iniciales antes de cada prueba
    const datosIniciales = {
        "juan.jose@urosario.edu.co": [
            { "balance": "100", "type": "Deposit", "timestamp": "2025-02-11 14:17:21.921536" }
        ],
        "sara.palaciosc@urosario.edu.co": []
    };
    escribirArchivo(datosIniciales);
});

test('Transferencia entre cuentas', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);

    expect(resultado.exito).toBe(true);
    expect(resultado.mensaje).toBe('Transferencia de 30 realizada correctamente de juan.jose@urosario.edu.co a sara.palaciosc@urosario.edu.co.');

    // Verificar saldo actualizado
    const saldoJuan = calcularSaldo('juan.jose@urosario.edu.co');
    const saldoSara = calcularSaldo('sara.palaciosc@urosario.edu.co');

    expect(saldoJuan).toBe(70);
    expect(saldoSara).toBe(30);
});

test('Transferencia con saldo insuficiente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 1000);

    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('Saldo insuficiente en la cuenta de juan.jose@urosario.edu.co.');

    // Verificar que los saldos no cambian
    const saldoJuan = calcularSaldo('juan.jose@urosario.edu.co');
    const saldoSara = calcularSaldo('sara.palaciosc@urosario.edu.co');

    expect(saldoJuan).toBe(100);
    expect(saldoSara).toBe(0);
});

test('Transferencia a cuenta inexistente', () => {
    const resultado = transferir('juan.jose@urosario.edu.co', 'andres.perez@urosario.edu.co', 30);

    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('La cuenta de destino (andres.perez@urosario.edu.co) no existe.');
});

test('Transferencia desde cuenta inexistente', () => {
    const resultado = transferir('pedro.gomez@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 30);

    expect(resultado.exito).toBe(false);
    expect(resultado.mensaje).toBe('La cuenta de origen (pedro.gomez@urosario.edu.co) no existe.');
});
