<?php
include "config.php";
$data = json_decode(file_get_contents("php://input"), true);
$name = trim($data['name'] ?? '');
$email = trim($data['email'] ?? '');
$pass = $data['password'] ?? '';

if(!$name || !$email || !$pass){ echo json_encode(["status"=>"error","message"=>"Semua field wajib"]); exit;}
if(!filter_var($email, FILTER_VALIDATE_EMAIL)){ echo json_encode(["status"=>"error","message"=>"Email tidak valid"]); exit;}

$check = $conn->prepare("SELECT id FROM users WHERE email=?");
$check->bind_param("s",$email); $check->execute();
if($check->get_result()->num_rows>0){ echo json_encode(["status"=>"error","message"=>"Email sudah terdaftar"]); exit;}

$hash = password_hash($pass, PASSWORD_BCRYPT);
$stmt = $conn->prepare("INSERT INTO users(name,email,password) VALUES(?,?,?)");
$stmt->bind_param("sss",$name,$email,$hash);
echo json_encode($stmt->execute()
  ? ["status"=>"success","message"=>"Registrasi berhasil"]
  : ["status"=>"error","message"=>"Gagal daftar"]);
?>