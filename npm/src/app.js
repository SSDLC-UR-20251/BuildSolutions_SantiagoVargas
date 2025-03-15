const fs = require('fs');

// Función para leer el archivo transactions.txt
function leerArchivo() {
    try {
        const data = fs.readFileSync('src/transactions.txt', 'utf8'); 
        const jsonData = JSON.parse(data);
        return jsonData;
    } catch (error) {
        console.error("Error al leer el archivo:", error);
        return {};
    }
}

// Función para escribir el archivo transactions.txt
function escribirArchivo(data) {
    try {
        fs.writeFileSync('src/transactions.txt', JSON.stringify(data, null, 4), 'utf8');
    } catch (error) {
        console.error("Error al escribir el archivo:", error);
    }
}

// Función para calcular el saldo actual de un usuario
function calcularSaldo(usuario) {
    const data = leerArchivo();
    if (!data[usuario]) return 0;

    return data[usuario].reduce((saldo, transaccion) => saldo + parseFloat(transaccion.balance), 0);
}

// Función para realizar la transferencia entre cuentas
function transferir(de, para, monto) {
    const data = leerArchivo();

    // Validar existencia de cuentas
    if (!data[de]) return { exito: false, mensaje: `La cuenta de origen (${de}) no existe.` };
    if (!data[para]) return { exito: false, mensaje: `La cuenta de destino (${para}) no existe.` };

    // Validar saldo suficiente
    const saldoOrigen = calcularSaldo(de);
    if (saldoOrigen < monto) return { exito: false, mensaje: `Saldo insuficiente en la cuenta de ${de}.` };

    // Registrar transacción de retiro en la cuenta de origen
    data[de].push({
        balance: `-${monto}`,
        type: "Transfer Out",
        timestamp: new Date().toISOString()
    });

    // Registrar transacción de depósito en la cuenta de destino
    data[para].push({
        balance: `${monto}`,
        type: "Transfer In",
        timestamp: new Date().toISOString()
    });

    // Guardar cambios en el archivo
    escribirArchivo(data);

    return { exito: true, mensaje: `Transferencia de ${monto} realizada correctamente de ${de} a ${para}.` };
}

// Ejecutar prueba de transferencia
const resultado = transferir('juan.jose@urosario.edu.co', 'sara.palaciosc@urosario.edu.co', 50);
console.log(resultado.mensaje);

// Exportar funciones para pruebas
module.exports = { leerArchivo, escribirArchivo, calcularSaldo, transferir };
