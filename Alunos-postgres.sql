CREATE TABLE Aluno (
    ID_Aluno INT PRIMARY KEY,
    Nome VARCHAR(65),
    Idade INT,
    Telefone VARCHAR(20)
);

CREATE TABLE Livro (
    ID_Livro INT PRIMARY KEY,
    Titulo VARCHAR(100),
    Genero VARCHAR(50),
    Autor VARCHAR(65),
    ISBN VARCHAR(20)
);

CREATE TABLE Emprestimo (
    ID_Emprestimo INT PRIMARY KEY,
    ID_Aluno INT,
    ID_Livro INT,
    Preco DECIMAL(10,2),

    FOREIGN KEY (ID_Aluno) REFERENCES Aluno(ID_Aluno),
    FOREIGN KEY (ID_Livro) REFERENCES Livro(ID_Livro)
);

select * from aluno
select * from Emprestimo
select * from Livro
