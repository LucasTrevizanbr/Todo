[![Build e Testes](https://github.com/LucasTrevizanbr/Todo/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/LucasTrevizanbr/Todo/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/LucasTrevizanbr/Todo/branch/main/graph/badge.svg?token=O4JL9DRXAM)](https://codecov.io/gh/LucasTrevizanbr/Todo)
# Todo - Ideia
A proposta da API é ser um centralizador de metas de um usuário, onde ele pode definir seus prazos de conclusão e cadastrar tarefas relacionadas a meta para uma dimensão maior do objetivo. As metas então concedem pontos após serem concluídas, baseado no nível de dificuldade, prazo de conclusão e etc.. :
<div align- "center">
<img src="https://user-images.githubusercontent.com/72326473/172437527-3900f345-dc45-487e-b4d1-5bb8645bdfb9.png" width="400px" />
</div>

# Todo - arquitetura
Eu tentei me guiar bastante por alguns conceitos de DDD para fazer a arquitetura do meu projeto, então separei tudo em 3 **vertentes/camadas** principais, a de *domínio*, *app* e *infra estrutura*. Outro conceito que segui foi o de "aggregate", por exemplo, minhas tarefas só podem ser acessadas pelo root que é a Meta (já que uma tarefa só existe se estiver relacionada a uma Meta).
<br>
<br>
Como uma boa prática de desenvolvimento e também para testar meus conhecimentos, fiz uma cobertura de testes bem forte no meu código, chegando a 85% (muito do que faltou para ser 100% são códigos indiferentes a teste, como getters e setters).
<div align- "center">
<img src="https://user-images.githubusercontent.com/72326473/172444090-9516fc48-f868-4064-88dd-1fa89090a8e4.png" width="400px" />
</div>

# Todo - Como rodar o projeto na máquina local - Docker
Não precisa configurar seu ambiente, basta rodar o projeto de maneira conteinerizada.
## você vai precisar:
- Ter o docker e o docker compose instalados na máquina

- Clone o projeto ou baixe o zip
- Abra um terminal no diretório principal(Todo) e digite o comando `docker-compose up` , container com a aplicação e o banco de dados serão criados, depois de construir e rodar os container o projeto será iniciado na porta 8080
- Basta acessar `http://localhost:8080/swagger-ui.html#/` para ter acesso a documentação.

# Todo - Como rodar o projeto na máquina local - Arquivo Jar
Se não quiser usar containers vai precisar fazer o package do projeto.
## você vai precisar:
- Ter o algum banco de dados instalado
- Ter as variáveis de ambiente apontando para o java 17 ou superior
- Ter o maven instalado
## Clone o projeto ou baixe o zip dele
- Abra um terminal no diretório raiz do projeto (Todo/todo) e digite `mvn clean package`, isso vai gerar um .jar do projeto
- <div align- "center"> <img src="https://user-images.githubusercontent.com/72326473/172445956-fa68215c-31a8-42ad-a787-14a2a4573333.png" width="200px" /></div>
- Se quiser rodar esse .jar você precisa passar os valores de diversas variáveis de ambiente, são elas : `DB_URL` , `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`,
`MAIL_USERNAME`, `MAIL_PASSWORD`. Você passa o valor de uma variável de ambiente na linha de comando da seguinte maneira:**"-Dxxxx=yyyy"** x=nome da variável, y=valor da variável
- Então para executar o projeto digite `java -jar Dxxx=yyy Dxxx=yyy`. A partir dai você pode acessar a url "localhost:8080/swagger-ui.html#/" para ver a documentação

# Se preferir pode consumir a API na nuvem
Se todos esses passos forem muito chatos você pode consumir minha API direto na nuvem acessando essa url: https://todogameficado-api.herokuapp.com/swagger-ui.html#/
