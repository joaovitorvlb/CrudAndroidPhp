<?php
	include "conexao.php";
	$id = $_POST['id'];
	$nome = $_POST['nome'];
	$telefone = $_POST['telefone'];
	$email = $_POST['email'];

	$sql_update = "UPDATE contatos SET nome = '$nome', telefone = '$telefone', email = '$email'WHERE id = '$id'";
    	

	if ($conn->query($sql_update) === TRUE) {
         $json = '{"update":"ok"}';
    }else{
      $json = '{"update":"erro"}'. mysqli_error($conn);
    }
 echo $json;
?>
