<?php
    include "conexao.php";

    $nome = $_POST['nome'];
    $telefone = $_POST['telefone'];
    $email = $_POST['email'];

    $sql="INSERT INTO contatos (nome, telefone, email)
    	VALUES ('$nome', '$telefone', '$email')";

    if ($conn->query($sql) === TRUE) {
        $dados = array('create', 'ok');
    } else {
        $dados = array('create', 'erro');
    }
    echo json_encode($dados);

    $conn->close(); 
?>

