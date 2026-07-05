<?php
include "config.php";
$data = json_decode(file_get_contents("php://input"), true);
$email = trim($data['email'] ?? '');
$pass  = $data['password'] ?? '';

$stmt = $conn->prepare("SELECT id,name,password FROM users WHERE email=?");
$stmt->bind_param("s",$email); $stmt->execute();
$res = $stmt->get_result();
if($res->num_rows==0){ echo json_encode(["status"=>"error","message"=>"Email tidak ditemukan"]); exit;}
$u = $res->fetch_assoc();
if(!password_verify($pass, $u['password'])){ echo json_encode(["status"=>"error","message"=>"Password salah"]); exit;}
echo json_encode(["status"=>"success","message"=>"Login berhasil","user"=>["id"=>$u['id'],"name"=>$u['name'],"email"=>$email]]);
?>