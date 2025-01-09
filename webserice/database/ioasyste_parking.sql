/*
 Navicat Premium Data Transfer

 Source Server         : APP_WEB
 Source Server Type    : MySQL
 Source Server Version : 80040
 Source Host           : www.ioasystem.com:3306
 Source Schema         : ioasyste_parking

 Target Server Type    : MySQL
 Target Server Version : 80040
 File Encoding         : 65001

 Date: 08/01/2025 23:15:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for carros
-- ----------------------------
DROP TABLE IF EXISTS `carros`;
CREATE TABLE `carros`  (
  `cod_carro` int NOT NULL AUTO_INCREMENT,
  `cedula_carro` varchar(13) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `nombre_carro` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `placa_carro` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `hora_entrada` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `hora_salida` timestamp NULL DEFAULT NULL,
  `valor_pago` double NULL DEFAULT NULL,
  `numero_horas` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `estado_carro` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `usu_codigo` int NULL DEFAULT NULL,
  PRIMARY KEY (`cod_carro`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of carros
-- ----------------------------
INSERT INTO `carros` VALUES (1, '1715730022', 'otro demo', 'pbu3452', '2025-01-07 23:58:54', '2025-01-08 21:46:32', 19.5, NULL, 'Pagado', NULL);
INSERT INTO `carros` VALUES (2, '1715730022', 'otro demo', 'pbu3452', '2025-01-08 23:58:55', '2025-01-08 22:07:17', 2.25, NULL, 'Pagado', NULL);
INSERT INTO `carros` VALUES (3, '1715730022', 'ivan orlando', 'ppb3450', '2025-01-08 00:07:26', '2025-01-08 22:07:28', 19.5, NULL, 'Pagado', NULL);
INSERT INTO `carros` VALUES (4, '1717733239', 'alex moro', 'gso3121', '2025-01-08 22:07:53', '2025-01-08 22:08:24', 3.75, NULL, 'Pendiente', NULL);
INSERT INTO `carros` VALUES (5, '1715730025', 'ivan torres', 'pbh1569', '2025-01-08 22:42:08', NULL, NULL, NULL, 'Pendiente', NULL);
INSERT INTO `carros` VALUES (6, '1715730035', 'karla torres', 'ppl2153', '2025-01-08 23:06:12', '2025-01-08 23:07:48', 3.75, NULL, 'Pagado', NULL);
INSERT INTO `carros` VALUES (7, '1717733030', 'maty lopez', 'ppu1021', '0000-00-00 00:00:00', NULL, NULL, NULL, 'Pendiente', NULL);

-- ----------------------------
-- Table structure for usuario
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario`  (
  `cod_usuario` int NOT NULL AUTO_INCREMENT,
  `nom_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ape_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `ci_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `correo_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `clave_usuario` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `status_usuario` int NULL DEFAULT NULL,
  PRIMARY KEY (`cod_usuario`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of usuario
-- ----------------------------
INSERT INTO `usuario` VALUES (1, 'IVAN', 'ANCALLAY', '1715730071', 'ivan@gmail.com', '12345678', 0);
INSERT INTO `usuario` VALUES (2, 'OPERADOR', 'PARKING', '1715730071', 'operador@gmail.com', '12345678', 0);

SET FOREIGN_KEY_CHECKS = 1;
