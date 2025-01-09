<?php

include('api_config.php');

// Configurar encabezados
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With, Origin, Accept');
header('Content-Type: application/json; charset=utf-8');

// Leer el cuerpo de la solicitud
$post = json_decode(file_get_contents('php://input'), true);

// Verificar si hay accion definida
if (!isset($post['accion'])) {
    echo json_encode(['code' => 400, 'response' => 'No action provided', 'estado' => false]);
    exit;
}

// Variables globales
$respuesta = [];
$data = [];

// Acciones seg迆n el valor de 'accion'
switch ($post['accion']) {
    case 'todos':
        $sql = "SELECT * FROM carros";
        $query = mysqli_query($conn, $sql);

        if ($query && mysqli_num_rows($query) > 0) {
            while ($row = mysqli_fetch_assoc($query)) {
                $data[] = [
                    'cod_carro' => $row['cod_carro'],
                    'cedula_carro' => $row['cedula_carro'],
                    'nombre_carro' => $row['nombre_carro'],
                    'placa_carro' => $row['placa_carro'],
                    'hora_entrada' => $row['hora_entrada'],
                    'hora_salida' => $row['hora_salida'],
                    'valor_pago' => $row['valor_pago'],
                    'estado_carro' => $row['estado_carro']
                ];
            }
            $respuesta = ['code' => 200, 'response' => 'Data fetched successfully', 'estado' => true, 'data' => $data];
        } else {
            $respuesta = ['code' => 400, 'response' => 'No data found', 'estado' => false];
        }
        break;

    case 'unCarro':
        $sql = sprintf(
            "SELECT * FROM carros WHERE cod_carro='%s'",
            mysqli_real_escape_string($conn, $post['cod_carro'])
        );
        $query = mysqli_query($conn, $sql);

        if ($query && mysqli_num_rows($query) > 0) {
            while ($row = mysqli_fetch_assoc($query)) {
                $data[] = [
                    'cod_carro' => $row['cod_carro'],
                    'cedula_carro' => $row['cedula_carro'],
                    'nombre_carro' => $row['nombre_carro'],
                    'placa_carro' => $row['placa_carro'],
                    'hora_entrada' => $row['hora_entrada'],
                    'hora_salida' => $row['hora_salida'],
                    'valor_pago' => $row['valor_pago'],
                    'estado_carro' => $row['estado_carro']
                ];
            }
            $respuesta = ['code' => 200, 'response' => 'Data fetched successfully', 'estado' => true, 'data' => $data];
        } else {
            $respuesta = ['code' => 400, 'response' => 'No data found', 'estado' => false];
        }
        break;

    case 'generarPago':

        $fechaActual = date('Y-m-d H:i:s');
        $sql = sprintf(
            "SELECT * FROM carros WHERE cod_carro='%s'",
            mysqli_real_escape_string($conn, $post['cod_carro'])
        );
        $query = mysqli_query($conn, $sql);

        if ($query && mysqli_num_rows($query) > 0) {
            while ($row = mysqli_fetch_assoc($query)) {
                $horaEntrada = new DateTime($row['hora_entrada']);
                $horaSalida = new DateTime($fechaActual);

                $diferencia = $horaEntrada->diff($horaSalida);
                $horasTranscurridas = $diferencia->days * 24 + $diferencia->h;
                $valorPorHora = 0.75;
                $valorPago = $horasTranscurridas * $valorPorHora;

                $data[] = [
                    'cod_carro' => $row['cod_carro'],
                    'cedula_carro' => $row['cedula_carro'],
                    'nombre_carro' => $row['nombre_carro'],
                    'placa_carro' => $row['placa_carro'],
                    'hora_entrada' => $row['hora_entrada'],
                    'hora_salida' => $fechaActual,
                    'valor_pago' => number_format($valorPago, 2),
                    'horas_transcurridas' => $horasTranscurridas,
                    'estado_carro' => $row['estado_carro']
                ];
            }
            $respuesta = ['code' => 200, 'response' => 'Data fetched successfully', 'estado' => true, 'data' => $data];
        } else {
            $respuesta = ['code' => 400, 'response' => 'No data found', 'estado' => false];
        }
        break;

    case 'pagar':
        $sql = sprintf(
            "UPDATE carros
            SET valor_pago='%s', hora_salida=CURRENT_TIMESTAMP, estado_carro='%s'
            WHERE cod_carro='%s'",
            mysqli_real_escape_string($conn, $post['valor_pago']),
            mysqli_real_escape_string($conn, "Pagado"),
            mysqli_real_escape_string($conn, $post['cod_carro'])
        );
        $query = mysqli_query($conn, $sql);

        $respuesta = $query
            ? ['code' => 200, 'response' => 'Payment successfully completed', 'estado' => true]
            : ['code' => 400, 'response' => 'Failed to insert data', 'estado' => false];
        break;

    case 'insertar':
        $sql = sprintf(
            "INSERT INTO carros (cedula_carro, nombre_carro, placa_carro, hora_entrada, estado_carro) 
            VALUES ('%s', '%s', '%s', CURRENT_TIMESTAMP, '%s')",
            mysqli_real_escape_string($conn, $post['cedula_carro']),
            mysqli_real_escape_string($conn, $post['nombre_carro']),
            mysqli_real_escape_string($conn, $post['placa_carro']),
            mysqli_real_escape_string($conn, 'Pendiente')
        );
        $query = mysqli_query($conn, $sql);

        if (!$query) {
            $respuesta = ['code' => 400, 'response' => mysqli_error($conn), 'estado' => false];
        } else {
            $respuesta = $query
                ? ['code' => 200, 'response' => 'Data inserted successfully', 'estado' => true]
                : ['code' => 400, 'response' => 'Failed to insert data', 'estado' => false];
        }


        break;

    case 'salida':
        $sql = sprintf(
            "UPDATE carros 
            SET valor_pago='%s', estado_carro='%s', hora_salida=CURRENT_TIMESTAMP
            WHERE cod_carro='%s'",
            mysqli_real_escape_string($conn, $post['valor_pago']),
            mysqli_real_escape_string($conn, "Pagado"),
            mysqli_real_escape_string($conn, $post['cod_carro'])
        );
        $query = mysqli_query($conn, $sql);

        $respuesta = $query
            ? ['code' => 200, 'response' => 'Data updated successfully', 'estado' => true]
            : ['code' => 400, 'response' => 'Failed to update data', 'estado' => false];
        break;

    case 'eliminar':
        $sql = sprintf(
            "DELETE FROM carros WHERE cod_carro='%s'",
            mysqli_real_escape_string($conn, $post['cod_carro'])
        );
        $query = mysqli_query($conn, $sql);

        $respuesta = $query
            ? ['code' => 200, 'response' => 'Data deleted successfully', 'estado' => true]
            : ['code' => 400, 'response' => 'Failed to delete data', 'estado' => false];
        break;

    case 'verificar_cedula':
        $sql = sprintf(
            "SELECT * FROM carros WHERE cedula_carro ='%s' AND estado_carro='Pendiente'",
            mysqli_real_escape_string($conn, $post['cedula_carro'])
        );
        $query = mysqli_query($conn, $sql);

        if ($query && mysqli_num_rows($query) > 0) {
            $respuesta = ['code' => 200, 'response' => 'User utilizing a parking lot', 'estado' => true];
        } else {
            $respuesta = ['code' => 400, 'response' => 'No data found', 'estado' => false];
        }
        break;

    default:
        $respuesta = ['code' => 400, 'response' => 'Invalid action', 'estado' => false];
        break;
}

// Devolver la respuesta en formato JSON
echo json_encode($respuesta);
