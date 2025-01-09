<?php
//TODO Llamar al archivo de configuracion
include('api_config.php');

//TODO Configurar encabezados CORS
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Credentials: true');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type, Authorization, X-Requested-With, Origin, Accept');
header('Content-Type: application/json; charset=utf-8');

//TODO Leer la solicitud
$post = json_decode(file_get_contents('php://input'), true);

//TODO Verificar si hay accion definida
if (!isset($post['accion'])) {
    echo json_encode(['code' => 400, 'response' => 'No action provided', 'estado' => false]);
    exit;
}

//TODO Variables globales
$respuesta = [];
$data = [];

switch ($post['accion']) {
    case 'login':
        $hashedPassword = $post['password'];

        //TODO Verificar si el usuario existe
        $sql = sprintf(
            "SELECT * FROM usuario WHERE correo_usuario='%s'",
            mysqli_real_escape_string($conn, $post['email'])
        );
        $query = mysqli_query($conn, $sql);

        if ($query->num_rows > 0) {
            $row = $query->fetch_assoc();
            //TODO Verificar la contrasenia
            if($hashedPassword == $row['clave_usuario']){
            //if (password_verify($hashedPassword, $row['clave_usuario'])) {
                $data[] = [
                    'cod_usuario' => $row['cod_usuario'],
                    'nom_usuario' => $row['nom_usuario'],
                    'ape_usuario' => $row['ape_usuario'],
                    'ci_usuario' => $row['ci_usuario'],
                    'correo_usuario' => $row['correo_usuario'],
                    'clave_usuario' => $row['clave_usuario'],
                    'logged' => true
                ];

                $respuesta = ['code' => 200, 'response' => 'Login successful', 'estado' => true, 'data' => $data];
            } else {
                $respuesta = ['code' => 400, 'response' => 'Invalid credentials', 'estado' => false];
            }
        } else {
            $respuesta = ['code' => 400, 'response' => 'User not found', 'estado' => false];
        }
        break;

    default:
        $respuesta = ['code' => 400, 'response' => 'Invalid action', 'estado' => false];
        break;
}
echo json_encode($respuesta);
